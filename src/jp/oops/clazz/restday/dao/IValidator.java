package jp.oops.clazz.restday.dao;

import com.eclipsesource.json.JsonObject;
import java.util.Map;
import jp.oops.clazz.restday.exception.RestdayException;

/**
 *
 * <div>Copyright (c) 2019, 2020 Masahito Hemmi </div>
 * <div>This software is released under the MIT License.</div>
 */
public interface IValidator {
    
    DataAfterVerification doConversionAndValidation(Map<String, String> inputMap, JsonObject ruleJson) throws RestdayException;
}
