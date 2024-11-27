package com.ssgss.common.service.task;

import com.ssgss.common.constant.BlockQueueConstant;
import com.ssgss.common.constant.CommonConstant;
import com.ssgss.common.constant.SraException;
import com.ssgss.common.entity.SraDTO;
import com.ssgss.common.util.CSVUtil;
import com.ssgss.qiime2.constant.Bag;
import com.ssgss.qiime2.constant.SampleData;
import com.ssgss.qiime2.entity.SraQiime2DTO;
import com.ssgss.qiime2.service.Qiime2Service;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    public void run() {
        log.info("{} 执行了", super.getName());
        SraQiime2DTO sraQiime2DTO = new SraQiime2DTO(sra);
        sraQiime2DTO.setType(sra.isPaired()? SampleData.Paired : SampleData.Single);
        sraQiime2DTO.setBag(sra.isPaired()? Bag.Paired : Bag.Single);
        log.info("Qiime2:doImport 步骤准备, Sra:{}, 处理线程: {}",
                sra.getSraId(), Thread.currentThread().getName());
        try {
            if (Qiime2Service.doImport(sraQiime2DTO)) {
                log.info("Qiime2:doImport 完成, Sra:{}, 处理线程: {}",
                        sra.getSraId(), Thread.currentThread().getName());
                outputQueue.put(sraQiime2DTO);
                CommonConstant.IMPORT_SUCCEED.getAndIncrement();
            }else {
                CommonConstant.IMPORT_FAILED.getAndIncrement();
                CommonConstant.FAILED_NUM.getAndIncrement();
                CommonConstant.putData(sra.getSraId(), type, "Import 步骤失败");
            }
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        } catch (SraException e) {
            CommonConstant.IMPORT_FAILED.getAndIncrement();
            CommonConstant.FAILED_NUM.getAndIncrement();
            CommonConstant.putData(sra.getSraId(), type, e.getMessage());
        }finally {
            log.info("============================================IMPORT=============================================");
            log.info("一共有 {} 条数据，已经处理了 {} 条，其中 {} 成功，共 {} 失败，本步骤失败 {}", CommonConstant.NUM,
                    CommonConstant.IMPORT_SUCCEED.get() + CommonConstant.FAILED_NUM.get(),
                    CommonConstant.IMPORT_SUCCEED.get(),
                    CommonConstant.FAILED_NUM.get(),
                    CommonConstant.IMPORT_FAILED.get());
            log.info("============================================IMPORT=============================================");
        }
    }
}
