package com.albert.fitness.pumpit.fragment;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.albert.fitness.pumpit.model.Event;
import com.albert.fitness.pumpit.model.UserRegister;
import com.albert.fitness.pumpit.model.nutrition.Foods;
import com.albert.fitness.pumpit.model.nutrition.room.SumNutritionPojo;
import com.albert.fitness.pumpit.model.nutrition.room.WaterTracker;
import com.albert.fitness.pumpit.nutrition.SearchFoodsActivity;
import com.albert.fitness.pumpit.nutrition.ShowAllNutritionActivity;
import com.albert.fitness.pumpit.utils.PrefsUtils;
import com.albert.fitness.pumpit.viewmodel.NutritionViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import fitness.albert.com.pumpit.R;


public class NutritionFragment extends Fragment {

    private final String TAG = "NutritionFragment";
    private TextView tvGoal, tvFood, tvExercise, tvRemaining, tvFat, tvProtien, tvCarbs;
    private List<Foods> foodList = new ArrayList<>();
    private PrefsUtils prefsUtils = new PrefsUtils();
    private UserRegister user = new UserRegister();
    private float kcal, fat, protein, carbs, waterMl;
    private int calculationGoal;
    private RoundCornerProgressBar progressCarbs, progressProtien, progressFat;
    private int ly1ElementCount = 0, ly2ElementCount = 0, ly3ElementCount = 0, ly4ElementCount = 0;
    private NutritionViewModel viewModel;


    public NutritionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nutrition, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(NutritionViewModel.class);
        init(view);
        getCaloriesBurned();

        getMeal(Event.getTodayData());
        getUserDataAndSetGoal();
        emailIsOnNutrition();
        datePicker(view);
        setViewDrink(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }


    private void init(View view) {
        //add food btn
        ImageView btnAddBreakfast = view.findViewById(R.id.btn_add_breakfast);
        ImageView btnAddDinner = view.findViewById(R.id.btn_add_dinner);
        ImageView btnAddLunch = view.findViewById(R.id.btn_add_lunch);
        ImageView btnAddSnacks = view.findViewById(R.id.btn_add_snacks);
        TextView tvDetails = view.findViewById(R.id.tv_details);

        tvGoal = view.findViewById(R.id.tv_goal);
        tvExercise = view.findViewById(R.id.tv_exercise);
        tvFood = view.findViewById(R.id.tv_food);
        tvRemaining = view.findViewById(R.id.tv_remaining);
        tvFat = view.findViewById(R.id.tv_fat);
        tvCarbs = view.findViewById(R.id.tv_carbs);
        tvProtien = view.findViewById(R.id.tv_protein);
        progressCarbs = view.findViewById(R.id.pb_carbs);
        progressProtien = view.findViewById(R.id.pb_protein);
        progressFat = view.findViewById(R.id.pb_fat);

        //disable RecyclerView scrolling
        //rvListFood.setNestedScrollingEnabled(false);
        btnAddBreakfast.setOnClickListener(onClickListener);
        btnAddSnacks.setOnClickListener(onClickListener);
        btnAddLunch.setOnClickListener(onClickListener);
        btnAddDinner.setOnClickListener(onClickListener);
        tvDetails.setOnClickListener(onClickListener);
    }

