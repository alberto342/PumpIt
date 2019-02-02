package fitness.albert.com.pumpit.Adapter;

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

import com.squareup.picasso.Picasso;

import java.util.List;

import fitness.albert.com.pumpit.Model.Foods;
import fitness.albert.com.pumpit.R;
import fitness.albert.com.pumpit.ShowItemFoodActivity;

public class FirestoreFoodListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    Context mContext;
    private List<Foods> foodsList;
    private String TAG;

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


    private void bindViews(final ViewHolder holder, int position) {

        Picasso.get()
                .load(foodsList.get(position).getThumb())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.ivImage);


        holder.tvFoodName.setText(foodsList.get(position).getFood_name());
        holder.tvCalories.setText(String.valueOf(foodsList.get(position).getNf_calories() + " Kcal, "
                + foodsList.get(position).getNf_total_carbohydrate() + " Carb"));
        holder.tvProtein.setText(foodsList.get(position).getNf_protein()
                + " Protein");
        holder.tvServiceQty.setText("Qty: " + foodsList.get(position).getServing_qty());

        holder.itemFoodSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on: " + holder.tvFoodName.getText().toString());

                Intent intent = new Intent(mContext, ShowItemFoodActivity.class);

                intent.putExtra("foodName", holder.tvFoodName.getText().toString());
                mContext.startActivity(intent);
                foodsList.clear();
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
