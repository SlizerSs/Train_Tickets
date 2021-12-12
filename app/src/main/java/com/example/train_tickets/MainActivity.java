package com.example.train_tickets;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.example.train_tickets.Database.DbHelper;
import com.example.train_tickets.Model.Ticket;
import com.example.train_tickets.Model.User;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView ticketsList;
    DbHelper dbHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;

    ArrayList<Ticket> tickets = new ArrayList<>();
    TextView textView;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ticketsList = findViewById(R.id.list);
        ticketsList.setLayoutManager(new LinearLayoutManager(this));
        dbHelper = new DbHelper(getApplicationContext());
        db = dbHelper.getReadableDatabase();
        user = JSONHelper.importFromJSONExternal(this).get(0);

        textView = findViewById(R.id.textView);
        userCursor = db.rawQuery("select * from TICKET where IDUSER = ?", new String[] {String.valueOf(user.getId())});
        userCursor.moveToFirst();
        while (!userCursor.isAfterLast()) {
            Ticket ticket = new Ticket();
            ticket.setId(userCursor.getInt(0));
            ticket.setIdUser(userCursor.getInt(1));
            ticket.setTime(userCursor.getString(2));
            ticket.setDate(userCursor.getString(3));
            ticket.setFrom(userCursor.getString(4));
            ticket.setWhere(userCursor.getString(5));
            userCursor.moveToNext();
            tickets.add(ticket);
        }
        TicketAdapter ticketAdapter = new TicketAdapter(tickets);
        ticketsList.setAdapter(ticketAdapter);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.myTickets :
                Intent intent1 = new Intent(this, MainActivity.class);
                startActivity(intent1);
                return true;
            case R.id.getTickets:
                Intent intent2 = new Intent(this, GetTicketsActivity.class);
                startActivity(intent2);
                return true;
            case R.id.logout:
                Intent intent3 = new Intent(this, LoginActivity.class);
                startActivity(intent3);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        // Закрываем подключение и курсор
        db.close();
        userCursor.close();
    }
}