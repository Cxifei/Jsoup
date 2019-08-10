package com.cxf.jsoup;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Jsoup爬取网站图片工具类
 *
 * @author always_on_the_way
 * @date 2019-08-02
 */
public class FetchImgsUtil {

    public static void main(String[] args) {

        //利用jsoup获得连接
        Connection connect = Jsoup.connect("https://www.mockplus.cn/features");

        try {
            //得到Document对象
            Document document = connect.get();
            //查找所有img标签
            Elements imgs = document.getElementsByTag("img");
            System.out.println("共检测到下列图片URL");
            System.out.println("开始下载");
            //遍历img标签并获得src的属性
            for (Element element : imgs) {
                //获取每个标签URL"abs:"表示绝对路径
                String imgSrc = element.attr("abs:src");
                //获取当前图片文件名
                String fileName = imgSrc.substring(imgSrc.lastIndexOf('/') + 1, imgSrc.length());
                if (fileName.startsWith("logo")){
                    //打印URL
                    System.out.println(imgSrc);
                    //下载图片到本地
                    FetchImgsUtil.downImages("/Users/always_on_the_way/Desktop/logos",imgSrc);
                }
            }
            System.out.println("下载完成");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void downImages(String filePath,String imgUrl){

        //若指定文件夹没有，则创建
        File dir = new File(filePath);
        if (!dir.exists()){
            dir.mkdirs();
        }

        //截取图片文件名
        String fileName = imgUrl.substring(imgUrl.lastIndexOf('/') + 1, imgUrl.length());
        //如果文件名中没有中文，最好就不要转码了，避免出现其他问题
//        try {
//            //文件名里面可能有中文或者空格，所以这里要进行处理。但空格又会被URLEncoder转义为加号
//            String urlTail = URLEncoder.encode(fileName, "UTF-8");
//            //因此要将加号转化为UTF-8格式的%20
//            imgUrl = imgUrl.substring(0,imgUrl.lastIndexOf('/')+1)
//                    + urlTail.replaceAll("\\+","\\%20");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }


        //写出的路径
        File file =  new File(filePath + File.separator + fileName);

        try {
            //获取图片URL
            URL url = new URL(imgUrl);
            //获得连接
            URLConnection connection = url.openConnection();
            //设置10秒的响应时间
            connection.setConnectTimeout(10*1000);
            //获得输入流
            InputStream in = connection.getInputStream();
            //获得输出流
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            //构建缓冲区
            byte[] buf = new byte[1024];
            int size;
            //写入到文件
            while ((size = in.read(buf)) != -1){

                out.write(buf,0,size);

            }
            out.close();
            in.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
