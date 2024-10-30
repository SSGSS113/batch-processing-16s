package com.ssgss.common.service.task;

import com.ssgss.common.aop.annotation.ProcessTimer;
import com.ssgss.common.constant.BlockQueueConstant;
import com.ssgss.qiime2.entity.SraQiime2DTO;
import com.ssgss.qiime2.service.Qiime2Service;

import java.util.concurrent.BlockingDeque;

public class AlphaTask extends AbstractTask{
    private final SraQiime2DTO sra;
    private static final BlockingDeque<Object> outputQueue = BlockQueueConstant.TAXONOXY_LIST;
    private static final String type = ":Alpha";
    public AlphaTask(Object sra) {
        super(((SraQiime2DTO)sra).getSra().getSraId() + type);
        this.sra = (SraQiime2DTO) sra;
    }

    @Override
    @ProcessTimer("Qiime2:getAlpha")
    public void run() {
        if (Qiime2Service.doAlpha(sra)) {
            try {
                outputQueue.put(sra);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
