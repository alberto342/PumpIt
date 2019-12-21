package com.albert.fitness.pumpit.model.nutrition.room;

import androidx.room.ColumnInfo;

public class QueryAltMeasures {

    private String measure;
    private int qty;
    private int seq;
    @ColumnInfo(name = "serving_weight")
    private int servingWeight;
    @ColumnInfo(name = "serving_weight_grams")
    private int servingWeightGrams;


    public QueryAltMeasures() {
    }

    public QueryAltMeasures(String measure, int qty, int seq, int servingWeight, int servingWeightGrams) {
        this.measure = measure;
        this.qty = qty;
        this.seq = seq;
        this.servingWeight = servingWeight;
        this.servingWeightGrams = servingWeightGrams;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getServingWeight() {
        return servingWeight;
    }

    public void setServingWeight(int servingWeight) {
        this.servingWeight = servingWeight;
    }

    public int getServingWeightGrams() {
        return servingWeightGrams;
    }

    public void setServingWeightGrams(int servingWeightGrams) {
        this.servingWeightGrams = servingWeightGrams;
    }
}
