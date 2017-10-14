package ru.sergeykamyshov.schedule.utils;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

// TODO: удалить, если не найду способ как использовать настройку
public class FileUtils {

    public static final String CONFIG_PROPERTIES_FILENAME = "config.properties";
    public static final String UPDATE_FROM_JSON_PROPERTIE = "db.needUpdateFromJSON";

    public static void changeDbPropertieToFalse(Context context) {
        FileOutputStream outputStream = null;
        try {
            outputStream = context.openFileOutput(CONFIG_PROPERTIES_FILENAME, Context.MODE_PRIVATE);
            outputStream.write("db.needUpdateFromJSON".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean isDbPropertieEqulasTrue(Context context) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = context.openFileInput(CONFIG_PROPERTIES_FILENAME);
            Properties properties = new Properties();
            properties.load(fileInputStream);
            return Boolean.valueOf(properties.getProperty(UPDATE_FROM_JSON_PROPERTIE));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}
