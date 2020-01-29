package jp.oops.clazz.restday.dao;

import java.util.Map;

/**
 * <div>Copyright (c) 2019-2020 Masahito Hemmi . </div>
 * <div>This software is released under the MIT License.</div>
 */
public final class SqlTemplateResults {
    
    private final Map<String, Integer> map;
    private final String template;
    
    SqlTemplateResults(String template, Map<String, Integer> map) {
        this.template = template;
        this.map = map;
    }

    /**
     * @return the map
     */
    public Map<String, Integer> getMap() {
        return map;
    }

    /**
     * @return the template
     */
    public String getTemplate() {
        return template;
    }
}
