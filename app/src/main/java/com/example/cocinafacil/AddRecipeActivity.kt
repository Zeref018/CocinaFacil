package com.example.cocinafacil

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cocinafacil.databinding.ActivityAddRecipeBinding
import com.example.cocinafacil.db.RecipeDbHelper
import com.example.cocinafacil.models.Recipe
import android.graphics.Color
import android.view.View

class AddRecipeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddRecipeBinding
    private lateinit var db: RecipeDbHelper

    private var selectedImageUri: Uri? = null
    private val PICK_IMAGE_REQUEST = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Forzar iconos oscuros en la barra de estado
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        // Poner fondo blanco en la barra de estado
        window.statusBarColor = Color.WHITE

        db = RecipeDbHelper(this)

        binding.btnBack.setOnClickListener {
            finish()
        }

        // Clic en ImageView para abrir galería
        binding.ivRecipeImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
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
                image = selectedImageUri?.toString()
            )

            val id = db.insert(recipe)
            recipe.id = id

            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                selectedImageUri = uri

                // 🔒 Mantiene permiso de lectura incluso tras reiniciar la app
                contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )

                binding.ivRecipeImage.setImageURI(uri)
            }
        }
    }
}
