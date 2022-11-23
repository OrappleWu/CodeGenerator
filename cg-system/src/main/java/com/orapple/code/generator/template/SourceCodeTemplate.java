package com.orapple.code.generator.template;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author: Orapple Wu
 * @Time: 2022/11/22
 * @Description: information of this type！
 */
public class SourceCodeTemplate extends DaoCodeTemplate {

    private String rootFileCatalog;

    public SourceCodeTemplate(InputStream inputStream, String rootFileCatalog) {
        super(inputStream, rootFileCatalog);
        this.rootFileCatalog = rootFileCatalog;
    }


    @Override
    public FileWriter initFileWriter() {

        String fileName = "application.properties";
        String filePath = this.rootFileCatalog.concat("/main/resources");

        try {
            return new FileWriter(this.initFile(filePath, fileName), true);
        } catch (IOException e) {
            throw new RuntimeException("初始化文件写出源失败");
        }
    }
}
