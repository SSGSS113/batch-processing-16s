package com.ssgss.common.service.task;

import com.ssgss.common.constant.BlockQueueConstant;
import com.ssgss.common.constant.CommonConstant;
import com.ssgss.common.constant.SraException;
import com.ssgss.common.util.CSVUtil;
import com.ssgss.qiime2.constant.AlphaConstant;
import com.ssgss.qiime2.constant.Qiime2Constant;
import com.ssgss.qiime2.constant.Qiime2FileConstant;
import com.ssgss.qiime2.entity.AlphaNode;
import com.ssgss.qiime2.entity.SraQiime2DTO;
import com.ssgss.qiime2.service.Qiime2Service;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingDeque;
@Slf4j
public class AlphaTask extends AbstractTask{
    private final SraQiime2DTO sra;
    private static final BlockingDeque<Object> outputQueue = null;
    private static final String type = ":Alpha";
    public AlphaTask(Object sra) {
        super(((SraQiime2DTO)sra).getSra().getSraId() + type);
        this.sra = (SraQiime2DTO) sra;
    }

    @Override
    public void run() {
        log.info("{} 执行了", super.getName());
        log.info("Qiime2:getAlpha 步骤完成, Sra:{}, 处理线程: {}",
                sra.getSra().getSraId(), Thread.currentThread().getName());
        try {
            if (Qiime2Service.doAlpha(sra)) {
                log.info("Qiime2:getAlpha 完成, Sra:{}, 处理线程: {}",
                        sra.getSra().getSraId(), Thread.currentThread().getName());
                    CommonConstant.ALPHY_SUCCEED.getAndIncrement();
            }else{
                CommonConstant.ALPHY_FAILED.getAndIncrement();
                CommonConstant.FAILED_NUM.getAndIncrement();
                CommonConstant.putData(sra.getSra().getSraId(), type, "Alpha 步骤失败");
            }
        } catch (SraException e) {
            CommonConstant.ALPHY_FAILED.getAndIncrement();
            CommonConstant.FAILED_NUM.getAndIncrement();
            CommonConstant.putData(sra.getSra().getSraId(), type, e.getMessage());
        } finally {
            int nums = CommonConstant.ALPHY_SUCCEED.get() + CommonConstant.FAILED_NUM.get();
            log.info("=============================================ALPHY=============================================");
            log.info("一共有 {} 条数据，已经处理了 {} 条，其中 {} 成功，共 {} 失败，本步骤失败 {}", CommonConstant.NUM,
                    nums,
                    CommonConstant.ALPHY_SUCCEED.get(),
                    CommonConstant.FAILED_NUM.get(),
                    CommonConstant.ALPHY_FAILED.get());
            log.info("=============================================ALPHY=============================================");
            if(nums >= CommonConstant.NUM) {
                fresh();
            }
        }
    }
    private void fresh() {
        Map<Integer, AlphaConstant> INDEX = new HashMap<>();
        for(AlphaConstant alpha : Qiime2Constant.alphaList.keySet()){
            INDEX.put(Qiime2Constant.alphaList.get(alpha), alpha);
        }
        String [] header = new String[5];
        header[0] = "SraId";
        for(int i = 1;i<=4;i++){
            header[i] = INDEX.get(i).getType();
        }
        List<String[]> newData = new ArrayList<>();
        newData.add(header);
        for(String s: header){
            log.info("header : {}", s);
        }
        log.info("ALPHA_MAP 数据数量为：{}", Qiime2Constant.ALPHY_MAP.size());
        for(AlphaNode node:Qiime2Constant.ALPHY_MAP.values()){
            StringBuilder sb = new StringBuilder();
            String[] line = new String[5];
            line[0] = node.getSraId();
            sb.append(line[0]).append(",");
            for(int i = 1;i<=4;i++){
                line[i] = String.valueOf(node.getAlphaMap().get(INDEX.get(i)));
                sb.append(line[i]).append(",");
            }
            log.info(sb.toString());
            newData.add(line);
        }
        log.info("alpha多样性文档已更新");
        CSVUtil.freshData(Qiime2FileConstant.ALPHA_OUTPUT.getPath(), newData, ",");
    }
}
