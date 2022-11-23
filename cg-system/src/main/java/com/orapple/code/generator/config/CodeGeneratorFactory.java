package com.orapple.code.generator.config;

import com.orapple.code.generator.AbstractCodeTemplate;
import com.orapple.code.generator.FileInfo;
import com.orapple.code.generator.content.CodeTemplateContext;
import com.orapple.code.generator.jdbc.AbstractJdbcTemplate;
import com.orapple.code.generator.jdbc.MaySqlTemplate;
import com.orapple.code.generator.template.CodeTemplateFactory;
import com.orapple.code.generator.utils.CodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @Author: Orapple Wu
 * @Time: 2022/11/22
 * @Description: information of this type！
 */

@Component
public class CodeGeneratorFactory {

    @Autowired(required = false)
    private CodeConfig codeConfig;

    private final static String CONFIG_FIRE = "project.properties";

    private final static String URL = "databaseUrl";
    private final static String USERNAME = "databaseUsername";
    private final static String PASSWORD = "databasePassword";
    private final static String ROOT_PACKAGE = "rootPackage";
    private final static String PROJECT_VERSION = "projectVersion";
    private final static String ARTIFACT_NAME = "artifactName";
    private final static String MAIN_CLASS_NAME = "mainClassName";
    private final static String AUTHOR_INFO = "authorInfo";
    private final static String BASE_DTR = "baseDir";

    private final static String[] ROOT_FILES = new String[]{
            "ApplicationRun.txt", "BaseController.txt", "ApplicationProperties.txt",
            "Pom.txt", "SearchByPage.txt", "WrapperUtils.txt"
    };

    private final static String[] CODE_FILES = new String[]{
            "Controller.txt", "Dao.txt", "Entity.txt", "Mapper.txt", "Service.txt", "ServiceImpl.txt"
    };

    private void initProjectInfo() throws IOException {

        if (this.checkCodeConfigIsEmpty()) {
            Properties properties = new Properties();
            ClassPathResource classPathResource = new ClassPathResource(CONFIG_FIRE);
            properties.load(classPathResource.getInputStream());
            CodeTemplateContext.setVal(AbstractCodeTemplate.DATABASE_URL, properties.getProperty(URL));
            CodeTemplateContext.setVal(AbstractCodeTemplate.DATABASE_USERNAME, properties.getProperty(USERNAME));
            CodeTemplateContext.setVal(AbstractCodeTemplate.DATABASE_PASSWORD, properties.getProperty(PASSWORD));
            CodeTemplateContext.setVal(AbstractCodeTemplate.ROOT_PACKAGE, properties.getProperty(ROOT_PACKAGE));
            CodeTemplateContext.setVal(AbstractCodeTemplate.PROJECT_VERSION, properties.getProperty(PROJECT_VERSION));
            CodeTemplateContext.setVal(AbstractCodeTemplate.ARTIFACT_NAME, properties.getProperty(ARTIFACT_NAME));
            CodeTemplateContext.setVal(AbstractCodeTemplate.MAIN_CLASS_NAME, properties.getProperty(MAIN_CLASS_NAME));
            CodeTemplateContext.setVal(AbstractCodeTemplate.AUTHOR_INFO, properties.getProperty(AUTHOR_INFO));
            CodeTemplateContext.setVal(AbstractCodeTemplate.BASE_DTR, properties.getProperty(BASE_DTR));
        } else {
            CodeTemplateContext.setVal(AbstractCodeTemplate.DATABASE_URL, codeConfig.getDatabaseUrl());
            CodeTemplateContext.setVal(AbstractCodeTemplate.DATABASE_USERNAME, codeConfig.getDatabaseUsername());
            CodeTemplateContext.setVal(AbstractCodeTemplate.DATABASE_PASSWORD, codeConfig.getDatabasePassword());
            CodeTemplateContext.setVal(AbstractCodeTemplate.ROOT_PACKAGE, codeConfig.getRootPackage());
            CodeTemplateContext.setVal(AbstractCodeTemplate.PROJECT_VERSION, codeConfig.getProjectVersion());
            CodeTemplateContext.setVal(AbstractCodeTemplate.ARTIFACT_NAME, codeConfig.getArtifactName());
            CodeTemplateContext.setVal(AbstractCodeTemplate.MAIN_CLASS_NAME, codeConfig.getMainClassName());
            CodeTemplateContext.setVal(AbstractCodeTemplate.AUTHOR_INFO, codeConfig.getAuthorInfo());
            CodeTemplateContext.setVal(AbstractCodeTemplate.BASE_DTR, codeConfig.getBaseDir());
        }

    }

