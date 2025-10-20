package com.example.cocinafacil

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cocinafacil.databinding.ActivityAddRecipeBinding
import com.example.cocinafacil.db.RecipeDbHelper
import com.example.cocinafacil.models.Recipe


class AddRecipeActivity : AppCompatActivity() {


    private lateinit var binding: ActivityAddRecipeBinding
    private lateinit var db: RecipeDbHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        db = RecipeDbHelper(this)


        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString().trim()
            val ing = binding.etIngredients.text.toString().trim()
            val ins = binding.etInstructions.text.toString().trim()


            if (title.isEmpty()) {
                binding.tilTitle.error = "El título es obligatorio"
                return@setOnClickListener
            }


            val r = Recipe(title = title, ingredients = ing, instructions = ins)
            val id = db.insert(r)
            r.id = id


// Almacenamos también en sesión un valor (ej. última receta creada)
            val session = SessionManager(this)
            session.setLoggedUser("Último:${r.title}")


// Mostramos un AlertDialog confirmando
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle(getString(R.string.alert_saved_title))
                .setMessage(getString(R.string.alert_saved_message))
                .setPositiveButton(getString(R.string.ok)) { dlg, _ ->
                    dlg.dismiss()
// Volvemos al Main
                    setResult(Activity.RESULT_OK)
                    finish()
                }
                .show()
        }
    }
}