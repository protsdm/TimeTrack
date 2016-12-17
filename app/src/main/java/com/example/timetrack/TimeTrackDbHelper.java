package com.example.timetrack;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

/**
 * Created by Дима on 13.11.2016.
 */

public class TimeTrackDbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "TimeTrack.db";

    public final String CREATE_CATEGORY =
            String.format("create table %s " +
                    "(%s integer primary key autoincrement, " +
                    "%s text );",
                    TimeTrackContract.Category.TABLE_NAME,
                    TimeTrackContract.Category._ID,
                    TimeTrackContract.Category.NAME);
    public final String CREATE_RECORD =
            String.format("create table %s " +
                    "(%s integer primary key autoincrement," +
                    "%s integer," +
                    "%s integer," +
                    "%s text," +
                    "%s integer," +
                    "%s integer references %s(%s) );",
                    TimeTrackContract.Record.TABLE_NAME,
                    TimeTrackContract.Record._ID,
                    TimeTrackContract.Record.START,
                    TimeTrackContract.Record.END,
                    TimeTrackContract.Record.DESCRIPTION,
                    TimeTrackContract.Record.DURATION,
                    TimeTrackContract.Record.CATEGORY_ID,
                    TimeTrackContract.Category.TABLE_NAME,
                    TimeTrackContract.Category._ID);
    public final String CREATE_PHOTO =
            String.format("create table %s " +
                    "(%s integer primary key autoincrement, " +
                    "%s text, " +
                    "%s integer references %s(%s) );",
                    TimeTrackContract.Photo.TABLE_NAME,
                    TimeTrackContract.Photo._ID,
                    TimeTrackContract.Photo.PHOTO_PATH,
                    TimeTrackContract.Photo.RECORD_ID,
                    TimeTrackContract.Record.TABLE_NAME,
                    TimeTrackContract.Record._ID);
    //public final String CREATE_STATEMENT = CREATE_CATEGORY + CREATE_RECORD + CREATE_PHOTO;

    public TimeTrackDbHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CATEGORY);
        db.execSQL(CREATE_RECORD);
        db.execSQL(CREATE_PHOTO);

        DbTestHelper.TestDataFill(db);

        /*
        ContentValues values = new ContentValues();
        values.put(TimeTrackContract.Category.NAME, "Working");
        long insertW = db.insert(TimeTrackContract.Category.TABLE_NAME, null, values);

        values.put(TimeTrackContract.Category.NAME, "Eating");
        long insertE = db.insert(TimeTrackContract.Category.TABLE_NAME, null, values);

        values.put(TimeTrackContract.Category.NAME, "Relaxing");
        long insertR = db.insert(TimeTrackContract.Category.TABLE_NAME, null, values);

        values.put(TimeTrackContract.Category.NAME, "Cleaning");
        long insertC = db.insert(TimeTrackContract.Category.TABLE_NAME, null, values);

        values.put(TimeTrackContract.Category.NAME, "Sleeping");
        long insertS = db.insert(TimeTrackContract.Category.TABLE_NAME, null, values);

        long insertRecord;

        values.clear();
        values.put(TimeTrackContract.Record.START, new Date().getTime());
        values.put(TimeTrackContract.Record.END, new Date().getTime() + 3600000);
        values.put(TimeTrackContract.Record.DURATION, 3600000);
        values.put(TimeTrackContract.Record.DESCRIPTION, "Some clearing description");
        values.put(TimeTrackContract.Record.CATEGORY_ID, insertC);
        insertRecord = db.insert(TimeTrackContract.Record.TABLE_NAME, null, values);

        values.clear();
        values.put(TimeTrackContract.Record.START, new Date().getTime());
        values.put(TimeTrackContract.Record.END, new Date().getTime() + 18000000);
        values.put(TimeTrackContract.Record.DURATION, 18000000);
        values.put(TimeTrackContract.Record.DESCRIPTION, "Some clearing2 description");
        values.put(TimeTrackContract.Record.CATEGORY_ID, insertC);
        insertRecord = db.insert(TimeTrackContract.Record.TABLE_NAME, null, values);

        values.clear();
        values.put(TimeTrackContract.Record.START, new Date().getTime());
        values.put(TimeTrackContract.Record.END, new Date().getTime() + 1440000);
        values.put(TimeTrackContract.Record.DURATION, 14400000);
        values.put(TimeTrackContract.Record.DESCRIPTION, "Some working description");
        values.put(TimeTrackContract.Record.CATEGORY_ID, insertW);
        insertRecord = db.insert(TimeTrackContract.Record.TABLE_NAME, null, values);

        values.clear();
        values.put(TimeTrackContract.Record.START, new Date().getTime());
        values.put(TimeTrackContract.Record.END, new Date().getTime() + 7200000);
        values.put(TimeTrackContract.Record.DURATION, 7200000);
        values.put(TimeTrackContract.Record.DESCRIPTION, "Some sleeping description");
        values.put(TimeTrackContract.Record.CATEGORY_ID, insertS);
        insertRecord = db.insert(TimeTrackContract.Record.TABLE_NAME, null, values);

        values.clear();
        values.put(TimeTrackContract.Record.START, new Date().getTime());
        values.put(TimeTrackContract.Record.END, new Date().getTime() + 10800000);
        values.put(TimeTrackContract.Record.DURATION, 10800000);
        values.put(TimeTrackContract.Record.DESCRIPTION, "Some relaxing description");
        values.put(TimeTrackContract.Record.CATEGORY_ID, insertR);
        insertRecord = db.insert(TimeTrackContract.Record.TABLE_NAME, null, values);

        values.clear();
        values.put(TimeTrackContract.Record.START, new Date().getTime());
        values.put(TimeTrackContract.Record.END, new Date().getTime() + 36000000);
        values.put(TimeTrackContract.Record.DURATION, 36000000);
        values.put(TimeTrackContract.Record.DESCRIPTION, "Some eating description");
        values.put(TimeTrackContract.Record.CATEGORY_ID, insertE);
        insertRecord = db.insert(TimeTrackContract.Record.TABLE_NAME, null, values);
*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
