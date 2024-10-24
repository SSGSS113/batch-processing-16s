package com.ssgss.qiime2.factory;

import com.ssgss.common.command.Command;
import com.ssgss.qiime2.command.SingleData2Command;
import com.ssgss.qiime2.constant.Qiime2Constant;
import com.ssgss.qiime2.entity.SraQiime2DTO;

public class SingleData2CommandFactory {
    public static Command getCommand(SraQiime2DTO sra){
        return new SingleData2Command.Builder()
                .addArg("--i-demultiplexed-seqs")
                .addArg(sra.getTaxonomy().toString())
                .addArg("--p-n-threads")
                .addArg(String.valueOf(Qiime2Constant.THREAD))
                .addArg("--p-trim-left")
                .addArg(String.valueOf(sra.getLeftLen()))
                .addArg("--p-trunc-len")
                .addArg(String.valueOf(sra.getRightLen()))
                .addArg("--o-representative-sequences")
                .addArg(sra.getRep().toString())
                .addArg("--o-table")
                .addArg(sra.getTable().toString())
                .addArg("--o-denoising-stats")
                .addArg(sra.getStats().toString())
                .build();
    }
}