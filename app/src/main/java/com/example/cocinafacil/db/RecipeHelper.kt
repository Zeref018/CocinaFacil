package com.example.cocinafacil.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.cocinafacil.models.Recipe

class RecipeDbHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        private const val DB_NAME = "recipes.db"
        private const val DB_VERSION = 1
        private const val TABLE_NAME = "recipes"
        private const val COL_ID = "id"
        private const val COL_TITLE = "title"
        private const val COL_INGREDIENTS = "ingredients"
        private const val COL_INSTRUCTIONS = "instructions"
        private const val COL_IMAGE = "image"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val sql = """
            CREATE TABLE $TABLE_NAME (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_TITLE TEXT NOT NULL,
                $COL_INGREDIENTS TEXT,
                $COL_INSTRUCTIONS TEXT,
                $COL_IMAGE TEXT
            )
        """.trimIndent()
        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insert(recipe: Recipe): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_TITLE, recipe.title)
            put(COL_INGREDIENTS, recipe.ingredients)
            put(COL_INSTRUCTIONS, recipe.instructions)
            put(COL_IMAGE, recipe.image)
        }
        val id = db.insert(TABLE_NAME, null, values)
        db.close()
        return id.toInt()
    }

    fun getAll(): List<Recipe> {
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)
        val list = mutableListOf<Recipe>()
        cursor.use {
            while (it.moveToNext()) {
                list.add(
                    Recipe(
                        id = it.getInt(it.getColumnIndexOrThrow(COL_ID)),
                        title = it.getString(it.getColumnIndexOrThrow(COL_TITLE)),
                        ingredients = it.getString(it.getColumnIndexOrThrow(COL_INGREDIENTS)),
                        instructions = it.getString(it.getColumnIndexOrThrow(COL_INSTRUCTIONS)),
                        image = it.getString(it.getColumnIndexOrThrow(COL_IMAGE))
                    )
                )
            }
        }
        db.close()
        return list
    }
}
