package com.ssgss.common.service.task;

import com.ssgss.common.aop.annotation.ProcessTimer;
import com.ssgss.common.constant.BlockQueueConstant;
import com.ssgss.common.entity.SraDTO;
import com.ssgss.qiime2.constant.Bag;
import com.ssgss.qiime2.constant.Qiime2FileConstant;
import com.ssgss.qiime2.constant.SampleData;
import com.ssgss.qiime2.entity.SraQiime2DTO;
import com.ssgss.qiime2.service.Qiime2Service;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.concurrent.BlockingDeque;
@Slf4j
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
        sraQiime2DTO.setType(sra.isPaired()? SampleData.Paired : SampleData.Single);
        sraQiime2DTO.setBag(sra.isPaired()? Bag.Paired : Bag.Single);
        log.info("Qiime2:doImport 步骤准备, Sra:{}, 处理线程: {}",
                sra.getSraId(), Thread.currentThread().getName());
        if (Qiime2Service.doImport(sraQiime2DTO)) {
            log.info("Qiime2:doImport 完成, Sra:{}, 处理线程: {}",
                    sra.getSraId(), Thread.currentThread().getName());
            try {
                outputQueue.put(sraQiime2DTO);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
