package com.ssgss.qiime2.entity;

import com.ssgss.common.entity.SraDTO;
import com.ssgss.qiime2.constant.Bag;
import com.ssgss.qiime2.constant.SampleData;

import java.io.File;

public class SraQiime2DTO {
    private SraDTO sra;
    private SampleData type;
    private File manifest;
    private File demux;

    public SraDTO getSra() {
        return sra;
    }

    public void setSra(SraDTO sra) {
        this.sra = sra;
    }

    public SampleData getType() {
        return type;
    }

    public void setType(SampleData type) {
        this.type = type;
    }

    public File getManifest() {
        return manifest;
    }

    public void setManifest(File manifest) {
        this.manifest = manifest;
    }

    public File getDemux() {
        return demux;
    }

    public void setDemux(File demux) {
        this.demux = demux;
    }

    public Bag getBag() {
        return bag;
    }

    public void setBag(Bag bag) {
        this.bag = bag;
    }

    public File getRep() {
        return rep;
    }

    public void setRep(File rep) {
        this.rep = rep;
    }

    public File getTable() {
        return table;
    }

    public void setTable(File table) {
        this.table = table;
    }

    public File getStats() {
        return stats;
    }

    public void setStats(File stats) {
        this.stats = stats;
    }

    public File getTaxonomy() {
        return taxonomy;
    }

    public void setTaxonomy(File taxonomy) {
        this.taxonomy = taxonomy;
    }

    public File getTaxonomy_tsv() {
        return taxonomy_tsv;
    }

    public void setTaxonomy_tsv(File taxonomy_tsv) {
        this.taxonomy_tsv = taxonomy_tsv;
    }

    private Bag bag;
    private File rep;
    private File table;
    private File stats;
    private File taxonomy;
    private File taxonomy_tsv;
    public SraQiime2DTO(SraDTO sra){
        this.sra = sra;
    }
}