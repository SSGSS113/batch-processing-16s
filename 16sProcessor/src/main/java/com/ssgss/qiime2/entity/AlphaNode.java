package com.ssgss.qiime2.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlphyNode {
    private String sraId = "NAN";
    private double simpson = -1;
    private double otu = -1;
    private double chao1 = -1;
    private double shannon = -1;
}
