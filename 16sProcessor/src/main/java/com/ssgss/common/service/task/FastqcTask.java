package com.ssgss.common.service.task;

import com.ssgss.common.constant.BlockQueueConstant;
import com.ssgss.common.constant.CommonConstant;
import com.ssgss.common.constant.SraException;
import com.ssgss.common.util.CSVUtil;
import com.ssgss.fastqc.constant.FastqcConstant;
import com.ssgss.fastqc.constant.FastqcFileConstant;
import com.ssgss.fastqc.entity.FastqcNode;
import com.ssgss.fastqc.entity.FastqcRequest;
import com.ssgss.fastqc.service.FastqcService;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingDeque;

@Slf4j
public class FastqcTask extends AbstractTask{
    private final FastqcRequest sra;
    private static final BlockingDeque<Object> outputQueue = BlockQueueConstant.FASTQC_LIST;
    private static final String type = ":Fastqc";
    public FastqcTask(Object sra) {
        super(((FastqcRequest)sra).getSra().getSraId() + type);
        this.sra = (FastqcRequest) sra;
    }

    @Override
    public void run() {
        log.info("{} 执行了", super.getName());
        log.info("Fastqc:doFastqc 步骤准备, Sra:{}, 处理线程: {}",
                sra.getSra().getSraId(), Thread.currentThread().getName());
        try {
            if (FastqcService.doFastqc(sra)) {
                log.info("Fastqc:doFastqc 完成, Sra:{}, 处理线程: {}",
                        sra.getSra().getSraId(), Thread.currentThread().getName());
                outputQueue.put(sra.getSra());
                CommonConstant.FASTQC_SUCCEED.getAndIncrement();
            }else{
                log.error("fastqc 失败");
                CommonConstant.FASTQC_FAILED.getAndIncrement();
                CommonConstant.FAILED_NUM.getAndIncrement();
                CommonConstant.putData(sra.getSra().getSraId(), type, "Fastqc 失败");
            }
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        } catch (SraException e){
            log.error(e.getMessage());
            log.error("fastqc 未知异常");
            CommonConstant.FASTQC_FAILED.getAndIncrement();
            CommonConstant.FAILED_NUM.getAndIncrement();
            CommonConstant.putData(sra.getSra().getSraId(), type, e.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            int numbers = CommonConstant.FASTQC_SUCCEED.get() + CommonConstant.FAILED_NUM.get();
            log.info("===========================================FASTQC==============================================");
            log.info("一共有 {} 条数据，已经处理了 {} 条，其中 {} 成功，共 {} 失败，本步骤失败 {}", CommonConstant.NUM,
                    numbers,
                    CommonConstant.FASTQC_SUCCEED.get(),
                    CommonConstant.FAILED_NUM.get(),
                    CommonConstant.FASTQC_FAILED.get());
            log.info("============================================FASTQC=============================================");
            if(numbers >= CommonConstant.NUM){
                refresh();
            }
        }
    }
    private void refresh(){
        List<String[]> newData = new LinkedList<>();
        String[] headers = new String[3];
        headers[0] = "SraId";
        headers[1] = "left";
        headers[2] = "right";
        newData.add(headers);
        for(Map.Entry<String, FastqcNode> line : FastqcConstant.MAPS.entrySet()){
            String[] newLine = new String[3];
            newLine[0] = line.getKey();
            newLine[1] = String.valueOf(line.getValue().getLeft());
            newLine[2] = String.valueOf(line.getValue().getRight());
            newData.add(newLine);
        }
        log.info("fastqc.csv 已经更新");
        CSVUtil.freshData(FastqcFileConstant.FASTQC_RECORD.getPath(), newData, ",");
    }
}
