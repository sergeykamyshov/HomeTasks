package ru.sergeykamyshov.schedule.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "schedule.db";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE = "CREATE TABLE " + StationSchema.TABLE_NAME + " (" +
            StationSchema._ID + " INTEGER PRIMARY KEY, " +
            StationSchema.Cols.COLUMN_COUNTRY_TITLE + " TEXT, " +
            StationSchema.Cols.COLUMN_CITY_TITLE + " TEXT, " +
            StationSchema.Cols.COLUMN_STATION_TITLE + " TEXT" +
            ")";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + StationSchema.TABLE_NAME);
        db.execSQL(SQL_CREATE_TABLE);
    }
}
