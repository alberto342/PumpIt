package fitness.albert.com.pumpit.Adapter;

import android.app.Activity;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fitness.albert.com.pumpit.Model.Foods;
import fitness.albert.com.pumpit.R;
import fitness.albert.com.pumpit.ShowFoodBeforeAddedActivity;

public class FoodListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    public static ArrayList<Foods> mListItem;
    public static int mItemPosition;
    private ClickListener clickListener;
    private final String TAG = "FoodListAdapter";
    public static List<String> measure = new ArrayList<>();
    public static Map<String, Float> measureMap = new HashMap<>();

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
                getNutrition(position);
                context.startActivity(new Intent(context, ShowFoodBeforeAddedActivity.class));
                ((Activity) mContext).finish();
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


    private void getNutrition(final int position) {

        Log.d(TAG, "onClick: clicked on: " + mListItem.get(position));

        mItemPosition = position;

        if (mListItem.get(position).getAlt_measures().size() == 0) {
            try {
                mListItem.get(position).getAlt_measures().get(0).setServing_weight("1");
                mListItem.get(position).getAlt_measures().get(0).setMeasure("package");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        for (int i = 0; i < mListItem.get(position).getAlt_measures().size(); i++) {
            measure.add(String.valueOf(mListItem.get(position).getAlt_measures().get(i).getMeasure()));
        }


        //remove existed
        if(!mListItem.get(position).getAlt_measures().isEmpty()) {
            if(String.valueOf(mListItem.get(position).getServing_weight_grams()) != mListItem.get(position).getAlt_measures().get(position).getServing_weight()) {
                String itemOne = measure.get(0);
                measure.remove(0);
                measure.add(itemOne);
            }
        }


        for (int i = 1; i < measure.size(); i++) {
            if (measure.get(0).equals(measure.get(i))) {
                measure.remove(i);
            }
        }

        for (int i = 0; i < mListItem.get(position).getAlt_measures().size(); i++) {
            measureMap.put(mListItem.get(position).getAlt_measures().get(i).getMeasure(), Float.valueOf(mListItem.get(position).getAlt_measures().get(i).getServing_weight()));
        }
    }

    @Override
    public long getItemId(int position) {
        //Auto-generated method stub
        return position;
    }


}
