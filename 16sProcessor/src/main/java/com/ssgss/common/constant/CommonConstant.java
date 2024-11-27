package com.ssgss.common.constant;

import com.ssgss.common.entity.ErrorNode;
import com.ssgss.common.util.CSVUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

@Configuration
@ConfigurationProperties(prefix = "file")
public class CommonConstant {
    public static int NUM = Integer.MAX_VALUE;
    public static final AtomicInteger DOWNLOAD_SUCCEED = new AtomicInteger(0);
    public static final AtomicInteger DOWNLOAD_FAILED = new AtomicInteger(0);
    public static final AtomicInteger FASTQ_DUMP_SUCCEED = new AtomicInteger(0);
    public static final AtomicInteger FASTQ_DUMP_FAILED = new AtomicInteger(0);
    public static final AtomicInteger FASTQC_SUCCEED = new AtomicInteger(0);
    public static final AtomicInteger FASTQC_FAILED = new AtomicInteger(0);
    public static final AtomicInteger IMPORT_SUCCEED = new AtomicInteger(0);
    public static final AtomicInteger IMPORT_FAILED = new AtomicInteger(0);
    public static final AtomicInteger DENOISE_SUCCEED = new AtomicInteger(0);
    public static final AtomicInteger DENOISE_FAILED = new AtomicInteger(0);
    public static final AtomicInteger TAXONOMY_SUCCEED = new AtomicInteger(0);
    public static final AtomicInteger TAXONOMY_FAILED = new AtomicInteger(0);
    public static final AtomicInteger ALPHY_SUCCEED = new AtomicInteger(0);
    public static final AtomicInteger ALPHY_FAILED = new AtomicInteger(0);
    public static final AtomicInteger FAILED_NUM = new AtomicInteger(0);
    public static final File LOG = new File(FileConstant.getFILES(), "ErrorLog.csv");
    public static final ReentrantLock LOG_LOCK = new ReentrantLock();
    public static final Map<String, ErrorNode> ERROR_NODE_MAP = new ConcurrentHashMap<>();
    static {
        List<String> header = new ArrayList<>();
        header.add("SraId");
        header.add("Type");
        header.add("result");
        try {
            CSVUtil.createCSV(LOG.getPath(), header, ',');
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<String[]> data = CSVUtil.getData(LOG.getPath(), ",");
        data.remove(0);
        for(String[] line : data){
            String SraId = line[0];
            String tpye = line[1];
            String result = line[2];
            ErrorNode newNode = new ErrorNode(tpye, SraId, result);
            ERROR_NODE_MAP.put(SraId, newNode);
        }
    }
    public static void putData(String SraId, String type, String message){
        ErrorNode node = new ErrorNode(type, SraId,message);
        ERROR_NODE_MAP.put(SraId, node);
        freshLog();
    }
    private static void freshLog(){
        List<String[]> newData = new ArrayList<>();
        String[] header = new String[3];
        header[0] = "SraId";
        header[1] = "Type";
        header[2] = "result";
        newData.add(header);
        for(ErrorNode node : ERROR_NODE_MAP.values()){
            String[] newLine = new String[3];
            newLine[0] = node.getSraId();
            newLine[1] = node.getType();
            newLine[2] = node.getMessage();
            newData.add(newLine);
        }
        CSVUtil.freshData(LOG.getPath(), newData, ",");
    }
}
