package com.jbb.mgt.server.rs.exception;

import java.io.IOException;

public class IOExceptionWrapper extends RuntimeException {
	private static final long serialVersionUID = 2286839711396123213L;

    public IOExceptionWrapper(IOException cause) {
        super(cause);
    }
}
