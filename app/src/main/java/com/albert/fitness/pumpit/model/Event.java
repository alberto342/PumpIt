package com.albert.fitness.pumpit.model;

import android.annotation.SuppressLint;
import android.content.Context;

import com.albert.fitness.pumpit.utils.PrefsUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Event {
    public static final String EVENT_CALENDER = "EventCalender";
    private long time;
    private String eventText;
    private int eventColor;

    public Event() {
    }

    public Event(long time, String eventText) {
        this.time = time;
        this.eventText = eventText;
    }

    public Event(long time, String eventText, int eventColor) {
        this.time = time;
        this.eventText = eventText;
        this.eventColor = eventColor;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getEventText() {
        return eventText;
    }

    public void setEventText(String eventText) {
        this.eventText = eventText;
    }

    public int getEventColor() {
        return eventColor;
    }

    public void setEventColor(int eventColor) {
        this.eventColor = eventColor;
    }


    public static String getTodayData() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
        return df.format(c);
    }

    public static String getCurrentTime() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss", Locale.US);
        return df.format(c);
    }

    public static String getDayName() {
        Calendar cal = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat monthDate = new SimpleDateFormat("EEEE");
        return monthDate.format(cal.getTime());
    }

    public static String getWorkoutDayName() {
        Calendar calendar = Calendar.getInstance();
        String[] days = new String[]{"Workout 1", "Workout 2", "Workout 3", "Workout 4", "Workout 5", "Workout 6", "Workout 7"};
        return days[calendar.get(Calendar.DAY_OF_WEEK) - 1];
    }

    public String getMonthData(int numMonth, int year) {
        String date, digitMonth = null;
        int maxDay = maxDayInMonth(numMonth, year);
        String monthFromNum = monthAdded(numMonth);
        try {
            for (int i = 0; i <= maxDay; i++) {

                int newMonth = numMonth + 1;

                if (i < 10) {
                    date = "0" + i + "-" + monthFromNum + "-" + year;
                    digitMonth = "0" + i + "-" + newMonth + "-" + year;
                } else {
                    date = i + "-" + monthFromNum + "-" + year;
                    digitMonth = i + "-" + newMonth + "-" + year;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return digitMonth;
    }

    private static String receiveFullMonth() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMMM-yyyy", Locale.US);
        return df.format(c.getTime()).replace('-', ' ');
    }

    public static void saveEvent(Context context) {
        PrefsUtils prefsUtils = new PrefsUtils();
        prefsUtils.createSharedPreferencesFiles(context, Event.EVENT_CALENDER);
        prefsUtils.saveData(receiveFullMonth() + " TEXT", "●");
        prefsUtils.saveData(receiveFullMonth() + " COLOR", -16711936);
    }

    private int maxDayInMonth(int month, int year) {
        int date = 1;
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    private String monthAdded(int month) {
        List<String> monthName = new ArrayList<>();
        monthName.add("Jan");
        monthName.add("Feb");
        monthName.add("Mar");
        monthName.add("Apr");
        monthName.add("May");
        monthName.add("Jun");
        monthName.add("Jul");
        monthName.add("Aug");
        monthName.add("Sep");
        monthName.add("Oct");
        monthName.add("Nov");
        monthName.add("Dec");
        return monthName.get(month);
    }
}
