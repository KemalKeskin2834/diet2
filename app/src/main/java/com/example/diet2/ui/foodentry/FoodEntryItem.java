package com.example.diet2.ui.foodentry;

public class FoodEntryItem {

    private final long id;
    private String foodName;
    private String amount;
    private String weightType;
    private String cookingMethod;

    public FoodEntryItem(long id) {
        this.id = id;
        this.foodName = "";
        this.amount = "";
        this.weightType = "g";
        this.cookingMethod = "Raw";
    }

    public long getId() {
        return id;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getWeightType() {
        return weightType;
    }

    public void setWeightType(String weightType) {
        this.weightType = weightType;
    }

    public String getCookingMethod() {
        return cookingMethod;
    }

    public void setCookingMethod(String cookingMethod) {
        this.cookingMethod = cookingMethod;
    }
}
