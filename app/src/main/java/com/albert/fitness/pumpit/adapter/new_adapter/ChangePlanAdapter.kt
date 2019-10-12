package com.albert.fitness.pumpit.adapter.new_adapterimport

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.albert.fitness.pumpit.model.WorkoutPlanObj
import fitness.albert.com.pumpit.R
import kotlinx.android.synthetic.main.item_list_change_plan.view.*

class ChangePlanAdapter(private val interaction: Interaction? = null) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<WorkoutPlanObj>() {
        override fun areItemsTheSame(oldItem: WorkoutPlanObj, newItem: WorkoutPlanObj): Boolean {
            TODO("not implemented")
        }

        override fun areContentsTheSame(oldItem: WorkoutPlanObj, newItem: WorkoutPlanObj): Boolean {
            TODO("not implemented")
        }
    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ChangePlanViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.item_list_change_plan,
                        parent,
                        false
                ),
                interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ChangePlanViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<WorkoutPlanObj>) {
        differ.submitList(list)
    }

    class ChangePlanViewHolder
    constructor(
            itemView: View,
            private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: WorkoutPlanObj) = with(itemView) {
            itemView.setOnClickListener {
                itemView.change_selected_plan.setImageResource(R.mipmap.ic_ok_selected)
                interaction?.onItemSelected(adapterPosition, item)
            }
            itemView.tv_change_plan_name.text = item.routineName
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: WorkoutPlanObj)
    }
}