package ru.sergeykamyshov.schedule.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static ru.sergeykamyshov.schedule.utils.DBUtils.PREFERENCES_FILENAME;
import static ru.sergeykamyshov.schedule.utils.DBUtils.PREFERENCES_JSON_MD5;

/**
 * Класс утилита для работы с файлами
 */
public class FileUtils {

    /**
     * Проверяет сгенерированный и сохраненный хеш файла. Если они отличаются, значит файл был изменен
     *
     * @param context - контекст для доступа к json файлу
     * @return true если файл был изменен
     */
    public static boolean isJsonFileChanged(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_FILENAME, Context.MODE_PRIVATE);
        String md5 = getMd5Hash(context);
        if (!preferences.getString(PREFERENCES_JSON_MD5, "").equals(md5)) {
            preferences.edit().putString(PREFERENCES_JSON_MD5, md5).apply();
            return true;
        }
        return false;
    }

    /**
     * Генерирует md5 хэш для json файла
     *
     * @param context - контекст для доступа к json файлу
     * @return md5 хэш
     */
    private static String getMd5Hash(Context context) {
        String md5Hash = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            InputStream fis = context.getAssets().open("allStations.json");
            byte[] dataBytes = new byte[1024];
            int nread = 0;
            while ((nread = fis.read(dataBytes)) != -1) {
                md.update(dataBytes, 0, nread);
            }
            byte[] digest = md.digest();
            md5Hash = new BigInteger(1, digest).toString(16);
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
        return md5Hash;
    }
}
