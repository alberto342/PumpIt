package fitness.albert.com.pumpit.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import fitness.albert.com.pumpit.Model.Foods;
import fitness.albert.com.pumpit.R;
import fitness.albert.com.pumpit.ShowAllNutritionActivity;
import fitness.albert.com.pumpit.ShowItemFoodActivity;

public class FirestoreFoodListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context mContext;
    public static List<Foods> foodsList;
    private final String TAG = "FirestoreFoodListAdapter";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    //Firebase item id
    public static String fireId;
    public static String foodName;
    public static int qty;
    public static String unit;
    public static String nutrition;


    public FirestoreFoodListAdapter(Context mContext, List<Foods> foodsList) {
        this.mContext = mContext;
        this.foodsList = foodsList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_food_nutrition, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        bindViews((ViewHolder) holder, position);
    }


    @SuppressLint({"LongLogTag"})
    private void bindViews(final ViewHolder holder, final int position) {


        Picasso.get()
                .load(foodsList.get(position).getPhoto().getThumb())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.ivImage);


        holder.tvFoodName.setText(foodsList.get(position).getFood_name());
        holder.tvCalories.setText(String.format(Locale.getDefault(), "%.0f Kcal,  %.0f Carbs", foodsList.get(position).getNf_calories(), foodsList.get(position).getNf_total_carbohydrate()));
        holder.tvProtein.setText(String.format(Locale.getDefault(), "%.0f Protein", foodsList.get(position).getNf_protein()));
        holder.tvServiceQty.setText("Qty: " + foodsList.get(position).getServing_qty());


        holder.itemFoodSelected.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {

                //refresh page
                mContext.startActivity(new Intent(mContext, ShowAllNutritionActivity.class));
                ((ShowAllNutritionActivity) mContext).finish();

                //Get Food id
                getFoodId(position);

                Log.d(TAG, "onClick: clicked on: " + holder.tvFoodName.getText().toString());

                foodName = holder.tvFoodName.getText().toString();
                qty = foodsList.get(position).getServing_qty();
                unit = foodsList.get(position).getServing_unit();

                Intent intent = new Intent(mContext, ShowItemFoodActivity.class);
                mContext.startActivity(intent);
                foodsList.clear();
                ((Activity) mContext).finish();
            }
        });
    }

    //Get firebase food item id
    private void getFoodId(final int position) {
        db.collection(Foods.nutrition).document(getEmailRegister())
                .collection(Foods.breakfast).document(getTodayDate())
                .collection(Foods.fruit).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            fireId = task.getResult().getDocuments().get(position).getId();


                            //getDocuments splite for get nutrition
                            String getDocuments = String.valueOf(task.getResult().getDocuments());
                            String[] part = getDocuments.split("/");
                            nutrition = part[2];

                            Log.d(TAG, "Nutrition: " + nutrition);
                            Log.d(TAG, "Documents: " + task.getResult().getDocuments());
//                            SavePref savePref = new SavePref();
//                            savePref.createSharedPreferencesFiles(mContext, "fire_id");
//                            savePref.saveData("foodId", task.getResult().getDocuments().get(position).getId());

                            Log.d(TAG, "FireId: " + fireId);
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


    @Override
    public int getItemCount() {
        return foodsList.size();
    }


    public void removeItem(int position) {
        foodsList.remove(position);
        notifyItemRemoved(position);
    }


    public void restoreItem(Foods item, int position) {
        foodsList.add(position, item);
        notifyItemInserted(position);
    }


    public List<Foods> getData() {
        return foodsList;
    }

    public String getEmailRegister() {
        String email = null;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            email = mAuth.getCurrentUser().getEmail();
        }
        return email;
    }


    public String getTodayDate() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        return df.format(c);
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView ivImage;
        private TextView tvFoodName;
        private TextView tvCalories;
        private TextView tvProtein;
        private TextView tvServiceQty;
        private LinearLayout itemFoodSelected;


        public ViewHolder(View rowView) {
            super(rowView);
            rowView.setOnClickListener(this);

            ivImage = rowView.findViewById(R.id.iv_food_img);
            tvFoodName = rowView.findViewById(R.id.tv_food_name);
            tvCalories = rowView.findViewById(R.id.tv_calories);
            tvProtein = rowView.findViewById(R.id.tv_protin);
            tvServiceQty = rowView.findViewById(R.id.tv_service_quantity);
            itemFoodSelected = rowView.findViewById(R.id.item_food_selected);
        }

        @Override
        public void onClick(View view) {

        }
    }


    @Override
    public long getItemId(int position) {
        //Auto-generated method stub
        return position;
    }


}
