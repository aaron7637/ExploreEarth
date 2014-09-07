package com.example.exploreearth.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Map;
import java.util.EnumSet;
import java.util.HashMap;

import android.database.SQLException;
import android.util.Log;

public class CountryDatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "countriesDatabase";
    private static final String TABLE_COUNTRIES = "countries";

    private static final String countryNameField = "countryName";
    private static final String continentNameField = "continentName";
    private static final String capitalField = "capital";
    private static final String populationField = "population";
    private static final String currencyCodeField = "currencyCode";
    private static final String urlField = "url";
    private static final String foodField = "food";

    public CountryDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public enum COUNTRYFIELDS {
        COUNTRYNAME("countryName"), CONTINENTNAME("continentName"), CAPITAL("capital"), POPULATION("population"), CURRENCY("currencyCode"), URL("url"), FOOD("food");

        private static final Map<String, COUNTRYFIELDS> lookup = new HashMap<String, COUNTRYFIELDS>();

        static {
            for (COUNTRYFIELDS s : EnumSet.allOf(COUNTRYFIELDS.class))
                lookup.put(s.getCountryField(), s);
        }

        private String countryField;

        private COUNTRYFIELDS(String countryField) {
            this.countryField = countryField;
        }

        public String getCountryField() {
            return countryField;
        }

        public static COUNTRYFIELDS get(int code) {
            return lookup.get(code);
        }
    }

    // Creating Tables 
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_COUNTRIES_TABLE = "CREATE TABLE countries ( id INTEGER PRIMARY KEY autoincrement, countryName Text UNIQUE, continentName Text, capital Text, areaInSqKm Text, population Text, currencyCode Text, url Text, food Text )";
        db.execSQL(CREATE_COUNTRIES_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUNTRIES);

        // Create tables again
        onCreate(db);
    }

    void addCountryRecord(String countryName, String continentName, String capital, String population, String currencyCode, String url, String food) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(countryNameField, countryName);
        values.put(continentNameField, continentName);
        values.put(capitalField, capital);
        values.put(populationField, population);
        values.put(currencyCodeField, currencyCode);
        values.put(foodField, food);
        values.put(urlField, url);

        try {
            db.insertOrThrow(TABLE_COUNTRIES, null, values);
        } catch (SQLException exception) {
            Log.d("database", "SQLException: Record already exists");
        }
        db.close();
    }

    public String[] getCountries(String searchStr) {
        String selectQuery = "SELECT " + countryNameField + " FROM " + TABLE_COUNTRIES + " WHERE " + urlField + " IS NOT NULL AND " + countryNameField + " like '" + searchStr + "%' ORDER BY " + countryNameField + " ASC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String[] countryList = new String[cursor.getCount()];
        int i = 0;

        if (cursor.moveToFirst()) {
            do {
                countryList[i] = cursor.getString(0);
                i++;
            }

            while (cursor.moveToNext());
        }

        db.close();

        return countryList;
    }

    public String getField(String country, COUNTRYFIELDS field) {
        String selectQuery = "SELECT " + field + " FROM " + TABLE_COUNTRIES + " WHERE " + COUNTRYFIELDS.COUNTRYNAME + "='" + country + "';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String url = "";

        if (cursor.moveToFirst()) {
            url = cursor.getString(0);
        }

        db.close();

        return url;
    }
}
