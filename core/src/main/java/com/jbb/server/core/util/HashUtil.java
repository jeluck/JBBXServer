package com.jbb.server.core.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.jbb.server.common.Constants;
import com.jbb.server.common.exception.ExecutionException;
import com.jbb.server.common.util.CodecUtil;

/**
 * A class to generate a hash value using SHA-1 or MD5 algorithms
 */
public final class HashUtil {
    /** Digest algorithm SHA-1 */
    public static final String DIGEST_ALGORITHM_SHA1 = "SHA-1";
    static final String DIGEST_ALGORITHM_SHA256 = "SHA-256";
    static final String DIGEST_ALGORITHM_SHA512 = "SHA-512";

    /**
     * Generate a hash value string using SHA-256 algorithm
     * 
     * @param inputStr an input value
     * @return generated hash value in HEX format
     *
     *         As the result will be in HEX format, the returned string size will be in two times longer than requested
     *         hash size.
     */
    public static String getHashString(String inputStr) {
        return getHashString(inputStr, DIGEST_ALGORITHM_SHA256);
    }

    /**
     * Generate a hash value string using SHA-256 algorithm
     * 
     * @param inputStr an input value
     * @param digestAlgorithm digest algorithm (SHA-256, SHA-512, SHA-1)
     * @return generated hash value in HEX format
     *
     *         As the result will be in HEX format, the returned string size will be in two times longer than requested
     *         hash size.
     */
    public static String getHashString(String inputStr, String digestAlgorithm) {
        byte[] hash;
        try {
            byte[] input = inputStr.getBytes(Constants.UTF8);
            hash = getHashValue(input, input.length, digestAlgorithm);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            throw new ExecutionException(e);
        }

        return new String(CodecUtil.toHex(hash));
    }

    /**
     * Generate a hash value (without Salt)
     * 
     * @param input an input value
     * @param maxInputLength maximum amount of bytes of input to be used to generate the hash value
     * @param digestAlgorithm digest algorithm (MD5, SHA-1, SHA-512)
     * @return generated hash value
     */
    static byte[] getHashValue(byte[] input, int maxInputLength, String digestAlgorithm)
        throws NoSuchAlgorithmException {
        int inputLength = input.length <= maxInputLength ? input.length : maxInputLength;

        MessageDigest digest = MessageDigest.getInstance(digestAlgorithm);
        digest.update(input, 0, inputLength);
        return digest.digest();
    }
}
