package ru.sergeykamyshov.fivecards.model;

/**
 * Класс описывает модель поста
 */
public class PostType implements CardType {

    private int mId;
    private String mTitle;
    private String mBody;

    public PostType(int id, String title, String body) {
        mId = id;
        mTitle = title;
        mBody = body;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getBody() {
        return mBody;
    }

    public void setBody(String body) {
        mBody = body;
    }
}
