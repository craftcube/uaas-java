package com.cffex.exception;

import com.cffex.entity.ResponseResult;

public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = -2521417884229743055L;

    //error code, greater than 0
    private int errorCode = 500;
    private String errorMsg;

    public ServiceException() {
        super();
    }

    public ServiceException(int errorCode, String errorMsg, Exception ex) {
        super(ex);
        if (errorCode <= 0) {
            throw new RuntimeException("error code cannot be less than zero");
        }
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public ServiceException(String errorMsg, Exception ex) {
        super(ex);
        this.errorMsg = errorMsg;
    }

    public ServiceException(String errorMsg) {
        super();
        this.errorMsg = errorMsg;
    }

    public ServiceException(int errorCode, String errorMsg) {
        super();
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public ResponseResult getResponseResult() {
        ResponseResult result = new ResponseResult();
        result.setErrorCode(this.getErrorCode());
        result.setErrorMsg(this.getErrorMsg());
        return result;
    }

}
