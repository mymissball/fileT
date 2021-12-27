package com.example.demo.bookStore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookStore {

    public void sendHeader(){
        conn.setRequestProperty("user-agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36");
    }

    public static URLConnection conn;
    public void getConnection(String url){

        try {
            URL realUrl=new URL(url);
            conn=realUrl.openConnection();
            sendHeader();
        } catch (MalformedURLException e) {
            System.out.println("URL转换失败"+e);
        } catch (IOException e) {
            System.out.println("conn创建失败"+e.getMessage());
        }


    }


    public String readContext() throws IOException {
        System.out.println("当前encoder:"+conn.getHeaderFields());

        List<String> encoder=conn.getHeaderFields().get("Content-Type");

        String realCoder ="utf-8";
        if(encoder!=null){
            realCoder=encoder.get(0).split("=")[1];
       
        }



        BufferedReader br=new BufferedReader( new InputStreamReader( conn.getInputStream(),realCoder));
        String temp=null;
        StringBuilder builder=new StringBuilder();

        while((temp=br.readLine())!=null){

            builder.append(temp+"\n");
        }
        return builder.toString();
    }

    String regex="id=\"mainleft\"(\\s*|.*)*<p>(.*?)</p>(\\s*|.*)*src=\"(.*?)\"";//<p>(.*?)</p>

    void parseContext(String context){
        Pattern pattern=Pattern.compile(regex);
        Matcher matcher=pattern.matcher(context);

        if(matcher.find()){
            System.out.println("***********"+matcher.group(1)+"================="+matcher.group(3)+"================================\n匹配成功:"+matcher.group(2));
        }

    }


    public static void main(String[] args) throws IOException {
        String url="https://www.wendangwang.com/doc/d1908c230377ca47b4d148ee/5";
        BookStore bs=new BookStore();
        bs.getConnection(url);
        String context=bs.readContext();
        System.out.println("context:"+context);
        bs.parseContext(context);
    }

}
