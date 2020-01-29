package jp.oops.clazz.restday.dao;

/**
 * <div>Copyright (c) 2019-2020 Masahito Hemmi </div>
 * <div>This software is released under the MIT License.</div>
 */
public class DaoResult {
 
    Object val;
    
    public DaoResult() {
        this.val = null;
    }
    
    public DaoResult(Object value) {
        this.val = value;
    }
    
    public Object getObject() {
        return val;
    }
    
}
