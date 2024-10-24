package com.ssgss.qiime2.constant;

import lombok.Getter;

@Getter
public enum SampleData {
    Paired("SampleData[PairedEndSequencesWithQuality]"),
    Single("SampleData[SequencesWithQuality]");
    private final String type;
    SampleData(String type) {
        this.type = type;
    }
}
