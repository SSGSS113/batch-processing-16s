package com.ssgss.fastqc.constant;

import com.ssgss.common.constant.FileConstant;
import com.ssgss.common.util.FileUtil;

import java.io.File;

public final class FastqcFileConstant {
    public static final File FASTQC_PATH;
    static {
        FASTQC_PATH = new File(FileConstant.getWorkDirectory(), "Fastqc");
        FileUtil.createDirectory(FASTQC_PATH);
    }
}
