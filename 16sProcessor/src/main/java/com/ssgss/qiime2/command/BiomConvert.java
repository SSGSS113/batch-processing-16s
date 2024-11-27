package com.ssgss.qiime2.command;

import com.ssgss.common.command.AbstractCommand;
import com.ssgss.qiime2.constant.Qiime2Constant;

public class BiomConvert extends AbstractCommand {
    BiomConvert(Builder builder){
        super(builder.buildCommandLine());
    }
    public static class Builder extends AbstractCommand.Builder<BiomConvert.Builder> {

        // 返回自身类型，确保链式调用
        @Override
        protected BiomConvert.Builder self() {
            return this;
        }

        // 构建并返回具体的 Command 对象
        @Override
        public BiomConvert build() {
            this.setCommandLine(Qiime2Constant.BIOM_CONVERT_BASELINE);
            return new BiomConvert(this);
        }
    }
}
