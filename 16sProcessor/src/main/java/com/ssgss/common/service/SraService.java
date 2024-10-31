package com.ssgss.common.service;

import com.ssgss.common.constant.BlockQueueConstant;
import com.ssgss.common.configration.FileConfig;
import com.ssgss.common.constant.FileConstant;
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
        Thread t1 = new Thread(new CSVReadTask(FileConstant.getCsvPath()));
        t1.start();
    }
    public void doDownload(){
        Thread t1 = new Thread(new Performer(BlockQueueConstant.SRA_LIST, threadPoolControl.getDownloadPoll(),
                "Download", DownloadTask.class));
        t1.start();
    }
    public void doFastqDump(){
        new Thread(new Performer(BlockQueueConstant.DOWNLOAD_LIST, threadPoolControl.getDumpPool(),
                "Fastq_Dump", FastqDumpTask.class)).start();
    }
    public void doFastqc(){
        new Thread(new Performer(BlockQueueConstant.FASTQ_DUMP, threadPoolControl.getFastqcPool(),
                "Fastqc", FastqcTask.class)).start();

    }
    public void doImport(){
        new Thread(new Performer(BlockQueueConstant.FASTQC_LIST, threadPoolControl.getImportPool(),
                "Import", ImportTask.class)).start();
    }
    public void doDenoise(){
        new Thread(new Performer(BlockQueueConstant.IMPORT_LIST, threadPoolControl.getDenoisePool(),
                "Denoise", DenoiseTask.class)).start();
    }
    public void doTaxonomy(){
        new Thread(new Performer(BlockQueueConstant.DENOISE_LIST, threadPoolControl.getTaxonomyPool(),
                "Taxonomy", TaxonomyTask.class)).start();
    }
    public void doAlpha(){
        new Thread(new Performer(BlockQueueConstant.DENOISE_LIST, threadPoolControl.getAlphaPool(),
                "Alpha", AlphaTask.class)).start();
    }
}
