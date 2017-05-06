package com.example.ankit.smartreminder;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Launch extends AppCompatActivity {
    private Handler handler= new Handler();
    Vibrator v;


    SQLiteDatabase db ;
    GPSTracker gps;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_launch);
        handler.postDelayed(new Runnable(){
            public void run(){
                Intent intent = new Intent(Launch.this,MainActivity.class);
                startActivity(intent);
            }

        },2300);

        db = openOrCreateDatabase("ankit",MODE_PRIVATE,null);
        startService(new Intent(Launch.this,MyService.class));



    }




    public List<List> getReminderList( SQLiteDatabase db){
        List<List> reminderList = new ArrayList<>();
        cursor = db.rawQuery("select * from reminder", null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                List<String> reminder = new ArrayList<>();
                reminder.add(cursor.getString(0));
                reminder.add(cursor.getString(1));
                reminderList.add(reminder);
            } while (cursor.moveToNext());
        }

        return reminderList;

    }

    public void displayReminder(SQLiteDatabase db){
        gps = new GPSTracker(Launch.this);
        List<List> reminderList = getReminderList(db);
        for(List<String> r:reminderList){
            String place = r.get(1);
            List<Double> latlon = getCordinateOfPlace(place,db);
            //Log.i("LatLon",latlon.get(0)+" "+ latlon.get(1));
            List<Double> currentlatlon = getCurrentLocation();
           // Log.i("cLatLon",currentlatlon.get(0)+" "+ currentlatlon.get(1));
            try {
                if (((currentlatlon.get(0) >= latlon.get(0) && currentlatlon.get(0) <= latlon.get(0) + (0.00019))
                        || (currentlatlon.get(0) <= latlon.get(0) && currentlatlon.get(0) >= latlon.get(0) - (0.00019)))
                        && ((currentlatlon.get(1) >= latlon.get(1) && currentlatlon.get(1) <= latlon.get(1) + (0.00019))
                        || (currentlatlon.get(1) <= latlon.get(1) && currentlatlon.get(1) >= latlon.get(1) - (0.00019)))) {

                    Toast.makeText(getApplicationContext(), "You are at " + place, Toast.LENGTH_SHORT).show();

                    v = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(500);

                }


            }

            catch (Exception e){
                e.printStackTrace();
                //Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }


        }


    }

    public  List<Double> getCordinateOfPlace(String place,SQLiteDatabase db){
        List<Double> latlon = new ArrayList<>();
        cursor = db.rawQuery("select lat,lon from location where label='"+place+"'", null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                latlon.add(cursor.getDouble(0));
                latlon.add(cursor.getDouble(1));
            } while (cursor.moveToNext());
        }
        return latlon;

    }


    public List<Double> getCurrentLocation(){
        List<Double> currentlatlon = new ArrayList<>();
        currentlatlon.add(gps.getLatitude());
        currentlatlon.add(gps.getLongitude());
        return currentlatlon;
    }

}
