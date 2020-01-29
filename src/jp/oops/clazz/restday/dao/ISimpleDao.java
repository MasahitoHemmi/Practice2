package jp.oops.clazz.restday.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import jp.oops.clazz.restday.exception.RestdayException;

/**
 * <div>Copyright (c) 2019-2020 Masahito Hemmi </div>
 * <div>This software is released under the MIT License.</div>
 */
public interface ISimpleDao {

    DaoResult insert(Connection connection, String sqlTempl, Map<String, SQLValue> checkedMap) throws SQLException, RestdayException;

    List<Map<String, Object>> select(Connection connection, String sqlTempl, Map<String, SQLValue> checkedMap, GetParams getParam) throws SQLException, RestdayException;

    int update(Connection connection, String sqlTempl, Map<String, SQLValue> checkedMap) throws SQLException, RestdayException;
    
    int delete(Connection connection, String sqlTempl, Map<String, SQLValue> checkedMap) throws SQLException, RestdayException;

}
