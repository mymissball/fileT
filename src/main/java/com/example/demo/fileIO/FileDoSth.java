package com.example.demo.fileIO;

import com.example.demo.iFile.IFile;

import java.io.*;
import java.math.RoundingMode;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileDoSth implements IFile {

    static long fileCount=0;
    static long dirCount=0;
    static long startTime;
    static long endTime;




    void writeWord(String word){
        for(int k=1;k<=30000;k++) {
            System.out.print(" " + word);

            if(k%400==0){
                System.out.println();
            }
        }
    }




//E:\2021\2021-9
    static String filePath="D:\\2021";//"d:\\2021TK";
    public static void main1(String[] args) {
        startTime=System.currentTimeMillis();
        File file=new File(filePath);
       // new FileDoSth().traver(file);
       new FileDoSth().recursionForDel(file);
        endTime=System.currentTimeMillis();


        System.out.println("查找了"+dirCount+"个文件夹."+fileCount+"个文件,删除"+delCount+"个文件,删除失败"+delFailCount+"个文件——用时--"+((endTime-startTime)/1000.0));
    }






    public static void main(String[] args) {
        new FileDoSth().writeWord("");


    }

    static int delCount=0;
    @Override
    public void saveFile(File file, String path) {

        System.out.println("------------------------"+file.getAbsolutePath()+"----------------------保存文件---"+file.getName()+"--------------------------------------");
        String temp="d:/"+file.getName();
        String tempPath=file.getAbsolutePath();
        System.out.println("temp:"+tempPath);
       // tempPath="c:"+tempPath.substring(tempPath.indexOf("\\"),tempPath.lastIndexOf("\\")+1);
        tempPath=tempPath.substring(0,tempPath.lastIndexOf("\\")+1);
        System.out.println("tempPath:"+tempPath);


        File dirFile=new File(tempPath);
        if(!dirFile.exists()){
            if(!dirFile.mkdirs()){
                System.out.println("创建文件夹'"+dirFile.getAbsolutePath()+"'时发生错误，程序退出！");
                System.exit(1);

            }
        }
        File f=new File(tempPath+file.getName());
        System.out.println("的文件："+f.getAbsolutePath());
        try {
            //如果不存在就创建，如果文件存在就跳过直接对文件进行写入
            if(!f.exists())
                if(f.createNewFile()){
                    System.out.println(f.getAbsolutePath()+"不存在需要创建文件");
                }else{
                    System.out.println("文件'"+file.getName()+"'创建失败");
                    System.exit(1);
                }
        } catch (IOException e) {
            e.printStackTrace();
        }


        FileOutputStream fos= null;
        try {
            fos = new FileOutputStream(f);
            fos.write(path.getBytes("gbk"));
            System.out.println("文件保存成功");
            fos.close();
//System.exit(1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
         //   System.exit(1);
        }

    }




    void readFileChange(File file) {
        StringBuilder builder = new StringBuilder();

        BufferedReader br = null;

        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
            String temp = null;

            while ((temp = br.readLine()) != null) {
                String curTemp = temp;

                builder.append(curTemp + "\n");
            }
            saveFile(file,builder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {

                System.out.println("流已经关闭");

            }
        }

    }
    String AVERAGESTR="平均值,";
    String CPKSTR ="CPK,";


    @Override
    public void readFile(File file) {
        StringBuilder builder=new StringBuilder();
        String averageTemp = null,cpkTemp=null;
        double total=0;
        double average=0;
        String regex="\"(\\d)\",";
        Pattern pattern=Pattern.compile(regex);

        DecimalFormat df=new DecimalFormat("#.000");
        df.setRoundingMode(RoundingMode.FLOOR);
        BufferedReader br=null;

        try {
            br=new BufferedReader(new InputStreamReader(new FileInputStream(file),"utf-8"));
            String temp=null;
            int c=0;
            int stateCount=0;
            while ((temp=br.readLine())!=null){
                String curTemp=temp;
                //抬头信息
                System.out.println(temp);
                if(temp.startsWith(AVERAGESTR))
                    curTemp=averageTemp=temp;
                else if(temp.startsWith(CPKSTR))
                    curTemp=cpkTemp=temp;




                Matcher matcher=pattern.matcher(temp);
                if(matcher.find()){
                    c++;
                    System.out.println("找到文件'"+file.getAbsolutePath()+"'修改的数据:\n"+temp);

                    String[] strings=temp.split(",");
                    //strings[5]=strings[5].replaceAll("\"","");
                    double value=Double.valueOf(strings[5].replaceAll("\"",""));
                    //total+=value;
                    if(value>=25 && value<=28){
                        System.err.println("文件"+ file.getAbsolutePath()+"  第"+matcher.group(1)+"项值为'"+value+"'不需要修改");
                        total+=value;
                        curTemp=temp;

                  //      Thread.sleep(500);
                    }else{
                        double num=25+Math.random()*3;
                        num=Double.valueOf(df.format(num));
                        total+=num;
                        System.out.println("num:"+num);
                        temp=temp.replaceAll(value+"",num+"");
                        //
                        stateCount++;
                        curTemp=temp;
                    }
                    for(String str:strings) {
                        System.out.println(str+"       "+curTemp);

                    }
                }

              //  System.out.println(temp);
                builder.append(curTemp+"\n");
            }
            if(br!=null) {
                br.close();
            }else {
                System.out.println("流已经关闭");

                saveFile(file, builder.toString());
                return;
            }
            System.out.println(averageTemp+" "+AVERAGESTR+""+(total/c));
            System.out.println("total:"+total+" cpkTemp:"+Double.valueOf(cpkTemp.split(",")[1]));
            System.out.println(builder.toString());
           // System.exit(1);
            if(stateCount<=0 && (Double.valueOf(cpkTemp.split(",")[1])>=1.65 && Double.valueOf(cpkTemp.split(",")[1])<=1.8 )){
                System.err.println(file.getName()+"文件不需要修改");
                saveFile(file,builder.toString());
               // System.exit(1);
                return ;
            }
            StringBuilder sb =builder;
            if(stateCount>0) {
                sb = new StringBuilder(builder.toString().replaceAll(averageTemp, AVERAGESTR + "" + (Double.valueOf(df.format(total / c)))));
                builder=sb;
            }
            Double num=null;
            if(!(Double.valueOf(cpkTemp.split(",")[1])>=1.65 && Double.valueOf(cpkTemp.split(",")[1])<=1.8 )){

                 num=Double.valueOf(df.format(1.65+(Math.random()*0.15)));
                 System.out.println("cpk:"+num);
                 builder=new StringBuilder(sb.toString().replaceAll(cpkTemp,CPKSTR+num));
            }




            saveFile(file,builder.toString());
        } catch (FileNotFoundException e) {
            System.out.println("读取文件"+file.getAbsolutePath()+"失败，可能文件不存在。程序退出！"+e.getMessage());

            System.exit(1);

        } catch (IOException e) {
            e.printStackTrace();
        }  finally{
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


    protected  boolean delBadFile(File file){

            if(file.delete()){
                System.out.println("文件‘"+file.getName()+"’删除成功");

                return true;
            }else{
                System.out.println("文件‘"+file.getName()+"’删除失败");
                System.exit(1);
            }



        return false;
    }

    int count=0;
    /**
     * 遍历目录过滤需要的文件
     * @param file
     */
    private void recursion(File file){

        if(!file.isDirectory()){


            fileCount++;
            System.out.println(file.getName());
            if(file.getName().indexOf("rar")!=-1 || !file.getName().substring(file.getName().lastIndexOf(".")+1).equals("csv")){
                return;
            }


            if(file.getName().indexOf("exe")!=-1){
                System.out.println("有错误的文件"+file.getName());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                delBadFile(file);
               return;
            }


            readFileChange(file);
           // System.exit(0);
            count++;
//            if(count>=5){
//                System.exit(0);
//            }

        }else{
            dirCount++;
           File[] fs= file.listFiles();
            for(File f:fs){
                String fileName=f.getAbsolutePath();

                String regex="020晶片推力|020二焊推力|UEI二焊推力|UEI晶片推力";
                Pattern pattern=Pattern.compile(regex);
                Matcher matcher=pattern.matcher(fileName);
                if(matcher.find()){
                    recursion(f);
                }

            }
        }

    }

    @Override
    public void traver(File file) {

        if(!file.exists()){
            System.out.println(file.getAbsolutePath()+"不存在，程序退出！！！");
            System.exit(1);
        }

        File[] files=file.listFiles();

        for(File curFile:files){
            System.out.println("文件夹："+curFile.getName());

            recursion(curFile);
        }

    }

static int delFailCount=0;
    /**
     * 删除不要的文件
     * @param file
     */
    private void recursionForDel(File file){

        if(!file.isDirectory()){
            fileCount++;
            System.err.println("获取父目录："+file.getParent()+"_____当前文件____:"+file.getName());
//            if(file.getName().indexOf("exe")!=-1){
//                System.out.println("有错误的文件"+file.getName());
//
//                if(delBadFile(file)){
//                    delCount++;
//                }else{
//                    delFailCount++;
//                }
//
//            }


        }else{
            dirCount++;
            File[] fs= file.listFiles();
            for(File f:fs){

                    recursionForDel(f);


            }
        }

    }



}
