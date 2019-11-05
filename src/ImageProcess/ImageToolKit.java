package ImageProcess;

import ThirdCodes.BilinearInterpolation;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.Arrays;
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

    public static void printArray(int array[], int mode) {
        System.out.println("*************************");
        for (int rowX = 0; rowX < array.length; rowX++) {
            if (mode == HEX_MODE) {
                System.out.printf("0x%08x ", array[rowX]);
            } else {
                System.out.print(" " + array[rowX]);
            }
        }
        System.out.println("\n");
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

    public static Vector<BufferedImage> buildRFreqAmpImage(BufferedImage image) {
        return transferRFreqAmpImage(getArrayRGB(image));
    }

    public static BufferedImage buildHistogramImage(BufferedImage image) {
        return transferHistogramImage(getArrayRGB(image));
    }

    public static BufferedImage buildParticallyHistogramImage(BufferedImage image, int particallySize) {
        return transferParticallyHistogramEqualizationImage(getArrayRGB(image), particallySize);
    }

    public static BufferedImage buildEqualizationFilteringImage(BufferedImage image, int particallySize) {
        return ImageToolKit.buildImageWithArray(ImageToolKit.transferEqualizationFiltering(getArrayRGB(image), particallySize), BufferedImage.TYPE_BYTE_GRAY);
    }

    public static BufferedImage buildAddWeightFilteringImage(BufferedImage image, int particallySize) {
        if (particallySize > 3) {
            System.out.println("大于3X3的权值滤波都没实现");
            return image;
        }
        return ImageToolKit.buildImageWithArray(ImageToolKit.transferAddWeightFilteringFiltering(getArrayRGB(image), particallySize), BufferedImage.TYPE_BYTE_GRAY);
    }

    public static BufferedImage buildMiddleValueFilteringImage(BufferedImage image, int particallySize) {
        return ImageToolKit.buildImageWithArray(ImageToolKit.transferMiddleValueFilteringFiltering(getArrayRGB(image), particallySize), BufferedImage.TYPE_BYTE_GRAY);
    }

    public static Vector<BufferedImage> extractRFreqAmpSignImage(BufferedImage image, BufferedImage signImage) {
        return extractRFreqAmpSignImage(getArrayRGB(image), getArrayRGB(signImage));
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
    public static Vector<BufferedImage> extractRFreqAmpSignImage(int[][] source, int[][] signSourceImage) {
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
        resImage.add(ImageToolKit.buildImageWithArray(signedImage, BufferedImage.TYPE_3BYTE_BGR));
//        BufferedImage tempImage = null;
//        try {
//            tempImage = ImageToolKit.buildImageWithArray(signedImage, BufferedImage.TYPE_3BYTE_BGR);
//            ImageIO.write(tempImage, "BMP", new File("./test.bmp"));
//            tempImage = ImageIO.read(new FileInputStream(new File("./test.bmp")));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        source = ImageToolKit.getArrayRGB(tempImage);
//        for (int sourceRow = 0; sourceRow < source.length; sourceRow++) {
//            for (int sourceCol = 0; sourceCol < source[0].length; sourceCol++) {
//                value = ImageToolKit.getSingleRGBFromValue(source[sourceRow][sourceCol], ImageToolKit.R);
//                pcplx[sourceRow][sourceCol] = new Complex(value, 0);
//            }
//        }
        FFT2.FFT_2(pcplx, height);
//        Complex[] res = FFT2.twoDimensionalArraysToOneDimensionalArray(pcplx);
//        FFT2.FFT_2_Shift(res, height);  //分散于四个角的低频信号经过移位到中心后，变得更加的易于观察了
//        pcplx = FFT2.oneDimensionalArrayToTwoDimensionalArrays(res);

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

        //res = FFT2.twoDimensionalArraysToOneDimensionalArray(pcplx);
        //FFT2.FFT_2_Shift(res, height);  //分散于四个角的低频信号经过移位到中心后，变得更加的易于观察了
        //pcplx = FFT2.oneDimensionalArrayToTwoDimensionalArrays(res);
        resImage.add(ImageToolKit.buildImageWithArray(freqRedImage, BufferedImage.TYPE_3BYTE_BGR));
        resImage.add(ImageToolKit.buildImageWithArray(ampRedImage, BufferedImage.TYPE_3BYTE_BGR));
        return resImage;
    }

    public static BufferedImage transferHistogramImage(int[][] source) {
        return ImageToolKit.buildImageWithArray(transferHistogramEqualization(source), BufferedImage.TYPE_BYTE_GRAY);
    }

    public static BufferedImage transferParticallyHistogramEqualizationImage(int[][] source, int particallySize) {
        int[][] target = new int[source.length][source[0].length];
        int gray;
        int grayLevel;
        for (int sourceRow = 0; sourceRow < source.length; sourceRow++) {
            for (int sourceCol = 0; sourceCol < source[0].length; sourceCol++) {
                grayLevel = ImageToolKit.transferParticallyHistogramEqualization(source, sourceRow, sourceCol, particallySize);
                gray = (grayLevel << 16) + (grayLevel << 8) + grayLevel;
                target[sourceRow][sourceCol] = gray;
            }
        }
        return ImageToolKit.buildImageWithArray(target, BufferedImage.TYPE_BYTE_GRAY);
    }

    public static int[][] transferEqualizationFiltering(int[][] source, int particallySize) {
        int[][] target = new int[source.length][source[0].length];
        int gray;
        int grayLevel;
        for (int sourceRow = 0; sourceRow < source.length; sourceRow++) {
            for (int sourceCol = 0; sourceCol < source[0].length; sourceCol++) {
                grayLevel = ImageToolKit.transferEqualizationFiltering(source, sourceRow, sourceCol, particallySize);
                gray = (grayLevel << 16) + (grayLevel << 8) + grayLevel;
                target[sourceRow][sourceCol] = gray;
            }
        }
        return target;
    }

    public static int[][] transferAddWeightFilteringFiltering(int[][] source, int particallySize) {
        int[][] target = new int[source.length][source[0].length];
        int gray;
        int grayLevel;
        for (int sourceRow = 0; sourceRow < source.length; sourceRow++) {
            for (int sourceCol = 0; sourceCol < source[0].length; sourceCol++) {
                grayLevel = ImageToolKit.transferAddWeightFilteringFiltering(source, sourceRow, sourceCol, particallySize);
                gray = (grayLevel << 16) + (grayLevel << 8) + grayLevel;
                target[sourceRow][sourceCol] = gray;
            }
        }
        return target;
    }

    public static int[][] transferMiddleValueFilteringFiltering(int[][] source, int particallySize) {
        int[][] target = new int[source.length][source[0].length];
        int gray;
        int grayLevel;
        for (int sourceRow = 0; sourceRow < source.length; sourceRow++) {
            for (int sourceCol = 0; sourceCol < source[0].length; sourceCol++) {
                grayLevel = ImageToolKit.transferMiddleValueFilteringFiltering(source, sourceRow, sourceCol, particallySize);
                gray = (grayLevel << 16) + (grayLevel << 8) + grayLevel;
                target[sourceRow][sourceCol] = gray;
            }
        }
        return target;
    }

    public static int[][] transferHistogramEqualization(int[][] source) {
        int[][] target = new int[source.length][source[0].length];
        float[] histogramRate = new float[256];
        float[] p = new float[256];
        float[] s = new float[256];
        int total = source.length * source[0].length;
        for (int sourceRow = 0; sourceRow < source.length; sourceRow++) {
            for (int sourceCol = 0; sourceCol < source[0].length; sourceCol++) {
                histogramRate[source[sourceRow][sourceCol] & 0x000000FF]++;
            }
        }

        BigDecimal b;
        for (int i = 0; i < histogramRate.length; i++) {
            p[i] = histogramRate[i] / total;
            b = new BigDecimal(p[i]);
            p[i] = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
            if (i >= 1) {
                s[i] = p[i] + s[i - 1];
            } else {
                s[i] = p[i];
            }
            b = new BigDecimal(s[i]);
            s[i] = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        }
        int grayLevel, gray;
        int originalGrayLevel = 0;
        for (int sourceRow = 0; sourceRow < source.length; sourceRow++) {
            for (int sourceCol = 0; sourceCol < source[0].length; sourceCol++) {
                originalGrayLevel = source[sourceRow][sourceCol] & 0x000000FF;
                grayLevel = Math.round(s[originalGrayLevel] * 255);
                gray = (grayLevel << 16) + (grayLevel << 8) + grayLevel;
                target[sourceRow][sourceCol] = gray;
            }
        }
        return target;
    }

    public static int transferParticallyHistogramEqualization(int[][] source, int x, int y, int particallySize) {
        if (x < particallySize / 2 || y < particallySize / 2 || (x + particallySize / 2 >= source.length) || (y + particallySize / 2 >= source[0].length)) {
            //System.out.println("zhijie fanhui");
            return source[x][y];
        } else {
            //System.out.println("[" + x + "][" + y + "]");
            int[][] res = ImageToolKit.fetchParticallyArray(source, x, y, particallySize);
            //ImageToolKit.printArray(res, ImageToolKit.DEC_MODE);
            res = ImageToolKit.transferHistogramEqualization(res);
            //System.out.println(source[x][y] + ":" + res[particallySize / 2][particallySize / 2]);
            return res[particallySize / 2][particallySize / 2];
        }
    }

    public static int transferEqualizationFiltering(int[][] source, int x, int y, int particallySize) {
        if (x < particallySize / 2 || y < particallySize / 2 || (x + particallySize / 2 >= source.length) || (y + particallySize / 2 >= source[0].length)) {
            return (source[x][y] & 0x000000FF);
        } else {
            int[][] res = ImageToolKit.fetchParticallyArray(source, x, y, particallySize);
            /*
            1 1 1
            1 1 1
            1 1 1
             */
            int sum = 0;
            for (int sourceRow = 0; sourceRow < res.length; sourceRow++) {
                for (int sourceCol = 0; sourceCol < res[0].length; sourceCol++) {
                    sum += (res[sourceRow][sourceCol] & 0x000000FF);
                }
            }
            return sum / 9; // 9 是平均滤波9个1相加
        }
    }

    public static int transferAddWeightFilteringFiltering(int[][] source, int x, int y, int particallySize) {
        if (x < particallySize / 2 || y < particallySize / 2 || (x + particallySize / 2 >= source.length) || (y + particallySize / 2 >= source[0].length)) {
            return (source[x][y] & 0x000000FF);
        } else {
            int[][] res = ImageToolKit.fetchParticallyArray(source, x, y, particallySize);
            /*
            1 2 1
            2 4 2
            1 2 1
             */
            int[][] weight = new int[3][3];
            weight[0][0] = 1;
            weight[0][1] = 2;
            weight[0][2] = 1;
            weight[1][0] = 2;
            weight[1][1] = 4;
            weight[1][2] = 2;
            weight[2][0] = 1;
            weight[2][1] = 2;
            weight[2][2] = 1;

            int sum = 0;
            for (int sourceRow = 0; sourceRow < res.length; sourceRow++) {
                for (int sourceCol = 0; sourceCol < res[0].length; sourceCol++) {
                    sum += (res[sourceRow][sourceCol] & 0x000000FF) * weight[sourceRow][sourceCol];
                }
            }
            return sum / 16;  //16 是平均滤波9个权重相加
        }
    }

    public static int transferMiddleValueFilteringFiltering(int[][] source, int x, int y, int particallySize) {
        if (x < particallySize / 2 || y < particallySize / 2 || (x + particallySize / 2 >= source.length) || (y + particallySize / 2 >= source[0].length)) {
            return (source[x][y] & 0x000000FF);
        } else {
            int[][] res = ImageToolKit.fetchParticallyArray(source, x, y, particallySize);
            for (int sourceRow = 0; sourceRow < res.length; sourceRow++) {
                for (int sourceCol = 0; sourceCol < res[0].length; sourceCol++) {
                    res[sourceRow][sourceCol] = res[sourceRow][sourceCol] & 0x000000FF;
                }
            }
            int[] temp = FFT2.twoDimensionalArraysToOneDimensionalArray(res);
            Arrays.sort(temp);
            return temp[temp.length / 2 + 1];
        }
    }

    public static int[][] fetchParticallyArray(int[][] source, int x, int y, int particallySize) {
        int[][] target = new int[particallySize][particallySize];
        int[] factor = new int[particallySize];
        int counter = 0;
        for (int i = factor.length - 1; i >= 0; i--) {
            factor[i] = particallySize / 2 - counter;
            counter++;
        }
//        for (int i = 0; i < factor.length; i++) {
//            System.out.println(factor[i]);
//        }
        int x_factor_position = 0;
        int y_factor_position = 0;
        int temp_x = 0;
        int temp_y = 0;
        //System.out.println("[" + x + "][" + y + "]");
        for (int sourceRow = 0; sourceRow < target.length; sourceRow++) {
            for (int sourceCol = 0; sourceCol < target[0].length; sourceCol++) {
                //System.out.println(x_factor_position);
                temp_x = x + factor[x_factor_position];
                temp_y = y + factor[y_factor_position];
                //System.out.println("[" + temp_x + "][" + temp_y + "]");
                target[sourceRow][sourceCol] = source[temp_x][temp_y];
                y_factor_position++;
            }
            x_factor_position++;
            y_factor_position = 0;
        }

        return target;
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
                gray = (gray << 16) + (gray << 8) + gray;
                target[sourceRow][sourceCol] = gray;
            }
        }
        //ImageToolKit.printArray(target, ImageToolKit.DEC_MODE);
        return target;
    }

    public static void main(String[] args) {
        try {
//            BufferedImage image = ImageIO.read(new FileInputStream(new File(ImageApp.SOURCE_IMAGE_FILE)));
//            BufferedImage signedImage = ImageIO.read(new FileInputStream(new File(ImageApp.SOURCE_IMAGE_SIGN_FILE)));
//            Vector<BufferedImage> tempVector = ImageToolKit.extractRFreqAmpSignImage(image, signedImage);
//            int count = 0;
//            for (BufferedImage temp : tempVector) {
//                ImageIO.write(temp, "jpg", new File("test" + String.valueOf(count) + ".jpg"));
//                count++;
//            }
            int[][] source = new int[5][5];
            for (int sourceRow = 0; sourceRow < source.length; sourceRow++) {
                for (int sourceCol = 0; sourceCol < source[0].length; sourceCol++) {
                    source[sourceRow][sourceCol] = 0x000a0a0a;
                }
            }
            source[0][0] = 0x00010101;
            source[0][1] = 0x00010101;
            source[1][0] = 0x00010101;
            source[1][1] = 0x00010101;
            source[2][0] = 0x00010101;
            source[2][1] = 0x00010101;
            source[3][0] = 0x00010101;
            source[3][1] = 0x00010101;
            source[4][0] = 0x00010101;
            source[4][1] = 1;
            ImageToolKit.printArray(source, ImageToolKit.HEX_MODE);
            ImageToolKit.printArray(ImageToolKit.transferEqualizationFiltering(source, 3), ImageToolKit.HEX_MODE);

//            int num = 1;
//            for (int sourceRow = 0; sourceRow < source.length; sourceRow++) {
//                for (int sourceCol = 0; sourceCol < source[0].length; sourceCol++) {
//                    source[sourceRow][sourceCol] = num++;
//                }
//            }
//            ImageToolKit.printArray(source, ImageToolKit.DEC_MODE);
//            int[][] target = new int[source.length][source[0].length];
//            for (int sourceRow = 0; sourceRow < source.length; sourceRow++) {
//                for (int sourceCol = 0; sourceCol < source[0].length; sourceCol++) {
//                    target[sourceRow][sourceCol] = ImageToolKit.transferParticallyHistogramEqualization(source, sourceRow, sourceCol, 3);
//                }
//            }
            //ImageToolKit.printArray(ImageToolKit.fetchParticallyArray(source, 5, 5, 3), ImageToolKit.DEC_MODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
