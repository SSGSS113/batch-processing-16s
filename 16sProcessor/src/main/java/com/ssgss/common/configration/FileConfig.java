package com.ssgss.common.configration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class FileConfig {
    @Value("${file.ALL_WORK_DIRECTORY}")
    private File allWorkDirectory;
    @Value("${file.FILES}")
    private File files;
    @Value("${file.CLASSIFIER_PATH}")
    private File classifier;
    @Value("${file.CSV_PATH}")
    private File csv;
}