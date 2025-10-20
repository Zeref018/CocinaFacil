package com.example.cocinafacil.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Recipe(
    var id: Long = 0L,
    var title: String = "",
    var ingredients: String = "",
    var instructions: String = ""
): Parcelable