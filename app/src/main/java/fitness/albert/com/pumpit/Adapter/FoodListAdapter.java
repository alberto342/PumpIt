package fitness.albert.com.pumpit.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import fitness.albert.com.pumpit.Model.Foods;
import fitness.albert.com.pumpit.R;
import fitness.albert.com.pumpit.ShowFoodBeforeAddedActivity;

public class FoodListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    public static ArrayList<Foods> mListItem;
    private ClickListener clickListener;
    private String TAG;


    public FoodListAdapter(Context ctx, ArrayList<Foods> mListItem) {
        this.mContext = ctx;
        this.mListItem = mListItem;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_food, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindViews((ViewHolder) holder, position);
    }



    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;

    }

    public interface ClickListener {
        void itemClicked(View view, int position);

    }

    private void bindViews(final ViewHolder holder, final int position) {

        Picasso.get()
                .load(mListItem.get(position).getPhoto().getThumb())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.ivImage);

        holder.tvFoodName.setText(mListItem.get(position).getFood_name());
        holder.tvCalories.setText(mContext.getString(R.string.calories) + ": " + mListItem.get(position).getNf_calories());
        holder.tvServingQuantity.setText(mContext.getString(R.string.serving_qty) + ": " + mListItem.get(position).getServing_qty());
        holder.tvServingUnit.setText(mContext.getString(R.string.serving_unit) + ": " + mListItem.get(position).getServing_unit());
        holder.llSelectedItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, ShowFoodBeforeAddedActivity.class);
                putExtra(intent, position);
                context.startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return mListItem.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView ivImage;
        private TextView tvFoodName;
        private TextView tvCalories;
        private TextView tvServingQuantity;
        private TextView tvServingUnit;
        private LinearLayout llSelectedItem;


        public ViewHolder(View rowView) {
            super(rowView);
            rowView.setOnClickListener(this);

            ivImage = rowView.findViewById(R.id.ivImage);
            tvFoodName = rowView.findViewById(R.id.tvFoodName);
            tvCalories = rowView.findViewById(R.id.tvCalories);
            tvServingQuantity = rowView.findViewById(R.id.tvServingQuantity);
            tvServingUnit = rowView.findViewById(R.id.tvServingUnit);
            llSelectedItem = rowView.findViewById(R.id.item_food_selected);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.itemClicked(view, getAdapterPosition());
            }
        }
    }



    private void putExtra (Intent intent, final int position) {

        Log.d(TAG, "onClick: clicked on: " + mListItem.get(position));
        intent.putExtra("food_name", mListItem.get(position).getFood_name());
        intent.putExtra("nf_calories", mListItem.get(position).getNf_calories());
        intent.putExtra("serving_qty", mListItem.get(position).getServing_qty());
        intent.putExtra("serving_unit", mListItem.get(position).getServing_unit());
        intent.putExtra("nf_total_fat", mListItem.get(position).getNf_total_fat());
        intent.putExtra("serving_weight_grams", mListItem.get(position).getServing_weight_grams());
        intent.putExtra("nf_cholesterol", mListItem.get(position).getNf_cholesterol());
        intent.putExtra("nf_saturated_fat", mListItem.get(position).getNf_saturated_fat());
        intent.putExtra("nf_sodium", mListItem.get(position).getNf_sodium());
        intent.putExtra("nf_total_carbohydrate", mListItem.get(position).getNf_total_carbohydrate());
        intent.putExtra("nf_dietary_fiber", mListItem.get(position).getNf_dietary_fiber());
        intent.putExtra("nf_sugars", mListItem.get(position).getNf_sugars());
        intent.putExtra("nf_protein", mListItem.get(position).getNf_protein());
        intent.putExtra("nf_potassium", mListItem.get(position).getNf_potassium());
        intent.putExtra("nf_p", mListItem.get(position).getNf_p());
        intent.putExtra("thumb", mListItem.get(position).getPhoto().getThumb());
        intent.putExtra("photoHighres", mListItem.get(position).getPhoto().getHighres());
        mContext.startActivity(intent);
    }

    @Override
    public long getItemId(int position) {
        //Auto-generated method stub
        return position;
    }
}
