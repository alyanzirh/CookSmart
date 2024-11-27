package com.example.cooksmart_v2;

public class DataClass {

    private String cookTime;
    private String directions;
    private String ingredients;
    private String mealPerPerson;

    private String recipeName;
    private String numCal;
    private String dietType;
    private String imageUrl;
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getNumCal() {
        return numCal;
    }

    public void setNumCal(String numCal) {
        this.numCal = numCal;
    }

    public String getDietType() {
        return dietType;
    }

    public void setDietType(String dietType) {
        this.dietType = dietType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCookTime() {
        return cookTime;
    }

    public void setCookTime(String cookTime) {
        this.cookTime = cookTime;
    }

    public String getDirections() {
        return directions;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getMealPerPerson() {
        return mealPerPerson;
    }

    public void setMealPerPerson(String mealPerPerson) {
        this.mealPerPerson = mealPerPerson;
    }

    public DataClass(String recipeName, String numCal, String dietType, String recipeImg, String cookTime, String mealPerPerson, String directions, String ingredients) {
        this.recipeName = recipeName;
        this.numCal = mealPerPerson;
        this.dietType = dietType;
        this.imageUrl = recipeImg;
        this.cookTime = cookTime;
        this.directions = directions;
        this.ingredients = ingredients;
        this.mealPerPerson = mealPerPerson;
    }
    public DataClass(){

    }
}
