package com.hanyukeji.webmagicdemo.utils;

import com.alibaba.fastjson.JSON;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OcrUtils {

    private static final String filename = "F:\\gz5055\\project\\webmagic-demo\\src\\main\\resources\\tess4jdata\\test4j.jpg";

    public static void main(String[] args) throws IOException, TesseractException {
        String ocr = getOcrNumber();
        System.out.println(ocr);
    }

    public static String resolveOcrNumber(String ocr){
        String regex = "[^0-9]";
        Pattern pattern = Pattern.compile( regex );
        Matcher matcher = pattern.matcher( ocr );
        return matcher.replaceAll( "" ).trim();
    }


    public static String getOcrNumber() throws IOException, TesseractException {
  //      dowonPic();
        File imageFile = new File( filename );
        Tesseract instance = new Tesseract();
        instance.setLanguage( "eng" );
        instance.setDatapath( "G:\\software\\install\\tess4j\\tessdata" );
        return resolveOcrNumber( instance.doOCR(imageFile).replace( "\n","" ));
    }

    private static void dowonPic() throws IOException{
        String urlString = "http://www.beian.gov.cn/common/image.jsp?t=1";
        URL url = new URL(urlString);
        URLConnection con = url.openConnection();
        con.setConnectTimeout(5*1000);
        InputStream is = con.getInputStream();
        byte[] bs = new byte[1024];
        int len;
        File file = new File(filename);
        FileOutputStream os = new FileOutputStream(file);
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        os.close();
        is.close();
    }

}
