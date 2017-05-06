package com.example.ankit.smartreminder;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Reminder extends AppCompatActivity {

    SQLiteDatabase db;
    ListView reminderListView;
    List<String> labelList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    String reminderName;
    Spinner spinner;
    EditText remindtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = openOrCreateDatabase("ankit",MODE_PRIVATE,null);
        loadSpinner();

    }

    @Override
    public boolean onSupportNavigateUp() {
        //return super.onSupportNavigateUp();
        finish();
        return true;
    }


    public void addReminder(View view){

        remindtext = (EditText)findViewById(R.id.remindtext);
        String reminderText = remindtext.getText().toString().trim();
        String place = spinner.getSelectedItem().toString();
        try {
            if(reminderText.length()!=0) {
                db.execSQL("insert into reminder values('" + reminderText + "','"+place+"')");
                Toast.makeText(getApplicationContext(), "Reminder added successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Reminder.this, MainActivity.class);
                startActivity(intent);
            }

            else {
                Toast.makeText(getApplicationContext(), "Add some reminder first", Toast.LENGTH_SHORT).show();
            }
        }

        catch (Exception e){
            Toast.makeText(getApplicationContext(),"Error while adding Reminder", Toast.LENGTH_SHORT).show();
        }

    }


    public void loadSpinner(){

        fetchData();
        spinner = (Spinner)findViewById(R.id.spinner);
        adapter = new ArrayAdapter(Reminder.this,R.layout.support_simple_spinner_dropdown_item,labelList);
        spinner.setAdapter(adapter);
    }

    public void fetchData() {
        String l;
        Cursor cursor = db.rawQuery("select * from location", null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                l = cursor.getString(0);
                labelList.add(l);
            } while (cursor.moveToNext());
        } else {
            Toast.makeText(getApplicationContext(), "No label set for now", Toast.LENGTH_SHORT).show();
        }
    }

}
