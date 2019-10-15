package ImageProcess;

import ThirdCodes.BilinearInterpolation;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.Vector;
import javax.imageio.ImageIO;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author l67liu
 */
public class ImageToolKit {

    public final static int B = 1;
    public final static int G = 2;
    public final static int R = 3;
    public final static int A = 4;
    public final static int HEX_MODE = 1;
    public final static int DEC_MODE = 2;

//    public final static int[] sourceOne = new int[]{1, 2, 3, 4};
//    public final static int[][] sourceThree = new int[][]{{1, 3, 5, 7}, {9, 11, 13, 15}, {17, 19, 21, 23}};
//    public final static int[][] sourceThree_1 = new int[][]{{1, 3, 5, 7}, {9, 11, 13, 15}, {17, 19, 21, 23}, {25, 27, 29, 31}};
    public static int[][] getArrayRGB(BufferedImage image) {

        int height = image.getHeight();
        int width = image.getWidth();
        int source[][] = new int[height][width];
        for (int rowX = 0; rowX < width; rowX++) {
            for (int colY = 0; colY < height; colY++) {
                //System.out.println(rowX + " " + colY);
                source[colY][rowX] = image.getRGB(rowX, colY);
            }
        }
        return source;
    }

    public static BufferedImage buildImageWithArray(int[][] source, int type) {
        int width = source[0].length;
        int height = source.length;
        //BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        BufferedImage image = new BufferedImage(width, height, type);
        for (int rowX = 0; rowX < width; rowX++) {
            for (int colY = 0; colY < height; colY++) {
                image.setRGB(rowX, colY, source[colY][rowX]);
            }
        }
        return image;
    }

    public static void printArray(int array[][], int mode) {
        System.out.println("*************************");
        for (int rowX = 0; rowX < array.length; rowX++) {
            for (int colY = 0; colY < array[0].length; colY++) {
                if (mode == HEX_MODE) {
                    System.out.printf("0x%08x ", array[rowX][colY]);
                } else {
                    System.out.print(" " + array[rowX][colY]);
                }
            }
            System.out.println("\n");
        }
    }

    public static int getGreyValueFromValue(int value) {
        double a = 0.3 * getSingleRGBFromValue(value, ImageToolKit.R);
        double b = 0.59 * getSingleRGBFromValue(value, ImageToolKit.G);
        double c = 0.11 * getSingleRGBFromValue(value, ImageToolKit.B);
        return (int) (a + b + c);
    }

    public static int getSingleRGBFromValue(int value, int type) {
        switch (type) {
            case ImageToolKit.R:
                return (value & 0x00FF0000) >> 16;
            case ImageToolKit.G:
                return (value & 0x0000FF00) >> 8;
            case ImageToolKit.B:
                return (value & 0x000000FF);
            default:
                return (value & 0x00FF0000) >> 16;
        }
    }

    public static int setSingleARGBToValue(int value, int RGB, int type) {
        switch (type) {
            case ImageToolKit.R:
                return (value & 0x0000FFFF) | (RGB << 16);
            case ImageToolKit.G:
                return (value & 0x00FF00FF) | (RGB << 8);
            case ImageToolKit.B:
                return (value & 0x00FFFF00) | RGB;
            case ImageToolKit.A:
                return value | 0xFF000000;
            default:
                return (value & 0x0000FFFF) | (RGB << 16);
        }
    }

    public static int getAVGRGBFromTwoRGB(int value1, int value2) {
        int returnValue = 0xFF000000;
        int tempR = (getSingleRGBFromValue(value1, ImageToolKit.R) + getSingleRGBFromValue(value2, ImageToolKit.R)) / 2;
        int tempG = (getSingleRGBFromValue(value1, ImageToolKit.G) + getSingleRGBFromValue(value2, ImageToolKit.G)) / 2;
        int tempB = (getSingleRGBFromValue(value1, ImageToolKit.B) + getSingleRGBFromValue(value2, ImageToolKit.B)) / 2;
        returnValue = setSingleARGBToValue(returnValue, tempR, ImageToolKit.R);
        returnValue = setSingleARGBToValue(returnValue, tempG, ImageToolKit.G);
        returnValue = setSingleARGBToValue(returnValue, tempB, ImageToolKit.B);
        return returnValue;
    }

