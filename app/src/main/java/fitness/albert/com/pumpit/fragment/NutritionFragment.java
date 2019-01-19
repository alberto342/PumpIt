package fitness.albert.com.pumpit.fragment;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fitness.albert.com.pumpit.Adapter.FirestoreFoodListAdapter;
import fitness.albert.com.pumpit.Model.Foods;
import fitness.albert.com.pumpit.Model.SavePref;
import fitness.albert.com.pumpit.Model.SwipeToDeleteCallback;
import fitness.albert.com.pumpit.Model.UserRegister;
import fitness.albert.com.pumpit.R;
import fitness.albert.com.pumpit.SearchFoodsActivity;


public class NutritionFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ImageView btnAddBreakfast, btnAddLunch, btnAddSnacks, btnAddDinner;
    private ImageView btnSelectBreakfast, btnSelectLunch, btnSelectDinner, btnSelectSnacks;
    private TextView tvGoal, tvFood, tvExersice, tvRemaining, tvFat, tvProtien, tvCarbs;
    private List<Foods> foodList = new ArrayList<>();
    private RecyclerView rvListFood;
    private SavePref savePref = new SavePref();
    UserRegister user = new UserRegister();
    private float kcal, fat, protien, carbs;
    private String TAG;
    private FirestoreFoodListAdapter mAdapter = new FirestoreFoodListAdapter(getActivity(), foodList);
    CoordinatorLayout coordinatorLayout;


    //Todo finish the selected on food



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

        getMealFromFs("breakfast");

        getUserDataAndSetGoal();

        enableSwipeToDeleteAndUndo();

    }


    private void init(View view) {
        //add food btn
        btnAddBreakfast = view.findViewById(R.id.btn_add_breakfast);
        btnAddDinner = view.findViewById(R.id.btn_add_dinner);
        btnAddLunch = view.findViewById(R.id.btn_add_lunch);
        btnAddSnacks = view.findViewById(R.id.btn_add_snacks);

        //RecyclerView
        rvListFood = view.findViewById(R.id.rv_list_food);

        //coordinatorLayout
//        coordinatorLayout = view.findViewById(R.id.coordinatorLayout);

        //btn selected
        btnSelectBreakfast = view.findViewById(R.id.breakfast);
        btnSelectDinner = view.findViewById(R.id.dinner);
        btnSelectLunch = view.findViewById(R.id.lunch);
        btnSelectSnacks = view.findViewById(R.id.snacks);


        tvGoal = view.findViewById(R.id.tv_goal);
        tvExersice = view.findViewById(R.id.tv_exercise);

        tvFood = view.findViewById(R.id.tv_food);
        tvRemaining = view.findViewById(R.id.tv_remaining);

        tvFat = view.findViewById(R.id.tv_fat);
        tvCarbs = view.findViewById(R.id.tv_carbs);
        tvProtien = view.findViewById(R.id.tv_protein);


        //disable RecyclerView scrolling
        rvListFood.setNestedScrollingEnabled(false);


        btnAddBreakfast.setOnClickListener(onClickListener);
        btnAddSnacks.setOnClickListener(onClickListener);
        btnAddLunch.setOnClickListener(onClickListener);
        btnAddDinner.setOnClickListener(onClickListener);


//        btnSelectBreakfast.setOnClickListener(onClickListener);
//        btnSelectDinner.setOnClickListener(onClickListener);
//        btnSelectLunch.setOnClickListener(onClickListener);
//        btnSelectSnacks.setOnClickListener(onClickListener);
    }




    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            savePref.createSharedPreferencesFiles(getActivity(), Foods.SharedPreferencesFile);
            switch (v.getId()) {
                case R.id.btn_add_dinner:
                    saveMealToSP(true, false, false, false);
                    break;

                case R.id.btn_add_breakfast:
                    saveMealToSP(false,true,false,false);
                    break;

                case R.id.btn_add_lunch:
                    saveMealToSP(false,false,true,false);
                    break;

                case R.id.btn_add_snacks:
                    saveMealToSP(false,false,false,true);
                    break;

//                case R.id.breakfast:
//
//                    break;
//                case R.id.dinner:
//
//                   break;
//                case R.id.lunch:
//
//                    break;
//                case R.id.snacks:
//
//                    break;
            }
        }
    };

    private void saveMealToSP(boolean dinner, boolean breakfast, boolean lunch, boolean snack) {
        savePref.saveData("dinner", dinner);
        savePref.saveData("breakfast", breakfast);
        savePref.saveData("lunch", lunch);
        savePref.saveData("snack", snack);

        startActivity( new Intent(getActivity(), SearchFoodsActivity.class));
        getActivity().getFragmentManager().popBackStack();
    }


    private void getMealFromFs(String keyValue) {

        final ProgressDialog progressdialog = new ProgressDialog(getActivity());
        progressdialog.setMessage("Please Wait....");
        progressdialog.show();

        //get nutrition from firestone
        db.collection("nutrition").document(user.getEmailRegister()).collection(keyValue).document(getTodayDate()).collection("fruit").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        //hide ProgressDialog
                        progressdialog.hide();

                        if(task.isSuccessful()) {

                            for (int i = 0; i < task.getResult().getDocuments().size(); i++) {
                                Foods foods = task.getResult().getDocuments().get(i).toObject(Foods.class);
                                foodList.add(foods);

                                initRecyclerView();

                                Log.d(TAG, "DocumentSnapshot data: " + task.getResult().getDocuments());

                                //set nutrition to float
                                kcal += foodList.get(i).getNf_calories();
                                carbs += foodList.get(i).getNf_total_carbohydrate();
                                fat += foodList.get(i).getNf_total_fat();
                                protien += foodList.get(i).getNf_protein();
                            }


                            tvFood.setText(String.format("%.2f", kcal));
                            tvCarbs.setText(String.format("%.2fg of 334g", carbs));
                            tvProtien.setText(String.format("%.2fg of 25g", protien));
                            tvFat.setText(String.format("%.2fg of 67g", fat));
                            float remaining = kcal + 0;
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
        db.collection("users").document(user.getEmailRegister()).get()
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


    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init food recyclerView");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        rvListFood.setLayoutManager(layoutManager);
        mAdapter = new FirestoreFoodListAdapter(getActivity(), foodList);
        rvListFood.setAdapter(mAdapter);
    }

    private String getTodayDate() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        return df.format(c);
    }


    private int getListSize() {
        return foodList.size();
    }




    // Swipe to delete item
    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getActivity()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();
                final Foods item = mAdapter.getData().get(position);

                mAdapter.removeItem(position);

                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                            mAdapter.restoreItem(item, position);
                            rvListFood.scrollToPosition(position);
                    }
                });
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            }
        };


        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(rvListFood);
    }



    //Todo Delete data from firestore chack if user not click on UNDO
    private void deleteFromFs() {

    }

    private int dpToPxl(int paddingDp) {
        float density = getActivity().getResources().getDisplayMetrics().density;
        return (int)(paddingDp * density);
    }

    //Without this method Data will be double
    @SuppressLint("MissingSuperCall")
    @Override
    public void onPause() {
        super.onPause();
        this.foodList.clear();
    }
}

