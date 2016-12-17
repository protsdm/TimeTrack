package com.example.timetrack;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Дима on 21.11.2016.
 */

public class RecordDto implements Serializable {
    private long _id;
    private long _start;
    private long _end;
    private String _description;
    private long _categoryId;

    public ArrayList<PhotoDto> getPhotos() {
        return photos;
    }

    private ArrayList<PhotoDto> photos;

    public RecordDto() {
        photos = new ArrayList<>();
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public long get_start() {
        return _start;
    }

    public void set_start(long _start) {
        this._start = _start;
    }

    public long get_end() {
        return _end;
    }

    public void set_end(long _end) {
        this._end = _end;
    }

    public String get_description() {
        return _description;
    }

    public void set_description(String _description) {
        this._description = _description;
    }

    public long get_categoryId() {
        return _categoryId;
    }

    public void set_categoryId(long _categoryId) {
        this._categoryId = _categoryId;
    }

    public void AddPhoto(PhotoDto dto) {
        photos.add(dto);

    }

    public ArrayList<Uri> getPhotoUris() {
        ArrayList<Uri> result = new ArrayList<>();
        for (PhotoDto dto : photos) {
            result.add(dto.getPhotoUri());
        }

        return result;
    }

}
