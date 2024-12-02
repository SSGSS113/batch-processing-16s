//package com.ssgss.common.configration;
//
//import com.ssgss.qiime2.constant.Qiime2Constant;
//import jakarta.annotation.PostConstruct;
//import lombok.Getter;
//import lombok.Setter;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@ConfigurationProperties(prefix = "thread")
//@Getter
//@Setter
//@Slf4j
//public class ThreadConfig {
//    private int denoise;
//    private int taxonomy;
//    @PostConstruct
//    public void init(){
//        try {
//            log.info("ThreadConfig 初始化开始");
//            Qiime2Constant.setDenoiseThread(denoise);
//            Qiime2Constant.setTaxonomyThread(taxonomy);
//        } catch (Exception e) {
//            log.error("ThreadConfig 初始化失败", e);
//            throw e; // 重新抛出异常，以便 Spring 处理
//        }
//        log.info("ThreadConfig 初始化结束");
//    }
//}
