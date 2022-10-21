package top.fixyou;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import top.fixyou.utils.ConfigUtil;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;

public class GlobalKeyManager {
    private static HashSet<String> operations = new HashSet(16);
    private static boolean running;
    private static String[] exitKeys;
    private static KeyListener keyListener;

    GlobalKeyManager() throws NativeHookException, IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        this.start();
    }

    // 启动键盘监听
    public void start() throws NativeHookException, IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (keyListener == null) {
            keyListener = new KeyListener() {};
        }
        keyListener.register();
        setRunning(true);
        exitKeys = ConfigUtil.get("exitKey", String.class).split(",");
    }

    // 返回监听器状态
    public boolean running() {
        return running;
    }

    // 设定监听器状态
    private static void setRunning(boolean running) {
        GlobalKeyManager.running = running;
    }

    private interface KeyListener extends NativeKeyListener {
        default void register() throws NativeHookException {
            GlobalScreen.addNativeKeyListener(this);
            GlobalScreen.registerNativeHook();
        }
        default boolean exitAble(String[] keys){
            if (keys == null) {
                return false;
            }
            for (String key : keys) {
                if (!operations.contains(key)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        default void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
            int keyCode = nativeKeyEvent.getKeyCode();
            String keyText = NativeKeyEvent.getKeyText(keyCode);
            operations.add(keyText);
            if (exitAble(exitKeys)) {
                setRunning(false);
            }
        }

        @Override
        default void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {

        }

        @Override
        default void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {

        }
    }

}