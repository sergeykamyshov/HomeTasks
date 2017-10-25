package ru.sergeykamyshov.fivecards.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import ru.sergeykamyshov.fivecards.model.ImageType;
import ru.sergeykamyshov.fivecards.model.PostType;
import ru.sergeykamyshov.fivecards.model.TodoType;
import ru.sergeykamyshov.fivecards.model.UsersType;

public class QueryUtils {

    public static final String POST_TYPE = "postType";
    public static final String COMMENT_TYPE = "commentType";
    public static final String USERS_TYPE = "usersType";
    public static final String IMAGE_TYPE = "imageType";
    public static final String TODO_TYPE = "todoType";

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    private static final String HTTPS_REQUEST_URL = "https://jsonplaceholder.typicode.com";

    public static CardType fetchCardTypeData(String type) {
        return fetchCardTypeData(type, null);
    }

    public static CardType fetchCardTypeData(String type, String id) {
        URL url = createUrl(type, id);
        String jsonResult = makeHttpRequest(url, type);
        switch (type) {
            case POST_TYPE:
                return extractPosts(jsonResult, type);
            case COMMENT_TYPE:
                return extractComments(jsonResult, type);
            case USERS_TYPE:
                return extractUsers(jsonResult, type);
            case IMAGE_TYPE:
                return extractImage(jsonResult, type);
            case TODO_TYPE:
                return extractTodo(jsonResult, type);
            default:
                Log.i(LOG_TAG, "Cannot extract data. Unknown type " + type);
                return null;
        }
    }

    /**
     * Создает URL для конкретного типа карточки
     *
     * @param type - тип карточки
     * @return URL
     */
    private static URL createUrl(String type, String id) {
        URL url = null;
        try {
            switch (type) {
                case POST_TYPE:
                    url = new URL(HTTPS_REQUEST_URL + "/posts/" + id);
                    break;
                case COMMENT_TYPE:
                    url = new URL(HTTPS_REQUEST_URL + "/comments/" + id);
                    break;
                case USERS_TYPE:
                    url = new URL(HTTPS_REQUEST_URL + "/users");
                    break;
                case IMAGE_TYPE:
                    url = new URL(HTTPS_REQUEST_URL + "/photos/3");
                    break;
                case TODO_TYPE:
                    // Генерируем случайный id задачи
                    int randomTodoId = 1 + (int) (Math.random() * 200);
                    url = new URL(HTTPS_REQUEST_URL + "/todos/" + randomTodoId);
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
     * Парсит JSON строку для получения поста
     *
     * @param jsonResult - строка в формате JSON
     * @param type       - тип карточки, для которой выполнялся запрос
     * @return тип карточки - пост
     */
    private static CardType extractPosts(String jsonResult, String type) {
        CardType postType = new PostType(0, "", "");
        if (jsonResult == null || jsonResult.length() == 0) {
            return postType;
        }
        try {
            JSONObject postObject = new JSONObject(jsonResult);
            int id = postObject.getInt("id");
            String title = postObject.getString("title");
            String body = postObject.getString("body");
            return new PostType(id, title, body);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing JSON result for type " + type, e);
        }
        return postType;
    }

    /**
     * Парсит JSON строку для получения комментария
     *
     * @param jsonResult - строка в формате JSON
     * @param type       - тип карточки, для которой выполнялся запрос
     * @return тип карточки - комментарий
     */
    private static CardType extractComments(String jsonResult, String type) {
        CardType commentType = new CommentType(0, "", "", "");
        if (jsonResult == null || jsonResult.length() == 0) {
            return commentType;
        }
        try {
            JSONObject commentObject = new JSONObject(jsonResult);
            int id = commentObject.getInt("id");
            String name = commentObject.getString("name");
            String email = commentObject.getString("email");
            String body = commentObject.getString("body");
            commentType = new CommentType(id, name, email, body);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing JSON result for type " + type, e);
        }
        return commentType;
    }

    /**
     * Парсит JSON строку для получения пользователей
     *
     * @param jsonResult - строка в формате JSON
     * @param type       - тип карточки, для которой выполнялся запрос
     * @return тип карточки - пользователи
     */
    private static CardType extractUsers(String jsonResult, String type) {
        CardType usersType = new UsersType(new ArrayList<String>());
        if (jsonResult == null || jsonResult.length() == 0) {
            return usersType;
        }
        try {
            JSONArray usersArray = new JSONArray(jsonResult);
            List<String> usersNames = new ArrayList<>();
            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject userObject = (JSONObject) usersArray.get(i);
                String name = userObject.getString("name");
                usersNames.add(name);
            }
            usersType = new UsersType(usersNames);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing JSON result for type " + type, e);
        }
        return usersType;
    }

    /**
     * Парсит JSON строку для получения изображения
     *
     * @param jsonResult - строка в формате JSON
     * @param type       - тип карточки, для которой выполнялся запрос
     * @return тип карточки - изображение
     */
    private static CardType extractImage(String jsonResult, String type) {
        CardType imageType = new ImageType(null);
        if (jsonResult == null || jsonResult.length() == 0) {
            return imageType;
        }
        try {
            JSONObject imageObject = new JSONObject(jsonResult);
            String urlString = imageObject.getString("url");

            // Загружаем изображение по полученной ссылке
            InputStream stream = new URL(urlString).openConnection().getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(stream);
            imageType = new ImageType(bitmap);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing JSON result for type " + type, e);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem getting image from connection for type " + type, e);
        }
        return imageType;
    }

    /**
     * Парсит JSON строку для получения задачи из списка дел
     *
     * @param jsonResult - строка в формате JSON
     * @param type       - тип карточки, для которой выполнялся запрос
     * @return тип карточки - задача из список дел
     */
    private static CardType extractTodo(String jsonResult, String type) {
        CardType todoType = new TodoType("", false);
        if (jsonResult == null || jsonResult.length() == 0) {
            return todoType;
        }
        try {
            JSONObject todoObject = new JSONObject(jsonResult);
            String title = todoObject.getString("title");
            boolean isCompleted = todoObject.getBoolean("completed");
            todoType = new TodoType(title, isCompleted);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing JSON result for type " + type, e);
        }
        return todoType;
    }

}
