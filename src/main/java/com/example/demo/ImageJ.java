package com.example.demo;



import ij.IJ;
import ij.ImageListener;
import ij.ImagePlus;
import ij.gui.ImagePanel;
import ij.io.FileInfo;
import ij.io.FileSaver;
import ij.io.Opener;
import ij.process.LUT;

import org.apache.logging.log4j.util.StringBuilders;
import org.springframework.boot.web.server.Http2;
import org.springframework.http.HttpMessage;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.*;
import sun.net.www.http.HttpCapture;
import sun.net.www.http.HttpClient;

import javax.websocket.Decoder;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageJ {

    private static int width=85,height=86;
    private static String path="/usr/local/upload/";

    //是否需要更改图片尺寸大小
    private static boolean needChange=true;

    private static void parseContext(String context) throws URISyntaxException, IOException {
        System.out.println(context);
        String regex="totalCount\":(.*?)\\[(.*?)searchParam";//providerResId";
        //System.out.println(context.matches(regex));
        //regex="alicdn.*?v640";
        Pattern pattern=Pattern.compile(regex);
        Matcher matcher=pattern.matcher(context);
        if(matcher.find()){
            String one=matcher.group(1);
            String two=matcher.group(2);
            System.out.println("匹配到信息:"+matcher.group(0)+"_\n"+one+"_\n"+two);
            regex="\\{(.*?)}";
            pattern=Pattern.compile(regex);
            matcher=pattern.matcher(two);
            regex="id\":(.*?),.*?:\"(.*?)\"";
            pattern=Pattern.compile(regex);


            Deque<String>list=new ArrayDeque<>();
            int i=0;
            while (matcher.find()){
                String str=matcher.group(0);
                System.out.println(i+++"."+str);
                list.add(str);

            }
            String str=list.poll();
            List<String>strings=new ArrayList<>();
            do{

                matcher=pattern.matcher(str);
                if(matcher.find()){

                    strings.add(matcher.group(2));
                }

            }while((str=list.poll())!=null);

            System.out.println(strings);
            String id=null;
            int j=0;
            for(String strs:strings){
                System.out.println(strs);
                id=strs;
                SimpleClientHttpRequestFactory simpleClientHttpRequestFactory=new SimpleClientHttpRequestFactory();
                ClientHttpRequestFactory clientHttpRequest=new SimpleClientHttpRequestFactory();
                String url="https://tenfei03.cfp.cn/creative/vcg/veer/1600water/veer-"+id+".jpg";
                 ClientHttpRequest request=clientHttpRequest.createRequest(new URI(url),HttpMethod.GET);
                    request.getHeaders().add("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.93 Safari/537.36");
                    ClientHttpResponse response=request.execute();

                    InputStream isr=response.getBody();

                    FileOutputStream fileOutputStream=new FileOutputStream(new File("/usr/local/upload/pic/"+j+++".jpg"));
                    byte[] bys=new byte[1024];
                    int length=-1;
                    while((length=isr.read(bys))!=-1){
                        fileOutputStream.write(bys,0,length);
                    }
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    isr.close();
                    long time= (long) (Math.random()*5000);
                    System.out.println("time:"+time);
                    try {
                        Thread.sleep(time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }
        }
        System.out.println(regex);
    }

    public static void main(String[] args) {
        //String picUrl="https://thumbs.dreamstime.com/z/teddy-bear-cactus-tuscon-arizona-closeup-picuture-taken-sonora-desert-museum-176300460.jpg";
        //picUrl="https://tenfei03.cfp.cn/creative/vcg/veer/1600water/veer-312002591.jpg";
        //picUrl="https://tenfei03.cfp.cn/creative/vcg/veer/1600water/veer-148461976.jpg";
        String picUrl="e:/root";
        File file=new File(picUrl);
        System.out.println(file+"_"+file.exists());
        File[] strs=file.listFiles();


        for(File str:strs){
            traverFile(str);
        }

    }

    private static String basePath="e:/";//需要保存的目标根目录
    public static void traverFile(File file){
//        //如果是文件根目录需要排除这些文件or文件夹 否则抛异常...
//        if("S-1-5-18".equals(file.getName())||"System Volume Information".equals(file.getName()))
//            return;
        String sourcePath="";//储存源文件的绝对路径用于提交到转换图片文件时的:imagePlus=opener.openImage(picPath);
        new ImageJ().addListen(); //注册 监听ImageJ 文件被修改时的处理
        if(file.isDirectory()){
            File[]list=file.listFiles();
            for(File f:list)
                traverFile(f);
        }else{
            String fileName=file.getName();
            //获取文件后缀分隔'.'的下标,用于下方获取文件后缀名
            int index=fileName.lastIndexOf(".");

            String suffix=null;
            if(index==-1)
                return;
            suffix=fileName.substring(index+1);
            //自定义的文件后缀
            String regex="png|jpeg|jpg|png|tif";
            //正则
            Pattern pattern=Pattern.compile(regex);
            Matcher matcher=pattern.matcher(suffix);
            System.out.print(fileName+"___"+suffix);
            //匹配是否为图片后缀
            if(matcher.find()){
                String realPath=file.getParent();
                sourcePath=realPath+"/"+fileName; //源文件路径

                //确定文件转换后的子路径 basePath+ ***
                String pathTemp="";
                if(!realPath.equals(basePath)) {
                    realPath = realPath.substring(basePath.length());
                    pathTemp="coverPic/"+realPath+"/";
                }

                path=basePath+pathTemp; //储存转换后的文件真实路径
                File f=new File(path);
                System.out.println("___path:__"+path);
                if(!f.exists()){
                    System.out.println("文件'"+path+"'不存在,创建状态:"+f.mkdirs());
                }

                loadingFile(sourcePath,needChange);
            }
        }
    }


    /**
     * 载入图片,设置需要转换图片的大小。会被图片update监听到然后调用方法保存新的图片
     * @param picPath 图片的路径
     */
    private static void loadingFile(String picPath,boolean needChange){

        Opener opener=new Opener();

        ImagePlus imagePlus=opener.openImage(picPath);
        String fileName=imagePlus.getTitle();

        if(needChange) {
            System.out.println("需要将载入图片进行转换--异步回调--下方设置转换的尺寸大小-"+width+"*"+height);
            imagePlus.resize(width, height, null);
        }else {
            System.out.println("当前不转换载入的图片-----40000000000000000---"+fileName);
        }
    }


    static int i=0;
    /**
     * 保存图片
     * @param imagePlus 图片源
     */
    private static void saveFile(ImagePlus imagePlus){
        String fileName=imagePlus.getOriginalFileInfo().fileName;
        FileSaver fileSaver=new FileSaver(imagePlus);
        System.out.println(path+fileName+" -title- "+i+++"."+fileSaver.saveAsPng(path+fileName));
        imagePlus.close();


    }


    /**
     *  监听图片状态
     */
    private void addListen(){
        ImagePlus.addImageListener(new ImageListener() {
            @Override //此状态是在图片打开窗口时触发
            public void imageOpened(ImagePlus imp) {
                System.out.println("图片打开:"+imp);
            }

            @Override //此状态是在图片打开窗口被关闭时触发
            public void imageClosed(ImagePlus imp) {
                System.out.println("图片关闭:"+imp);
            }

            @Override //此状态是在图片被更改有些参数时触发,比如更改图片大小
            public void imageUpdated(ImagePlus imp) {
                saveFile(imp);
            }
        });
    }

}
