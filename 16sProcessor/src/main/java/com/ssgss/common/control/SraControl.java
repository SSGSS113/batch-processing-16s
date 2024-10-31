package com.ssgss.common.control;

import com.ssgss.common.constant.FileConstant;
import com.ssgss.common.service.SraService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Slf4j
public class SraControl implements CommandLineRunner {
    @Resource
    SraService service;
    public void start(){
        log.info(String.format("开始读取 CSV 文件，CSV_PATH: %s", FileConstant.getCsvPath()));
        service.doCSVRead();
        log.info("开启 Download 线程池");
        service.doDownload();
        log.info("开启 FastqDump 线程池");
        service.doFastqDump();
        log.info("开启 Fastqc 线程池");
        service.doFastqc();
        log.info("开启 Import 线程池");
        service.doImport();
        log.info("开启 Denoise 线程池");
        service.doDenoise();
        log.info("开启 Taxonomy 线程池");
        service.doTaxonomy();
        log.info("开启 Alpha 线程池");
        service.doAlpha();
    }

    @Override
    public void run(String... args) throws Exception {
        start();
    }
}
