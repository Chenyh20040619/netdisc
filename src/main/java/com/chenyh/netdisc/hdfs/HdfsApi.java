package com.chenyh.netdisc.hdfs;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletResponse;

public class HdfsApi {
    static FileSystem fs;
    static {
        Configuration conf = new Configuration();
        try {
            conf.set("dfs.client.use.datanode.hostname", "true");
            URI uri = new URI("hdfs://8.130.21.241:9000");
            String user = "root";
            fs = FileSystem.get(uri, conf, user);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public HdfsApi() throws IOException, InterruptedException, URISyntaxException {
    }

    /**
     * 创建文件目录
     * @param destPath
     * @return
     * @throws IOException
     */
    public Boolean mkdir(String destPath) throws IOException {
        boolean b = fs.mkdirs(new Path(destPath));
        return b;
    }



    /**
     *创建文件
     * @param destPath Hdfs目录
     * @return 文件流可追加
     * @throws IOException
     */
    public FSDataOutputStream createFile(String destPath) throws IOException {
        return fs.create(new Path(destPath));
    }

    /**
     * 追加已存在的文件
     * @param destPath 文件路径
     * @return 文件流可追加
     * @throws IOException
     */
    public FSDataOutputStream appendFile(String destPath) throws IOException {
        return fs.append(new Path(destPath));
    }

    /**
     * 删除目录文件
     * @param destPath 文件路径
     * @param recursive 是否递归删除
     * @return
     * @throws IOException
     */
    public Boolean rmdir(String destPath, Boolean recursive) throws IOException {
        return fs.delete(new Path(destPath), recursive);
    }

    /**
     * 上传文件
     * @param localPath 本地目录
     * @param destPath  Hdfs路径
     * @throws IOException
     */
    public void upLoadFile(String localPath, String destPath) throws IOException {
        fs.copyFromLocalFile(false, true, new Path(localPath), new Path(destPath));
    }

    /**
     * 按字节流从客户端拉取文件到服务器
     * @param in
     * @param destPath 目的文件路径
     */
    public void upLoadFile(InputStream in, String destPath) throws IOException {
        Path path = new Path(destPath);
        System.out.println(path);
        FSDataOutputStream os = fs.create(path);
        System.out.println("传输字节");
        byte[] bytes = new byte[1024];
        int len;
        while ((len = in.read(bytes)) > 0){
            System.out.println(Arrays.toString(bytes));
            System.out.println(len);
            os.write(bytes, 0 ,len);
        }
        os.flush(); //文件小于8k
        os.close();
    }

    /**
     * 从服务器下载文件到客户端
     * @param srcFile
     * @param destFile
     * @throws IOException
     */
    public void downLoadFile(String srcFile, String destFile) throws IOException {
        fs.copyToLocalFile(new Path(srcFile), new Path(destFile));
    }

    /**
     * 从服务器上读取文件到本地
     * @param srcFile 源路径
     * @param response
     * @throws UnsupportedEncodingException
     */
    public boolean downLoadFile(String srcFile, HttpServletResponse response) throws IOException {
        Path sPath = new Path(srcFile);
//        String fileName = srcFile.substring(srcFile.lastIndexOf("/") + 1);
//        response.setContentType(new MimetypesFileTypeMap().getContentType(new File(fileName)));
//        response.setHeader("Content-Disposition",
//                "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        FSDataInputStream is = fs.open(sPath);
        byte[] data = new byte[1024];
        OutputStream out = response.getOutputStream();
        while(is.read(data) != -1){
            out.write(data);
            System.out.println(data+"here");
        }
        out.flush();
        is.close();
        out.close();
        return true;
    }

    /**
     * 查找某个文件在 HDFS集群的位置【文件块的信息】
     *
     * @param filePath
     * @return BlockLocation[]
     */
    public BlockLocation[] getFileBlockLocations(String filePath) {

        Path path = new Path(filePath);
        // 文件块位置列表
        BlockLocation[] blkLocations = new BlockLocation[0];
        try {
            // 获取文件目录
            FileStatus filestatus = fs.getFileStatus(path);
            // 获取文件块位置列表
            blkLocations = fs.getFileBlockLocations(filestatus, 0, filestatus.getLen());
            for (BlockLocation blockLocation : blkLocations) {
                long length = blockLocation.getLength();
                System.out.println("文件块的长度[" + length + "/1024 = 文件的大小" + (length / 1024d) + "kb]：" + length);
                System.out.println("文件块的偏移量：" + blockLocation.getOffset());
                List<String> nodes = new ArrayList<>();
                for (String hostName : blockLocation.getHosts()) {
                    nodes.add(hostName);
                }
                System.out.println("文件块存储的主机（DataNode）列表（主机名）：" + nodes);
            }
        } catch (IOException e) {
            System.err.println(e);
        }
        return blkLocations;
    }

    /**
     * 文件更名
     * @param srcPath 源文件
     * @param destPath 目的文件
     * @throws IOException
     * @return
     */
    public boolean rename(String srcPath, String destPath) throws IOException {
        boolean b = fs.rename(new Path(srcPath), new Path(destPath));
        System.out.println(srcPath + " rename to " + destPath + ",成功");
        return b;
    }

    /**
     * 判断路径是否存在
     * @param path
     * @return
     * @throws IOException
     */
    public Boolean exists(String path) throws IOException {
        return fs.exists(new Path(path));
    }

    /**
     * 判断目录是否存在
     * @param dirPath 目录
     * @param create 不存在是否创建
     * @return
     * @throws IOException
     */
    public Boolean existDir(String dirPath, Boolean create) throws IOException {
        Path path = new Path(dirPath);
        Boolean flag = false;
        if (create){
            if (! fs.exists(path)){
                fs.create(path);
            }
            if (fs.isDirectory(path)){
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 判断文件是否存在
     * @param path 文件路径
     * @return
     * @throws IOException
     */
    public Boolean existFile(String path) throws IOException {
        Boolean flag = false;
        Path sPath = new Path(path);
        if (fs.isFile(sPath)){
            flag = true;
        }
        return flag;
    }

    /**
     * 打开文件
     * @param srcPath 文件路径
     * @return 返回输入流
     * @throws IOException
     */
    public FSDataInputStream open(String srcPath) throws IOException {
        return fs.open(new Path(srcPath));
    }

    /**
     * 更改文件路径
     * @param srcPath
     * @param destPath
     * @return
     * @throws IOException
     */
    public Boolean move(String srcPath, String destPath) throws IOException {
        boolean b = fs.rename(new Path(srcPath), new Path(destPath));
        return b;
    }

    /**
     * 文件里面写内容 == 适用于创建文本文件并写入内容
     * @param srcPath 文件路径
     * @param content 内容
     * @throws IOException
     */
    public void putStringToFile(String srcPath, String content) throws IOException {
        FSDataOutputStream stream = createFile(srcPath);
        stream.write(content.getBytes());
        stream.close();
    }

    /**
     * 文件里面些内容 == 适用于已存在文件
     * @param srcPath 文件路径
     * @param content 内容
     * @throws IOException
     */
    public void appendStringToFile(String srcPath, String content) throws IOException {
        FSDataOutputStream stream = appendFile(srcPath);
        stream.write(content.getBytes());
        stream.close();
    }

    /**
     * 读取文件并以字符串形式返回
     * @param srcPath 文件路径
     * @return
     * @throws IOException
     */
    public String readFileToString(String srcPath) throws IOException {
        FSDataInputStream stream = open(srcPath);
        return IOUtils.toString(stream);
    }

    /**
     * 字节大小转文件大小GB、MB、KB
     *
     * @param size
     * @return
     */
    public String getByteToSize(long size) {

        StringBuffer bytes = new StringBuffer();
        // 保留两位有效数字
        DecimalFormat format = new DecimalFormat("###.00");
        if (size >= 1024 * 1024 * 1024) {
            double i = (size / (1024.0 * 1024.0 * 1024.0));
            bytes.append(format.format(i)).append("GB");
        } else if (size >= 1024 * 1024) {
            double i = (size / (1024.0 * 1024.0));
            bytes.append(format.format(i)).append("MB");
        } else if (size >= 1024) {
            double i = (size / (1024.0));
            bytes.append(format.format(i)).append("KB");
        } else if (size < 1024) {
            if (size <= 0) {
                bytes.append("0B");
            } else {
                bytes.append((int) size).append("B");
            }
        }
        return bytes.toString();
    }

    /**
     * 释放fs
     *
     * @throws IOException
     */
    public void close() throws IOException {
        fs.close();
    }
    public static void main(String[] args) throws Exception {
        HdfsApi hdfsApi = new HdfsApi();
        hdfsApi.upLoadFile("E:\\test.txt", "/scda");

//        hdfsApi.createFile("/vs");
//        hdfsApi.mkdir("/test");
    }
}
