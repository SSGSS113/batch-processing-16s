package com.ssgss.SraToolKit.command;

import com.ssgss.SraToolKit.constant.SraToolKitConstant;
import com.ssgss.common.command.AbstractCommand;

import java.io.File;

public class FastqDumpCommand extends AbstractCommand {
    FastqDumpCommand(String commond, File work_directory){
        super(commond, work_directory);
    }

    private FastqDumpCommand(Builder builder){
        super(builder.buildCommandLine(), builder.workingDirectory);
    }

    public static class Builder extends AbstractCommand.Builder<Builder> {
        private File workingDirectory;
        // 返回自身类型，确保链式调用

        public Builder setWorkingDirectory(File workingDirectory){
            this.workingDirectory = workingDirectory;
            return self();
        }
        @Override
        protected Builder self() {
            return this;
        }

        // 构建并返回具体的 Command 对象
        @Override
        public FastqDumpCommand build() {
            this.setCommandLine(SraToolKitConstant.FASTQ_DUMP);
            return new FastqDumpCommand(this);
        }
    }
}
