package com.ssgss.SraToolKit.entity;

import com.ssgss.common.entity.SraDTO;

import java.io.File;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SraDownloadDTO {
    private SraDTO sra;
    private File SraPath;

    public SraDownloadDTO(){

    }

    public SraDownloadDTO(SraDTO sra, File sraPath) {
        this.sra = sra;
        SraPath = sraPath;
    }

    public SraDTO getSra() {
        return sra;
    }

    public void setSra(SraDTO sra) {
        this.sra = sra;
    }

    public File getSraPath() {
        return SraPath;
    }

    public void setSraPath(File sraPath) {
        SraPath = sraPath;
    }
}