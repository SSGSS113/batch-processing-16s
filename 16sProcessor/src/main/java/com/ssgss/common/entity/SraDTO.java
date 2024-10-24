package com.ssgss.common.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Getter
@Setter
public class SraDTO {
    private String sraId;
    private boolean isPaired;
    private File leftPath;
    private File rightPath;
    private int leftLen;
    private int rightLen;
    private int leftTrim;
    private int rightTrim;
    private File manifestPath;
}
