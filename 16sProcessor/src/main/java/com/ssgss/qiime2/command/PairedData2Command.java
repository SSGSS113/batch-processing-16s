package com.ssgss.qiime2.command;

import com.ssgss.common.command.AbstractCommand;
import com.ssgss.qiime2.constant.Qiime2Constant;

public class PairedData2Command extends AbstractCommand {
    PairedData2Command(Builder builder){
        super(builder.buildCommandLine());
    }
    public static class Builder extends AbstractCommand.Builder<PairedData2Command.Builder> {

        // 返回自身类型，确保链式调用
        @Override
        protected PairedData2Command.Builder self() {
            return this;
        }

        // 构建并返回具体的 Command 对象
        @Override
        public PairedData2Command build() {
            this.setCommandLine(Qiime2Constant.PAIRED_DATA2_BASELINE);
            return new PairedData2Command(this);
        }
    }
}
