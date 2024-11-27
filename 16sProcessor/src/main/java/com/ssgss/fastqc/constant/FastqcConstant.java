package com.ssgss.fastqc.constant;

import com.ssgss.common.util.CSVUtil;
import com.ssgss.fastqc.entity.FastqcNode;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class FastqcConstant {
    private static final Runtime runtime = Runtime.getRuntime();
    public static int THREAD_NUMBER =  runtime.availableProcessors()/2;
    public static final String FastqcBaseLine = "fastqc";
    public static final Map<String, FastqcNode> MAPS = new HashMap<>();
    public static final ConcurrentHashMap<String, Integer> HEADERS = new ConcurrentHashMap<>();
    static {
        HEADERS.put("SraId", 0);
        HEADERS.put("left", 1);
        HEADERS.put("right", 2);
        List<String[]> data = CSVUtil.getData(FastqcFileConstant.FASTQC_RECORD.getPath(),",");
        data.remove(0);
        for(String[] line : data){
            FastqcNode node = new FastqcNode();
            node.setSraId(line[0]);
            node.setLeft(line[1].equals("N")?0 : Integer.parseInt(line[1]));
            node.setRight(line[2].equals("N")||line[2].equals("NAN")?0 : Integer.parseInt(line[2]));
            MAPS.put(line[0], node);
        }
    }
}
