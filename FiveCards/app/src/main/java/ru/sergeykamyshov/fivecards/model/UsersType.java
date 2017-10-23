package ru.sergeykamyshov.fivecards.model;

import java.util.List;

/**
 * Класс описывает модель списка пользователей
 */
public class UsersType implements CardType {

    private List<String> mUsers;

    public UsersType(List<String> users) {
        mUsers = users;
    }

    public List<String> getUsers() {
        return mUsers;
    }

    public void setUsers(List<String> users) {
        mUsers = users;
    }
}
