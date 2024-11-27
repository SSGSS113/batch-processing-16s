package com.ssgss.common.configration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "threadpool")
public class ThreadPollConfigrature {
    private int downloadCores;
    private int denoiseCores;
    private int alphaCores;
    private int fastqcCores;
    private int importCores;
    private int fastqdumpCores;
    private int taxonomyCores;
    private int keepAliveTime;

    public int getDownloadCores() {
        return downloadCores;
    }

    public void setDownloadCores(int downloadCores) {
        this.downloadCores = downloadCores;
    }

    public int getDenoiseCores() {
        return denoiseCores;
    }

    public void setDenoiseCores(int denoiseCores) {
        this.denoiseCores = denoiseCores;
    }

    public int getAlphaCores() {
        return alphaCores;
    }

    public void setAlphaCores(int alphaCores) {
        this.alphaCores = alphaCores;
    }

    public int getFastqcCores() {
        return fastqcCores;
    }

    public void setFastqcCores(int fastqcCores) {
        this.fastqcCores = fastqcCores;
    }

    public int getImportCores() {
        return importCores;
    }

    public void setImportCores(int importCores) {
        this.importCores = importCores;
    }

    public int getFastqdumpCores() {
        return fastqdumpCores;
    }

    public void setFastqdumpCores(int fastqdumpCores) {
        this.fastqdumpCores = fastqdumpCores;
    }

    public int getTaxonomyCores() {
        return taxonomyCores;
    }

    public void setTaxonomyCores(int taxonomyCores) {
        this.taxonomyCores = taxonomyCores;
    }

    public int getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(int keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }
}
