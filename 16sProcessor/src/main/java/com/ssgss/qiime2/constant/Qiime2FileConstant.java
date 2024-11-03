package com.ssgss.qiime2.constant;

import com.ssgss.common.constant.FileConstant;
import com.ssgss.common.util.CSVUtil;
import com.ssgss.common.util.FileUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Slf4j
public final class Qiime2FileConstant {
    public final static File MANIFEST_PATH;
    public final static File DEMUX_PATH;
    public final static File REP_PATH;
    public final static File STATS_PATH;
    public final static File TABLE_PATH;
    public final static File TAXONOMY_PATH;
    public final static File SHANNON_PATH;
    public final static File OTU_PATH;
    public final static File CHAO1_PATH;
    public final static File SIMPSON_PATH;
    public final static File TAXONOMY_TSV_PATH;
    public final static File DENOISE_TSV_PATH;
    public final static File ALPHA_PATH;
    public final static File OUT_TSV_PATH;
    public final static File CHAO1_TSV_PATH;
    public final static File SIMPSON_TSV_PATH;
    public final static File SHANNON_TSV_PATH;
    public final static File ALPHA_OUTPUT;
    static {
        log.info("Qiime2FileConstant 初始化开始");
        MANIFEST_PATH = new File(FileConstant.getWorkDirectory(), "manifest");
        FileUtil.createDirectory(MANIFEST_PATH);
        DEMUX_PATH = new File(FileConstant.getWorkDirectory(), "demux");
        FileUtil.createDirectory(DEMUX_PATH);
        REP_PATH = new File(FileConstant.getWorkDirectory(), "rep");
        FileUtil.createDirectory(REP_PATH);
        STATS_PATH = new File(FileConstant.getWorkDirectory(), "stats");
        FileUtil.createDirectory(STATS_PATH);
        TABLE_PATH = new File(FileConstant.getWorkDirectory(), "table");
        FileUtil.createDirectory(TABLE_PATH);
        TAXONOMY_PATH = new File(FileConstant.getWorkDirectory(), "taxonomy");
        FileUtil.createDirectory(TAXONOMY_PATH);
        ALPHA_PATH = new File(FileConstant.getWorkDirectory(), "alpha");
        FileUtil.createDirectory(ALPHA_PATH);
        SHANNON_PATH = new File(ALPHA_PATH, "shannon");
        FileUtil.createDirectory(SHANNON_PATH);
        OTU_PATH = new File(ALPHA_PATH, "OTU");
        FileUtil.createDirectory(OTU_PATH);
        CHAO1_PATH = new File(ALPHA_PATH, "chao1");
        FileUtil.createDirectory(CHAO1_PATH);
        SIMPSON_PATH = new File(ALPHA_PATH, "simpson");
        FileUtil.createDirectory(SIMPSON_PATH);
        TAXONOMY_TSV_PATH = new File(TAXONOMY_PATH, "TSV");
        FileUtil.createDirectory(TAXONOMY_TSV_PATH);
        DENOISE_TSV_PATH = new File(FileConstant.getWorkDirectory(), "denoise");
        FileUtil.createDirectory(DENOISE_TSV_PATH);
        OUT_TSV_PATH = new File(OTU_PATH, "TSV");
        FileUtil.createDirectory(OUT_TSV_PATH);
        CHAO1_TSV_PATH = new File(CHAO1_PATH, "TSV");
        FileUtil.createDirectory(CHAO1_TSV_PATH);
        SIMPSON_TSV_PATH = new File(SIMPSON_PATH, "TSV");
        FileUtil.createDirectory(SIMPSON_TSV_PATH);
        SHANNON_TSV_PATH = new File(SHANNON_PATH, "TSV");
        FileUtil.createDirectory(SHANNON_TSV_PATH);
        ALPHA_OUTPUT = new File(FileConstant.getFILES(),"alpha_output.csv");
        List<String> headers = new ArrayList<>();
        headers.add("SraId");
        headers.add(AlphaConstant.SIMPSON.getType());
        headers.add(AlphaConstant.OTU.getType());
        headers.add(AlphaConstant.CHAO1.getType());
        headers.add(AlphaConstant.SHANNON.getType());
        try {
            CSVUtil.createCSV(ALPHA_OUTPUT.getPath(),headers, ',');
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("Qiime2FileConstant 初始化结束");
    }
}
