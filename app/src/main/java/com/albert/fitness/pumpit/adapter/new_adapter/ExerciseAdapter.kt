package com.albert.fitness.pumpit.adapterimport

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.albert.fitness.pumpit.helper.BitmapFromAssent
import com.albert.fitness.pumpit.model.Exercise
import com.bumptech.glide.Glide
import fitness.albert.com.pumpit.R
import kotlinx.android.synthetic.main.item_list_exercise.view.*

class ExerciseAdapter(private val interaction: Interaction? = null) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Exercise>() {
        override fun areItemsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
            return  oldItem.exerciseId == newItem.exerciseId
        }

        override fun areContentsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ExerciseViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.item_list_exercise,
                        parent,
                        false
                ),
                interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ExerciseViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Exercise>) {
        differ.submitList(list)
    }

    class ExerciseViewHolder
    constructor(
            itemView: View,
            private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Exercise) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
            val bitmap = BitmapFromAssent.getBitmapFromAsset(itemView.context,item.imgName)
                        Glide.with(itemView.context)
                   .load(bitmap)
                  .into(itemView.exercise_img)

            itemView.tv_exercise_name.text = item.exerciseName
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Exercise)
    }
}