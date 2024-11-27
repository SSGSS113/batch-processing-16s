package com.ssgss.qiime2.constant;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum SampleData {
    Paired("SampleData[PairedEndSequencesWithQuality]"),
    Single("SampleData[SequencesWithQuality]");
    private final String type;
    SampleData(String type) {
        this.type = type;
    }
    public String getType(){
        return type;
    }
}
