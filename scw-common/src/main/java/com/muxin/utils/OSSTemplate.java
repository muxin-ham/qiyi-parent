package com.muxin.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.Data;
import lombok.ToString;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * OSS文件上传工具类
 */
@Data
@ToString
public class OSSTemplate {
    private String endpoint;
    private String bucketDomain;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

    public String upload(InputStream stream,String fileName){
        /*1、加工文件夹和文件名*/
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String folderName = simpleDateFormat.format(new Date());
        fileName = UUID.randomUUID().toString().replace("-","")+"_"+fileName;
        /*2、创建OSSClient实例*/
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        /*3、上传文件流，指定bucket的名称*/
        ossClient.putObject(bucketName,"pic/"+folderName+"/"+fileName,stream);
        /*4、关闭资源*/
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ossClient.shutdown();
        String url= "https://"+bucketDomain+"/pic/"+folderName+"/"+fileName;
        System.out.println("上传文件访问路径:"+url);
        return url;
    }
}