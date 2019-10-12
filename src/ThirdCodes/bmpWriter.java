/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ThirdCodes;

import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author l67liu
 */
public class bmpWriter {

    public static void write(BufferedImage img, File out) {

        int width = img.getWidth();
        int tripleWidth = width * 3;
        int height = img.getHeight();
        int fullTriWidth = tripleWidth % 4 == 0 ? tripleWidth
                : 4 * ((tripleWidth / 4) + 1);
        int[] px = new int[width * height];
        px = img.getRGB(0, 0, width, height, px, 0, width);
        byte[] rgbs = new byte[tripleWidth * height];
        int index = 0;
        // r, g, b数组
        for (int i = height - 1; i >= 0; i--) {
            for (int j = 0; j < width; j++) {
                int pixel = px[i * width + j];
                rgbs[index++] = (byte) pixel;
                rgbs[index++] = (byte) (pixel >>> 8);
                rgbs[index++] = (byte) (pixel >>> 16);
            }
        }
        // 补齐扫描行长度为4的倍数
        byte[] fullrgbs = new byte[fullTriWidth * height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < fullTriWidth; j++) {
                if (j < tripleWidth) {
                    fullrgbs[fullTriWidth * i + j] = rgbs[i * tripleWidth + j];
                } else {
                    fullrgbs[fullTriWidth * i + j] = 0;
                }
            }
        }

        index = 0;

        int fheader = 14;
        int infoheader = 40;
        int board = 0;
        int offset = fheader + infoheader + board;
        int length = width * height * 3 + offset;
        short frame = 1;
        short deep = 24;
        int fbl = 3800;
        DataOutputStream dos = null;
        try {
            FileOutputStream fos = new FileOutputStream(out);
            dos = new DataOutputStream(fos);
            dos.write('B');
            dos.write('M');// 1格式头
            wInt(dos, length);// 2-3文件大小
            wInt(dos, 0);// 4-5保留
            wInt(dos, offset);// 6-7偏移量
            wInt(dos, infoheader);// 8-9头信息
            wInt(dos, width);// 10-11宽
            wInt(dos, height);// 12-13高
            wShort(dos, frame);// 14 = 1帧数
            wShort(dos, deep);// 15 = 24位数
            wInt(dos, 0);// 16-17压缩
            wInt(dos, 4);// 18-19 size
            wInt(dos, fbl);// 20-21水平分辨率
            wInt(dos, fbl);// 22-23垂直分辨率
            wInt(dos, 0);// 24-25颜色索引 0为所有
            wInt(dos, 0);// 26-27重要颜色索引 0为所有
            // wInt(0);//28-35
            // wInt(0);
            // wInt(0);
            // wInt(0);//28-35彩色板
            dos.write(fullrgbs);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                dos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void wInt(DataOutputStream dos, int i) throws IOException {
        dos.write(i);
        dos.write(i >> 8);
        dos.write(i >> 16);
        dos.write(i >> 24);
    }

    private static void wShort(DataOutputStream dos, short i)
            throws IOException {
        dos.write(i);
        dos.write(i >> 8);
    }

}
