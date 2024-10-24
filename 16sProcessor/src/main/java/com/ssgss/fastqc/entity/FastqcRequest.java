package com.ssgss.fastqc.entity;

import com.ssgss.common.entity.SraDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Getter
@Setter
public class FastqcRequest {
    private File outPutPath;
    private File fastqc_output_1;
    private File fastqc_output_2;
    private SraDTO sra;
}
