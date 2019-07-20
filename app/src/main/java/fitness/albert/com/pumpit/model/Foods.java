package fitness.albert.com.pumpit.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

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


    @SerializedName("serving_unit")
    private String servingUnit;

    @SerializedName("nf_total_carbohydrate")
    private float nfTotalCarbohydrate;

    @SerializedName("nf_sodium")
    private float nfSodium;

    private String lng;

    @SerializedName("nf_p")
    private float nfP;

    @SerializedName("alt_measures")
    private List<AltMeasures> altMeasures;

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


    @SerializedName("nf_sugars")
    private float nfSugars;

    @SerializedName("nf_calories")
    private float nfCalories;

    @SerializedName("brand_name")
    private String brandName;

    private String lat;

    private Metadata metadata;

    private Tags tags;

    @SerializedName("sub_recipe")
    private String subRecipe;

    @SerializedName("ndb_no")
    private String ndbNo;

    @SerializedName("food_name")
    private String foodName;

    @SerializedName("meal_type")
    private String mealType;

    @SerializedName("nf_saturated_fat")
    private float nfSaturatedFat;

    @SerializedName("nf_protein")
    private float nfProtein;

    @SerializedName("nf_total_fat")
    private float nfTotalFat;

    @SerializedName("consumed_at")
    private String consumedAt;

    @SerializedName("nf_dietary_fiber")
    private float nfDietaryFiber;

    @SerializedName("nf_cholesterol")
    private float nfCholesterol;

    private Photo photo;

    private String upc;

    @SerializedName("nf_potassium")
    private float nfPotassium;

    @SerializedName("nix_brand_name")
    private String nixBrandName;

    @SerializedName("full_nutrients")
    private List<FullNutrients> fullNutrients;

    @SerializedName("nix_item_id")
    private String nixItemId;

    private String source;

    @SerializedName("nix_item_name")
    private String nixItemName;

    @SerializedName("serving_weight_grams")
    private float servingWeightGrams;

    @SerializedName("nix_brand_id")
    private String nixBrandId;

    @SerializedName("serving_qty")
    private int servingQty;

    private String imgUrl;

    private String thumb;

    public String getServingUnit() {
        return servingUnit;
    }

    public void setServingUnit(String servingUnit) {
        this.servingUnit = servingUnit;
    }

    public float getNfTotalCarbohydrate() {
        return nfTotalCarbohydrate;
    }

    public void setNfTotalCarbohydrate(float nfTotalCarbohydrate) {
        this.nfTotalCarbohydrate = nfTotalCarbohydrate;
    }

    public float getNfSodium() {
        return nfSodium;
    }

    public void setNfSodium(float nfSodium) {
        this.nfSodium = nfSodium;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public float getNfP() {
        return nfP;
    }

    public void setNfP(float nfP) {
        this.nfP = nfP;
    }

    public List<AltMeasures> getAltMeasures() {
        return altMeasures;
    }

//    public AltMeasures[] getAltMeasures() {
//        return AltMeasures;
//    }
//
//    public void setAltMeasures(AltMeasures[] AltMeasures) {
//        this.AltMeasures = AltMeasures;
//    }

    public float getNfSugars() {
        return nfSugars;
    }

    public void setNfSugars(float nfSugars) {
        this.nfSugars = nfSugars;
    }

    public float getNfCalories() {
        return nfCalories;
    }

    public void setNfCalories(float nfCalories) {
        this.nfCalories = nfCalories;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
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

    public String getSubRecipe() {
        return subRecipe;
    }

    public void setSubRecipe(String subRecipe) {
        this.subRecipe = subRecipe;
    }

    public String getNdbNo() {
        return ndbNo;
    }

    public void setNdbNo(String ndbNo) {
        this.ndbNo = ndbNo;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public float getNfSaturatedFat() {
        return nfSaturatedFat;
    }

    public void setNfSaturatedFat(float nfSaturatedFat) {
        this.nfSaturatedFat = nfSaturatedFat;
    }

    public float getNfProtein() {
        return nfProtein;
    }

    public void setNfProtein(float nfProtein) {
        this.nfProtein = nfProtein;
    }

    public float getNfTotalFat() {
        return nfTotalFat;
    }

    public void setNfTotalFat(float nfTotalFat) {
        this.nfTotalFat = nfTotalFat;
    }

    public String getConsumedAt() {
        return consumedAt;
    }

    public void setConsumedAt(String consumedAt) {
        this.consumedAt = consumedAt;
    }

    public float getNfDietaryFiber() {
        return nfDietaryFiber;
    }

    public void setNfDietaryFiber(float nfDietaryFiber) {
        this.nfDietaryFiber = nfDietaryFiber;
    }

    public float getNfCholesterol() {
        return nfCholesterol;
    }

    public void setNfCholesterol(float nfCholesterol) {
        this.nfCholesterol = nfCholesterol;
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

    public float getNfPotassium() {
        return nfPotassium;
    }

    public void setNfPotassium(float nfPotassium) {
        this.nfPotassium = nfPotassium;
    }

    public String getNixBrandName() {
        return nixBrandName;
    }

    public void setNixBrandName(String nixBrandName) {
        this.nixBrandName = nixBrandName;
    }

    public List<FullNutrients> getFullNutrients() {
        return fullNutrients;
    }

    //    public FullNutrients[] getFullNutrients() {
//        return FullNutrients;
//    }
//
//    public void setFullNutrients(FullNutrients[] FullNutrients) {
//        this.FullNutrients = FullNutrients;
//    }

    public String getNixItemId() {
        return nixItemId;
    }

    public void setNixItemId(String nixItemId) {
        this.nixItemId = nixItemId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getNixItemName() {
        return nixItemName;
    }

    public void setNixItemName(String nixItemName) {
        this.nixItemName = nixItemName;
    }

    public float getServingWeightGrams() {
        return servingWeightGrams;
    }

    public void setServingWeightGrams(float servingWeightGrams) {
        this.servingWeightGrams = servingWeightGrams;
    }

    public String getNixBrandId() {
        return nixBrandId;
    }

    public void setNixBrandId(String nixBrandId) {
        this.nixBrandId = nixBrandId;
    }

    public int getServingQty() {
        return servingQty;
    }

    public void setServingQty(int servingQty) {
        this.servingQty = servingQty;
    }

    //    public String getServingQty() {
//        return servingQty;
//    }
//
//    public void setServingQty(String servingQty) {
//        this.servingQty = servingQty;
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


    public void setAltMeasures(List<AltMeasures> altMeasures) {
        this.altMeasures = altMeasures;
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

    public void setFullNutrients(List<FullNutrients> fullNutrients) {
        this.fullNutrients = fullNutrients;
    }

    @NonNull
    @Override
    public String toString() {
        return "ClassPojo [servingUnit = " + servingUnit + ", nfTotalCarbohydrate = " + nfTotalCarbohydrate + ", nfSodium = " + nfSodium + ", lng = " + lng + ", nfP = " + nfP + ", AltMeasures = " + altMeasures + ", nfSugars = " + nfSugars + ", nfCalories = " + nfCalories + ", brandName = " + brandName + ", lat = " + lat + ", metadata = " + metadata + ", tags = " + tags + ", subRecipe = " + subRecipe + ", ndbNo = " + ndbNo + ", foodName = " + foodName + ", mealType = " + mealType + ", nfSaturatedFat = " + nfSaturatedFat + ", nfProtein = " + nfProtein + ", nfTotalFat = " + nfTotalFat + ", consumedAt = " + consumedAt + ", nfDietaryFiber = " + nfDietaryFiber + ", nfCholesterol = " + nfCholesterol + ", photo = " + photo + ", upc = " + upc + ", nfPotassium = " + nfPotassium + ", nixBrandName = " + nixBrandName + ", FullNutrients = " + fullNutrients + ", nixItemId = " + nixItemId + ", source = " + source + ", nixItemName = " + nixItemName + ", servingWeightGrams = " + servingWeightGrams + ", nixBrandId = " + nixBrandId + ", servingQty = " + servingQty + "]";
    }


}
