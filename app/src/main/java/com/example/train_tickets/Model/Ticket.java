package com.example.train_tickets.Model;

import androidx.annotation.Nullable;

public class Ticket {
    private int id;
    private String time;
    private String date;
    private String from;
    private String where;
    @Nullable
    private int idUser;

    public Ticket(String time, String date, String from, String where) {
        this.time = time;
        this.date = date;
        this.from = from;
        this.where = where;
    }

    public Ticket() { }


    public int getId() {return id;}
    public String getTime() {return time;}
    public String getDate() {return date;}
    public String getFrom() {return from;}
    public String getWhere() {return where;}
    public int getIdUser() {return idUser;}

    public void setId(int id) {this.id = id;}
    public void setTime(String time) {this.time = time;}
    public void setDate(String date) {this.date = date;}
    public void setFrom(String from) {this.from = from;}
    public void setWhere(String where) {this.where = where;}
    public void setIdUser(int idUser) {this.idUser = idUser;}
}
