package com.albert.fitness.pumpit.nutrition;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.albert.fitness.pumpit.adapter.BreakfastListAdapter;
import com.albert.fitness.pumpit.adapter.DinnerListAdapter;
import com.albert.fitness.pumpit.adapter.LunchListAdapter;
import com.albert.fitness.pumpit.adapter.SnacksListAdapter;
import com.albert.fitness.pumpit.model.Event;
import com.albert.fitness.pumpit.model.UserRegister;
import com.albert.fitness.pumpit.model.nutrition.Foods;
import com.albert.fitness.pumpit.model.nutrition.room.FoodLog;
import com.albert.fitness.pumpit.model.nutrition.room.QueryAltMeasures;
import com.albert.fitness.pumpit.model.nutrition.room.QueryNutritionItem;
import com.albert.fitness.pumpit.model.nutrition.room.SumNutritionPojo;
import com.albert.fitness.pumpit.utils.PrefsUtils;
import com.albert.fitness.pumpit.viewmodel.NutritionViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import fitness.albert.com.pumpit.R;

public class ShowAllNutritionActivity extends AppCompatActivity {

    private final String TAG = "ShowAllNutritionActivity";
    private RecyclerView rvListBreakfast, rvListLunch, rvListDinner, rvListSnacks;
    private List<Foods> foodListBreakfast = new ArrayList<>();
    private List<Foods> foodListLunch = new ArrayList<>();
    private List<Foods> foodListDinner = new ArrayList<>();
    private List<Foods> foodListSnacks = new ArrayList<>();
    private TextView tvTotalBreakfast, tvTotalLunch, tvTotalDinner, tvTotalSnacks;
    //    private ConstraintLayout constraintLayout;
    private LinearLayout breakfastContainer, lunchContainer, dinnerContainer, snacksContainer;
    public static String nutritionName;
    private RoundCornerProgressBar progressCarbs, progressProtien, progressFat;
    private UserRegister user = new UserRegister();
    private TextView tvCarbs, tvProtien, tvFat;
    private int calculationGoal;
    private NutritionViewModel viewModel;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_nutrition);
        setTitle("Nutrition");
        viewModel = ViewModelProviders.of(this).get(NutritionViewModel.class);

        init();
        datePicker();

        calAltMeasures(Event.getTodayData(), Foods.BREAKFAST, rvListBreakfast);
        calAltMeasures(Event.getTodayData(), Foods.DINNER, rvListDinner);
        calAltMeasures(Event.getTodayData(), Foods.LUNCH, rvListLunch);
        calAltMeasures(Event.getTodayData(), Foods.SNACK, rvListSnacks);
        getMeal(Event.getTodayData());
    }

    private void init() {
        rvListBreakfast = findViewById(R.id.rv_breakfast);
        rvListLunch = findViewById(R.id.rv_lunch);
        rvListDinner = findViewById(R.id.rv_dinner);
        rvListSnacks = findViewById(R.id.rv_snacks);
        tvTotalBreakfast = findViewById(R.id.tv_total_breakfast);
        tvTotalLunch = findViewById(R.id.tv_total_lunch);
        tvTotalDinner = findViewById(R.id.tv_total_dinner);
        tvTotalSnacks = findViewById(R.id.tv_total_snacks);
        //constraintLayout = findViewById(R.id.coordinator_layout);
        tvCarbs = findViewById(R.id.tv_carbs_all_nutrition);
        tvProtien = findViewById(R.id.tv_protein_all_nutrition);
        tvFat = findViewById(R.id.tv_fat_all_nutrition);
        progressCarbs = findViewById(R.id.pb_carbs);
        progressProtien = findViewById(R.id.pb_protein);
        progressFat = findViewById(R.id.pb_fat);
        breakfastContainer = findViewById(R.id.breakfast_container);
        lunchContainer = findViewById(R.id.lunch_container);
        dinnerContainer = findViewById(R.id.dinner_container);
        snacksContainer = findViewById(R.id.snacks_container);
    }


    private void datePicker() {
        /* starts before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        HorizontalCalendar calendarView = new HorizontalCalendar.Builder(this, R.id.calendar_view_custom)
                .range(startDate, endDate)
                .datesNumberOnScreen(7)
                .build();

        calendarView.setCalendarListener(new HorizontalCalendarListener() {
            @SuppressLint({"LongLogTag", "WrongConstant"})
            @Override
            public void onDateSelected(Calendar date, int position) {
                restData();

                ProgressDialog progressdialog = new ProgressDialog(ShowAllNutritionActivity.this);
                progressdialog.setMessage("Please Wait....");
                progressdialog.show();

                String newDate;
                int day = date.get(Calendar.DAY_OF_MONTH);
                int year = date.get(Calendar.YEAR);

                if (day < 10) {
                    newDate = "0" + day + "-" + monthAdded(date.get(Calendar.MONDAY)) + "-" + year;
                } else {
                    newDate = day + "-" + monthAdded(date.get(Calendar.MONDAY)) + "-" + year;
                }
                Log.d(TAG, "Date Selected: " + newDate);

                getMeal(newDate);

                calAltMeasures(newDate, Foods.BREAKFAST, rvListBreakfast);
                calAltMeasures(newDate, Foods.LUNCH, rvListLunch);
                calAltMeasures(newDate, Foods.DINNER, rvListDinner);
                calAltMeasures(newDate, Foods.SNACK, rvListSnacks);
                progressdialog.hide();
            }
        });
    }

    private void restData() {
        //kcal = fat = protein = carbs = 0;
        progressCarbs.setProgress(0);
        progressFat.setProgress(0);
        progressProtien.setProgress(0);

        foodListBreakfast.clear();
        foodListDinner.clear();
        foodListLunch.clear();
        foodListSnacks.clear();
        breakfastContainer.setVisibility(View.GONE);
        lunchContainer.setVisibility(View.GONE);
        dinnerContainer.setVisibility(View.GONE);
        snacksContainer.setVisibility(View.GONE);
    }

    private void calAltMeasures(final String date, final String type, final RecyclerView recycler) {
        viewModel.getAltMeasuresQtyAndWeight(date, type).observe(this, new Observer<List<QueryAltMeasures>>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onChanged(List<QueryAltMeasures> queryAltMeasures) {
                if (!queryAltMeasures.isEmpty()) {
                    for (QueryAltMeasures measures : queryAltMeasures) {
                        float calAltMeasure = 1;
                        if (measures.getMeasure().equals("g")) {
                            try {
                                calAltMeasure = measures.getServingWeight() * measures.getQty() / measures.getServingWeightGrams() / 100;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                calAltMeasure = measures.getServingWeight() * measures.getQty() / measures.getServingWeightGrams();
                                //cal =

                              //  calAltMeasure = measures.getServingWeight() * measures.getQty() / measures.getServingWeightGrams() * measures.getQty();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        Log.d(TAG, "calAltMeasure: " + calAltMeasure);
                        getNutrition(date, type, recycler, calAltMeasure);
                    }
                }
            }
        });
    }

    private void getMeal(String date) {
        viewModel.getSumOfNutritionByDate(date)
                .observe(this, new Observer<SumNutritionPojo>() {
                    @Override
                    public void onChanged(SumNutritionPojo nutrition) {
                        if (nutrition != null) {
                            PrefsUtils prefsUtils = new PrefsUtils(ShowAllNutritionActivity.this, PrefsUtils.SETTINGS_PREFERENCES_FILE);
                            String activityLevel = prefsUtils.getString(PrefsUtils.ACTIVITY_LEVEL, "");
                            calculationGoal = user.thermicEffect(activityLevel);

                            tvCarbs.setText(String.format(Locale.getDefault(), "%.2fg of %dg", nutrition.getCarb(), calculationGoal / 2));
                            tvProtien.setText(String.format(Locale.getDefault(), "%.2fg of %dg", nutrition.getProtein(), calculationGoal * 20 / 100));
                            tvFat.setText(String.format(Locale.getDefault(), "%.2fg of %dg", nutrition.getFat(), calculationGoal * 30 / 100));

                            updateProgressColor(progressCarbs, nutrition.getCarb(), calculationGoal / 2);
                            updateProgressColor(progressProtien, nutrition.getProtein(), calculationGoal * 20 / 100);
                            updateProgressColor(progressFat, nutrition.getFat(), calculationGoal * 30 / 100);
                        }
                    }
                });
    }

    private void getMealByType(String date, final String type, final TextView totalMeal) {
        viewModel.getSumOfNutritionByDateAndMealType(date, type)
                .observe(this, new Observer<SumNutritionPojo>() {
                    @Override
                    public void onChanged(SumNutritionPojo nutrition) {
                        if (nutrition != null) {
                            nutritionName = type;

                            totalMeal.setText(String.format(Locale.US, "Total: %.2f" + " Kcal.  " + "%.2f" +
                                            " Carbs.  " + "%.2f" + " Protein.  " + "%.2f" + " Fat.  ",
                                    nutrition.getCalories(), nutrition.getCarb(), nutrition.getProtein(), nutrition.getFat()));

                            updateProgressColor(progressCarbs, nutrition.getCarb(), calculationGoal / 2);
                            updateProgressColor(progressProtien, nutrition.getProtein(), calculationGoal * 20 / 100);
                            updateProgressColor(progressFat, nutrition.getFat(), calculationGoal * 30 / 100);
                        }
                    }
                });
    }

    @SuppressLint("LongLogTag")
    private void getNutrition(final String date, final String type, final RecyclerView recyclerView, final float calAltMeasure) {
        final float newCalAltMeasure = calAltMeasure == 0.0 ? 1 : calAltMeasure;
        Log.d(TAG, "getNutrition: " + newCalAltMeasure);

        viewModel.getNutritionItem(date, type)
                .observe(this, new Observer<List<QueryNutritionItem>>() {
                    @Override
                    public void onChanged(List<QueryNutritionItem> queryNutritionItems) {
                        if (!queryNutritionItems.isEmpty()) {
                            visibleNutrition(type);

                            for (int i = 0; i < queryNutritionItems.size(); i++) {
                                float calories = queryNutritionItems.get(i).getCalories();
                                float protein = queryNutritionItems.get(i).getProtein();
                                float carbohydrate = queryNutritionItems.get(i).getTotalCarbohydrate();

                                Log.d(TAG, "onChanged: cal " + calories + " pro " + protein);

                                queryNutritionItems.get(i).setCalories(calories * newCalAltMeasure);
                                queryNutritionItems.get(i).setProtein(protein * newCalAltMeasure);
                                queryNutritionItems.get(i).setTotalCarbohydrate(carbohydrate * newCalAltMeasure);
                            }
                            initRecyclerView(type, recyclerView, queryNutritionItems);

                            switch (type) {
                                case Foods.BREAKFAST:
                                    getMealByType(date, type, tvTotalBreakfast);
                                    break;
                                case Foods.DINNER:
                                    getMealByType(date, type, tvTotalDinner);
                                    break;
                                case Foods.LUNCH:
                                    getMealByType(date, type, tvTotalLunch);
                                    break;
                                case Foods.SNACK:
                                    getMealByType(date, type, tvTotalSnacks);
                            }
                            for (QueryNutritionItem i : queryNutritionItems) {
                                Log.d(TAG, "get Nutrition name for adapter: " + i.getFoodName());
                            }
                        }
                    }
                });
    }


    private void visibleNutrition(String nutritionType) {
        switch (nutritionType) {
            case Foods.BREAKFAST:
                breakfastContainer.setVisibility(View.VISIBLE);
                break;
            case Foods.LUNCH:
                lunchContainer.setVisibility(View.VISIBLE);
                break;
            case Foods.DINNER:
                dinnerContainer.setVisibility(View.VISIBLE);
                break;
            case Foods.SNACK:
                snacksContainer.setVisibility(View.VISIBLE);
        }
    }

    private void initRecyclerView(final String nutritionType, RecyclerView recyclerView, final List<QueryNutritionItem> nutritionItems) {
        @SuppressLint("WrongConstant")
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        switch (nutritionType) {
            case Foods.BREAKFAST:
                BreakfastListAdapter breakfastAdapter = new BreakfastListAdapter(this, nutritionItems);
                recyclerView.setAdapter(breakfastAdapter);
                breakfastAdapter.notifyDataSetChanged();
                break;
            case Foods.LUNCH:
                LunchListAdapter lunchAdapter = new LunchListAdapter(this, nutritionItems);
                recyclerView.setAdapter(lunchAdapter);
                lunchAdapter.notifyDataSetChanged();
                break;
            case Foods.DINNER:
                DinnerListAdapter dinnerAdapter = new DinnerListAdapter(this, nutritionItems);
                recyclerView.setAdapter(dinnerAdapter);
                dinnerAdapter.notifyDataSetChanged();
                break;
            case Foods.SNACK:
                SnacksListAdapter snacksAdapter = new SnacksListAdapter(this, nutritionItems);
                recyclerView.setAdapter(snacksAdapter);
                snacksAdapter.notifyDataSetChanged();
        }
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();
                final int logId = nutritionItems.get(position).getLogId();
                Log.d(TAG, "onSwiped: " + logId);

                viewModel.getFoodLogByLogId(logId)
                        .observe(ShowAllNutritionActivity.this, new Observer<FoodLog>() {
                            @Override
                            public void onChanged(FoodLog foodLog) {
                                if (foodLog != null) {
                                    viewModel.deleteFoodLog(foodLog);
                                    Log.d(TAG, "onSwiped: Deleted successfully lodId" + logId);
                                }
                            }
                        });
            }
        }).attachToRecyclerView(recyclerView);
    }

    private void updateProgressColor(RoundCornerProgressBar roundCornerProgressBar, float nutrition, int calculationGoal) {
        float fractionToPercent = nutrition / calculationGoal * 100;
        roundCornerProgressBar.setProgress(fractionToPercent);
        float progress = roundCornerProgressBar.getProgress();
        if (progress <= 50) {
            roundCornerProgressBar.setProgressColor(getResources().getColor(R.color.yellow_dolly));
        } else if (progress > 75 && progress <= 100) {
            roundCornerProgressBar.setProgressColor(getResources().getColor(R.color.md_green_600));
        } else if (progress > 100) {
            roundCornerProgressBar.setProgressColor(getResources().getColor(R.color.md_red_500));
        }
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

    //Without this method Data will be double
    public void onPause() {
        super.onPause();
        this.foodListBreakfast.clear();
        this.foodListDinner.clear();
        this.foodListLunch.clear();
        this.foodListSnacks.clear();
    }
}
