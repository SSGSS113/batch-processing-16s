package com.ssgss.common.service.task;

import com.ssgss.common.constant.CommonConstant;
import com.ssgss.qiime2.entity.SraQiime2DTO;
import com.ssgss.qiime2.service.Qiime2Service;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingDeque;

@Service
public class TaxonomyTask implements Runnable{
    private final SraQiime2DTO sra;
    @Resource
    private Qiime2Service service;
    private static final BlockingDeque<SraQiime2DTO> outputQueue = CommonConstant.TAXONOXY_LIST;

    public TaxonomyTask(Object sra) {
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
