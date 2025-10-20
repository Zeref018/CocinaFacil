package com.example.cocinafacil

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cocinafacil.adapter.RecipeAdapter
import com.example.cocinafacil.databinding.ActivityMainBinding
import com.example.cocinafacil.db.RecipeDbHelper
import com.example.cocinafacil.models.Recipe
import com.example.cocinafacil.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: RecipeAdapter
    private lateinit var db: RecipeDbHelper
    private lateinit var session: SessionManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setSupportActionBar(binding.topAppBar)


        session = SessionManager(this)
// Guardamos un valor de sesión por demo
        session.setLoggedUser("UsuarioDemonstracion")


        db = RecipeDbHelper(this)


        adapter = RecipeAdapter(mutableListOf()) { recipe ->
// Manejar click (lambda). Pasamos la receta a la activity detalle como extra.
            val i = Intent(this, RecipeDetailActivity::class.java)
            i.putExtra("recipe", recipe)
            startActivity(i)
        }


        binding.rvRecipes.layoutManager = LinearLayoutManager(this)
        binding.rvRecipes.adapter = adapter


        binding.fabAdd.setOnClickListener {
            val i = Intent(this, AddRecipeActivity::class.java)
            startActivity(i)
        }


        binding.topAppBar.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.action_add -> {
                    startActivity(Intent(this, AddRecipeActivity::class.java))
                    true
                }
                R.id.action_settings -> {
// Mostramos un AlertDialog como requisito
                    AlertDialog.Builder(this)
                        .setTitle("Sesión")
                        .setMessage("Usuario: ${session.getLoggedUser()}")
                        .setPositiveButton(getString(R.string.ok), null)
                        .show()
                    true
                }
                else -> false
            }
        }


// Cargar recetas: primero desde DB local, luego API
        loadLocal()
        fetchFromApi()
    }


    private fun loadLocal() {
        val local = db.getAll()
        if (local.isNotEmpty()) adapter.setData(local)
    }


    private fun fetchFromApi() {
// Ejemplo con Retrofit (Call en hilo background manejado por Retrofit)
        val call = RetrofitClient.instance.getRecipes()
        call.enqueue(object: Callback<List<Recipe>> {
            override fun onResponse(call: Call<List<Recipe>>, response: Response<List<Recipe>>) {
                if (response.isSuccessful) {
                    val list = response.body() ?: emptyList()
// Si API devuelve, actualizamos RecyclerView
                    if (list.isNotEmpty()) adapter.setData(list)
                }
            }


            override fun onFailure(call: Call<List<Recipe>>, t: Throwable) {
// Podríamos mostrar un mensaje o log
            }
        })
    }


    override fun onResume() {
        super.onResume()
// Si venimos de crear receta, recargamos local
        loadLocal()
    }
}