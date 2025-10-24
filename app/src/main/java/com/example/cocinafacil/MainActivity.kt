package com.example.cocinafacil

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cocinafacil.adapter.RecipeAdapter
import com.example.cocinafacil.databinding.ActivityMainBinding
import com.example.cocinafacil.db.RecipeDbHelper
import com.example.cocinafacil.models.Recipe
import com.example.cocinafacil.network.RecipeResponse
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

        // Iconos oscuros en la barra de estado (modo claro)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.WHITE

        setSupportActionBar(binding.topAppBar)

        // Inicializar sesión
        session = SessionManager(this)
        if (!session.isLoggedIn()) {
            session.setLoggedUser("UsuarioDemostracion")
        }

        // Mostrar usuario actual en la AppBar
        binding.topAppBar.subtitle = session.getLoggedUser()

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

        loadRecipes()
    }

    override fun onResume() {
        super.onResume()
        loadRecipes() // recargar todo al volver
    }

    /** Carga recetas locales + API */
    private fun loadRecipes() {
        val local = db.getAll().toMutableList()

        val call: Call<RecipeResponse> = RetrofitClient.instance.getRecipes()
        call.enqueue(object : Callback<RecipeResponse> {
            override fun onResponse(call: Call<RecipeResponse>, response: Response<RecipeResponse>) {
                if (response.isSuccessful) {
                    val apiRecipes = response.body()?.recipes?.map { it.toRecipe() } ?: emptyList()
                    val combined = apiRecipes + local
                    adapter.setData(combined)
                } else {
                    adapter.setData(local)
                }
            }

            override fun onFailure(call: Call<RecipeResponse>, t: Throwable) {
                t.printStackTrace()
                adapter.setData(local)
            }
        })
    }

    /** Inflar menú con la lupa y ajustes */
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

    /** Manejo de opciones del menú */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                startActivity(Intent(this, AddRecipeActivity::class.java))
                true
            }

            R.id.action_settings -> {
                showSessionDialog()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    /** Diálogo para ver/cambiar usuario o cerrar sesión */
    private fun showSessionDialog() {
        val currentUser = session.getLoggedUser() ?: "Ninguno"

        AlertDialog.Builder(this)
            .setTitle("Sesión")
            .setMessage("Usuario actual: $currentUser")
            .setPositiveButton("Cambiar usuario") { _, _ ->
                val input = EditText(this)
                input.hint = "Nuevo nombre de usuario"

                AlertDialog.Builder(this)
                    .setTitle("Cambiar usuario")
                    .setView(input)
                    .setPositiveButton("Guardar") { _, _ ->
                        val newUser = input.text.toString().trim()
                        if (newUser.isNotEmpty()) {
                            session.setLoggedUser(newUser)
                            binding.topAppBar.subtitle = newUser
                            Toast.makeText(this, "Usuario cambiado a $newUser", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Nombre vacío. No guardado.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()
            }
            .setNeutralButton("Cerrar sesión") { _, _ ->
                session.logout()
                binding.topAppBar.subtitle = "Invitado"
                Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
            }

            .show()
    }
}
