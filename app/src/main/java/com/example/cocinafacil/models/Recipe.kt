package com.example.cocinafacil.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recipe(
    var id: Int = 0,
    val title: String,
    val ingredients: String,
    val instructions: String,
    val image: String? = null
) : Parcelable
