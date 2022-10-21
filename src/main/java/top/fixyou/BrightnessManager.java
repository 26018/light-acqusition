package top.fixyou;

import top.fixyou.utils.ComputeUtil;
import top.fixyou.utils.ConfigUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;

public class BrightnessManager {

    private static int currentLight = -1;
    private static int floatLight;
    private static int lightMax;

    static {
        try {
            floatLight = ConfigUtil.get("floatLight", Integer.class);
            lightMax = ConfigUtil.get("lightMax", Integer.class);
        } catch (IOException | NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setBrightnessByGrayValue(int grayValue) throws IOException {
        if (changeLightValue(grayValue)) {
            currentLight = grayValue;
            int percentage = Math.min(grayValue, lightMax) * 100 / lightMax;
            System.out.println(percentage);
            setBrightnessValue(percentage);
        }
    }

    private static void setBrightnessValue(int brightnessValue) throws IOException {
        //Creates a powerShell command that will set the brightness to the requested value (0-100),after the requested delay (in milliseconds) has passed.
        String s = String.format("$brightness = %d;", brightnessValue)
                + "$delay = 1;"
                + "$myMonitor = Get-WmiObject -Namespace root\\wmi -Class WmiMonitorBrightnessMethods;"
                + "$myMonitor.WmiSetBrightness($delay,$brightness)";
        String command = "powershell.exe  " + s;
        Runtime.getRuntime().exec(command);
    }

    private static boolean changeLightValue(int lightValue) {
        int distance = Math.abs(lightValue - currentLight);
        return distance > floatLight;
    }
}