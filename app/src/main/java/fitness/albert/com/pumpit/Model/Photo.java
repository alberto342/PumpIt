package fitness.albert.com.pumpit.Model;

public class Photo {
    private String highres;

    private String thumb;

    private String is_user_uploaded;

    public String getHighres ()
    {
        return highres;
    }

    public void setHighres (String highres)
    {
        this.highres = highres;
    }

    public String getThumb ()
    {
        return thumb;
    }

    public void setThumb (String thumb)
    {
        this.thumb = thumb;
    }

    public String getIs_user_uploaded ()
    {
        return is_user_uploaded;
    }

    public void setIs_user_uploaded (String is_user_uploaded) {
        this.is_user_uploaded = is_user_uploaded;
    }

    @Override
    public String toString() {
        return "ClassPojo [highres = "+highres+", thumb = "+thumb+", is_user_uploaded = "+is_user_uploaded+"]";
    }
}
