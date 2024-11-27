package com.ssgss.fastqc.constant;

import com.ssgss.common.constant.FileConstant;
import com.ssgss.common.util.CSVUtil;
import com.ssgss.common.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public final class FastqcFileConstant {
    public static final List<String> HEADER = new ArrayList<>();
    public static final File FASTQC_PATH;
    public static final File FASTQC_RECORD;
    public static final ReentrantLock FASTQC_LOCK = new ReentrantLock();
    static {
        HEADER.add("SraId");
        HEADER.add("left");
        HEADER.add("right");
        FASTQC_PATH = new File(FileConstant.getWorkDirectory(), "Fastqc");
        FileUtil.createDirectory(FASTQC_PATH);
        FASTQC_RECORD = new File(FileConstant.getFILES(), "fastqc.csv");
        if(!FASTQC_RECORD.exists()){
            List<String> header = new ArrayList<>();
            header.add("SraId");
            header.add("left");
            header.add("right");
            try {
                CSVUtil.createCSV(FASTQC_RECORD.getPath(), header, ',');
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
