package com.example.demo.controller;

import com.example.demo.entity.ResultJson;
import com.example.demo.exception.FileException;
import com.example.demo.exception.SuperException;
import com.example.demo.os.OSInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RestController
public class FileController {


    String basePath="d:/share";

    @RequestMapping("getFiles")
    public ResultJson<List<String>> getFile(HttpServletResponse resp, HttpServletRequest request){
        String osName= OSInfo.getOSname().toString();
        System.out.println("操作系统:"+osName);
        if(!"windows".equals(osName)){
            basePath="/usr/local/upload/";
        }
       
        resp.setCharacterEncoding("utf-8");
        String path=request.getParameter("path");
        System.out.println("getFileController...."+path);
        path=(path==null||path.equals("undefined")||path.equals("/"))?basePath:basePath+path;
        return new ResultJson<List<String>>(200,traverFile(path));

    }

    @RequestMapping("download")
    public ResultJson<String> down(String filePath,HttpServletResponse response) throws IOException {
        System.out.println(basePath+" down....."+filePath);
        BufferedReader br=null;

        File file=new File(basePath+filePath);
        if(!file.exists()){
            return new ResultJson<>(400,"file not found...");
        }

        FileInputStream fis=new FileInputStream(file);
        int length=-1;
        byte[] bys=new byte[1024];

      //  response.setContentType("application/x-download");
        response.addHeader("Content-Disposition","attachment;filename="+new String(file.getName().getBytes("utf-8"),"ISO8859-1"));
         response.setContentLength((int) file.length());
        //response.addHeader("Content-Length",file.length()+"");


        while((length=fis.read(bys))!=-1){
            response.getOutputStream().write(bys,0,length);
        }
    //    fis.close();
//        br=new BufferedReader(new InputStreamReader(fis,"utf-8"));
//        BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(response.getOutputStream()));
//        String temp=null;
//        while((temp=br.readLine())!=null){
//            bw.write(temp);
//        }
//        bw.flush();
//        bw.close();
//        br.close();


        return new ResultJson<>(200,file.getName()+" downloaded");
    }

    private List<String> traverFile(String filePath){
        File root=new File(filePath);
        File[]files=root.listFiles();
        List<String> list=new ArrayList<>();

        for(File file:files){
            if(file.isDirectory()){
                list.add(file.getName()+"/");
            }else
                list.add(file.getName());
        }
        System.out.println(list);
        return list;
    }

}
