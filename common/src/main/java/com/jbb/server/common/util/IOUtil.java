package com.jbb.server.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * IOUtil
 * @author VincentTang
 * @date 2017年12月20日
 */
public class IOUtil {
	/**
	 * Read input stream to byte array exactly as it is
	 * @param is input stream
	 * @return read data
	 * @throws IOException
	 */
    public static byte[] readStream(InputStream is)
    throws IOException {
        return readStreamLimited(is, 0);
    }
    
    public static byte[] readStreamLimited(InputStream is, int limit)
    throws IOException {
        ArrayList<byte[]> buf = new ArrayList<byte[]>();
        ArrayList<Integer> bufLens = new ArrayList<Integer>();
        int totalRead = 0;
        int byteRead = 0;
        int BUFFER_SIZE = 10240;
        
        for (;;) {
            int bufLen = is.available();
            if (bufLen <= 0) bufLen = BUFFER_SIZE;
            byte[] buffer = new byte[bufLen];
            
            byteRead = is.read(buffer);
            if (byteRead <= 0) break;
            
            buf.add(buffer);
            bufLens.add(byteRead);
            totalRead += byteRead;
            
            if ((limit > 0) && (totalRead > limit)) {
                throw new IOException("Input size " + totalRead + " exceeds " + limit);
            }
        }
        
        byte[] res = null;
        int bufSize = buf.size();
        
        if (bufSize == 1) {
            res = buf.get(0);
            if (res.length > totalRead) {
            	res = Arrays.copyOf(res, totalRead);
            }
        } else {
            res = new byte[totalRead];
            int pos = 0;
            for (int i = 0; i < bufSize; i++) {
                byte[] buffer = buf.get(i);
                int len = bufLens.get(i);
                System.arraycopy(buffer, 0, res, pos, len);
                pos += len;
            }
        }
        
        return res;
    }
    
	/**
	 * Read input stream to byte array with removing last leading zeros
	 * @param is input stream
	 * @return read data
	 * @throws IOException
	 */
    public static byte[] readStreamClean(InputStream is)
    throws IOException {
    	return clearLeadingZeros(readStream(is));
    }
    
    /**
     * Read input stream to byte array of specified length
     * @param is input stream
     * @param contentLength input length
     * @return read data
     * @throws IOException
     */
    public static byte[] readStream(InputStream is, int contentLength)
    throws IOException {
        int totalRead = 0;
        int byteRead = 0;
        int BUFFER_SIZE = 1024;
        byte[] buffer = new byte[contentLength];
        int bytesLeft = contentLength;
        
        for (; bytesLeft > 0;) {
            int readLen = is.available();
            if (readLen <= 0) readLen = BUFFER_SIZE;
            if (readLen > bytesLeft) readLen = bytesLeft;

            byteRead = is.read(buffer, totalRead, readLen);
            if (byteRead <= 0) break;
            
            totalRead += byteRead;
            bytesLeft -= byteRead;
        }

        if (buffer.length > totalRead) {
        	buffer = Arrays.copyOf(buffer, totalRead);
        }
        
        return buffer;
    }
    
    /**
     * Remove leading zeros
     * @param data
     * @return same or new data byte array
     */
    private static byte[] clearLeadingZeros(byte[] data) {
        int size = data.length - 1;
        for (; (size >= 0) && (data[size] == 0); size--);
        size++;
        
        if (data.length > size) {
        	data = Arrays.copyOf(data, size);
        }
        
        return data;
    }
}
