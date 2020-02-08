package com.albert.fitness.pumpit.fragment.profile;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.albert.fitness.pumpit.utils.PrefsUtils;

import java.util.Calendar;

import fitness.albert.com.pumpit.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangeDateFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private TextView tvDate;
    private Button btnChangeDate;
    private PrefsUtils prefsUtils;

    public ChangeDateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_date, container, false);
        tvDate = view.findViewById(R.id.tv_change_date_of_birth);
        btnChangeDate = view.findViewById(R.id.btn_change_date);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showDate();
        btnChangeDate.setOnClickListener(v -> {
            showDatePickerDialog();
        });
    }

    private void showDate(){
        prefsUtils = new PrefsUtils(getActivity(), PrefsUtils.SETTINGS_PREFERENCES_FILE);
        String date = prefsUtils.getString("date_of_birth","");
        if(!date.isEmpty()){
            tvDate.setText(date);
        }
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                android.R.style.Theme_DeviceDefault_Light_Dialog,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = month + 1 + "/" + dayOfMonth + "/" + year;
        prefsUtils.saveData("date_of_birth", date);
        tvDate.setText(date);
    }
}
