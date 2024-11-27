package com.ssgss.qiime2.constant;

import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum AlphaConstant {
    SHANNON("shannon"), OTU("observed_features"), CHAO1("chao1"), SIMPSON("simpson");
    private final String type;
    AlphaConstant(String type){
        this.type = type;
    }
    public String getType(){
        return type;
    }
    public static AlphaConstant getValue(String type){
        switch (type){
            case "shannon":
                return SHANNON;
            case "observed_features":
                return OTU;
            case "chao1":
                return CHAO1;
            case "simpson":
                return SIMPSON;
            default:
                return null;
        }
    }
}
