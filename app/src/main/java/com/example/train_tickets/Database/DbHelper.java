package com.example.train_tickets.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.train_tickets.Model.Ticket;
import com.example.train_tickets.Model.User;

public class DbHelper extends SQLiteOpenHelper {

    private static final int SCHEMA = 1;
    private static final String DATABASE_NAME = "TICKETSDB";
    public static final String USER_TABLE = "USER";
    public static final String TICKET_TABLE = "TICKET";

    private static DbHelper instance = null;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }


    public static DbHelper getInstance(Context context) {
        if(instance == null) instance = new DbHelper(context);
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + USER_TABLE + " (                    "
                    + "_id integer primary key autoincrement not null,"
                    + "LOGIN text not null,                              "
                    + "PASSWORD text not null,                           "
                    + "IMAGE text not null                             );"
        );
        db.execSQL("create table " + TICKET_TABLE + " (                           "
                    + "_id integer primary key autoincrement not null,       "
                    + "IDUSER integer,                                            "
                    + "TIME text not null,                                        "
                    + "DATE text not null,                                        "
                    + "[FROM] text not null,                                      "
                    + "[WHERE] text not null,                                     "
                    + "foreign key(IDUSER) references " + USER_TABLE + "(_id));"
        );

        TicketDB ticketDB = new TicketDB();
        for (int t = 13; t<31; t++){
            Ticket ticket1 = new Ticket("12:00",t+".12.2021","Minsk","Vitebsk");
            Ticket ticket2 = new Ticket("17:00",t+".12.2021","Vitebsk","Minsk");
            ticketDB.add(db,ticket1);
            ticketDB.add(db,ticket2);

        }
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists " + USER_TABLE);
        db.execSQL("drop table if exists " + TICKET_TABLE);
        onCreate(db);
    }
}
