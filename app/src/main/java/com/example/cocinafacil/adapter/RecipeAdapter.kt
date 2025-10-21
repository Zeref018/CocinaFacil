package com.example.cocinafacil.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cocinafacil.R
import com.example.cocinafacil.models.Recipe

class RecipeAdapter(
    private var recipes: MutableList<Recipe>,
    private val onClick: (Recipe) -> Unit
) : RecyclerView.Adapter<RecipeAdapter.Holder>(), Filterable {

    private var recipesFull: List<Recipe> = ArrayList(recipes)

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvIngredients: TextView = itemView.findViewById(R.id.tvIngredients)
        val ivRecipe: ImageView = itemView.findViewById(R.id.ivRecipe)

        fun bind(recipe: Recipe) {
            tvTitle.text = recipe.title
            tvIngredients.text = recipe.ingredients // ya es String
            Glide.with(itemView.context)
                .load(recipe.image)
                .placeholder(R.drawable.ic_placeholder)
                .into(ivRecipe)

            itemView.setOnClickListener {
                onClick(recipe)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recipe, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(recipes[position])
    }

    override fun getItemCount(): Int = recipes.size

    fun setData(newList: List<Recipe>) {
        recipes = newList.toMutableList()
        recipesFull = ArrayList(newList)
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = if (constraint == null || constraint.isEmpty()) {
                    recipesFull
                } else {
                    val filterPattern = constraint.toString().lowercase().trim()
                    recipesFull.filter {
                        it.title.lowercase().contains(filterPattern)
                    }
                }
                val results = FilterResults()
                results.values = filteredList
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                recipes = (results?.values as? List<Recipe>)?.toMutableList() ?: mutableListOf()
                notifyDataSetChanged()
            }
        }
    }
}
