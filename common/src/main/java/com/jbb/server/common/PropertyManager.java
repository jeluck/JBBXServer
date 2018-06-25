package com.jbb.server.common;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Function;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbb.server.common.util.StringUtil;

/**
 * A class which implements several methods to access values of properties stored in the system and property files. All
 * the property files must be located in %HOME_DIR%/config/properties/ or in resources Property files are checked for
 * changes periodically.
 * 
 * @author Vincent Tang
 */
public final class PropertyManager {
    private static final Logger logger = LoggerFactory.getLogger(PropertyManager.class);

    // a file sub directory inside the home directory, where property files are
    // located
    private static final String PROPERTY_SUB_DIR = "properties";

    // property files extension
    private static final String PROPERTY_FILE_SUFFIX = ".properties";

    // property files stored in resources
    private static final String[] RESOURSE_PROPERTY_FILES = {"test.properties"};

    // The property container
    private static final Properties props = new Properties(); // "Properties" is
                                                              // synchronized
    // in memory properties overwrites file properties
    private static final Properties inMemoryProps = new Properties();
    // the property directory
    private static File propertyDir = null;
    private static String propertyDirPath = null;

    // property file time stamps to check if they were changed before reloading
    private static final Hashtable<String, Long> propertyFileTimestamp = new Hashtable<>();

    private static boolean isConfigured = false;
    private static long lastRefreshedTime;

    private static final long DEFAULT_REFRESH_PERIOD = 600000L; // 10 min
    private static final String REFRESH_PROPERTY = "jbb.settings.refresh";
    private static long refreshPeriod = DEFAULT_REFRESH_PERIOD;

    private static final char VALUE_DELIMITER = ',';

    private static StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();

    private static final String ENCRYPTION_PREFIX = "ENC(";
    private static final int ENCRYPTION_PREFIX_LEN = ENCRYPTION_PREFIX.length();

    // Lock object
    private static final ReentrantLock refreshLock = new ReentrantLock();

    private static final ConcurrentHashMap<String, String[]> parsedProperties = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, int[]> parsedIntProperties = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, String[]> parsedNonEmptyProperties = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Object> parsedObjectProperties = new ConcurrentHashMap<>();

    private static Thread watchThread;
    private static volatile boolean watching;
    private static final ConcurrentLinkedQueue<Consumer<String[]>> watchConsumers = new ConcurrentLinkedQueue<>();
    private static Timer timer;
    private static final TimerTask task = new TimerTask() {
        @Override
        public void run() {
            refresh();
        }
    };

    /* Initialize the property system. */
    static {
        initSync();
    }

    /** Do not allow instantiation of this class. */
    private PropertyManager() {}

    /**
     * Thread safe initialization method
     */
    private static synchronized void initSync() {
        if (!isConfigured)
            init();
    }

    /** Initialize the property system. */
    private static void init() {
        logger.info("Initialize the property system.");

        setPropertyDir();

        logger.info("Module " + Home.getModuleName() + " version: " + Home.getModuleVersion());
        logger.info("Common version: " + Home.getCommonVersion());
        logger.info("Core version: " + Home.getCoreVersion());

        props.putAll(System.getProperties());

        try {
            loadProperties();
        } catch (Exception e) {
            logger.error("Exception during PropertyManager initialisation", e);
        }

        encryptor.setAlgorithm(Home.ENCRYPTION_ALGORITHM);
        String password = Home.getEncryptionPassword();
        if (!StringUtil.isEmpty(password))
            encryptor.setPassword(password);

        isConfigured = true;

        startPropertyFilesWatcher();

        logger.info("The property system initialized.");
    }

