package com.ssgss.fastqc.service;

import com.ssgss.fastqc.entity.FastqcRequest;


public interface FastqcService {
    boolean doFastqc(FastqcRequest request);
}
