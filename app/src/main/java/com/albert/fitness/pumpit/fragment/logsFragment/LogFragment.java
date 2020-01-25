package com.albert.fitness.pumpit.fragment.logsFragment;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.albert.fitness.pumpit.helper.CalenderEvent;
import com.albert.fitness.pumpit.model.UserRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fitness.albert.com.pumpit.R;


public class LogFragment extends Fragment {

    public static String date;
    private final String TAG = "LogFragment";
    private CalenderEvent calenderEvent;

    public LogFragment() {

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_logs, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initCalendar(view);
        calenderOnClick();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    private void initCalendar(View view) {
        calenderEvent = view.findViewById(R.id.calender_event);

        if (date == null) {
            date = UserRegister.getTodayDate();
        }
    }

    private void calenderOnClick() {
        calenderEvent.initCalderItemClickCallback(dayContainerModel -> {
            String monthFromNum = monthAdded(dayContainerModel.getMonthNumber());
            if (dayContainerModel.getDay() < 10) {
                date = "0" + dayContainerModel.getDay() + "-" + monthFromNum + "-" + dayContainerModel.getYear();
            } else {
                date = dayContainerModel.getDay() + "-" + monthFromNum + "-" + dayContainerModel.getYear();
            }
            Log.d(TAG, "date: " + date);

            Objects.requireNonNull(getActivity()).startActivity(new Intent(getActivity(), LogTabActivity.class));
        });
        calenderEvent.setOnMonthChangedListener((calenderEvent, numMonth, month, year) -> {

        });
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
