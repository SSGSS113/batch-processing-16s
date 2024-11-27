package com.ssgss.common.configration;

import com.ssgss.common.constant.FileConstant;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Slf4j
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

    @PostConstruct
    public void init(){
        try {
            log.info("FileConfig 初始化开始");
            FileConstant.setWorkDirectory(allWorkDirectory);
            FileConstant.setFILES(files == null ? new File(allWorkDirectory, "files") : files);
            FileConstant.setCLASSIFIER(classifier);
            FileConstant.setCsvPath(csv);
        } catch (Exception e) {
            log.error("FileConfig 初始化失败", e);
            throw e; // 重新抛出异常，以便 Spring 处理
        }
        log.info("FileConfig 初始化结束");
    }
}