package com.example.cocinafacil

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cocinafacil.adapter.RecipeAdapter
import com.example.cocinafacil.databinding.ActivityMainBinding
import com.example.cocinafacil.db.RecipeDbHelper
import com.example.cocinafacil.models.Recipe
import com.example.cocinafacil.network.RetrofitClient
import com.example.cocinafacil.network.RecipeResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.graphics.Color
import android.view.View

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: RecipeAdapter
    private lateinit var db: RecipeDbHelper
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Forzar iconos oscuros en la barra de estado
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        // Poner fondo blanco en la barra de estado
        window.statusBarColor = Color.WHITE

        setSupportActionBar(binding.topAppBar)

        session = SessionManager(this)
        session.setLoggedUser("UsuarioDemostracion")

        db = RecipeDbHelper(this)

        adapter = RecipeAdapter(mutableListOf()) { recipe ->
            val i = Intent(this, RecipeDetailActivity::class.java)
            i.putExtra("recipe", recipe)
            startActivity(i)
        }

        binding.rvRecipes.layoutManager = LinearLayoutManager(this)
        binding.rvRecipes.adapter = adapter

        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, AddRecipeActivity::class.java))
        }

        loadLocal()
        fetchFromApi()
    }

    // Inflar menú con lupa y demás
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)

        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as? androidx.appcompat.widget.SearchView

        searchView?.queryHint = "Buscar receta"
        searchView?.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })

        return true
    }

    // Gestionar clicks de menú
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                startActivity(Intent(this, AddRecipeActivity::class.java))
                true
            }
            R.id.action_settings -> {
                AlertDialog.Builder(this)
                    .setTitle("Sesión")
                    .setMessage("Usuario: ${session.getLoggedUser()}")
                    .setPositiveButton(getString(R.string.ok), null)
                    .show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loadLocal() {
        val local = db.getAll()
        if (local.isNotEmpty()) adapter.setData(local)
    }

    private fun fetchFromApi() {
        val call: Call<RecipeResponse> = RetrofitClient.instance.getRecipes()
        call.enqueue(object : Callback<RecipeResponse> {
            override fun onResponse(call: Call<RecipeResponse>, response: Response<RecipeResponse>) {
                if (response.isSuccessful) {
                    val apiRecipes: List<Recipe> = response.body()?.recipes?.map { it.toRecipe() } ?: emptyList()
                    if (apiRecipes.isNotEmpty()) adapter.setData(apiRecipes)
                }
            }

            override fun onFailure(call: Call<RecipeResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        loadLocal()
    }
}
