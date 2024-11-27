package com.ssgss.fastqc.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FastqcNode {
    private String sraId;
    private int left = 0;
    private int right = 0;
}
