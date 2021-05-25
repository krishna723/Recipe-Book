package com.recipe.recipebook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.LongBuffer;
import java.util.List;

import com.recipe.recipebook.data.recipeContract.recipeEntry;

public class Add_recipe extends AppCompatActivity {

    public static final int REQUEST_CAMERA = 100;
    public static final int SELECT_FILES = 101;

    private ImageView recipeImg;
    private TextInputLayout editTRecipeName,editTCookingTime,editTIngrident,editTMethod;
    Button btnSave;
    Uri uri;
    long id;
    Uri newUri;
    String currentPhotoPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        recipeImg=findViewById(R.id.recipeImg);
        editTRecipeName=findViewById(R.id.editTRecipeName);
        editTCookingTime=findViewById(R.id.editTCookingTime);
        editTIngrident=findViewById(R.id.editTIngrident);
        editTMethod=findViewById(R.id.editTMethod);
        btnSave=findViewById(R.id.btnSave);

        id=getIntent().getLongExtra("id",0);
        if(id>0){
            showData();
        }

        recipeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id>0){
                    updateData();
                }
                else {
                    insertDate();
                }

            }
        });
    }

    private void showData() {
        newUri= ContentUris.withAppendedId(recipeEntry.CONTENT_URI,id);

        String[] projection={
                recipeEntry.COLUMN_RECIPE_NAME,
                recipeEntry.COLUMN_COOKING_TIME,
                recipeEntry.COLUMN_INGREDIENT,
                recipeEntry.COLUMN_METHOD,
                recipeEntry.COLUMN_IMAGE
        };

        Cursor cursor=getContentResolver().query(newUri,projection,null,null,null);
        try{
            while (cursor.moveToNext()){
                editTRecipeName.getEditText().setText(cursor.getString(0));
                editTCookingTime.getEditText().setText(cursor.getString(1));
                editTIngrident.getEditText().setText(cursor.getString(2));
                editTMethod.getEditText().setText(cursor.getString(3));
                recipeImg.setImageBitmap(imageUtils.stringToBitmap(cursor.getString(4)));
            }
        }finally {
            cursor.close();
        }
    }

    private void checkPermission(){
        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                if (multiplePermissionsReport.areAllPermissionsGranted()){
                    selectImage();
                }

            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();

            }
        }).check();
    }

    public void selectImage(){
        final CharSequence[] items={"Camera","Gallery","Cancel"};
        final AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle("Add Picture");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if(items[i].equals("Camera")){
                    String fileName="photo";
                    File storageDirectory=getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    try {
                        File imageFile=File.createTempFile(fileName,".jpg",storageDirectory);
                        currentPhotoPath=imageFile.getAbsolutePath();
                        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        uri= FileProvider.getUriForFile(Add_recipe.this,"com.recipe.recipebook.fileprovider",imageFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
                        startActivityForResult(intent,REQUEST_CAMERA);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if (items[i].equals("Gallery")){
                    Intent intent=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent,"Select File"),SELECT_FILES);
                }
                else if (items[i].equals("Cancel")){
                    dialog.dismiss();
                }

            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            if (requestCode==REQUEST_CAMERA){
                Bitmap bitmap=BitmapFactory.decodeFile(currentPhotoPath);

                recipeImg.setImageBitmap(bitmap);

            }
            if (requestCode==SELECT_FILES){
                uri=data.getData();
                try {
                    recipeImg.setImageBitmap(BitmapFactory.decodeStream(getContentResolver().openInputStream(uri)));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void insertDate(){
        String recipeName=editTRecipeName.getEditText().getText().toString();
        String cookingTime=editTCookingTime.getEditText().getText().toString();
        String ingrident=editTIngrident.getEditText().getText().toString();
        String metode=editTMethod.getEditText().getText().toString();
        Bitmap bitmapImage=((BitmapDrawable)recipeImg.getDrawable()).getBitmap();
        String stringImage=imageUtils.bitmapToString(imageUtils.resizeBitmap(bitmapImage));

     //   Toast.makeText(this, cookingTime, Toast.LENGTH_SHORT).show();


        ContentValues values=new ContentValues();

        values.put(recipeEntry.COLUMN_RECIPE_NAME,recipeName);
        values.put(recipeEntry.COLUMN_COOKING_TIME,cookingTime);
        values.put(recipeEntry.COLUMN_INGREDIENT,ingrident);
        values.put(recipeEntry.COLUMN_METHOD,metode);
        values.put(recipeEntry.COLUMN_IMAGE,stringImage);

        Uri newUri=getContentResolver().insert(recipeEntry.CONTENT_URI,values);

        if (newUri!=null){
            Toast.makeText(this, "Data Insert", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Something wrong", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    private void updateData(){
        String recipeName=editTRecipeName.getEditText().getText().toString();
        String cookingTime=editTCookingTime.getEditText().getText().toString();
        String ingrident=editTIngrident.getEditText().getText().toString();
        String metode=editTMethod.getEditText().getText().toString();
        Bitmap bitmapImage=((BitmapDrawable)recipeImg.getDrawable()).getBitmap();
        String stringImage=imageUtils.bitmapToString(imageUtils.resizeBitmap(bitmapImage));

       // Toast.makeText(this, cookingTime, Toast.LENGTH_SHORT).show();


        ContentValues values=new ContentValues();

        values.put(recipeEntry.COLUMN_RECIPE_NAME,recipeName);
        values.put(recipeEntry.COLUMN_COOKING_TIME,cookingTime);
        values.put(recipeEntry.COLUMN_INGREDIENT,ingrident);
        values.put(recipeEntry.COLUMN_METHOD,metode);
        values.put(recipeEntry.COLUMN_IMAGE,stringImage);

        int updateId=getContentResolver().update(newUri,values,null,null);
        finish();
    }
}
