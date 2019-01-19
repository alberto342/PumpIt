package fitness.albert.com.pumpit.Model;

public class Tags {
    private String measure;

    private String food_group;

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

    public String getFood_group ()
    {
        return food_group;
    }

    public void setFood_group (String food_group)
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
}
