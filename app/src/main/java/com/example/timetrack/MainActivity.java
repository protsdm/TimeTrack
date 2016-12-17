package com.example.timetrack;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    private ArrayList<CategoryItem> categoryItems;
    //private SQLiteDatabase database;
    private ListView categoryList;
    private TimeTrackDbHelper dbHelper = new TimeTrackDbHelper(MainActivity.this);

    private static final int ADD_RECORD = 3;
    private static final int GET_STATISTIC = 4;

    //todo check onResume updates records count in rows!

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //boolean deleteDatabase = getApplicationContext().deleteDatabase("TimeTrack.db");
        //TimeTrackDbHelper dbHelper = new TimeTrackDbHelper(getApplicationContext());
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        categoryItems = getCategoriesWithCount(database);

        categoryList = (ListView)findViewById(R.id.categoryList);
        CategoryAdapter adapter = new CategoryAdapter(this, R.layout.category_item, categoryItems);
        categoryList.setAdapter(adapter);

        categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String categoryName = ((TextView) view.findViewById(R.id.categoryName)).getText().toString();
                openList(categoryName);
            }
        });

        categoryList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Edit category");
                final CharSequence oldItem = ((TextView) view.findViewById(R.id.categoryName)).getText();
                final EditText editText = new EditText(MainActivity.this);
                editText.setText(oldItem);
                builder.setView(editText);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        renameCategory(categoryItems.get(position), editText.getText().toString());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteCategory(categoryItems.get(position));
                    }
                });
                builder.show();

                return true;
            }
        });

        FloatingActionButton floatButton = (FloatingActionButton)findViewById(R.id.addRecordBtn);
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newRecordIntent = new Intent(MainActivity.this, AddActivity.class);

                CategoryDto[] categories = new CategoryDto[categoryItems.size()];

                for (int i = 0; i < categories.length; i++) {
                    categories[i] = categoryItems.get(i).getDto();
                }
                newRecordIntent.putExtra("categories", categories);
                startActivityForResult(newRecordIntent, ADD_RECORD);
            }
        });

        database.close();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == ADD_RECORD) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                UpdateList(database);
                database.close();
                //Toast.makeText(getApplicationContext(), record.get_description(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.addCategory);
        item.setIcon(android.R.drawable.ic_menu_add);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT | MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void renameCategory(CategoryItem item, String newName) {
        ContentValues cv = new ContentValues();
        cv.put(TimeTrackContract.Category.NAME, newName);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int update = database.update(TimeTrackContract.Category.TABLE_NAME, cv, TimeTrackContract.Category._ID + " = ?",
                new String[]{String.valueOf(item.getDto().get_id())});
        UpdateList(database);
        database.close();
    }

    private void deleteCategory(CategoryItem item) {
        ArrayList<String> photoUris = new ArrayList<>();
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery(String.format("select * from %s where %s in " +
                        "(select %s from %s where %s = %s)",
                TimeTrackContract.Photo.TABLE_NAME,
                TimeTrackContract.Photo.RECORD_ID,
                TimeTrackContract.Record._ID,
                TimeTrackContract.Record.TABLE_NAME,
                TimeTrackContract.Record.CATEGORY_ID,
                item.getDto().get_id()), null);
        while (cursor.moveToNext()) {
            photoUris.add(cursor.getString(cursor.getColumnIndex(TimeTrackContract.Photo.PHOTO_PATH)));
        }
        cursor.close();
        for (String uri : photoUris) {
            File file = new File(getFilesDir(), DbTestHelper.md5(uri.toString()));
            boolean deleted = file.delete();
        }

        database.execSQL(String.format("delete from %s where %s in " +
                        "(select %s from %s where %s = %s)",
                TimeTrackContract.Photo.TABLE_NAME,
                TimeTrackContract.Photo.RECORD_ID,
                TimeTrackContract.Record._ID,
                TimeTrackContract.Record.TABLE_NAME,
                TimeTrackContract.Record.CATEGORY_ID,
                item.getDto().get_id()));
        database.execSQL(String.format("delete from %s where %s = %s",
                TimeTrackContract.Record.TABLE_NAME,
                TimeTrackContract.Record.CATEGORY_ID,
                item.getDto().get_id()));
        database.execSQL(String.format("delete from %s where %s = %s",
                TimeTrackContract.Category.TABLE_NAME,
                TimeTrackContract.Category._ID,
                item.getDto().get_id()));
        UpdateList(database);
        database.close();
    }

    public void onStatistic(MenuItem item) {
        Intent statIntent = new Intent(this, StatisticActivity.class);
        CategoryDto[] categories = new CategoryDto[categoryItems.size()];
        for (int i=0; i< categories.length; i++) {
            categories[i] = categoryItems.get(i).getDto();
        }
        statIntent.putExtra("categories", categories);
        startActivity(statIntent);
    }

    public void onAddCategory(MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Add Category");
        final EditText input = new EditText(this);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                CategoryDto dto = new CategoryDto();
                dto.set_name(input.getText().toString());
                ContentValues values = new ContentValues();
                values.put(TimeTrackContract.Category.NAME, input.getText().toString());
                long newId = database.insert(TimeTrackContract.Category.TABLE_NAME, null,values);
                dto.set_id(newId);
                CategoryItem newItem = new CategoryItem();
                newItem.setDto(dto);
                newItem.setCount(0);

                categoryItems.add(newItem);

                ((CategoryAdapter)categoryList.getAdapter()).notifyDataSetChanged();
                database.close();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void openList(String category) {
        CategoryDto[] categories = new CategoryDto[categoryItems.size()];
        for (int i = 0; i < categories.length; i++) {
            categories[i] = categoryItems.get(i).getDto();
        }
        Intent intent = new Intent(MainActivity.this, ListActivity.class);
        intent.putExtra("category", category);
        intent.putExtra("categories", categories);
        startActivity(intent);
    }

    private void UpdateList(SQLiteDatabase database) {

        categoryItems.clear();
        categoryItems.addAll(getCategoriesWithCount(database));
        ((CategoryAdapter)categoryList.getAdapter()).notifyDataSetChanged();
    }

    public ArrayList<CategoryItem> getCategoriesWithCount(SQLiteDatabase database) {

        ArrayList<CategoryItem> items = new ArrayList<>();

        ArrayList<CategoryDto> dtos = new ArrayList<>();
        String[] projection = {
                TimeTrackContract.Category._ID,
                TimeTrackContract.Category.NAME
        };
        Cursor cursor = database.query(TimeTrackContract.Category.TABLE_NAME, projection, null, null, null, null, null);

        while (cursor.moveToNext()) {
            CategoryDto dto = new CategoryDto();
            dto.set_id(cursor.getInt(cursor.getColumnIndex(projection[0])));
            dto.set_name(cursor.getString(cursor.getColumnIndex(projection[1])));
            dtos.add(dto);
        }
        cursor.close();
        for (CategoryDto dto : dtos) {
            Cursor countCursor = database.rawQuery(String.format("select count(*) from %s where %s = %s",
                                                    TimeTrackContract.Record.TABLE_NAME,
                                                    TimeTrackContract.Record.CATEGORY_ID,
                                                    dto.get_id() ), null);
            countCursor.moveToFirst();
            CategoryItem item = new CategoryItem();
            item.setDto(dto);
            item.setCount(countCursor.getInt(0));
            items.add(item);
            countCursor.close();
        }
        return items;
    }


}
