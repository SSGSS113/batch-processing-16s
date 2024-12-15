package com.ssgss.fastqc.service;

import com.ssgss.common.aop.annotation.ProcessTimer;
import com.ssgss.common.command.Command;
import com.ssgss.common.constant.SraException;
import com.ssgss.common.entity.Result;
import com.ssgss.common.util.FastQCAnalyzeUtil;
import com.ssgss.fastqc.constant.FastqcConstant;
import com.ssgss.fastqc.entity.FastqcNode;
import com.ssgss.fastqc.entity.FastqcRequest;
import com.ssgss.fastqc.factory.FastqcCommandFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Service
@Slf4j
public class FastqcService {
    @ProcessTimer("Fastqc:doFastqc")
    public static boolean doFastqc(FastqcRequest request) throws IOException {
        if(request.getSra().isPaired()){
            File fastqc_output_1 = new File(request.getOutPutPath(),
                    String.format("%s_1_fastqc.zip", request.getSra().getSraId()));
            request.setFastqc_output_1(fastqc_output_1);
            File fastqc_output_2 = new File(request.getOutPutPath(),
                    String.format("%s_2_fastqc.zip", request.getSra().getSraId()));
            request.setFastqc_output_2(fastqc_output_2);
        }else{
            File fastqc_output_1 = new File(request.getOutPutPath(),
                    String.format("%s_fastqc.zip", request.getSra().getSraId()));
            request.setFastqc_output_1(fastqc_output_1);
        }
        if(Objects.requireNonNull(request.getOutPutPath().listFiles()).length > 0){
            log.info("{} 中fastqc文件已存在", request.getOutPutPath());
            if(FastqcConstant.MAPS.containsKey(request.getSra().getSraId())){
                FastqcNode node = FastqcConstant.MAPS.get(request.getSra().getSraId());
                if(node.getLeft() > 0){
                    int left = node.getLeft();
                    request.getSra().setLeftLen(left);
                    if(request.getSra().isPaired()){
                        log.info("fastqc.csv 中存在 {} 的记录,left = {}", request.getSra().getSraId(), left);
                        int right = node.getRight();
                        if(right > 0){
                            log.info("fastqc.csv 中存在 {} 的记录,right = {}", request.getSra().getSraId(), right);
                            request.getSra().setRightLen(right);
                        }else{
                            log.info("fastqc.csv 中不存在 {} 的逆向长度", request.getSra().getSraId());
                            recordLength(request);
                        }
                    }
                }else{
                    log.info("fastqc.csv 中不存在 {}", request.getSra().getSraId());
                    recordLength(request);
                }
                return true;
            }
        }
        Command command = FastqcCommandFactory.getCommand(request);
        Result result = command.execute();
        if(!isSuccess(result)){
            log.error(String.format("Fastqc发生错误，sra = %s",request.getSra().getSraId()));
            return false;
        }
        recordLength(request);
        return result.isSucess();
    }

    private static void recordLength(FastqcRequest request) {
        FastqcNode newNode = FastqcConstant.MAPS.getOrDefault(request.getSra().getSraId(), new FastqcNode());
        newNode.setSraId(request.getSra().getSraId());
        FastqcConstant.MAPS.put(request.getSra().getSraId(), newNode);
        if(!request.getFastqc_output_1().exists()){
            throw new SraException(String.format("%s fastqc分析报告R1 不存在", request.getSra().getSraId()));
        }else{
            try {
                log.info("分析 {}", request.getFastqc_output_1());
                int len = FastQCAnalyzeUtil.analyzeFastQC(request.getFastqc_output_1());
                int leftLen = Math.max(len - request.getSra().getLeftTrim(), 150);
                leftLen = Math.min(leftLen, 300);
                request.getSra().setLeftLen(leftLen);
                newNode.setLeft(leftLen);
                log.info("sra: {} 的 fastqc 分析的 leftLen 的值为 {}", request.getSra().getSraId(), leftLen);
            } catch (IOException e) {
                log.error(e.getMessage());
                throw new SraException(String.format("%s R1分析报告解析出错", request.getSra().getSraId()), e);
            }
        }
        if(request.getSra().isPaired()){
            if(!request.getFastqc_output_2().exists()){
                throw new SraException(String.format("%s fastqc分析报告R2 不存在", request.getSra().getSraId()));
            }else{
                try {
                    log.info("分析 {}", request.getFastqc_output_2());
                    int len = FastQCAnalyzeUtil.analyzeFastQC(request.getFastqc_output_2());
                    int rightLen = Math.max(len - request.getSra().getRightTrim(), 150);
                    rightLen = Math.min(rightLen, 300);
                    request.getSra().setRightLen(rightLen);
//                    rightLen = Math.max(rightLen, request.getSra().getLeftLen() - 50);
                    newNode.setRight(rightLen);
                    log.info("sra: {} 的 fastqc 分析的 rightLen 的值为 {}", request.getSra().getSraId(), rightLen);
                } catch (IOException e) {
                    throw new SraException(String.format("%s R2分析报告解析出错", request.getSra().getSraId()), e);
                }
            }
        }
    }
    private static boolean isSuccess(Result result){
        return result != null && result.isSucess();
    }
}