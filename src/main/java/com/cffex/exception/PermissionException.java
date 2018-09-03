package com.cffex.exception;

public class PermissionException extends ServiceException {
    private static final long serialVersionUID = 1L;


    public PermissionException() {
        super(401, "unauthorized");
    }

    public PermissionException(String message, Exception ex) {
        super(401, message, ex);
    }
}
