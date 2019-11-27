package org.alvin.home.demo;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static org.alvin.home.imageutil.ImageStoreUtils.*;

/**
 * @author 唐植超
 * @date 2019/11/27
 */
public class ImageStoreUtils {

    public static void main(String[] args) throws IOException {
        String data = "hello ! my name is alvin";
        String imgPath = "D:/test.jpeg";
        BufferedImage bufferedImage = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g2 = bufferedImage.createGraphics();
        g2.setColor(Color.white);
        g2.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
        g2.setColor(Color.black);
        g2.fillRect(10, 10, 50, 50);
        g2.dispose();
        byte[] imageToByte = imageToBytes(bufferedImage, "jpeg");
        putData2Image(imgPath, imageToByte, data.getBytes("utf-8"));
        byte[] res = readDateFromImage(imgPath);
        System.out.println(new String(res, "utf-8"));
    }
}
