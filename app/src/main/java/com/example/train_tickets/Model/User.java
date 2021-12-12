package com.example.train_tickets.Model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class User {

    private int id;
    private String login;
    private String password;
    private String image;

    public User(String login, String password, String image) {
        this.login = login;
        this.password = password;
        this.image = image;
    }

    public User() { }


    public int getId() {return id;}
    public String getLogin() {return login;}
    public String getPassword() {return password;}
    public String getImage() {return image;}

    public void setId(int id) {this.id = id;}
    public void setLogin(String login) {this.login = login;}
    public void setPassword(String password) {this.password = password;}
    public void setImage(String image) {this.image = image;}
}