    static void shutdown() {
        watching = false;
        watchThread.interrupt();

        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /**
     * Register a consumer, which will be called, if a property file has been modified. The accept() argument is an
     * array of modified file names. It can contain null elements at the end.
     */
    public static void registerWatcher(Consumer<String[]> consumer) {
        watchConsumers.add(consumer);
    }

    /**
     * Get a property.
     * 
     * @param name property name.
     * @return property value or null.
     */
    public static String getProperty(String name) {
        return props.getProperty(name);
    }

    /**
     * Set a property.
     * 
     * @param name Property name.
     * @param value Property value.
     */
    public static String setProperty(String name, String value) {
        // this property value will restored after refreshing it from a file
        inMemoryProps.put(name, value);
        return (String)props.setProperty(name, value);
    }

    /**
     * Get a property.
     * 
     * @param name Property name.
     * @param defaultValue Default property value.
     * @return Property value or default.
     */
    public static String getProperty(String name, String defaultValue) {
        String value = getProperty(name);
        return value != null ? value : defaultValue;
    }

    /**
     * Get split values of the property around comma
     * 
     * @param name property name
     * @return An array of property values
     */
    public static String[] getProperties(String name) {
        final String propValue = getProperty(name);
        if ((propValue == null) || (propValue.length() == 0)) {
            return null;
        }

        return parsedProperties.computeIfAbsent(propValue, new Function<String, String[]>() {
            @Override
            public String[] apply(String k) {
                return StringUtil.split(propValue, VALUE_DELIMITER, StringUtil.UNLIMITED_SPLIT);
            }
        });
    }

    /**
     * Get split non-empty values of the property around comma
     * 
     * @param name property name
     * @return An array of non empty property values only
     */
    public static String[] getNonEmptyProperties(String name) {
        String propValue = getProperty(name);
        if ((propValue == null) || (propValue.length() == 0))
            return null;

        String[] res = parsedNonEmptyProperties.get(propValue);

        if (res == null) {
            res = parseNonEmptyStrings(propValue);
            if (res != null)
                parsedNonEmptyProperties.put(propValue, res);
        }

        return res;
    }

    private static String[] parseNonEmptyStrings(String propValue) {
        String[] valueStrArr = StringUtil.split(propValue, VALUE_DELIMITER, StringUtil.UNLIMITED_SPLIT);
        if (valueStrArr.length == 0)
            return null;

        ArrayList<String> valueStrList = new ArrayList<>(valueStrArr.length);
        for (String value : valueStrArr) {
            if (!StringUtil.isEmpty(value))
                valueStrList.add(value);
        }

        return valueStrList.toArray(new String[valueStrList.size()]);
    }

    /**
     * Check if the non-empty property contains this value
     * 
     * @param name property name
     * @param value property value to check
     * @return true, if the non-empty property values array contains the checked value
     */
    public static boolean contains(String name, String value) {
        String[] values = getNonEmptyProperties(name);
        if (values == null)
            return false;

        for (String v : values) {
            if (v.equals(value))
                return true;
        }

        return false;
    }

    /**
     * Get an integer property.
     * 
     * @param name Property name.
     * @param defaultValue Default property value.
     * @return Property value or default.
     */
    public static int getIntProperty(String name, int defaultValue) {
        String value = getProperty(name);

        if ((value != null) && (value.length() > 0)) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                logger.error("Exception in parsing of the int property value: " + name + "='" + value + "'");
            }
        }

