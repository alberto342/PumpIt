package fitness.albert.com.pumpit.adapter;


import android.annotation.SuppressLint;
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

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import fitness.albert.com.pumpit.model.Foods;
import fitness.albert.com.pumpit.R;
import fitness.albert.com.pumpit.nutrition.ShowAllNutritionActivity;
import fitness.albert.com.pumpit.nutrition.ShowDinnerActivity;

public class DinnerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final String TAG = "DinnerListAdapter";
    private Context mContext;
    private List<Foods> foodsList;
    public static String foodName;


    public DinnerListAdapter(Context mContext, List<Foods> foodsList) {
        this.mContext = mContext;
        this.foodsList = foodsList;
    }


    @NonNull
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


    @SuppressLint({"LongLogTag", "SetTextI18n"})
    private void bindViews(final ViewHolder holder, final int position) {

        Picasso.get()
                .load(foodsList.get(position).getPhoto().getThumb())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.ivImage);

        holder.tvFoodName.setText(foodsList.get(position).getFoodName());
        holder.tvCalories.setText(String.format(Locale.getDefault(), "%.0f Kcal,  %.0f Carbs", foodsList.get(position).getNfCalories(), foodsList.get(position).getNfTotalCarbohydrate()));
        holder.tvProtein.setText(String.format(Locale.getDefault(), "%.0f Protein", foodsList.get(position).getNfProtein()));
        holder.tvServiceQty.setText("Qty: " + foodsList.get(position).getServingQty());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "onClick: clicked on: " + holder.tvFoodName.getText().toString());

                foodName = foodsList.get(position).getFoodName();

                String cotxt = mContext.toString();
                String[] parts = cotxt.split("@");
                String part1 = parts[0];

                //refresh page
                if (!part1.equals("fitness.albert.com.pumpit.fragment.logsFragment.LogTabActivity")) {
                    mContext.startActivity(new Intent(mContext, ShowAllNutritionActivity.class));
                    ((ShowAllNutritionActivity) mContext).finish();
                }

                Intent i = new Intent(mContext, ShowDinnerActivity.class);
                i.putExtra("foodPhoto", foodsList.get(position).getPhoto().getHighres());
                i.putExtra("kcal", foodsList.get(position).getNfCalories());
                i.putExtra("fat", foodsList.get(position).getNfTotalFat());
                i.putExtra("protein", foodsList.get(position).getNfProtein());
                i.putExtra("carbohydrate", foodsList.get(position).getNfTotalCarbohydrate());
                i.putExtra("servingWeightGrams", foodsList.get(position).getServingWeightGrams());
                i.putExtra("qty", foodsList.get(position).getServingQty());
                i.putExtra("servingUnit", foodsList.get(position).getServingUnit());
                i.putExtra("altMeasuresSize", foodsList.get(position).getAltMeasures().size());
                i.putExtra("fullNutrientsSize", foodsList.get(position).getFullNutrients().size());
                i.putExtra("foodGroup", foodsList.get(position).getTags().getFoodGroup());


                for (int r = 0; r < foodsList.get(position).getAltMeasures().size(); r++) {
                    i.putExtra("measure" + r, foodsList.get(position).getAltMeasures().get(r).getMeasure());
                    i.putExtra("measureServingWeight" + r, foodsList.get(position).getAltMeasures().get(r).getServing_weight());
                }

                for (int r = 0; r < foodsList.get(position).getFullNutrients().size(); r++) {
                    i.putExtra("attrId" + r, foodsList.get(position).getFullNutrients().get(r).getAttrId());
                    i.putExtra("values" + r, foodsList.get(position).getFullNutrients().get(r).getValue());
                }

                mContext.startActivity(i);
            }
        });
    }


    @Override
    public int getItemCount() {
        return foodsList.size();
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


    public List<Foods> getData() {
        return foodsList;
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
