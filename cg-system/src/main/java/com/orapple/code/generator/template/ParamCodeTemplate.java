package com.orapple.code.generator.template;

import com.orapple.code.generator.AbstractCodeTemplate;
import com.orapple.code.generator.content.CodeTemplateContext;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author: Orapple Wu
 * @Time: 2022/11/22
 * @Description: information of this type！
 */
public class ParamCodeTemplate extends DaoCodeTemplate {
    private String rootFileCatalog;

    public ParamCodeTemplate(InputStream inputStream, String rootFileCatalog) {
        super(inputStream, rootFileCatalog);
        this.rootFileCatalog = rootFileCatalog;
    }

    @Override
    public FileWriter initFileWriter() {
        String fileName = "SearchByPage.java";

        String filePath = this.rootFileCatalog.concat("/main/java/") +
                this.transferPageNameToPath(CodeTemplateContext.getVal(AbstractCodeTemplate.ROOT_PACKAGE)) + "/param";

        try {
            return new FileWriter(this.initFile(filePath, fileName), true);
        } catch (IOException e) {
            throw new RuntimeException("初始化文件写出源失败");
        }
    }
}