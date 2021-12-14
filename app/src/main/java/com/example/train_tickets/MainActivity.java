package com.example.train_tickets;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.train_tickets.Database.DbHelper;
import com.example.train_tickets.Model.Ticket;
import com.example.train_tickets.Model.User;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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

    String apikey = "fedc36ed7df3300b6db8c1a6a622740b";
    //погода
    String city = "Minsk";
    String readyurl = "https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+apikey+"&units=metric&lang=ru";
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
            case R.id.getTime:
                try {
                    new GetURLData().execute(readyurl);

                } catch (Exception e) {
                    e.printStackTrace();
                }
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
    private class GetURLData extends AsyncTask<String, String, String> {


        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line=reader.readLine()) != null)
                    buffer.append(line).append("\n");

                return buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(connection != null)
                    connection.disconnect();

                try {
                    if (reader != null) {
                        reader.close();
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                Toast toast = Toast.makeText(getApplicationContext(), jsonObject.getString("name") + " " + jsonObject.getJSONObject("main").getDouble("temp") + " " + jsonObject.getJSONArray("weather").getJSONObject(0).getString("description"),Toast.LENGTH_SHORT);
                toast.show();
            } catch(Exception e) {

            }
        }
    }
}