package com.example.train_tickets;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.train_tickets.Database.DbHelper;
import com.example.train_tickets.Database.UserDB;
import com.example.train_tickets.Model.User;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private EditText loginField,passwordField;
    public static String log_name;
    DbHelper databaseHelper;
    SQLiteDatabase db;
    UserDB userDB;
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        databaseHelper = new DbHelper(getApplicationContext());
        db = databaseHelper.getWritableDatabase();
        userDB = new UserDB();

        if(!checkPermission())
            requestPermission();
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private boolean checkPermission() {
        return Environment.isExternalStorageManager();
    }
    private void requestPermission() {
        try {
            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setData(Uri.parse(String.format("package:%s",getApplicationContext().getPackageName())));
            startActivityForResult(intent, 2296);
        } catch (Exception e) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
            startActivityForResult(intent, 2296);
        }

    }
    public void init(){
        loginField = findViewById(R.id.et_login);
        passwordField = findViewById(R.id.et_password);
    }

    public void Authorization(View view){
        log_name=loginField.getText().toString();
        String log="";
        String pass="";
        log=loginField.getText().toString();
        pass=passwordField.getText().toString();
        if(log.equals("")||pass.equals("")) {
            Toast.makeText(getApplicationContext(), "Пустые поля недопустимы", Toast.LENGTH_SHORT).show();
        }
        else {
            User user;
            if((user=userDB.SelectUserByLoginAndPassword(db, log, pass))!=null) {

                List<User> users = new ArrayList<>();
                users.add(user);
                JSONHelper.exportToJSONExternal(this, users);

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                Log.d("User login", "Success");
            }
            else
                Toast.makeText(getApplicationContext(), "Неверный логин или пароль", Toast.LENGTH_SHORT).show();
        }
    }

    public void Registration (View view){
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
        Log.d("Registration open","Success");
    }
}