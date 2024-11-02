package com.ssgss.qiime2.constant;

import com.ssgss.common.constant.CommonConstant;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class Qiime2Constant {
    private static final Runtime runtime = Runtime.getRuntime();
    public static final int THREAD;
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
