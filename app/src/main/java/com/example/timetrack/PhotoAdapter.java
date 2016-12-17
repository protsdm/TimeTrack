package com.example.timetrack;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
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
 * Created by Дима on 06.12.2016.
 */

public class PhotoAdapter extends BaseAdapter {

    private ArrayList<PhotoDto> photos;
    private Context mContext;
    private int layout;

    public PhotoAdapter(Context context, int layoutId, ArrayList<PhotoDto> source) {
        mContext = context;
        layout = layoutId;
        photos = source;
    }

    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public Object getItem(int position) {
        return photos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PhotoHolder holder = null;

        if (convertView == null) {
            holder = new PhotoHolder();
            LayoutInflater layoutInflater = ((ListActivity) mContext).getLayoutInflater();
            convertView = layoutInflater.inflate(layout, parent, false);
            holder.imageItem = (ImageView) convertView.findViewById(R.id.item_image);
            holder.position = position;
            convertView.setTag(holder);
        } else {
            holder = (PhotoHolder) convertView.getTag();
        }
        PhotoDto photoDto = photos.get(position);
        FileInputStream fis = null;
        try {
            fis = mContext.openFileInput(DbTestHelper.md5(photoDto.getPhotoUri().toString() +photoDto.get_id()));
            //ByteArrayOutputStream bos = new ByteArrayOutputStream();
            //while (fis.available() > 0) {
             //   bos.write(fis.read());
           // }
            byte[] bytes = new byte[(int)fis.getChannel().size()];
            fis.read(bytes);
            fis.close();

           // byte[] bytes = bos.toByteArray();
            //bos.close();
            holder.imageItem.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    private class PhotoHolder {
        ImageView imageItem;
        int position;
    }
}
