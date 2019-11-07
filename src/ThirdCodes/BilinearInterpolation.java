package ThirdCodes;

import ImageProcess.ImageToolKit;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BilinearInterpolation {
    /* gets the 'n'th byte of a 4-byte integer */

    public static int get(int self, int n) {
        return (self >> (n * 8)) & 0xFF;
    }

    public static float lerp(float s, float e, float t) {
        return s + (e - s) * t;
    }

    public static float blerp(final Float c00, float c10, float c01, float c11, float tx, float ty) {
        return lerp(lerp(c00, c10, tx), lerp(c01, c11, tx), ty);
    }

    private static BufferedImage scale(BufferedImage self, float scaleX, float scaleY) {

        System.out.println(self.getType());
        //ImageToolKit.printArray(ImageToolKit.getArrayRGB(self), ImageToolKit.HEX_MODE);
        int newWidth = (int) (self.getWidth() * scaleX);
        int newHeight = (int) (self.getHeight() * scaleY);
        BufferedImage newImage = new BufferedImage(newWidth, newHeight, self.getType());
        for (int x = 0; x < newWidth; ++x) {
            for (int y = 0; y < newHeight; ++y) {
                float gx = ((float) x) / newWidth * (self.getWidth() - 1);
                float gy = ((float) y) / newHeight * (self.getHeight() - 1);
                int gxi = (int) gx;
                int gyi = (int) gy;
                int rgb = 0;
                //System.out.println(x + " " + y);
                //System.out.println(gx + " " + gy);
                //System.out.println("[" + gxi + "," + gyi + "],[" + (gxi + 1) + "," + gyi + "],[" + gxi + "," + (gyi + 1) + "],[" + (gxi + 1) + "," + (gyi + 1) + "]");
                int c00 = self.getRGB(gxi, gyi);
                int c10 = self.getRGB(gxi + 1, gyi);
                int c01 = self.getRGB(gxi, gyi + 1);
                int c11 = self.getRGB(gxi + 1, gyi + 1);
                for (int i = 0; i <= 2; ++i) {
                    float b00 = get(c00, i);
                    float b10 = get(c10, i);
                    float b01 = get(c01, i);
                    float b11 = get(c11, i);
                    int ble = ((int) blerp(b00, b10, b01, b11, gx - gxi, gy - gyi)) << (8 * i);
                    rgb = rgb | ble;
                }
                newImage.setRGB(x, y, rgb);
            }
        }
        ImageToolKit.printArray(ImageToolKit.getArrayRGB(newImage), ImageToolKit.HEX_MODE);
        return newImage;
    }

    public static void main(String[] args) throws IOException {
        File lenna = new File("flower.jpg");
        BufferedImage image = ImageIO.read(lenna);
        BufferedImage image2 = scale(image, 2.0f, 2.0f);
        File lenna2 = new File("flower0001.jpg");
        ImageIO.write(image2, "jpg", lenna2);
    }
}