    // 为空返回true
    private boolean checkCodeConfigIsEmpty() {
        if(this.codeConfig == null){
            return true;
        }

        if(StringUtils.isEmpty(this.codeConfig.getArtifactName())){
            return true;
        }

        if(StringUtils.isEmpty(this.codeConfig.getBaseDir())){
            return true;
        }

        if(StringUtils.isEmpty(this.codeConfig.getDatabasePassword())){
            return true;
        }

        if(StringUtils.isEmpty(this.codeConfig.getDatabaseUrl())){
            return true;
        }

        if(StringUtils.isEmpty(this.codeConfig.getDatabaseUsername())){
            return true;
        }

        if(StringUtils.isEmpty(this.codeConfig.getRootPackage())){
            return true;
        }

        if(StringUtils.isEmpty(this.codeConfig.getMainClassName())){
            return true;
        }

        return StringUtils.isEmpty(this.codeConfig.getProjectVersion());
    }

    public final void generatorProjectCode() throws Exception {
        this.initProjectInfo();
        this.handlerGenerator(ROOT_FILES);

        String url = CodeTemplateContext.getVal(AbstractCodeTemplate.DATABASE_URL);
        final String db = url.substring(url.lastIndexOf("/") + 1);
        final String sql = "select table_name from information_schema.tables where table_schema='" + db + "'";
        AbstractJdbcTemplate jdbcTemplate = new MaySqlTemplate();
        List<Map<Integer, String>> mapList = jdbcTemplate.execute(sql);
        List<String> resultList = new ArrayList<>();
        for (Map<Integer, String> map : mapList) {
            resultList.add(map.get(0));
        }
        if(resultList.size() <= 0){
            return;
        }
        ExecutorService executorService = Executors.newFixedThreadPool(resultList.size());

        for (String tableName : resultList) {
            executorService.submit(() -> {
                try {
                    this.initProjectInfo();
                    this.initOtherContextInfo(tableName);

                    this.handlerGenerator(CODE_FILES);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        executorService.shutdown();

    }

    private void handlerGenerator(String... files) throws IOException {
        final String rootFileCatalog = this.initProjectPackage();
        List<FileInfo> fileList = this.getNeedToInitFile(files);
        for (FileInfo file : fileList) {
            AbstractCodeTemplate codeTemplate = CodeTemplateFactory.codeTemplate(file, rootFileCatalog);

            if (codeTemplate != null) {
                codeTemplate.doProjectInit();
            }
        }
    }

    private void initOtherContextInfo(String tableName) {
        CodeTemplateContext.setVal(AbstractCodeTemplate.TABLE_NAME, tableName);
        String reqUrl = "/" + tableName.replaceAll("_", "/");
        String entityName = CodeUtils.transferToJavaClassName(tableName);
        String rootPackage = CodeTemplateContext.getVal(AbstractCodeTemplate.ROOT_PACKAGE);
        String basePackage = rootPackage + "." + entityName.toLowerCase();
        CodeTemplateContext.setVal(AbstractCodeTemplate.ENTITY_NAME, entityName);
        CodeTemplateContext.setVal(AbstractCodeTemplate.BASE_PACKAGE, basePackage);
        CodeTemplateContext.setVal(AbstractCodeTemplate.REQUEST_URL, reqUrl);
    }

    private List<FileInfo> getNeedToInitFile(String... files) {
        try {
            List<FileInfo> fileInfoList = new ArrayList<>();
            for(String file : files){
                ClassPathResource classPathResource = new ClassPathResource("template/" + file);
                FileInfo fileInfo = new FileInfo();
                fileInfo.setFileName(file);
                fileInfo.setFileStream(classPathResource.getInputStream());
                fileInfoList.add(fileInfo);
            }
            return fileInfoList;
        } catch (IOException e) {
            throw new RuntimeException("获取模板文件出现错误");
        }
    }

    private String initProjectPackage() {
        return CodeTemplateContext.getVal(AbstractCodeTemplate.BASE_DTR)
                .concat(CodeTemplateContext.getVal(AbstractCodeTemplate.ARTIFACT_NAME)).concat("/src");
    }


}
