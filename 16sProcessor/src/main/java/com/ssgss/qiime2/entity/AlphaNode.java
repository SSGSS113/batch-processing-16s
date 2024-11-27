package com.ssgss.qiime2.entity;

import com.ssgss.qiime2.constant.AlphaConstant;
import com.ssgss.qiime2.constant.Qiime2Constant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class AlphaNode {
    private String sraId = "NAN";
    private Map<AlphaConstant, Double> alphaMap;
    public AlphaNode() {
        alphaMap = new HashMap<>();
        for(AlphaConstant alpha : Qiime2Constant.alphaList.keySet()){
            alphaMap.put(alpha, -1.0);
        }
    }
}
