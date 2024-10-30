package com.ssgss.qiime2.service;

import com.ssgss.common.command.Command;
import com.ssgss.common.constant.SraException;
import com.ssgss.common.entity.Result;
import com.ssgss.common.util.CSVUtil;
import com.ssgss.qiime2.constant.AlphaConstant;
import com.ssgss.qiime2.constant.Qiime2Constant;
import com.ssgss.qiime2.constant.Qiime2FileConstant;
import com.ssgss.qiime2.entity.SraQiime2DTO;
import com.ssgss.qiime2.factory.AlphaCommandFactory;
import com.ssgss.qiime2.factory.ExportCommandFactory;
import com.ssgss.qiime2.factory.ImportCommandFactory;
import com.ssgss.qiime2.factory.PairedData2CommandFactory;
import com.ssgss.qiime2.factory.SingleData2CommandFactory;
import com.ssgss.qiime2.factory.TaxonomyCommandFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Service
@Slf4j
public class Qiime2Service {
    public static boolean doImport(SraQiime2DTO sra) throws SraException {
        sra.setDemux(new File(Qiime2FileConstant.DEMUX_PATH, String.format("%s_demux.qza", sra.getSra().getSraId())));
        getManifest(sra);
        Command command = ImportCommandFactory.getCommand(sra);
        Result result = command.execute();
        if(!isSuccess(result)){
            String message = String.format("QIIME2: %s 的 doImport 命令失败", sra.getSra().getSraId());
            log.error(message);
            throw new SraException(message);
        }
        return result.isSucess();
    }

    public static boolean doDenoise(SraQiime2DTO sra) throws SraException {
        sra.setRep(new File(Qiime2FileConstant.REP_PATH, String.format("%s_rep_seqs.qza", sra.getSra().getSraId())));
        sra.setTable(new File(Qiime2FileConstant.TABLE_PATH, String.format("%s_table.qza", sra.getSra().getSraId())));
        sra.setStats(new File(Qiime2FileConstant.STATS_PATH, String.format("%s_stats.qza", sra.getSra().getSraId())));
        Command command = sra.getSra().isPaired()? PairedData2CommandFactory.getCommand(sra)
                : SingleData2CommandFactory.getCommand(sra);
        Result result = command.execute();
        if(!isSuccess(result)){
            String message = String.format("QIIME2: %s 的 doDenoise 命令失败", sra.getSra().getSraId());
            log.error(message);
            throw new SraException(message);
        }
        doExport(sra, sra.getDemux(), new File(Qiime2FileConstant.DENOISE_TSV_PATH,
                String.format("%s_denoise.tsv", sra.getSra().getSraId())));
        return result.isSucess();
    }

    public static boolean doClassify(SraQiime2DTO sra) throws SraException{
        sra.setTaxonomy(new File(Qiime2FileConstant.TAXONOMY_PATH,
                String.format("%s_taxonomy.qza", sra.getSra().getSraId())));
        Command command = TaxonomyCommandFactory.getCommand(sra);
        sra.setTaxonomy_tsv(new File(Qiime2FileConstant.TAXONOMY_TSV_PATH,
                String.format("%s_taxonomy.tsv", sra.getSra().getSraId())));
        Result result = command.execute();
        if(!isSuccess(result)){
            String message = String.format("QIIME2: %s 的 doClassify 命令失败", sra.getSra().getSraId());
            log.error(message);
            throw new SraException(message);
        }
        doExport(sra, sra.getTaxonomy(), sra.getTaxonomy_tsv());
        return result.isSucess();
    }

    public static boolean doAlpha(SraQiime2DTO sra) throws SraException{
        boolean ans = true;
        for(AlphaConstant alpha : Qiime2Constant.alphaList){
            String type = alpha.getType();
            File outputPath = null;
            File tsvPath = null;
            switch (type) {
                case "shannon":
                    outputPath = new File(Qiime2FileConstant.SHANNON_PATH,
                            String.format("%s_%s.qza", sra.getSra().getSraId(), type));
                    tsvPath = new File(Qiime2FileConstant.SHANNON_TSV_PATH,
                            String.format("%s_%s.tsv", sra.getSra().getSraId(), type));
                    break;
                case "observed_features":
                    outputPath = new File(Qiime2FileConstant.OTU_PATH,
                            String.format("%s_%s.qza", sra.getSra().getSraId(), type));
                    tsvPath = new File(Qiime2FileConstant.OUT_TSV_PATH,
                            String.format("%s_%s.tsv", sra.getSra().getSraId(), type));
                    break;
                case "chao1":
                    outputPath = new File(Qiime2FileConstant.CHAO1_PATH,
                            String.format("%s_%s.qza", sra.getSra().getSraId(), type));
                    tsvPath = new File(Qiime2FileConstant.CHAO1_TSV_PATH,
                            String.format("%s_%s.tsv", sra.getSra().getSraId(), type));
                    break;
                case "simpson":
                    outputPath = new File(Qiime2FileConstant.SIMPSON_PATH,
                            String.format("%s_%s.qza", sra.getSra().getSraId(), type));
                    tsvPath = new File(Qiime2FileConstant.SIMPSON_TSV_PATH,
                            String.format("%s_%s.tsv", sra.getSra().getSraId(), type));
                    break;
            }
            assert outputPath != null;
            Command command = AlphaCommandFactory.getCommand(sra, type, outputPath.getPath());
            Result result = command.execute();
            if(!isSuccess(result)){
                String message = String.format("QIIME2: %s 的doAlpha命令失败", sra.getSra().getSraId());
                log.error(message);
                throw new SraException(message);
            }
            doExport(sra, outputPath, tsvPath);
            ans = ans && result.isSucess();
        }
        return ans;
    }

    private static void getManifest(SraQiime2DTO sra) throws SraException{
        File manifest = new File(Qiime2FileConstant.MANIFEST_PATH,
                String.format("%s_manifest.csv", sra.getSra().getSraId()));
        List<String> headers = new ArrayList<>();
        headers.add("sample-id");
        List<String> line = new ArrayList<>();
        line.add(sra.getSra().getSraId());
        line.add(sra.getSra().getLeftPath().getPath());
        if(sra.getSra().isPaired()){
            headers.add("forward-absolute-filepath");
            headers.add("reverse-absolute-filepath");
            line.add(sra.getSra().getRightPath().getPath());
        }else{
            headers.add("absolute-filepath");
        }
        try {
            CSVUtil.createCSV(manifest.getPath(), headers);
        } catch (IOException e) {
            throw new SraException(String.format("%s 创建Manifest表格出错", sra.getSra().getSraId()), e);
        }
        try {
            CSVUtil.addDataToCSV(manifest.getPath(), line);
        } catch (IOException e) {
            throw new SraException(String.format("%s Manifest表格添加数据错误", sra.getSra().getSraId()), e);
        }
        sra.setManifest(manifest);
    }

    private static void doExport(SraQiime2DTO sra, File inputPath, File outputPath) throws SraException {
        Command command = ExportCommandFactory.getCommand(sra, inputPath.getPath(), outputPath.getPath());
        if(outputPath.exists()){
            return;
        }
        Result result = command.execute();
        if(!isSuccess(result)){
            String message = String.format("QIIME2: %s 的doExprot命令失败，inputPath = %s",
                    sra.getSra().getSraId(), inputPath);
            log.error(message);
            throw new SraException(message);
        }
    }
    private static boolean isSuccess(Result result){
        return result != null && result.isSucess();
    }
}
