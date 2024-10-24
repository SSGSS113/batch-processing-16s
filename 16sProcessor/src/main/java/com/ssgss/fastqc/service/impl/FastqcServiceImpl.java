package com.ssgss.fastqc.service.impl;

import com.ssgss.common.command.Command;
import com.ssgss.common.constant.SraException;
import com.ssgss.common.util.FastQCAnalyzeUtil;
import com.ssgss.fastqc.command.FastqcCommand;
import com.ssgss.fastqc.entity.FastqcRequest;
import com.ssgss.fastqc.factory.FastqcCommandFactory;
import com.ssgss.fastqc.service.FastqcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class FastqcServiceImpl implements FastqcService {
    @Override
    public boolean doFastqc(FastqcRequest request) {
        Command command = FastqcCommandFactory.getCommand(request);
        try{
            String callBack = command.execute();
            File fastqc_output_1 = new File(request.getOutPutPath(),
                    String.format("%s_trimmed_R1_fastqc.zip", request.getSra().getSraId()));
            request.setFastqc_output_1(fastqc_output_1);
            if(request.getSra().isPaired()){
                File fastqc_output_2 = new File(request.getOutPutPath(),
                        String.format("%s_trimmed_R2_fastqc.zip", request.getSra().getSraId()));
                request.setFastqc_output_2(fastqc_output_2);
            }
        }catch (SraException e){
            throw new SraException(String.format("Fastqc发生错误，sra = %s",request.getSra().getSraId()), e);
        }
        recordLength(request);
        return true;
    }

    private void recordLength(FastqcRequest request) {
        if(!request.getFastqc_output_1().exists()){
            throw new SraException(String.format("%s fastqc分析报告R1 不存在", request.getSra().getSraId()));
        }else{
            try {
                int leftLen = FastQCAnalyzeUtil.analyzeFastQC(request.getFastqc_output_1());
                request.getSra().setLeftLen(leftLen);
            } catch (IOException e) {
                throw new SraException(String.format("%s R1分析报告解析出错", request.getSra().getSraId()), e);
            }
        }
        if(request.getSra().isPaired()){
            if(!request.getFastqc_output_2().exists()){
                throw new SraException(String.format("%s fastqc分析报告R2 不存在", request.getSra().getSraId()));
            }else{
                try {
                    int rightLen = FastQCAnalyzeUtil.analyzeFastQC(request.getFastqc_output_2());
                    request.getSra().setRightLen(rightLen);
                } catch (IOException e) {
                    throw new SraException(String.format("%s R2分析报告解析出错", request.getSra().getSraId()), e);
                }
            }
        }
    }
}
