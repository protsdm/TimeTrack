package com.example.timetrack;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * Created by Дима on 09.12.2016.
 */

public class DbTestHelper {
    public static void TestDataFill(SQLiteDatabase db) {
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

        values.clear();
        values.put(TimeTrackContract.Record.START, 1452142800000L);
        values.put(TimeTrackContract.Record.END, 1452175200000L);
        values.put(TimeTrackContract.Record.DURATION, 32400000);
        values.put(TimeTrackContract.Record.DESCRIPTION, "Working 1");
        values.put(TimeTrackContract.Record.CATEGORY_ID, insertW);
        db.insert(TimeTrackContract.Record.TABLE_NAME, null, values);

        values.clear();
        values.put(TimeTrackContract.Record.START, 1454821200000L);
        values.put(TimeTrackContract.Record.END, 1454853600000L);
        values.put(TimeTrackContract.Record.DURATION, 32400000);
        values.put(TimeTrackContract.Record.DESCRIPTION, "Working 2");
        values.put(TimeTrackContract.Record.CATEGORY_ID, insertW);
        db.insert(TimeTrackContract.Record.TABLE_NAME, null, values);

        values.clear();
        values.put(TimeTrackContract.Record.START, 1457326800000L);
        values.put(TimeTrackContract.Record.END, 1457359200000L);
        values.put(TimeTrackContract.Record.DURATION, 32400000);
        values.put(TimeTrackContract.Record.DESCRIPTION, "Working 3");
        values.put(TimeTrackContract.Record.CATEGORY_ID, insertW);
        db.insert(TimeTrackContract.Record.TABLE_NAME, null, values);


        values.clear();
        values.put(TimeTrackContract.Record.START, 1454659200000L);
        values.put(TimeTrackContract.Record.END, 1454662800000L);
        values.put(TimeTrackContract.Record.DURATION, 3600000);
        values.put(TimeTrackContract.Record.DESCRIPTION, "Eating 1");
        values.put(TimeTrackContract.Record.CATEGORY_ID, insertE);
        db.insert(TimeTrackContract.Record.TABLE_NAME, null, values);

        values.clear();
        values.put(TimeTrackContract.Record.START, 1454745600000L);
        values.put(TimeTrackContract.Record.END, 1454749200000L);
        values.put(TimeTrackContract.Record.DURATION, 3600000);
        values.put(TimeTrackContract.Record.DESCRIPTION, "Eating 2");
        values.put(TimeTrackContract.Record.CATEGORY_ID, insertE);
        db.insert(TimeTrackContract.Record.TABLE_NAME, null, values);

        values.clear();
        values.put(TimeTrackContract.Record.START, 1459929600000L);
        values.put(TimeTrackContract.Record.END, 1459933200000L);
        values.put(TimeTrackContract.Record.DURATION, 3600000);
        values.put(TimeTrackContract.Record.DESCRIPTION, "Eating 3");
        values.put(TimeTrackContract.Record.CATEGORY_ID, insertE);
        db.insert(TimeTrackContract.Record.TABLE_NAME, null, values);

        values.clear();
        values.put(TimeTrackContract.Record.START, 1462773600000L);
        values.put(TimeTrackContract.Record.END, 1462795200000L);
        values.put(TimeTrackContract.Record.DURATION, 21600000);
        values.put(TimeTrackContract.Record.DESCRIPTION, "Cleaning 1");
        values.put(TimeTrackContract.Record.CATEGORY_ID, insertC);
        db.insert(TimeTrackContract.Record.TABLE_NAME, null, values);

        values.clear();
        values.put(TimeTrackContract.Record.START, 1463378400000L);
        values.put(TimeTrackContract.Record.END, 1463400000000L);
        values.put(TimeTrackContract.Record.DURATION, 21600000);
        values.put(TimeTrackContract.Record.DESCRIPTION, "Cleaning 2");
        values.put(TimeTrackContract.Record.CATEGORY_ID, insertC);
        db.insert(TimeTrackContract.Record.TABLE_NAME, null, values);

        values.clear();
        values.put(TimeTrackContract.Record.START, 1463983200000L);
        values.put(TimeTrackContract.Record.END, 1464004800000L);
        values.put(TimeTrackContract.Record.DURATION, 21600000);
        values.put(TimeTrackContract.Record.DESCRIPTION, "Cleaning 3");
        values.put(TimeTrackContract.Record.CATEGORY_ID, insertC);
        db.insert(TimeTrackContract.Record.TABLE_NAME, null, values);

        values.clear();
        values.put(TimeTrackContract.Record.START, 1464728400000L);
        values.put(TimeTrackContract.Record.END, 1464764400000L);
        values.put(TimeTrackContract.Record.DURATION, 36000000);
        values.put(TimeTrackContract.Record.DESCRIPTION, "Sleeping 1");
        values.put(TimeTrackContract.Record.CATEGORY_ID, insertS);
        db.insert(TimeTrackContract.Record.TABLE_NAME, null, values);

        values.clear();
        values.put(TimeTrackContract.Record.START, 1467320400000L);
        values.put(TimeTrackContract.Record.END, 1467356400000L);
        values.put(TimeTrackContract.Record.DURATION, 36000000);
        values.put(TimeTrackContract.Record.DESCRIPTION, "Sleeping 2");
        values.put(TimeTrackContract.Record.CATEGORY_ID, insertS);
        db.insert(TimeTrackContract.Record.TABLE_NAME, null, values);

        values.clear();
        values.put(TimeTrackContract.Record.START, 1469998800000L);
        values.put(TimeTrackContract.Record.END, 1470034800000L);
        values.put(TimeTrackContract.Record.DURATION, 36000000);
        values.put(TimeTrackContract.Record.DESCRIPTION, "Sleeping 3");
        values.put(TimeTrackContract.Record.CATEGORY_ID, insertS);
        db.insert(TimeTrackContract.Record.TABLE_NAME, null, values);

        values.clear();
        values.put(TimeTrackContract.Record.START, 1472709600000L);
        values.put(TimeTrackContract.Record.END, 1472745600000L);
        values.put(TimeTrackContract.Record.DURATION, 36000000);
        values.put(TimeTrackContract.Record.DESCRIPTION, "Relaxing 1");
        values.put(TimeTrackContract.Record.CATEGORY_ID, insertR);
        db.insert(TimeTrackContract.Record.TABLE_NAME, null, values);

        values.clear();
        values.put(TimeTrackContract.Record.START, 1475301600000L);
        values.put(TimeTrackContract.Record.END, 1475337600000L);
        values.put(TimeTrackContract.Record.DURATION, 36000000);
        values.put(TimeTrackContract.Record.DESCRIPTION, "Relaxing 2");
        values.put(TimeTrackContract.Record.CATEGORY_ID, insertR);
        db.insert(TimeTrackContract.Record.TABLE_NAME, null, values);

        values.clear();
        values.put(TimeTrackContract.Record.START, 1477980000000L);
        values.put(TimeTrackContract.Record.END, 1478016000000L);
        values.put(TimeTrackContract.Record.DURATION, 36000000);
        values.put(TimeTrackContract.Record.DESCRIPTION, "Relaxing 3");
        values.put(TimeTrackContract.Record.CATEGORY_ID, insertR);
        db.insert(TimeTrackContract.Record.TABLE_NAME, null, values);

    }

    public static String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
