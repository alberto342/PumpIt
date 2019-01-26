package fitness.albert.com.pumpit.Model;

import io.opencensus.tags.Tags;

public class Foods {

    public static final String SharedPreferencesFile = "meal";
    public static final String dinner = "dinner";
    public static final String breakfast = "breakfast";
    public static final String lunch = "lunch";
    public static final String snack = "snack";


    public Foods() {
    }


    public Foods(String serving_unit, float nf_total_carbohydrate, float nf_sodium, String lng, float nf_p, Alt_measures[] alt_measures, float nf_sugars, float nf_calories, String brand_name, String lat, Metadata metadata, Tags tags, String sub_recipe, String ndb_no, String food_name, String meal_type, float nf_saturated_fat, float nf_protein, float nf_total_fat, String consumed_at, float nf_dietary_fiber, float nf_cholesterol, Photo photo, String upc, float nf_potassium, String nix_brand_name, String nix_item_id, String source, String nix_item_name, float serving_weight_grams, String nix_brand_id, String serving_qty) {
        this.serving_unit = serving_unit;
        this.nf_total_carbohydrate = nf_total_carbohydrate;
        this.nf_sodium = nf_sodium;
        this.lng = lng;
        this.nf_p = nf_p;
        this.alt_measures = alt_measures;
        this.nf_sugars = nf_sugars;
        this.nf_calories = nf_calories;
        this.brand_name = brand_name;
        this.lat = lat;
        this.metadata = metadata;
        this.tags = tags;
        this.sub_recipe = sub_recipe;
        this.ndb_no = ndb_no;
        this.food_name = food_name;
        this.meal_type = meal_type;
        this.nf_saturated_fat = nf_saturated_fat;
        this.nf_protein = nf_protein;
        this.nf_total_fat = nf_total_fat;
        this.consumed_at = consumed_at;
        this.nf_dietary_fiber = nf_dietary_fiber;
        this.nf_cholesterol = nf_cholesterol;
        this.photo = photo;
        this.upc = upc;
        this.nf_potassium = nf_potassium;
        this.nix_brand_name = nix_brand_name;
        this.nix_item_id = nix_item_id;
        this.source = source;
        this.nix_item_name = nix_item_name;
        this.serving_weight_grams = serving_weight_grams;
        this.nix_brand_id = nix_brand_id;
        this.serving_qty = serving_qty;
    }



    private String serving_unit;

    private float nf_total_carbohydrate;

    private float nf_sodium;

    private String lng;

    private float nf_p;

    private Alt_measures[] alt_measures;

    private float nf_sugars;

    private float nf_calories;

    private String brand_name;

    private String lat;

    private Metadata metadata;

    private Tags tags;

    private String sub_recipe;

    private String ndb_no;

    private String food_name;

    private String meal_type;

    private float nf_saturated_fat;

    private float nf_protein;

    private float nf_total_fat;

    private String consumed_at;

    private float nf_dietary_fiber;

    private float nf_cholesterol;

    private Photo photo;

    private String upc;

    private float nf_potassium;

    private String nix_brand_name;


    private Full_nutrients[] full_nutrients;




    private String nix_item_id;

    private String source;

    private String nix_item_name;

    private float serving_weight_grams;

    private String nix_brand_id;

    private String serving_qty;

    private String imgUrl;

    private String thumb;

    public String getServing_unit() {
        return serving_unit;
    }

    public void setServing_unit(String serving_unit) {
        this.serving_unit = serving_unit;
    }

    public float getNf_total_carbohydrate() {
        return nf_total_carbohydrate;
    }

    public void setNf_total_carbohydrate(float nf_total_carbohydrate) {
        this.nf_total_carbohydrate = nf_total_carbohydrate;
    }

    public float getNf_sodium() {
        return nf_sodium;
    }

