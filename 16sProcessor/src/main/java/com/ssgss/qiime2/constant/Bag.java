package com.ssgss.qiime2.constant;

import lombok.Getter;

@Getter
public enum Bag {
    Paired("PairedEndFastqManifestPhred33V2"), Single("PairedEndFastqManifestPhred33V2");
    private final String bag;
    Bag(String bag){
        this.bag = bag;
    }
}
