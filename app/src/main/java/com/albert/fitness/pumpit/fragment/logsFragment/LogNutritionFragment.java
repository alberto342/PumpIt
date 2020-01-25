package com.albert.fitness.pumpit.fragment.logsFragment;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.albert.fitness.pumpit.adapter.BreakfastListAdapter;
import com.albert.fitness.pumpit.adapter.DinnerListAdapter;
import com.albert.fitness.pumpit.adapter.LunchListAdapter;
import com.albert.fitness.pumpit.adapter.SnacksListAdapter;
import com.albert.fitness.pumpit.model.Event;
import com.albert.fitness.pumpit.model.nutrition.Foods;
import com.albert.fitness.pumpit.model.nutrition.room.QueryNutritionItem;
import com.albert.fitness.pumpit.utils.PrefsUtils;
import com.albert.fitness.pumpit.utils.SwipeHelper;
import com.albert.fitness.pumpit.viewmodel.NutritionViewModel;

import java.util.ArrayList;
import java.util.List;

import fitness.albert.com.pumpit.R;

public class LogNutritionFragment extends Fragment {

    private final String TAG = "LogNutritionFragment";
    private RecyclerView rvBreakfast, rvLunch, rvDinner, rvSnacks;
    private List<QueryNutritionItem> breakfastList = new ArrayList<>();
    private List<QueryNutritionItem> lunchList = new ArrayList<>();
    private List<QueryNutritionItem> dinnerList = new ArrayList<>();
    private List<QueryNutritionItem> snacksList = new ArrayList<>();
    //private TextView tvBreakfast, tvLunch, tvDinner, tvSnacks;
    private LinearLayout llBreakfast, llLunch, llDinner, llSnacks;
    private NutritionViewModel viewModel;
    private String date = LogFragment.date;
    private boolean breakfastIsEmpty, lunchIsEmpty, dinnerIsEmpty, snacksIsEmpty;
    private BreakfastListAdapter breakfastListAdapter;
    private LunchListAdapter lunchListAdapter;
    private SnacksListAdapter snacksListAdapter;
    private DinnerListAdapter dinnerListAdapter;


    public LogNutritionFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_nutrition, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(NutritionViewModel.class);
        rvBreakfast = view.findViewById(R.id.rv_log_Breakfast);
        rvLunch = view.findViewById(R.id.rv_log_lunch);
        rvDinner = view.findViewById(R.id.rv_log_dinner);
        rvSnacks = view.findViewById(R.id.rv_log_snacks);

//        tvBreakfast = view.findViewById(R.id.tv_log_breakfast);
//        tvLunch = view.findViewById(R.id.tv_log_lunch);
//        tvDinner = view.findViewById(R.id.tv_log_dinner);
//        tvSnacks = view.findViewById(R.id.tv_log_snacks);

        llBreakfast = view.findViewById(R.id.breakfast_container);
        llDinner = view.findViewById(R.id.dinner_container);
        llLunch = view.findViewById(R.id.lunch_container);
        llSnacks = view.findViewById(R.id.snacks_container);


        rvBreakfast.setNestedScrollingEnabled(false);
        rvLunch.setNestedScrollingEnabled(false);
        rvDinner.setNestedScrollingEnabled(false);
        rvSnacks.setNestedScrollingEnabled(false);

        getNutrition(Foods.BREAKFAST);
        getNutrition(Foods.LUNCH);
        getNutrition(Foods.DINNER);
        getNutrition(Foods.SNACK);

