package com.chenyh.netdisc.controller;

import com.chenyh.netdisc.hdfs.HdfsApi;
import com.chenyh.netdisc.hdfs.HdfsApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Arrays;

@CrossOrigin
@RestController
public class HdfsController {
    @Autowired
    private HdfsApiService apiService;

    /**
     * 上传文件
     * @param file
     * @param path
     * @return
     * @throws IOException
     * @throws URISyntaxException
     * @throws InterruptedException
     */
    @PostMapping("/upload")
    public boolean upLoadFile(@RequestParam("file") MultipartFile file, @RequestParam("path") String path) throws IOException, URISyntaxException, InterruptedException {
        HdfsApi api = new HdfsApi();
        InputStream inputStream = file.getInputStream();
        String filename = file.getOriginalFilename();
        System.out.println(filename);
        api.upLoadFile(inputStream, "/"+path+"/"+filename);
        return true;
    }

    /**
     * 下载文件
     * @param srcPath
     * @param response
     * @return
     * @throws IOException
     * @throws URISyntaxException
     * @throws InterruptedException
     */
    @GetMapping("/download")
    public boolean download(@RequestParam("path") String srcPath, HttpServletResponse response) throws IOException, URISyntaxException, InterruptedException {
        HdfsApi api = new HdfsApi();
        System.out.println(srcPath);
        api.downLoadFile("/" + srcPath, response);
        return true;
    }
}
