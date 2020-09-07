package com.bignerdranch.minesweeper30.dataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MineDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "mineSweeperBase.db";
    public static final int VERSION = 1;

    public MineDbHelper(Context c){
        super(c, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + MineDbSchema.RecordTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                MineDbSchema.RecordTable.Cols.TIME + ", " +
                MineDbSchema.RecordTable.Cols.DATE + ", " +
                MineDbSchema.RecordTable.Cols.BOMBS + ", " +
                MineDbSchema.RecordTable.Cols.FIELD + ", " +
                MineDbSchema.RecordTable.Cols.IMAGE + ")" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
