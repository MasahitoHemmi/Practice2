package jp.oops.clazz.restday.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jp.oops.clazz.restday.exception.ConfigurationSyntaxErrorException;
import jp.oops.clazz.restday.exception.MayBeDuplicateKeyException;
import jp.oops.clazz.restday.exception.RestdayException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <div>Copyright (c) 2019-2020 Masahito Hemmi . </div>
 * <div>This software is released under the MIT License.</div>
 */
public final class SimpleDaoImpl implements ISimpleDao {

    private static final Logger LOG = LogManager.getLogger(SimpleDaoImpl.class);

    @Override
    public List<Map<String, Object>> select(Connection connection, String sqlTempl, Map<String, SQLValue> checkedMap, GetParams getParams) throws SQLException, RestdayException {

        ArrayList<Map<String, Object>> resultList = new ArrayList<>();

        SqlTemplateResults sqlTemplResult = parseTemplate(sqlTempl);
        PreparedStatement ps = connection.prepareStatement(sqlTemplResult.getTemplate());
        try {
            fillData(ps, sqlTemplResult.getMap(), checkedMap);
            LOG.debug("45) ps = " + ps);

            ResultSet rs = ps.executeQuery();

            ResultSetMetaData metaData = rs.getMetaData();

            ArrayList<String> columnNameList = new ArrayList<>();
            int count = metaData.getColumnCount();
            for (int i = 1; i <= count; i++) {
                columnNameList.add(metaData.getColumnName(i));
            }

            while (rs.next()) {

                HashMap<String, Object> row = new HashMap<>();
                for (int i = 1; i <= count; i++) {
                    Object obj = rs.getObject(i);

                    row.put(columnNameList.get(i - 1), obj);
                }
                resultList.add(row);
            }

        } finally {
            ps.close();
        }

        return resultList;
    }

    @Override
    public int delete(Connection connection, String sqlTempl, Map<String, SQLValue> checkedMap) throws SQLException, RestdayException {

        int nUpdate;
        SqlTemplateResults sqlTemplResult = parseTemplate(sqlTempl);
        PreparedStatement ps = connection.prepareStatement(sqlTemplResult.getTemplate());
        try {
            fillData(ps, sqlTemplResult.getMap(), checkedMap);

            nUpdate = ps.executeUpdate();
        } finally {
            ps.close();
        }
        return nUpdate;
    }

    @Override
    public DaoResult insert(Connection connection, String sqlTempl, Map<String, SQLValue> checkedMap) throws SQLException, RestdayException {

        SqlTemplateResults sqlTemplResult = parseTemplate(sqlTempl);

        try {
            PreparedStatement ps = connection.prepareStatement(sqlTemplResult.getTemplate());
            try {
                fillData(ps, sqlTemplResult.getMap(), checkedMap);

                LOG.debug("ps=" + ps);
                ps.execute();
            } finally {
                ps.close();
            }
        } catch (SQLException sqle) {

            String errMsg = sqle.getMessage();
            if (errMsg.indexOf(DUPLICATE_ERR) >= 0) {

                throw new MayBeDuplicateKeyException("may be duplicate key", null);
//                resultJson = " {  \"error\": \"may be duplicate key\"  } ";
            } else {
                throw sqle;
            }
        }

        Object autoIncrement = null;
        try {
            PreparedStatement ps2 = connection.prepareStatement(SELECT_LAST);
            try {
                ResultSet rs = ps2.executeQuery();
                if (rs.next()) {
                    autoIncrement = rs.getObject("LAST");
                }
            } finally {
                ps2.close();
            }
        } catch (SQLException sqle2) {
            throw sqle2;
        }
        
        return new DaoResult(autoIncrement);
    }

    public static String SELECT_LAST = "SELECT LAST_INSERT_ID() as LAST";

    public static String DUPLICATE_ERR = "uplicate";

    @Override
    public int update(Connection connection, String sqlTempl, Map<String, SQLValue> checkedMap) throws SQLException, RestdayException {

        SqlTemplateResults sqlTemplResult = parseTemplate(sqlTempl);

        PreparedStatement ps = connection.prepareStatement(sqlTemplResult.getTemplate());
        try {
            fillData(ps, sqlTemplResult.getMap(), checkedMap);

            LOG.debug("ps=" + ps);
            ps.execute();
        } finally {
            ps.close();
        }
        return 1;
    }

    void fillData(PreparedStatement ps, Map<String, Integer> map, Map<String, SQLValue> checkedMap) throws SQLException, RestdayException {

//        LOG.info("74)" + checkedMap);
        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();

            SQLValue sqlValue = checkedMap.get(key);

            if (sqlValue == null) {

                RestdayException me = new RestdayException("" + key + " not found", null).setMessageForDevelopers("Is the configuration file or request wrong?");
                throw me;
            }

            Integer integer = map.get(key);
            sqlValue.setTo(ps, integer);
        }
    }

    SqlTemplateResults parseTemplate(String sqlTempl) throws RestdayException {

        StringBuilder sb = new StringBuilder();
        int start = 0;
        HashMap<String, Integer> map = new HashMap<>();

        int count = 1; // ? が発見された数のカウント

        for (;;) {
            int findPos = sqlTempl.indexOf("?", start);

            if (findPos < 0) {
                sb.append(sqlTempl.substring(start));
                break;
            }

            int findPos2 = searchEnd(sqlTempl, findPos + 1);
            if (findPos2 < 0) {
                throw new ConfigurationSyntaxErrorException("bad sql template", null);
            }

            String keyword = sqlTempl.substring(findPos + 1, findPos2);

            map.put(keyword, count);

            sb.append(sqlTempl.substring(start, findPos + 1));
            start = findPos2;
            count++;
        }

//        LOG.info("template =" + sb.toString());
//        LOG.info("map=" + map.toString());
        return new SqlTemplateResults(sb.toString(), map);
    }

    // ? の後に、識別子として許可される文字の時　falseを返す
    // Returns false if the character is allowed as an identifier after '?'.
    private boolean isInvalidChar(char ch) {

        if ('0' <= ch && ch <= '9') {
            return false;
        }
        if ('a' <= ch && ch <= 'z') {
            return false;
        }
        if ('A' <= ch && ch <= 'Z') {
            return false;
        }
        if (ch == '_') {
            return false;
        }
        return true;
    }

    // ?以降のワードのindex
    //  Returns the index of the word following '?'.
    int searchEnd(String str, int startPos) {

        int pos = startPos;
        int end = str.length();
        while (pos < end) {
            char ch = str.charAt(pos);

            if (isInvalidChar(ch)) {
                break;
            }
            if (ch <= ' ') {
                break;
            }
            pos++;
        }
        return pos;
    }

}
