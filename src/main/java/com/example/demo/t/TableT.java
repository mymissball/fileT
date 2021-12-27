package com.example.demo.t;

import com.example.demo.entity.MachineInfo;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TableT {

    private static List<CellRangeAddress>rangeAddressList=null;
    private static String picTestPath="C:\\Users\\Administrator\\Desktop\\德润支架020.jpg";


    String[][] dataStr=null;
    static CellStyle[][] styles=null;

    public List<MachineInfo>machineInfoList=new ArrayList<>();


    public List<MachineInfo> writeObj(String filePath){
        List<MachineInfo>machineInfoList=new ArrayList<>();
        readFile(filePath);
        for(int i=0;i<dataStr.length;i++){
            int j=0;
            MachineInfo info=new MachineInfo();
            try {
                info.setId(Integer.valueOf(dataStr[i][j++]));
            }catch(NumberFormatException e){
                System.out.println(e.getMessage());
            }
            info.setDep_name(dataStr[i][j++]);
            info.setM_num(dataStr[i][j++]);
            info.setDate_of_repair(dataStr[i][j++]);
            info.setBreakDown(dataStr[i][j++]);
            info.setReason(dataStr[i][j++]);
            info.setSolution(dataStr[i][j++]);
            info.setM_desp(dataStr[i][j++]);
            info.setName(dataStr[i][j++]);
            info.setOrder_date(dataStr[i][j++]);
            info.setEnd_date(dataStr[i][j++]);
            info.setRemark(dataStr[i][j++]);
            machineInfoList.add(info);

        }
        return machineInfoList;
    }


    private void readFile(String filePath){
        XSSFWorkbook xw= null;
        try {
            xw = new XSSFWorkbook(filePath);
        } catch (Throwable e) {
            System.err.println("发生异常！"+e.getMessage());
            System.exit(-1);
        }

        System.out.println("sheetNum:"+xw.getNumberOfSheets());
        Sheet sheet=null;
        if(xw.getNumberOfSheets()>1) {
            sheet= xw.getSheetAt(0);
        }else{
            sheet= xw.getSheetAt(0);
        }


        rangeAddressList=sheet.getMergedRegions();
        System.out.println("合并格："+rangeAddressList+"_|合并数量组：_"+rangeAddressList.size());
        String[][] dataStr=null;
        for(int i=0;i<=sheet.getLastRowNum();i++){
            Row row=sheet.getRow(i);
            int col=row.getLastCellNum();
            if(dataStr==null) {
                dataStr = new String[sheet.getLastRowNum() + 1][row.getLastCellNum()];
                styles=new CellStyle[sheet.getLastRowNum()+1][row.getLastCellNum()];
            }
            for(int j=0;j<col;j++){
                Cell cell=row.getCell(j);

                if(cell.getCellType()==CellType.NUMERIC){
                    cell.setCellType(CellType.STRING);
                }


                dataStr[i][j]= cell.getStringCellValue();
                styles[i][j]=cell.getCellStyle();


            }


        }
        try {
            xw.close();
        } catch (IOException e) {
            System.out.println("发生异常！close:"+e.getMessage());
        }
        this.dataStr=dataStr;

    }

    private void writeFile(String filePath) throws IOException {

        Workbook wb=new XSSFWorkbook();
        File xsFile=new File(filePath);
        FileOutputStream fos=null;
        byte[]bys=new byte[]{};
        FileInputStream fis=new FileInputStream(new File(picTestPath));
    /*    bys=fis.readAllBytes();*/


      //  int picID=wb.addPicture(bys,Workbook.PICTURE_TYPE_PNG);














        if(!xsFile.exists()) {
            System.out.println("文件"+filePath+"不存在，开始创建");
            try {
                if(!xsFile.createNewFile()){
                    System.out.println("文件"+filePath+"创建失败，程序结束");
                    System.exit(-1);
                }else{
                    fos=new FileOutputStream(xsFile);
                }
            } catch (IOException e) {
                System.out.println("文件创建异常："+e.getMessage());
            }
        }else{
            try {
                fos=new FileOutputStream(xsFile);
            } catch (FileNotFoundException e) {
                System.out.println("fileOutputStreamFail.."+e.getMessage());
                System.exit(-1);
            }
        }
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

        Sheet sheet=wb.createSheet(sdf.format(new Date()));
        Row row=null;


//
//        CreationHelper helper = wb.getCreationHelper();
//        // Create the drawing patriarch. This is the top level container for
//        // all shapes.
//        Drawing drawing = sheet.createDrawingPatriarch();
//        // add a picture shape
//        ClientAnchor anchor = helper.createClientAnchor();
//        // set top-left corner of the picture,
//        // subsequent call of Picture#resize() will operate relative to it
//        anchor.setCol1(2);
//        anchor.setCol2(8);
//        anchor.setRow1(33);
//        anchor.setRow2(55);
//        anchor.setDx1(0);
//        anchor.setDy1(0);
//        anchor.setDx2(0);
//        anchor.setDy2(0);
//        anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);
//        Picture pict = drawing.createPicture(anchor, picID);
//        pict.resize(1);







        //设置合并
        rangeAddressList.forEach(sheet::addMergedRegion);

        //储存列中最长的字段用于生成合适宽度
        int[] resultWidth=new int[dataStr[0].length];

        for(int i=0;i<dataStr.length;i++) {
            row = sheet.createRow(i);
            for (int j = 0; j < dataStr[0].length; j++) {
                Cell cell = row.createCell(j);

                XSSFCellStyle xStyle2 = (XSSFCellStyle) styles[i][j];
                XSSFCellStyle xStyle = (XSSFCellStyle) wb.createCellStyle();
                xStyle.cloneStyleFrom(xStyle2);
                //向cell中填入值
                cell.setCellValue(dataStr[i][j]);
                //设置样式
                cell.setCellStyle(xStyle);

               // System.out.println(i+"字体"+j+":"+xStyle.getFont().getFontHeightInPoints()+"----"+xStyle.getFont().getFontHeightInPoints()+"/index:"+xStyle.getFontIndexAsInt());


//                if(!(rangeAddressList.get(0).containsColumn(j)&&rangeAddressList.get(0).containsRow(i))){
                   int result=calcWidth(dataStr[i][j]);
                    resultWidth[j]=result>resultWidth[j]?result:resultWidth[j];
                    System.err.println(i+"++++++"+j);
//                }
            }
        }

        //遍厉列宽并设置宽度
        for(int i=0;i<resultWidth.length;i++){
       //     System.out.println("resultWidth["+i+"]"+resultWidth[i]);
            sheet.setColumnWidth(i,resultWidth[i]+256);
        }

        try {

            wb.write(fos);
            wb.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }




    }

    private int  calcWidth(String param){
        int width=0;

        String regex="([A-Za-z0-9\\.\\s])";
        //String regex2="([\\u4e00-\\u9fa5])";
        Pattern pattern=Pattern.compile(regex);
        Matcher matcher=pattern.matcher(param);
        int k=0;

        while(matcher.find()){
            //System.err.println(++k+"."+matcher.group(1));
            ++k;
            width+=256;
        }

    //    System.out.println("宽度："+param.length()+" 计算宽度："+k);
        int mid=param.length()-k;

        return width+=mid*512;
    }

    public static void main(String[] args) throws IOException {
//        String filePath="C:\\Users\\Administrator\\Desktop\\工具表.xlsx";//"D:\\excel\\设备维修记录表 - 12 - 副本 - 副本.xlsx";
//        new TableT().readFile(filePath);
//        new TableT().writeFile("D:\\excel\\工具表.xlsx");
//        System.out.println((XSSFCellStyle)styles[0][0]+"___"+rangeAddressList);


    }


}

