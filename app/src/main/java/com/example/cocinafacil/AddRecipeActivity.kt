package com.example.cocinafacil

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cocinafacil.databinding.ActivityAddRecipeBinding
import com.example.cocinafacil.db.RecipeDbHelper
import com.example.cocinafacil.models.Recipe

class AddRecipeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddRecipeBinding
    private lateinit var db: RecipeDbHelper

    private var selectedImageUri: Uri? = null
    private val PICK_IMAGE_REQUEST = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = RecipeDbHelper(this)

        // Clic en ImageView para abrir galería
        binding.ivRecipeImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString().trim()
            val ing = binding.etIngredients.text.toString().trim()
            val ins = binding.etInstructions.text.toString().trim()

            if (title.isEmpty()) {
                binding.tilTitle.error = "El título es obligatorio"
                return@setOnClickListener
            }

            val recipe = Recipe(
                title = title,
                ingredients = ing,
                instructions = ins,
                image = selectedImageUri?.toString() // Guardamos la URI de la imagen
            )

            val id = db.insert(recipe)
            recipe.id = id

            // Volvemos a MainActivity
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    // Recogemos el resultado de la galería
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                selectedImageUri = uri
                binding.ivRecipeImage.setImageURI(uri) // Mostramos la imagen
            }
        }
    }
}
