package com.ssgss.common.service.task;

import com.ssgss.common.aop.annotation.ProcessTimer;
import com.ssgss.common.constant.BlockQueueConstant;
import com.ssgss.qiime2.entity.SraQiime2DTO;
import com.ssgss.qiime2.service.Qiime2Service;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingDeque;
@Slf4j
public class TaxonomyTask extends AbstractTask{
    private final SraQiime2DTO sra;
    private static final BlockingDeque<Object> outputQueue = BlockQueueConstant.TAXONOXY_LIST;
    private static final String type = ":Taxonomy";
    public TaxonomyTask(Object sra) {
        super(((SraQiime2DTO)sra).getSra().getSraId() + type);
        this.sra = (SraQiime2DTO) sra;
    }

    @Override
    @ProcessTimer("Qiime2:doTaxonomy")
    public void run() {
        log.info("Qiime2:doTaxonomy 步骤准备, Sra:{}, 处理线程: {}",
                sra.getSra().getSraId(), Thread.currentThread().getName());
        if (Qiime2Service.doClassify(sra)) {
            log.info("Qiime2:doTaxonomy 完成, Sra:{}, 处理线程: {}",
                    sra.getSra().getSraId(), Thread.currentThread().getName());
            try {
                outputQueue.put(sra);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
