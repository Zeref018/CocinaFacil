package com.example.cocinafacil

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cocinafacil.databinding.ActivityRecipeDetailBinding
import com.example.cocinafacil.models.Recipe
import com.bumptech.glide.Glide



class RecipeDetailActivity : AppCompatActivity() {


    private lateinit var binding: ActivityRecipeDetailBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


// Recuperamos extra enviado desde MainActivity (paso de parámetro entre actividades)
        val recipe = intent.getParcelableExtra<Recipe>("recipe")!!

        binding.tvTitle.text = recipe.title
        binding.tvIngredients.text = recipe.ingredients
        binding.tvInstructions.text = recipe.instructions

        Glide.with(this)
            .load(recipe.image)
            .placeholder(R.drawable.ic_launcher_background)
            .into(binding.ivRecipeImage)

// Botón atrás en la barra
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }
}