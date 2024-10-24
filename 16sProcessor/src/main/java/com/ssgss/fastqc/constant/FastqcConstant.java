package com.ssgss.fastqc.constant;

import com.ssgss.common.constant.CommonConstant;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class FastqcConstant {
    private static final Runtime runtime = Runtime.getRuntime();
    public static int THREAD_NUMBER =  runtime.availableProcessors()/2;
    public static File WORK_DIRECTORY = new File(CommonConstant.ALL_WORK_DIRECTORY, "Fastqc");
    public static final String FastqcBaseLine = "fastqc";
    static {
        if(!WORK_DIRECTORY.exists()){
            if(WORK_DIRECTORY.mkdir()){
                log.info("成功创建 Fastqc 目录:{}", WORK_DIRECTORY.getPath());
            }else{
                log.error("创建 Fastqc 目录失败:{}", WORK_DIRECTORY.getPath());
            }
        }else{
            log.info("Fastqc 目录已存在:{}", WORK_DIRECTORY.getPath());
        }
    }
}
