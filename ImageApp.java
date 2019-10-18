package ImageProcess;

import static ImageProcess.ImageToolKit.getArrayRGB;
import static ImageProcess.ImageToolKit.logarithmImage;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ImageApp extends JFrame implements ItemListener {

    private final ImagePanel imagePanel;
    private final JComboBox imageCommandList;
    private final String[] COMMANDDESCRIPTION = new String[]{"--imageCommand--", "还原", "普通放大", "二维插值放大", "灰度图", "图像反转",
        "对数图像变化", "RGB分离", "傅里叶变化", "签名(红色(R)通道)-傅里叶变化","签名(RGB通道)-傅里叶变化"
    };
    public static final String SOURCE_IMAGE_FILE = ".\\res\\image_1.jpg";
    public static final String SOURCE_IMAGE_SIGNED_FILE = ".\\res\\image_signed_1.jpg";
    public static final String SOURCE_IMAGE_X_FILE = ".\\res\\\\image_x.jpeg";
    public static final String SOURCE_IMAGE_FOURIER_FILE = ".\\res\\\\fourier_1.jpg";
    public static final String SOURCE_IMAGE_SIGN_FILE = ".\\res\\sign_image.jpg";
     public static final String SOURCE_IMAGE_SIGN_1_FILE = ".\\res\\sign_image_1.jpg";
    private static final int PANEL_WIDTH = 1200;
    private static final int PANEL_HEIGHT = 1200;

    public ImageApp() {

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 1200);
        //setResizable(false);
        imagePanel = new ImagePanel(PANEL_WIDTH, PANEL_HEIGHT);
        imagePanel.setBounds(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
        imageCommandList = new JComboBox();
        imageCommandList.addItemListener(this);
        for (int i = 0; i < COMMANDDESCRIPTION.length; i++) {
            imageCommandList.addItem(COMMANDDESCRIPTION[i]);
        }
        getContentPane().add(imagePanel, BorderLayout.CENTER);
        getContentPane().add(imageCommandList, BorderLayout.NORTH);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        BufferedImage tempImage,tempImage1;
        Vector<BufferedImage> tempVector;
        if (e.getStateChange() == ItemEvent.SELECTED) {
            try {
                //System.out.println("---ItemEvent performed:index:" + imageCommandList.getSelectedIndex() + ":,description:" + e.paramString() + "---");
                switch (imageCommandList.getSelectedIndex()) {
                    case 0:
                        break;
                    case 1: //"还原"
                        imagePanel.restoreImageList();
                        imagePanel.reFresh();
                        break;
                    case 2: //  "普通放大"
                        imagePanel.setChangedImg(ImageToolKit.zoomOutNormal(imagePanel.getSourceImg()));
                        imagePanel.reFresh();
                        break;
                    case 3: //"二维插值放大"
                        imagePanel.setChangedImg(ImageToolKit.zoomOutChaZhi(imagePanel.getSourceImg()));
                        imagePanel.reFresh();
                        break;
                    case 4: //"灰度图"
                        imagePanel.setChangedImg(ImageToolKit.buildGreyImage(imagePanel.getSourceImg()));
                        imagePanel.reFresh();
                        break;
                    case 5: //"图像反转"
                        //imagePanel.setSourceImgBufferedImage(ImageToolKit.buildGreyImage(imagePanel.getSourceImg()));
                        tempImage = ImageIO.read(new FileInputStream(new File(ImageApp.SOURCE_IMAGE_X_FILE)));
                        imagePanel.setSourceImgBufferedImage(ImageToolKit.buildGreyImage(tempImage));
                        imagePanel.addChangedImg(ImageToolKit.buildReversalmage(tempImage));
                        imagePanel.reFresh();
                        break;
                    case 6: // "对数图像变化"
                        tempImage = ImageIO.read(new FileInputStream(new File(ImageApp.SOURCE_IMAGE_FOURIER_FILE)));
                        imagePanel.setSourceImgBufferedImage(ImageToolKit.buildGreyImage(tempImage));
                        imagePanel.addChangedImg(ImageToolKit.buildImageWithArray(logarithmImage(getArrayRGB(ImageToolKit.buildGreyImage(tempImage))), BufferedImage.TYPE_BYTE_GRAY));
                        //imagePanel.addChangedImg(ImageToolKit.buildLogarithmlmage(temp1));
                        imagePanel.reFresh();
                        break;
                    case 7: //"RGB分离"
                        imagePanel.setChangedImg(ImageToolKit.buildRImage(imagePanel.getSourceImg()));
                        imagePanel.addChangedImg(ImageToolKit.buildBImage(imagePanel.getSourceImg()));
                        imagePanel.addChangedImg(ImageToolKit.buildGImage(imagePanel.getSourceImg()));
                        imagePanel.reFresh();
                        break;
                    case 8://"傅里叶变化"
                        imagePanel.restoreImageList();
                        tempVector = ImageToolKit.buildRFreqAmpImage(imagePanel.getSourceImg());
                        for (BufferedImage image : tempVector) {
                            imagePanel.addChangedImg(image);
                        }
                        imagePanel.reFresh();
                        break;
                    case 9://"签名(红色(R)通道)-傅里叶变化"
                        imagePanel.restoreImageList();
                        tempImage = ImageIO.read(new FileInputStream(new File(ImageApp.SOURCE_IMAGE_SIGN_FILE)));
                        tempVector = ImageToolKit.buildRFreqAmpSignImage(imagePanel.getSourceImg(), tempImage);
                        for (BufferedImage image : tempVector) {
                            imagePanel.addChangedImg(image);
                        }
                        imagePanel.reFresh();
                        break;
                    case 10://"签名(RGB通道)-傅里叶变化"
                        imagePanel.restoreImageList();
                        tempImage = ImageIO.read(new FileInputStream(new File(ImageApp.SOURCE_IMAGE_SIGN_1_FILE)));
                        tempVector = ImageToolKit.buildRGBFreqAmpSignImage(imagePanel.getSourceImg(), tempImage);
                        for (BufferedImage image : tempVector) {
                            imagePanel.addChangedImg(image);
                        }
                        imagePanel.reFresh();
                        break;
//                    case 11://"签名剥离"
//                        imagePanel.restoreImageList();
//                        tempImage = ImageIO.read(new FileInputStream(new File(".\\res\\sign_image.bmp")));
//                        tempImage1 = ImageIO.read(new FileInputStream(new File(ImageApp.SOURCE_IMAGE_FILE)));
//                        
//                        tempVector = ImageToolKit.extraceImagefronSignedImage(tempImage,tempImage1);
//                        for (BufferedImage image : tempVector) {
//                            imagePanel.addChangedImg(image);
//                        }
//                        imagePanel.reFresh();
//                        break;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}

class ImagePanel extends JPanel {

    private Vector<BufferedImage> imageList = new Vector();
    BufferedImage sourceImg;
    private Graphics g;
    private int width, height;

    public ImagePanel(int width, int height) {
        try {
            this.width = width;
            this.height = height;
            sourceImg = ImageIO.read(new FileInputStream(new File(ImageApp.SOURCE_IMAGE_FILE)));
            imageList.add(sourceImg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void restoreImageList() {
        this.imageList.clear();
        this.imageList.add(sourceImg);
    }

    public BufferedImage getSourceImg() {
        return this.sourceImg;
    }

    public void setSourceImgBufferedImage(BufferedImage sourceImg) {
        this.imageList.clear();
        this.imageList.add(sourceImg);
    }

    public void setChangedImg(BufferedImage changedImg) {
        this.imageList.clear();
        this.imageList.add(sourceImg);
        this.imageList.add(changedImg);
    }

    public void addChangedImg(BufferedImage changedImg) {
        this.imageList.add(changedImg);
    }

    public void reFresh() {
        this.repaint();
    }

    @Override
    public void paint(Graphics g) {
        try {
            super.paint(g);
            //g.setColor(Color.WHITE);
            //g.fillRect(0, 0, super.getWidth(), super.getHeight());
            int currentPositionX = 0, currentPositionY = 0;
            for (int i = 0; i < imageList.size(); i++) {
                if ((currentPositionX + imageList.get(i).getWidth()) > width) {
                    currentPositionX = 0;
                    currentPositionY += imageList.get(i - 1).getHeight();
                }
                g.drawImage(imageList.get(i), currentPositionX, currentPositionY, this);
                currentPositionX += imageList.get(i).getWidth();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
