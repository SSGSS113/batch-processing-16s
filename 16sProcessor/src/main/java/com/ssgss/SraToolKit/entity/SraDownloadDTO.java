package com.ssgss.SraToolKit.entity;

import com.ssgss.common.entity.SraDTO;
import lombok.*;

import java.io.File;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SraDownloadDTO {
    private SraDTO sra;
    private File SraPath;
}