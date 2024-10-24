package com.ssgss.SraToolKit.constant;

import com.ssgss.common.constant.CommonConstant;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class SraToolKitConstant {
    public static File DOWNLOAD_DIRECTORY = new File(CommonConstant.ALL_WORK_DIRECTORY, "SraDownload");
    public static File SRA_DIRECTORY = new File(CommonConstant.ALL_WORK_DIRECTORY, "SraFastq");
    public static final String PREFETCH = "prefetch";
    public static final String FASTQ_DUMP = "fastq-dump";
    public static final String VDBDUMP = "vdb-dump -R 1 -C READ_TYPE";
    static {
        if(!DOWNLOAD_DIRECTORY.exists()){
            if(DOWNLOAD_DIRECTORY.mkdir()){
                log.info("成功创建 SraDownload 目录:{}", DOWNLOAD_DIRECTORY.getPath());
            }else{
                log.error("创建 SraDownload 目录失败:{}", DOWNLOAD_DIRECTORY.getPath());
            }
        }else{
            log.info("SraDownload 目录已存在:{}", DOWNLOAD_DIRECTORY.getPath());
        }
        if(!SRA_DIRECTORY.exists()){
            if(SRA_DIRECTORY.mkdir()){
                log.info("成功创建 SraFastq 目录:{}", SRA_DIRECTORY.getPath());
            }else{
                log.error("创建 SraFastq 目录失败:{}", SRA_DIRECTORY.getPath());
            }
        }else{
            log.info("SraFastq 目录已存在:{}", SRA_DIRECTORY.getPath());
        }
    }
}