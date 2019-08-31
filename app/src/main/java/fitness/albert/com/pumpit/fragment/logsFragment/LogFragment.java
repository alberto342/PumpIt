package fitness.albert.com.pumpit.fragment.logsFragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import fitness.albert.com.pumpit.R;
import fitness.albert.com.pumpit.helper.CalenderEvent;
import fitness.albert.com.pumpit.listener.CalenderDayClickListener;
import fitness.albert.com.pumpit.listener.OnMonthChangedListener;
import fitness.albert.com.pumpit.model.DayContainerModel;
import fitness.albert.com.pumpit.model.Event;
import fitness.albert.com.pumpit.model.FinishTraining;
import fitness.albert.com.pumpit.model.FireBaseInit;
import fitness.albert.com.pumpit.model.Foods;
import fitness.albert.com.pumpit.model.UserRegister;

/**
 * A simple {@link Fragment} subclass.
 */
public class LogFragment extends Fragment {

    public static String date;
    private final String TAG = "LogFragment";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CalenderEvent calenderEvent;

    public LogFragment() {
        // Required empty public constructor
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

        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        getMonthData(month, year);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

    private void initCalendar(View view) {
        calenderEvent = view.findViewById(R.id.calender_event);

        if (date == null) {
            date = UserRegister.getTodayDate();
        }
    }

    private void calenderOnClick() {
        calenderEvent.initCalderItemClickCallback(new CalenderDayClickListener() {
            @Override
            public void onGetDay(DayContainerModel dayContainerModel) {

                String monthFromNum = monthAdded(dayContainerModel.getMonthNumber());

                if (dayContainerModel.getDay() < 10) {
                    date = "0" + dayContainerModel.getDay() + "-" + monthFromNum + "-" + dayContainerModel.getYear();
                } else {
                    date = dayContainerModel.getDay() + "-" + monthFromNum + "-" + dayContainerModel.getYear();
                }
                Log.d(TAG, "date: " + date);
                Objects.requireNonNull(getActivity()).startActivity(new Intent(getActivity(), LogTabActivity.class));
            }
        });
        calenderEvent.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(CalenderEvent calenderEvent, int numMonth, String month, int year) {
                getMonthData(numMonth, year);
            }
        });
    }

    private void getMonthData(int numMonth, int year) {
        String date, digitMonth;
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
                getNutritionFromFb(date, digitMonth, Foods.BREAKFAST);
                getNutritionFromFb(date, digitMonth, Foods.LUNCH);
                getNutritionFromFb(date, digitMonth, Foods.DINNER);
                getNutritionFromFb(date, digitMonth, Foods.SNACK);

                getFitnessLogFromFb(date, digitMonth);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int maxDayInMonth(int month, int year) {
        int date = 1;
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    private void getNutritionFromFb(final String date, final String digitMonth, final String nutritionType) {

        Thread thread = new Thread(new Runnable() {
            public void run() {
                db.collection(Foods.NUTRITION).document(FireBaseInit.getEmailRegister()).collection(nutritionType)
                        .document(date).collection(Foods.All_NUTRITION).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful() && Objects.requireNonNull(task.getResult()).getDocuments().size() > 0) {

                                    Log.d(TAG, "have nutrition in this data: " + date);

                                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

                                    try {
                                        Date mDate = sdf.parse(digitMonth);
                                        long timeInMilliseconds = mDate.getTime();

                                        calenderEvent.addEvent(new Event(timeInMilliseconds, "\uD83C\uDF4F", Color.GREEN));
                                        Log.d(TAG, "Success added date " + digitMonth);

                                    } catch (ParseException e) {
                                        Log.i(TAG, "error: " + e);
                                    }
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.i(TAG, "Filed receive data " + e);
                            }
                        });
            }
        });
        thread.start();
    }

    private void getFitnessLogFromFb(final String date, final String digitMonth) {

        Thread thread = new Thread(new Runnable() {
            public void run() {
                db.collection(FinishTraining.TRAINING_LOG).document(FireBaseInit.getEmailRegister())
                        .collection(date).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful() && Objects.requireNonNull(task.getResult()).getDocuments().size() > 0) {
                                    Log.d(TAG, "have fitness in this data: " + date);

                                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

                                    try {
                                        Date mDate = sdf.parse(digitMonth);
                                        long timeInMilliseconds = mDate.getTime();

                                        calenderEvent.addEvent(new Event(timeInMilliseconds, "\uD83D\uDCAA", Color.GREEN));
                                        Log.d(TAG, "Success added date " + digitMonth);

                                    } catch (ParseException e) {
                                        Log.i(TAG, "error: " + e);
                                    }
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.i(TAG, "Filed receive data " + e);
                            }
                        });
            }
        });
        thread.start();
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
