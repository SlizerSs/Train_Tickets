package com.example.train_tickets;

import android.content.Context;
import android.os.Environment;
import android.widget.TextView;
import android.widget.Toast;

import com.example.train_tickets.Model.User;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

class JSONHelper {

    private static final String FILE_NAME = "user.json";

    static boolean exportToJSONExternal(Context context, List<User> dataList) {

        Gson gson = new Gson();
        DataItems dataItems = new DataItems();
        dataItems.setApps(dataList);
        String jsonString = gson.toJson(dataItems);

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), FILE_NAME);
        try
        {
            //Если нет директорий в пути, то они будут созданы:
            if (!file.getParentFile().exists())
                file.getParentFile().mkdirs();
            //Если файл существует, то он будет перезаписан:
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file, false);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.write(jsonString);
            myOutWriter.close();
            fOut.close();
            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return false;
    }
    static List<User> importFromJSONExternal(Context context) {

        InputStreamReader streamReader = null;
        FileOutputStream fos = null;
        FileReader fr = null;
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), FILE_NAME);
        StringBuilder stringBuilder = new StringBuilder();

        try
        {
            fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            stringBuilder.append(line);

            Gson gson = new Gson();
            DataItems dataItems = gson.fromJson(stringBuilder.toString(), DataItems.class);
            return  dataItems != null ? dataItems.getApps() : new ArrayList<User>();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            try{
                if(fos!=null)
                    fos.close();
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }

        return null;
    }


    private static class DataItems {
        private List<User> users;
        List<User> getApps() {
            return users;
        }
        void setApps(List<User> users) {
            this.users = users;
        }
    }
}