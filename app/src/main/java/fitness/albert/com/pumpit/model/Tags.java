package fitness.albert.com.pumpit.model;

import com.google.gson.annotations.SerializedName;

public class Tags {
    private String measure;

    @SerializedName("food_group")
    private int foodGroup;

    private String item;

    private String quantity;

    @SerializedName("tag_id")
    private String tagId;

    public String getMeasure ()
    {
        return measure;
    }

    public void setMeasure (String measure)
    {
        this.measure = measure;
    }

    public int getFoodGroup()
    {
        return foodGroup;
    }

    public void setFoodGroup(int foodGroup)
    {
        this.foodGroup = foodGroup;
    }

    public String getItem ()
    {
        return item;
    }

    public void setItem (String item)
    {
        this.item = item;
    }

    public String getQuantity ()
    {
        return quantity;
    }

    public void setQuantity (String quantity)
    {
        this.quantity = quantity;
    }

    public String getTagId()
    {
        return tagId;
    }

    public void setTagId(String tagId)
    {
        this.tagId = tagId;
    }


    @Override
    public String toString() {
        return "ClassPojo [measure = "+measure+", foodGroup = "+ foodGroup +", item = "+item+", quantity = "+quantity+", tagId = "+ tagId +"]";
    }

    public static String foodGroup(int id) {
        switch (id) {
            case 1:
                return "Dairy";
            case 2:
                return "Protein";
            case 3:
                return "Fruit";
            case 4:
                return "Vegetable";
            case 5:
                return "Grain";
            case 6:
                return "Fat";
            case 7:
                return "Legume";
            case 8:
                return "Combination (multiple food groups)";
            case 9:
                return "Not applicable";
        }
        return "Combination (multiple food groups)";
    }
}
