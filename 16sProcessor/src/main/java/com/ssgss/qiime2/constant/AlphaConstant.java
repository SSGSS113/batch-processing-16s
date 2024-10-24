package com.ssgss.qiime2.constant;

import lombok.Getter;

@Getter
public enum AlphaConstant {
    SHANNON("shannon"), OTU("observed_features"), CHAO1("chao1"), SIMPSON("simpson");
    private final String type;
    AlphaConstant(String type){
        this.type = type;
    }
}
