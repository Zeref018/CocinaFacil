package com.example.cocinafacil

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cocinafacil.databinding.ActivityRecipeDetailBinding
import com.example.cocinafacil.models.Recipe
import com.bumptech.glide.Glide
import android.graphics.Color
import android.view.View


class RecipeDetailActivity : AppCompatActivity() {


    private lateinit var binding: ActivityRecipeDetailBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Forzar iconos oscuros en la barra de estado
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        // Poner fondo blanco en la barra de estado
        window.statusBarColor = Color.WHITE

        // Botón "Atrás" personalizado
        binding.btnBack.setOnClickListener {
            finish() // cierra la activity y vuelve a la anterior
        }

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