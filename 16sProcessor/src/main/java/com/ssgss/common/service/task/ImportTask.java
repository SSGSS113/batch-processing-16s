package com.ssgss.common.service.task;

import com.ssgss.SraToolKit.entity.SraDownloadDTO;
import com.ssgss.SraToolKit.service.SraToolKitService;
import com.ssgss.common.constant.BlockQueueConstant;
import com.ssgss.common.constant.CommonConstant;
import com.ssgss.common.entity.SraDTO;
import com.ssgss.qiime2.entity.SraQiime2DTO;
import com.ssgss.qiime2.service.Qiime2Service;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingDeque;

public class ImportTask extends AbstractTask{
    private final SraDTO sra;
    @Resource
    private Qiime2Service service;
    private static final BlockingDeque<Object> outputQueue = BlockQueueConstant.IMPORT_LIST;
    private static final String type = ":Import";
    public ImportTask(Object sra) {
        super(((SraDTO)sra).getSraId() + type);
        this.sra = (SraDTO) sra;
    }

    @Override
    public void run() {
        SraQiime2DTO sraQiime2DTO = new SraQiime2DTO(sra);
        if (service.doImport(sraQiime2DTO)) {
            try {
                outputQueue.put(sraQiime2DTO);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
