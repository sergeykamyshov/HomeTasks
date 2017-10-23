package ru.sergeykamyshov.fivecards.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import ru.sergeykamyshov.fivecards.model.CardType;
import ru.sergeykamyshov.fivecards.model.CommentType;
import ru.sergeykamyshov.fivecards.model.PostType;
import ru.sergeykamyshov.fivecards.model.UsersType;

public class QueryUtils {

    public static final String POST_TYPE = "postType";
    public static final String COMMENT_TYPE = "commentType";
    public static final String USERS_TYPE = "usersType";

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    private static final String HTTPS_REQUEST_URL = "https://jsonplaceholder.typicode.com";

    public static List<CardType> fetchCardTypeData(String type) {
        URL url = createUrl(type);
        String jsonResult = makeHttpRequest(url, type);
        switch (type) {
            case POST_TYPE:
                return extractPosts(jsonResult, type);
            case COMMENT_TYPE:
                return extractComments(jsonResult, type);
            case USERS_TYPE:
                return extractUsers(jsonResult, type);
            default:
                Log.i(LOG_TAG, "Cannot extract data. Unknown type " + type);
                return new ArrayList<>();
        }
    }

    /**
     * Создает URL для конкретного типа карточки
     *
     * @param type - тип карточки
     * @return URL
     */
    private static URL createUrl(String type) {
        URL url = null;
        try {
            switch (type) {
                case POST_TYPE:
                    url = new URL(HTTPS_REQUEST_URL + "/posts");
                    break;
                case COMMENT_TYPE:
                    url = new URL(HTTPS_REQUEST_URL + "/comments");
                    break;
                case USERS_TYPE:
                    url = new URL(HTTPS_REQUEST_URL + "/users");
                    break;
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the url for type: " + type, e);
        }
        return url;
    }

    /**
     * Делает запрос к серверу и считывает результат из потока данных
     *
     * @param type - тип карточки, для которого выполняется зарпос
     * @param url  - URL запроса
     * @return результат полученный от сервера в формате JSON
     */
    private static String makeHttpRequest(URL url, String type) {
        String jsonResult = "";
        if (url == null) {
            return jsonResult;
        }

        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
                jsonResult = readFromStream(inputStream);
            } else {
                Log.i(LOG_TAG, "Error response code: " + httpURLConnection.getResponseCode() + ", for type " + type);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving JSON result from request for type " + type, e);
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error when try to close InputStream", e);
                }
            }
        }
        return jsonResult;
    }

    /**
     * Читает данные из потока и преобразует их в строку
     *
     * @param inputStream - поток с информацией в формате JSON
     * @return строка с информацией в формате JSON
     * @throws IOException
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                output.append(line);
            }
        }
        return output.toString();
    }

    /**
     * Парсит JSON строку для получения списка постов
     *
     * @param jsonResult - строка в формате JSON
     * @param type       - тип карточки, для которой выполнялся запрос
     * @return список постов
     */
    private static List<CardType> extractPosts(String jsonResult, String type) {
        List<CardType> postTypes = new ArrayList<>();
        if (jsonResult == null || jsonResult.length() == 0) {
            return postTypes;
        }
        try {
            JSONArray postsArray = new JSONArray(jsonResult);
            for (int i = 0; i < postsArray.length(); i++) {
                JSONObject postObject = (JSONObject) postsArray.get(i);
                int id = postObject.getInt("id");
                String title = postObject.getString("title");
                String body = postObject.getString("body");
                postTypes.add(new PostType(id, title, body));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing JSON result for type " + type);
        }
        return postTypes;
    }

    /**
     * Парсит JSON строку для получения списка комментариев
     *
     * @param jsonResult - строка в формате JSON
     * @param type       - тип карточки, для которой выполнялся запрос
     * @return список комментариев
     */
    private static List<CardType> extractComments(String jsonResult, String type) {
        List<CardType> commentTypes = new ArrayList<>();
        if (jsonResult == null || jsonResult.length() == 0) {
            return commentTypes;
        }
        try {
            JSONArray commentsArray = new JSONArray(jsonResult);
            for (int i = 0; i < commentsArray.length(); i++) {
                JSONObject commentObject = (JSONObject) commentsArray.get(i);
                int id = commentObject.getInt("id");
                String name = commentObject.getString("name");
                String email = commentObject.getString("email");
                String body = commentObject.getString("body");
                commentTypes.add(new CommentType(id, name, email, body));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing JSON result for type " + type);
        }
        return commentTypes;
    }

    /**
     * Парсит JSON строку для получения списка пользователей
     *
     * @param jsonResult - строка в формате JSON
     * @param type       - тип карточки, для которой выполнялся запрос
     * @return список пользователей
     */
    private static List<CardType> extractUsers(String jsonResult, String type) {
        List<CardType> usersTypes = new ArrayList<>();
        if (jsonResult == null || jsonResult.length() == 0) {
            return usersTypes;
        }
        try {
            JSONArray usersArray = new JSONArray(jsonResult);
            List<String> usersNames = new ArrayList<>();
            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject userObject = (JSONObject) usersArray.get(i);
                String name = userObject.getString("name");
                usersNames.add(name);
            }
            usersTypes.add(new UsersType(usersNames));
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing JSON result for type " + type);
        }
        return usersTypes;
    }
}
