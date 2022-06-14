package com.chenyh.netdisc.controller;

import com.chenyh.netdisc.hdfs.HdfsApi;
import com.chenyh.netdisc.hdfs.HdfsApiService;
import com.chenyh.netdisc.hdfs.HdfsOp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;

@CrossOrigin
@RestController
public class HdfsController {
    @Autowired
    private HdfsApiService apiService;
    HdfsApi api = new HdfsApi();

    public HdfsController() throws IOException, URISyntaxException, InterruptedException {
    }

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
    public String upLoadFile(@RequestParam("file") MultipartFile file, @RequestParam("path") String path) throws IOException, URISyntaxException, InterruptedException {
        InputStream inputStream = file.getInputStream();
        String filename = file.getOriginalFilename();
        System.out.println(filename);
        api.upLoadFile(inputStream, path+"/"+filename);
        return filename;
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
    //如果使用HttpServletResponse response参数 那么接口返回了out的数据
    @GetMapping("/download")
    public boolean download(@RequestParam("path") String srcPath, HttpServletResponse response) throws IOException, URISyntaxException, InterruptedException {
        System.out.println(srcPath);
        api.downLoadFile(srcPath, response);
        System.out.println(true);
        return true;
    }
    @PostMapping("/showDir")
    public ArrayList<String> ops(@RequestBody HdfsOp hdfsOp) throws Exception {
        ArrayList<String> list = apiService.showDir(api, hdfsOp);
        System.out.println(list);
        return list;
    }
    @PostMapping("/file/delete")
    public boolean delete(@RequestBody HdfsOp hdfsOp) throws Exception {
        apiService.delete(api, hdfsOp);
        return true;
    }
    @PostMapping("/file/mkdir")
    public boolean mkdir(@RequestBody HdfsOp hdfsOp) throws Exception {
        apiService.create(api, hdfsOp);
        return true;
    }
}
