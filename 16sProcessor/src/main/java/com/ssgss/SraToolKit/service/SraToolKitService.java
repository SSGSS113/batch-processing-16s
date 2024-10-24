package com.ssgss.SraToolKit.service;

import com.ssgss.SraToolKit.entity.SraDownloadDTO;
import com.ssgss.common.constant.SraException;
import org.springframework.stereotype.Service;

@Service
public interface SraToolKitService {
    boolean isPaired(SraDownloadDTO sra) throws SraException;
    boolean downPrefetch(SraDownloadDTO sra) throws SraException;
    boolean doFastqDump(SraDownloadDTO sra) throws SraException;
}
