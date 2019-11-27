package org.alvin.home.imageutil;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 唐植超
 * @date 2019/11/06
 */
public class QRCodeImageUtils {
    /**
     * 生成二维码
     *
     * @param content
     * @param size
     * @return
     */
    public static BitMatrix genQrCode(String content, int size) {
        //设置图片的文字编码以及内边框
        Map<EncodeHintType, Object> hints = new HashMap<>();
        //编码
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        //边框距
        hints.put(EncodeHintType.MARGIN, 0);
        BitMatrix bitMatrix;
        try {
            //参数分别为：编码内容、编码类型、图片宽度、图片高度，设置参数
            bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, size, size, hints);
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
        return bitMatrix;
    }

    /**
     * 二维码转字节
     *
     * @param content
     * @param size
     * @return
     * @throws IOException
     */
    public static byte[] getQrBuffer(String content, int size) throws IOException {
        try (ByteArrayOutputStream byteArray = new ByteArrayOutputStream()) {
            MatrixToImageWriter.writeToStream(genQrCode(content, size), "png", byteArray);
            return byteArray.toByteArray();
        }
    }

    /**
     * 二维码转图片
     *
     * @param content
     * @param size
     * @return
     */
    public static BufferedImage getQRImg(String content, int size) {
        return MatrixToImageWriter.toBufferedImage(genQrCode(content, size));
    }


    /**
     * 合并图片和二维码
     *
     * @param logoPath  图标路径
     * @param imgPath   模板途径
     * @param qrContent 二维码内容
     * @param size      //二维码尺寸
     * @param x         //二维码贴的位置x
     * @param y         //二维码贴的位置y
     * @param angel     //传0没有角度
     * @return
     */
    public static BufferedImage mergeImageAndQR(String logoPath, String imgPath, String qrContent, int size, int x, int y, double angel) throws IOException {
        try (InputStream is = Files.newInputStream(Paths.get(imgPath))) {
            BufferedImage bi = ImageIO.read(is);
            Graphics2D g2 = bi.createGraphics();
            BufferedImage bufferedImage = null;
            if (logoPath != null && !logoPath.isEmpty()) {
                File logoFile = new File(logoPath);
                if (logoFile != null && logoFile.exists()) {
                    bufferedImage = drawLogoQRCode(logoFile, qrContent, size, angel);
                }
            } else {
                bufferedImage = Thumbnails.of(getQRImg(qrContent, size)).size(size, size).rotate(angel).asBufferedImage();
            }
            //消除文字锯齿
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            //消除画图锯齿
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.drawImage(bufferedImage, x, y, null);
            g2.dispose();
            return bi;
        }

    }


    /**
     * 根据图片路径识别二维码
     *
     * @param imgPath
     * @return
     * @throws IOException
     * @throws NotFoundException
     */
    public static String readQr(String imgPath) throws IOException, NotFoundException {
        try (InputStream is = Files.newInputStream(Paths.get(imgPath))) {
            return readQrByImg(ImageIO.read(is));
        }
    }

    //根据传入的图片，识别二维码
    public static String readQrByImg(BufferedImage img) throws NotFoundException {
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(img)));
        HashMap properties = new HashMap();
        properties.put(EncodeHintType.CHARACTER_SET, "utf-8");//字符集
        MultiFormatReader formatReader = new MultiFormatReader();
        Result result = formatReader.decode(binaryBitmap, properties);
        return result.getText();
    }

    /**
     * 生成带logo的二维码图片
     *
     * @param logoFile logo路径
     * @param content  内容
     * @param size     二维码大小
     * @param angel
     */
    public static BufferedImage drawLogoQRCode(File logoFile, String content, int size, double angel) throws IOException {
        BufferedImage image = Thumbnails.of(getQRImg(content, size)).size(size, size).rotate(angel).asBufferedImage();
        int width = image.getWidth();
        int height = image.getHeight();
        //绘制logo
        // 构建绘图对象
        Graphics2D g2 = image.createGraphics();
        // 读取Logo图片
        int x = width * 2 / 5;
        int y = height * 2 / 5;
        int cWidth = width * 2 / 10;
        int cHeight = height * 2 / 10;
        BufferedImage bufferedImage = Thumbnails.of(ImageIO.read(logoFile)).size(width, height).rotate(angel).asBufferedImage();
        // 开始绘制logo图片
        //消除文字锯齿
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        //消除画图锯齿
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawImage(bufferedImage, x, y, cWidth, cHeight, null);
        g2.dispose();
        bufferedImage.flush();
        image.flush();
        return image;
    }


}