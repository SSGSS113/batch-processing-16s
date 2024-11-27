package com.ssgss.common.service.task;

import com.ssgss.common.constant.BlockQueueConstant;
import com.ssgss.common.constant.CommonConstant;
import com.ssgss.common.constant.SraException;
import com.ssgss.common.util.CSVUtil;
import com.ssgss.qiime2.entity.SraQiime2DTO;
import com.ssgss.qiime2.service.Qiime2Service;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
@Slf4j
public class DenoiseTask extends AbstractTask{
    private final SraQiime2DTO sra;
    private static final BlockingDeque<Object> outputQueue = BlockQueueConstant.DENOISE_LIST;
    private static final String type = ":Denoise";
    public DenoiseTask(Object sra) {
        super(((SraQiime2DTO)sra).getSra().getSraId() + type);
        this.sra = (SraQiime2DTO) sra;
    }

    @Override
    public void run() {
        log.info("{} 执行了", super.getName());
        log.info("Qiime2:getDenoise 步骤准备, Sra:{}, 处理线程: {}",
                sra.getSra().getSraId(), Thread.currentThread().getName());
        try {
            if (Qiime2Service.doDenoise(sra)) {
                log.info("Qiime2:getDenoise 完成, Sra:{}, 处理线程: {}",
                        sra.getSra().getSraId(), Thread.currentThread().getName());
                    outputQueue.put(sra);
                    CommonConstant.DENOISE_SUCCEED.getAndIncrement();
            }else{
                CommonConstant.DENOISE_FAILED.getAndIncrement();
                CommonConstant.FAILED_NUM.getAndIncrement();
                CommonConstant.putData(sra.getSra().getSraId(), type, "Denoise 步骤失败");
            }
        } catch (SraException e){
            CommonConstant.DENOISE_FAILED.getAndIncrement();
            CommonConstant.FAILED_NUM.getAndIncrement();
            CommonConstant.putData(sra.getSra().getSraId(), type, e.getMessage());
        } catch (RuntimeException e) {
            CommonConstant.DENOISE_FAILED.getAndIncrement();
            CommonConstant.FAILED_NUM.getAndIncrement();
            CommonConstant.putData(sra.getSra().getSraId(), type, e.getMessage());
            log.error(e.getMessage());
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            CommonConstant.DENOISE_FAILED.getAndIncrement();
            CommonConstant.FAILED_NUM.getAndIncrement();
            CommonConstant.putData(sra.getSra().getSraId(), type, e.getMessage());
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }finally {
            log.info("============================================DENOISE============================================");
            log.info("一共有 {} 条数据，已经处理了 {} 条，其中 {} 成功，共 {} 失败， 本步骤失败 {}", CommonConstant.NUM,
                    CommonConstant.DENOISE_SUCCEED.get() + CommonConstant.FAILED_NUM.get(),
                    CommonConstant.DENOISE_SUCCEED.get(),
                    CommonConstant.FAILED_NUM.get(),
                    CommonConstant.DENOISE_FAILED.get());
            log.info("============================================DENOISE=============================================");
        }
    }
}
