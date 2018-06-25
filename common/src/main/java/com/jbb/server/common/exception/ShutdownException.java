package com.jbb.server.common.exception;

/**
 * Common Shutdown Exception class
 */
public class ShutdownException extends RuntimeException {
	private static final long serialVersionUID = -2216023691688930271L;

	/**
     * A constructor from a string error message
     * @param message error message
     */
    public ShutdownException(String message) {
        super(message);
    }
}
