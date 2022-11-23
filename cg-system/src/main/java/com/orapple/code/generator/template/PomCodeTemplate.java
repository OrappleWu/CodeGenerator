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
public class PomCodeTemplate extends SourceCodeTemplate {

    private String rootFileCatalog;

    public PomCodeTemplate(InputStream inputStream, String rootFileCatalog) {
        super(inputStream, rootFileCatalog);

        this.rootFileCatalog = rootFileCatalog;
    }

    @Override
    public FileWriter initFileWriter() {
        String fileName = "pom.xml";
        String filePath = this.rootFileCatalog.replace("/src", "");

        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdir();
        }

        File file1 = new File(filePath + "/" + fileName);

        if (file1.exists()) {
            file1.delete();
        }

        try {
            return new FileWriter(new File(filePath + "/" + fileName), true);
        } catch (IOException e) {
            throw new RuntimeException("初始化文件写出源失败");
        }
    }
}
