package com.ssgss.fastqc.entity;

import com.ssgss.common.entity.SraDTO;
import lombok.NonNull;
import org.antlr.v4.runtime.misc.NotNull;

import java.io.File;

public class FastqcRequest {
    private File outPutPath;
    private File fastqc_output_1;
    private File fastqc_output_2;
    @NotNull
    private SraDTO sra;

    public File getOutPutPath() {
        return outPutPath;
    }

    public void setOutPutPath(File outPutPath) {
        this.outPutPath = outPutPath;
    }

    public File getFastqc_output_1() {
        return fastqc_output_1;
    }

    public void setFastqc_output_1(File fastqc_output_1) {
        this.fastqc_output_1 = fastqc_output_1;
    }

    public File getFastqc_output_2() {
        return fastqc_output_2;
    }

    public void setFastqc_output_2(File fastqc_output_2) {
        this.fastqc_output_2 = fastqc_output_2;
    }

    public SraDTO getSra() {
        return sra;
    }

    public void setSra(SraDTO sra) {
        this.sra = sra;
    }
}
