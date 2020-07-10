package com.recipeapp;

public class FoodData {

    private String itemName;
    private String itemDescription;
    private String itemPrize;
    private String itemImage;
    private String key;

    public FoodData(){}

    public FoodData(String itemName, String itemDescription, String itemPrize, String itemImage) {
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemPrize = itemPrize;
        this.itemImage = itemImage;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemPrize() {
        return itemPrize;
    }

    public void setItemPrize(String itemPrize) {
        this.itemPrize = itemPrize;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
