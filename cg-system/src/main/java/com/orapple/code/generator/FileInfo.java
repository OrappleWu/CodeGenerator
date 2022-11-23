package com.orapple.code.generator;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.InputStream;

/**
 * @Author: Orapple Wu
 * @Time: 2022/11/23
 * @Description: information of this type！
 */
@Data
public class FileInfo {

    @ApiModelProperty("文件名称")
    private String fileName;

    @ApiModelProperty("文件对应的流")
    private InputStream fileStream;
}
