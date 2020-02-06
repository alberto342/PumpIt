package com.albert.fitness.pumpit.listener;

import com.albert.fitness.pumpit.helper.CalenderEvent;

public interface OnMonthChangedListener {
    void onMonthChanged(CalenderEvent calenderEvent, int numMonth, String month, int year);

}
