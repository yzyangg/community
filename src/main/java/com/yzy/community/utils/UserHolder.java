package com.yzy.community.utils;


import com.yzy.community.model.entity.User;

/**
 * @author: yzy
 **/
public class UserHolder {

    private static final ThreadLocal<User> users = new ThreadLocal<>();

    public static void setUser(User user) {
        users.set(user);
    }

    public static User getUser() {
        return users.get();
    }

    public static void clear() {
        users.remove();
    }
}
