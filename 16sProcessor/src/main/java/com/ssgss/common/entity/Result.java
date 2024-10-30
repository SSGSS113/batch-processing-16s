package com.ssgss.common.entity;

import com.ssgss.common.command.AbstractCommand;

public class Result {
    boolean sucess;
    String text;
    public Result(Builder builder) {
        this.text = builder.text;
        this.sucess = builder.sucess;
    }

    public boolean isSucess() {
        return sucess;
    }

    public String getText() {
        return text;
    }

    public static class Builder{
        private boolean sucess;
        private String text;
        public Builder setSucess(boolean sucess){
            this.sucess = sucess;
            return this;
        }
        public Builder setText(String text){
            this.text = text;
            return this;
        }
        public Result build(){
            return new Result(this);
        }
    }
}
