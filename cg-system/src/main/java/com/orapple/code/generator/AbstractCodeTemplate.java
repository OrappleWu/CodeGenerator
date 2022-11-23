package com.orapple.code.generator;

import com.orapple.code.generator.content.CodeTemplateContext;
import com.orapple.code.generator.jdbc.AbstractJdbcTemplate;
import com.orapple.code.generator.jdbc.MaySqlTemplate;
import com.orapple.code.generator.utils.CodeUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: Orapple Wu
 * @Time: 2022/11/22
 * @Description: information of this type！
 */
public abstract class AbstractCodeTemplate {

    // 数据源的URL
    public final static String DATABASE_URL = "#@DATABASE_URL@#";

    // 项目初始化的根目录
    public static final String BASE_DTR = "#@BASE_DTR@#";

    // 连接数据源用户名
    public final static String DATABASE_USERNAME = "#@DATABASE_USERNAME@#";

    // 连接数据源密码
    public final static String DATABASE_PASSWORD = "#@DATABASE_PASSWORD@#";

    // 数据库表生成的根目录
    public final static String BASE_PACKAGE = "#@BASE_PACKAGE@#";

    // 作者信息
    public final static String AUTHOR_INFO = "#@AUTHOR_INFO@#";

    // 启动类名称
    public final static String MAIN_CLASS_NAME = "#@MAIN_CLASS_NAME@#";

    // 项目的根目录
    public final static String ROOT_PACKAGE = "#@ROOT_PACKAGE@#";

    // 实体名称
    public final static String ENTITY_NAME = "#@ENTITY_NAME@#";

    // 数据库表名称
    public final static String TABLE_NAME = "#@TABLE_NAME@#";

    // 项目的组ID
    public final static String ARTIFACT_NAME = "#@ARTIFACT_NAME@#";

    // 项目的版本
    public final static String PROJECT_VERSION = "#@PROJECT_VERSION@#";

    // 项目的访问地址
    public final static String REQUEST_URL = "#@REQUEST_URL@#";

    public abstract InputStreamReader initFileReader();

    public abstract FileWriter initFileWriter();

    public final void doProjectInit() throws IOException {
        final InputStreamReader fileReader = initFileReader();
        BufferedReader reader = new BufferedReader(fileReader);

        final FileWriter fileWriter = initFileWriter();
        BufferedWriter writer = new BufferedWriter(fileWriter);

        String readStr;

        while ((readStr = reader.readLine()) != null) {
            if (readStr.contains("#@ENTITY_CONTENT@#")) {
                try {
                    List<FieldColumnBean> fields = this.initAllEntityFields(CodeTemplateContext.getVal(TABLE_NAME));
                    for (FieldColumnBean field : fields) {
                        writer.write(this.transferFieldToString(field));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                continue;
            }
            String newFileStr = readStr.replaceAll(DATABASE_URL, CodeTemplateContext.getVal(DATABASE_URL))
                    .replaceAll(DATABASE_USERNAME, CodeTemplateContext.getVal(DATABASE_USERNAME))
                    .replaceAll(DATABASE_PASSWORD, CodeTemplateContext.getVal(DATABASE_PASSWORD))
                    .replaceAll(BASE_PACKAGE, CodeTemplateContext.getVal(BASE_PACKAGE))
                    .replaceAll(AUTHOR_INFO, CodeTemplateContext.getVal(AUTHOR_INFO))
                    .replaceAll(MAIN_CLASS_NAME, CodeTemplateContext.getVal(MAIN_CLASS_NAME))
                    .replaceAll(ROOT_PACKAGE, CodeTemplateContext.getVal(ROOT_PACKAGE))
                    .replaceAll(ENTITY_NAME, CodeTemplateContext.getVal(ENTITY_NAME))
                    .replaceAll(TABLE_NAME, CodeTemplateContext.getVal(TABLE_NAME))
                    .replaceAll(ARTIFACT_NAME, CodeTemplateContext.getVal(ARTIFACT_NAME))
                    .replaceAll(PROJECT_VERSION, CodeTemplateContext.getVal(PROJECT_VERSION))
                    .replaceAll(REQUEST_URL, CodeTemplateContext.getVal(REQUEST_URL));

            writer.write(newFileStr + "\n");
        }


        writer.close();
        fileWriter.close();
        reader.close();
        fileReader.close();
    }

    private List<FieldColumnBean> initAllEntityFields(String tableName) throws Exception {
        String url = CodeTemplateContext.getVal(DATABASE_URL);
        final String db = url.substring(url.lastIndexOf("/") + 1);
        final String sql = "select column_name, data_type, column_comment from information_schema.columns " +
                "where table_schema='" + db + "' and table_name = '" + tableName + "'";
        AbstractJdbcTemplate template = new MaySqlTemplate();
        List<Map<Integer, String>> mapList = template.execute(sql);
        List<FieldColumnBean> result = new ArrayList<>();
        for (Map<Integer, String> map : mapList) {

            FieldColumnBean columnBean = new FieldColumnBean();
            columnBean.setName(map.get(0));
            columnBean.setType(map.get(1));
            columnBean.setNote(map.get(2));
            result.add(columnBean);
        }
        return result;
    }

    private String transferFieldToString(FieldColumnBean field) {
        StringBuilder sb = new StringBuilder("\t@ApiModelProperty(\"").append(field.getNote()).append("\")\n");
        sb.append("\tprivate ");
        if (field.getType().equals("datetime")) {
            sb.append("Date ");
        }

        if (field.getType().equals("tinyint")) {
            sb.append("Integer ");
        }

        if (field.getType().equals("varchar")) {
            sb.append("String ");
        }

        if (field.getType().equals("int")) {
            sb.append("Integer ");
        }

        if (field.getType().equals("bigint")) {
            sb.append("Long ");
        }

        if (field.getType().equals("decimal")) {
            sb.append("BigDecimal ");
        }

        sb.append(CodeUtils.transferToJavaField(field.getName())).append(";\n\n");
        return sb.toString();
    }


    protected String transferPageNameToPath(String path) {
        return path.replaceAll("\\.", "/");
    }

    protected File initFile(String filePath, String fileName) throws IOException {
        File file = new File(filePath + "/" + fileName);
        if (file.exists()) {
            file.delete();
        }
        String[] split = filePath.split("/");
        String tempPath = split[0] + "//";
        for (int i = 0; i < split.length - 2; i++) {
            tempPath = tempPath + "/" + split[i + 2];
            File tempFile = new File(tempPath);
            if (!tempFile.exists()) {
                tempFile.mkdir();
            }
        }
        return new File(filePath + "/" + fileName);
    }
}
