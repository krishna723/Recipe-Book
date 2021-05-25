package com.recipe.recipebook.model;

public class recipeModel {
    long id;
    String recipeName;
    String image;

    public recipeModel(long id, String recipeName, String image) {
        this.id = id;
        this.recipeName = recipeName;
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public String getImage() {
        return image;
    }
}
