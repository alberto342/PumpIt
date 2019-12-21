package com.albert.fitness.pumpit.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.albert.fitness.pumpit.model.nutrition.room.QueryNutritionItem;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import fitness.albert.com.pumpit.R;

public class DinnerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final String TAG = "DinnerListAdapter";
    private Context mContext;
    public static String foodName;
    private List<QueryNutritionItem> nutritionItems;


    public DinnerListAdapter(Context mContext, List<QueryNutritionItem> queryNutritionItems) {
        this.mContext = mContext;
        this.nutritionItems = queryNutritionItems;

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

        Picasso.get()
                .load(nutritionItems.get(position).getThumb())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.ivImage);

        holder.tvFoodName.setText(nutritionItems.get(position).getFoodName());
        holder.tvCalories.setText(String.format(Locale.getDefault(), "%.0f Kcal,  %.0f Carbs", nutritionItems.get(position).getCalories(), nutritionItems.get(position).getTotalCarbohydrate()));
        holder.tvProtein.setText(String.format(Locale.getDefault(), "%.0f Protein", nutritionItems.get(position).getProtein()));
        holder.tvServiceQty.setText("Qty: " + nutritionItems.get(position).getQty());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Log.d(TAG, "onClick: clicked on: " + holder.tvFoodName.getText().toString());
//
//                foodName = foodsList.get(position).getFoodName();
//
//                String cotxt = mContext.toString();
//                String[] parts = cotxt.split("@");
//                String part1 = parts[0];
//
//                //refresh page
//                if (!part1.equals("LogTabActivity")) {
//                    mContext.startActivity(new Intent(mContext, ShowAllNutritionActivity.class));
//                    ((ShowAllNutritionActivity) mContext).finish();
//                }
//
//                Intent i = new Intent(mContext, ShowDinnerActivity.class);
//                i.putExtra("foodPhoto", foodsList.get(position).getPhoto().getHighres());
//                i.putExtra("kcal", foodsList.get(position).getNfCalories());
//                i.putExtra("fat", foodsList.get(position).getNfTotalFat());
//                i.putExtra("protein", foodsList.get(position).getNfProtein());
//                i.putExtra("carbohydrate", foodsList.get(position).getNfTotalCarbohydrate());
//                i.putExtra("servingWeightGrams", foodsList.get(position).getServingWeightGrams());
//                i.putExtra("qty", foodsList.get(position).getServingQty());
//                i.putExtra("servingUnit", foodsList.get(position).getServingUnit());
//                i.putExtra("altMeasuresSize", foodsList.get(position).getAltMeasures().size());
//                i.putExtra("fullNutrientsSize", foodsList.get(position).getFullNutrients().size());
//                i.putExtra("foodGroup", foodsList.get(position).getTags().getFoodGroup());
//
//
//                for (int r = 0; r < foodsList.get(position).getAltMeasures().size(); r++) {
//                    i.putExtra("measure" + r, foodsList.get(position).getAltMeasures().get(r).getMeasure());
//                    i.putExtra("measureServingWeight" + r, foodsList.get(position).getAltMeasures().get(r).getServingWeight());
//                }
//
//                for (int r = 0; r < foodsList.get(position).getFullNutrients().size(); r++) {
//                    i.putExtra("attrId" + r, foodsList.get(position).getFullNutrients().get(r).getAttrId());
//                    i.putExtra("values" + r, foodsList.get(position).getFullNutrients().get(r).getValue());
//                }
//
//                mContext.startActivity(i);
            }
        });
    }


    @Override
    public int getItemCount() {
        return nutritionItems.size();
    }

//
//    public void removeItem(int position) {
//        foodsList.remove(position);
//        notifyItemRemoved(position);
//    }
//
//
//    public void restoreItem(Foods item, int position) {
//        foodsList.add(position, item);
//        notifyItemInserted(position);
//    }


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
