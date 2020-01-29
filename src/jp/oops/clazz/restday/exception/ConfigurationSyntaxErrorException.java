package jp.oops.clazz.restday.exception;

import com.eclipsesource.json.JsonValue;

/**
 * <div>設定ファイルのJsonを解釈中に発生したエラーを表現するクラス</div>
 * 
 * <div>Class that represents the error that occurred while interpreting Json in the configuration file.</div>
 * 
 * <div>Copyright (c) 2019-2020 Masahito Hemmi </div>
 * <div>This software is released under the MIT License.</div>
 */
public class ConfigurationSyntaxErrorException extends RestdayException {
    
    public static final String MSG  = "Json syntax error : ";
    
    private final JsonValue config;
            
    public ConfigurationSyntaxErrorException(String msg, Throwable th) {
        super(msg, th);
        this.config = null;
    }
    
    public ConfigurationSyntaxErrorException(String msg, Throwable th, JsonValue config) {
        super(msg, th);
        this.config = config;
    }
    
    public ConfigurationSyntaxErrorException(String msg, Throwable th, String detail) {
        super(msg, th);
        super.messageForDevelopers = detail;
        config = null;
    }



    @Override
    public String getMessage() {
        if (config == null) {
            return super.getMessage();
        }
        return super.getMessage() + " " + config.toString();
    }
    
}
