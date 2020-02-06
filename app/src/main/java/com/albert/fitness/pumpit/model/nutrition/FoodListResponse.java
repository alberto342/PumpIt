package com.albert.fitness.pumpit.model.nutrition;

import java.util.ArrayList;

public class FoodListResponse {
    private ArrayList<Foods> foods;

    public ArrayList<Foods> getFoods() {
        return foods;
    }

    public void setFoods(ArrayList<Foods> foods) {
        this.foods = foods;
    }

    @Override
    public String toString() {
        return "ClassPojo [Foods = " + foods + "]";
    }
}
