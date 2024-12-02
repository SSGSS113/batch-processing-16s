package com.ssgss.common.configration;

import com.ssgss.qiime2.constant.Qiime2Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ThreadCommandLineAppStartupRunner implements CommandLineRunner {
    @Value("${denoise:10}")
    private int denoise;

    @Value("${taxonomy:10}")
    private int taxonomy;
    @Override
    public void run(String... args) throws Exception {
        log.info("DENOISETHREAD = {}", Qiime2Constant.DENOISE_THREAD);
        Qiime2Constant.setDenoiseThread(denoise);
        log.info("denoise = {}", denoise);
        log.info("DENOISETHREAD = {}", Qiime2Constant.DENOISE_THREAD);
        log.info("TAXONOMYTHREAD = {}", Qiime2Constant.TAXONOMY_THREAD);
        Qiime2Constant.setTaxonomyThread(taxonomy);
        log.info("taxonomy = {}", taxonomy);
        log.info("TAXONOMYTHREAD = {}", Qiime2Constant.TAXONOMY_THREAD);
    }
}
