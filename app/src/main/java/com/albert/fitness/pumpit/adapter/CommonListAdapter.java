package com.albert.fitness.pumpit.adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import com.albert.fitness.pumpit.model.Common;
import fitness.albert.com.pumpit.R;

public class CommonListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    ArrayList<Common> mListItem;
    private ClickListener clickListener;


    public CommonListAdapter(Context ctx, ArrayList<Common> mListItem) {
        this.mContext = ctx;
        this.mListItem = mListItem;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_food, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        bindViews((ViewHolder) holder, position);
    }




    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void itemClicked(View view, int position);
    }

    private void bindViews(final ViewHolder holder, final int position) {


        Log.d("TEst", "hle: " + mListItem.get(position).getFoodName());

//
//        Picasso.get()
//                .load(mListItem.get(position).getImage())
//                .placeholder(R.mipmap.ic_launcher)
//                .error(R.mipmap.ic_launcher)
//                .into(holder.ivImage);



//        Picasso.get()
//                .load(mListItem.get(position).getPhoto().getThumb())
//                .placeholder(R.mipmap.ic_launcher)
//                .error(R.mipmap.ic_launcher)
//                .into(holder.ivImage);


        holder.tvFoodName.setText(mListItem.get(position).getFoodName());
       // holder.tvServingQuantity.setText(mContext.getString(R.string.serving_qty) + " :- " + mListItem.get(position).getServingQty());
        //holder.tvServingUnit.setText(mContext.getString(R.string.serving_unit) + " :- " + mListItem.get(position).getServingUnit());
    }

    @Override
    public int getItemCount() {
        return 1;
//        return mListItem.size();
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
            tvFoodName = rowView.findViewById(R.id.tv_food_name);
            tvServingQuantity = rowView.findViewById(R.id.tv_qty);
            tvServingUnit = rowView.findViewById(R.id.tv_unit);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.itemClicked(view, getAdapterPosition());
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
