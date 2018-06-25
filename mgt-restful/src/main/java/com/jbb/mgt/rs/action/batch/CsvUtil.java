package com.jbb.mgt.rs.action.batch;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import com.jbb.mgt.core.domain.TeleMarketingDetail;

/**
 * csv文件操作
 * 
 * @author zmy
 * @date 2018/05/05
 */
public class CsvUtil {

    /**
     * 生成为CVS文件
     * 
     * @param exportData 源数据List
     * @param fileds
     * @param map csv文件的列表头map
     * @param outPutPath 文件路径
     * @param fileName 文件名称
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static File createCSVFile(List exportData, String[] fileds, LinkedHashMap map, String outPutPath,
        String fileName) {
        File csvFile = null;
        BufferedWriter csvFileOutputStream = null;
        try {
            File file = new File(outPutPath);
            if (!file.exists()) {
                file.mkdir();
            }
            // 定义文件名格式并创建
            csvFile = File.createTempFile(fileName, ".csv", new File(outPutPath));
            // UTF-8使正确读取分隔符","
            csvFileOutputStream
                = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "GB2312"), 1024);
            // 写入文件头部
            for (Iterator propertyIterator = map.entrySet().iterator(); propertyIterator.hasNext();) {
                java.util.Map.Entry propertyEntry = (java.util.Map.Entry)propertyIterator.next();
                csvFileOutputStream.write((String)propertyEntry.getValue() != null
                    ? new String(((String)propertyEntry.getValue()).getBytes("GB2312"), "GB2312") : "");
                if (propertyIterator.hasNext()) {
                    csvFileOutputStream.write(",");
                }
            }
            csvFileOutputStream.write("\r\n");
            // 写入文件内容,
            // ============
            // 填充实体类的基本信息==================
            for (int j = 0; exportData != null && !exportData.isEmpty() && j < exportData.size(); j++) {
                TeleMarketingDetail t = (TeleMarketingDetail)exportData.get(j);
                Class clazz = t.getClass();
                String[] contents = new String[fileds.length];
                for (int i = 0; fileds != null && i < fileds.length; i++) {
                    String filedName = toUpperCaseFirstOne(fileds[i]);
                    Method method = clazz.getMethod(filedName);
                    method.setAccessible(true);
                    Object obj = method.invoke(t);
                    String str = String.valueOf(obj);
                    if (str == null || str.equals("null"))
                        str = "";
                    contents[i] = str;

                }

                for (int n = 0; n < contents.length; n++) {
                    // 将生成的单元格添加到工作表中
                    csvFileOutputStream.write(contents[n]);
                    csvFileOutputStream.write(",");

                }
                csvFileOutputStream.write("\r\n");
            }

            csvFileOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                csvFileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return csvFile;
    }

    /**
     * 下载文件
     * 
     * @param response
     * @param csvFilePath 文件路径
     * @param fileName 文件名称
     * @throws IOException
     */
    public static void exportFile(HttpServletResponse response, String csvFilePath, String fileName)
        throws IOException {
        response.setContentType("application/csv;charset=GB2312");
        response.setHeader("Content-Disposition",
            "attachment;  filename=" + new String(fileName.getBytes("GB2312"), "ISO8859-1"));
        InputStream in = null;
        try {
            in = new FileInputStream(csvFilePath);
            int len = 0;
            byte[] buffer = new byte[1024];
            response.setCharacterEncoding("GB2312");
            OutputStream out = response.getOutputStream();
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * 删除该目录filePath下的所有文件
     * 
     * @param filePath 文件目录路径
     */
    public static void deleteFiles(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    files[i].delete();
                }
            }
        }
    }

    /**
     * 删除单个文件
     * 
     * @param filePath 文件目录路径
     * @param fileName 文件名称
     */
    public static void deleteFile(String filePath, String fileName) {
        File file = new File(filePath);
        if (file.exists()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    if (files[i].getName().equals(fileName)) {
                        files[i].delete();
                        return;
                    }
                }
            }
        }
    }

    private static String toUpperCaseFirstOne(String origin) {
        StringBuffer sb = new StringBuffer(origin);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        sb.insert(0, "get");
        return sb.toString();
    }

}