package com.example.demo.controller;

import com.example.demo.entity.ResultJson;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@RestController
public class UploadController {


    @RequestMapping("api/upload")
    public ResultJson uploadFile( HttpServletRequest request) {

        DiskFileItemFactory factory=new DiskFileItemFactory();
       // DiskFileItem fileItem=new DiskFileItem();
        factory.setRepository(new File("d:/test"));
        ServletFileUpload servletFileUpload=new ServletFileUpload(factory);
      //  servletFileUpload.setHeaderEncoding("utf-8");
        System.out.println(request+"__"+servletFileUpload);

        //file.transferTo(Path.of(new URI("d:/test")));
       // Map<String, List<FileItem>> lists=servletFileUpload.parseRequest(request);

        try{

            Map<String, List<FileItem>> map=servletFileUpload.parseParameterMap(request);
            System.out.println(map.size()+"_"+map);
            for(Map.Entry<String, List<FileItem>> list:map.entrySet()){
                System.out.println(list.getKey()+"__"+list.getValue());
                for(FileItem item:list.getValue()){
                    System.out.println(item);
                    item.write(new File("d:/share/"+item.getName()));
                }
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return new ResultJson(0,factory.getDefaultCharset()+"文件上传成功"+factory.getRepository(),System.getProperty("java.io.tmpdir"));
    }
}
