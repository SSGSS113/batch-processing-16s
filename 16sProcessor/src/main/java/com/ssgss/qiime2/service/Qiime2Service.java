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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class Qiime2Service {
    public static boolean doImport(SraQiime2DTO sra) throws SraException {
        sra.setDemux(new File(Qiime2FileConstant.DEMUX_PATH, String.format("%s_demux.qza", sra.getSra().getSraId())));
        getManifest(sra);
        if(sra.getDemux().exists()){
            log.info("{} 已经存在", sra.getDemux());
            return true;
        }
        log.info("Sra: {} 的 DemuxPath = {}", sra.getSra().getSraId(), sra.getDemux().getPath());
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
        sra.setTaxonomy(new File(Qiime2FileConstant.TAXONOMY_PATH,
                String.format("%s_taxonomy.qza", sra.getSra().getSraId())));
        sra.setTaxonomy_tsv(new File(Qiime2FileConstant.TAXONOMY_TSV_PATH,
                String.format("%s_taxonomy.tsv", sra.getSra().getSraId())));
        log.info("Sra : {} , rep = {}, table = {}, stats = {}", sra.getSra().getSraId(),
                sra.getRep(), sra.getTable(), sra.getStats());
        if(sra.getRep().exists() && sra.getStats().exists() && sra.getTable().exists()){
            log.info("Sra : {} 的 rep、table、stats均已存在", sra.getSra().getSraId());
        }
        Command command = sra.getSra().isPaired()? PairedData2CommandFactory.getCommand(sra)
                : SingleData2CommandFactory.getCommand(sra);
        Result result = command.execute();
        if(!isSuccess(result)){
            String message = String.format("QIIME2: %s 的 doDenoise 命令失败", sra.getSra().getSraId());
            log.error(message);
        }
        log.info("Sra: {} 的 denois.tsv 文件路径为: {}",sra.getSra().getSraId(),
                Qiime2FileConstant.DENOISE_TSV_PATH);
        doExport(sra, sra.getDemux(), new File(Qiime2FileConstant.DENOISE_TSV_PATH,
                String.format("%s_denoise.tsv", sra.getSra().getSraId())));
        return result.isSucess();
    }

    public static boolean doClassify(SraQiime2DTO sra) throws SraException{
        sra.setTaxonomy(new File(Qiime2FileConstant.TAXONOMY_PATH,
                String.format("%s_taxonomy.qza", sra.getSra().getSraId())));
        if(sra.getTaxonomy().exists()){
            log.info("Sra: {} 的 Taxonomy 文件已经存在: {}", sra.getSra().getSraId(), sra.getTaxonomy().getPath());
        }
        if(!sra.getTable().exists() || !sra.getStats().exists() || !sra.getRep().exists()){
            log.error("{} taxonomy 步骤欠缺前序文件", sra.getSra().getSraId());
            throw new SraException("欠缺table、stats、rep文件");
        }
        Command command = TaxonomyCommandFactory.getCommand(sra);
        Result result = command.execute();
        sra.setTaxonomy_tsv(new File(Qiime2FileConstant.TAXONOMY_TSV_PATH,
                String.format("%s_taxonomy.tsv", sra.getSra().getSraId())));
        if(!isSuccess(result)){
            String message = String.format("QIIME2: %s 的 doClassify 命令失败", sra.getSra().getSraId());
            log.error(message);
            throw new SraException(message);
        }
        log.info("Sra: {} 的 taxonomy.tsv 文件路径为: {}",sra.getSra().getSraId(),
                sra.getTaxonomy_tsv().getPath());
        doExport(sra, sra.getTaxonomy(), sra.getTaxonomy_tsv());
        return result.isSucess();
    }

    public static boolean doAlpha(SraQiime2DTO sra) throws SraException {
        if(!sra.getTable().exists()){
            log.error("table 文件不存在");
            throw new SraException(String.format("%s 处理 alpha 多样性过程中 {} 文件不存在",
                    sra.getSra().getSraId(), sra.getTable().getPath()));
        }
        boolean ans = true;
        try {
            if(!CSVUtil.findValueByHeader(Qiime2FileConstant.ALPHA_OUTPUT, sra.getSra().getSraId(),"SraId")) {
                CSVUtil.addValueByHeader(Qiime2FileConstant.ALPHA_OUTPUT,
                        sra.getSra().getSraId(), "SraId",',');
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for(AlphaConstant alpha : Qiime2Constant.alphaList){
            String type = alpha.getType();
            File outputPath = null;
            File tsvPath = null;
            if(CSVUtil.getValueFromCSV(Qiime2FileConstant.ALPHA_OUTPUT.getPath(),
                    "SraId", sra.getSra().getSraId(), type) != null){
                log.info("Alpha 多样性的 {} 已存在", type);
                continue;
            }
            switch (type) {
                case "shannon":
                    outputPath = new File(Qiime2FileConstant.SHANNON_PATH,
                            String.format("%s_%s.qza", sra.getSra().getSraId(), type));
                    tsvPath = new File(Qiime2FileConstant.SHANNON_TSV_PATH,
                            sra.getSra().getSraId());
                    break;
                case "observed_features":
                    outputPath = new File(Qiime2FileConstant.OTU_PATH,
                            String.format("%s_%s.qza", sra.getSra().getSraId(), type));
                    tsvPath = new File(Qiime2FileConstant.OUT_TSV_PATH,
                            sra.getSra().getSraId());
                    break;
                case "chao1":
                    outputPath = new File(Qiime2FileConstant.CHAO1_PATH,
                            String.format("%s_%s.qza", sra.getSra().getSraId(), type));
                    tsvPath = new File(Qiime2FileConstant.CHAO1_TSV_PATH,
                            sra.getSra().getSraId());
                    break;
                case "simpson":
                    outputPath = new File(Qiime2FileConstant.SIMPSON_PATH,
                            String.format("%s_%s.qza", sra.getSra().getSraId(), type));
                    tsvPath = new File(Qiime2FileConstant.SIMPSON_TSV_PATH,
                            sra.getSra().getSraId());
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
            File alpha_diversity = new File(tsvPath, "alpha-diversity.tsv");
            ans = ans && result.isSucess();
            if(!alpha_diversity.exists()){
                doExport(sra, outputPath, tsvPath);
            }
            String value = getAlphaValue(alpha_diversity);
            log.info("type: {}, 读取 alpha_path = {}, value = {}", type, alpha_diversity.getPath(), value);
            try {
                CSVUtil.modifyCSV(Qiime2FileConstant.ALPHA_OUTPUT.getPath(),
                        "SraId", sra.getSra().getSraId(),
                        alpha.getType(), value);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return ans;
    }

    private static String getAlphaValue(File file){
        log.info("读取 {}", file.getPath());
        try (BufferedReader resultFile = new BufferedReader(new FileReader(file))) {
            resultFile.readLine(); // 跳过标题行
            String line = resultFile.readLine(); // 读取下一行
            if (line != null) {
                String[] values = line.trim().split("\t");
                if (values.length > 1) {
                    return values[1]; // 获取指定列的值
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
            CSVUtil.createCSV(manifest.getPath(), headers, '\t');
        } catch (IOException e) {
            throw new SraException(String.format("%s 创建Manifest表格出错", sra.getSra().getSraId()), e);
        }
        try {
            CSVUtil.addDataToCSV(manifest.getPath(), line, '\t');
        } catch (IOException e) {
            throw new SraException(String.format("%s Manifest表格添加数据错误", sra.getSra().getSraId()), e);
        }
        sra.setManifest(manifest);
    }

    private static void doExport(SraQiime2DTO sra, File inputPath, File outputPath) throws SraException{
        log.info("doExport 命令: Sra = {}, inputPath = {}, outputPath = {}", sra.getSra().getSraId(),
                inputPath, outputPath);
        Command command = ExportCommandFactory.getCommand(sra, inputPath.getPath(), outputPath.getPath());
        if(!isEmpty(outputPath)){
            log.info("Export 输出目录文件已存在: {}", outputPath);
            return;
        }
        Result result = command.execute();
        if(!isSuccess(result)){
            String message = String.format("QIIME2: %s 的doExprot命令失败，inputPath = %s",
                    sra.getSra().getSraId(), inputPath);
            log.error(message);
        }
    }
    private static boolean isSuccess(Result result){
        return result != null && result.isSucess();
    }
    private static boolean isEmpty(File file){
        if(file != null && file.isDirectory()){
            File[] filse = file.listFiles();
            return filse == null || filse.length <= 0;
        }else{
            return true;
        }
    }
}
