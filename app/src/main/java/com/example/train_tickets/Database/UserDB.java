package com.example.train_tickets.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.train_tickets.Model.User;

import java.util.ArrayList;

public class UserDB {
    private static final String USER_TABLE = "USER";


    public static long add(SQLiteDatabase db, User user) {
        ContentValues values = new ContentValues();

        values.put("LOGIN", user.getLogin());
        values.put("PASSWORD", user.getPassword());
        values.put("IMAGE", user.getImage());

        return db.insert(USER_TABLE, null, values);
    }
    public User SelectUserByLoginAndPassword(SQLiteDatabase db, String login, String password)
    {
        User user = new User();
        String[] columns = new String[]{"_id", "LOGIN", "PASSWORD", "IMAGE"};
        Cursor userCursor;
        userCursor = db.query(DbHelper.USER_TABLE, columns, "LOGIN=? and PASSWORD=?", new String[]{login, password}, null, null, null);
        if(userCursor.getCount()!=0) {
            userCursor.moveToFirst();
            user.setId(userCursor.getInt(0));
            user.setLogin(userCursor.getString(1));
            user.setPassword(userCursor.getString(2));
            user.setImage(userCursor.getString(3));

            userCursor.close();
            return user;
        }
        else {
            userCursor.close();
            return null;
        }
    }
    public static Cursor getAll(SQLiteDatabase db) {
        return db.rawQuery("select * from " + USER_TABLE + ";", null);
    }
}
