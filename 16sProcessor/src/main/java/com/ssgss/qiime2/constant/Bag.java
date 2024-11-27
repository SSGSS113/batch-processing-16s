package com.ssgss.qiime2.constant;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum Bag {
    Paired("PairedEndFastqManifestPhred33V2"), Single("SingleEndFastqManifestPhred33V2");
    private final String bag;
    Bag(String bag){
        this.bag = bag;
    }
    public String getBag(){
        return bag;
    }
}
