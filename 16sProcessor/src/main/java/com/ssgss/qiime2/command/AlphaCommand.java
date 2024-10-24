package com.ssgss.qiime2.command;

import com.ssgss.common.command.AbstractCommand;
import com.ssgss.qiime2.constant.Qiime2Constant;

public class PairedData2 extends AbstractCommand {
    PairedData2(Builder builder){
        super(builder.buildCommandLine());
    }
    public static class Builder extends AbstractCommand.Builder<PairedData2.Builder> {

        // 返回自身类型，确保链式调用
        @Override
        protected PairedData2.Builder self() {
            return this;
        }

        // 构建并返回具体的 Command 对象
        @Override
        public PairedData2 build() {
            this.setCommandLine(Qiime2Constant.PAIRED_DATA2);
            return new PairedData2(this);
        }
    }
}
