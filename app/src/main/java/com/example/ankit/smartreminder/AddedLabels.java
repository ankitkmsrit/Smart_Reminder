package com.example.ankit.smartreminder;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.borderlessButtonStyle;
import static android.R.attr.onClick;

public class AddedLabels extends AppCompatActivity {
    SQLiteDatabase db;
    ListView labelListView;
    List<String> addedDetails = new ArrayList<>();
    ArrayAdapter<String> adapter;
    String itemName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_added_labels);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = openOrCreateDatabase("ankit",MODE_PRIVATE,null);
        fetchData();
        showListView();

        registerForContextMenu(labelListView);
        labelListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                itemName = adapter.getItem(position);
                return false;
            }
        });

    }


    public void showListView(){
        labelListView=(ListView)findViewById(R.id.added_labels);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,addedDetails);
        labelListView.setAdapter(adapter);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, v.getId(), 0, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //return super.onContextItemSelected(item);
        if(item.getTitle() =="Delete"){
            confirmDelete();
        }
        else {
            return false;
        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        //return super.onSupportNavigateUp();
        finish();
        return true;
    }



    public void fetchData() {
        String l;
        Cursor cursor = db.rawQuery("select * from location", null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                l = cursor.getString(0) + "   " + cursor.getString(1) + "   " + cursor.getString(2);
                addedDetails.add(l);
            } while (cursor.moveToNext());
        } else {
            Toast.makeText(getApplicationContext(), "No labels set for now", Toast.LENGTH_SHORT).show();
        }
    }

    public void confirmDelete(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Are you sure to delete ?");
        alertDialog.setPositiveButton("Yes",  new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                adapter.remove(itemName);
                adapter.notifyDataSetChanged();
                String[] labelparts = itemName.split(" ");
                String labelName = labelparts[0];
                db.execSQL("delete from location where label='" + labelName + "'");
                Toast.makeText(getApplicationContext(), "Deleted Succesfully", Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();

    }


}
