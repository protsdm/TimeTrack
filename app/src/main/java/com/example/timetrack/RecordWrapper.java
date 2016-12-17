package com.example.timetrack;

import android.database.Cursor;
import android.database.CursorWrapper;

/**
 * Created by Дима on 27.11.2016.
 */

public class RecordWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public RecordWrapper(Cursor cursor) {
        super(cursor);
    }
}
