package fitness.albert.com.pumpit.fragment.logsFragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import fitness.albert.com.pumpit.Adapter.BreakfastListAdapter;
import fitness.albert.com.pumpit.Adapter.DinnerListAdapter;
import fitness.albert.com.pumpit.Adapter.LunchListAdapter;
import fitness.albert.com.pumpit.Adapter.SnacksListAdapter;
import fitness.albert.com.pumpit.Model.FireBaseInit;
import fitness.albert.com.pumpit.Model.Foods;
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
    private TextView tvBreakfast, tvLunch, tvDinner,tvSnacks;


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

        rvBreakfast.setNestedScrollingEnabled(false);
        rvLunch.setNestedScrollingEnabled(false);
        rvDinner.setNestedScrollingEnabled(false);
        rvSnacks.setNestedScrollingEnabled(false);

    }

    @Override
    public void onResume() {
        super.onResume();
        getNutritionFromFb(LogFragment.date, Foods.BREAKFAST);
        getNutritionFromFb(LogFragment.date, Foods.LUNCH);
        getNutritionFromFb(LogFragment.date, Foods.DINNER);
        getNutritionFromFb(LogFragment.date, Foods.SNACK);
    }


    private void getNutritionFromFb(String date, final String nutritionType) {
        db.collection(Foods.NUTRITION).document(FireBaseInit.getEmailRegister()).collection(nutritionType)
                .document(date).collection(Foods.All_NUTRITION).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    for (int i = 0; i < task.getResult().size(); i++) {
                        Foods foods = task.getResult().getDocuments().get(i).toObject(Foods.class);
                        switch (nutritionType) {
                            case "breakfast":
                                breakfastList.add(foods);
                                if (!breakfastList.isEmpty()) {
                                  rvBreakfast.setVisibility(View.VISIBLE);
                                  tvBreakfast.setVisibility(View.VISIBLE);
                                    initBreakfastRecyclerView();
                                } else {
                                    rvBreakfast.setVisibility(View.GONE);
                                    tvBreakfast.setVisibility(View.GONE);
                                }
                                break;
                            case "lunch":
                                lunchList.add(foods);
                                if (!lunchList.isEmpty()) {
                                    rvLunch.setVisibility(View.VISIBLE);
                                    initLunchRecyclerView();
                                } else {
                                    rvLunch.setVisibility(View.GONE);
                                    tvLunch.setVisibility(View.GONE);
                                }
                                break;
                            case "dinner":
                                dinnerList.add(foods);
                                if (!dinnerList.isEmpty()) {
                                    rvDinner.setVisibility(View.VISIBLE);
                                    initLunchRecyclerView();
                                } else {
                                    rvDinner.setVisibility(View.GONE);
                                    tvDinner.setVisibility(View.GONE);
                                }
                                initDinnerRecyclerView();
                                break;
                            case "Snack":
                                snacksList.add(foods);
                                if (!snacksList.isEmpty()) {
                                   rvSnacks.setVisibility(View.VISIBLE);
                                   tvSnacks.setVisibility(View.VISIBLE);
                                    initLunchRecyclerView();
                                } else {
                                    rvSnacks.setVisibility(View.GONE);
                                    tvSnacks.setVisibility(View.GONE);
                                }
                                initSnacksRecyclerView();
                        }
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

    private void initBreakfastRecyclerView() {
        BreakfastListAdapter breakfastListAdapter;
        Log.d(TAG, "initRecyclerView: init LogNutrition recyclerView" + breakfastList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvBreakfast.setLayoutManager(layoutManager);
        breakfastListAdapter = new BreakfastListAdapter(getActivity(), breakfastList);
        rvBreakfast.setAdapter(breakfastListAdapter);
    }

    private void initLunchRecyclerView() {
        LunchListAdapter lunchListAdapter;
        Log.d(TAG, "initRecyclerView: init LogNutrition recyclerView" + rvLunch);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvLunch.setLayoutManager(layoutManager);
        lunchListAdapter = new LunchListAdapter(getActivity(), lunchList);
        rvLunch.setAdapter(lunchListAdapter);
    }


    private void initDinnerRecyclerView() {
        DinnerListAdapter dinnerListAdapter;
        Log.d(TAG, "initRecyclerView: init LogNutrition recyclerView" + dinnerList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvDinner.setLayoutManager(layoutManager);
        dinnerListAdapter = new DinnerListAdapter(getActivity(), dinnerList);
        rvDinner.setAdapter(dinnerListAdapter);
    }

    private void initSnacksRecyclerView() {
        SnacksListAdapter snacksListAdapter;
        Log.d(TAG, "initRecyclerView: init LogNutrition recyclerView" + snacksList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvSnacks.setLayoutManager(layoutManager);
        snacksListAdapter = new SnacksListAdapter(getActivity(), snacksList);
        rvSnacks.setAdapter(snacksListAdapter);
    }
}
