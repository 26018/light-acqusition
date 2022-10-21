package top.fixyou;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

public class Main {

    public static void main(String[] args) throws NativeHookException, IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, InterruptedException {
        // 关闭日志
        Logger.getLogger(GlobalScreen.class.getPackage().getName()).setLevel(Level.OFF);

        GlobalKeyManager keyManager = new GlobalKeyManager();
        CameraManager cameraManager = new CameraManager();

        // 开启环境光检测
        cameraManager.bind(keyManager);
        cameraManager.startCamera();

    }
}