    public static BufferedImage zoomOutNormal(BufferedImage image) { //图像临近插值放大
        return ImageToolKit.buildImageWithArray(zoomOutNormal(getArrayRGB(image)), BufferedImage.TYPE_3BYTE_BGR);
    }

    public static BufferedImage zoomOutChaZhi(BufferedImage image) { //图像二维插值放大
        return ImageToolKit.buildImageWithArray(zoomOutChaZhi(getArrayRGB(image)), BufferedImage.TYPE_3BYTE_BGR);
    }

    public static BufferedImage buildGreyImage(BufferedImage image) { //图像反转
        return ImageToolKit.buildImageWithArray(transferGreyImage(getArrayRGB(image)), BufferedImage.TYPE_BYTE_GRAY);
    }

    public static BufferedImage buildReversalmage(BufferedImage image) {  //图像反转
        return ImageToolKit.buildImageWithArray(reversalImage(getArrayRGB(image)), BufferedImage.TYPE_BYTE_GRAY);
    }

    public static BufferedImage buildLogarithmlmage(BufferedImage image) { //图像Log函数反转
        return ImageToolKit.buildImageWithArray(logarithmImage(getArrayRGB(image)), BufferedImage.TYPE_BYTE_GRAY);
    }

    public static Vector<BufferedImage> buildRFreqAmpImage(BufferedImage image) { //图像Log函数反转
        return transferRFreqAmpImage(getArrayRGB(image));
    }

    public static Vector<BufferedImage> buildRFreqAmpSignImage(BufferedImage image, BufferedImage signImage) { //图像Log函数反转
        return transferRFreqAmpSignImage(getArrayRGB(image), getArrayRGB(signImage));
    }

    public static Vector<BufferedImage> extraceImagefronSignedImage(BufferedImage image, BufferedImage image1) { //图像Log函数反转
        return extraceImagefronSignedImage(getArrayRGB(image), getArrayRGB(image1));
    }

    public static BufferedImage buildRImage(BufferedImage image) {
        return ImageToolKit.buildImageWithArray(transferRImage(getArrayRGB(image)), BufferedImage.TYPE_3BYTE_BGR);
    }

    public static BufferedImage buildBImage(BufferedImage image) {
        return ImageToolKit.buildImageWithArray(transferBImage(getArrayRGB(image)), BufferedImage.TYPE_3BYTE_BGR);
    }

    public static BufferedImage buildGImage(BufferedImage image) {
        return ImageToolKit.buildImageWithArray(transferGImage(getArrayRGB(image)), BufferedImage.TYPE_3BYTE_BGR);
    }

    public static int[][] zoomOutNormal(int[][] source) {
        //ImageToolKit.printArray(source, ImageToolKit.DEC_MODE);
        int[][] tempTarget = new int[source.length * 2][source[0].length];
        int[][] target = new int[source.length * 2][source[0].length * 2];
        int targetRow = 0;
        int targetCol = 0;
        for (int sourceRow = 0; sourceRow < source.length; sourceRow++) {
            for (int sourceCol = 0; sourceCol < source[0].length; sourceCol++) {
                tempTarget[targetRow][targetCol] = source[sourceRow][sourceCol];
                tempTarget[targetRow + 1][targetCol] = source[sourceRow][sourceCol];
                targetCol++;
            }
            targetRow += 2;
            targetCol = 0;
        }
        targetRow = 0;
        targetCol = 0;
        for (int sourceCol = 0; sourceCol < tempTarget[0].length; sourceCol++) {
            for (int sourceRow = 0; sourceRow < tempTarget.length; sourceRow++) {
                target[targetRow][targetCol] = tempTarget[sourceRow][sourceCol];
                target[targetRow][targetCol + 1] = tempTarget[sourceRow][sourceCol];
                targetRow++;
            }
            targetRow = 0;
            targetCol += 2;
        }
        //ImageToolKit.printArray(target, ImageToolKit.DEC_MODE);
        return target;
    }

