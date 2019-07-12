package fitness.albert.com.pumpit.Model;

import com.google.gson.annotations.SerializedName;

public class Metadata {
    @SerializedName("is_raw_food")
    private String isRawFood;

    public String getIsRawFood()
    {
        return isRawFood;
    }

    public void setIsRawFood(String isRawFood)
    {
        this.isRawFood = isRawFood;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [isRawFood = "+ isRawFood +"]";
    }
}
