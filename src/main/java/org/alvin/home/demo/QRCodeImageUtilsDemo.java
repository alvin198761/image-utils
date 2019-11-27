package org.alvin.home.demo;

import com.google.zxing.NotFoundException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.alvin.home.imageutil.QRCodeImageUtils.mergeImageAndQR;

/**
 * @author 唐植超
 * @date 2019/11/27
 */
public class QRCodeImageUtilsDemo {

    public static void main(String[] args) throws IOException, NotFoundException {
        String imagePath = "D:\\test.jpg";
        String qrContent = "http://www.baidu.com";
        int size = 100;
        int x = 30;
        int y = 380;
        //合并输出二维码
//        BufferedImage img = mergeImageAndQRByAngel(imagePath, qrContent, size, x, y, 20);
//        ImageIO.write(img, "png", new File("D:/test1.png"));
//        识别二维码
//        System.out.println(readQrByImg(img));
        BufferedImage img = mergeImageAndQR("D:/th.jpg", imagePath, qrContent, size, x, y, 30);
        ImageIO.write(img, "png", new File("D:/test1.png"));
    }
}
