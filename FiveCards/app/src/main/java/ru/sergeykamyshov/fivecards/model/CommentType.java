package ru.sergeykamyshov.fivecards.model;

/**
 * Класс описывает модель комментария
 */
public class CommentType implements CardType {

    private int mId;
    private String mName;
    private String mEmail;
    private String mBody;

    public CommentType(int id, String name, String email, String body) {
        mId = id;
        mName = name;
        mEmail = email;
        mBody = body;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getBody() {
        return mBody;
    }

    public void setBody(String body) {
        mBody = body;
    }
}
