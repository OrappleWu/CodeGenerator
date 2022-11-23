package com.orapple.code.generator.template;

import com.orapple.code.generator.AbstractCodeTemplate;
import com.orapple.code.generator.FileInfo;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

/**
 * @Author: Orapple Wu
 * @Time: 2022/11/22
 * @Description: information of this type！
 */
public final class CodeTemplateFactory {
    private CodeTemplateFactory() {
    }

    public static AbstractCodeTemplate codeTemplate(FileInfo fileInfo, String rootFileCatalog) {
        if ("Dao.txt".equals(fileInfo.getFileName())) {
            return new DaoCodeTemplate(fileInfo.getFileStream(), rootFileCatalog);
        }

        if ("ApplicationProperties.txt".equals(fileInfo.getFileName())) {
            return new SourceCodeTemplate(fileInfo.getFileStream(), rootFileCatalog);
        }

        if ("Pom.txt".equals(fileInfo.getFileName())) {
            return new PomCodeTemplate(fileInfo.getFileStream(), rootFileCatalog);
        }

        if ("ApplicationRun.txt".equals(fileInfo.getFileName())) {
            return new ApplicationRunCodeTemplate(fileInfo.getFileStream(), rootFileCatalog);
        }

        if ("BaseController.txt".equals(fileInfo.getFileName())) {
            return new BaseControllerCodeTemplate(fileInfo.getFileStream(), rootFileCatalog);
        }

        if ("Controller.txt".equals(fileInfo.getFileName())) {
            return new ControllerCodeTemplate(fileInfo.getFileStream(), rootFileCatalog);
        }

        if ("Entity.txt".equals(fileInfo.getFileName())) {
            return new EntityCodeTemplate(fileInfo.getFileStream(), rootFileCatalog);
        }

        if ("Mapper.txt".equals(fileInfo.getFileName())) {
            return new MapperCodeTemplate(fileInfo.getFileStream(), rootFileCatalog);
        }

        if ("SearchByPage.txt".equals(fileInfo.getFileName())) {
            return new ParamCodeTemplate(fileInfo.getFileStream(), rootFileCatalog);
        }

        if ("Service.txt".equals(fileInfo.getFileName())) {
            return new ServiceCodeTemplate(fileInfo.getFileStream(), rootFileCatalog);
        }

        if ("ServiceImpl.txt".equals(fileInfo.getFileName())) {
            return new ServiceImplCodeTemplate(fileInfo.getFileStream(), rootFileCatalog);
        }

        if ("WrapperUtils.txt".equals(fileInfo.getFileName())) {
            return new UtilsCodeTemplate(fileInfo.getFileStream(), rootFileCatalog);
        }
        return null;
    }

}
