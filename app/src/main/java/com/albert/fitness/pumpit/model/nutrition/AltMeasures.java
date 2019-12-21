package com.albert.fitness.pumpit.model.nutrition;

import com.google.gson.annotations.SerializedName;

public class AltMeasures {

    private String measure;

    @SerializedName("serving_weight")
    private String servingWeight;

    private String seq;

    private int qty;

    public String getMeasure ()
    {
        return measure;
    }

    public void setMeasure (String measure)
    {
        this.measure = measure;
    }

    public String getServingWeight()
    {
        return servingWeight;
    }

    public void setServing_weight (String servingWeight)
    {
        this.servingWeight = servingWeight;
    }

    public String getSeq ()
    {
        return seq;
    }

    public void setSeq (String seq)
    {
        this.seq = seq;
    }

    public int getQty ()
    {
        return qty;
    }

    public void setQty (int qty)
    {
        this.qty = qty;
    }

    @Override
    public String toString() {
        return "ClassPojo [measure = "+measure+", serving_weight = "+servingWeight+", seq = "+seq+", qty = "+qty+"]";
    }
}
