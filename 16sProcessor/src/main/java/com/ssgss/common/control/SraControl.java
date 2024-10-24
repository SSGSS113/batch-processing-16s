package com.ssgss.common.control;

import com.ssgss.common.service.SraService;
import jakarta.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class SraControl implements CommandLineRunner {
    @Resource
    SraService service;
    public void start(){
        service.doCSVRead();
        service.doDownload();
        service.doFastqDump();
        service.doFastqc();
        service.doImport();
        service.doDenoise();
        service.doTaxonomy();
        service.doAlpha();
    }

    @Override
    public void run(String... args) throws Exception {
        start();
    }
}
