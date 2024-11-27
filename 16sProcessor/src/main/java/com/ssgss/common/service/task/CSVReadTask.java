package com.ssgss.common.service.task;

import com.ssgss.common.aop.annotation.ProcessTimer;
import com.ssgss.common.constant.BlockQueueConstant;
import com.ssgss.common.constant.CommonConstant;
import com.ssgss.common.constant.SraException;
import com.ssgss.common.entity.SraDTO;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.BlockingDeque;

@Slf4j
public class CSVReadTask implements Runnable{
    File CSV;
    public CSVReadTask(File CSV){
        this.CSV = CSV;
    }
    private static final BlockingDeque<Object> outputQueue = BlockQueueConstant.SRA_LIST;
    @Override
    @ProcessTimer("CSVRead")
    public void run() throws SraException{
        if(CSV == null || !CSV.exists()){
            log.error(String.format("CSV 文件: %s 不存在",CSV.getPath()));
            throw new SraException(String.format("CSV 文件: %s 不存在",CSV.getPath()));
        }
        try (BufferedReader br = new BufferedReader(new FileReader(CSV))) {
            String line;
            if(br.readLine() == null){
                throw new SraException(String.format("CSV文件: %s 内容为空", CSV.getPath()));
            }
            int len = 0;
            while ((line = br.readLine()) != null) {
                len ++;
                // 分割每一行，假设逗号作为分隔符
                String[] fields = line.split(",");
                // 获取SraID、前引物、后引物
                String sraId = fields.length > 0 ? fields[0].trim() : null;
                String forwardPrimer = fields.length > 1 ? fields[1].trim() : null;
                String reversePrimer = fields.length > 2 ? fields[2].trim() : null;
                SraDTO sra = new SraDTO();
                sra.setSraId(sraId);
                sra.setLeftTrim(forwardPrimer == null ? 20 : forwardPrimer.length());
                sra.setRightTrim(reversePrimer == null ? 20 : reversePrimer.length());
                outputQueue.put(sra);
                log.info("CSV 文件读取了 SraId: {}, 已放入阻塞队列", sra.getSraId());
            }
            CommonConstant.NUM = len;
        } catch (IOException e) {
            log.error("CSV 文件: {} 出错了", CSV.getPath());
            throw new SraException(String.format("CSV 文件: %s 出错了",CSV.getPath()));
        } catch (InterruptedException e) {
            log.error("CSV 文件读取被打断");
        } finally {
            log.info("====================================  一共有 {} 条数据  ==========================================",
                    CommonConstant.NUM);
        }
    }
}