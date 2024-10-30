package com.ssgss.SraToolKit.constant;

import com.ssgss.common.constant.FileConstant;
import com.ssgss.common.util.FileUtil;

import java.io.File;

public final class SraToolKitFileConstant {
    public static final File DOWNLOAD_DIRECTORY;
    public static final File SRA_DIRECTORY;
    static {
        DOWNLOAD_DIRECTORY = new File(FileConstant.WORK_DIRECTORY, "SraDownload");
        SRA_DIRECTORY = new File(FileConstant.WORK_DIRECTORY, "SraFastq");
        FileUtil.createDirectory(DOWNLOAD_DIRECTORY);
        FileUtil.createDirectory(SRA_DIRECTORY);
    }
}
