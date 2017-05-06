package com.example.ankit.smartreminder;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.GpsStatus;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddLocation extends AppCompatActivity {

    GPSTracker gps;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       openDataBase();

    }

    public void openDataBase(){
        db = openOrCreateDatabase("ankit",MODE_PRIVATE,null);
        try{
            db.execSQL("create table if not exists location ( label text,lat real,lon real)");
        }

        catch (Exception e){
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        //return super.onSupportNavigateUp();
        finish();
        return true;
    }

    public void addLocation(View view){

        try {

            EditText locText = (EditText) findViewById(R.id.locText);
            String locLabel = locText.getText().toString().trim();

            gps = new GPSTracker(AddLocation.this);

            if(gps.canGetLocation()){

                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();
                if(locText.length()!=0) {
                    if(latitude== 0 && longitude==0){
                        Toast.makeText(getApplicationContext(), "Please wait a moment while we grab your location", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        db.execSQL("insert into location values('" + locLabel + "','" + latitude + "','" + longitude + "')");
                        Toast.makeText(getApplicationContext(), "Current Location added successfully", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Add a label for current location", Toast.LENGTH_SHORT).show();
                }

            }
            else{

                gps.showSettingsAlert();
            }

        }

        catch (Exception e){
            e.printStackTrace();
        }


    }




}
