package com.example.timetrack;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * Created by Дима on 19.11.2016.
 */

public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Uri> source;
    private int layoutId;
    private ImageAdapter self = this;

    public ImageAdapter(Context c, int layoutResourceId, ArrayList<Uri> source) {

        mContext = c;
        this.source = source;
        layoutId = layoutResourceId;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        RecordHolder holder = null;

        if (convertView == null) {
            holder = new RecordHolder();
            LayoutInflater layoutInflater = mContext instanceof AddActivity ? ((AddActivity) mContext).getLayoutInflater()
                    : ((ListActivity) mContext).getLayoutInflater();
            convertView = layoutInflater.inflate(layoutId, parent, false);
            holder.imageItem = (ImageView) convertView.findViewById(R.id.item_image);
            holder.position = position;
            convertView.setTag(holder);
        } else {
            holder = (RecordHolder) convertView.getTag();
        }

        FloatingActionButton btn = (FloatingActionButton) convertView.findViewById(R.id.deleteImage);
        if (btn != null) {
            btn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    source.remove(position);
                    self.notifyDataSetChanged();
                }
            });
        }

        try {
            if (layoutId == R.layout.grid_photo_edit_item) {
                holder.imageItem.setImageBitmap(MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), source.get(position)));
            } else if (layoutId == R.layout.grid_photo_item) {
                FileInputStream fis = mContext.openFileInput(DbTestHelper.md5(source.get(position).toString()));
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                while (fis.available() > 0) {
                    bos.write(fis.read());
                }
                fis.close();
                byte[] bytes = bos.toByteArray();
                holder.imageItem.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    public ArrayList<Uri> getAttachments()
    {
        return source;
    }

    static class RecordHolder {
        ImageView imageItem;
        int position;
    }
}
