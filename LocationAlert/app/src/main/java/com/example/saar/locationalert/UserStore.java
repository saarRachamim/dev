package com.example.saar.locationalert;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by Saar on 1/9/2016.
 */
public class UserStore {
    public static final String SP_NAME = "userDetails";
    SharedPreferences userLoaclDB;

    public UserStore(Context context)
    {
        userLoaclDB = context.getSharedPreferences(SP_NAME, 0);
    }

    public void saveUserInLocalDB(User user)
    {
        Editor localEditor = userLoaclDB.edit();
        localEditor.putString("name", user.name);
        localEditor.putString("userName", user.userName);
        localEditor.putString("password", user.password);
        localEditor.commit();
    }

    public User getLoggenInUser()
    {
        String name = userLoaclDB.getString("name", "");
        String userName = userLoaclDB.getString("userName", "");
        String password = userLoaclDB.getString("password", "");
        return new User(name, userName, password);
    }

    public void setIsUserLoggedIn(boolean isLoggedIn)
    {
        Editor localEditor = userLoaclDB.edit();
        localEditor.putBoolean("isLoggedIn", isLoggedIn);
        localEditor.commit();
    }

    public boolean isUserLoggedIn()
    {
        return userLoaclDB.getBoolean("isLoggedIn", false);
    }

    public void clearData()
    {
        Editor localEditor = userLoaclDB.edit();
        localEditor.clear();
        localEditor.commit();
    }
}
