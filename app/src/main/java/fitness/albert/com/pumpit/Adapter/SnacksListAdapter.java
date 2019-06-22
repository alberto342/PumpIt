package fitness.albert.com.pumpit.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import fitness.albert.com.pumpit.Model.Foods;
import fitness.albert.com.pumpit.R;
import fitness.albert.com.pumpit.ShowAllNutritionActivity;
import fitness.albert.com.pumpit.ShowSnackActivity;

public class SnacksListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Foods> foodsList;
    private final String TAG = "SnacksListAdapter";
    public static String foodName;


    public SnacksListAdapter(Context mContext, List<Foods> foodsList) {
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

        holder.tvFoodName.setText(foodsList.get(position).getFood_name());
        holder.tvCalories.setText(String.format(Locale.getDefault(), "%.0f Kcal,  %.0f Carbs", foodsList.get(position).getNf_calories(), foodsList.get(position).getNf_total_carbohydrate()));
        holder.tvProtein.setText(String.format(Locale.getDefault(), "%.0f Protein", foodsList.get(position).getNf_protein()));
        holder.tvServiceQty.setText("Qty: " + foodsList.get(position).getServing_qty());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on: " + holder.tvFoodName.getText().toString());

                foodName = foodsList.get(position).getFood_name();

                String cotxt = mContext.toString();
                String[] parts = cotxt.split("@");
                String part1 = parts[0];

                //refresh page
                if (!part1.equals("fitness.albert.com.pumpit.fragment.logsFragment.LogTabActivity")) {
                    mContext.startActivity(new Intent(mContext, ShowAllNutritionActivity.class));
                    ((ShowAllNutritionActivity) mContext).finish();
                }


                Intent i = new Intent(mContext, ShowSnackActivity.class);
                i.putExtra("foodPhoto", foodsList.get(position).getPhoto().getHighres());
                i.putExtra("kcal", foodsList.get(position).getNf_calories());
                i.putExtra("fat", foodsList.get(position).getNf_total_fat());
                i.putExtra("protein", foodsList.get(position).getNf_protein());
                i.putExtra("carbohydrate", foodsList.get(position).getNf_total_carbohydrate());
                i.putExtra("servingWeightGrams", foodsList.get(position).getServing_weight_grams());
                i.putExtra("qty", foodsList.get(position).getServing_qty());
                i.putExtra("servingUnit", foodsList.get(position).getServing_unit());
                i.putExtra("altMeasuresSize", foodsList.get(position).getAlt_measures().size());


                for (int r = 0; r < foodsList.get(position).getAlt_measures().size(); r++) {
                    i.putExtra("measure" + r, foodsList.get(position).getAlt_measures().get(r).getMeasure());
                    i.putExtra("measureServingWeight" + r, foodsList.get(position).getAlt_measures().get(r).getServing_weight());
                }
                mContext.startActivity(i);

//                Intent intent = new Intent(mContext, ShowSnackActivity.class);
//                mContext.startActivity(intent);
//                foodsList.clear();
//                ((Activity) mContext).finish();
            }
        });
    }




    @Override
    public int getItemCount() {
        return foodsList.size();
    }


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

