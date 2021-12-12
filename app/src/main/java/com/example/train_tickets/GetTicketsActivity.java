package com.example.train_tickets;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.train_tickets.Database.DbHelper;
import com.example.train_tickets.Database.TicketDB;
import com.example.train_tickets.Model.Ticket;
import com.example.train_tickets.Model.User;

import java.util.ArrayList;

public class GetTicketsActivity extends AppCompatActivity {
    RecyclerView ticketsList;
    DbHelper dbHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;

    ArrayList<Ticket> tempTickets;
    ArrayList<Ticket> tickets = new ArrayList<>();
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_tickets);
        ticketsList = findViewById(R.id.list);
        ticketsList.setLayoutManager(new LinearLayoutManager(this));
        dbHelper = new DbHelper(getApplicationContext());
        db = dbHelper.getReadableDatabase();
        user = JSONHelper.importFromJSONExternal(this).get(0);
        tempTickets = new ArrayList<>();

        userCursor = db.rawQuery("select * from TICKET where IDUSER is NULL", null);
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

        ticketsList.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        TextView textViewFromWhere = (TextView) view.findViewById(R.id.textViewFromWhere);

                        textViewFromWhere.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openOptionMenu(v,position);
                            }
                        });
                    }
                })
        );

        CalendarView cw = (CalendarView) findViewById(R.id.calendarView);
        cw.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                int mYear = year;
                int mMonth = month;
                int mDay = dayOfMonth;
                String selectedDate = new StringBuilder().append(mDay).append(".").append(mMonth + 1).append(".").append(mYear).toString();


                for (Ticket t : tickets) {
                    if(t.getDate().equals(selectedDate))
                        tempTickets.add(t);
                }
                TicketAdapter ticketAdapter = new TicketAdapter(tempTickets);
                ticketsList.setAdapter(ticketAdapter);
            }
        });
    }
    public void openOptionMenu(View v,final int position){
        PopupMenu popup = new PopupMenu(v.getContext(), v);
        popup.getMenuInflater().inflate(R.menu.popupmenu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                TicketDB ticketDB = new TicketDB();
                ticketDB.addUserID(db,tempTickets.get(position).getId(),user.getId());
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                return true;
            }
        });
        popup.show();
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
}