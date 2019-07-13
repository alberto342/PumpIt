package fitness.albert.com.pumpit.Adapter;

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
    public static List<String> measure = new ArrayList<>();
    public static Map<String, Float> measureMap = new HashMap<>();

    public FoodListAdapter(Context ctx, ArrayList<Foods> mListItem) {
        this.mContext = ctx;
        this.mListItem = mListItem;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_food, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        bindViews((ViewHolder) holder, position);
    }


//    public void setClickListener(ClickListener clickListener) {
//        this.clickListener = clickListener;
//    }

    public interface ClickListener {
        void itemClicked(View view, int position);
    }

    private void bindViews(final ViewHolder holder, final int position) {

        Picasso.get()
                .load(mListItem.get(position).getPhoto().getThumb())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.ivImage);

        holder.tvFoodName.setText(mListItem.get(position).getFoodName());
        holder.tvCalories.setText(mContext.getString(R.string.calories) + ": " + mListItem.get(position).getNfCalories());
        holder.tvServingQuantity.setText(mContext.getString(R.string.servingQty) + ": " + mListItem.get(position).getServingQty());
        holder.tvServingUnit.setText(mContext.getString(R.string.servingUnit) + ": " + mListItem.get(position).getServingUnit());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                getNutrition(position);
                context.startActivity(new Intent(context, ShowFoodBeforeAddedActivity.class));
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

        public ViewHolder(View rowView) {
            super(rowView);
            rowView.setOnClickListener(this);

            ivImage = rowView.findViewById(R.id.ivImage);
            tvFoodName = rowView.findViewById(R.id.tvFoodName);
            tvCalories = rowView.findViewById(R.id.tvCalories);
            tvServingQuantity = rowView.findViewById(R.id.tvServingQuantity);
            tvServingUnit = rowView.findViewById(R.id.tvServingUnit);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.itemClicked(view, getAdapterPosition());
            }
        }
    }


    private void getNutrition(final int position) {

        String TAG = "FoodListAdapter";
        Log.d(TAG, "onClick: clicked on: " + mListItem.get(position));

        mItemPosition = position;

        if (mListItem.get(position).getAltMeasures().size() == 0) {
            try {
                mListItem.get(position).getAltMeasures().get(0).setServing_weight("1");
                mListItem.get(position).getAltMeasures().get(0).setMeasure("package");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < mListItem.get(position).getAltMeasures().size(); i++) {
            measure.add(String.valueOf(mListItem.get(position).getAltMeasures().get(i).getMeasure()));
        }


        //remove existed
        if(!mListItem.get(position).getAltMeasures().isEmpty()) {
            if(!String.valueOf(mListItem.get(position).getServingWeightGrams()).equals(mListItem.get(position).getAltMeasures().get(position).getServing_weight())) {
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

        for (int i = 0; i < mListItem.get(position).getAltMeasures().size(); i++) {
            measureMap.put(mListItem.get(position).getAltMeasures().get(i).getMeasure(), Float.valueOf(mListItem.get(position).getAltMeasures().get(i).getServing_weight()));
        }
    }

    @Override
    public long getItemId(int position) {
        //Auto-generated method stub
        return position;
    }


}
