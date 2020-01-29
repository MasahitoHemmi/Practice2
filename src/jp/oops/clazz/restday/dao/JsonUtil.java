package jp.oops.clazz.restday.dao;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import jp.oops.clazz.restday.exception.ConfigurationSyntaxErrorException;

/**
 * 使っているjsonライブラリは、ほとんどRuntimeExceptionを発生させる。
 * 検査例外にしたかったので、変換するユーティリティクラスを作った。
 *
 *  The json library I use mostly raises RuntimeException.
 *  I wanted to make a check exception, so I created a utility class to convert.
 * 
 * <div>Copyright (c) 2020 Masahito Hemmi</div> 
 * <div>This software is released under the MIT License.</div>
 */
public final class JsonUtil {

    public static String    asStringStrictly(JsonValue jv, JsonValue config) throws ConfigurationSyntaxErrorException {

        if (jv == null)throw new ConfigurationSyntaxErrorException("can't convert NULL to String", null, config);
        String str;
        try {
            str = jv.asString();
        } catch (Exception ex) {
            throw new ConfigurationSyntaxErrorException("can't convert to String", ex, config);
        }
        return str;
    }

    public static JsonObject    asJsonObjectStrictly(JsonValue jv, JsonValue config) throws ConfigurationSyntaxErrorException {

        if (jv == null)throw new ConfigurationSyntaxErrorException("can't convert NULL to String", null, config);
        JsonObject str;
        try {
            str = jv.asObject();
        } catch (Exception ex) {
            throw new ConfigurationSyntaxErrorException("can't convert to JsonObject", ex, config);
        }
        return str;
    }

    public static JsonArray   asArrayStrictly(JsonValue jv, JsonValue config) throws ConfigurationSyntaxErrorException {

        if (jv == null)throw new ConfigurationSyntaxErrorException("can't convert NULL to String", null, config);
        JsonArray arr;
        try {
            arr = jv.asArray();
        } catch (Exception ex) {
            throw new ConfigurationSyntaxErrorException("can't convert to JsonArray", ex, config);
        }
        return arr;
    }
    
    public static int   asIntStrictly(JsonValue jv, JsonValue config) throws ConfigurationSyntaxErrorException {

        if (jv == null)throw new ConfigurationSyntaxErrorException("can't convert NULL to String", null, config);
        int val;
        try {
            val = jv.asInt();
        } catch (Exception ex) {
            throw new ConfigurationSyntaxErrorException("can't convert to int", ex, config);
        }
        return val;
    }
    
    public static long   asLongStrictly(JsonValue jv, JsonValue config) throws ConfigurationSyntaxErrorException {

        if (jv == null)throw new ConfigurationSyntaxErrorException("can't convert NULL to String", null, config);
        long val;
        try {
            val = jv.asLong();
        } catch (Exception ex) {
            throw new ConfigurationSyntaxErrorException("can't convert to long", ex, config);
        }
        return val;
    }
    
    private JsonUtil() {
    }
}
