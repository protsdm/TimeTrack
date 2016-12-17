package com.example.timetrack;

import java.util.Comparator;

/**
 * Created by Дима on 04.12.2016.
 */

public class CategoryItemDurationComparator implements Comparator<CategoryItem> {
    @Override
    public int compare(CategoryItem o1, CategoryItem o2) {
        return Double.compare(o2.getDuration(), o1.getDuration());
    }
}
