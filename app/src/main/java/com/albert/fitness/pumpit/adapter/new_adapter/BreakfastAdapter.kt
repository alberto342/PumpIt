package com.albert.fitness.pumpit.adapter.new_adapterimport

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.albert.fitness.pumpit.model.nutrition.Foods
import com.squareup.picasso.Picasso
import fitness.albert.com.pumpit.R
import kotlinx.android.synthetic.main.item_list_food_nutrition.view.*
import java.util.*

class BreakfastAdapter(private val interaction: Interaction? = null) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Foods>() {
        override fun areItemsTheSame(oldItem: Foods, newItem: Foods): Boolean {
            return oldItem.foodName == newItem.foodName
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Foods, newItem: Foods): Boolean {
            return  oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BreakfastViewModel(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.item_list_food_nutrition,
                        parent,
                        false
                ),
                interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BreakfastViewModel -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Foods>) {
        differ.submitList(list)
    }

    class BreakfastViewModel
    constructor(
            itemView: View,
            private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(item: Foods) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
            Picasso.get()
                    .load(item.photo.thumb)
                    .error(R.mipmap.ic_launcher)
                    .into(itemView.iv_food_img)

            itemView.tv_food_name.text = item.foodName
            itemView.tv_calories.text = String.format(Locale.getDefault(), "%.0f Kcal,  " +
                    "%.0f Carbs", item.nfCalories, item.nfTotalCarbohydrate)
            itemView.tv_protin.text = String.format(Locale.getDefault(), "%.0f Protein", item.nfProtein)
            itemView.tv_service_quantity.text = "Qty: " + item.servingQty
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Foods)
    }
}