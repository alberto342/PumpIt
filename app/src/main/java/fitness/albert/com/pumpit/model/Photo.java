package fitness.albert.com.pumpit.model;

import com.google.gson.annotations.SerializedName;

public class Photo {

    private String highres;

    private String thumb;

    @SerializedName("is_user_uploaded")
    private String isUserUploaded;

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

    public String getIsUserUploaded()
    {
        return isUserUploaded;
    }

    public void setIsUserUploaded(String isUserUploaded) {
        this.isUserUploaded = isUserUploaded;
    }

    @Override
    public String toString() {
        return "ClassPojo [highres = "+highres+", thumb = "+thumb+", isUserUploaded = "+ isUserUploaded +"]";
    }
}
