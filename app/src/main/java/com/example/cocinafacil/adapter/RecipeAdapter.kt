package com.example.cocinafacil.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cocinafacil.databinding.ItemRecipeBinding
import com.example.cocinafacil.models.Recipe


class RecipeAdapter(
    private val items: MutableList<Recipe>,
    private val onClick: (Recipe) -> Unit
) : RecyclerView.Adapter<RecipeAdapter.Holder>() {


    inner class Holder(val binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(r: Recipe) {
            binding.tvTitle.text = r.title
            binding.tvSummary.text = r.ingredients.take(80)
            binding.root.setOnClickListener { onClick(r) } // lambda click
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }


    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(items[position])
    }


    override fun getItemCount(): Int = items.size


    fun setData(list: List<Recipe>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }


    fun add(recipe: Recipe) {
        items.add(0, recipe)
        notifyItemInserted(0)
    }
}