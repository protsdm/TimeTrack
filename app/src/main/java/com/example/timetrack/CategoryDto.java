package com.example.timetrack;

import java.io.Serializable;

/**
 * Created by Дима on 21.11.2016.
 */

public class CategoryDto implements Serializable {
    private long _id;
    private String _name;

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    @Override
    public String toString() {
        return _name;
    }
}
