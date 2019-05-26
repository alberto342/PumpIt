package fitness.albert.com.pumpit.fragment.logsFragment;


import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import fitness.albert.com.pumpit.Model.FinishTraining;
import fitness.albert.com.pumpit.Model.FireBaseInit;
import fitness.albert.com.pumpit.Model.Foods;
import fitness.albert.com.pumpit.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LogFragment extends Fragment {

    TabLayout tabs;
    ViewPager viewPager;

    private final String TAG = "LogFragment";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

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

        tabs = view.findViewById(R.id.tablayout_logs);
        viewPager = view.findViewById(R.id.vp_logs);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        assert activity != null;
        assert activity.getSupportActionBar() != null;
        activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorDarkBlue)));

        CalendarView calendarView = view.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                List<String> monthName = new ArrayList<>();
                monthName.add("January");
                monthName.add("February");
                monthName.add("March");
                monthName.add("April");
                monthName.add("May");
                monthName.add("June");
                monthName.add("July");
                monthName.add("August");
                monthName.add("September");
                monthName.add("October");
                monthName.add("November");
                monthName.add("December");

                String newMonth = monthName.get(month);
                String date = dayOfMonth + "-" + newMonth + "-" + year;
                Log.d(TAG, "MyDateSelect: " + date);

                getNutritionFromFb(date, Foods.BREAKFAST);
                getNutritionFromFb(date, Foods.DINNER);
                getNutritionFromFb(date, Foods.LUNCH);
                getNutritionFromFb(date, Foods.SNACK);
            }
        });
    }

    private void getNutritionFromFb(String date, String nutritionType) {
        final List<Foods> foodsList = new ArrayList<>();
        db.collection(Foods.NUTRITION).document(FireBaseInit.getEmailRegister()).collection(nutritionType)
                .document(date).collection(Foods.FRUIT).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    for (int i = 0; i < task.getResult().size(); i++) {
                        Foods foods = task.getResult().getDocuments().get(i).toObject(Foods.class);
                        foodsList.add(foods);
                    }
                }
                Log.d(TAG, "Successfully receive data from fb");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "Filed receive data " + e);
            }
        });
    }

    private void getWorkoutFromFb(String date) {

        final List<FinishTraining> finishTrainingList = new ArrayList<>();

        db.collection(FinishTraining.TRAINING_LOG).document(FireBaseInit.getEmailRegister())
                .collection(date).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful() && task.getResult() != null) {
                    for (int i = 0; i < task.getResult().size(); i++) {
                        FinishTraining finishTraining = task.getResult().getDocuments().get(i).toObject(FinishTraining.class);
                        finishTrainingList.add(finishTraining);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "field get data " + e);
            }
        });
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupViewPager(viewPager);
        tabs.setupWithViewPager(viewPager);

        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(new LogNutritionFragment(), "Nutrition Log");
        viewPagerAdapter.addFragment(new LogWorkoutFragment(), "Workout Log");
        viewPager.setAdapter(viewPagerAdapter);
    }


    private class ViewPagerAdapter extends FragmentPagerAdapter {

        List<Fragment> fragmentList = new ArrayList<>();
        List<String> fragmentTitles = new ArrayList<>();

        ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitles.get(position);
        }

        void addFragment(Fragment fragment, String name) {
            fragmentList.add(fragment);
            fragmentTitles.add(name);
        }
    }

}
