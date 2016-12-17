package com.example.timetrack;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Дима on 21.11.2016.
 */

public class CategoryAdapter extends BaseAdapter {

    private ArrayList<CategoryItem> source;
    private Context mContext;
    private int layout;

    public CategoryAdapter(Context c, int layoutSource, ArrayList<CategoryItem> categories) {
        mContext = c;
        layout = layoutSource;
        source = categories;
    }

    @Override
    public int getCount() {
        return source.size();
    }

    @Override
    public Object getItem(int position) {
        return source.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       CategoryHolder holder = null;
        if (convertView == null) {

            holder = new CategoryHolder();
            LayoutInflater layoutInflater = ((Activity)mContext).getLayoutInflater();
            convertView = layoutInflater.inflate(layout, parent, false);

            holder.categoryNameView = (TextView) convertView.findViewById(R.id.categoryName);
            holder.categoryCountView = (TextView) convertView.findViewById(R.id.categoryCount);

            convertView.setTag(holder);
        }
        else {
             holder = (CategoryHolder) convertView.getTag();
        }

        CategoryItem item = source.get(position);
        holder.categoryNameView.setText(item.getDto().get_name());
        holder.categoryCountView.setText(item.getText());
        return convertView;
    }

    static class CategoryHolder {
        TextView categoryNameView;
        TextView categoryCountView;
    }
}
