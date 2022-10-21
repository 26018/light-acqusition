package top.fixyou;

import com.github.sarxos.webcam.Webcam;
import top.fixyou.utils.ComputeUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class CameraManager {
    private static Webcam camera;
    private static GlobalKeyManager exitKeyManager;

    public void startCamera() throws IOException {
        camera = Webcam.getDefault();
        camera.open();
        while (true) {
            BufferedImage image = camera.getImage();
            if (image != null) {
                int width = image.getWidth();
                int height = image.getHeight();
                for (int i = 0; i < width; i++) {
                    for (int j = 0; j < height; j++) {
                        int rgb = image.getRGB(i, j);
                        int red = (rgb >> 16) & 0xFF;
                        int green = (rgb >> 8) & 0xFF;
                        int blue = (rgb) & 0xFF;
                        ComputeUtil.addPixel(red, green, blue);
                    }
                }
                int grayValue = ComputeUtil.grayValue();
                BrightnessManager.setBrightnessByGrayValue(grayValue);
            }
            if (!exitKeyManager.running()) {
                break;
            }
        }
        camera.close();
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

    public void bind(GlobalKeyManager keyManager) {
        exitKeyManager = keyManager;
    }
}
