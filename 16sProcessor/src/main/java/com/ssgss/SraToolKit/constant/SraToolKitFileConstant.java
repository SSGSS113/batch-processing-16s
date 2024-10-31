package com.ssgss.SraToolKit.constant;

import com.ssgss.common.constant.FileConstant;
import com.ssgss.common.util.FileUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
@Slf4j
public final class SraToolKitFileConstant {
    public static final File DOWNLOAD_DIRECTORY;
    public static final File SRA_DIRECTORY;
    static {
        log.info("SraToolKitFileConstants 初始化开始");
        DOWNLOAD_DIRECTORY = new File(FileConstant.getWorkDirectory(), "SraDownload");
        SRA_DIRECTORY = new File(FileConstant.getWorkDirectory(), "SraFastq");
        FileUtil.createDirectory(DOWNLOAD_DIRECTORY);
        FileUtil.createDirectory(SRA_DIRECTORY);
        log.info("SraToolKitFileConstants 初始化结束");
    }
}
