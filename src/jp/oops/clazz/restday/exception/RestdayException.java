package jp.oops.clazz.restday.exception;
/**
 * 
 * <div>Copyright (c) 2019-2020 Masahito Hemmi</div> 
 * <div>This software is released under the MIT License.</div>
 */
public class RestdayException extends Exception {
    
    public RestdayException(String msg) {
        super(msg);
    }
    
    public RestdayException(String msg, Throwable th) {
        super(msg, th);
    }

    String messageForDevelopers = "";
    
    public String getMessageForDevelopers() {
        return messageForDevelopers;
    }
    
    public RestdayException setMessageForDevelopers(String msg) {
        messageForDevelopers = msg;
        return this;
    }
}
