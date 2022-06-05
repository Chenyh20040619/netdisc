package com.chenyh.netdisc.hdfs;

import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

public interface HdfsApiService {
    /**
     *创建文件或目录
     * @param api
     * @param hdfsOp srcPath| srcPath/content
     * @return
     */
    boolean create(HdfsApi api, HdfsOp hdfsOp) throws Exception;

    /**
     * 删除文件或目录
     * @param api
     * @param hdfsOp srcPath
     * @return
     */
    boolean delete(HdfsApi api, HdfsOp hdfsOp) throws Exception;

    /**
     * 重命名文件或目录
     * @param api
     * @param hdfsOp srcPath destPath
     * @return
     */
    boolean rename(HdfsApi api, HdfsOp hdfsOp) throws Exception;

    /**
     * 移动文件或目录
     * @param hdfsOp srcPath destPath
     * @param api
     * @return
     */
    boolean move(HdfsApi api, HdfsOp hdfsOp) throws Exception;

    /**
     * 打开一个文件，读取内容
     * @param api
     * @param hdfsOp  path response
     * @param response
     */
    void open(HdfsApi api, HdfsOp hdfsOp, HttpServletResponse response) throws Exception;

    /**
     * 往文件里写内容， 文件不存在则创建(新文件)
     * @param api
     * @param hdfsOp content srcPath
     * @return
     */
    boolean write(HdfsApi api, HdfsOp hdfsOp) throws Exception;

    /**
     * 往文件追加内容
     * @param api
     * @param hdfsOp srcPath content
     * @return
     */
    boolean append(HdfsApi api, HdfsOp hdfsOp) throws Exception;

    /**
     * 下载文件
     * @param api 本地到服务器
     * @param hdfsOp srcPath destPath
     * @return
     */
    boolean downLoad(HdfsApi api, HdfsOp hdfsOp) throws Exception;

    /**
     * 上传文件
     * @param api
     * @param hdfsOp srcPath destPath 本地到服务器
     * @return
     * @throws Exception
     */
    boolean upLoad(HdfsApi api, HdfsOp hdfsOp) throws Exception;

    /**
     * 显示目录结构
     * @param api
     * @param hdfsOp
     * @return
     * @throws Exception
     */
    ArrayList<String> showDir(HdfsApi api, HdfsOp hdfsOp) throws Exception;
}
