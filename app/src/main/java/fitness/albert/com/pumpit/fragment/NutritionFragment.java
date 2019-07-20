package fitness.albert.com.pumpit.fragment;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import androidx.fragment.app.FragmentActivity;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import fitness.albert.com.pumpit.model.FireBaseInit;
import fitness.albert.com.pumpit.model.Foods;
import fitness.albert.com.pumpit.model.PrefsUtils;
import fitness.albert.com.pumpit.model.UserRegister;
import fitness.albert.com.pumpit.R;
import fitness.albert.com.pumpit.nutrition.SearchFoodsActivity;
import fitness.albert.com.pumpit.nutrition.ShowAllNutritionActivity;


public class NutritionFragment extends Fragment {

    private final String TAG = "NutritionFragment";
    private TextView tvGoal, tvFood, tvExersice, tvRemaining, tvFat, tvProtien, tvCarbs;
    private List<Foods> foodList = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private PrefsUtils prefsUtils = new PrefsUtils();
    private UserRegister user = new UserRegister();
    private float kcal, fat, protein, carbs, waterMl;
    private int calculationGoal, waterCount = 0, waterCount2 = 0, waterCount3 = 0, waterCount4 = 0;
    private FragmentActivity myContext;
    private RoundCornerProgressBar progressCarbs, progressProtien, progressFat;
    // final Calendar myCalendar = Calendar.getInstance();


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
        init(view);
        mealFromFs();
        getUserDataAndSetGoal();
        emailIsOnNutrition();
        datePicker(view);
        setViewDrink(view);
        // setToolBar(view);
    }

    private void setToolBar(View view) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        assert activity != null;
        assert activity.getSupportActionBar() != null;
        activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorDarkBlue)));
        activity.getSupportActionBar().setTitle(UserRegister.getTodayData());
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    private void init(View view) {
        //add food btn
        ImageView btnAddBreakfast = view.findViewById(R.id.btn_add_breakfast);
        ImageView btnAddDinner = view.findViewById(R.id.btn_add_dinner);
        ImageView btnAddLunch = view.findViewById(R.id.btn_add_lunch);
        ImageView btnAddSnacks = view.findViewById(R.id.btn_add_snacks);
        TextView tvDetails = view.findViewById(R.id.tv_details);


        tvGoal = view.findViewById(R.id.tv_goal);
        tvExersice = view.findViewById(R.id.tv_exercise);
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

//    private void datePickerDialog() {
//
//        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
//
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear,
//                                  int dayOfMonth) {
//                myCalendar.set(Calendar.YEAR, year);
//                myCalendar.set(Calendar.MONTH, monthOfYear);
//                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                updateLabel();
//            }
//
//        };
//        toolbarTxt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new DatePickerDialog(getActivity(), date, myCalendar
//                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
//            }
//        });
//    }
//
//    private void updateLabel() {
//        String myFormat = "MM/dd/yy"; //In which you need put here
//        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
//
//        toolbarTxt.setText(sdf.format(myCalendar.getTime()));
//    }


    private void mealFromFs() {
        //   if (isOnNutrition) {
        ProgressDialog progressdialog = new ProgressDialog(getActivity());
        progressdialog.setMessage("Please Wait....");
        progressdialog.show();

        getMealFromFs(Foods.BREAKFAST, UserRegister.getTodayData());
        getMealFromFs(Foods.SNACK, UserRegister.getTodayData());
        getMealFromFs(Foods.LUNCH, UserRegister.getTodayData());
        getMealFromFs(Foods.DINNER, UserRegister.getTodayData());
        progressdialog.hide();
        //   }
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

                getMealFromFs(Foods.BREAKFAST, newDate);
                getMealFromFs(Foods.SNACK, newDate);
                getMealFromFs(Foods.LUNCH, newDate);
                getMealFromFs(Foods.DINNER, newDate);
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
        DocumentReference docRef = db.collection(Foods.NUTRITION).document(FireBaseInit.getEmailRegister());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    assert document != null;
                    if ((document.exists())) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        //   isOnNutrition = true;
                    } else {
                        Log.d(TAG, "No such document");
                        // isOnNutrition = false;
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void saveMealToSP(boolean dinner, boolean breakfast, boolean lunch, boolean snack) {
        prefsUtils.saveData("dinner", dinner);
        prefsUtils.saveData("breakfast", breakfast);
        prefsUtils.saveData("lunch", lunch);
        prefsUtils.saveData("Snack", snack);

        startActivity(new Intent(getActivity(), SearchFoodsActivity.class));
        Objects.requireNonNull(getActivity()).getFragmentManager().popBackStack();
    }


    private void getMealFromFs(String keyValue, String date) {
        //get NUTRITION from firestone

        db.collection(Foods.NUTRITION).document(FireBaseInit.getEmailRegister())
                .collection(keyValue).document(date)
                .collection(Foods.All_NUTRITION).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful() && task.getResult() != null) {
                            for (int i = 0; i < task.getResult().getDocuments().size(); i++) {
                                Foods foods = task.getResult().getDocuments().get(i).toObject(Foods.class);
                                foodList.add(foods);

                                Log.d(TAG, "DocumentSnapshot data: " + task.getResult().getDocuments());

                                //Set NUTRITION to float
                                kcal += foodList.get(i).getNfCalories();
                                carbs += foodList.get(i).getNfTotalCarbohydrate();
                                fat += foodList.get(i).getNfTotalFat();
                                protein += foodList.get(i).getNfProtein();

                                Log.d(TAG, "Kcal: " + kcal);

                                tvFood.setText(String.format(Locale.getDefault(), "%.0f", kcal));
                                tvCarbs.setText(String.format(Locale.getDefault(), "%.0fg of %dg", carbs, calculationGoal / 2));
                                tvProtien.setText(String.format(Locale.getDefault(), "%.0fg of %dg", protein, calculationGoal * 20 / 100));
                                tvFat.setText(String.format(Locale.getDefault(), "%.0fg of %dg", fat, calculationGoal * 30 / 100));
                                //Need to be calculation
                                tvRemaining.setText(String.format(Locale.getDefault(), "%.0f", kcal));

                                updateProgressColor(progressCarbs, carbs, calculationGoal / 2);
                                updateProgressColor(progressProtien, protein, calculationGoal * 20 / 100);
                                updateProgressColor(progressFat, fat, calculationGoal * 30 / 100);
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }


    private void getUserDataAndSetGoal() {
        final ProgressDialog progressdialog = new ProgressDialog(getActivity());
        progressdialog.setMessage("Please Wait....");
        progressdialog.show();

        db.collection(UserRegister.fireBaseUsers).document(FireBaseInit.getEmailRegister()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        user = documentSnapshot.toObject(UserRegister.class);

                        assert user != null;
                        calculationGoal = user.thermicEffect(user.getActivityLevel());

                        tvGoal.setText(String.valueOf(calculationGoal));
                        tvCarbs.setText(String.format(Locale.getDefault(), "%.0fg of %dg", carbs, calculationGoal / 2));
                        tvProtien.setText(String.format(Locale.getDefault(), "%.0fg of %dg", protein, calculationGoal * 20 / 100));
                        tvFat.setText(String.format(Locale.getDefault(), "%.0fg of %dg", fat, calculationGoal * 30 / 100));

                        updateProgressColor(progressCarbs, carbs, calculationGoal / 2);
                        updateProgressColor(progressProtien, protein, calculationGoal * 20 / 100);
                        updateProgressColor(progressFat, fat, calculationGoal * 30 / 100);

                        progressdialog.hide();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
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


    // TODO: 2019-07-20 fix bug add water
    private void setViewDrink(View rowView) {
        final TextView tvWaterMl = rowView.findViewById(R.id.tv_water_ml);
        final TableRow tableLayout1 = rowView.findViewById(R.id.tab_layout_1);
        final TableRow tableLayout2 = rowView.findViewById(R.id.tab_layout_2);
        final TableRow tableLayout3 = rowView.findViewById(R.id.tab_layout_3);
        final TableRow tableLayout4 = rowView.findViewById(R.id.tab_layout_4);
        final ImageView addWater = rowView.findViewById(R.id.iv_water_icon);

        final boolean[] isRemovable1 = {true};
        final boolean[] isRemovable2 = {true};
        final boolean[] isRemovable3 = {true};

        final ImageView ivFullWater = new ImageView(getActivity());
        ivFullWater.setBackgroundResource(R.mipmap.full_water);

        addWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //add water in the view
                if (waterCount < 6) {
                    tableLayout1.addView(ivFullWater);
                    waterCount++;
                } else if (waterCount2 < 6) {
                    tableLayout2.addView(ivFullWater);
                    waterCount2++;
                    isRemovable1[0] = false;
                } else if (waterCount3 < 6) {
                    tableLayout3.addView(ivFullWater);
                    isRemovable2[0] = false;
                    waterCount3++;
                } else if (waterCount4 < 6) {
                    tableLayout4.addView(ivFullWater);
                    waterCount4++;
                    isRemovable3[0] = false;
                }

                waterMl += 0.25f;

                ivFullWater.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (waterCount > 0 && waterCount <= 6 && isRemovable1[0]) {
                            onRemoveWater(tableLayout1);
                            waterCount--;
                        } else if (waterCount2 > 0 && waterCount2 <= 6 && isRemovable2[0]) {
                            Log.d(TAG, "onClick: " + waterCount2 + " count1: " + waterCount + " boolean: " + isRemovable1[0]);
                            onRemoveWater(tableLayout2);
                            waterCount2--;

                        } else if (waterCount3 > 0 && waterCount3 <= 6 && isRemovable3[0]) {
                            onRemoveWater(tableLayout3);
                            waterCount3--;
                        } else if (waterCount4 > 0 && waterCount4 <= 6) {
                            onRemoveWater(tableLayout4);
                            waterCount4--;
                        }

                        waterMl -= 0.25f;
                        tvWaterMl.setText(waterMl + " L");
                    }
                });

                tvWaterMl.setText(waterMl + " L");
            }
        });
    }

    private void onRemoveWater(TableRow tableRow) {
        if (tableRow != null) {
            tableRow.removeViewAt(1);
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
}

