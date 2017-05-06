package com.example.ankit.smartreminder;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Ankit on 26-04-2017.
 */

public class ShowReminder extends AppCompatActivity {

    /*SQLiteDatabase db;
    GPSTracker gps;
    Cursor cursor;


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        db=openOrCreateDatabase("ankit",MODE_PRIVATE, null);

        super.onCreate(savedInstanceState, persistentState);

        //displayReminder(db);

        startService(new Intent(ShowReminder.this,MyService.class));

    }

    public List<List> getReminderList(SQLiteDatabase db) {
        List<List> reminderList = new ArrayList<>();
        try {
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
        }

        catch (Exception e){
            e.printStackTrace();
        }

        return reminderList;

    }

    public void displayReminder(SQLiteDatabase db) {
        //Toast.makeText(MainActivity.this,"Entering display", Toast.LENGTH_SHORT).show();
        gps = new GPSTracker(ShowReminder.this);
        List<List> reminderList = getReminderList(db);
        for (List<String> r : reminderList) {
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

                }
            } catch (Exception e) {
                //Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }
        }


    }

    public List<Double> getCordinateOfPlace(String place, SQLiteDatabase db) {
        List<Double> latlon = new ArrayList<>();
        cursor = db.rawQuery("select lat,lon from location where label='" + place + "'", null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                latlon.add(cursor.getDouble(0));
                latlon.add(cursor.getDouble(1));
            } while (cursor.moveToNext());
        }
        return latlon;

    }


    public List<Double> getCurrentLocation() {
        List<Double> currentlatlon = new ArrayList<>();
        currentlatlon.add(gps.getLatitude());
        currentlatlon.add(gps.getLongitude());
        return currentlatlon;
    }

    */


}
