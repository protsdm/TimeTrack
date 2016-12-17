package com.example.timetrack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

public class StatisticActivity extends AppCompatActivity {

    RadioButton monthButton;
    RadioButton periodButton;
    LinearLayout monthLayout;
    LinearLayout periodLayout;

    DatePicker startDatePicker;
    DatePicker endDatePicker;
    TimePicker startTimePicker;
    TimePicker endTimePicker;

    boolean isMonthFilter = true;
    Spinner monthSpinner;
    String[] months = {"Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        monthLayout = (LinearLayout) findViewById(R.id.monthView);
        periodLayout = (LinearLayout) findViewById(R.id.periodView);

        monthButton = (RadioButton) findViewById(R.id.radioMonth);
        periodButton = (RadioButton) findViewById(R.id.radioPeriod);

        monthButton.setOnClickListener(rbListener);
        periodButton.setOnClickListener(rbListener);

        monthSpinner = (Spinner) findViewById(R.id.monthsSpinner);
        ArrayAdapter monthAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, months);
        monthSpinner.setAdapter(monthAdapter);

        startTimePicker = (TimePicker) findViewById(R.id.startTimePicker);
        endTimePicker = (TimePicker) findViewById(R.id.toTimePicker);

        startTimePicker.setIs24HourView(true);
        endTimePicker.setIs24HourView(true);

    }

    public void getStatList(View v) {

        Intent getStatsIntent = new Intent(this, StatListActivity.class);
        switch(v.getId()){
            case R.id.mostFreqAction: {
                getStatsIntent.putExtra("stat", 1);
                break;
            }
            case R.id.longestAction: {
                getStatsIntent.putExtra("stat", 2);
                break;
            }
            case R.id.longestChosenAction: {
                getStatsIntent.putExtra("stat", 3);
                break;
            }
            case R.id.pieDiag: {
                getStatsIntent.putExtra("stat", 4);
                break;
            }
        }
        CategoryDto[] categoryDtos = (CategoryDto[]) getIntent().getSerializableExtra("categories");

        if (isMonthFilter) {
            int month = monthSpinner.getSelectedItemPosition()+1;
            getStatsIntent.putExtra("mode", "month");
            getStatsIntent.putExtra("month", month);
        }
        else {
            getStatsIntent.putExtra("mode", "period");

            startDatePicker = (DatePicker)findViewById(R.id.fromDatePicker);
            endDatePicker = (DatePicker)findViewById(R.id.toDatePicker);
            Date startDate = getDateFromDatePicket(startDatePicker, startTimePicker);
            Date endDate = getDateFromDatePicket(endDatePicker, endTimePicker);

            getStatsIntent.putExtra("start", startDate.getTime());
            getStatsIntent.putExtra("end", endDate.getTime());

        }
        getStatsIntent.putExtra("categories", categoryDtos);

        startActivity(getStatsIntent);
    }


    View.OnClickListener rbListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            RadioButton rButton = (RadioButton) v;
            switch (rButton.getId()) {
                case R.id.radioMonth: {
                    monthLayout.setVisibility(View.VISIBLE);
                    periodLayout.setVisibility(View.GONE);
                    isMonthFilter = true;
                    break;
                }
                case R.id.radioPeriod: {
                    periodLayout.setVisibility(View.VISIBLE);
                    monthLayout.setVisibility(View.GONE);
                    isMonthFilter = false;
                    break;
                }
            }
        }
    };

    public static java.util.Date getDateFromDatePicket(DatePicker datePicker, TimePicker timePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();
        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);

        return calendar.getTime();
    }
}
