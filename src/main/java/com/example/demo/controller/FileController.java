package com.example.demo.controller;

import com.example.demo.entity.ResultJson;
import com.example.demo.os.OSInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class FileController {


    String basePath="d:/shared";

    @RequestMapping("getFiles")
    public ResultJson<List<String>> getFile(HttpServletResponse resp, HttpServletRequest request){
        String osName= OSInfo.getOSname().toString().toLowerCase();
        System.out.println("操作系统-getFile:"+osName);
        if(!"windows".equals(osName)){
            basePath="/usr/local/upload/";
        }

        resp.setCharacterEncoding("utf-8");
        String path=request.getParameter("path");
        System.out.println("getFileController...."+path);
        path=(path==null||path.equals("undefined")||path.equals("/"))?basePath:basePath+path;

        if((basePath+"/test/").equals(path)){
            return new ResultJson<List<String>>(600,null,"需要权限，请输入密码:");
        }
        return new ResultJson<List<String>>(200,traverFile(path));

    }


    String pass="112266";
    @RequestMapping("checkCode")
    public ResultJson<List<String>> checkEncrypt(String encrypt, String path){
        System.out.println("checkCript:"+encrypt+"__path:"+path);
        if(!pass.equals(encrypt))
            return new ResultJson<List<String>>(600,null,"密码错误!!!");

        path=basePath+path;
        return new ResultJson<List<String>>(200,traverFile(path));
    }

    @RequestMapping("download")
    public ResultJson<String> down(String filePath,HttpServletResponse response) throws IOException {
        System.out.println(basePath+" down....."+filePath);

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

        return new ResultJson<>(200,file.getName()+" downloaded");
    }

    private List<String> traverFile(String filePath){
        File root=new File(filePath);
        File[]files=root.listFiles();
        List<String> list=new ArrayList<>();

        for(File file:files){
            String fileName=file.getName();
            UploadController.fileNames.add(fileName);
            if(file.isDirectory()){
                list.add(fileName+"/");
            }else
                list.add(fileName);
        }
        System.out.println(list);
        return list;
    }

}
