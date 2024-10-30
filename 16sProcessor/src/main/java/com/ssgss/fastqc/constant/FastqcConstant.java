package com.ssgss.fastqc.constant;

import com.ssgss.common.constant.CommonConstant;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class FastqcConstant {
    private static final Runtime runtime = Runtime.getRuntime();
    public static int THREAD_NUMBER =  runtime.availableProcessors()/2;
    public static final String FastqcBaseLine = "fastqc";
}
