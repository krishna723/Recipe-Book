package com.recipe.recipebook;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.recipe.recipebook.data.recipeDbHelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;


import com.recipe.recipebook.data.recipeContract.recipeEntry;
import com.recipe.recipebook.model.recipeModel;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class MainActivity extends AppCompatActivity implements recipeAdapter.OnRecipeListner {

    recipeDbHelper mDbHelper;
    List<recipeModel> recipes=new ArrayList<>();

    GridLayoutManager gridLayoutManager;
    RecyclerView recyclerView;

    recipeAdapter adapter;

    @Override
    protected void onStop() {
        super.onStop();
        recipes.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView=findViewById(R.id.recycleView);
        gridLayoutManager=new GridLayoutManager(this,2,RecyclerView.VERTICAL,false);

        FloatingActionButton fab = findViewById(R.id.add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                recipes.clear();
                Intent intent=new Intent(MainActivity.this,Add_recipe.class);
                startActivity(intent);
            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    protected void onStart() {
        super.onStart();
        displayData();
    }

    private void displayData(){
        String[] projection={
                recipeEntry._ID,
                recipeEntry.COLUMN_RECIPE_NAME,
                recipeEntry.COLUMN_IMAGE
        };

        Cursor cursor=getContentResolver().query(recipeEntry.CONTENT_URI,projection,null,null,null);
        try {
            while (cursor.moveToNext()) {
                recipes.add(new recipeModel(cursor.getLong(0), cursor.getString(1), cursor.getString(2)));
            }
        }
        finally {
            cursor.close();
        }

        adapter=new recipeAdapter(this,recipes,this);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onRecipeClick(long id) {
        recipes.clear();
        Intent intent=new Intent(MainActivity.this,ShowRecipe.class);
        intent.putExtra("id",id);
        startActivity(intent);

    }
}
