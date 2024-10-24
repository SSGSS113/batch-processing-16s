package com.ssgss.qiime2.command;

import com.ssgss.common.command.AbstractCommand;
import com.ssgss.qiime2.constant.Qiime2Constant;

public class Taxonomy extends AbstractCommand {
    Taxonomy(Builder builder){
        super(builder.buildCommandLine());
    }
    public static class Builder extends AbstractCommand.Builder<Taxonomy.Builder> {

        // 返回自身类型，确保链式调用
        @Override
        protected Taxonomy.Builder self() {
            return this;
        }

        // 构建并返回具体的 Command 对象
        @Override
        public Taxonomy build() {
            this.setCommandLine(Qiime2Constant.TAXONOMY);
            return new Taxonomy(this);
        }
    }
}
