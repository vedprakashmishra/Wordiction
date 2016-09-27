package com.wordiction;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;


/**
 * Created by Rohit on 05-May-16.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "UserName";

    // Labels table name
    private static final String TABLE_NAME = "Uname";


    // Labels Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_USERNAME_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT)";
        db.execSQL(CREATE_USERNAME_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    /**
     * Inserting new lable into lables table
     * */
    public void insertName(String label){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, label);

        // Inserting Row
        db.insert( TABLE_NAME, null, values);

        db.close(); // Closing database connection

    }

    /**
     * Getting all labels
     * returns list of labels
     * */
    public String getUserName(){
        String s="";
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE ID=(SELECT MAX(ID) FROM " + TABLE_NAME +")";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                s=cursor.getString(1);
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();
        Log.e("1", s);
        return s;
    }
}
