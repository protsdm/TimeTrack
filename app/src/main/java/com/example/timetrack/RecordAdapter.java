package com.example.timetrack;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import android.icu.text.RelativeDateTimeFormatter;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Дима on 25.11.2016.
 */

public class RecordAdapter extends BaseAdapter {

    private DeleteRecordListener listener;
    private ArrayList<RecordDto> records;
    private Context mContext;
    private int layout;

    public RecordAdapter(ArrayList<RecordDto> records, Context mContext, int layout, DeleteRecordListener listener) {
        this.records = records;
        this.mContext = mContext;
        this.layout = layout;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return records.size();
    }

    @Override
    public Object getItem(int position) {
        return records.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        RecordHolder holder = null;
        ImageButton deleteButton = null;
        if (convertView == null) {

            holder = new RecordHolder();
            LayoutInflater layoutInflater = ((ListActivity)mContext).getLayoutInflater();
            convertView = layoutInflater.inflate(layout, parent, false);

            holder.dateView = (TextView) convertView.findViewById(R.id.dateTextView);
            holder.timeView = (TextView) convertView.findViewById(R.id.timeTextView);
            holder.descriptionView = (TextView) convertView.findViewById(R.id.descriptionView);
            //holder.photosGrid = (GridView) convertView.findViewById(R.id.photosView);

            convertView.setTag(holder);
        }
        else {
            holder = (RecordHolder) convertView.getTag();
        }

        RecordDto item = records.get(position);
        holder.dto = item;
        holder.dto.set_id(item.get_id());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfTime = new SimpleDateFormat("H:mm");

        holder.dateView.setText(sdf.format(new Date(item.get_start())));
        holder.timeView.setText(String.format("from %s to %s",sdfTime.format(new Date(item.get_start())),sdfTime.format(new Date(item.get_end()))));
        holder.descriptionView.setText(item.get_description());

        //PhotoAdapter photosAdapter = new PhotoAdapter(mContext, R.layout.grid_photo_item, item.getPhotos());
        //holder.photosGrid.setAdapter(photosAdapter);

        TableLayout tl = (TableLayout)convertView.findViewById(R.id.photoTable);
        for (PhotoDto photoDto : item.getPhotos()) {
            TableRow tRow  = new TableRow(mContext);
            //tRow.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            ImageView photoView = new ImageView(mContext);

            try {
                FileInputStream fis = mContext.openFileInput(DbTestHelper.md5(photoDto.getPhotoUri().toString() + photoDto.get_id()));

                byte[] bytes = new byte[(int) fis.getChannel().size()];
                fis.read(bytes);
                fis.close();

                //photoView.setLayoutParams(new ViewGroup.LayoutParams(800, 600));
                photoView.setPadding(10, 15, 0, 5);
                photoView.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            tRow.addView(photoView, 800, 600);
            tl.addView(tRow);
        }

        deleteButton = (ImageButton)convertView.findViewById(R.id.deleteRecord);
        deleteButton.setTag(holder);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RecordHolder tag = (RecordHolder) v.getTag();
                listener.deleteRecord(tag.dto);
            }
        });

        return convertView;
    }



    static class RecordHolder {
        RecordDto dto;
        TextView dateView;
        TextView timeView;
        TextView descriptionView;
        GridView photosGrid;
    }
}
