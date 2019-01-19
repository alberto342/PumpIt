package fitness.albert.com.pumpit.Model;

public class Metadata {
    private String is_raw_food;

    public String getIs_raw_food ()
    {
        return is_raw_food;
    }

    public void setIs_raw_food (String is_raw_food)
    {
        this.is_raw_food = is_raw_food;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [is_raw_food = "+is_raw_food+"]";
    }
}
