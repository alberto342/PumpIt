package fitness.albert.com.pumpit.fragment.logsFragment;


import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fitness.albert.com.pumpit.Model.UserRegister;
import fitness.albert.com.pumpit.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LogFragment extends Fragment {

    public static String date;
    private final String TAG = "LogFragment";


    public LogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_logs, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        assert activity != null;
        assert activity.getSupportActionBar() != null;
        activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorDarkBlue)));

        final CalendarView calendarView = view.findViewById(R.id.calendarView);

        if (date == null) {
            date = UserRegister.getTodayData();
        }

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

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

                String newMonth = monthName.get(month);
                if(dayOfMonth < 10) {
                    date = "0" + dayOfMonth + "-" + newMonth + "-" + year;
                } else {
                    date = dayOfMonth + "-" + newMonth + "-" + year;
                }
                Log.d(TAG, "MyDateSelect: " + date);

                Objects.requireNonNull(getActivity()).startActivity(new Intent(getActivity(), LogTabActivity.class));
            }
        });
    }
}
