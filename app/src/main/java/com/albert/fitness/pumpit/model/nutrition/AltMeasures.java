package com.albert.fitness.pumpit.model.nutrition;

public class AltMeasures {
    private String measure;

    private String serving_weight;

    private String seq;

    private String qty;

    public String getMeasure ()
    {
        return measure;
    }

    public void setMeasure (String measure)
    {
        this.measure = measure;
    }

    public String getServing_weight ()
    {
        return serving_weight;
    }

    public void setServing_weight (String serving_weight)
    {
        this.serving_weight = serving_weight;
    }

    public String getSeq ()
    {
        return seq;
    }

    public void setSeq (String seq)
    {
        this.seq = seq;
    }

    public String getQty ()
    {
        return qty;
    }

    public void setQty (String qty)
    {
        this.qty = qty;
    }

    @Override
    public String toString() {
        return "ClassPojo [measure = "+measure+", serving_weight = "+serving_weight+", seq = "+seq+", qty = "+qty+"]";
    }
}
