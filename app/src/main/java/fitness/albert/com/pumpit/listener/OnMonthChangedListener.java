package fitness.albert.com.pumpit.listener;

import fitness.albert.com.pumpit.helper.CalenderEvent;

public interface OnMonthChangedListener {
    void onMonthChanged(CalenderEvent calenderEvent, int numMonth, String month, int year);

}
