package com.ssgss.qiime2.constant;

import com.ssgss.common.constant.CommonConstant;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class Qiime2Constant {
    private static final Runtime runtime = Runtime.getRuntime();
    public static final int THREAD;
    public static File CLASSIFIER_PATH;
    public static final File MANIFEST_PATH = new File(CommonConstant.ALL_WORK_DIRECTORY, "MANIFEST");
    public static final File DEMUX_PATH = new File(CommonConstant.ALL_WORK_DIRECTORY, "DEMUX");
    public static final File REP_PATH = new File(CommonConstant.ALL_WORK_DIRECTORY, "REP");
    public static final File STATS_PATH = new File(CommonConstant.ALL_WORK_DIRECTORY, "STATS");
    public static final File TABLE_PATH = new File(CommonConstant.ALL_WORK_DIRECTORY, "TABLE");
    public static final File TAXONOMY_PATH = new File(CommonConstant.ALL_WORK_DIRECTORY, "TAXONOMY");
    public static final File SHANNON_PATH = new File(CommonConstant.ALL_WORK_DIRECTORY, "SHANNON");
    public static final File OTU_PATH = new File(CommonConstant.ALL_WORK_DIRECTORY, "OTU");
    public static final File CHAO1_PATH = new File(CommonConstant.ALL_WORK_DIRECTORY, "CHAO1");
    public static final File SIMPSON_PATH = new File(CommonConstant.ALL_WORK_DIRECTORY, "SIMPSON");
    public static final File TAXONOMY_TSV_PATH = new File(CommonConstant.ALL_WORK_DIRECTORY, "TAXONOMY_TSV");
    public static final String IMPORT_BASELINE = "qiime tools import";
    public static final String SINGLE_DATA2_BASELINE = "qiime dada2 denoise-single";
    public static final String PAIRED_DATA2_BASELINE = "qiime dada2 denoise-paired";
    public static final String TAXONOMY_BASELINE = "qiime feature-classifier classify-sklearn";
    public static final String ALPHA_DIV_BASELINE = "qiime diversity alpha";
    public static final String EXPORT_BASELINE = "qiime tools export";
    public static final List<AlphaConstant> alphaList;
    static {
        THREAD = runtime.availableProcessors()/2;
        alphaList = new ArrayList<>();
        alphaList.add(AlphaConstant.OTU);
        alphaList.add(AlphaConstant.CHAO1);
        alphaList.add(AlphaConstant.SHANNON);
        alphaList.add(AlphaConstant.SIMPSON);
    }
}
