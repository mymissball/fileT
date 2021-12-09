package com.example.demo.t;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TMall {
    URLConnection conn=null;
    private void getConn(String urlStr){

        URL url=null;
        try {
            url=new URL(urlStr);
            conn=url.openConnection();
        }  catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void sendHeaders(){
        String agent="Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36";
        conn.setRequestProperty("user-agent",agent);
    }

    private void getRequest(){
        if(conn==null){
            return ;
        }
        sendHeaders();
        Map<String, List<String>> list=conn.getHeaderFields();
        System.out.println(list.size());

        list.forEach((l,map)->{
            System.out.println(l+" "+map);
        });
    }

    private void getCookie(String cookieUrl){
        getConn(cookieUrl);
    }
    public static void main(String[] args) throws IOException {
//        TMall main=new TMall();
//        String url="https://login.taobao.com/jump?group=tao&target=https://list.tmall.com/search_product.htm?tbpm=1&q=iphone";
//        main.getCookie(url);
//        main.getRequest();

        String filePath="D:\\2021TK\\2021-1\\020推力测试";

        getFile(filePath);
    }


    private static void getFile(String filePath) throws IOException {
        long startTime=System.currentTimeMillis();
        File file=new File(filePath);
        if(!file.exists()){
            System.out.println(filePath+"没有找到");
            System.exit(1);
        }

        File[] files=file.listFiles();
        System.out.println("=============================开始时间:"+new Date(startTime)+"================="+filePath+"文件夹内有个"+files.length+"文件=====================================");
        for(File fileT:files){
            System.out.println(fileT);
            BufferedReader br= null;
            try {
                FileInputStream fis=new FileInputStream(fileT);
                 br=new BufferedReader(new InputStreamReader(fis,"gbk"));
                 String temp=null;
                 while((temp=br.readLine())!=null){
                  //   System.out.println(temp);
                     String regex="\"[1-9]?,金球推力测试";   //"4","金球推力测试","020","01003","9L","26.916","焊点 1","OK","lao","上午 10:17:22"
                     Pattern pattern=Pattern.compile(regex);
                     Matcher matcher=pattern.matcher(temp);
                     if(matcher.find()){
                         System.out.println("找到需要更改的数据:"+temp);
                     }
                 }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }finally {
                br.close();
            }
        }

        long endTime=System.currentTimeMillis();
        System.err.println("结束时间‘"+new Date(endTime)+"’用时:"+(endTime-startTime)/1000.0+"S");
    }


}
/*
* "4","金球推力测试","020","01003","9L","26.916","焊点 1","OK","lao","上午 10:17:22"
"5","金球推力测试","020","01003","9L","34.781","焊点 1","OK","lao","上午 10:17:29"
*
* */