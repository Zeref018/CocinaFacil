package com.example.cocinafacil.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cocinafacil.R
import com.example.cocinafacil.databinding.ItemRecipeBinding
import com.example.cocinafacil.models.Recipe

class RecipeAdapter(
    private var recipes: MutableList<Recipe>,
    private val onClick: (Recipe) -> Unit
) : RecyclerView.Adapter<RecipeAdapter.Holder>() {

    inner class Holder(private val binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: Recipe) {
            binding.tvTitle.text = recipe.title
            binding.tvIngredients.text = recipe.ingredients
            // Cargamos imagen con Glide
            Glide.with(binding.root)
                .load(recipe.image)
                .placeholder(R.drawable.ic_launcher_foreground) // placeholder funcional
                .into(binding.ivRecipe)

            binding.root.setOnClickListener { onClick(recipe) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(recipes[position])
    }

    override fun getItemCount(): Int = recipes.size

    fun setData(newList: List<Recipe>) {
        recipes.clear()
        recipes.addAll(newList)
        notifyDataSetChanged()
    }
}
