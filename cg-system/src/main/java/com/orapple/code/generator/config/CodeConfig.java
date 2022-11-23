package com.orapple.code.generator.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Orapple Wu
 * @Time: 2022/11/23
 * @Description: information of this type！
 */
@Configuration
@Data
public class CodeConfig {

    @Value("${databaseUrl:}")
    private String databaseUrl;

    @Value("${databaseUsername:}")
    private String databaseUsername;

    @Value("${databasePassword:}")
    private String databasePassword;

    @Value("${baseDir:}")
    private String baseDir;

    @Value("${rootPackage:}")
    private String rootPackage;

    @Value("${projectVersion:}")
    private String projectVersion;

    @Value("${artifactName:}")
    private String artifactName;

    @Value("${mainClassName:}")
    private String mainClassName;

    @Value("${authorInfo:}")
    private String authorInfo;
}
