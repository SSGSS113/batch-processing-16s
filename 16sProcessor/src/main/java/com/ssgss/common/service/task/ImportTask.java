package com.ssgss.common.service.task;

import com.ssgss.common.aop.annotation.ProcessTimer;
import com.ssgss.common.constant.BlockQueueConstant;
import com.ssgss.common.entity.SraDTO;
import com.ssgss.qiime2.entity.SraQiime2DTO;
import com.ssgss.qiime2.service.Qiime2Service;

import java.util.concurrent.BlockingDeque;

public class ImportTask extends AbstractTask{
    private final SraDTO sra;
    private static final BlockingDeque<Object> outputQueue = BlockQueueConstant.IMPORT_LIST;
    private static final String type = ":Import";
    public ImportTask(Object sra) {
        super(((SraDTO)sra).getSraId() + type);
        this.sra = (SraDTO) sra;
    }

    @Override
    @ProcessTimer("Qiime2:doImport")
    public void run() {
        SraQiime2DTO sraQiime2DTO = new SraQiime2DTO(sra);
        if (Qiime2Service.doImport(sraQiime2DTO)) {
            try {
                outputQueue.put(sraQiime2DTO);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
