package fitness.albert.com.pumpit.Model;

import java.util.List;

public class Foods {

    public static final String SHARED_PREFERENCES_FILE = "meal";
    public static final String DINNER = "dinner";
    public static final String BREAKFAST = "breakfast";
    public static final String LUNCH = "lunch";
    public static final String SNACK = "snack";
    public static final String NUTRITION = "nutrition";
    public static final String All_NUTRITION = "all_nutrition";

    public Foods() {
    }


    private String serving_unit;

    private float nf_total_carbohydrate;

    private float nf_sodium;

    private String lng;

    private float nf_p;

    private List<Alt_measures> alt_measures;

//    private Alt_measures[] alt_measures;


    String calcium;
    String carbohydrate;
    String energy;
    String fattyAcidsTotalSaturated;
    String totalLipid;
    String fattyAcidsTotalTrans;
    String iron;
    String fiber;
    String potassium;
    String sodium;
    String protein;
    String sugars;
    String sugarsAdded;
    String vitaminD;
    String alanine;
    String Aacohol;
    String arginine;
    String ash;
    String asparticAcid;
    String betaine;
    String caffeine;
    String campesterol;
    String caroteneAlpha;
    String caroteneBeta;
    String vitaminD3;
    String choline;
    String cryptoxanthinBeta;
    String copper;
    String cystine;
    String vitaminD2;
    String undifferentiated1601;
    String c1601;
    String t1601;
    String undifferentiated1801;
    String c1801;
    String t1801;
    String t180111;
    String undifferentiated1802;
    String clas;
    String n61802;
    String t1802;
    String undifferentiated1803;
    String ala;
    String n61803;
    String n62002;
    String undifferentiated2003;
    String n32003;
    String n6203;
    String undifferentiated204;
    String n6204;
    String epa;
    String undifferentiated2201;
    String dpa;
    String dha;
    String fattyAcidsTotalMonounsaturated;
    String fattyAcidsTotalPolyunsaturated;
    String fattyAcidsTotalTransMonoenoic;
    String fattyAcidsTotalTransPolyenoic;
    String fluoride;
    String folateTotal;
    String folicAcid;
    String folateDFE;
    String folateFood;
    String fructose;
    String galactose;
    String glutamicAcid;
    String glucose;
    String glycine;
    String histidine;
    String hydroxyproline;
    String isoleucine;
    String lactose;
    String leucine;
    String luteinAndZaxanthin;
    String lycopene;
    String lysine;
    String maltose;
    String methionine;
    String magnesium;
    String menaquinone4;
    String manganese;
    String niacin;
    String vitaminE;
    String vitaminB12;
    String adjustedProtein;
    String t2201;
    String c2201;
    String i1803;
    String n1802;
    String tnotFurtherDefined;
    String i1802;
    String phosphorus;
    String pantothenic;
    String acid;
    String phenylalanine;
    String phytosterols;
    String proline;
    String retinol;
    String riboflavin;
    String selenium;
    String serine;
    String betaSitosterol;
    String starch;
    String stigmasterol;
    String sucrose;
    String theobromine;
    String thiamin;
    String threonine;
    String vitamin;
    String alphaTocopherol;
    String tocopherolBeta;
    String tocopherolDelta;
    String tocopherolGamma;
    String tryptophan;
    String tyrosine;
    String valine;
    String vitaminAIU;
    String vitaminARAE;
    String vitaminB122;
    String vitaminB6;
    String vitaminC;
    String vitaminD2AndD3;
    String vitaminK;
    String dihydrophylloquinone;
    String water;
    String zinc;



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



//    private Full_nutrients[] full_nutrients;

    private List<Full_nutrients> full_nutrients;


    private String nix_item_id;

    private String source;

    private String nix_item_name;

    private float serving_weight_grams;

    private String nix_brand_id;

    private int serving_qty;

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

    public List<Alt_measures> getAlt_measures() {
        return alt_measures;
    }

//    public Alt_measures[] getAlt_measures() {
//        return alt_measures;
//    }
//
//    public void setAlt_measures(Alt_measures[] alt_measures) {
//        this.alt_measures = alt_measures;
//    }

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

    public List<Full_nutrients> getFull_nutrients() {
        return full_nutrients;
    }

    //    public Full_nutrients[] getFull_nutrients() {
//        return full_nutrients;
//    }
//
//    public void setFull_nutrients(Full_nutrients[] full_nutrients) {
//        this.full_nutrients = full_nutrients;
//    }

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

    public int getServing_qty() {
        return serving_qty;
    }

    public void setServing_qty(int serving_qty) {
        this.serving_qty = serving_qty;
    }

