package com.ssgss.SraToolKit.constant;

import com.ssgss.common.constant.CommonConstant;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class SraToolKitConstant {
    private static final Runtime runtime = Runtime.getRuntime();
    public static final String PREFETCH = "prefetch";
    public static final String FASTQ_DUMP = "fasterq-dump --split-3";
    public static final String VDBDUMP = "vdb-dump -R 1 -C READ_TYPE";
    public static final int THREADNUM = runtime.availableProcessors()/5;
}