package com.ssgss.fastqc.constant;

import com.ssgss.common.constant.FileConstant;
import com.ssgss.common.util.FileUtil;

import java.io.File;

public final class FastqcFileConstant {
    public static final File WORK_DIRECTORY;
    static {
        WORK_DIRECTORY = new File(FileConstant.getWorkDirectory(), "Fastqc");
        FileUtil.createDirectory(WORK_DIRECTORY);
    }
}
