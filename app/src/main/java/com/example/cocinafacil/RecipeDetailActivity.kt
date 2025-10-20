package com.example.cocinafacil

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cocinafacil.databinding.ActivityRecipeDetailBinding
import com.example.cocinafacil.models.Recipe


class RecipeDetailActivity : AppCompatActivity() {


    private lateinit var binding: ActivityRecipeDetailBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


// Recuperamos extra enviado desde MainActivity (paso de parámetro entre actividades)
        val recipe = intent.getParcelableExtra<Recipe>("recipe")
        recipe?.let { r ->
            binding.tvDetailTitle.text = r.title
            binding.tvDetailIngredients.text = r.ingredients
            binding.tvDetailInstructions.text = r.instructions
        }
    }
}