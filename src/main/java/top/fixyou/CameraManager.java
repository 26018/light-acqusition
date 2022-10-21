package top.fixyou;

import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import top.fixyou.utils.ComputeUtil;
import top.fixyou.utils.ConfigUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class CameraManager {

    public void startCamera() throws IOException {
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        Java2DFrameConverter converter = new Java2DFrameConverter();
        grabber.start();

        while (true) {
            Frame frame = grabber.grab();
            BufferedImage bufferedImage = converter.getBufferedImage(frame);
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int rgb = bufferedImage.getRGB(i, j);
                    int red = (rgb >> 16) & 0xFF;
                    int green = (rgb >> 8) & 0xFF;
                    int blue = (rgb) & 0xFF;
                    ComputeUtil.addPixel(red, green, blue);
                }
            }
            int grayValue = ComputeUtil.grayValue();
            BrightnessManager.setBrightnessByGrayValue(grayValue);
            try {
                Thread.sleep(ConfigUtil.get("gapTime", Integer.class));
            } catch (Exception e) {
                System.out.println("call me back");
                break;
            }
        }
        grabber.release();
        cameraExitWindow(true);
    }

    private static void cameraExitWindow(boolean showWindow) {
        int w = 300;
        int h = 200;
        JFrame jFrame = new JFrame("环境光采集");
        jFrame.setSize(w, h);
        double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        jFrame.setLocation((int) (width - w) / 2, (int) (height - h) / 2);
        jFrame.setAlwaysOnTop(true);
        jFrame.add(new TextField("关闭窗口退出程序"));
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(showWindow);
    }
}
