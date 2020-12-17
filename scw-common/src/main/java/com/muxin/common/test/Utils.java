package com.muxin.common.test;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class Utils {
    public static void main(String[] args) throws IOException {
        //1、创建一个httpClient
        HttpClient httpClient = new DefaultHttpClient();
        //2、构造一个请求对象
        HttpGet httpGet = new HttpGet("https://baidu.com");
        //3、发送请求,并接收响应
        HttpResponse response = httpClient.execute(httpGet);
        //4、获取响应的数据
        HttpEntity entity = response.getEntity();
        //5、拿到相应的字符串数据
        String s = EntityUtils.toString(entity);
        //打印结果
        System.out.println(s);
    }
}
