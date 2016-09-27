package com.wordiction;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public class Data extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "Database_name";

    // Labels table name
    private static final String TABLE_NAME = "Uimage";


    // Labels Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_IMAGE = "image";

    public Data(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_USERIMAGE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"  + KEY_IMAGE + " BLOB );";
        db.execSQL(CREATE_USERIMAGE_TABLE);
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
    public void insertImage(Bitmap image){
        SQLiteDatabase db = this.getWritableDatabase();
        byte[] data=getBitmapAsByteArray(image);
        ContentValues cv = new ContentValues();
        //cv.put(KEY_NAME, name);
        cv.put(KEY_IMAGE,data);

        // Inserting Row
        db.insert( TABLE_NAME, null, cv);

        db.close(); // Closing database connection

    }

    /**
     * Getting all labels
     * returns list of labels
     * */
    public Bitmap getImage(){
        String s="";
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE ID=(SELECT MAX(ID) FROM " + TABLE_NAME +")";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
             do {
                 byte[] bytes = cursor.getBlob(cursor.getColumnIndex(KEY_IMAGE));

                 Log.e("ST", "STTT");
                 return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
             } while (cursor.moveToNext());
        }

        if (cursor!=null&&!cursor.isClosed()) {
            cursor.close();
        }
        // closing connection
        db.close();
        Log.e("1", s);
        return null;
    }

    public static byte [] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,0,stream);
        return stream.toByteArray();
    }
}
