package com.ssgss.common.constant;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class BlockQueueConstant {
    public static BlockingDeque<Object> SRA_LIST = new LinkedBlockingDeque<>();
    public static BlockingDeque<Object> DOWNLOAD_LIST = new LinkedBlockingDeque<>();
    public static BlockingDeque<Object> FASTQC_LIST = new LinkedBlockingDeque<>();
    public static BlockingDeque<Object> FASTQ_DUMP = new LinkedBlockingDeque<>();
    public static BlockingDeque<Object> IMPORT_LIST = new LinkedBlockingDeque<>();
    public static BlockingDeque<Object> DENOISE_LIST = new LinkedBlockingDeque<>();
    public static BlockingDeque<Object> TAXONOXY_LIST = new LinkedBlockingDeque<>();
}