    private void datePicker(View view) {
        /* starts before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        HorizontalCalendar calendarView = new HorizontalCalendar.Builder(view, R.id.calendar_view_custom)
                .range(startDate, endDate)
                .datesNumberOnScreen(7)
                .build();

        calendarView.setCalendarListener(new HorizontalCalendarListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onDateSelected(Calendar date, int position) {
                ProgressDialog progressdialog = new ProgressDialog(getActivity());
                progressdialog.setMessage("Please Wait....");
                progressdialog.show();

                restData();

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
                progressdialog.hide();
            }
        });
    }

    private void restData() {
        kcal = fat = protein = carbs = 0;
        tvFood.setText("0");
        tvRemaining.setText("0");
        tvCarbs.setText("0");
        tvProtien.setText("0");
        tvFat.setText("0");
        getUserDataAndSetGoal();
        //  progressCarbs.setProgress(0);
        //progressFat.setProgress(0);
        //progressProtien.setProgress(0);
    }


    //Check meal selected
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            prefsUtils.createSharedPreferencesFiles(getActivity(), Foods.SHARED_PREFERENCES_FILE);

            switch (v.getId()) {
                case R.id.tv_details:
                    startActivity(new Intent(getActivity(), ShowAllNutritionActivity.class));
                    break;
                case R.id.btn_add_dinner:
                    saveMealToSP(true, false, false, false);
                    break;
                case R.id.btn_add_breakfast:
                    saveMealToSP(false, true, false, false);
                    break;
                case R.id.btn_add_lunch:
                    saveMealToSP(false, false, true, false);
                    break;
                case R.id.btn_add_snacks:
                    saveMealToSP(false, false, false, true);
            }
        }
    };

    //The first time the email not exist, only after add food is exists
    private void emailIsOnNutrition() {
//        DocumentReference docRef = db.collection(Foods.NUTRITION).document(FireBaseInit.getEmailRegister());
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    assert document != null;
//                    if ((document.exists())) {
//                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
//                        //   isOnNutrition = true;
//                    } else {
//                        Log.d(TAG, "No such document");
//                        // isOnNutrition = false;
//                    }
//                } else {
//                    Log.d(TAG, "get failed with ", task.getException());
//                }
//            }
//        });
    }

    private void saveMealToSP(boolean dinner, boolean breakfast, boolean lunch, boolean snack) {
        prefsUtils.saveData("dinner", dinner);
        prefsUtils.saveData("breakfast", breakfast);
        prefsUtils.saveData("lunch", lunch);
        prefsUtils.saveData("snack", snack);

        startActivity(new Intent(getActivity(), SearchFoodsActivity.class));
        Objects.requireNonNull(getActivity()).getFragmentManager().popBackStack();
    }

    private void getCaloriesBurned() {
        final int[] caloriesBurned = {0};
//        db.collection(FinishTraining.TRAINING_LOG).document(FireBaseInit.getEmailRegister())
//                .collection(UserRegister.getTodayDate()).get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if(task.isSuccessful() && task.getResult() != null) {
//
//                            for(int i=0; i<task.getResult().size(); i++) {
//                                FinishTraining finishTraining = task.getResult().getDocuments().get(i).toObject(FinishTraining.class);
//                                caloriesBurned[0] += finishTraining.getCaloriesBurned();
//                            }
//                            tvExercise.setText(String.valueOf(caloriesBurned[0]));
//                            Log.d(TAG, "Calories Burned: " + caloriesBurned[0]);
//                        }
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.w(TAG, "onFailure: " + e);
//            }
//        });
    }


    private void getMeal(String date) {
        viewModel.getSumOfNutritionByDate(date).observe(this, new Observer<SumNutritionPojo>() {
            @Override
            public void onChanged(SumNutritionPojo nutrition) {
                PrefsUtils prefsUtils = new PrefsUtils(getActivity(), PrefsUtils.SETTINGS_PREFERENCES_FILE);
                String activityLevel = prefsUtils.getString(PrefsUtils.ACTIVITY_LEVEL, "");
                calculationGoal = user.thermicEffect(activityLevel);

                Log.d(TAG, "calculationGoal: " + calculationGoal);

                tvFood.setText(String.format(Locale.getDefault(), "%.0f", nutrition.getCalories()));
                tvCarbs.setText(String.format(Locale.getDefault(), "%.0fg", nutrition.getCarb()));
                tvProtien.setText(String.format(Locale.getDefault(), "%.0fg", nutrition.getProtein()));
                tvFat.setText(String.format(Locale.getDefault(), "%.0fg", nutrition.getFat()));

                tvFood.setText(String.format(Locale.getDefault(), "%.0f", nutrition.getCalories()));
                tvCarbs.setText(String.format(Locale.getDefault(), "%.0fg of %dg", nutrition.getCarb(), calculationGoal / 2));
                tvProtien.setText(String.format(Locale.getDefault(), "%.0fg of %dg", nutrition.getProtein(), calculationGoal * 20 / 100));
                tvFat.setText(String.format(Locale.getDefault(), "%.0fg of %dg", nutrition.getFat(), calculationGoal * 30 / 100));
                tvRemaining.setText(String.format(Locale.getDefault(), "%.0f", calculationGoal - kcal + Integer.parseInt(tvExercise.getText().toString())));

                updateProgressColor(progressCarbs, carbs, calculationGoal / 2);
                updateProgressColor(progressProtien, protein, calculationGoal * 20 / 100);
                updateProgressColor(progressFat, fat, calculationGoal * 30 / 100);
            }
        });
    }


    private void getUserDataAndSetGoal() {
//        final ProgressDialog progressdialog = new ProgressDialog(getActivity());
//        progressdialog.setMessage("Please Wait....");
//        progressdialog.show();
//
//        db.collection(UserRegister.FIRE_BASE_USERS).document(FireBaseInit.getEmailRegister()).get()
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        user = documentSnapshot.toObject(UserRegister.class);
//
//                        assert user != null;
//                        calculationGoal = user.thermicEffect(user.getActivityLevel());
//
//                        tvGoal.setText(String.valueOf(calculationGoal));
//                        tvCarbs.setText(String.format(Locale.getDefault(), "%.0fg of %dg", carbs, calculationGoal / 2));
//                        tvProtien.setText(String.format(Locale.getDefault(), "%.0fg of %dg", protein, calculationGoal * 20 / 100));
//                        tvFat.setText(String.format(Locale.getDefault(), "%.0fg of %dg", fat, calculationGoal * 30 / 100));
//
//                        updateProgressColor(progressCarbs, carbs, calculationGoal / 2);
//                        updateProgressColor(progressProtien, protein, calculationGoal * 20 / 100);
//                        updateProgressColor(progressFat, fat, calculationGoal * 30 / 100);
//
//                        progressdialog.hide();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        e.printStackTrace();
//                    }
//                });
    }

    private void updateProgressColor(RoundCornerProgressBar roundCornerProgressBar, float nutrition, int calculationGoal) {
        float fractionToPercent = nutrition / calculationGoal * 100;
        try {
            roundCornerProgressBar.setProgress(fractionToPercent);
            float progress = roundCornerProgressBar.getProgress();
            if (progress <= 50) {
                roundCornerProgressBar.setProgressColor(getResources().getColor(R.color.yellow_dolly));
            } else if (progress > 75 && progress <= 100) {
                roundCornerProgressBar.setProgressColor(getResources().getColor(R.color.md_green_600));
            } else if (progress > 100) {
                roundCornerProgressBar.setProgressColor(getResources().getColor(R.color.md_red_500));
            }
        } catch (Exception e) {
            Log.i(TAG, "updateProgressColor: " + e.getMessage());
        }
    }


    private void setViewDrink(View rowView) {
        final TextView tvWaterMl = rowView.findViewById(R.id.tv_water_ml);
        final TableRow tableLayout1 = rowView.findViewById(R.id.tab_layout_1);
        final TableRow tableLayout2 = rowView.findViewById(R.id.tab_layout_2);
        final TableRow tableLayout3 = rowView.findViewById(R.id.tab_layout_3);
        final TableRow tableLayout4 = rowView.findViewById(R.id.tab_layout_4);
        final ImageView addWater = rowView.findViewById(R.id.iv_water_icon);
        final ImageView addWater2 = rowView.findViewById(R.id.iv_water_icon_2);
        final ImageView addWater3 = rowView.findViewById(R.id.iv_water_icon_3);
        final ImageView addWater4 = rowView.findViewById(R.id.iv_water_icon_4);
        tvWaterMl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                waterTrackerSaved(tvWaterMl.getText().toString());
            }
        });

        final boolean[] isRemovable1 = {true};
        final boolean[] isRemovable2 = {true};
        final boolean[] isRemovable3 = {true};

        addWater.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                ImageView ivFullWater = new ImageView(getActivity());
                ivFullWater.setImageResource(R.mipmap.full_water);

                Log.d(TAG, "onClick: add water " + ly1ElementCount);

                if (ly1ElementCount < 6) {
                    tableLayout1.addView(ivFullWater);
                    ly1ElementCount++;
                    waterMl += 0.25f;
                }

                if (ly1ElementCount == 6) {
                    addWater.setImageResource(R.mipmap.full_water);
                    addWater2.setVisibility(View.VISIBLE);
                    isRemovable1[0] = false;
                }

                ivFullWater.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View view) {
                        Log.d(TAG, "onClick: remove water " + ly1ElementCount);

                        if (ly1ElementCount >= 1 && ly1ElementCount <= 6 && isRemovable1[0]) {
                            tableLayout1.removeViewAt(1);
                            ly1ElementCount--;
                            waterMl -= 0.25f;
                        }
                        tvWaterMl.setText(waterMl + " L");
                    }
                });
                tvWaterMl.setText(waterMl + " L");
            }
        });
        addWater2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: add water 2: " + ly2ElementCount);

                ImageView ivFullWater = new ImageView(getActivity());
                ivFullWater.setImageResource(R.mipmap.full_water);

                if (ly2ElementCount < 6) {
                    tableLayout2.addView(ivFullWater);
                    ly2ElementCount++;
                    waterMl += 0.25f;
                    isRemovable1[0] = false;
                }

                if (ly2ElementCount == 6) {
                    addWater2.setImageResource(R.mipmap.full_water);
                    addWater3.setVisibility(View.VISIBLE);
                    isRemovable2[0] = false;
                }

                ivFullWater.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View view) {
                        Log.d(TAG, "onClick: remove water 2: " + ly2ElementCount);

                        if (ly2ElementCount >= 1 && ly2ElementCount <= 6 && isRemovable2[0]) {
                            tableLayout2.removeViewAt(1);
                            ly2ElementCount--;
                            waterMl -= 0.25f;
                        }
                        if (ly2ElementCount == 0) {
                            isRemovable1[0] = true;
                            addWater.setVisibility(View.VISIBLE);
                            addWater.setImageResource(R.mipmap.add_water_icon);
                            addWater2.setVisibility(View.GONE);
                        }
                        tvWaterMl.setText(waterMl + " L");
                    }
                });
                tvWaterMl.setText(waterMl + " L");
            }
        });
        addWater3.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {

                Log.d(TAG, "onClick: add water 3: " + ly3ElementCount);

                ImageView ivFullWater = new ImageView(getActivity());
                ivFullWater.setImageResource(R.mipmap.full_water);

                if (ly3ElementCount < 6) {
                    tableLayout3.addView(ivFullWater);
                    ly3ElementCount++;
                    isRemovable2[0] = false;
                    waterMl += 0.25f;
                }

                if (ly3ElementCount == 6) {
                    addWater3.setImageResource(R.mipmap.full_water);
                    addWater4.setVisibility(View.VISIBLE);
                    isRemovable3[0] = false;
                }
                ivFullWater.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View view) {
                        Log.d(TAG, "onClick: remove water 3: " + ly3ElementCount);
                        if (ly3ElementCount >= 1 && ly3ElementCount <= 6 && isRemovable3[0]) {
                            tableLayout3.removeViewAt(1);
                            ly3ElementCount--;
                            waterMl -= 0.25f;
                        }
                        if (ly3ElementCount == 0) {
                            isRemovable2[0] = true;
                            addWater2.setVisibility(View.VISIBLE);
                            addWater2.setImageResource(R.mipmap.add_water_icon);
                            addWater3.setVisibility(View.GONE);
                        }
                        tvWaterMl.setText(waterMl + " L");
                    }
                });
                tvWaterMl.setText(waterMl + " L");
            }
        });
        addWater4.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: add water 4: " + ly4ElementCount);

                ImageView ivFullWater = new ImageView(getActivity());
                ivFullWater.setImageResource(R.mipmap.full_water);

                if (ly4ElementCount < 6) {
                    tableLayout4.addView(ivFullWater);
                    ly4ElementCount++;
                    isRemovable3[0] = false;
                    waterMl += 0.25f;
                }
                ivFullWater.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View view) {
                        Log.d(TAG, "onClick: remove water 4: " + ly4ElementCount);
                        if (ly4ElementCount >= 1 && ly4ElementCount <= 6) {
                            tableLayout4.removeViewAt(1);
                            ly4ElementCount--;
                            waterMl -= 0.25f;
                        }
                        if (ly4ElementCount == 0) {
                            isRemovable3[0] = true;
                            addWater3.setVisibility(View.VISIBLE);
                            addWater3.setImageResource(R.mipmap.add_water_icon);
                            addWater4.setVisibility(View.GONE);
                        }
                        tvWaterMl.setText(waterMl + " L");
                    }
                });
                tvWaterMl.setText(waterMl + " L");
            }
        });
    }

    private void waterTrackerSaved(String waterTracker) {
        viewModel.addNewWaterTracker(new WaterTracker(Event.getTodayData(), waterTracker));
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

