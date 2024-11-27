package com.ssgss.qiime2.constant;

import com.ssgss.common.util.CSVUtil;
import com.ssgss.qiime2.entity.AlphaNode;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
@Slf4j
public final class Qiime2Constant {
    public final static ReentrantLock ALPHY_LOCK = new ReentrantLock();
    private static final Runtime runtime = Runtime.getRuntime();
    public static final int DENOISE_THREAD;
    public static final int TAXONOMY_THREAD;
    public static final String IMPORT_BASELINE = "qiime tools import";
    public static final String SINGLE_DATA2_BASELINE = "qiime dada2 denoise-single";
    public static final String PAIRED_DATA2_BASELINE = "qiime dada2 denoise-paired";
    public static final String TAXONOMY_BASELINE = "qiime feature-classifier";
    public static final String ALPHA_DIV_BASELINE = "qiime diversity alpha";
    public static final String EXPORT_BASELINE = "qiime tools export";
    public static final String BIOM_CONVERT_BASELINE = "biom convert";
    public static final Map<AlphaConstant, Integer> alphaList;
    public static final ConcurrentHashMap<String, AlphaNode> ALPHY_MAP = new ConcurrentHashMap<>();
    static {
        DENOISE_THREAD = 20;
        TAXONOMY_THREAD = 20;
        alphaList = new HashMap<>();
        List<String[]> data = CSVUtil.getData(Qiime2FileConstant.ALPHA_OUTPUT.getPath(),",");
        for(int i = 0;i<data.size();i++){
            String[] line = data.get(i);
            if(i == 0){
                for(int j=1;j<=4;j++) {
                    String name = line[j];
                    log.info("Qiime2Constant 初始化 name = {}", name);
                    alphaList.put(AlphaConstant.getValue(line[j].trim()), j);
                }
                continue;
            }
            AlphaNode newNode = new AlphaNode();
            newNode.setSraId(line[0]);
            for(int j = 1;j<=4;j++){
                AlphaConstant key = AlphaConstant.getValue(line[j]);
                newNode.getAlphaMap().put(key, Double.valueOf(line[alphaList.get(key)]));
            }
        }
    }
}