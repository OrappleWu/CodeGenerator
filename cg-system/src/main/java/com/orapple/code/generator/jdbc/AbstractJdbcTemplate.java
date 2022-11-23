package com.orapple.code.generator.jdbc;

import org.springframework.util.CollectionUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: Orapple Wu
 * @Time: 2022/11/23
 * @Description: information of this type！
 */
public abstract class AbstractJdbcTemplate {


    public final List<Map<Integer, String>> execute(String sql) {
        Connection connection = null;
        Statement statement = null;
        List<Map<Integer, String>> result = new ArrayList<>();
        try {
            connection = this.initConnection();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                Map<Integer, String> map = new HashMap<>();
                int index = 0;
                do {
                    try {
                        map.put(index++, resultSet.getString(index));
                    } catch (Exception e) {
                        break;
                    }
                } while (index <= 10000);
                result.add(map);
            }
            return result.stream().filter(v -> !CollectionUtils.isEmpty(v)).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("执行SQL查询发生异常！");
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected abstract Connection initConnection();


}
