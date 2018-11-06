package com.example.pavan.mobiofire.DatabaseHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "mobio_db";

    // table name
    private static final String TABLENMAE = "Thumbs";

    private static final String id = "id";
    private static final String description = "description";
    private static final String thumb = "thumb";
    private static final String title = "title";
    private static final String url = "url";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLENMAE + "("
                +id + " INTEGER," + description + " TEXT,"
                + thumb + " TEXT," + title + " TEXT," + url + " TEXT"+ ")";
        sqLiteDatabase.execSQL(CREATE_CONTACTS_TABLE);

        Log.d("Created", "Yes");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int Old, int New) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLENMAE);
    }

    // Getting All Thumbs
    public List<thumbs_model> getAllContacts() {
        List<thumbs_model> thumbList = new ArrayList<thumbs_model>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLENMAE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                thumbs_model thumb = new thumbs_model();
                thumb.setId(cursor.getInt(0));
                thumb.setDescription(cursor.getString(1));
                thumb.setThumb(cursor.getString(2));
                thumb.setTitle(cursor.getString(3));
                thumb.setUrl(cursor.getString(4));
                // Adding contact to list
                thumbList.add(thumb);
            } while (cursor.moveToNext());
        }
        return thumbList;
    }
    public thumbs_model getdata_based_on_id(int id_) {
        thumbs_model thumb = new thumbs_model();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLENMAE+" where "+id+" = "+id_;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                thumb.setId(cursor.getInt(0));
                thumb.setDescription(cursor.getString(1));
                thumb.setThumb(cursor.getString(2));
                thumb.setTitle(cursor.getString(3));
                thumb.setUrl(cursor.getString(4));
                // Adding contact to list
//                thumbList.add(thumb);
            } while (cursor.moveToNext());
        }
        return thumb;
    }


    public void addThumb(thumbs_model thumbs_model) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(id, thumbs_model.getId());
        values.put(description, thumbs_model.getDescription());
        values.put(thumb, thumbs_model.getThumb());
        values.put(url, thumbs_model.getUrl());
        values.put(title, thumbs_model.getTitle());

        db.insert(TABLENMAE, null, values);
        db.close(); // Closing database connection
    }

    public boolean delete_allThumb() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLENMAE, null, null);
        return false;
    }
}
