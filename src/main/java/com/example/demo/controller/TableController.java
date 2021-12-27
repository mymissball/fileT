package com.example.demo.controller;

import com.example.demo.entity.ResultJson;
import com.example.demo.os.OSInfo;
import com.example.demo.t.TableT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@Controller
@ResponseBody
public class TableController {

    String basePath="";

    @Autowired
    private TableT serviceTable;
    @RequestMapping("/getFile")
    public ResultJson getFile(String path)  {
        String osName= OSInfo.getOSname().toString();
        System.out.println("操作系统:"+osName);
        if(!"windows".equals(osName)){
            basePath="/usr/local/upload/";
        }
        System.out.println(path);
        String filePath="D:\\excel\\设备维修记录表 - 12 - 副本 - 副本.xlsx";
        if(path!=null)
            filePath=path;
        return  new ResultJson(0,serviceTable.writeObj(filePath),"文件获取成功！路径:"+filePath);
    }
    @RequestMapping("/upload")
    public ResultJson upload(@RequestParam("file") MultipartFile file, HttpServletRequest request)  {

        System.out.println("tableT:"+serviceTable+" file:"+file);
        System.out.println(file.getName()+"_"+file.isEmpty()+"__"+file.getOriginalFilename());

        String contentType=file.getContentType();
        String fileName=file.getOriginalFilename();
        String filePath=request.getServletContext().getRealPath("/upload/");


        File file2=new File(filePath,fileName);
        if(!file2.exists()){
            file2.mkdirs();
        }
        try {
            file.transferTo(file2);
        } catch (IOException e) {
            return  new ResultJson(400,e.getMessage(),"文件上传失败！！！"+filePath);
        }
//        FileOutputStream fos=null;
//        try {
//            fos=new FileOutputStream(new File(filePath,fileName));
//            fos.write(file.getBytes());
//        }catch (IOException e) {
//            e.printStackTrace();
//        }finally {
//            fos.close();
//        }

        System.out.println(file2.getAbsolutePath());

        return  new ResultJson(0,serviceTable.writeObj(file2.getAbsolutePath()),file2.getAbsolutePath());
    }


    @RequestMapping("/gotoUpload")
    public String gotoUpload(){
        System.out.println("gotoUpload");
        return  "uploadFile";
    }
}