        return defaultValue;
    }

    /**
     * Get integer property values from a comma separated list
     * 
     * @param name property list name
     * @param defaultValue default value used in case value is empty or cannot be parsed
     * @return An array of property values
     */
    public static int[] getIntProperties(String name, final int defaultValue) {
        final String valueStr = getProperty(name);
        if ((valueStr == null) || (valueStr.length() == 0))
            return null;

        return parsedIntProperties.computeIfAbsent(valueStr, new Function<String, int[]>() {
            @Override
            public int[] apply(String k) {
                return StringUtil.parseIntArray(valueStr, VALUE_DELIMITER, 10, defaultValue);
            }
        });
    }

    /**
     * Get a long property.
     * 
     * @param name Property name.
     * @param defaultValue Default property value.
     * @return Property value or default.
     */
    public static long getLongProperty(String name, long defaultValue) {
        String value = getProperty(name);

        if ((value != null) && (value.length() > 0)) {
            try {
                return Long.parseLong(value);
            } catch (NumberFormatException e) {
                logger.error("Exception in parsing of the long property value: " + name + "='" + value + "'");
            }
        }

        return defaultValue;
    }

    /**
     * Get a float property.
     * 
     * @param name Property name.
     * @param defaultValue Default property value.
     * @return Property value or default.
     */
    public static float getFloatProperty(String name, float defaultValue) {
        String value = getProperty(name);

        if ((value != null) && (value.length() > 0)) {
            try {
                return Float.parseFloat(value);
            } catch (NumberFormatException e) {
                logger.error("Exception in parsing of the float property value: " + name + "='" + value + "'");
            }
        }

        return defaultValue;
    }

    /**
     * Get a boolean property.
     * 
     * @param name Property name.
     * @param defaultValue Default property value.
     * @return Property value or default.
     */
    public static boolean getBooleanProperty(String name, boolean defaultValue) {
        String value = getProperty(name);
        return value == null ? defaultValue : "true".equals(value);
    }

    /**
     * Get and decrypt property value
     * 
     * @param name property name
     * @return Decrypted value or value itself
     */
    public static String decryptProperty(String name) {
        String value = getProperty(name);
        if ((value != null) && value.startsWith(ENCRYPTION_PREFIX)) {
            value = value.substring(ENCRYPTION_PREFIX_LEN, value.length() - 1);
            value = encryptor.decrypt(value);
        }
        return value;
    }

    static String encryptProperty(String value) {
        return ENCRYPTION_PREFIX + encryptor.encrypt(value) + ")";
    }

    private static void setRefreshPeriod() {
        String valueStr = props.getProperty(REFRESH_PROPERTY);
        long value = DEFAULT_REFRESH_PERIOD;

        if (valueStr != null) {
            try {
                value = Long.parseLong(valueStr);
            } catch (NumberFormatException e) {
                logger.error("Exception in parsing of the long property value: settings.refresh=" + valueStr);
            }
        }

        if ((refreshPeriod != value) || ((timer == null) && (value > 0))) {
            refreshPeriod = value;

            if (timer != null) {
                timer.cancel();
                timer = null;
            }

            if (refreshPeriod > 0) {
                timer = new Timer();
                timer.scheduleAtFixedRate(task, refreshPeriod, refreshPeriod);
            }
        }
    }

    /**
     * Set property directory
     */
    private static void setPropertyDir() {
        String homeDir = Home.getHomeDir();
        if (homeDir == null) {
            logger.error("Empty home dir");
            System.out.println("Empty home dir");
            return;
        }

        String propConf = Home.getConfigDir() + PROPERTY_SUB_DIR;

        try {
            File dir = new File(propConf);

            if (dir.exists() && dir.isDirectory()) {
                propertyDir = dir;
                propertyDirPath = propConf + File.separator;
                logger.info("found property directory: " + propertyDirPath);
            } else {
                logger.error("Cannot find property directory: " + propConf);
                System.out.println("Cannot find property directory: " + propConf);
            }
        } catch (Exception e) {
            logger.error("Exception in configuring property directory", e);
        }
    }

    // /////////////////////////////////////////////////////////////////////////
    // Property Loading //
    // /////////////////////////////////////////////////////////////////////////
    /**
     * Load properties and restore manual settings
     */
    private static void loadProperties() {
        if (loadFromFile()) {
            if (!inMemoryProps.isEmpty())
                props.putAll(inMemoryProps);
            clearParsedProperties();
            setRefreshPeriod();
        }
        lastRefreshedTime = System.currentTimeMillis();
    }

    /**
     * Load system properties from files located in the properties dir
     */
    private static boolean loadFromFile() {
        logger.debug(">loadFromFile() property directory: " + propertyDir);
        boolean loaded = false;

        try {
            for (String fileName : RESOURSE_PROPERTY_FILES) {
                Enumeration<URL> urls = PropertyManager.class.getClassLoader().getResources(fileName);
                if (urls != null) {
                    while (urls.hasMoreElements()) {
                        File file = new File(urls.nextElement().toURI());
                        if (loadFromFile(file))
                            loaded = true;
                    }
                }
            }

            // load properties from the properties dir
            if (propertyDir != null) {
                File[] files = propertyDir.listFiles();

                if ((files == null) || (files.length == 0)) {
                    logger.warn("Empty properties directory " + propertyDir);
                } else {
                    for (File file : files) {
                        if (loadFromFile(file))
                            loaded = true;
                    }
                }
            } else {
                logger.error("Empty properties directory setting value");
            }
        } catch (Exception ie) {
            logger.error("Exception in reading property files: ", ie);
        }

        if (logger.isDebugEnabled())
            logger.debug("<loadFromFile() loaded=" + loaded);
        return loaded;
    }

    /**
     * Load properties from a file
     * 
     * @param file property file
     * @return true, if loaded
     */
    private static boolean loadFromFile(File file) throws IOException {
        if (!file.isFile() || !file.canRead()) {
            logger.warn("Cannot read file " + file.getPath());
            return false;
        }

        boolean loaded = false;
        String filePath = file.getCanonicalPath();

        // if this is a property file
        if (filePath.endsWith(PROPERTY_FILE_SUFFIX)) {
            boolean loadedBefore = propertyFileTimestamp.containsKey(filePath);

            if (!loadedBefore || (file.lastModified() != propertyFileTimestamp.get(filePath))) {
                if (logger.isInfoEnabled()) {
                    logger
                        .info((loadedBefore ? "Property file modified: " : "Property file first loading: ") + filePath);
                }

                try (InputStream is = new BufferedInputStream(new FileInputStream(file))) {
                    props.load(is);
                    loaded = true;
                }

                propertyFileTimestamp.put(filePath, file.lastModified());
            }
        }

        return loaded;
    }

    /**
     * Force to refresh all properties immediately. Thread safe. If another thread is refreshing the properties at the
     * same time, the current thread becomes disabled until properties will be updated.
     */
    private static void forceRefreshAll() {
        try {
            if (refreshLock.tryLock())
                loadProperties(); // update properties in the current thread
            else
                refreshLock.lockInterruptibly(); // wait until properties will
                                                 // be updated by another
                                                 // thread
        } catch (Exception e) {
            logger.error("Exception in refreshing properties", e);
        } finally {
            refreshLock.unlock();
        }
    }

    private static boolean forceRefreshFile(File file) {
        boolean res = false;

        try {
            refreshLock.lockInterruptibly();
            res = loadFromFile(file); // update properties in the current thread

            if (res) {
                if (!inMemoryProps.isEmpty())
                    props.putAll(inMemoryProps);
                clearParsedProperties();
                setRefreshPeriod();
            }
        } catch (Exception e) {
            logger.error("Exception in refreshing properties from " + file, e);
        } finally {
            refreshLock.unlock();
        }

        return res;
    }

    /**
     * Refresh all properties if timeout ended. Thread safe.
     */
    private static void refresh() {
        if (System.currentTimeMillis() - lastRefreshedTime > refreshPeriod) {
            if (!isConfigured) {
                initSync();
            } else {
                boolean locked = false;
                try {
                    locked = refreshLock.tryLock();
                    if (locked && (System.currentTimeMillis() - lastRefreshedTime > refreshPeriod)) {
                        loadProperties();
                    }
                } catch (Exception e) {
                    logger.error("Exception in refreshing properties", e);
                } finally {
                    if (locked)
                        refreshLock.unlock();
                }
            }
        }
    }

    private static WatchService createPropertyFilesWatchService() throws IOException {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        Paths.get(propertyDirPath).register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
        return watchService;
    }

    private static void startPropertyFilesWatcher() {
        watching = true;

        watchThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try (WatchService watchService = createPropertyFilesWatchService()) {
                    while (watching) {
                        try {
                            WatchKey key = watchService.take();

                            Thread.sleep(1000L); // prevent receiving duplicate
                                                 // event

                            List<WatchEvent<?>> events = key.pollEvents();
                            if ((events == null) || events.isEmpty()) {
                                key.reset();
                                continue;
                            }

                            final String[] fileNames = new String[events.size()];
                            boolean updated = false;
                            int i = 0;

                            for (WatchEvent<?> e : events) {
                                WatchEvent.Kind<?> kind = e.kind();
                                Object context = e.context();

                                if (context instanceof Path) {
                                    Path contextPath = (Path)context;
                                    File file = contextPath.toFile();
                                    String fileName = file.getName();

                                    if (!fileName.endsWith(PROPERTY_FILE_SUFFIX))
                                        continue;

                                    if (logger.isInfoEnabled()) {
                                        logger.info("Property file event: " + kind.name() + ", path="
                                            + file.getCanonicalPath() + ", absolute=" + file.isAbsolute());
                                    }

                                    if (!file.isAbsolute()) {
                                        file = new File(propertyDirPath + fileName);
                                    }

                                    if (forceRefreshFile(file)) {
                                        updated = true;
                                        fileNames[i++] = fileName;
                                    }
                                } else {
                                    logger.warn("Other event: kind=" + kind.name() + ", context=" + context);
                                }
                            }

                            key.reset();

                            if (updated)
                                watchConsumers.forEach(new Consumer<Consumer<String[]>>() {
                                    @Override
                                    public void accept(Consumer<String[]> c) {
                                        c.accept(fileNames);
                                    }
                                });
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                } catch (IOException ignore) {
                }
            }
        });

        watchThread.start();
    }

    private static void clearParsedProperties() {
        parsedProperties.clear();
        parsedIntProperties.clear();
        parsedNonEmptyProperties.clear();
        parsedObjectProperties.clear();
    }
}
