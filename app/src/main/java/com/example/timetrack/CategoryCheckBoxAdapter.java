package com.example.timetrack;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Дима on 04.12.2016.
 */

public class CategoryCheckBoxAdapter extends BaseAdapter {
    private CategoryDto[] categories;
    private Context mContext;
    private int layout;
    private ArrayList<CategoryDto> checked = new ArrayList<>();

    public CategoryCheckBoxAdapter(Context c, int layoutSource, CategoryDto[] categories) {
        mContext = c;
        layout = layoutSource;
        this.categories = categories;
    }

    @Override
    public int getCount() {
        return categories.length;
    }

    @Override
    public Object getItem(int position) {
        return categories[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CategoryHolder holder;
        if (convertView == null) {

            holder = new CategoryHolder();
            LayoutInflater layoutInflater = ((Activity)mContext).getLayoutInflater();
            convertView = layoutInflater.inflate(layout, parent, false);
            CheckBox checkBox =  (CheckBox) convertView.findViewById(R.id.catCheckBox);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        checked.add(holder.getDto());
                    }
                    else {
                        checked.remove(holder.getDto());
                    }
                }
            });

            holder.categoryNameView = (TextView) convertView.findViewById(R.id.categoryText);
            holder.categoryCheckbox = checkBox;

            convertView.setTag(holder);
        }
        else {
            holder = (CategoryHolder) convertView.getTag();
        }

        CategoryDto item = categories[position];
        holder.setDto(item);
        holder.categoryNameView.setText(item.get_name());

        return convertView;
    }

    public ArrayList<CategoryDto> getChecked() {
        return new ArrayList<>(checked);
    }

    private class CategoryHolder {
        TextView categoryNameView;
        CheckBox categoryCheckbox;

        public CategoryDto getDto() {
            return dto;
        }

        public void setDto(CategoryDto dto) {
            this.dto = dto;
        }

        CategoryDto dto;
    }

}
