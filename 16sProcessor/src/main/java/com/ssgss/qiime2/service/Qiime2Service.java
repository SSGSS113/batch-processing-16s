package com.ssgss.qiime2.service;

import com.ssgss.SraToolKit.constant.SraToolKitFileConstant;
import com.ssgss.common.aop.annotation.ProcessTimer;
import com.ssgss.common.command.Command;
import com.ssgss.common.constant.FileConstant;
import com.ssgss.common.constant.SraException;
import com.ssgss.common.entity.Result;
import com.ssgss.common.util.CSVUtil;
import com.ssgss.common.util.FileUtil;
import com.ssgss.qiime2.constant.AlphaConstant;
import com.ssgss.qiime2.constant.Qiime2Constant;
import com.ssgss.qiime2.constant.Qiime2FileConstant;
import com.ssgss.qiime2.entity.AlphaNode;
import com.ssgss.qiime2.entity.SraQiime2DTO;
import com.ssgss.qiime2.factory.*;
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
    @ProcessTimer("Qiime2:doImport")
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
        }
        return result.isSucess();
    }
    @ProcessTimer("Qiime2:getDenoise")
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
        Result result = null;
        if(!sra.getRep().exists() || !sra.getStats().exists() || !sra.getTable().exists()){
            Command command = sra.getSra().isPaired()? PairedData2CommandFactory.getCommand(sra)
                    : SingleData2CommandFactory.getCommand(sra);
            result = command.execute();
            if(!isSuccess(result)){
                String message = String.format("QIIME2: %s 的 doDenoise 命令失败", sra.getSra().getSraId());
                log.error(message);
                return false;
            }
        }else{
            log.info("Sra : {} 的 rep、table、stats均已存在", sra.getSra().getSraId());
            result = new Result.Builder().setSucess(true).setText("文件都存在").build();
        }
        log.info("Sra: {} 的 denois.tsv 文件路径为: {}",sra.getSra().getSraId(),
                Qiime2FileConstant.DENOISE_TSV_PATH);
        File table_biom = new File(Qiime2FileConstant.DENOISE_BIOM_PATH,
                String.format("%s_table.biom", sra.getSra().getSraId()));
        File table_tsv = new File(Qiime2FileConstant.DENOISE_TSV_PATH,
                String.format("%s_table.tsv", sra.getSra().getSraId()));
        sra.setTable_biom(table_biom);
        sra.setTable_tsv(table_tsv);
        if(!table_tsv.exists()) {
            if(!table_biom.exists()){
                File outputPath = new File(Qiime2FileConstant.DENOISE_PATH, sra.getSra().getSraId());
                doExport(sra, sra.getTable(), outputPath);
                File oldPath = new File(outputPath, "feature-table.biom");
                FileUtil.MoveFileTo(oldPath.getPath(), table_biom.getPath());
            }
            Command command = BiomConvertCommandFactory.getCommand(sra);
            Result execute = command.execute();
            if(!execute.isSucess()){
                log.error("biom 转换 tsv 失败");
                return false;
            }else{
                log.info("{} 转换 tsv 成功", sra.getSra().getSraId());
            }
        }
        return result.isSucess();
    }
    @ProcessTimer("Qiime2:doTaxonomy")
    public static boolean doClassify(SraQiime2DTO sra) throws SraException{
        sra.setTaxonomy(new File(Qiime2FileConstant.TAXONOMY_PATH,
                String.format("%s_taxonomy.qza", sra.getSra().getSraId())));
        Result result = null;
        if(!sra.getTaxonomy().exists()){
            if(!sra.getTable().exists() || !sra.getStats().exists() || !sra.getRep().exists()){
                log.error("{} taxonomy 步骤欠缺前序文件", sra.getSra().getSraId());
                throw new SraException("欠缺table、stats、rep文件");
            }
            Command command = TaxonomyCommandFactory.getCommand(sra);
            result = command.execute();
            sra.setTaxonomy_tsv(new File(Qiime2FileConstant.TAXONOMY_TSV_PATH,
                    String.format("%s_taxonomy.tsv", sra.getSra().getSraId())));
            if(!isSuccess(result)){
                String message = String.format("QIIME2: %s 的 doClassify 命令失败", sra.getSra().getSraId());
                log.error(message);
                return false;
            }
        }else{
            log.info("Sra: {} 的 Taxonomy 文件已经存在: {}", sra.getSra().getSraId(), sra.getTaxonomy().getPath());
            result = new Result.Builder().setSucess(true).setText("Taxonomy 文件已经存在").build();
        }
        log.info("Sra: {} 的 taxonomy.tsv 文件路径为: {}",sra.getSra().getSraId(),
                sra.getTaxonomy_tsv().getPath());
        File taxonomy_tsv = new File(Qiime2FileConstant.TAXONOMY_TSV_PATH,
                String.format("%s_taxonomy.tsv", sra.getSra().getSraId()));
        if(!taxonomy_tsv.exists()) {
            File outputPath = new File(Qiime2FileConstant.TAXONOMY_TSV_PATH, sra.getSra().getSraId());
            doExport(sra, sra.getTaxonomy(), outputPath);
            File oldPath = new File(outputPath, "taxonomy.tsv");
            FileUtil.MoveFileTo(oldPath.getPath(), taxonomy_tsv.getPath());
        }
        return result.isSucess();
    }
    @ProcessTimer("Qiime2:getAlpha")
    public static boolean doAlpha(SraQiime2DTO sra) throws SraException {
        if(!sra.getTable().exists()){
            log.error("table 文件不存在");
            throw new SraException(String.format("%s 处理 alpha 多样性过程中 {} 文件不存在",
                    sra.getSra().getSraId(), sra.getTable().getPath()));
        }
        boolean ans = true;
        AlphaNode node = Qiime2Constant.ALPHY_MAP.getOrDefault(sra.getSra().getSraId(), new AlphaNode());
        for(AlphaConstant alpha : Qiime2Constant.alphaList.keySet()){
            String type = alpha.getType();
            File outputPath = null;
            File tsvPath = null;

            if(node.getAlphaMap().get(alpha) >= 0){
                log.info("{} 的 {} α多样性已经存在", sra.getSra().getSraId(), type);
                continue;
            }
            switch (type) {
                case "shannon":
                    outputPath = new File(Qiime2FileConstant.SHANNON_PATH,
                            String.format("%s_%s.qza", sra.getSra().getSraId(), type));
                    tsvPath = Qiime2FileConstant.SHANNON_TSV_PATH;
                    break;
                case "observed_features":
                    outputPath = new File(Qiime2FileConstant.OTU_PATH,
                            String.format("%s_%s.qza", sra.getSra().getSraId(), type));
                    tsvPath = Qiime2FileConstant.OUT_TSV_PATH;
                    break;
                case "chao1":
                    outputPath = new File(Qiime2FileConstant.CHAO1_PATH,
                            String.format("%s_%s.qza", sra.getSra().getSraId(), type));
                    tsvPath = Qiime2FileConstant.CHAO1_TSV_PATH;
                    break;
                case "simpson":
                    outputPath = new File(Qiime2FileConstant.SIMPSON_PATH,
                            String.format("%s_%s.qza", sra.getSra().getSraId(), type));
                    tsvPath = Qiime2FileConstant.SIMPSON_TSV_PATH;
                    break;
            }
            Result result;
            if(!outputPath.exists()){
                Command command = AlphaCommandFactory.getCommand(sra, type, outputPath.getPath());
                result = command.execute();
                if(!isSuccess(result)){
                    String message = String.format("QIIME2: %s 的doAlpha命令失败", sra.getSra().getSraId());
                    log.error(message);
                    return false;
                }
            }else{
                result = new Result.Builder().setSucess(true).setText("已存在").build();
            }
            File tsv_output = new File(tsvPath, sra.getSra().getSraId());
            File alpha_diversity = new File(tsvPath, String.format("%s_alpha-diversity.tsv", sra.getSra().getSraId()));
            ans = ans && result.isSucess();
            if(!alpha_diversity.exists()) {
                doExport(sra, outputPath, tsv_output);
                File oldPath = new File(tsv_output, "alpha-diversity.tsv");
                FileUtil.MoveFileTo(oldPath.getPath(), alpha_diversity.getPath());
            }
            String value = getAlphaValue(alpha_diversity);
            log.info("type: {}, 读取 alpha_path = {}, value = {}", type, alpha_diversity.getPath(), value);
            if(value == null){
                log.error("{} 的 α多样性 {} 的值为空", sra.getSra().getSraId(), type);
            }else{
                node.getAlphaMap().put(alpha, Double.valueOf(value));
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
            log.info("Export 输出目录文件已存在: {}", outputPath.getPath());
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
