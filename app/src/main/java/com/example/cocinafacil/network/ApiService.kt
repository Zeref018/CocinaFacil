package com.example.cocinafacil.network

import com.example.cocinafacil.models.Recipe
import retrofit2.Call
import retrofit2.http.GET


interface ApiService {
    // Usamos un endpoint público de prueba (Dummy JSON). Se puede cambiar.
    @GET("/recipes")
    fun getRecipes(): Call<List<Recipe>>
}