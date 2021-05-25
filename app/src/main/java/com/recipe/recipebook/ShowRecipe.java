package com.recipe.recipebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.recipe.recipebook.data.recipeContract.recipeEntry;

public class ShowRecipe extends AppCompatActivity {
    TextView recipeName,txtTime,txtIngrident,txtMethod;
    ImageView imgRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_recipe);

        recipeName=findViewById(R.id.recipeName);
        txtTime=findViewById(R.id.txtTime);
        txtIngrident=findViewById(R.id.txtIngrident);
        txtMethod=findViewById(R.id.txtMethod);
        imgRecipe=findViewById(R.id.imgRecipe);


        displayRecipe();

    }

    private void displayRecipe() {
        long id=getIntent().getLongExtra("id",0);

        Uri newUri= ContentUris.withAppendedId(recipeEntry.CONTENT_URI,id);

        String[] projection={
                recipeEntry.COLUMN_RECIPE_NAME,
                recipeEntry.COLUMN_COOKING_TIME,
                recipeEntry.COLUMN_INGREDIENT,
                recipeEntry.COLUMN_METHOD,
                recipeEntry.COLUMN_IMAGE
        };

        Cursor cursor=getContentResolver().query(newUri,projection,null,null,null);
        try {
            while (cursor.moveToNext()){
                recipeName.setText(cursor.getString(0));
                txtTime.setText(cursor.getString(1));
                txtIngrident.setText(cursor.getString(2));
                txtMethod.setText(cursor.getString(3));
                imgRecipe.setImageBitmap(imageUtils.stringToBitmap(cursor.getString(4)));
            }
        }finally {
            cursor.close();
        }
    }
}
