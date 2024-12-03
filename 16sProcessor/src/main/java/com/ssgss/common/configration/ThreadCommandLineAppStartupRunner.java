package com.ssgss.common.configration;

import com.ssgss.common.constant.FileConstant;
import com.ssgss.qiime2.constant.Qiime2Constant;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;

@Slf4j
@Component
public class ThreadCommandLineAppStartupRunner implements CommandLineRunner {
    @Value("${DT:10}")
    private int denoise;

    @Value("${TT:10}")
    private int taxonomy;

    @Value("${download:5}")
    private int downloadCores;

    @Value("${fastqdump:3}")
    private int fastqdumpCores;

    @Value("${fastqc:3}")
    private int fastqcCores;

    @Value("${import:3}")
    private int importCores;

    @Value("${denoise:1}")
    private int denoiseCores;

    @Value("${taxonomy:1}")
    private int taxonomyCores;

    @Value("${alpha:1}")
    private int alphaCores;

    @Value("${alive:20}")
    private int keepAliveTime;

    @Value("${DIR:./}")
    private String DIR;

    @Value("${classifier}")
    private String CLA;

    @Value("${CSV}")
    private String CSV;

    @Resource
    ThreadPollConfigrature threadPollConfigrature;
    @Override
    public void run(String... args) throws Exception {
        if(DIR == null || DIR.isBlank()){
            log.error("工作路径为空");
            Runtime.getRuntime().exit(1);
        }
        File allWorkDirectory = new File(DIR);
        if(CLA == null || CLA.isBlank()){
            log.error("分类器路径异常");
            Runtime.getRuntime().exit(1);
        }
        File classifier = new File(CLA);
        if(CSV == null || CSV.isBlank()){
            log.error("CSV 路径异常");
            Runtime.getRuntime().exit(1);
        }
        File csv = new File(CSV);
        try {
            log.info("ThreadConfig 初始化开始");
            Qiime2Constant.setDenoiseThread(denoise);
            Qiime2Constant.setTaxonomyThread(taxonomy);
        } catch (Exception e) {
            log.error("ThreadConfig 初始化失败", e);
            throw e; // 重新抛出异常，以便 Spring 处理
        }
        log.info("ThreadConfig 初始化结束");
        try {
            log.info("FileConfig 初始化开始");
            FileConstant.setWorkDirectory(allWorkDirectory);
            FileConstant.setFILES(new File(allWorkDirectory, "files"));
            FileConstant.setCLASSIFIER(classifier);
            FileConstant.setCsvPath(csv);
        } catch (Exception e) {
            log.error("FileConfig 初始化失败", e);
            throw e; // 重新抛出异常，以便 Spring 处理
        }
        log.info("FileConfig 初始化结束");
        log.info("ThreadPollConfig 初始化开始");
        threadPollConfigrature.setDownloadCores(downloadCores);
        threadPollConfigrature.setFastqdumpCores(fastqdumpCores);
        threadPollConfigrature.setFastqcCores(fastqcCores);
        threadPollConfigrature.setImportCores(importCores);
        threadPollConfigrature.setDenoiseCores(denoiseCores);
        threadPollConfigrature.setTaxonomyCores(taxonomyCores);
        threadPollConfigrature.setAlphaCores(alphaCores);
        threadPollConfigrature.setKeepAliveTime(keepAliveTime);
        log.info("ThreadPollConfig 初始化结束");
    }
}
