package fitness.albert.com.pumpit.Model;

public class Tags {
    private String measure;

    private int food_group;

    private String item;

    private String quantity;

    private String tag_id;

    public String getMeasure ()
    {
        return measure;
    }

    public void setMeasure (String measure)
    {
        this.measure = measure;
    }

    public int getFood_group ()
    {
        return food_group;
    }

    public void setFood_group (int food_group)
    {
        this.food_group = food_group;
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

    public String getTag_id ()
    {
        return tag_id;
    }

    public void setTag_id (String tag_id)
    {
        this.tag_id = tag_id;
    }


    @Override
    public String toString() {
        return "ClassPojo [measure = "+measure+", food_group = "+food_group+", item = "+item+", quantity = "+quantity+", tag_id = "+tag_id+"]";
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
                return "Combination (multiple food groups, not discernable)";

            case 9:
                return "Not applicable";
        }
        return null;
    }
}
