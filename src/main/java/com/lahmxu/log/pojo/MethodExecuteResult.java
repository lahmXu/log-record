package com.lahmxu.log.pojo;

public class MethodExecuteResult {

    public boolean result;

    public Throwable throwable;

    private String errorMsg;

    public MethodExecuteResult(boolean result, Throwable throwable, String errorMsg) {
        this.result = result;
        this.throwable = throwable;
        this.errorMsg = errorMsg;
    }

    public boolean isSuccess(){
        return result;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
