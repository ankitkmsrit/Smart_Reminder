package com.example.ankit.smartreminder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SQLiteDatabase db;
    ListView reminderListView;
    List<String> addedReminder = new ArrayList<>();
    ArrayAdapter<String> adapter;
    String reminderName;
    Button btnShowLocation;
    GPSTracker gps;
    private Context context;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //.setAction("Action", null).show();
                Intent intent = new Intent(MainActivity.this, Reminder.class);
                startActivity(intent);

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getLocation();

        connectDb();
        fetchData();
        showListView();

        registerForContextMenu(reminderListView);
        reminderListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                reminderName = adapter.getItem(position);
                return false;
            }
        });

    }


    public void connectDb(){

        db = openOrCreateDatabase("ankit",MODE_PRIVATE,null);
        try{
            db.execSQL("create table if not exists reminder(remind text,place text)");
        }

        catch (Exception e){
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
        }

    }

    public void showListView(){
        reminderListView=(ListView)findViewById(R.id.added_reminder);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,addedReminder);
        reminderListView.setAdapter(adapter);

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

    public void confirmDelete(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Are you sure to delete ?");
        alertDialog.setPositiveButton("Yes",  new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                adapter.remove(reminderName);
                adapter.notifyDataSetChanged();
                String[] labelparts = reminderName.split(" ");
                String labelName = labelparts[labelparts.length-1];
                db.execSQL("delete from reminder where place='"+labelName+"'");
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


    public void fetchData() {
        String l;
        Cursor cursor = db.rawQuery("select * from reminder", null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                l = cursor.getString(0) + "  "+"when I reach" +"  "+ cursor.getString(1);
                addedReminder.add(l);
            } while (cursor.moveToNext());
        } else {
            Toast.makeText(getApplicationContext(), "No reminder set for now", Toast.LENGTH_SHORT).show();
        }
    }


    public void getLocation(){

        btnShowLocation = (Button) findViewById(R.id.btnShowLocation);

        // show location button click event
        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // create class object

                gps = new GPSTracker(MainActivity.this);
                // check if GPS enabled
                if(gps.canGetLocation()){

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    if(latitude== 0 && longitude==0){
                        Toast.makeText(getApplicationContext(), "Please wait a moment while we grab your location", Toast.LENGTH_SHORT).show();
                    }
                    else {

                        Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                    }
                }else{

                    gps.showSettingsAlert();
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_exit) {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.add_location) {
            Intent intent = new Intent(MainActivity.this,AddLocation.class);
            startActivity(intent);

        }
        else if (id == R.id.added_labels) {

            Intent intent = new Intent(MainActivity.this,AddedLabels.class);
            startActivity(intent);

        }  else if (id == R.id.nav_share) {


        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
