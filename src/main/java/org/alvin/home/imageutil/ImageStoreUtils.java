package org.alvin.home.imageutil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

/**
 * @author 唐植超
 * @date 2019/11/27
 * 图片里面存放数据
 */
public class ImageStoreUtils {
    /**
     * long 转8位字节
     *
     * @param value
     * @return
     */
    public static byte[] longTo8Bytes(long value) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(value);
        return buffer.array();
    }

    /**
     * 8位字节转long
     *
     * @param bytes
     * @return
     */
    public static long bytes8ToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(bytes);
        buffer.flip();//need flip
        return buffer.getLong();
    }

    /**
     * 往图片放入数据
     *
     * @param imgPath
     * @param imageData
     * @param data
     */
    public static void putData2Image(String imgPath, byte[] imageData, byte[] data) throws IOException {
        long length = data.length;
        byte[] lengthData = longTo8Bytes(length);
        //验证后8位
        try (RandomAccessFile file = new RandomAccessFile(imgPath, "rws")) {
            file.write(imageData);
            file.write(data);
            file.write(lengthData);
        }
    }

    /**
     * 读取图片藏的内容
     *
     * @param imgPath
     * @return
     * @throws IOException
     */
    public static byte[] readDateFromImage(String imgPath) throws IOException {
        try (RandomAccessFile file = new RandomAccessFile(imgPath, "r")) {
            byte[] lenData = new byte[8];
            file.seek(file.length() - lenData.length);
            for (int i = 0; i < lenData.length; i++) {
                lenData[i] = file.readByte();
            }
            long len = bytes8ToLong(lenData);
            file.seek(file.length() - len - lenData.length);
            byte[] dataArray = new byte[(int) len];
            for (int i = 0; i < len; i++) {
                dataArray[i] = file.readByte();
            }
            return dataArray;
        }
    }

    /**
     * 图片转byte[]
     *
     * @param img
     * @return
     * @throws IOException
     */
    public static byte[] imageToBytes(BufferedImage img, String format) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            ImageIO.write(img, format, bos);
            return bos.toByteArray();
        }
    }

    /**
     * 字节转图片
     *
     * @param data
     * @return
     * @throws IOException
     */
    public static BufferedImage bytes2Image(byte[] data) throws IOException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data)) {
            return ImageIO.read(bis);
        }
    }

}
