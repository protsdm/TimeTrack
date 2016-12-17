package com.example.timetrack;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;

public class AddActivity extends AppCompatActivity {

    private static final int ADD_IMAGE = 2;
    private ArrayList<Bitmap> imageSource = new ArrayList<>();
    private ArrayList<Uri> imagePaths = new ArrayList<>();
    private ArrayList<CategoryDto> categories;
    private GridView gridView;
    private Spinner categoryDropdown;
    private TimePicker startPicker;
    private TimePicker endPicker;
    private EditText descriptionEditor;
    private TimeTrackDbHelper dbHelper = new TimeTrackDbHelper(AddActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Intent intent = getIntent();

        CategoryDto[] categoryDtos = (CategoryDto[]) intent.getSerializableExtra("categories");
        int categoryIndex = intent.getIntExtra("index",0);
        categoryDropdown = (Spinner) findViewById(R.id.categorySpinner);
        ArrayAdapter<CategoryDto> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categoryDtos);
        categoryDropdown.setAdapter(categoryAdapter);
        categoryDropdown.setSelection(categoryIndex);

        startPicker = (TimePicker) findViewById(R.id.startPicker);
        startPicker.setIs24HourView(true);

        endPicker = (TimePicker) findViewById(R.id.endPicker);
        endPicker.setIs24HourView(true);
        endPicker.setCurrentMinute(Calendar.getInstance().get(Calendar.MINUTE) + 1);

        descriptionEditor = (EditText)findViewById(R.id.editDescription);

        gridView = (GridView) findViewById(R.id.grid_preview);
        ImageAdapter adapter = new ImageAdapter(this, R.layout.grid_photo_edit_item, imagePaths);
        gridView.setAdapter(adapter);

        Button addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                onAddRecord(database);

                database.close();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    public void onAddImage(MenuItem item) {
        Intent needFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        needFileIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        needFileIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(needFileIntent, "Select picture"), ADD_IMAGE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == ADD_IMAGE) {
                ParcelFileDescriptor parcelFileDescriptor;
                Uri selectedImageUri;
                if ((selectedImageUri = data.getData()) == null) {
                    ClipData clipData = data.getClipData();
                    for (int i = 0; i < clipData.getItemCount(); i++) {

                        selectedImageUri = clipData.getItemAt(i).getUri();
                        try {
                            parcelFileDescriptor = getContentResolver().openFileDescriptor(selectedImageUri, "r");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            Log.e("MainActivity", "File not found.");
                            return;
                        }
                        FileDescriptor fd = parcelFileDescriptor.getFileDescriptor();

                        imagePaths.add(selectedImageUri);

                    }
                }
                else {
                        imagePaths.add(selectedImageUri);

                }
                ((ImageAdapter) gridView.getAdapter()).notifyDataSetChanged();
            }
        }
    }

    public void onAddRecord(SQLiteDatabase db) {
        RecordDto dto = new RecordDto();

        dto.set_categoryId(((CategoryDto)categoryDropdown.getSelectedItem()).get_id());

        //todo check start < end!

        if (startPicker.getCurrentHour() > endPicker.getCurrentHour() ||
                (startPicker.getCurrentHour() == endPicker.getCurrentHour() && startPicker.getCurrentMinute() >= endPicker.getCurrentMinute())) {
            Toast.makeText(this, "Invalid time stamp!", Toast.LENGTH_SHORT).show();
            return;
        }
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, startPicker.getCurrentHour());
        today.set(Calendar.MINUTE, startPicker.getCurrentMinute());
        dto.set_start(today.getTimeInMillis());

        today.set(Calendar.HOUR_OF_DAY, endPicker.getCurrentHour());
        today.set(Calendar.MINUTE, endPicker.getCurrentMinute());
        dto.set_end(today.getTimeInMillis());

        dto.set_description(descriptionEditor.getText().toString());

        ArrayList<Uri> attachments = ((ImageAdapter) gridView.getAdapter()).getAttachments();

        /*
        for (Bitmap attach: attachments) {
            ByteBuffer buffer = ByteBuffer.allocate(attach.getByteCount());
            attach.copyPixelsToBuffer(buffer);
            dto.AddPhoto(buffer.array());
        }*/

        ContentValues newValue = new ContentValues();
        newValue.put(TimeTrackContract.Record.CATEGORY_ID, dto.get_categoryId());
        newValue.put(TimeTrackContract.Record.DESCRIPTION, dto.get_description());
        newValue.put(TimeTrackContract.Record.START, dto.get_start());
        newValue.put(TimeTrackContract.Record.END, dto.get_end());
        newValue.put(TimeTrackContract.Record.DURATION, dto.get_end() - dto.get_start());
        long recordId = db.insert(TimeTrackContract.Record.TABLE_NAME, null, newValue);


        for (Uri photoUri : attachments) {
            newValue.clear();
            newValue.put(TimeTrackContract.Photo.RECORD_ID, recordId);
            newValue.put(TimeTrackContract.Photo.PHOTO_PATH, photoUri.toString());
            long insertId = db.insert(TimeTrackContract.Photo.TABLE_NAME, null, newValue);

            try {
                FileOutputStream fos = openFileOutput(DbTestHelper.md5(photoUri.toString() + insertId), MODE_PRIVATE);

                InputStream inputStream = getContentResolver().openInputStream(photoUri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];

                int len = 0;
                while ((len = inputStream.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                }

                fos.write(baos.toByteArray());
                fos.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        Intent resultIntent = new Intent();

        setResult(AddActivity.RESULT_OK, resultIntent);
        finish();
    }

}

