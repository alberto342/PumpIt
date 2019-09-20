package com.albert.fitness.pumpit.model;

import java.util.ArrayList;

public class CommonListResponse {
    private ArrayList<Common> foods;

    public ArrayList<Common> getFoods() {
        return foods;
    }

    public void setFoods(ArrayList<Common> foods) {
        this.foods = foods;
    }

    @Override
    public String toString() {
        return "ClassPojo [Foods = " + foods + "]";
    }
}