    public static int[][] zoomOutChaZhi(int[][] source) {
        float scaleX = 2f;
        float scaleY = 2f;
        int newWidth = (int) (source[0].length * scaleX);
        int newHeight = (int) (source.length * scaleY);
        int[][] target = new int[newHeight][newWidth];

        for (int x = 0; x < newWidth; ++x) {
            for (int y = 0; y < newHeight; ++y) {
                float gx = ((float) x) / newWidth * (source[0].length - 1);
                float gy = ((float) y) / newHeight * (source.length - 1);
                int gxi = (int) gx;
                int gyi = (int) gy;
                int rgb = 0;
                //System.out.println(x + " " + y);
                //System.out.println(gx + " " + gy);
                //System.out.println("[" + gxi + "," + gyi + "],[" + (gxi + 1) + "," + gyi + "],[" + gxi + "," + (gyi + 1) + "],[" + (gxi + 1) + "," + (gyi + 1) + "]");
                int c00 = source[gyi][gxi];
                int c10 = source[gyi][gxi + 1];
                int c01 = source[gyi + 1][gxi];
                int c11 = source[gyi + 1][gxi + 1];
                int tempR = (int) BilinearInterpolation.blerp((float) ImageToolKit.getSingleRGBFromValue(c00, ImageToolKit.R), (float) ImageToolKit.getSingleRGBFromValue(c10, ImageToolKit.R),
                        (float) ImageToolKit.getSingleRGBFromValue(c01, ImageToolKit.R),
                        (float) ImageToolKit.getSingleRGBFromValue(c11, ImageToolKit.R), gx - gxi, gy - gyi);
                int tempG = (int) BilinearInterpolation.blerp((float) ImageToolKit.getSingleRGBFromValue(c00, ImageToolKit.G), (float) ImageToolKit.getSingleRGBFromValue(c10, ImageToolKit.G),
                        (float) ImageToolKit.getSingleRGBFromValue(c01, ImageToolKit.G),
                        (float) ImageToolKit.getSingleRGBFromValue(c11, ImageToolKit.G), gx - gxi, gy - gyi);
                int tempB = (int) BilinearInterpolation.blerp((float) ImageToolKit.getSingleRGBFromValue(c00, ImageToolKit.B), (float) ImageToolKit.getSingleRGBFromValue(c10, ImageToolKit.B),
                        (float) ImageToolKit.getSingleRGBFromValue(c01, ImageToolKit.B),
                        (float) ImageToolKit.getSingleRGBFromValue(c11, ImageToolKit.B), gx - gxi, gy - gyi);
                rgb = ImageToolKit.setSingleARGBToValue(rgb, tempR, ImageToolKit.R);
                rgb = ImageToolKit.setSingleARGBToValue(rgb, tempG, ImageToolKit.G);
                rgb = ImageToolKit.setSingleARGBToValue(rgb, tempB, ImageToolKit.B);
                target[y][x] = rgb;
            }
        }
        //ImageToolKit.printArray(target, ImageToolKit.HEX_MODE);
        return target;
    }

    public static int[][] transferGreyImage(int[][] source) {
        //ImageToolKit.printArray(source, ImageToolKit.DEC_MODE);
        int[][] target = new int[source.length][source[0].length];
        int gray, grayLevel = 0;
        for (int sourceRow = 0; sourceRow < source.length; sourceRow++) {
            for (int sourceCol = 0; sourceCol < source[0].length; sourceCol++) {
                grayLevel = ImageToolKit.getGreyValueFromValue(source[sourceRow][sourceCol]);
                gray = (grayLevel << 16) + (grayLevel << 8) + grayLevel;
                target[sourceRow][sourceCol] = gray;
            }
        }
        //ImageToolKit.printArray(target, ImageToolKit.DEC_MODE);
        return target;
    }

    public static int[][] transferRImage(int[][] source) {
        int[][] target = new int[source.length][source[0].length];
        int value = 0, temp = 0;
        for (int sourceRow = 0; sourceRow < source.length; sourceRow++) {
            for (int sourceCol = 0; sourceCol < source[0].length; sourceCol++) {
                value = ImageToolKit.getSingleRGBFromValue(source[sourceRow][sourceCol], ImageToolKit.R);
                temp = ImageToolKit.setSingleARGBToValue(temp, value, ImageToolKit.R);
                temp = ImageToolKit.setSingleARGBToValue(temp, 0, ImageToolKit.G);
                temp = ImageToolKit.setSingleARGBToValue(temp, 0, ImageToolKit.B);
                temp = ImageToolKit.setSingleARGBToValue(temp, 0, ImageToolKit.A);
                target[sourceRow][sourceCol] = temp;
            }
        }
        return target;
    }

