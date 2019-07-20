package fitness.albert.com.pumpit.fragment.logsFragment;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fitness.albert.com.pumpit.adapter.BreakfastListAdapter;
import fitness.albert.com.pumpit.adapter.DinnerListAdapter;
import fitness.albert.com.pumpit.adapter.LunchListAdapter;
import fitness.albert.com.pumpit.adapter.SnacksListAdapter;
import fitness.albert.com.pumpit.model.FireBaseInit;
import fitness.albert.com.pumpit.model.Foods;
import fitness.albert.com.pumpit.model.SwipeHelper;
import fitness.albert.com.pumpit.model.UserRegister;
import fitness.albert.com.pumpit.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LogNutritionFragment extends Fragment {

    private final String TAG = "LogNutritionFragment";
    private RecyclerView rvBreakfast, rvLunch, rvDinner, rvSnacks;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<Foods> breakfastList = new ArrayList<>();
    private ArrayList<Foods> lunchList = new ArrayList<>();
    private ArrayList<Foods> dinnerList = new ArrayList<>();
    private ArrayList<Foods> snacksList = new ArrayList<>();
    private TextView tvBreakfast, tvLunch, tvDinner, tvSnacks;
    private LinearLayout llBreakfast, llLunch, llDinner, llSnacks;
    private ProgressBar progressBar;


    public LogNutritionFragment() {
        // Required empty public constructor
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
        rvBreakfast = view.findViewById(R.id.rv_log_Breakfast);
        rvLunch = view.findViewById(R.id.rv_log_lunch);
        rvDinner = view.findViewById(R.id.rv_log_dinner);
        rvSnacks = view.findViewById(R.id.rv_log_snacks);

        tvBreakfast = view.findViewById(R.id.tv_log_breakfast);
        tvLunch = view.findViewById(R.id.tv_log_lunch);
        tvDinner = view.findViewById(R.id.tv_log_dinner);
        tvSnacks = view.findViewById(R.id.tv_log_snacks);

        llBreakfast = view.findViewById(R.id.breakfast_container);
        llDinner = view.findViewById(R.id.dinner_container);
        llLunch = view.findViewById(R.id.lunch_container);
        llSnacks = view.findViewById(R.id.snacks_container);

        progressBar = view.findViewById(R.id.pb_log_nutrition);

        rvBreakfast.setNestedScrollingEnabled(false);
        rvLunch.setNestedScrollingEnabled(false);
        rvDinner.setNestedScrollingEnabled(false);
        rvSnacks.setNestedScrollingEnabled(false);

        getNutritionFromFb(LogFragment.date, Foods.BREAKFAST);
        getNutritionFromFb(LogFragment.date, Foods.LUNCH);
        getNutritionFromFb(LogFragment.date, Foods.DINNER);
        getNutritionFromFb(LogFragment.date, Foods.SNACK);

        swipe(rvBreakfast);
        swipe(rvDinner);
        swipe(rvLunch);
        swipe(rvSnacks);
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private void getNutritionFromFb(final String date, final String nutritionType) {

        progressBar.setVisibility(View.VISIBLE);

        db.collection(Foods.NUTRITION).document(FireBaseInit.getEmailRegister()).collection(nutritionType)
                .document(date).collection(Foods.All_NUTRITION).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                progressBar.setVisibility(View.GONE);

                if(task.getResult().isEmpty()) {
                    Foods foods = null;
                    getNutrition(nutritionType, false, foods);
                }
                if (task.isSuccessful() && task.getResult() != null) {
                    for (int i = 0; i < task.getResult().size(); i++) {
                        Foods foods = task.getResult().getDocuments().get(i).toObject(Foods.class);
                        getNutrition(nutritionType, true, foods);
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


    private void getNutrition(String nutritionType, boolean isOnFb, Foods foods) {
        switch (nutritionType) {
            case Foods.BREAKFAST:
                if (isOnFb) {
                    breakfastList.add(foods);
                    initBreakfastRecyclerView();
                    llBreakfast.setVisibility(View.VISIBLE);
                } else {
                    llBreakfast.setVisibility(View.GONE);
                }
                break;
            case Foods.LUNCH:
                if (isOnFb) {
                    lunchList.add(foods);
                    initLunchRecyclerView();
                    llLunch.setVisibility(View.VISIBLE);
                } else {
                    llLunch.setVisibility(View.GONE);
                }
                break;
            case Foods.DINNER:
                if (isOnFb) {
                    dinnerList.add(foods);
                    initDinnerRecyclerView();
                    llDinner.setVisibility(View.VISIBLE);
                } else {
                    llDinner.setVisibility(View.GONE);
                }
                break;
            case Foods.SNACK:
                if (isOnFb) {
                    snacksList.add(foods);
                    initSnacksRecyclerView();
                    llSnacks.setVisibility(View.VISIBLE);
                } else {
                    llSnacks.setVisibility(View.GONE);
                }
        }
    }

    private void initBreakfastRecyclerView() {
        BreakfastListAdapter breakfastListAdapter;
        Log.d(TAG, "initRecyclerView: init LogNutrition recyclerView" + breakfastList);
        @SuppressLint("WrongConstant")
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvBreakfast.setLayoutManager(layoutManager);
        breakfastListAdapter = new BreakfastListAdapter(getActivity(), breakfastList);
        rvBreakfast.setAdapter(breakfastListAdapter);
    }

    private void initLunchRecyclerView() {
        LunchListAdapter lunchListAdapter;
        Log.d(TAG, "initRecyclerView: init LogNutrition recyclerView" + rvLunch);
        @SuppressLint("WrongConstant")
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvLunch.setLayoutManager(layoutManager);
        lunchListAdapter = new LunchListAdapter(getActivity(), lunchList);
        rvLunch.setAdapter(lunchListAdapter);
    }


    private void initDinnerRecyclerView() {
        DinnerListAdapter dinnerListAdapter;
        Log.d(TAG, "initRecyclerView: init LogNutrition recyclerView" + dinnerList);
        @SuppressLint("WrongConstant")
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvDinner.setLayoutManager(layoutManager);
        dinnerListAdapter = new DinnerListAdapter(getActivity(), dinnerList);
        rvDinner.setAdapter(dinnerListAdapter);
    }

    private void initSnacksRecyclerView() {
        SnacksListAdapter snacksListAdapter;
        Log.d(TAG, "initRecyclerView: init LogNutrition recyclerView" + snacksList);
        @SuppressLint("WrongConstant")
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvSnacks.setLayoutManager(layoutManager);
        snacksListAdapter = new SnacksListAdapter(getActivity(), snacksList);
        rvSnacks.setAdapter(snacksListAdapter);
    }


    private void swipe(RecyclerView recyclerView) {
        new SwipeHelper(getContext(), recyclerView) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Delete",
                        0,

                        Color.parseColor("#d50000"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                //    deleteItem(pos);
                                //  deleteFromFb(pos);
                                delNutrition(Foods.BREAKFAST, breakfastList, pos);

                            }
                        }
                ));
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Edit",
                        0,
                        Color.parseColor("#4caf50"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {

                            }
                        }
                ));
            }
        };
    }

    private void delNutrition(final String nutrition, ArrayList<Foods> foodsArrayList, int pos) {
        db.collection(Foods.NUTRITION).document(FireBaseInit.getEmailRegister())
                .collection(nutrition).document(UserRegister.getTodayData()).collection(Foods.All_NUTRITION)
                .whereEqualTo("food_name", foodsArrayList.get(pos).getFoodName())
                .whereEqualTo("serving_qty", foodsArrayList.get(pos).getServingQty()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (int i = 0; i < Objects.requireNonNull(task.getResult()).size(); i++) {
                                String docId = task.getResult().getDocuments().get(i).getId();
                                Log.d(TAG, "id: " + docId);
                                db.collection(Foods.NUTRITION).document(FireBaseInit.getEmailRegister())
                                        .collection(nutrition).document(UserRegister.getTodayData())
                                        .collection(Foods.All_NUTRITION).document(docId)
                                        .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.i(TAG, "Success delete doc");
                                    }
                                });
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "failed " + e.getMessage());
                    }
                });
    }
}
