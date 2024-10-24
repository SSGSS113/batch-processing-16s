package com.ssgss.common.service.task;

import com.ssgss.common.constant.BlockQueueConstant;
import com.ssgss.common.constant.CommonConstant;
import com.ssgss.common.entity.SraDTO;
import com.ssgss.qiime2.entity.SraQiime2DTO;
import com.ssgss.qiime2.service.Qiime2Service;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingDeque;

public class TaxonomyTask extends AbstractTask{
    private final SraQiime2DTO sra;
    @Resource
    private Qiime2Service service;
    private static final BlockingDeque<Object> outputQueue = BlockQueueConstant.TAXONOXY_LIST;
    private static final String type = ":Taxonomy";
    public TaxonomyTask(Object sra) {
        super(((SraQiime2DTO)sra).getSra().getSraId() + type);
        this.sra = (SraQiime2DTO) sra;
    }

    @Override
    public void run() {
        if (service.doClassify(sra)) {
            try {
                outputQueue.put(sra);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
