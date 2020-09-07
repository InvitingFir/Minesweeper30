package com.bignerdranch.minesweeper30.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import com.bignerdranch.minesweeper30.dataBase.MineDbHelper;
import com.bignerdranch.minesweeper30.dataBase.MineDbSchema.RecordTable;
import com.bignerdranch.minesweeper30.dataBase.MinesweeperCursorWrapper;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class RecordManager {
    public static final int RECORD_TABLE_SIZE = 10;
    private static RecordManager sRecordManager;
    private SQLiteDatabase mDatabase;
    private Context mContext;

    private RecordManager(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new MineDbHelper(mContext).getWritableDatabase();
    }

    public static RecordManager getInstance(Context context){
        if(sRecordManager == null) sRecordManager = new RecordManager(context);
        return sRecordManager;
    }

    public List<Record> getRecords(int bombs, int fieldSize){
        List<Record> records = new ArrayList<>();
        String bombsString = Integer.toString(bombs);
        String fieldSizeString = Integer.toString(fieldSize);
        MinesweeperCursorWrapper cursorWrapper = queryRecord(
                null,
                RecordTable.Cols.FIELD + "= ? AND " +
                        RecordTable.Cols.BOMBS + "= ?",
                new String[]{fieldSizeString, bombsString});
        try{
            cursorWrapper.moveToFirst();
            while(!cursorWrapper.isAfterLast()){
                Record record = cursorWrapper.getRecord();
                records.add(record);
                cursorWrapper.moveToNext();
            }
        }
        finally {
            cursorWrapper.close();
        }
        return records;
    }

    public boolean addRecord(Record record){
        List<Record> records = getRecords(record.getBombs(), record.getFieldSize());
        ContentValues values = toContentValues(record);
        if(records.size() < RECORD_TABLE_SIZE){
            mDatabase.insert(RecordTable.NAME, null, values);
            saveImage(record);
            return true;
        }
        else {
            String field = Integer.toString(record.getFieldSize());
            String bombs = Integer.toString(record.getBombs());
            MinesweeperCursorWrapper cursorWrapper = queryRecord(null,
                    RecordTable.Cols.FIELD + " = ? AND " + RecordTable.Cols.BOMBS + " = ?",
                    new String[]{field, bombs}
                    );
            cursorWrapper.moveToLast();
            Record deprecatedRecord = cursorWrapper.getRecord();
            if(deprecatedRecord.getTime() > record.getTime()) {
                deleteImage(deprecatedRecord);
                String index = Integer.toString(cursorWrapper.getIndex());
                mDatabase.update(RecordTable.NAME, values, "_id = ?", new String[]{index});
                saveImage(record);
                return true;
            }
            cursorWrapper.close();
        }
        return false;
    }

    private ContentValues toContentValues(Record record){
        ContentValues values = new ContentValues();
        values.put(RecordTable.Cols.TIME, record.getTime());
        values.put(RecordTable.Cols.DATE, record.getDate().getTime());
        values.put(RecordTable.Cols.BOMBS, Integer.toString(record.getBombs()));
        values.put(RecordTable.Cols.FIELD, Integer.toString(record.getFieldSize()));
        values.put(RecordTable.Cols.IMAGE, record.getImageName());
        return values;
    }

    private MinesweeperCursorWrapper queryRecord(String[] columns,
                                                 String whereClause,
                                                 String[] whereArgs){
        Cursor cursor = mDatabase.query(RecordTable.NAME,
                columns,
                whereClause,
                whereArgs, null, null, RecordTable.Cols.TIME + " ASC"
        );
        return new MinesweeperCursorWrapper(cursor);
    }

    private void saveImage(Record record){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(getImageFile(record));
            record.getImage().compress(Bitmap.CompressFormat.JPEG, 85, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void deleteImage(Record record){
        File file = getImageFile(record);
        file.delete();
    }

    public File getImageFile(Record record){
        File file = mContext.getFilesDir();
        return new File(file, record.getImageName());
    }
}
