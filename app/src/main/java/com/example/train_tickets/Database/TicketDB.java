package com.example.train_tickets.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.train_tickets.Model.Ticket;
import com.example.train_tickets.Model.User;

public class TicketDB {
    private static final String TICKET_TABLE = "TICKET";


    public static void add(SQLiteDatabase db, Ticket ticket) {
        ContentValues values = new ContentValues();

        db.execSQL("INSERT INTO TICKET (TIME, DATE, [FROM], [WHERE]) VALUES ('" + ticket.getTime() + "', '" + ticket.getDate() + "', '" + ticket.getFrom() + "', '" + ticket.getWhere() + "');");
    }

    public static Cursor getAll(SQLiteDatabase db) {
        return db.rawQuery("select * from " + TICKET_TABLE + ";", null);
    }

    public static long addUserID(SQLiteDatabase db, int ticketID, int userID) {
        ContentValues values = new ContentValues();
        values.put("IDUSER", userID);
        return db.update(TICKET_TABLE, values, "_id = ?", new String[] { String.valueOf(ticketID) });
    }
}
