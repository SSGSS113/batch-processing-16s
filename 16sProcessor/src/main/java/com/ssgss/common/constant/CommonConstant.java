package com.ssgss.common.constant;

import com.ssgss.SraToolKit.entity.SraDownloadDTO;
import com.ssgss.common.entity.SraDTO;
import com.ssgss.common.util.CSVUtil;
import com.ssgss.qiime2.entity.SraQiime2DTO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public final class CommonConstant {
    public static File ALL_WORK_DIRECTORY = new File("");
    public static File FILES = new File(ALL_WORK_DIRECTORY, "files");
    public static File FASTQC_LEN_CSV = new File(FILES, "fastqc_len.csv");

    public static File CSV_PATH = new File("C:\\Users\\14451\\Desktop\\test.csv");
    public static final int SRATOOLKIT_DOWNLOAD_NUMBER = 4;
    public static final int SRATOOLKIT_DUMP_NUMBER = 8;
    public static final int FASTQC_NUMBER = 4;
    public static int NUM = 389;
    static {
    }
}
