package com.recipe.recipebook;

import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.recipe.recipebook.data.recipeContract;
import com.recipe.recipebook.model.recipeModel;

import java.util.List;

public class recipeAdapter extends RecyclerView.Adapter<recipeAdapter.recipeViewHolder> {

    List<recipeModel> recipeItems;
    Context mContex;
    OnRecipeListner mOnRecipeListner;
    recipeModel recipe;

    public recipeAdapter(@NonNull Context context,List<recipeModel> recipeItems,OnRecipeListner mOnRecipeListner) {
        this.mContex=context;
        this.recipeItems=recipeItems;
        this.mOnRecipeListner=mOnRecipeListner;
    }

    @NonNull
    @Override
    public recipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.items,parent,false);

        return new recipeViewHolder(view,mOnRecipeListner);
    }

    @Override
    public void onBindViewHolder(@NonNull recipeViewHolder holder, final int position) {
        recipe=recipeItems.get(position);

        holder.txtRecipeName.setText(recipe.getRecipeName());
        holder.recipeImage.setImageBitmap(imageUtils.stringToBitmap(recipe.getImage()));

        holder.btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopMenu(v,recipeItems.get(position).getId(), position);
            }
        });

    }
    public void showPopMenu(View v, final long id, final int position){
        final Uri newUri= ContentUris.withAppendedId(recipeContract.recipeEntry.CONTENT_URI,id);
        PopupMenu popupMenu=new PopupMenu(mContex,v);
        popupMenu.inflate(R.menu.pop_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.popEdit:
                       // Toast.makeText(mContex, "clicked", Toast.LENGTH_SHORT).show();
                        recipeItems.clear();
                        Intent intent=new Intent(mContex,Add_recipe.class);
                        intent.putExtra("id",id);
                        mContex.startActivity(intent);
                        return true;
                    case R.id.popDelete:
                        AlertDialog.Builder builder=new AlertDialog.Builder(mContex);
                        builder.setTitle("Delete Confirmation");
                        builder.setMessage("Are you sure want to delete");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int deletId=mContex.getContentResolver().delete(newUri,null,null);

                                if (deletId>0){
                                    recipeItems.remove(position);
                                    notifyDataSetChanged();
                                }
                            }
                        });
                        builder.setNegativeButton("No",null);
                        builder.show();
                        return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    @Override
    public int getItemCount() {
        return recipeItems.size();
    }

    public class recipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView recipeImage;
        TextView txtRecipeName;
        OnRecipeListner onRecipeListner;
        ImageButton btnMenu;
        public recipeViewHolder(@NonNull View itemView,OnRecipeListner onRecipeListner) {
            super(itemView);
            recipeImage=itemView.findViewById(R.id.recipeImage);
            txtRecipeName=itemView.findViewById(R.id.txtRecipeName);
            this.onRecipeListner=onRecipeListner;
            btnMenu=itemView.findViewById(R.id.btnMenu);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onRecipeListner.onRecipeClick(recipeItems.get(getAdapterPosition()).getId());
        }
    }

    public interface OnRecipeListner{
        void onRecipeClick(long id);
    }

}
