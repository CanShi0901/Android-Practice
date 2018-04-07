package com.example.shican.androidlabs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ChatDatabaseHelper extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "Messages.db";
    static final int VERSION_NUM =4;
    static final String TABLE_NAME = "ChatMessageTable";
    static String KEY_ID = "_ID";
    static String KEY_MESSAGE = "MESSAGE";


    public ChatDatabaseHelper(Context ctx){
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       db.execSQL("CREATE TABLE " + TABLE_NAME +
                "( _ID INTEGER PRIMARY KEY AUTOINCREMENT, MESSAGE text);");
    Log.i("ChatDatabaseHelper", "Calling onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        Log.i("ChatDatabaseHelper", "Calling onUprade, oldVersion="
                + oldVer + "newVersion=" + newVer);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
