package com.example.timetrack;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class ListActivity extends AppCompatActivity implements DeleteRecordListener{

    private ArrayList<RecordDto> records;
    //SQLiteDatabase database;
    private String category;
    private static final int ADD_RECORD = 1;
    private TimeTrackDbHelper dbHelper = new TimeTrackDbHelper(ListActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Intent startingIntent = getIntent();
        category = startingIntent.getStringExtra("category");

        setTitle(category);

        //dbHelper = new TimeTrackDbHelper(getApplicationContext());
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        records = getRecords(database, category);

        database.close();

        RecordAdapter adapter = new RecordAdapter(records, this, R.layout.record_item, this);
        ListView listView = (ListView) findViewById(R.id.recordsListView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ListActivity.this, "Ouch! You tapped me! >_<", Toast.LENGTH_SHORT).show();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewRecord(view);
            }
        });

    }

    public void deleteRecord(RecordDto dto) {
        ArrayList<Uri> photoUris = dto.getPhotoUris();
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        if(photoUris.size() > 0) {
            database.rawQuery(String.format("delete from %s where %s = %s", TimeTrackContract.Photo.TABLE_NAME, TimeTrackContract.Photo.RECORD_ID, dto.get_id()), null);
            for (Uri uriToDelete : photoUris) {
                File file = new File(getFilesDir(), DbTestHelper.md5(uriToDelete.toString()));
                boolean deleted = file.delete();
            }
        }
        database.delete(TimeTrackContract.Record.TABLE_NAME, TimeTrackContract.Record._ID +" = ?", new String[] {String.valueOf(dto.get_id())});
        database.close();

        UpdateSource();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == ADD_RECORD) {
                UpdateSource();

                //Toast.makeText(getApplicationContext(), record.get_description(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void UpdateSource() {
        records.clear();
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        records.addAll(getRecords(database, category));
        ((RecordAdapter)((ListView)findViewById(R.id.recordsListView)).getAdapter()).notifyDataSetChanged();
        database.close();
    }

    private ArrayList<RecordDto> getRecords(SQLiteDatabase db, String category) {
        Cursor cursor = db.rawQuery(getDbQuery(category), null);
        ArrayList<RecordDto> records = new ArrayList<RecordDto>();
        while(cursor.moveToNext())
        {

            RecordDto record = new RecordDto();
            long start = cursor.getLong(cursor.getColumnIndex(TimeTrackContract.Record.START));
            long end = cursor.getLong(cursor.getColumnIndex(TimeTrackContract.Record.END));
            String desc = cursor.getString(cursor.getColumnIndex(TimeTrackContract.Record.DESCRIPTION));
            int id = cursor.getInt(cursor.getColumnIndex(TimeTrackContract.Record._ID));
            record.set_description(desc);
            record.set_start(start);
            record.set_end(end);
            record.set_id(id);

            Cursor photoCursor = db.rawQuery(getPhotosById(id), null);
            while (photoCursor.moveToNext()) {
                Uri photoUri = Uri.parse(photoCursor.getString(photoCursor.getColumnIndex(TimeTrackContract.Photo.PHOTO_PATH)));
                long photoId = photoCursor.getLong(photoCursor.getColumnIndex(TimeTrackContract.Photo._ID));

                PhotoDto photoDto = new PhotoDto();
                photoDto.set_id(photoId);
                photoDto.setPhotoUri(photoUri);
                record.AddPhoto(photoDto);
            }

            records.add(record);
        }
        return records;
    }

    private String getDbQuery(String category)
    {
        return String.format("select * from %s where %s in (select %s from %s where %s = \"%s\")",
                TimeTrackContract.Record.TABLE_NAME,
                TimeTrackContract.Record.CATEGORY_ID,
                TimeTrackContract.Category._ID,
                TimeTrackContract.Category.TABLE_NAME,
                TimeTrackContract.Category.NAME,
                category);
    }

    private String getPhotosById(int recordId) {
        return String.format("select * from %s where %s = %s",
                TimeTrackContract.Photo.TABLE_NAME,
                TimeTrackContract.Photo.RECORD_ID,
                recordId);
    }

    private void addNewRecord(View view) {
        Intent startIntent = getIntent();
        Intent newRecord = new Intent(this, AddActivity.class);

        CategoryDto[] categories = (CategoryDto[]) startIntent.getSerializableExtra("categories");
        int index = 0;
        for (int i = 0; i < categories.length; i++) {
            if (categories[i].toString().equals(category)) {
                index = i;
            }
        }
        newRecord.putExtra("index", index);
        newRecord.putExtra("categories", categories);
        startActivityForResult(newRecord, ADD_RECORD);
    }
}
