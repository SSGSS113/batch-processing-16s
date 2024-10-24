package com.ssgss.common.service;

import com.ssgss.common.constant.BlockQueueConstant;
import com.ssgss.common.constant.CommonConstant;
import com.ssgss.common.constant.ThreadPoolControl;
import com.ssgss.common.service.pipeline.Performer;
import com.ssgss.common.service.task.*;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class SraService {
    @Resource
    ThreadPoolControl threadPoolControl;
    public void doCSVRead(){
        Thread t1 = new Thread(new CSVReadTask(CommonConstant.CSV_PATH));
        t1.start();
    }
    public void doDownload(){
        new Performer(BlockQueueConstant.SRA_LIST, threadPoolControl.getDownloadPoll(),
                "Download", DownloadTask.class).consume();
    }
    public void doFastqDump(){
        new Performer(BlockQueueConstant.DOWNLOAD_LIST, threadPoolControl.getDumpPool(),
                "Fastq_Dump", FastqDumpTask.class).consume();
    }
    public void doFastqc(){
        new Performer(BlockQueueConstant.FASTQ_DUMP, threadPoolControl.getFastqcPool(),
                "Fastqc", FastqcTask.class).consume();
    }
    public void doImport(){
        new Performer(BlockQueueConstant.FASTQC_LIST, threadPoolControl.getImportPool(),
                "Import", ImportTask.class).consume();
    }
    public void doDenoise(){
        new Performer(BlockQueueConstant.IMPORT_LIST, threadPoolControl.getDenoisePool(),
                "Denoise", DenoiseTask.class).consume();
    }
    public void doTaxonomy(){
        new Performer(BlockQueueConstant.DENOISE_LIST, threadPoolControl.getTaxonomyPool(),
                "Taxonomy", TaxonomyTask.class).consume();
    }
    public void doAlpha(){
        new Performer(BlockQueueConstant.DENOISE_LIST, threadPoolControl.getAlphaPool(),
                "Alpha", AlphaTask.class).consume();
    }
}
