package com.recipe.recipebook.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.recipe.recipebook.data.recipeContract.recipeEntry;

import androidx.annotation.Nullable;

public class recipeDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="RecipeBook.db";
    public static final int DATABASE_VERSION = 1;
    public recipeDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + recipeEntry.TABLE_NAME +"(" +
                recipeEntry._ID + " INTEGER PRIMARY KEY," +
                recipeEntry.COLUMN_RECIPE_NAME + " TEXT,"+
                recipeEntry.COLUMN_COOKING_TIME + " TEXT,"+
                recipeEntry.COLUMN_INGREDIENT +" TEXT," +
                recipeEntry.COLUMN_METHOD +" TEXT," +
                recipeEntry.COLUMN_IMAGE + " TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
