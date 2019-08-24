package com.hanyukeji.webmagicdemo.ocrdemo;

import com.hanyukeji.webmagicdemo.utils.OcrUtils;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.openqa.selenium.*;
import org.openqa.selenium.Point;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;


public class BeianMain {
    private static WebDriver wd;
    static {
        System.setProperty( "webdriver.chrome.driver", "G:\\software\\package\\chromedriver.exe" );
        ChromeOptions options = new ChromeOptions();
        options.setHeadless( true );
        wd = new ChromeDriver( options );
    }
    public static void main(String[] args) throws IOException, TesseractException, InterruptedException {
        for (int i = 1;i<21;i++){
            Scanner sc = new Scanner(System.in);
            System.out.println("请输入website:");
            String websites = sc.next();
//            String websites = "www.beian.gov.cn";
            String contentHtml = login( websites );
            System.out.println("-----------------------="+i);
            if (contentHtml.contains( "联网备案信息" )){
                System.out.println("联网备案信息结果如下：");
                System.out.println(contentHtml);
            }else{
                System.out.println(contentHtml);
            }
        }
        quitWebDriver();
    }
    private static String login(String websites) throws IOException, TesseractException, InterruptedException {
        Thread.sleep( 2000 );
        wd.get( "http://www.beian.gov.cn/portal/recordQuery" );
        //下载验证码图片
        WebElement img = wd.findElement( By.xpath( "//*[@id=\"websitesform\"]/div/div[2]/div/img" ) );
        File screenshot = ((TakesScreenshot)wd).getScreenshotAs( OutputType.FILE );
        BufferedImage bufferedImage = ImageIO.read( screenshot );
        Point point = img.getLocation();
        ImageIO.write( bufferedImage.getSubimage( point.getX(), point.getY(), img.getSize().getWidth(), img.getSize().getHeight() ),"jpg",screenshot );
        FileUtils.copyFile( screenshot,new File("F:\\gz5055\\project\\webmagic-demo\\src\\main\\resources\\tess4jdata\\test4j.jpg") );
        //网站名称
        wd.findElement( By.id("websites")).clear();
        wd.findElement(By.id("websites")).sendKeys(websites);
        //验证码
        wd.findElement( By.id("ver3")).clear();
        wd.findElement(By.id("ver3")).sendKeys( OcrUtils.getOcrNumber() );
        //点击精准查询
        WebElement button = wd.findElement( By.cssSelector( "#websitesform > div > div:nth-child(3) > div > button" ));
        button.click();
        button.sendKeys( Keys.ENTER );
        //休息1秒，等待页面加载出来
        Thread.sleep( 2000 );
        //获取联网备案信息
        try{
            WebElement a_wzmc = wd.findElement( By.id( "a_wzmc" ) );
            if ("没有查询到数据！".equals( a_wzmc.getText())){
                return "没有查询到数据";
            }
        }catch (Exception e){
//            System.out.println("======有数据情况下取不到a_wzmc元素");
        }
        //没有查询到数据
        WebElement contentElement = wd.findElement( By.xpath( "/html/body/div[1]/div[5]"));
        String content = contentElement.getAttribute( "outerHTML" );
        if (content.contains( "请填写验证码" )){
            return "返回错误页面";
        }
        return content;
    }

    private static void quitWebDriver() throws IOException {
        wd.quit();
        String commend1 = "taskkill /im chrome.exe /F";
        Runtime.getRuntime().exec( commend1 );
        String commend2 = "taskkill /im chromedriver.exe /F";
        Runtime.getRuntime().exec( commend2 );
        System.err.println("*********************************");
        System.err.println("kill 掉google浏览器的后台进程，bye！");
        System.err.println("*********************************");
    }
}