        swipe(rvBreakfast,Foods.BREAKFAST);
        swipe(rvDinner,Foods.LUNCH);
        swipe(rvLunch,Foods.DINNER);
        swipe(rvSnacks,Foods.SNACK);
    }


    private void getNutrition(final String nutritionType) {
        viewModel.getNutritionItem(date, nutritionType)
                .observe(this, nutritionItems -> {
                    if (!nutritionItems.isEmpty()) {
                        getNutrition(nutritionType, true, nutritionItems);
                    } else {
                        getNutrition(nutritionType, false, nutritionItems);
                    }
                });
    }


    private void getNutrition(String nutritionType, boolean isNotEmpty, List<QueryNutritionItem> foods) {
        switch (nutritionType) {
            case Foods.BREAKFAST:
                if (isNotEmpty) {
                    breakfastList.addAll(foods);
                    initBreakfastRecyclerView();
                    llBreakfast.setVisibility(View.VISIBLE);
                } else {
                    llBreakfast.setVisibility(View.GONE);
                    breakfastIsEmpty = true;
                }
                break;
            case Foods.LUNCH:
                if (isNotEmpty) {
                    lunchList.addAll(foods);
                    initLunchRecyclerView();
                    llLunch.setVisibility(View.VISIBLE);
                } else {
                    llLunch.setVisibility(View.GONE);
                    lunchIsEmpty = true;
                }
                break;
            case Foods.DINNER:
                if (isNotEmpty) {
                    dinnerList.addAll(foods);
                    initDinnerRecyclerView();
                    llDinner.setVisibility(View.VISIBLE);
                } else {
                    llDinner.setVisibility(View.GONE);
                    dinnerIsEmpty = true;
                }
                break;
            case Foods.SNACK:
                if (isNotEmpty) {
                    snacksList.addAll(foods);
                    initSnacksRecyclerView();
                    llSnacks.setVisibility(View.VISIBLE);
                } else {
                    llSnacks.setVisibility(View.GONE);
                    snacksIsEmpty = true;
                }
        }

        if (breakfastIsEmpty && lunchIsEmpty && dinnerIsEmpty && snacksIsEmpty) {
            listIsEmpty();
        }
    }

    private void initBreakfastRecyclerView() {

        Log.d(TAG, "initRecyclerView: init LogNutrition recyclerView" + breakfastList);
        @SuppressLint("WrongConstant")
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvBreakfast.setLayoutManager(layoutManager);
        breakfastListAdapter = new BreakfastListAdapter(getActivity(), breakfastList);
        rvBreakfast.setAdapter(breakfastListAdapter);
    }

    private void initLunchRecyclerView() {

        Log.d(TAG, "initRecyclerView: init LogNutrition recyclerView" + rvLunch);
        @SuppressLint("WrongConstant")
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvLunch.setLayoutManager(layoutManager);
        lunchListAdapter = new LunchListAdapter(getActivity(), lunchList);
        rvLunch.setAdapter(lunchListAdapter);
    }


    private void initDinnerRecyclerView() {

        Log.d(TAG, "initRecyclerView: init LogNutrition recyclerView" + dinnerList);
        @SuppressLint("WrongConstant")
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvDinner.setLayoutManager(layoutManager);
        dinnerListAdapter = new DinnerListAdapter(getActivity(), dinnerList);
        rvDinner.setAdapter(dinnerListAdapter);
    }

    private void initSnacksRecyclerView() {

        Log.d(TAG, "initRecyclerView: init LogNutrition recyclerView" + snacksList);
        @SuppressLint("WrongConstant")
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvSnacks.setLayoutManager(layoutManager);
        snacksListAdapter = new SnacksListAdapter(getActivity(), snacksList);
        rvSnacks.setAdapter(snacksListAdapter);
    }


    private void swipe(RecyclerView recyclerView, String type) {
        new SwipeHelper(getContext(), recyclerView) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Delete",
                        0,
                        Color.parseColor("#d50000"),
                        pos -> delNutrition(type, pos)
                ));
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Edit",
                        0,
                        Color.parseColor("#4caf50"),
                        pos -> {

                        }
                ));
            }
        };
    }


    private void delNutrition(final String type, int pos) {
        int logId = 0;
        switch (type) {
            case Foods.BREAKFAST:
                logId = breakfastList.get(pos).getLogId();
                breakfastListAdapter.removeItem(pos);
                break;
            case Foods.DINNER:
                logId = dinnerList.get(pos).getLogId();
                dinnerListAdapter.removeItem(pos);
                break;
            case Foods.LUNCH:
                logId = lunchList.get(pos).getLogId();
                lunchListAdapter.removeItem(pos);
                break;
            case Foods.SNACK:
                logId = snacksList.get(pos).getLogId();
                snacksListAdapter.removeItem(pos);
        }
        int finalLogId = logId;
        viewModel.getFoodLogByLogId(logId)
                .observe(this, foodLog -> {
                    if (foodLog != null) {
                        viewModel.deleteFoodLog(foodLog);
                        Log.d(TAG, "onSwiped: Deleted successfully lodId: " + finalLogId);
                    }
                });

    }
    private void listIsEmpty() {
        PrefsUtils prefsUtils = new PrefsUtils(getContext(), PrefsUtils.HAVE_WORKOUT_EVENT);
        boolean haveWorkout = prefsUtils.getBoolean(date, false);
        if(haveWorkout) {
            Event.removeEvent(getContext());
        }

//        PrefsUtils p = new PrefsUtils(getContext(), PrefsUtils.HAVE_NUTRITION_EVENT);
//        p.saveData(date, true);
    }
}
