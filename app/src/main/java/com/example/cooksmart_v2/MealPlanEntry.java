package com.example.cooksmart_v2;

public class MealPlanEntry {

    private String key;
    private String recipeName;
    private String dietType;
    private String calories;
    private String cookingTime;
    private String person;
    private String ingredients;
    private String directions;
    private String imageUrl;
    private String date;
    private String meal;

    // Constructors

    public MealPlanEntry() {
    }

    public MealPlanEntry(String key, String recipeName, String dietType, String calories, String cookingTime, String person, String ingredients, String directions, String imageUrl, String date, String meal) {
        this.key = key;
        this.recipeName = recipeName;
        this.dietType = dietType;
        this.calories = calories;
        this.cookingTime = cookingTime;
        this.person = person;
        this.ingredients = ingredients;
        this.directions = directions;
        this.imageUrl = imageUrl;
        this.date = date;
        this.meal = meal;
    }

    // Getters and setters for all fields

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

    public String getDietType() {
        return dietType;
    }

    public void setDietType(String dietType) {
        this.dietType = dietType;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(String cookingTime) {
        this.cookingTime = cookingTime;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getDirections() {
        return directions;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMeal() {
        return meal;
    }

    public void setMeal(String meal) {
        this.meal = meal;
    }
}

