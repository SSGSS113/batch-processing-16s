package com.ssgss.qiime2.service.impl;

import com.ssgss.common.command.Command;
import com.ssgss.common.constant.SraException;
import com.ssgss.common.util.CSVUtil;
import com.ssgss.qiime2.constant.AlphaConstant;
import com.ssgss.qiime2.constant.Qiime2Constant;
import com.ssgss.qiime2.entity.SraQiime2DTO;
import com.ssgss.qiime2.factory.*;
import com.ssgss.qiime2.service.Qiime2Service;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Service
public class Qiime2ServiceImpl implements Qiime2Service {
    @Override
    public boolean doImport(SraQiime2DTO sra) {
        sra.setDemux(new File(Qiime2Constant.DEMUX_PATH, String.format("%s_demux.qza", sra.getSra().getSraId())));
        getManifest(sra);
        Command command = ImportCommandFactory.getCommand(sra);
        try{
            command.execute();
        }catch (SraException e){
            throw new SraException(String.format("QIIME2: %s 的Import命令失败", sra.getSra().getSraId()),e);
        }
        return true;
    }

    @Override
    public boolean doDenoise(SraQiime2DTO sra) {
        sra.setRep(new File(Qiime2Constant.REP_PATH, String.format("%s_rep_seqs.qza", sra.getSra().getSraId())));
        sra.setTable(new File(Qiime2Constant.TABLE_PATH, String.format("%s_table.qza", sra.getSra().getSraId())));
        sra.setStats(new File(Qiime2Constant.STATS_PATH, String.format("%s_stats.qza", sra.getSra().getSraId())));
        Command command = sra.getSra().isPaired()? PairedData2CommandFactory.getCommand(sra)
                : SingleData2CommandFactory.getCommand(sra);
        try{
            command.execute();
        }catch (SraException e) {
            throw new SraException (String.format("QIIME2: %s 的Data2命令失败", sra.getSra().getSraId()), e);
        }
        doExport(sra, sra.getDemux().getPath(), sra.getTaxonomy_tsv().getPath());
        return true;
    }

    @Override
    public boolean doClassify(SraQiime2DTO sra) {
        sra.setTaxonomy(new File(Qiime2Constant.TAXONOMY_PATH,
                String.format("%s_taxonomy.qza", sra.getSra().getSraId())));
        Command command = TaxonomyCommandFactory.getCommand(sra);
        sra.setTaxonomy_tsv(new File(Qiime2Constant.TAXONOMY_TSV_PATH,
                String.format("%s_taxonomy.tsv", sra.getSra().getSraId())));
        try{
            command.execute();
        }catch (SraException e) {
            throw new SraException (String.format("QIIME2: %s 的Classifer命令失败", sra.getSra().getSraId()), e);
        }
        doExport(sra, sra.getTaxonomy().getPath(), sra.getTaxonomy_tsv().getPath());
        return true;
    }

    @Override
    public boolean doAlpha(SraQiime2DTO sra) {
        for(AlphaConstant alpha : Qiime2Constant.alphaList){
            String type = alpha.getType();
            File outputPath = null;
            switch (type){
                case "shannon" -> outputPath = new File(Qiime2Constant.SHANNON_PATH,
                        String.format("%s_%s.qza",sra.getSra().getSraId(),type));
                case "observed_features" -> outputPath = new File(Qiime2Constant.OTU_PATH,
                        String.format("%s_%s.qza",sra.getSra().getSraId(),type));
                case "chao1" -> outputPath = new File(Qiime2Constant.CHAO1_PATH,
                        String.format("%s_%s.qza",sra.getSra().getSraId(),type));
                case "simpson" -> outputPath = new File(Qiime2Constant.SIMPSON_PATH,
                        String.format("%s_%s.qza",sra.getSra().getSraId(),type));
            }
            assert outputPath != null;
            Command command = AlphaCommandFactory.getCommand(sra, type, outputPath.getPath());
            try{
                command.execute();
            }catch (SraException e){
                throw new SraException(String.format("处理%s出现问题",type), e);
            }
        }
        return true;
    }

    private void getManifest(SraQiime2DTO sra){
        File manifest = new File(Qiime2Constant.MANIFEST_PATH,
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

    private void doExport(SraQiime2DTO sra, String inputPath, String outputPath) throws SraException {
        Command command = ExportCommandFactory.getCommand(sra, inputPath, outputPath);
        try{
            command.execute();
        }catch (SraException e){
            throw new SraException(String.format("sra = %s, inputPath = %s, outputPath = %s"
                    , sra.getSra().getSraId(), inputPath, outputPath), e);
        }
    }
}
