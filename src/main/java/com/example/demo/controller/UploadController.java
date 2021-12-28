package com.example.demo.controller;

import com.example.demo.ImageJ;
import com.example.demo.entity.ResultJson;
import com.example.demo.os.OSInfo;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UploadController {

    public static List<String>fileNames=new ArrayList<>();

    static String basePath="d:/shared";

    @RequestMapping("newFile")
    public static ResultJson newFolder(HttpServletRequest request){
        String osName= OSInfo.getOSname().toString().toLowerCase();
        System.out.println("操作系统-newFile:"+osName);
        if(!"windows".equals(osName)){
            basePath="/usr/local/upload/";
        }

        int resultCode=200;
        String fileName=request.getParameter("fileName");
        System.out.println("---------"+fileName+"----------文件创建");
        if(fileName==null||fileName.equals("")){
            resultCode=400;
            return new ResultJson(resultCode,"文件创建失败");
        }
        File file=new File(basePath,fileName);
        String msg="创建成功";
        if(!file.exists()){
            if(!file.mkdir()){
                resultCode=402;
                msg="文件创建失败";
            }
        }else{
            resultCode=401;
            msg="文件'"+fileName+"'已经存在，创建失败";
        }
        System.out.println(file.getAbsolutePath());
        String filePath=file.getParent().substring(basePath.length()-1);
        filePath=filePath.equals("")?"/":filePath;

        System.out.println("filePath:"+filePath);
        return new ResultJson(resultCode,msg,filePath);
    }


    @RequestMapping("api/upload")
    public ResultJson uploadFile( HttpServletRequest request) {
        int resultCode=0;
        String osName= OSInfo.getOSname().toString().toLowerCase();

        if(!"windows".equals(osName)){
            basePath="/usr/local/upload/";
        }
        DiskFileItemFactory factory=new DiskFileItemFactory();
        factory.setRepository(new File(basePath));
        factory.setSizeThreshold(1);
        ServletFileUpload servletFileUpload=new ServletFileUpload(factory);



        List<FileItem>fileItem=new ArrayList<>();
        Map<String,String>pathMap=new HashMap<>();
        try{

            Map<String, List<FileItem>> map=servletFileUpload.parseParameterMap(request);
            System.out.println(map.size()+"_"+map);
            for(Map.Entry<String, List<FileItem>> list:map.entrySet()){

                for(FileItem item:list.getValue()){
                    if(!item.isFormField()){//文件
                        fileItem.add(item);
                    }else{/* 如果是表单控件，则保存其值*/
                        String pathName=item.getFieldName();
                        String pathValue=item.getString("utf-8");
                        pathMap.put(pathName,pathValue);
                        System.out.println( pathName + "-->" + pathValue) ;
                    }

                }
            }

        } catch (Exception e) {
            resultCode=444;
            e.printStackTrace();
        }

        for(FileItem item:fileItem){
            String filePath=basePath+(pathMap.get("path").equals("")?"/":pathMap.get("path"))+item.getName();
            System.out.println("itemName:"+item.getFieldName()+"__"+filePath);
            File temp=new File(filePath);
            try {
                item.write(temp);

                item.delete();
            } catch (Exception e) {
                resultCode=444;
                e.printStackTrace();
            }

            ImageJ.traverFile(temp);
        }

        return new ResultJson(resultCode,"上传成功",System.getProperty("java.io.tmpdir"));
    }
}
