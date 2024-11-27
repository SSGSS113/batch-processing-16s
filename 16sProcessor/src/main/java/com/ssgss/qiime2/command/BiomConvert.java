package com.ssgss.qiime2.command;

import com.ssgss.common.command.AbstractCommand;
import com.ssgss.qiime2.constant.Qiime2Constant;

public class TaxonomyCommand extends AbstractCommand {
    TaxonomyCommand(Builder builder){
        super(builder.buildCommandLine());
    }
    public static class Builder extends AbstractCommand.Builder<TaxonomyCommand.Builder> {

        // 返回自身类型，确保链式调用
        @Override
        protected TaxonomyCommand.Builder self() {
            return this;
        }

        // 构建并返回具体的 Command 对象
        @Override
        public TaxonomyCommand build() {
            this.setCommandLine(Qiime2Constant.TAXONOMY_BASELINE);
            return new TaxonomyCommand(this);
        }
    }
}
