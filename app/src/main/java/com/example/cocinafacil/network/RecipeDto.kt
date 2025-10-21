package com.example.cocinafacil.network

import com.example.cocinafacil.models.Recipe

data class RecipeDto(
    val id: Number,
    val name: String,
    val ingredients: List<String>,
    val instructions: List<String>,
    val image: String? = null
) {
    fun toRecipe(): Recipe {
        return Recipe(
            id = id.toInt(),
            title = name,
            ingredients = ingredients.joinToString(", "),
            instructions = instructions.joinToString("\n"),
            image = image
        )
    }
}
