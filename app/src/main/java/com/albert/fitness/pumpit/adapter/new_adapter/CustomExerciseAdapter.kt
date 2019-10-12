package com.albert.fitness.pumpit.adapter.new_adapterimport

import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.albert.fitness.pumpit.model.CustomExerciseName
import com.bumptech.glide.Glide
import fitness.albert.com.pumpit.R
import kotlinx.android.synthetic.main.item_list_custom_exercise.view.*

class CustomExerciseAdapter(private val interaction: Interaction? = null) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CustomExerciseName>() {
        override fun areItemsTheSame(oldItem: CustomExerciseName, newItem: CustomExerciseName): Boolean {
            TODO("not implemented")
        }

        override fun areContentsTheSame(oldItem: CustomExerciseName, newItem: CustomExerciseName): Boolean {
            TODO("not implemented")
        }
    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CustomExerciseViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.item_list_custom_exercise,
                        parent,
                        false
                ),
                interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CustomExerciseViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<CustomExerciseName>) {
        differ.submitList(list)
    }

    class CustomExerciseViewHolder
    constructor(
            itemView: View,
            private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: CustomExerciseName) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
            val patch =  item.path
            val fileName =  item.file_name
            val bitCustom = MediaStore.Images.Media.getBitmap(itemView.context.contentResolver, Uri.parse(patch + fileName))

            Glide.with(itemView.context)
                    .load(bitCustom)
                    .into(itemView.cus_exercise_img)

            itemView.tv_cus_exercise_name.text = item.exercise_name
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: CustomExerciseName)
    }
}