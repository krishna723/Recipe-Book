package com.recipe.recipebook.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class recipeProvider extends ContentProvider {
    private static final int RECIPES=200;
    private static final int RECIPE_ID=201;

    private static final UriMatcher uriMetcher=new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMetcher.addURI(recipeContract.CONTENT_AUTHORITY,recipeContract.PATH_RECIPE,RECIPES);
        uriMetcher.addURI(recipeContract.CONTENT_AUTHORITY,recipeContract.PATH_RECIPE +"/#",RECIPE_ID);
    }

    private recipeDbHelper mDbHelper;
    @Override
    public boolean onCreate() {
        mDbHelper=new recipeDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int metch = uriMetcher.match(uri);
        switch (metch) {
            case RECIPES:
                cursor = db.query(recipeContract.recipeEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case RECIPE_ID:
                selection = recipeContract.recipeEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(recipeContract.recipeEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot Query unknown URI" + uri);

        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db=mDbHelper.getWritableDatabase();
        long newId=db.insert(recipeContract.recipeEntry.TABLE_NAME,null,values);
        if(newId==-1){
            return null;
        }
        return ContentUris.withAppendedId(uri,newId);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
       SQLiteDatabase db=mDbHelper.getReadableDatabase();

        int metch=uriMetcher.match(uri);
        int deleteId;

        switch (metch){
            case RECIPES:
                deleteId=db.delete(recipeContract.recipeEntry.TABLE_NAME,selection,selectionArgs);
                break;

            case RECIPE_ID:
                selection = recipeContract.recipeEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                deleteId=db.delete(recipeContract.recipeEntry.TABLE_NAME,selection,selectionArgs);
                break;
                default:
                    throw new IllegalArgumentException("Cannot delete unknown URI" + uri);


        }
        return deleteId;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db=mDbHelper.getReadableDatabase();

        selection = recipeContract.recipeEntry._ID + "=?";
        selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
        int updateId=db.update(recipeContract.recipeEntry.TABLE_NAME,values,selection,selectionArgs);
        return updateId;
    }
}
