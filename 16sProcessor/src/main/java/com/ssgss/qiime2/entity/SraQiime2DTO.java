package com.ssgss.qiime2.entity;

import com.ssgss.common.entity.SraDTO;
import com.ssgss.qiime2.constant.Bag;
import com.ssgss.qiime2.constant.SampleData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.File;

@Getter
@Setter
public class SraQiime2DTO {
    private SraDTO sra;
    private SampleData type;
    private File manifest;
    private File demux;
    private Bag bag;
    private File rep;
    private File table;
    private File stats;
    private File taxonomy;
    private File taxonomy_tsv;
    public SraQiime2DTO(SraDTO sra){
        this.sra = sra;
    }
}