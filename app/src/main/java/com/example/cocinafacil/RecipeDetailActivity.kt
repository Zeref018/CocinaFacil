package com.example.cocinafacil

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.cocinafacil.databinding.ActivityRecipeDetailBinding
import com.example.cocinafacil.db.RecipeDbHelper
import com.example.cocinafacil.models.Recipe

class RecipeDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecipeDetailBinding
    private lateinit var db: RecipeDbHelper
    private var recipe: Recipe? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Forzar iconos oscuros en la barra de estado
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.WHITE

        db = RecipeDbHelper(this)

        recipe = intent.getParcelableExtra("recipe")

        recipe?.let { r ->
            binding.tvTitle.text = r.title
            binding.tvIngredients.text = r.ingredients
            binding.tvInstructions.text = r.instructions

            val uri = r.image?.let { Uri.parse(it) }
            Glide.with(this)
                .load(uri)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
                .into(binding.ivRecipeImage)

            // Mostrar u ocultar botones solo si la receta es local
            if (r.id > 0) {
                binding.layoutButtons.visibility = View.VISIBLE
            } else {
                binding.layoutButtons.visibility = View.GONE
            }
        }

        binding.btnBack.setOnClickListener { finish() }

        // Botón borrar receta local
        binding.btnDelete.setOnClickListener {
            recipe?.let { r ->
                AlertDialog.Builder(this)
                    .setTitle("Eliminar receta")
                    .setMessage("¿Seguro que quieres eliminar \"${r.title}\"?")
                    .setPositiveButton("Sí") { _, _ ->
                        db.delete(r.id)
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()
            }
        }

        // Botón editar receta
        binding.btnEdit.setOnClickListener {
            recipe?.let { r ->
                val intent = Intent(this, AddRecipeActivity::class.java).apply {
                    putExtra("recipeToEdit", r)
                }
                startActivity(intent)
            }
        }
    }
}
