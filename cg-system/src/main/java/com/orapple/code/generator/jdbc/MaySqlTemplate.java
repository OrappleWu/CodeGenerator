package com.orapple.code.generator.jdbc;

import com.orapple.code.generator.AbstractCodeTemplate;
import com.orapple.code.generator.content.CodeTemplateContext;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @Author: Orapple Wu
 * @Time: 2022/11/23
 * @Description: information of this type！
 */
public class MaySqlTemplate extends AbstractJdbcTemplate {
    @Override
    protected Connection initConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = CodeTemplateContext.getVal(AbstractCodeTemplate.DATABASE_URL)
                    + "?characterEncoding=utf8&useUnicode=true&useSSL=false&serverTimezone=GMT%2B8&"
                    + "rewriteBatchedStatements=true&allowPublicKeyRetrieval=true";
            final String user = CodeTemplateContext.getVal(AbstractCodeTemplate.DATABASE_USERNAME);
            final String password = CodeTemplateContext.getVal(AbstractCodeTemplate.DATABASE_PASSWORD);
            return DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            return null;
        }
    }

}