    public void setNf_sodium(float nf_sodium) {
        this.nf_sodium = nf_sodium;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public float getNf_p() {
        return nf_p;
    }

    public void setNf_p(float nf_p) {
        this.nf_p = nf_p;
    }

    public Alt_measures[] getAlt_measures() {
        return alt_measures;
    }

    public void setAlt_measures(Alt_measures[] alt_measures) {
        this.alt_measures = alt_measures;
    }

    public float getNf_sugars() {
        return nf_sugars;
    }

    public void setNf_sugars(float nf_sugars) {
        this.nf_sugars = nf_sugars;
    }

    public float getNf_calories() {
        return nf_calories;
    }

    public void setNf_calories(float nf_calories) {
        this.nf_calories = nf_calories;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public Tags getTags() {
        return tags;
    }

    public void setTags(Tags tags) {
        this.tags = tags;
    }

    public String getSub_recipe() {
        return sub_recipe;
    }

    public void setSub_recipe(String sub_recipe) {
        this.sub_recipe = sub_recipe;
    }

    public String getNdb_no() {
        return ndb_no;
    }

    public void setNdb_no(String ndb_no) {
        this.ndb_no = ndb_no;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public String getMeal_type() {
        return meal_type;
    }

    public void setMeal_type(String meal_type) {
        this.meal_type = meal_type;
    }

    public float getNf_saturated_fat() {
        return nf_saturated_fat;
    }

    public void setNf_saturated_fat(float nf_saturated_fat) {
        this.nf_saturated_fat = nf_saturated_fat;
    }

    public float getNf_protein() {
        return nf_protein;
    }

    public void setNf_protein(float nf_protein) {
        this.nf_protein = nf_protein;
    }

    public float getNf_total_fat() {
        return nf_total_fat;
    }

    public void setNf_total_fat(float nf_total_fat) {
        this.nf_total_fat = nf_total_fat;
    }

    public String getConsumed_at() {
        return consumed_at;
    }

    public void setConsumed_at(String consumed_at) {
        this.consumed_at = consumed_at;
    }

    public float getNf_dietary_fiber() {
        return nf_dietary_fiber;
    }

    public void setNf_dietary_fiber(float nf_dietary_fiber) {
        this.nf_dietary_fiber = nf_dietary_fiber;
    }

    public float getNf_cholesterol() {
        return nf_cholesterol;
    }

    public void setNf_cholesterol(float nf_cholesterol) {
        this.nf_cholesterol = nf_cholesterol;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public float getNf_potassium() {
        return nf_potassium;
    }

    public void setNf_potassium(float nf_potassium) {
        this.nf_potassium = nf_potassium;
    }

    public String getNix_brand_name() {
        return nix_brand_name;
    }

    public void setNix_brand_name(String nix_brand_name) {
        this.nix_brand_name = nix_brand_name;
    }

    public Full_nutrients[] getFull_nutrients() {
        return full_nutrients;
    }

    public void setFull_nutrients(Full_nutrients[] full_nutrients) {
        this.full_nutrients = full_nutrients;
    }

    public String getNix_item_id() {
        return nix_item_id;
    }

    public void setNix_item_id(String nix_item_id) {
        this.nix_item_id = nix_item_id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getNix_item_name() {
        return nix_item_name;
    }

    public void setNix_item_name(String nix_item_name) {
        this.nix_item_name = nix_item_name;
    }

    public float getServing_weight_grams() {
        return serving_weight_grams;
    }

    public void setServing_weight_grams(float serving_weight_grams) {
        this.serving_weight_grams = serving_weight_grams;
    }

    public String getNix_brand_id() {
        return nix_brand_id;
    }

    public void setNix_brand_id(String nix_brand_id) {
        this.nix_brand_id = nix_brand_id;
    }

    public String getServing_qty() {
        return serving_qty;
    }

    public void setServing_qty(String serving_qty) {
        this.serving_qty = serving_qty;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    @Override
    public String toString() {
        return "ClassPojo [serving_unit = " + serving_unit + ", nf_total_carbohydrate = " + nf_total_carbohydrate + ", nf_sodium = " + nf_sodium + ", lng = " + lng + ", nf_p = " + nf_p + ", alt_measures = " + alt_measures + ", nf_sugars = " + nf_sugars + ", nf_calories = " + nf_calories + ", brand_name = " + brand_name + ", lat = " + lat + ", metadata = " + metadata + ", tags = " + tags + ", sub_recipe = " + sub_recipe + ", ndb_no = " + ndb_no + ", food_name = " + food_name + ", meal_type = " + meal_type + ", nf_saturated_fat = " + nf_saturated_fat + ", nf_protein = " + nf_protein + ", nf_total_fat = " + nf_total_fat + ", consumed_at = " + consumed_at + ", nf_dietary_fiber = " + nf_dietary_fiber + ", nf_cholesterol = " + nf_cholesterol + ", photo = " + photo + ", upc = " + upc + ", nf_potassium = " + nf_potassium + ", nix_brand_name = " + nix_brand_name + ", full_nutrients = " + full_nutrients + ", nix_item_id = " + nix_item_id + ", source = " + source + ", nix_item_name = " + nix_item_name + ", serving_weight_grams = " + serving_weight_grams + ", nix_brand_id = " + nix_brand_id + ", serving_qty = " + serving_qty + "]";
    }

}
