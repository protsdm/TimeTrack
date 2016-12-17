package com.example.timetrack;

import android.provider.BaseColumns;

/**
 * Created by Дима on 13.11.2016.
 */

public final class TimeTrackContract {

    private TimeTrackContract() {};

    public static class Category implements BaseColumns
    {
        public static final String TABLE_NAME = "Categories";
        //public static final String ID = "Id";
        public static final String NAME = "Name";
    }

    public static class Photo implements BaseColumns
    {
        public static final String TABLE_NAME = "Photos";
        //public static final String ID = "Id";
        public static final String RECORD_ID = "RecordId";
        public static final String PHOTO_PATH = "Photo";
    }

    public static class Record implements BaseColumns
    {
        public static final String TABLE_NAME = "Records";
       // public static final String ID = "Id";
        public static final String START = "StartDate";
        public static final String END = "EndDate";
        public static final String DESCRIPTION = "Description";
        public static final String DURATION = "Duration";
        public static final String CATEGORY_ID = "CategoryId";
    }

}
