package com.example.cocinafacil.network

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("/recipes")
    fun getRecipes(): Call<RecipeResponse>
}
