package com.bignerdranch.minesweeper30.dataBase;

import android.database.Cursor;
import android.database.CursorWrapper;
import com.bignerdranch.minesweeper30.data.Record;

import java.util.Date;

public class MinesweeperCursorWrapper extends CursorWrapper {

    public MinesweeperCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public Record getRecord(){
        int time = Integer.parseInt(getString(getColumnIndex(MineDbSchema.RecordTable.Cols.TIME)));
        long date = getLong(getColumnIndex(MineDbSchema.RecordTable.Cols.DATE));
        int bombs = Integer.parseInt(getString(getColumnIndex(MineDbSchema.RecordTable.Cols.BOMBS)));
        int field = Integer.parseInt(getString(getColumnIndex(MineDbSchema.RecordTable.Cols.FIELD)));
        String imageName = getString(getColumnIndex(MineDbSchema.RecordTable.Cols.IMAGE));
        return new Record(time, bombs, field, new Date(date), imageName);
    }

    public int getQuantity(){
        return getInt(1);
    }
    public int getIndex(){
        return getInt(0);
    }

}
