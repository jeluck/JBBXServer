package com.jbb.server.common.exception;

/**
 * ExecutionException
 * @author VincentTang
 * @date 2017年12月20日
 */
public class ExecutionException extends RuntimeException {
	private static final long serialVersionUID = -2216023691688928270L;

	/**
     * A constructor from a string error message
     * @param message error message
     */
    public ExecutionException(String message) {
        super(message);
    }
    
    /**
     * A constructor from a cause
     * @param e exception cause
     */
    public ExecutionException(Throwable e) {
        super(e);
    }

    /**
     * A constructor from a string error message and a cause
     * @param message error message
     * @param e exception cause
     */
    public ExecutionException(String message, Throwable e) {
        super(message, e);
    }
    
    /**
     * Returns the detail message string.
     * @return Exception error message 
     */
    @Override
	public String getMessage() {
        Throwable cause = getCause();
        return super.getMessage() + (cause != null ? "; cause: " + cause.getMessage() : "");
    }
}
