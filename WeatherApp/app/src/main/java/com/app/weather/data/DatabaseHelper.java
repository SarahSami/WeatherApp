package com.app.weather.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.app.weather.models.City;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Sarah on 6/22/17.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    final static String DATABASE_NAME = "weather_db.sqlite";
    private String DB_PATH = "";
    SQLiteDatabase myDataBase = null;
    private static Context myContext;
    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {

        if (instance == null) {
            instance = new DatabaseHelper(context);
            myContext = context;
        }
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 2);
        this.myContext = context;
        this.DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }


    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (!dbExist) {
            this.getWritableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each
     * time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {

        boolean checkDB = false;

        try {
            File dbFile = new File(DB_PATH + DATABASE_NAME);
            checkDB = dbFile.exists();
        } catch (SQLiteException e) {
            e.printStackTrace();

        }
        return checkDB;
    }

    /**
     * Copies your database from your local assets-folder to the just created
     * empty database in the system folder, from where it can be accessed and
     * handled. This is done by transfering bytestream.
     */
    private void copyDataBase() throws IOException {
        InputStream myInput = myContext.getAssets().open(DATABASE_NAME);
        String outFileName = DB_PATH + DATABASE_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }


    public void openDataBase() throws SQLException {
        String myPath = DB_PATH + DATABASE_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        myDataBase.close();
    }

    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public ArrayList<City> getAllCities() {

        String selectQuery = "SELECT * FROM CitiesTBL";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<City> items = new ArrayList<City>();
        City city;
        if (cursor.moveToFirst()) {
            do {
                city = new City();
                city.setName(cursor.getString(cursor.getColumnIndex("Name")));
                city.setCountry(cursor.getString(cursor.getColumnIndex("Country")));
                city.setLat(cursor.getDouble(cursor.getColumnIndex("Lat")));
                city.setLng(cursor.getDouble(cursor.getColumnIndex("Lng")));
                items.add(city);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return items;
    }


    public void addCity(City city) {
        SQLiteDatabase db;
        ContentValues values = new ContentValues();
        values.put("Name", city.getName());
        values.put("Country", city.getCountry());
        values.put("Lat", city.getLat());
        values.put("Lng", city.getLng());
        db = this.getWritableDatabase();
        db.insert("CitiesTBL", null, values);
        db.close();
    }


    public void removeCity(String cityName) {
        SQLiteDatabase db;
        db = this.getWritableDatabase();
        db.delete("CitiesTBL", "Name='" + cityName + "'", null);
        db.close();
    }

    public void removeAllCities() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("CitiesTBL", null, null);
        db.close();
    }

}
