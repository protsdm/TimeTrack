package com.example.timetrack;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.androidplot.pie.PieChart;
import com.androidplot.pie.Segment;
import com.androidplot.pie.SegmentFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class StatListActivity extends AppCompatActivity {

    private ListView categoryStatList;
    private SQLiteDatabase database;
    private ArrayList<CategoryItem> categoryItems = new ArrayList<>();
    private String mode;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat_list);

        TimeTrackDbHelper helper = new TimeTrackDbHelper(this);
        database = helper.getReadableDatabase();

        categoryStatList = (ListView) findViewById(R.id.categoryStatList);

        intent = getIntent();
        int statMode = intent.getIntExtra("stat", 0);
        switch (statMode) {
            case 1: {
                showFrequentStat(intent);
                break;
            }
            case 2: {
                showLongestStat(intent);
                break;
            }
            case 3: {
                showChosenLongectStat(intent);
                break;
            }
            case 4: {
                showPie(intent);
                break;
            }
        }
    }

    private void showChosenLongectStat(Intent intent) {
        mode = intent.getSerializableExtra("mode").toString();
        CategoryDto[] categories = (CategoryDto[]) intent.getSerializableExtra("categories");
        /*ArrayList<CategoryItem> items = new ArrayList<>();
        for (int i=0; i < categories.length; i++) {
            CategoryItem item = new CategoryItem();
            item.setDto(categories[i]);
            items.add(item);
        }*/

        Button showChosen = (Button) findViewById(R.id.showChosenBtn);
        showChosen.setVisibility(View.VISIBLE);

        ListView categoriesLV = (ListView) findViewById(R.id.categoryCheckBoxes);
        categoriesLV.setVisibility(View.VISIBLE);
        CategoryCheckBoxAdapter adapter = new CategoryCheckBoxAdapter(this,R.layout.category_check_box, categories);
        categoriesLV.setAdapter(adapter);
    }

    public void GetChosenStat(View v) {
        ListView lv = (ListView) findViewById(R.id.categoryCheckBoxes);
        CategoryCheckBoxAdapter adapter = (CategoryCheckBoxAdapter)lv.getAdapter();
        ArrayList<CategoryDto> checkedCategories = adapter.getChecked();
        categoryItems.clear();
        switch (mode) {
            case "month": {

                int monthNumber = getIntent().getIntExtra("month", -1);
                Cursor monthCursor = null;
                for (CategoryDto checked : checkedCategories) {

                    monthCursor = database.rawQuery(String.format("select sum(%s) as CatSum from %s " +
                                    "where ((strftime('%%m', %s / 1000, 'unixepoch') = '%s') and %s = %s )" +
                                    "or ( (strftime('%%m', %s / 1000, 'unixepoch') = '%s') and %s = %s);",
                            TimeTrackContract.Record.DURATION,
                            TimeTrackContract.Record.TABLE_NAME,
                            TimeTrackContract.Record.START,
                            String.format("%02d", monthNumber),
                            TimeTrackContract.Record.CATEGORY_ID,
                            checked.get_id(),
                            TimeTrackContract.Record.END,
                            String.format("%02d", monthNumber),
                            TimeTrackContract.Record.CATEGORY_ID,
                            checked.get_id()),null);
                    monthCursor.moveToFirst();
                    double categoriesDuration = (monthCursor.getLong(monthCursor.getColumnIndex("CatSum")) / 3600000.0);

                    CategoryItem item = new CategoryItem();
                    item.setDto(checked);
                    item.setDuration(categoriesDuration);

                    categoryItems.add(item);
                }
                if (monthCursor != null) {
                    monthCursor.close();
                }

                Collections.sort(categoryItems, new CategoryItemDurationComparator());
                CategoryAdapter adapterView = new CategoryAdapter(this, R.layout.category_item, categoryItems);
                categoryStatList.setAdapter(adapterView);
                break;
            }
            case "period": {
                long startPeriod = getIntent().getLongExtra("start", -1);
                long endPeriod = getIntent().getLongExtra("end", -1);

                Cursor periodCursor = null;
                for (CategoryDto checked : checkedCategories) {

                    periodCursor = database.rawQuery(String.format("select sum(%s) as CatSum from %s " +
                                    "where ( %s >= %s and %s = %s )" +
                                    "and (  %s  <= %s and %s = %s);",
                            TimeTrackContract.Record.DURATION,
                            TimeTrackContract.Record.TABLE_NAME,
                            TimeTrackContract.Record.START,
                            startPeriod,
                            TimeTrackContract.Record.CATEGORY_ID,
                            checked.get_id(),
                            TimeTrackContract.Record.END,
                            endPeriod,
                            TimeTrackContract.Record.CATEGORY_ID,
                            checked.get_id()),null);
                    periodCursor.moveToFirst();

                    double duration = periodCursor.getLong(periodCursor.getColumnIndex("CatSum"))/3600000.0;
                    CategoryItem item = new CategoryItem();
                    item.setDto(checked);
                    item.setDuration(duration);

                    categoryItems.add(item);
                }
                if (checkedCategories.size() > 0)periodCursor.close();

                Collections.sort(categoryItems, new CategoryItemDurationComparator());
                CategoryAdapter adapterView = new CategoryAdapter(this, R.layout.category_item, categoryItems);
                categoryStatList.setAdapter(adapterView);

                break;
            }
        }

    }

    private void showLongestStat(Intent intent) {
        String mode = intent.getSerializableExtra("mode").toString();
        CategoryDto[] categories = (CategoryDto[]) intent.getSerializableExtra("categories");

        switch (mode) {
            case "month": {

                int monthNumber = intent.getIntExtra("month", -1);
                Cursor monthCursor = null;
                for (int i=0; i < categories.length; i++) {

                    monthCursor = database.rawQuery(String.format("select sum(%s) as CatSum from %s " +
                                    "where ((strftime('%%m', %s / 1000, 'unixepoch') = '%s') and %s = %s )" +
                                    "or ( (strftime('%%m', %s / 1000, 'unixepoch') = '%s') and %s = %s);",
                            TimeTrackContract.Record.DURATION,
                            TimeTrackContract.Record.TABLE_NAME,
                            TimeTrackContract.Record.START,
                            String.format("%02d", monthNumber),
                            TimeTrackContract.Record.CATEGORY_ID,
                            categories[i].get_id(),
                            TimeTrackContract.Record.END,
                            String.format("%02d", monthNumber),
                            TimeTrackContract.Record.CATEGORY_ID,
                            categories[i].get_id()),null);
                    monthCursor.moveToFirst();
                    double categoriesDuration = (monthCursor.getLong(monthCursor.getColumnIndex("CatSum")) / 3600000.0);

                    CategoryItem item = new CategoryItem();
                    item.setDto(categories[i]);
                    item.setDuration(categoriesDuration);

                    categoryItems.add(item);
                }
                if (categories.length > 0) monthCursor.close();

                Collections.sort(categoryItems, new CategoryItemDurationComparator());
                CategoryAdapter adapter = new CategoryAdapter(this, R.layout.category_item, categoryItems);
                categoryStatList.setAdapter(adapter);
                break;
            }
            case "period": {
                long startPeriod = intent.getLongExtra("start", -1);
                long endPeriod = intent.getLongExtra("end", -1);

                Cursor periodCursor = null;
                for (int i=0; i < categories.length; i++) {

                    periodCursor = database.rawQuery(String.format("select sum(%s) as CatSum from %s " +
                                    "where ( %s >= %s and %s = %s )" +
                                    "and (  %s  <= %s and %s = %s);",
                            TimeTrackContract.Record.DURATION,
                            TimeTrackContract.Record.TABLE_NAME,
                            TimeTrackContract.Record.START,
                            startPeriod,
                            TimeTrackContract.Record.CATEGORY_ID,
                            categories[i].get_id(),
                            TimeTrackContract.Record.END,
                            endPeriod,
                            TimeTrackContract.Record.CATEGORY_ID,
                            categories[i].get_id()),null);
                    periodCursor.moveToFirst();

                    double duration = periodCursor.getLong(periodCursor.getColumnIndex("CatSum"))/3600000.0;
                    CategoryItem item = new CategoryItem();
                    item.setDto(categories[i]);
                    item.setDuration(duration);

                    categoryItems.add(item);
                }
                if (categories.length > 0) periodCursor.close();

                break;
            }
        }
        Collections.sort(categoryItems, new CategoryItemDurationComparator());

        CategoryAdapter adapter = new CategoryAdapter(this, R.layout.category_item, categoryItems);
        categoryStatList.setAdapter(adapter);
    }

    private void showPie(Intent intent) {
        String mode = intent.getSerializableExtra("mode").toString();
        CategoryDto[] categories = (CategoryDto[]) intent.getSerializableExtra("categories");
        switch (mode) {
            case "month": {

                int monthNumber = intent.getIntExtra("month", -1);
                Cursor monthCursor = null;
                for (int i=0; i < categories.length; i++) {

                    monthCursor = database.rawQuery(String.format("select sum(%s) as CatSum from %s " +
                                    "where ((strftime('%%m', %s / 1000, 'unixepoch') = '%s') and %s = %s )" +
                                    "or ( (strftime('%%m', %s / 1000, 'unixepoch') = '%s') and %s = %s);",
                            TimeTrackContract.Record.DURATION,
                            TimeTrackContract.Record.TABLE_NAME,
                            TimeTrackContract.Record.START,
                            String.format("%02d", monthNumber),
                            TimeTrackContract.Record.CATEGORY_ID,
                            categories[i].get_id(),
                            TimeTrackContract.Record.END,
                            String.format("%02d", monthNumber),
                            TimeTrackContract.Record.CATEGORY_ID,
                            categories[i].get_id()),null);
                    monthCursor.moveToFirst();
                    double categoriesDuration = (monthCursor.getLong(monthCursor.getColumnIndex("CatSum")) / 3600000.0);
                    if (categoriesDuration !=0){
                        CategoryItem item = new CategoryItem();
                        item.setDto(categories[i]);
                        item.setDuration(categoriesDuration);

                        categoryItems.add(item);
                    }
                }
                if (categories.length > 0) monthCursor.close();

                Collections.sort(categoryItems, new CategoryItemDurationComparator());
                CategoryAdapter adapter = new CategoryAdapter(this, R.layout.category_item, categoryItems);
                categoryStatList.setAdapter(adapter);
                break;
            }
            case "period": {
                long startPeriod = intent.getLongExtra("start", -1);
                long endPeriod = intent.getLongExtra("end", -1);

                Cursor periodCursor = null;
                for (int i=0; i < categories.length; i++) {

                    periodCursor = database.rawQuery(String.format("select sum(%s) as CatSum from %s " +
                                    "where ( %s >= %s and %s = %s )" +
                                    "and (  %s  <= %s and %s = %s);",
                            TimeTrackContract.Record.DURATION,
                            TimeTrackContract.Record.TABLE_NAME,
                            TimeTrackContract.Record.START,
                            startPeriod,
                            TimeTrackContract.Record.CATEGORY_ID,
                            categories[i].get_id(),
                            TimeTrackContract.Record.END,
                            endPeriod,
                            TimeTrackContract.Record.CATEGORY_ID,
                            categories[i].get_id()),null);
                    periodCursor.moveToFirst();

                    double duration = periodCursor.getLong(periodCursor.getColumnIndex("CatSum"))/3600000.0;
                    if (duration != 0) {
                        CategoryItem item = new CategoryItem();
                        item.setDto(categories[i]);
                        item.setDuration(duration);

                        categoryItems.add(item);
                    }
                }
                if (categories.length > 0) periodCursor.close();

                break;
            }
        }
        ListView lView = (ListView) findViewById(R.id.categoryStatList);
        lView.setVisibility(View.GONE);

        PieChart pie = (PieChart) findViewById(R.id.pieChart);
        if (categoryItems.size() == 0) {
            pie.setTitle("Nothing to show");
        } else for (CategoryItem item : categoryItems){
            Random rnd = new Random();
            Segment s = new Segment(item.getDto().get_name(), item.getDuration());
            SegmentFormatter sf = new SegmentFormatter(Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));
            Paint paint = new Paint();
            paint.setTextSize(25);
            paint.setFakeBoldText(true);
            sf.setLabelPaint(paint);
            pie.addSegment(s,sf);
        }
        pie.getBorderPaint().setColor(Color.TRANSPARENT);
        pie.getBackgroundPaint().setColor(Color.TRANSPARENT);

        pie.setVisibility(View.VISIBLE);
    }

    private void showFrequentStat(Intent intent) {
        String mode = intent.getSerializableExtra("mode").toString();
        CategoryDto[] categories = (CategoryDto[]) intent.getSerializableExtra("categories");
        switch (mode) {
            case "month": {

                int monthNumber = intent.getIntExtra("month", -1);

                int countMonth = -1;
                Cursor countCursor = database.rawQuery(String.format("select * from %s " +
                        "where (( strftime('%%m', %s / 1000, 'unixepoch') = '%s')" +
                        "or ( strftime('%%m', %s / 1000, 'unixepoch') = '%s'));",
                        TimeTrackContract.Record.TABLE_NAME,
                        TimeTrackContract.Record.START,
                        String.format("%02d", monthNumber),
                        TimeTrackContract.Record.END,
                        String.format("%02d", monthNumber)),null);
                countMonth = countCursor.getCount();
                countCursor.close();

                Cursor monthCursor = null;
                for (int i=0; i < categories.length; i++) {

                    monthCursor = database.rawQuery(String.format("select * from %s " +
                                    "where ((strftime('%%m', %s / 1000, 'unixepoch') = '%s') and %s = %s )" +
                                    "or ( (strftime('%%m', %s / 1000, 'unixepoch') = '%s') and %s = %s);",
                            TimeTrackContract.Record.TABLE_NAME,
                            TimeTrackContract.Record.START,
                            String.format("%02d", monthNumber),
                            TimeTrackContract.Record.CATEGORY_ID,
                            categories[i].get_id(),
                            TimeTrackContract.Record.END,
                            String.format("%02d", monthNumber),
                            TimeTrackContract.Record.CATEGORY_ID,
                            categories[i].get_id()),null);
                    int categoriesCount = monthCursor.getCount();

                    CategoryItem item = new CategoryItem();
                    item.setDto(categories[i]);
                    if (countMonth == 0) {
                        item.setPercent(0);
                    }
                    else {
                        item.setPercent(categoriesCount / ((countMonth + 0.0)) * 100);
                    }

                    categoryItems.add(item);
                }
                if (categories.length > 0) monthCursor.close();
                break;
            }
            case "period": {

                long startPeriod = intent.getLongExtra("start", -1);
                long endPeriod = intent.getLongExtra("end", -1);
                int countPeriod = -1;
                Cursor countCursor = database.rawQuery(String.format("select * from %s " +
                                "where ( %s  >= %s)" +
                                "and ( %s <= %s);",
                        TimeTrackContract.Record.TABLE_NAME,
                        TimeTrackContract.Record.START,
                        String.valueOf(startPeriod),
                        TimeTrackContract.Record.END,
                        String.valueOf(endPeriod)),null);
                countPeriod = countCursor.getCount();
                countCursor.close();

                Cursor monthCursor = null;
                for (int i=0; i < categories.length; i++) {

                    monthCursor = database.rawQuery(String.format("select * from %s " +
                                    "where ( %s >= %s and %s = %s )" +
                                    "and (  %s  <= %s and %s = %s);",
                            TimeTrackContract.Record.TABLE_NAME,
                            TimeTrackContract.Record.START,
                            startPeriod,
                            TimeTrackContract.Record.CATEGORY_ID,
                            categories[i].get_id(),
                            TimeTrackContract.Record.END,
                            endPeriod,
                            TimeTrackContract.Record.CATEGORY_ID,
                            categories[i].get_id()),null);
                    int categoriesCount = monthCursor.getCount();

                    CategoryItem item = new CategoryItem();
                    item.setDto(categories[i]);
                    if (countPeriod == 0) {
                        item.setPercent(0);
                    }
                    else {
                        item.setPercent(categoriesCount / ((countPeriod + 0.0)) * 100);
                    }

                    categoryItems.add(item);
                }
                if (categories.length > 0) monthCursor.close();

                break;
            }
        }
        Collections.sort(categoryItems, new CategoryItemPercentComparator());

        CategoryAdapter adapter = new CategoryAdapter(this, R.layout.category_item, categoryItems);
        categoryStatList.setAdapter(adapter);
    }
}
