package com.orapple.code.generator.template;

import com.orapple.code.generator.AbstractCodeTemplate;
import com.orapple.code.generator.content.CodeTemplateContext;

import java.io.*;

/**
 * @Author: Orapple Wu
 * @Time: 2022/11/22
 * @Description: information of this type！
 */
public class DaoCodeTemplate extends AbstractCodeTemplate {

    private InputStream inputStream;

    private String rootFileCatalog;

    public DaoCodeTemplate(InputStream inputStream, String rootFileCatalog) {
        this.inputStream = inputStream;
        this.rootFileCatalog = rootFileCatalog;
    }


    @Override
    public InputStreamReader initFileReader() {
        try {
            return new InputStreamReader(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("初始化文件读取源失败");
        }
    }

    @Override
    public FileWriter initFileWriter() {
        String fileName = "I".concat(CodeTemplateContext.getVal(AbstractCodeTemplate.ENTITY_NAME)).concat("Dao.java");
        String filePath = this.rootFileCatalog.concat("/main/java/")
                + this.transferPageNameToPath(CodeTemplateContext.getVal(AbstractCodeTemplate.BASE_PACKAGE)) + "/dao";

        try {
            return new FileWriter(this.initFile(filePath, fileName), true);
        } catch (IOException e) {
            throw new RuntimeException("初始化文件写出源失败");
        }
    }
}
