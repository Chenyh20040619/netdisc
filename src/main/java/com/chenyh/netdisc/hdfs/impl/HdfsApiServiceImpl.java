package com.chenyh.netdisc.hdfs.impl;

import com.chenyh.netdisc.hdfs.HdfsApi;
import com.chenyh.netdisc.hdfs.HdfsApiException;
import com.chenyh.netdisc.hdfs.HdfsApiService;
import com.chenyh.netdisc.hdfs.HdfsOp;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSInputStream;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.ArrayList;

@Service
public class HdfsApiServiceImpl implements HdfsApiService {
    @Override
    public boolean create(HdfsApi api, HdfsOp hdfsOp) throws Exception {
        String srcPath = hdfsOp.getSrcPath();
        if (StringUtils.isBlank(srcPath)) {
            throw new HdfsApiException(
                    "Path does not exist on HDFS or WebHDFS is disabled. Please check your path or enable WebHDFS");
        }

        String content = hdfsOp.getContent();
        // 如果内容空，直接创建文件或目录
        if (StringUtils.isBlank(content)) {
            api.mkdir(srcPath);
        } else {// 否则，创建文件的同时，写入内容
            api.putStringToFile(srcPath, content);
        }
        return true;
    }

    @Override
    public boolean delete(HdfsApi api, HdfsOp hdfsOp) throws Exception {
        String srcPath = hdfsOp.getSrcPath();
        if (StringUtils.isBlank(srcPath)) {
            throw new HdfsApiException(
                    "Path does not exist on HDFS or WebHDFS is disabled. Please check your path or enable WebHDFS");
        }
        Boolean recursive = hdfsOp.getRecursive();
        return api.rmdir(srcPath, recursive);
    }

    @Override
    public boolean rename(HdfsApi api, HdfsOp hdfsOp) throws Exception {
        boolean result = true;
        String srcPath = hdfsOp.getSrcPath();
        if (StringUtils.isBlank(srcPath)) {
            throw new HdfsApiException(
                    "Path does not exist on HDFS or WebHDFS is disabled. Please check your path or enable WebHDFS");
        }

        String destPath = hdfsOp.getDestPath();
        if (StringUtils.isBlank(destPath)) {
            destPath = srcPath;
        }
        result = api.rename(srcPath, destPath);

        if (result) {
            return true;
        } else {
            throw new HdfsApiException(
                    "Path does not exist on HDFS or WebHDFS is disabled. Please check your path or enable WebHDFS");
        }
    }

    @Override
    public boolean move(HdfsApi api, HdfsOp hdfsOp) throws Exception {
        String srcPath = hdfsOp.getSrcPath();
        if (StringUtils.isBlank(srcPath)) {
            throw new HdfsApiException(
                    "Path does not exist on HDFS or WebHDFS is disabled. Please check your path or enable WebHDFS");
        }

        String destPath = hdfsOp.getDestPath();
        if (StringUtils.isBlank(destPath)) {
            destPath = "";
        }

        api.move(srcPath, destPath);

        return true;
    }

    @Override
    public void open(HdfsApi api, HdfsOp hdfsOp, HttpServletResponse response) throws Exception {
        String path = hdfsOp.getSrcPath();
        if (StringUtils.isBlank(path)){
            throw new HdfsApiException(
                    "Path does not exist on HDFS or WebHDFS is disabled. Please check your path or enable WebHDFS"
            );
        }
        if (! api.exists(path)){
            throw new HdfsApiException(
                    "Path does not exist on HDFS or WebHDFS is disabled. Please check your path or enable WebHDFS"
            );
        }
        if (! api.existFile(path)){
            throw new HdfsApiException(
                    "The path is not a file so it can not open "
            );
        }
        FSDataInputStream in = null;
        OutputStream out = null;

        try {
            in = api.open(path);
            out = response.getOutputStream();

            if (getSuffixName(path).equals("jpg")){
                response.setContentType("image/png");
            }
            byte[] data = new byte[in.available()];
            while (in.read(data) != -1){
                out.write(data);
                out.flush();
                out.close();
                in.close();
            }
        }catch (Exception e){
            throw new HdfsApiException("The file read error or no data available in it  ");
        }
    }
    public String getSuffixName(String path) {
        return path.substring(path.lastIndexOf(".") + 1);
    }

    @Override
    public boolean write(HdfsApi api, HdfsOp hdfsOp) throws Exception {
        String content = hdfsOp.getContent();
        String srcPath = hdfsOp.getSrcPath();
        if (StringUtils.isBlank(srcPath)){
            throw new HdfsApiException(
                    "Path does not exist on HDFS or WebHDFS is disabled. Please check your path or enable WebHDFS"
            );
        }
        if (StringUtils.isBlank(content)){
            throw new HdfsApiException(
                    "The written content is empty and the operation terminates "
            );
        }
        api.putStringToFile(srcPath, content);
        return true;
    }

    @Override
    public boolean append(HdfsApi api, HdfsOp hdfsOp) throws Exception {
        String srcPath = hdfsOp.getSrcPath();
        if (StringUtils.isBlank(srcPath)){
            throw new HdfsApiException(
                    "Path does not exist on HDFS or WebHDFS is disabled. Please check your path or enable WebHDFS"
            );
        }
        if (!api.existFile(srcPath)) {
            throw new HdfsApiException("The path is not a file so it can not open ");
        }

        String content = hdfsOp.getContent();
        if (StringUtils.isBlank(content)) {
            throw new HdfsApiException("The written content is empty and the operation terminates ");
        }
        api.appendStringToFile(srcPath, content);
        return true;
    }

    @Override
    public boolean downLoad(HdfsApi api, HdfsOp hdfsOp) throws Exception {
        String srcPath = hdfsOp.getSrcPath();
        if (StringUtils.isBlank(srcPath)) {
            throw new HdfsApiException(
                    "Src Path does not exist on HDFS or WebHDFS is disabled. Please check your path or enable WebHDFS");
        }
        String destPath = hdfsOp.getDestPath();
        if (StringUtils.isBlank(destPath)) {
            throw new HdfsApiException(
                    "Dest Path does not exist on HDFS or WebHDFS is disabled. Please check your path or enable WebHDFS");
        }
        api.downLoadFile(srcPath, destPath);
        return true;
    }

    @Override
    public ArrayList<String> showDir(HdfsApi api, HdfsOp hdfsOp) throws Exception {
        String srcPath = hdfsOp.getSrcPath();
        ArrayList<String> dirList = api.showDir(srcPath);
        return dirList;
    }

    @Override
    public boolean upLoad(HdfsApi api, HdfsOp hdfsOp) throws Exception {
        String srcPath = hdfsOp.getSrcPath();
        if (StringUtils.isBlank(srcPath)) {
            throw new HdfsApiException(
                    "Src Path does not exist on HDFS or WebHDFS is disabled. Please check your path or enable WebHDFS");
        }
        String destPath = hdfsOp.getDestPath();
        if (StringUtils.isBlank(srcPath)) {
            throw new HdfsApiException(
                    "Dest Path does not exist on HDFS or WebHDFS is disabled. Please check your path or enable WebHDFS");
        }

        api.existDir(destPath, true);
        api.upLoadFile(srcPath, destPath);
        return true;
    }
}
