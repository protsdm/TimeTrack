package com.example.timetrack;

import java.util.Comparator;

/**
 * Created by Дима on 03.12.2016.
 */

public class CategoryItemPercentComparator implements Comparator<CategoryItem> {

    @Override
    public int compare(CategoryItem o1, CategoryItem o2) {
        return Double.compare(o2.getPercent(), o1.getPercent());
    }
}
