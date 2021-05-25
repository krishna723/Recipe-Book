package com.recipe.recipebook.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class recipeContract {

    public static final String CONTENT_AUTHORITY = "com.recipe.recipebook";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+ CONTENT_AUTHORITY);
    public static final String PATH_RECIPE = "recipe";

    public static final class recipeEntry implements BaseColumns {
        public static final String COLUMN_COOKING_TIME = "cookingTime";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_INGREDIENT = "ingredient";
        public static final String COLUMN_METHOD = "method";
        public static final String COLUMN_RECIPE_NAME = "name";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_RECIPE);
        public static final String TABLE_NAME = "recipe";
        public static final String _ID = "_id";
    }
}
