package top.fixyou;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import top.fixyou.utils.ConfigUtil;

public class Main {

    public static void main(String[] args) throws NativeHookException, IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, InterruptedException {
        // 关闭日志
        Logger.getLogger(GlobalScreen.class.getPackage().getName()).setLevel(Level.OFF);

        GlobalKeyManager keyManager = new GlobalKeyManager();
        CameraManager cameraManager = new CameraManager();

        Thread currentThread = Thread.currentThread();
        // 开启键盘监听
        Thread keyListener = new Thread(() -> {
            while (true) {
                if (!keyManager.running()) {
                    currentThread.interrupt();
                }
                try {
                    Thread.sleep(ConfigUtil.get("checkTime", Long.class));
                } catch (InterruptedException | IOException | NoSuchMethodException | InvocationTargetException |
                         InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        keyListener.start();

        // 开启环境光检测
        cameraManager.startCamera();
    }
}

