package com.example.ankit.smartreminder;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ankit on 28-04-2017.
 */

public class MyService extends Service {

    SQLiteDatabase db ;
    GPSTracker gps;
    MediaPlayer player;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //GPSTracker gps = new GPSTracker(MyService.this);
        db = openOrCreateDatabase("ankit",MODE_PRIVATE,null);
        //int i=0;

        //launch = new Launch();
        Toast.makeText(MyService.this,"Starting bg", Toast.LENGTH_SHORT).show();
        displayReminder();

       /* new Thread(){
            public void run() {
                displayReminder();
            }
        }.start();*/

        //player = new MediaPlayer();
       /* player = MediaPlayer.create(MyService.this, Settings.System.DEFAULT_NOTIFICATION_URI);
        player.setLooping(true);
        player.start();*/

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }



    public List<List> getReminderList(){
        List<List> reminderList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from reminder", null);
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

    public void displayReminder(){
        gps = new GPSTracker(MyService.this);
        List<List> reminderList = getReminderList();
        for(List<String> r:reminderList){
            String place = r.get(1);
            List<Double> latlon = getCordinateOfPlace(place);
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
            }

            catch (Exception e){
                //Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }
        }


    }

    public  List<Double> getCordinateOfPlace(String place){
        List<Double> latlon = new ArrayList<>();
        Cursor cursor = db.rawQuery("select lat,lon from location where label='"+place+"'", null);
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
