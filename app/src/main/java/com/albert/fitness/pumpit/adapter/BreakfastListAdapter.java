package com.albert.fitness.pumpit.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.albert.fitness.pumpit.fragment.logsFragment.LogTabActivity;
import com.albert.fitness.pumpit.model.nutrition.room.QueryNutritionItem;
import com.albert.fitness.pumpit.nutrition.ShowAllNutritionActivity;
import com.albert.fitness.pumpit.nutrition.ShowBreakfastActivity;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import fitness.albert.com.pumpit.R;

public class BreakfastListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final String TAG = "BreakfastListAdapter";
    private Context mContext;
    private List<QueryNutritionItem> nutritionItems;
    public static String foodName;

    public BreakfastListAdapter(Context mContext, List<QueryNutritionItem> nutritionItems) {
        this.mContext = mContext;
        this.nutritionItems = nutritionItems;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_food_nutrition, parent, false);
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

    @SuppressLint({"LongLogTag", "SetTextI18n"})
    private void bindViews(final ViewHolder holder, final int position) {

        Log.d(TAG, "bindViews: " + nutritionItems.get(position).getThumb());

        Picasso.get()
                .load(nutritionItems.get(position).getThumb())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.ivImage);

        holder.tvFoodName.setText(nutritionItems.get(position).getFoodName());
        holder.tvCalories.setText(String.format(Locale.getDefault(), "%.2f Kcal,  %.2f Carbs", nutritionItems.get(position).getCalories(),
                nutritionItems.get(position).getTotalCarbohydrate()));
        holder.tvProtein.setText(String.format(Locale.getDefault(), "%.2f Protein", nutritionItems.get(position).getProtein()));
        holder.tvServiceQty.setText("Qty: " + nutritionItems.get(position).getQty());
        holder.itemView.setOnClickListener(v -> {
            Log.d(TAG, "onClick: clicked on: " + holder.tvFoodName.getText().toString());

            foodName = holder.tvFoodName.getText().toString();

//            String cotxt = mContext.toString();
//            String[] parts = cotxt.split("@");
//            String part1 = parts[0];

            if(mContext instanceof LogTabActivity) {
                mContext.startActivity(new Intent(mContext, ShowAllNutritionActivity.class));
                ((LogTabActivity) mContext).finish();
            }

//            if (!part1.equals("LogTabActivity")) {
//                mContext.startActivity(new Intent(mContext, ShowAllNutritionActivity.class));
//                ((ShowAllNutritionActivity) mContext).finish();
//            }

            Intent i = new Intent(mContext, ShowBreakfastActivity.class);
            i.putExtra("logId", nutritionItems.get(position).getLogId());
            i.putExtra("foodName", nutritionItems.get(position).getFoodName());
            mContext.startActivity(i);


            ((Activity) mContext).recreate();

//                mContext.startActivity(new Intent(mContext, ShowBreakfastActivity.class));
//                foodsList.clear();
//                ((Activity) mContext).finish();
        });
    }


    @Override
    public int getItemCount() {
        return nutritionItems.size();
    }


    public void removeItem(int position) {
        nutritionItems.remove(position);
        notifyItemRemoved(position);
    }


    public void restoreItem(QueryNutritionItem item, int position) {
        nutritionItems.add(position, item);
        notifyItemInserted(position);
    }


    public List<QueryNutritionItem> getData() {
        return nutritionItems;
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView ivImage;
        private TextView tvFoodName;
        private TextView tvCalories;
        private TextView tvProtein;
        private TextView tvServiceQty;

        public ViewHolder(View rowView) {
            super(rowView);
            rowView.setOnClickListener(this);
            ivImage = rowView.findViewById(R.id.iv_food_img);
            tvFoodName = rowView.findViewById(R.id.tv_food_name);
            tvCalories = rowView.findViewById(R.id.tv_calories);
            tvProtein = rowView.findViewById(R.id.tv_protin);
            tvServiceQty = rowView.findViewById(R.id.tv_service_quantity);
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
