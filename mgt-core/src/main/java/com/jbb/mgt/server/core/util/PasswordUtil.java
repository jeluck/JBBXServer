package com.jbb.mgt.server.core.util;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbb.server.common.Constants;
import com.jbb.server.common.PropertyManager;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class PasswordUtil {
    private static Logger logger = LoggerFactory.getLogger(PasswordUtil.class);

    private static final Pattern USER_PASSWORD_PATTERN =
            Pattern.compile(PropertyManager.getProperty("jbb.password.format", "^.{8,}$"));

    private static final int ARGON2_ITERATIONS = 2048;
    private static final int ARGON2_MEMORY = 1 << 9;
    private static final int ARGON2_PARALLELISM = 1;

    private static ConcurrentLinkedQueue<Argon2> argon2Pool = new ConcurrentLinkedQueue<>();

    public static String passwordHash(String password) {
        if (password == null) return null;

        Argon2 argon2 = getArgon2();
        try {
            return argon2.hash(ARGON2_ITERATIONS, ARGON2_MEMORY, ARGON2_PARALLELISM, password, Constants.UTF8_CHARSET);
        } catch (UnsatisfiedLinkError|NoClassDefFoundError e) {
            logger.warn(e.toString());
            return HashUtil.getHashString(password, HashUtil.DIGEST_ALGORITHM_SHA1);
        } finally {
            returnArgon2(argon2);
        }
    }

    public static boolean verifyPassword(String password, String hash) {
        if ((hash == null) || (password == null)) return false;

        Argon2 argon2 = getArgon2();
        try {
            return argon2.verify(hash, password, Constants.UTF8_CHARSET);
        } catch (UnsatisfiedLinkError|NoClassDefFoundError e) {
            logger.warn(e.toString());
            return verifyPasswordSHA1(password, hash);
        } finally {
            returnArgon2(argon2);
        }
    }
    
    public static boolean verifyPasswordOld(String password, String md5) {
        if ((md5 == null) || (password == null)) return false;
        return md5.equals(MD5.MD5Encode(password));
    }

    private static Argon2 getArgon2() {
        Argon2 argon2 = argon2Pool.poll();
        return argon2 != null ? argon2 : Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2d);
    }

    private static void returnArgon2(Argon2 argon2) {
        argon2Pool.add(argon2);
    }

    public static boolean verifyPasswordSHA1(String password, String hash) {
        String hashedPassword = HashUtil.getHashString(password, HashUtil.DIGEST_ALGORITHM_SHA1);
        return hashedPassword.equals(hash);
    }

    public static boolean isValidUserPassword(String password) {
        return password != null && USER_PASSWORD_PATTERN.matcher(password).matches();
    }
}