    public static int[][] transferGImage(int[][] source) {
        //ImageToolKit.printArray(source, ImageToolKit.DEC_MODE);
        int[][] target = new int[source.length][source[0].length];
        int value = 0, temp = 0;
        for (int sourceRow = 0; sourceRow < source.length; sourceRow++) {
            for (int sourceCol = 0; sourceCol < source[0].length; sourceCol++) {
                value = ImageToolKit.getSingleRGBFromValue(source[sourceRow][sourceCol], ImageToolKit.G);
                temp = ImageToolKit.setSingleARGBToValue(temp, 0, ImageToolKit.R);
                temp = ImageToolKit.setSingleARGBToValue(temp, value, ImageToolKit.G);
                temp = ImageToolKit.setSingleARGBToValue(temp, 0, ImageToolKit.B);
                temp = ImageToolKit.setSingleARGBToValue(temp, 0, ImageToolKit.A);
                target[sourceRow][sourceCol] = temp;
                temp = 0;
            }
        }
        return target;
    }

    public static int[][] transferBImage(int[][] source) {
        int[][] target = new int[source.length][source[0].length];
        int value = 0, temp = 0;
        for (int sourceRow = 0; sourceRow < source.length; sourceRow++) {
            for (int sourceCol = 0; sourceCol < source[0].length; sourceCol++) {
                value = ImageToolKit.getSingleRGBFromValue(source[sourceRow][sourceCol], ImageToolKit.B);
                temp = ImageToolKit.setSingleARGBToValue(temp, 0, ImageToolKit.R);
                temp = ImageToolKit.setSingleARGBToValue(temp, 0, ImageToolKit.G);
                temp = ImageToolKit.setSingleARGBToValue(temp, value, ImageToolKit.B);
                temp = ImageToolKit.setSingleARGBToValue(temp, 0, ImageToolKit.A);
                target[sourceRow][sourceCol] = temp;
            }
        }
        return target;
    }

