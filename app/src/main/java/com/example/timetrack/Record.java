package com.example.timetrack;

import java.util.ArrayList;

/**
 * Created by Дима on 25.11.2016.
 */

public class Record {
    String description;
    Long startDate;
    Long endDate;

    public ArrayList<PhotoDto> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<PhotoDto> photos) {
        this.photos = photos;
    }

    ArrayList<PhotoDto> photos;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }
}
