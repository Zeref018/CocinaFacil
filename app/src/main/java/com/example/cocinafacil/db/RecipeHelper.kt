package com.example.cocinafacil.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.cocinafacil.models.Recipe


class RecipeDbHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        const val DB_NAME = "recetas.db"
        const val DB_VERSION = 1
        const val TABLE_NAME = "recipes"
        const val COL_ID = "id"
        const val COL_TITLE = "title"
        const val COL_ING = "ingredients"
        const val COL_INS = "instructions"
    }


    override fun onCreate(db: SQLiteDatabase) {
        val sql = """
CREATE TABLE $TABLE_NAME (
$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
$COL_TITLE TEXT,
$COL_ING TEXT,
$COL_INS TEXT
)
""".trimIndent()
        db.execSQL(sql)
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }


    fun insert(recipe: Recipe): Long {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(COL_TITLE, recipe.title)
        cv.put(COL_ING, recipe.ingredients)
        cv.put(COL_INS, recipe.instructions)
        return db.insert(TABLE_NAME, null, cv)
    }


    fun getAll(): List<Recipe> {
        val list = mutableListOf<Recipe>()
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, "$COL_ID DESC")
        cursor.use {
            while (it.moveToNext()) {
                val r = Recipe(
                    id = it.getLong(it.getColumnIndexOrThrow(COL_ID)),
                    title = it.getString(it.getColumnIndexOrThrow(COL_TITLE)),
                    ingredients = it.getString(it.getColumnIndexOrThrow(COL_ING)),
                    instructions = it.getString(it.getColumnIndexOrThrow(COL_INS))
                )
                list.add(r)
            }
        }
        return list
    }
}