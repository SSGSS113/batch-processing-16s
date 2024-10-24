package com.ssgss.qiime2.service;

import com.ssgss.SraToolKit.entity.SraDownloadDTO;
import com.ssgss.qiime2.entity.SraQiime2DTO;

public interface Qiime2Service {
    boolean doImport(SraQiime2DTO sra);

    boolean doDenoise(SraQiime2DTO sra);

    boolean doClassify(SraQiime2DTO sra);

    boolean doAlpha(SraQiime2DTO sra);
}
