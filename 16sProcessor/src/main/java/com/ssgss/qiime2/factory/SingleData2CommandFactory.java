package com.ssgss.qiime2.factory;

import com.ssgss.common.command.Command;
import com.ssgss.qiime2.command.SingleData2Command;
import com.ssgss.qiime2.constant.Qiime2Constant;
import com.ssgss.qiime2.entity.SraQiime2DTO;

public class SingleData2CommandFactory {
    public static Command getCommand(SraQiime2DTO sra){
        return new SingleData2Command.Builder()
                .addArg("--i-demultiplexed-seqs")
                .addArg(sra.getDemux().getPath())
                .addArg("--p-n-threads")
                .addArg(String.valueOf(Qiime2Constant.THREAD))
                .addArg("--p-trim-left")
                .addArg(String.valueOf(sra.getSra().getLeftTrim()))
                .addArg("--p-trunc-len")
                .addArg(String.valueOf(sra.getSra().getLeftLen()))
                .addArg("--o-representative-sequences")
                .addArg(sra.getRep().getPath())
                .addArg("--o-table")
                .addArg(sra.getTable().getPath())
                .addArg("--o-denoising-stats")
                .addArg(sra.getStats().getPath())
                .build();
    }
}