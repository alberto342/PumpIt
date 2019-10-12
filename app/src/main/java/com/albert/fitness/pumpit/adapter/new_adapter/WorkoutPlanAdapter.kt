package com.albert.fitness.pumpit.adapter.new_adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.albert.fitness.pumpit.model.WorkoutPlanObj
import com.bumptech.glide.Glide
import fitness.albert.com.pumpit.R
import kotlinx.android.synthetic.main.item_list_workout_plans.view.*

class WorkoutPlanAdapter(private val interaction: Interaction? = null) :
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
        return WorkoutPlanViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.item_list_workout_plans,
                        parent,
                        false
                ),
                interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is WorkoutPlanViewHolder -> {
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

    class WorkoutPlanViewHolder
    constructor(
            itemView: View,
            private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: WorkoutPlanObj) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)

                itemView.tv_workout_plan_name.text = item.routineName
                itemView.tv_general.text = item.daysWeek

                getRoutineType(itemView, item)
            }
        }

        private fun getRoutineType(itemView: View, item: WorkoutPlanObj) {
            when (item.routineType) {
                "General Fitness" ->
                    Glide.with(itemView.context)
                            .load(R.mipmap.ic_general_fitness)
                            .into(itemView.iv_plan_icon)
                "Bulking" ->
                    Glide.with(itemView.context)
                            .load(R.mipmap.ic_bulking)
                            .into(itemView.iv_plan_icon)
                "Cutting" ->
                    Glide.with(itemView.context)
                            .load(R.mipmap.ic_scale)
                            .into(itemView.iv_plan_icon)
                "Sport Specific" ->
                    Glide.with(itemView.context)
                            .load(R.mipmap.ic_sport_specific)
                            .into(itemView.iv_plan_icon)
            }
        }
    }


    interface Interaction {
        fun onItemSelected(position: Int, item: WorkoutPlanObj)
    }
}