    public static Vector<BufferedImage> transferRFreqAmpImage(int[][] source) {
        int Max = 0;
        int height = source.length; // sourceRow
        int weight = source[0].length; //sourceCol
        int value = 0, temp = 0;
        int R = 0, G = 0, B = 0;
        Complex[][] pcplx = new Complex[height][weight];
        int[][] redSourcelImage = new int[source.length][source[0].length];
        int[][] redChannelImage = new int[source.length][source[0].length];
        int[][] freqRedImage = new int[source.length][source[0].length];
        int[][] ampRedImage = new int[source.length][source[0].length];

        for (int sourceRow = 0; sourceRow < source.length; sourceRow++) {
            for (int sourceCol = 0; sourceCol < source[0].length; sourceCol++) {
                value = ImageToolKit.getSingleRGBFromValue(source[sourceRow][sourceCol], ImageToolKit.R);
                temp = ImageToolKit.setSingleARGBToValue(temp, value, ImageToolKit.R);
                temp = ImageToolKit.setSingleARGBToValue(temp, 0, ImageToolKit.G);
                temp = ImageToolKit.setSingleARGBToValue(temp, 0, ImageToolKit.B);
                temp = ImageToolKit.setSingleARGBToValue(temp, 0, ImageToolKit.A);
                redSourcelImage[sourceRow][sourceCol] = temp;
                pcplx[sourceRow][sourceCol] = new Complex(value, 0);
            }
        }

        double[][] Amp = new double[height][weight];
        double[][] power = new double[height][weight];

        FFT2.FFT_2(pcplx, height);
        Complex[] res = FFT2.twoDimensionalArraysToOneDimensionalArray(pcplx);
        FFT2.FFT_2_Shift(res, height);  //分散于四个角的低频信号经过移位到中心后，变得更加的易于观察了
        pcplx = FFT2.oneDimensionalArrayToTwoDimensionalArrays(res);

        //算能量和相位
        for (int sourceRow = 0; sourceRow < source.length; sourceRow++) {
            for (int sourceCol = 0; sourceCol < source[0].length; sourceCol++) {
                power[sourceRow][sourceCol] = Math.sqrt(pcplx[sourceRow][sourceCol].im * pcplx[sourceRow][sourceCol].im + pcplx[sourceRow][sourceCol].re + pcplx[sourceRow][sourceCol].re);
                if (pcplx[sourceRow][sourceCol].re * pcplx[sourceRow][sourceCol].re < 0.00000001) {
                    Amp[sourceRow][sourceCol] = 0;
                } else {
                    Amp[sourceRow][sourceCol] = Math.atan(pcplx[sourceRow][sourceCol].im / pcplx[sourceRow][sourceCol].re);
                }
                //对数变化，加强能量视觉显示
                power[sourceRow][sourceCol] = (int) (Math.log(1 + 3 * power[sourceRow][sourceCol]));
                if (power[sourceRow][sourceCol] > Max) {
                    Max = (int) (power[sourceRow][sourceCol]);
                }
            }
        }

        if (Max == 0) {
            for (int y = 0; y < height; y++) { // row
                for (int x = 0; x < weight; x++) { // col
                    freqRedImage[y][x] = 0;
                    R = (int) ((Amp[y][x] + 1.571) * 255 / 3.1416);
                    G = (int) ((Amp[y][x] + 1.58) * 255 / 3.1416);
                    B = (int) ((Amp[y][x] + 1.58) * 255 / 3.1416);
                    ampRedImage[y][x] = new Color(FFT2.filterNoise(R), FFT2.filterNoise(G), FFT2.filterNoise(B)).getRGB();
                }
            }
        } else {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < weight; x++) {
                    R = (int) (power[y][x] * 255 / Max);
                    G = (int) (power[y][x] * 255 / Max);
                    B = (int) (power[y][x] * 255 / Max);
                    freqRedImage[y][x] = new Color(FFT2.filterNoise(R), FFT2.filterNoise(G), FFT2.filterNoise(B)).getRGB();
                    R = (int) ((Amp[y][x] + 1.571) * 255 / 3.1416);
                    G = (int) ((Amp[y][x] + 1.58) * 255 / 3.1416);
                    B = (int) ((Amp[y][x] + 1.58) * 255 / 3.1416);
                    ampRedImage[y][x] = new Color(FFT2.filterNoise(R), FFT2.filterNoise(G), FFT2.filterNoise(B)).getRGB();
                }
            }
        }

        res = FFT2.twoDimensionalArraysToOneDimensionalArray(pcplx);
        FFT2.FFT_2_Shift(res, height);  //分散于四个角的低频信号经过移位到中心后，变得更加的易于观察了
        pcplx = FFT2.oneDimensionalArrayToTwoDimensionalArrays(res);
        FFT2.IFFT_2(pcplx, height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < weight; x++) {
                if (pcplx[y][x].re > 255) {
                    redChannelImage[y][x] = new Color(255, 0, 0).getRGB();
                    continue;
                }

                if (pcplx[y][x].re < 0) {
                    redChannelImage[y][x] = new Color(0, 0, 0).getRGB();
                    continue;
                }
                redChannelImage[y][x] = new Color((int) pcplx[y][x].re, 0, 0).getRGB();
            }
        }

        Vector<BufferedImage> resImage = new Vector();
        resImage.add(ImageToolKit.buildImageWithArray(redSourcelImage, BufferedImage.TYPE_3BYTE_BGR));
        resImage.add(ImageToolKit.buildImageWithArray(redChannelImage, BufferedImage.TYPE_3BYTE_BGR));
        resImage.add(ImageToolKit.buildImageWithArray(freqRedImage, BufferedImage.TYPE_3BYTE_BGR));
        resImage.add(ImageToolKit.buildImageWithArray(ampRedImage, BufferedImage.TYPE_3BYTE_BGR));

        return resImage;
    }
    /*
     这个方法和transferRFreqAmpImage大部分相同，
     唯一的区别就是：
     计算mapScale和签名叠加到频域
     */

    public static Vector<BufferedImage> transferRFreqAmpSignImage(int[][] source, int[][] signSourceImage) {
        int Max = 0;
        int height = source.length; // sourceRow
        int weight = source[0].length; //sourceCol
        int value = 0, temp = 0;
        int R = 0, G = 0, B = 0;
        Complex[][] pcplx = new Complex[height][weight];
        int[][] redSourcelImage = new int[source.length][source[0].length];
        int[][] redChannelImage = new int[source.length][source[0].length];
        int[][] freqRedImage = new int[source.length][source[0].length];
        int[][] ampRedImage = new int[source.length][source[0].length];
        int[][] signedImage = new int[source.length][source[0].length];
        double[][] Amp = new double[height][weight];
        double[][] power = new double[height][weight];
        float[][] mapScale = new float[signSourceImage.length][signSourceImage[0].length];
        for (int sourceRow = 0; sourceRow < signSourceImage.length; sourceRow++) {
            for (int sourceCol = 0; sourceCol < signSourceImage[0].length; sourceCol++) {
                //System.out.println(signSourceImage[sourceRow][sourceCol] & 0x00ffffff);
                if ((signSourceImage[sourceRow][sourceCol] & 0x00ffffff) > 0) {
                    mapScale[sourceRow][sourceCol] = 1.0f / 50;
                } else {
                    mapScale[sourceRow][sourceCol] = 1.f;
                }
            }
        }

        for (int sourceRow = 0; sourceRow < source.length; sourceRow++) {
            for (int sourceCol = 0; sourceCol < source[0].length; sourceCol++) {
                value = ImageToolKit.getSingleRGBFromValue(source[sourceRow][sourceCol], ImageToolKit.R);
                temp = ImageToolKit.setSingleARGBToValue(temp, value, ImageToolKit.R);
                temp = ImageToolKit.setSingleARGBToValue(temp, 0, ImageToolKit.G);
                temp = ImageToolKit.setSingleARGBToValue(temp, 0, ImageToolKit.B);
                temp = ImageToolKit.setSingleARGBToValue(temp, 0, ImageToolKit.A);
                redSourcelImage[sourceRow][sourceCol] = temp;
                pcplx[sourceRow][sourceCol] = new Complex(value, 0);
            }
        }

        FFT2.FFT_2(pcplx, height);

        //签名叠加到频域
        for (int y = 0; y < mapScale.length; y++) {
            for (int x = 0; x < mapScale[0].length; x++) {
                pcplx[y][x].re *= mapScale[y][x];
                pcplx[y][x].im *= mapScale[y][x];
                pcplx[(height / 2 + y)][weight / 2 + x].re *= mapScale[y][x];
                pcplx[(height / 2 + y)][weight / 2 + x].im *= mapScale[y][x];
            }
        }
        //实在不想把方法转换为二维数组了的，就偷懒了，原始作者的方法是一维的，那就二维转一维，再一维转二维了
        Complex[] res = FFT2.twoDimensionalArraysToOneDimensionalArray(pcplx);
        FFT2.FFT_2_Shift(res, height);  //分散于四个角的低频信号经过移位到中心后，变得更加的易于观察了
        pcplx = FFT2.oneDimensionalArrayToTwoDimensionalArrays(res);

        //算能量和相位
        for (int sourceRow = 0; sourceRow < source.length; sourceRow++) {
            for (int sourceCol = 0; sourceCol < source[0].length; sourceCol++) {
                power[sourceRow][sourceCol] = Math.sqrt(pcplx[sourceRow][sourceCol].im * pcplx[sourceRow][sourceCol].im + pcplx[sourceRow][sourceCol].re + pcplx[sourceRow][sourceCol].re);
                if (pcplx[sourceRow][sourceCol].re * pcplx[sourceRow][sourceCol].re < 0.00000001) {
                    Amp[sourceRow][sourceCol] = 0;
                } else {
                    Amp[sourceRow][sourceCol] = Math.atan(pcplx[sourceRow][sourceCol].im / pcplx[sourceRow][sourceCol].re);
                }
                //对数变化，加强能量视觉显示
                power[sourceRow][sourceCol] = (int) (Math.log(1 + 3 * power[sourceRow][sourceCol]));
                if (power[sourceRow][sourceCol] > Max) {
                    Max = (int) (power[sourceRow][sourceCol]);
                }
            }
        }

        if (Max == 0) {
            for (int y = 0; y < height; y++) { // row
                for (int x = 0; x < weight; x++) { // col
                    freqRedImage[y][x] = 0;
                    R = (int) ((Amp[y][x] + 1.571) * 255 / 3.1416);
                    G = (int) ((Amp[y][x] + 1.58) * 255 / 3.1416);
                    B = (int) ((Amp[y][x] + 1.58) * 255 / 3.1416);
                    ampRedImage[y][x] = new Color(FFT2.filterNoise(R), FFT2.filterNoise(G), FFT2.filterNoise(B)).getRGB();
                }
            }
        } else {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < weight; x++) {
                    R = (int) (power[y][x] * 255 / Max);
                    G = (int) (power[y][x] * 255 / Max);
                    B = (int) (power[y][x] * 255 / Max);
                    freqRedImage[y][x] = new Color(FFT2.filterNoise(R), FFT2.filterNoise(G), FFT2.filterNoise(B)).getRGB();
                    R = (int) ((Amp[y][x] + 1.571) * 255 / 3.1416);
                    G = (int) ((Amp[y][x] + 1.58) * 255 / 3.1416);
                    B = (int) ((Amp[y][x] + 1.58) * 255 / 3.1416);
                    ampRedImage[y][x] = new Color(FFT2.filterNoise(R), FFT2.filterNoise(G), FFT2.filterNoise(B)).getRGB();
                }
            }
        }

        res = FFT2.twoDimensionalArraysToOneDimensionalArray(pcplx);
        FFT2.FFT_2_Shift(res, height);  //分散于四个角的低频信号经过移位到中心后，变得更加的易于观察了
        pcplx = FFT2.oneDimensionalArrayToTwoDimensionalArrays(res);
        //逆傅里叶变化还原图像
        FFT2.IFFT_2(pcplx, height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < weight; x++) {
                if (pcplx[y][x].re > 255) {
                    redChannelImage[y][x] = new Color(255, 0, 0).getRGB();
                    continue;
                }

                if (pcplx[y][x].re < 0) {
                    redChannelImage[y][x] = new Color(0, 0, 0).getRGB();
                    continue;
                }
                redChannelImage[y][x] = new Color((int) pcplx[y][x].re, 0, 0).getRGB();
            }
        }

        for (int sourceRow = 0; sourceRow < signedImage.length; sourceRow++) {
            for (int sourceCol = 0; sourceCol < signedImage[0].length; sourceCol++) {
                signedImage[sourceRow][sourceCol] = ImageToolKit.setSingleARGBToValue(source[sourceRow][sourceCol], redChannelImage[sourceRow][sourceCol] >> 16, ImageToolKit.R);
            }
        }

        Vector<BufferedImage> resImage = new Vector();
        resImage.add(ImageToolKit.buildImageWithArray(redSourcelImage, BufferedImage.TYPE_3BYTE_BGR));
        resImage.add(ImageToolKit.buildImageWithArray(redChannelImage, BufferedImage.TYPE_3BYTE_BGR));
        resImage.add(ImageToolKit.buildImageWithArray(freqRedImage, BufferedImage.TYPE_3BYTE_BGR));
        resImage.add(ImageToolKit.buildImageWithArray(ampRedImage, BufferedImage.TYPE_3BYTE_BGR));
        resImage.add(ImageToolKit.buildImageWithArray(signedImage, BufferedImage.TYPE_3BYTE_BGR));
        return resImage;
    }

    public static Vector<BufferedImage> extraceImagefronSignedImage(int[][] sourceSigned, int[][] source) {
        int Max = 0;
        int height = source.length; // sourceRow
        int weight = source[0].length; //sourceCol
        int value = 0, temp = 0;
        int R = 0, G = 0, B = 0;
        Complex[][] pcplxSourceSigned = new Complex[height][weight];
        Complex[][] pcplxSource = new Complex[height][weight];
        int[][] signedImage = new int[source.length / 2][source[0].length / 2];
        double[][] mapScale = new double[sourceSigned.length / 2][sourceSigned[0].length / 2];
        int[][] mapScaleInt = new int[sourceSigned.length / 2][sourceSigned[0].length / 2];
        Complex[][] mapScaleComplex = new Complex[height / 2][weight / 2];

        for (int sourceRow = 0; sourceRow < source.length; sourceRow++) {
            for (int sourceCol = 0; sourceCol < source[0].length; sourceCol++) {
                //value = sourceSigned[sourceRow][sourceCol] & 0xffff0000;
                value = ImageToolKit.getSingleRGBFromValue(sourceSigned[sourceRow][sourceCol], ImageToolKit.R);
                pcplxSourceSigned[sourceRow][sourceCol] = new Complex(value, 0);
            }
        }

        for (int sourceRow = 0; sourceRow < source.length; sourceRow++) {
            for (int sourceCol = 0; sourceCol < source[0].length; sourceCol++) {
                value = ImageToolKit.getSingleRGBFromValue(source[sourceRow][sourceCol], ImageToolKit.R);
                pcplxSource[sourceRow][sourceCol] = new Complex(value, 0);

            }
        }

        FFT2.FFT_2(pcplxSourceSigned, height);
        FFT2.FFT_2(pcplxSource, height);

        for (int sourceRow = 0; sourceRow < mapScale.length; sourceRow++) {
            for (int sourceCol = 0; sourceCol < mapScale[0].length; sourceCol++) {
                mapScaleComplex[sourceRow][sourceCol] = new Complex(0, 0);
                mapScale[sourceRow][sourceCol] = pcplxSourceSigned[sourceRow][sourceCol].re / pcplxSource[sourceRow][sourceCol].re;
                //System.out.println(mapScaleInt[y][x]);
                mapScaleComplex[sourceRow][sourceCol].re = pcplxSourceSigned[sourceRow][sourceCol].re / pcplxSource[sourceRow][sourceCol].re;
                mapScaleComplex[sourceRow][sourceCol].im = pcplxSourceSigned[sourceRow][sourceCol].im / pcplxSource[sourceRow][sourceCol].im;
            }
        }
        FFT2.IFFT_2(mapScaleComplex, height / 2);

        for (int y = 0; y < height / 2; y++) {
            for (int x = 0; x < weight / 2; x++) {
                
                if (mapScaleComplex[y][x].re > 255) {
                    mapScaleInt[y][x] = new Color(255, 0, 0).getRGB();
                    continue;
                }

                if (mapScaleComplex[y][x].re < 0) {
                    mapScaleInt[y][x] = new Color(0, 0, 0).getRGB();
                    continue;
                }
                mapScaleInt[y][x] = new Color((int) mapScaleComplex[y][x].re, 0, 0).getRGB();
            }
        }

        Vector<BufferedImage> resImage = new Vector();
        resImage.add(ImageToolKit.buildImageWithArray(mapScaleInt, BufferedImage.TYPE_3BYTE_BGR));
        return resImage;
    }

    public static int[][] reversalImage(int[][] source) {
        //ImageToolKit.printArray(source, ImageToolKit.DEC_MODE);
        int[][] target = new int[source.length][source[0].length];
        int gray, orginalGray = 0;
        for (int sourceRow = 0; sourceRow < source.length; sourceRow++) {
            for (int sourceCol = 0; sourceCol < source[0].length; sourceCol++) {
                orginalGray = (source[sourceRow][sourceCol]) & 0x000000FF;
                gray = 255 - 1 - orginalGray;  // s=L-1-r
                gray = (gray << 16) + (gray << 8) + gray;
                target[sourceRow][sourceCol] = gray;
            }
        }
        //ImageToolKit.printArray(target, ImageToolKit.DEC_MODE);
        return target;
    }

    public static int[][] logarithmImage(int[][] source) {
        //ImageToolKit.printArray(source, ImageToolKit.DEC_MODE);
        int[][] target = new int[source.length][source[0].length];
        int gray, orginalGray = 0;
        int contantFactorC = 1;
        for (int sourceRow = 0; sourceRow < source.length; sourceRow++) {
            for (int sourceCol = 0; sourceCol < source[0].length; sourceCol++) {
                orginalGray = (source[sourceRow][sourceCol]) & 0x000000FF;
                // s=((c*log(1+r) - c*log(1+0)) / (c*log(1+255) - c*log(1+0))) * 255
                //gray = 255 - 1 - orginalGray; 
                if (orginalGray == 0) {
                    gray = 0;
                } else {
                    gray = (int) (contantFactorC * (Math.log(orginalGray) / contantFactorC * 5.545) * 255);
                    //gray = (int) ((Math.log(1 + 10 * orginalGray) / Math.log(11)));
                    //log2(1 + v*f)/log2(v+1)
                }
                //System.out.println(orginalGray + "  " + gray);

                gray = (gray << 16) + (gray << 8) + gray;
                target[sourceRow][sourceCol] = gray;
            }
        }
        //ImageToolKit.printArray(target, ImageToolKit.DEC_MODE);
        return target;
    }

    public static void test(int a[]) {
        for (int i = 0; i < a.length; i++) {
            System.out.println(a[i]);
        }
    }

    public static void main(String[] args) {
        try {
            BufferedImage image = ImageIO.read(new FileInputStream(new File(ImageApp.SOURCE_IMAGE_FILE)));
            BufferedImage signedImage = ImageIO.read(new FileInputStream(new File(ImageApp.SOURCE_IMAGE_SIGN_FILE)));
            Vector<BufferedImage> tempVector = ImageToolKit.buildRFreqAmpSignImage(image, signedImage);
            int count = 0;
            for (BufferedImage temp : tempVector) {
                ImageIO.write(temp, "jpg", new File("test" + String.valueOf(count) + ".jpg"));
                count++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
