package fitness.albert.com.pumpit.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import fitness.albert.com.pumpit.Model.FireBaseInit;
import fitness.albert.com.pumpit.Model.Foods;
import fitness.albert.com.pumpit.Model.SavePref;
import fitness.albert.com.pumpit.Model.UserRegister;
import fitness.albert.com.pumpit.R;
import fitness.albert.com.pumpit.SearchFoodsActivity;
import fitness.albert.com.pumpit.ShowAllNutritionActivity;


public class NutritionFragment extends Fragment {

    private ImageView btnAddBreakfast, btnAddLunch, btnAddSnacks, btnAddDinner;
    private TextView tvGoal, tvFood, tvExersice, tvRemaining, tvFat, tvProtien, tvCarbs, tvDetails;
    private List<Foods> foodList = new ArrayList<>();
    private SavePref savePref = new SavePref();
    UserRegister user = new UserRegister();
    private float kcal, fat, protein, carbs;
    private final String TAG = "NutritionFragment";


    public NutritionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nutrition, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        getUserDataAndSetGoal();

        getMealFromFs(Foods.breakfast);
        getMealFromFs(Foods.snack);
        getMealFromFs(Foods.lunch);
        getMealFromFs(Foods.dinner);
    }


    private void init(View view) {
        //add food btn
        btnAddBreakfast = view.findViewById(R.id.btn_add_breakfast);
        btnAddDinner = view.findViewById(R.id.btn_add_dinner);
        btnAddLunch = view.findViewById(R.id.btn_add_lunch);
        btnAddSnacks = view.findViewById(R.id.btn_add_snacks);
        tvDetails = view.findViewById(R.id.tv_details);


        tvGoal = view.findViewById(R.id.tv_goal);
        tvExersice = view.findViewById(R.id.tv_exercise);

        tvFood = view.findViewById(R.id.tv_food);
        tvRemaining = view.findViewById(R.id.tv_remaining);

        tvFat = view.findViewById(R.id.tv_fat);
        tvCarbs = view.findViewById(R.id.tv_carbs);
        tvProtien = view.findViewById(R.id.tv_protein);


        //disable RecyclerView scrolling
//        rvListFood.setNestedScrollingEnabled(false);


        btnAddBreakfast.setOnClickListener(onClickListener);
        btnAddSnacks.setOnClickListener(onClickListener);
        btnAddLunch.setOnClickListener(onClickListener);
        btnAddDinner.setOnClickListener(onClickListener);
        tvDetails.setOnClickListener(onClickListener);
    }


    //Check meal selected
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            savePref.createSharedPreferencesFiles(getActivity(), Foods.SharedPreferencesFile);


            switch (v.getId()) {
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
                    break;
                case R.id.tv_details:
                    startActivity(new Intent(getActivity(), ShowAllNutritionActivity.class));
                    break;
            }
        }
    };

    private void saveMealToSP(boolean dinner, boolean breakfast, boolean lunch, boolean snack) {
        savePref.saveData("dinner", dinner);
        savePref.saveData("breakfast", breakfast);
        savePref.saveData("lunch", lunch);
        savePref.saveData("snack", snack);

        startActivity(new Intent(getActivity(), SearchFoodsActivity.class));
        getActivity().getFragmentManager().popBackStack();
    }


    private void getMealFromFs(String keyValue) {

        final ProgressDialog progressdialog = new ProgressDialog(getActivity());
        progressdialog.setMessage("Please Wait....");
        progressdialog.show();

        //get nutrition from firestone
        FireBaseInit.getInstance(getActivity())
                .db.collection("nutrition").document(FireBaseInit.fireBaseInit.getEmailRegister())
                .collection(keyValue).document(FireBaseInit.fireBaseInit.getTodayDate())
                .collection("fruit").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        //hide ProgressDialog
                        progressdialog.hide();

                        if (task.isSuccessful()) {

                            for (int i = 0; i < task.getResult().getDocuments().size(); i++) {
                                Foods foods = task.getResult().getDocuments().get(i).toObject(Foods.class);
                                foodList.add(foods);


                                Log.d(TAG, "DocumentSnapshot data: " + task.getResult().getDocuments());

                                //set nutrition to float
                                kcal += foodList.get(i).getNf_calories();
                                carbs += foodList.get(i).getNf_total_carbohydrate();
                                fat += foodList.get(i).getNf_total_fat();
                                protein += foodList.get(i).getNf_protein();

                            }

                            tvFood.setText(String.format("%.2f", kcal));
                            tvCarbs.setText(String.format("%.2fg of 334g", carbs));
                            tvProtien.setText(String.format("%.2fg of 25g", protein));
                            tvFat.setText(String.format("%.2fg of 67g", fat));
                            float remaining = kcal;
                            tvRemaining.setText(String.format("%.2f", remaining));

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
        FireBaseInit.getInstance(getActivity())
                .db.collection("users").document(FireBaseInit.fireBaseInit.getEmailRegister()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        user = documentSnapshot.toObject(UserRegister.class);

                        tvGoal.setText(String.valueOf(user.calculatorBMR()));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }

}

