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

@CrossOrigin
@RestController
@RequestMapping("/netdisc")
public class HdfsController {
    @Autowired
    private HdfsApiService apiService;

    /**
     * 上传文件
     * @param file
     * @param destPath
     * @return
     * @throws IOException
     * @throws URISyntaxException
     * @throws InterruptedException
     */
    @PostMapping("/upLoad")
    public String upLoadFile(@RequestParam("file") MultipartFile file, @RequestParam("destPath") String destPath) throws IOException, URISyntaxException, InterruptedException {
        HdfsApi api = new HdfsApi();
        InputStream inputStream = file.getInputStream();
        String filename = file.getOriginalFilename();
        api.upLoadFile(inputStream, destPath+"/"+filename);
        api.close();
        return "OK!";
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
    public String download(@RequestParam("srcPath") String srcPath, HttpServletResponse response) throws IOException, URISyntaxException, InterruptedException {
        HdfsApi api = new HdfsApi();
        api.downLoadFile(srcPath, response);
        api.close();
        return "OK!";
    }
}
