package com.example.timetrack;

import android.net.Uri;

import java.io.Serializable;

/**
 * Created by Дима on 21.11.2016.
 */

public class PhotoDto implements Serializable {
    private long _id;
    private Uri photoUri;
    private int _recordId;

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public Uri getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(Uri photoUri) {
        this.photoUri = photoUri;
    }

    public int get_recordId() {
        return _recordId;
    }

    public void set_recordId(int _recordId) {
        this._recordId = _recordId;
    }
}
