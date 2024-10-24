package com.ssgss.common.service.task;

import com.ssgss.common.constant.CommonConstant;
import com.ssgss.common.entity.SraDTO;
import com.ssgss.qiime2.entity.SraQiime2DTO;
import com.ssgss.qiime2.service.Qiime2Service;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingDeque;

@Service
public class DenoiseTask implements Runnable{
    private final SraQiime2DTO sra;
    @Resource
    private Qiime2Service service;
    private static final BlockingDeque<SraQiime2DTO> outputQueue = CommonConstant.DENOISE_LIST;

    public DenoiseTask(Object sra) {
        this.sra = (SraQiime2DTO) sra;
    }

    @Override
    public void run() {
        if (service.doDenoise(sra)) {
            try {
                outputQueue.put(sra);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
