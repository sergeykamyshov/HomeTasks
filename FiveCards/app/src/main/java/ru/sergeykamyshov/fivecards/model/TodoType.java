package ru.sergeykamyshov.fivecards.model;

/**
 * Класс описывает модель задачи списка дел
 */
public class TodoType implements CardType {

    private String mTitle;
    private boolean mCompleted;

    public TodoType(String title, boolean completed) {
        mTitle = title;
        mCompleted = completed;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public boolean isCompleted() {
        return mCompleted;
    }

    public void setCompleted(boolean completed) {
        mCompleted = completed;
    }
}
