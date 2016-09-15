package com.blackpanther.bingo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by singapore on 12-09-2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    public static String DATABASE_NAME="saved_games.db";
    public static int DATABASE_VERSION=1;
    private String GAME_TABLE = "Saved_Games";
    private String GAME_PRIMARY_KEY = "id";
    public static String  GAME_NAME= "name";
    public static String GAME_BOARD = "board";
    private String GAME_TABLE_DATA_CREATE = " CREATE TABLE IF NOT EXISTS " + GAME_TABLE + "(" + GAME_PRIMARY_KEY +
            " INTEGER PRIMARY KEY AUTOINCREMENT," + GAME_NAME + " TEXT NOT NULL, "+ GAME_BOARD+" TEXT NOT NULL )";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(GAME_TABLE_DATA_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void StoreData(SQLiteDatabase db,String name,String board){
        ContentValues contentValues = new ContentValues();
        contentValues.put(GAME_NAME,name);
        contentValues.put(GAME_BOARD,board);
        db.insert(GAME_TABLE,null,contentValues);
    }
    public Cursor GetData(SQLiteDatabase db){
        return db.rawQuery("SELECT * FROM "+ GAME_TABLE,null);
    }
}
