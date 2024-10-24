package com.ssgss.fastqc.factory;

import com.ssgss.common.command.Command;
import com.ssgss.common.factory.CommandFatory;
import com.ssgss.fastqc.command.FastqcCommand;
import com.ssgss.fastqc.constant.FastqcConstant;
import com.ssgss.fastqc.entity.FastqcRequest;

public class FastqcCommandFactory {
    public static Command getCommand(FastqcRequest request) {
        if(request.getSra().isPaired()) {
            return new FastqcCommand.Builder()
                    .addArg("-o")
                    .addArg(request.getSra().getLeftPath().getPath())
                    .addArg(request.getSra().getRightPath().getPath())
                    .addArg(request.getOutPutPath().getPath())
                    .build();
        }else{
            return new FastqcCommand.Builder()
                    .addArg("-o")
                    .addArg(request.getSra().getLeftPath().getPath())
                    .addArg(request.getOutPutPath().getPath())
                    .build();
        }
    }
}
