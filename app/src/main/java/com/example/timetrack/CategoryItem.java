package com.example.timetrack;

/**
 * Created by Дима on 21.11.2016.
 */

public class CategoryItem {

    private CategoryDto dto;
    private int count;
    private double percent = -1;
    private double duration = -1;

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }



    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public CategoryDto getDto() {
        return dto;
    }

    public void setDto(CategoryDto dto) {
        this.dto = dto;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getText() {
        if (percent != -1) {
            return String.format("%.2f %%", percent);
        } else if (duration != -1) {
            return String.format("%.1f hours", duration);
        } else {
            String record = count % 10 == 1? "record" : "records";
            return String.format("%s " + record , count);
        }
    }

}
