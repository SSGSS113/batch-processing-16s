package com.ssgss.common.entity;

import java.io.File;

public class SraDTO {
    private String sraId;
    private boolean isPaired;

    public String getSraId() {
        return sraId;
    }

    public void setSraId(String sraId) {
        this.sraId = sraId;
    }

    public boolean isPaired() {
        return isPaired;
    }

    public void setPaired(boolean paired) {
        isPaired = paired;
    }

    public File getLeftPath() {
        return leftPath;
    }

    public void setLeftPath(File leftPath) {
        this.leftPath = leftPath;
    }

    public File getRightPath() {
        return rightPath;
    }

    public void setRightPath(File rightPath) {
        this.rightPath = rightPath;
    }

    public int getLeftLen() {
        return leftLen;
    }

    public void setLeftLen(int leftLen) {
        this.leftLen = leftLen;
    }

    public int getRightLen() {
        return rightLen;
    }

    public void setRightLen(int rightLen) {
        this.rightLen = rightLen;
    }

    public int getLeftTrim() {
        return leftTrim;
    }

    public void setLeftTrim(int leftTrim) {
        this.leftTrim = leftTrim;
    }

    public int getRightTrim() {
        return rightTrim;
    }

    public void setRightTrim(int rightTrim) {
        this.rightTrim = rightTrim;
    }

    public File getManifestPath() {
        return manifestPath;
    }

    public void setManifestPath(File manifestPath) {
        this.manifestPath = manifestPath;
    }

    private File leftPath;
    private File rightPath;
    private int leftLen;
    private int rightLen;
    private int leftTrim;
    private int rightTrim;
    private File manifestPath;
}
