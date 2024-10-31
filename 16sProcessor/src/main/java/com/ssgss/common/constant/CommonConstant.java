package com.ssgss.common.constant;

import com.ssgss.common.util.CSVUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
@ConfigurationProperties(prefix = "file")
public class CommonConstant {
    public static int NUM = Integer.MAX_VALUE;
}
