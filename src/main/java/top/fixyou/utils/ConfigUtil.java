package top.fixyou.utils;


import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.channels.FileLockInterruptionException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class ConfigUtil {

    private final static String path = "./config";

    private static Map<String, String> map;

    public static <T> T get(String key,Class<T> clazz) throws IOException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (map == null) {
            map = readConfigMap();
        }
        String value = map.get(key);
        T ret = clazz.getConstructor(String.class).newInstance(value);
        return ret;
    }
    private static Map<String, String> readConfigMap() throws IOException {
        if (map != null) {
            return map;
        }
        File file = new File(path);
        if (!file.exists()) {
            boolean newFile = file.createNewFile();
            if (!newFile) {
                throw new FileLockInterruptionException();
            }
            defaultConfig();
        }
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        Stream<String> lines = bufferedReader.lines();
        map = new HashMap<>();
        lines.forEach(line -> {
            if (!line.startsWith("#")){
                String[] split = line.split("::");
                map.put(split[0], split[1]);
            }
        });
        bufferedReader.close();
        fileReader.close();
        return map;
    }
    private static void defaultConfig() throws IOException {
        FileWriter fileWriter = new FileWriter(path);
        fileWriter.write("# 键盘监听时间间隔 单位(ms)\n");
        fileWriter.write("checkTime::1000\n");
        fileWriter.write("# 亮度浮动\n");
        fileWriter.write("floatLight::2\n");
        fileWriter.write("# 最大灰度值\n");
        fileWriter.write("lightMax::120\n");
        fileWriter.write("# 环境光采集间隔时间 单位(ms)\n");
        fileWriter.write("gapTime::1\n");
        fileWriter.write("# 退出程序按键定义\n");
        fileWriter.write("exitKey::L,J,H\n");
        fileWriter.flush();
        fileWriter.close();
    }

}