    //    public String getServing_qty() {
//        return serving_qty;
//    }
//
//    public void setServing_qty(String serving_qty) {
//        this.serving_qty = serving_qty;
//    }

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


    public void setAlt_measures(List<Alt_measures> alt_measures) {
        this.alt_measures = alt_measures;
    }

    public void setCalcium(String calcium) {
        this.calcium = calcium;
    }

    public void setCarbohydrate(String carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    public void setEnergy(String energy) {
        this.energy = energy;
    }

    public void setFattyAcidsTotalSaturated(String fattyAcidsTotalSaturated) {
        this.fattyAcidsTotalSaturated = fattyAcidsTotalSaturated;
    }

    public void setTotalLipid(String totalLipid) {
        this.totalLipid = totalLipid;
    }

    public void setFattyAcidsTotalTrans(String fattyAcidsTotalTrans) {
        this.fattyAcidsTotalTrans = fattyAcidsTotalTrans;
    }

    public void setIron(String iron) {
        this.iron = iron;
    }

    public void setFiber(String fiber) {
        this.fiber = fiber;
    }

    public void setPotassium(String potassium) {
        this.potassium = potassium;
    }

    public void setSodium(String sodium) {
        this.sodium = sodium;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public void setSugars(String sugars) {
        this.sugars = sugars;
    }

    public void setSugarsAdded(String sugarsAdded) {
        this.sugarsAdded = sugarsAdded;
    }

    public void setVitaminD(String vitaminD) {
        this.vitaminD = vitaminD;
    }

    public void setAlanine(String alanine) {
        this.alanine = alanine;
    }

    public void setAacohol(String aacohol) {
        Aacohol = aacohol;
    }

    public void setArginine(String arginine) {
        this.arginine = arginine;
    }

    public void setAsh(String ash) {
        this.ash = ash;
    }

    public void setAsparticAcid(String asparticAcid) {
        this.asparticAcid = asparticAcid;
    }

    public void setBetaine(String betaine) {
        this.betaine = betaine;
    }

    public void setCaffeine(String caffeine) {
        this.caffeine = caffeine;
    }

    public void setCampesterol(String campesterol) {
        this.campesterol = campesterol;
    }

    public void setCaroteneAlpha(String caroteneAlpha) {
        this.caroteneAlpha = caroteneAlpha;
    }

    public void setCaroteneBeta(String caroteneBeta) {
        this.caroteneBeta = caroteneBeta;
    }

    public void setVitaminD3(String vitaminD3) {
        this.vitaminD3 = vitaminD3;
    }

    public void setCholine(String choline) {
        this.choline = choline;
    }

    public void setCryptoxanthinBeta(String cryptoxanthinBeta) {
        this.cryptoxanthinBeta = cryptoxanthinBeta;
    }

    public void setCopper(String copper) {
        this.copper = copper;
    }

    public void setCystine(String cystine) {
        this.cystine = cystine;
    }

    public void setVitaminD2(String vitaminD2) {
        this.vitaminD2 = vitaminD2;
    }

    public void setUndifferentiated1601(String undifferentiated1601) {
        this.undifferentiated1601 = undifferentiated1601;
    }

    public void setC1601(String c1601) {
        this.c1601 = c1601;
    }

    public void setT1601(String t1601) {
        this.t1601 = t1601;
    }

    public void setUndifferentiated1801(String undifferentiated1801) {
        this.undifferentiated1801 = undifferentiated1801;
    }

    public void setC1801(String c1801) {
        this.c1801 = c1801;
    }

    public void setT1801(String t1801) {
        this.t1801 = t1801;
    }

    public void setT180111(String t180111) {
        this.t180111 = t180111;
    }

    public void setUndifferentiated1802(String undifferentiated1802) {
        this.undifferentiated1802 = undifferentiated1802;
    }

    public void setClas(String clas) {
        this.clas = clas;
    }

    public void setN61802(String n61802) {
        this.n61802 = n61802;
    }

    public void setT1802(String t1802) {
        this.t1802 = t1802;
    }

    public void setUndifferentiated1803(String undifferentiated1803) {
        this.undifferentiated1803 = undifferentiated1803;
    }

    public void setAla(String ala) {
        this.ala = ala;
    }

    public void setN61803(String n61803) {
        this.n61803 = n61803;
    }

    public void setN62002(String n62002) {
        this.n62002 = n62002;
    }

    public void setUndifferentiated2003(String undifferentiated2003) {
        this.undifferentiated2003 = undifferentiated2003;
    }

    public void setN32003(String n32003) {
        this.n32003 = n32003;
    }

    public void setN6203(String n6203) {
        this.n6203 = n6203;
    }

    public void setUndifferentiated204(String undifferentiated204) {
        this.undifferentiated204 = undifferentiated204;
    }

    public void setN6204(String n6204) {
        this.n6204 = n6204;
    }

    public void setEpa(String epa) {
        this.epa = epa;
    }

    public void setUndifferentiated2201(String undifferentiated2201) {
        this.undifferentiated2201 = undifferentiated2201;
    }

    public void setDpa(String dpa) {
        this.dpa = dpa;
    }

    public void setDha(String dha) {
        this.dha = dha;
    }

    public void setFattyAcidsTotalMonounsaturated(String fattyAcidsTotalMonounsaturated) {
        this.fattyAcidsTotalMonounsaturated = fattyAcidsTotalMonounsaturated;
    }

    public void setFattyAcidsTotalPolyunsaturated(String fattyAcidsTotalPolyunsaturated) {
        this.fattyAcidsTotalPolyunsaturated = fattyAcidsTotalPolyunsaturated;
    }

    public void setFattyAcidsTotalTransMonoenoic(String fattyAcidsTotalTransMonoenoic) {
        this.fattyAcidsTotalTransMonoenoic = fattyAcidsTotalTransMonoenoic;
    }

    public void setFattyAcidsTotalTransPolyenoic(String fattyAcidsTotalTransPolyenoic) {
        this.fattyAcidsTotalTransPolyenoic = fattyAcidsTotalTransPolyenoic;
    }

    public void setFluoride(String fluoride) {
        this.fluoride = fluoride;
    }

    public void setFolateTotal(String folateTotal) {
        this.folateTotal = folateTotal;
    }

    public void setFolicAcid(String folicAcid) {
        this.folicAcid = folicAcid;
    }

    public void setFolateDFE(String folateDFE) {
        this.folateDFE = folateDFE;
    }

    public void setFolateFood(String folateFood) {
        this.folateFood = folateFood;
    }

    public void setFructose(String fructose) {
        this.fructose = fructose;
    }

    public void setGalactose(String galactose) {
        this.galactose = galactose;
    }

    public void setGlutamicAcid(String glutamicAcid) {
        this.glutamicAcid = glutamicAcid;
    }

    public void setGlucose(String glucose) {
        this.glucose = glucose;
    }

    public void setGlycine(String glycine) {
        this.glycine = glycine;
    }

    public void setHistidine(String histidine) {
        this.histidine = histidine;
    }

    public void setHydroxyproline(String hydroxyproline) {
        this.hydroxyproline = hydroxyproline;
    }

    public void setIsoleucine(String isoleucine) {
        this.isoleucine = isoleucine;
    }

    public void setLactose(String lactose) {
        this.lactose = lactose;
    }

    public void setLeucine(String leucine) {
        this.leucine = leucine;
    }

    public void setLuteinAndZaxanthin(String luteinAndZaxanthin) {
        this.luteinAndZaxanthin = luteinAndZaxanthin;
    }

    public void setLycopene(String lycopene) {
        this.lycopene = lycopene;
    }

    public void setLysine(String lysine) {
        this.lysine = lysine;
    }

    public void setMaltose(String maltose) {
        this.maltose = maltose;
    }

    public void setMethionine(String methionine) {
        this.methionine = methionine;
    }

    public void setMagnesium(String magnesium) {
        this.magnesium = magnesium;
    }

    public void setMenaquinone4(String menaquinone4) {
        this.menaquinone4 = menaquinone4;
    }

    public void setManganese(String manganese) {
        this.manganese = manganese;
    }

    public void setNiacin(String niacin) {
        this.niacin = niacin;
    }

    public void setVitaminE(String vitaminE) {
        this.vitaminE = vitaminE;
    }

    public void setVitaminB12(String vitaminB12) {
        this.vitaminB12 = vitaminB12;
    }

    public void setAdjustedProtein(String adjustedProtein) {
        this.adjustedProtein = adjustedProtein;
    }

    public void setT2201(String t2201) {
        this.t2201 = t2201;
    }

    public void setC2201(String c2201) {
        this.c2201 = c2201;
    }

    public void setI1803(String i1803) {
        this.i1803 = i1803;
    }

    public void setN1802(String n1802) {
        this.n1802 = n1802;
    }

    public void setTnotFurtherDefined(String tnotFurtherDefined) {
        this.tnotFurtherDefined = tnotFurtherDefined;
    }

    public void setI1802(String i1802) {
        this.i1802 = i1802;
    }

    public void setPhosphorus(String phosphorus) {
        this.phosphorus = phosphorus;
    }

    public void setPantothenic(String pantothenic) {
        this.pantothenic = pantothenic;
    }

    public void setAcid(String acid) {
        this.acid = acid;
    }

    public void setPhenylalanine(String phenylalanine) {
        this.phenylalanine = phenylalanine;
    }

    public void setPhytosterols(String phytosterols) {
        this.phytosterols = phytosterols;
    }

    public void setProline(String proline) {
        this.proline = proline;
    }

    public void setRetinol(String retinol) {
        this.retinol = retinol;
    }

    public void setRiboflavin(String riboflavin) {
        this.riboflavin = riboflavin;
    }

    public void setSelenium(String selenium) {
        this.selenium = selenium;
    }

    public void setSerine(String serine) {
        this.serine = serine;
    }

    public void setBetaSitosterol(String betaSitosterol) {
        this.betaSitosterol = betaSitosterol;
    }

    public void setStarch(String starch) {
        this.starch = starch;
    }

    public void setStigmasterol(String stigmasterol) {
        this.stigmasterol = stigmasterol;
    }

    public void setSucrose(String sucrose) {
        this.sucrose = sucrose;
    }

    public void setTheobromine(String theobromine) {
        this.theobromine = theobromine;
    }

    public void setThiamin(String thiamin) {
        this.thiamin = thiamin;
    }

    public void setThreonine(String threonine) {
        this.threonine = threonine;
    }

    public void setVitamin(String vitamin) {
        this.vitamin = vitamin;
    }

    public void setAlphaTocopherol(String alphaTocopherol) {
        this.alphaTocopherol = alphaTocopherol;
    }

    public void setTocopherolBeta(String tocopherolBeta) {
        this.tocopherolBeta = tocopherolBeta;
    }

    public void setTocopherolDelta(String tocopherolDelta) {
        this.tocopherolDelta = tocopherolDelta;
    }

    public void setTocopherolGamma(String tocopherolGamma) {
        this.tocopherolGamma = tocopherolGamma;
    }

    public void setTryptophan(String tryptophan) {
        this.tryptophan = tryptophan;
    }

    public void setTyrosine(String tyrosine) {
        this.tyrosine = tyrosine;
    }

    public void setValine(String valine) {
        this.valine = valine;
    }

    public void setVitaminAIU(String vitaminAIU) {
        this.vitaminAIU = vitaminAIU;
    }

    public void setVitaminARAE(String vitaminARAE) {
        this.vitaminARAE = vitaminARAE;
    }

    public void setVitaminB122(String vitaminB122) {
        this.vitaminB122 = vitaminB122;
    }

    public void setVitaminB6(String vitaminB6) {
        this.vitaminB6 = vitaminB6;
    }

    public void setVitaminC(String vitaminC) {
        this.vitaminC = vitaminC;
    }

    public void setVitaminD2AndD3(String vitaminD2AndD3) {
        this.vitaminD2AndD3 = vitaminD2AndD3;
    }

    public void setVitaminK(String vitaminK) {
        this.vitaminK = vitaminK;
    }

    public void setDihydrophylloquinone(String dihydrophylloquinone) {
        this.dihydrophylloquinone = dihydrophylloquinone;
    }

    public void setWater(String water) {
        this.water = water;
    }

    public void setZinc(String zinc) {
        this.zinc = zinc;
    }

    public void setFull_nutrients(List<Full_nutrients> full_nutrients) {
        this.full_nutrients = full_nutrients;
    }

    @Override
    public String toString() {
        return "ClassPojo [serving_unit = " + serving_unit + ", nf_total_carbohydrate = " + nf_total_carbohydrate + ", nf_sodium = " + nf_sodium + ", lng = " + lng + ", nf_p = " + nf_p + ", alt_measures = " + alt_measures + ", nf_sugars = " + nf_sugars + ", nf_calories = " + nf_calories + ", brand_name = " + brand_name + ", lat = " + lat + ", metadata = " + metadata + ", tags = " + tags + ", sub_recipe = " + sub_recipe + ", ndb_no = " + ndb_no + ", food_name = " + food_name + ", meal_type = " + meal_type + ", nf_saturated_fat = " + nf_saturated_fat + ", nf_protein = " + nf_protein + ", nf_total_fat = " + nf_total_fat + ", consumed_at = " + consumed_at + ", nf_dietary_fiber = " + nf_dietary_fiber + ", nf_cholesterol = " + nf_cholesterol + ", photo = " + photo + ", upc = " + upc + ", nf_potassium = " + nf_potassium + ", nix_brand_name = " + nix_brand_name + ", full_nutrients = " + full_nutrients + ", nix_item_id = " + nix_item_id + ", source = " + source + ", nix_item_name = " + nix_item_name + ", serving_weight_grams = " + serving_weight_grams + ", nix_brand_id = " + nix_brand_id + ", serving_qty = " + serving_qty + "]";
    }






}
