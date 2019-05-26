package fitness.albert.com.pumpit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import fitness.albert.com.pumpit.Adapter.FoodListAdapter;
import fitness.albert.com.pumpit.Model.FireBaseInit;
import fitness.albert.com.pumpit.Model.Foods;
import fitness.albert.com.pumpit.Model.SavePref;
import fitness.albert.com.pumpit.Model.UserRegister;
import me.himanshusoni.quantityview.QuantityView;

public class ShowFoodBeforeAddedActivity extends AppCompatActivity implements QuantityView.OnQuantityChangeListener {

    //Todo check if spinner is empty

    private QuantityView quantityViewCustom;
    private Spinner spinnerServingUnit;
    private String spinnerSelectedItem;
    private TextView tvEnergy, tvCrabs, tvProtein, tvFat, tvServingWeightGrams, tvCholesterol,
            tvSodium, tvTotalCarbohydrate, tvDietaryFiber, tvSugars, tvPotassium, tvNfP, tvEnergyScroll,
            tvCrabsScroll, tvProteinScroll, tvFatScroll, tvSaturatedFat, tvCalcium,
            tvTotalSaturated, tvTotalLipid, tvTotalTrans, tvIron, tvTotalDietary, tvSugarsAdded,
            tvVitaminD, tvAlanine, tvAlcohol, tvArginine, tvAsh, tvAspartic, tvBetaine, tvCaffeine,
            tvCampesterol, tvCarotene, tvVitaminD3, tvCholine, tvCryptoxanthin, tvCopper, tvCystine,
            tvVitaminD2, tv161undifferentiated, tvPalmitoleicAcid, tv161t, tv181undifferentiated,
            tvVaccenicAcid, tv181t, tv18111t, tv182undifferentiated, tvClas, tvLinoleicAcid, tv182t,
            tv183undifferentiated, tvAla, tvCalendicAcid, tvEicosadienoicAcid, tv203undifferentiated,
            tvEicosatrienoicAcid, tvDihomoGammaLinolenicAcid, tv204undifferentiated, tvAdrenicAcid,
            tvEpa, tv221undifferentiated, tvDpa, tvDha, tvNervonicAcid, tvTotalMonounsaturated,
            tvTotalPolyunsaturated, tvTotalTransMonoenoic, tvTotalTransPolyenoic, tvFluoride,
            tvFolate, tvFolicAcid, tvFolateDfe, tvFolateFood, tvFructose, tvGalactose, tvGlutamicAcid,
            tvGlucose, tvGlycine, tvHistidine, tvHydroxyproline, tvLactose, tvLeucine,
            tvLuteinZeaxanthin, tvLycopene, tvLysine, tvMaltose, tvMethionine, tvMagnesium,
            tvMenaquinone, tvManganese, tvNiacin, tvVitaminE, tvVitaminB12, tvAdjustedProtein,
            tv221t, tv221c, tv183i, tvnotNurtherDefined, tv182i, tvPhosphorus, tvPantothenicAcid,
            tvPhenylalanine, tvPhytosterols, tvProline, tvRetinol, tvRiboflavin, tvSelenium, tvSerine,
            tvBetaSitosterol, tvStarch, tvStigmasterol, tvSucrose, tvTheobromine, tvThiamin, tvThreonine,
            tvVitaminEalphaTocopherol, tvTocopherolBeta, tvTocopherolDelta, tvTocopherolGamma,
            tvTryptophan, tvTyrosine, tvValine, tvVitaminAIU, tvVitaminARAE, tvVitaminB12_2,
            tvVitaminB6, tvVitaminCtotalAscorbicAcid, tvVitaminD2andD3, tvVitaminK,
            tvDihydrophylloquinone, tvWater, tvZinc;
    private ImageView ivFoodImg, btnSaveFood;
    private final String TAG = "ShowFoodBeforeAddedActivity";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private boolean testOnce = false;
    private float fKcal, fCrabs, fProtein, fFat, fCholesterol, fDietaryFiber, fNfP, fPotassium,
            fSaturatedFat, fSodium, fSugars;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_food_before_added);

        init();

        setActionBar();

        getFoodInfo();

        addItemsOnSpinner();

        onAddButtonClick();
    }

    private void init() {
        spinnerServingUnit = findViewById(R.id.spinner_serving_unit);
        tvEnergy = findViewById(R.id.tv_energy);
        tvCrabs = findViewById(R.id.tv_carbs);
        tvProtein = findViewById(R.id.tv_protein);
        tvFat = findViewById(R.id.tv_fat);
        btnSaveFood = findViewById(R.id.btn_save_food);


        //scroll
        tvEnergyScroll = findViewById(R.id.tv_nf_calories);
        tvCrabsScroll = findViewById(R.id.tv_nf_total_carbohydrate);
        tvProteinScroll = findViewById(R.id.tv_nf_protein);
        tvFatScroll = findViewById(R.id.tv_nf_total_fat);
        tvServingWeightGrams = findViewById(R.id.tv_serving_weight_grams);
        tvCholesterol = findViewById(R.id.tv_cholesterol);
        tvSodium = findViewById(R.id.tv_sodium);
        tvTotalCarbohydrate = findViewById(R.id.tv_total_carbohydrate);
        tvDietaryFiber = findViewById(R.id.tv_dietary_fiber);
        tvSugars = findViewById(R.id.tv_sugars);
        tvPotassium = findViewById(R.id.tv_potassium);
        tvNfP = findViewById(R.id.tv_nf_p);
        tvSaturatedFat = findViewById(R.id.tv_saturated_fat);
        ivFoodImg = findViewById(R.id.iv_food_img);
        tvCalcium = findViewById(R.id.calcium);
        tvTotalSaturated = findViewById(R.id.tv_total_saturated);
        tvTotalLipid = findViewById(R.id.tv_total_lipid);
        tvTotalTrans = findViewById(R.id.tv_total_trans);
        tvIron = findViewById(R.id.tv_iron);
        tvTotalDietary = findViewById(R.id.tv_total_dietary);
        tvVitaminD = findViewById(R.id.tv_vitamin_d);
        tvSugarsAdded = findViewById(R.id.tv_sugars_added);
        tvAlanine = findViewById(R.id.tv_alanine);
        tvAlcohol = findViewById(R.id.tv_alcohol);
        tvArginine = findViewById(R.id.tv_arginine);
        tvAsh = findViewById(R.id.tv_ash);
        tvAspartic = findViewById(R.id.tv_aspartic_acid);
        tvBetaine = findViewById(R.id.tv_betaine);
        tvCaffeine = findViewById(R.id.tv_caffeine);
        tvCampesterol = findViewById(R.id.tv_campesterol);
        tvCarotene = findViewById(R.id.tv_carotene_alpha);
        tvVitaminD3 = findViewById(R.id.tv_vitamin_d3);
        tvCholine = findViewById(R.id.tv_choline);
        tvCryptoxanthin = findViewById(R.id.tv_cryptoxanthin_beta);
        tvCopper = findViewById(R.id.tv_copper);
        tvCystine = findViewById(R.id.tv_cystine);
        tvVitaminD2 = findViewById(R.id.tv_Vitamin_d2);
        tv161undifferentiated = findViewById(R.id.tv_16_1_undifferentiated);
        tvPalmitoleicAcid = findViewById(R.id.tv_16_1c);
        tv161t = findViewById(R.id.tv_16_1t);
        tv181undifferentiated = findViewById(R.id.tv_18_1_undifferentiated);
        tvVaccenicAcid = findViewById(R.id.tv_18_1c);
        tv181t = findViewById(R.id.tv_18_1t);
        tv18111t = findViewById(R.id.tv_18_1_11t);
        tv182undifferentiated = findViewById(R.id.tv_18_2_undifferentiated);
        tvClas = findViewById(R.id.tv_cla);
        tvLinoleicAcid = findViewById(R.id.tv_18_2n6);
        tv182t = findViewById(R.id.tv_18_2t);
        tv183undifferentiated = findViewById(R.id.tv_18_3_undifferentiated);
        tvAla = findViewById(R.id.tv_ala);
        tvCalendicAcid = findViewById(R.id.tv_18_3n6);
        tvEicosadienoicAcid = findViewById(R.id.tv_20_2_n6);
        tv203undifferentiated = findViewById(R.id.tv_20_3_undifferentiated);
        tvEicosatrienoicAcid = findViewById(R.id.tv_20_3_n3);
        tvDihomoGammaLinolenicAcid = findViewById(R.id.tv_20_3_n6);
        tv204undifferentiated = findViewById(R.id.tv_20_4_undifferentiated);
        tvAdrenicAcid = findViewById(R.id.tv_20_4_n6);
        tvEpa = findViewById(R.id.tv_epa);
        tv221undifferentiated = findViewById(R.id.tv_22_1_undifferentiated);
        tvDpa = findViewById(R.id.tv_dpa);
        tvDha = findViewById(R.id.tv_dha);
        tvNervonicAcid = findViewById(R.id.tv_24_1c);
        tvTotalMonounsaturated = findViewById(R.id.tv_total_monounsaturated);
        tvTotalPolyunsaturated = findViewById(R.id.tv_total_polyunsaturated);
        tvTotalTransMonoenoic = findViewById(R.id.tv_total_trans_monoenoic);
        tvTotalTransPolyenoic = findViewById(R.id.tv_Total_trans_polyenoic);
        tvFluoride = findViewById(R.id.tv_fluoride);
        tvFolate = findViewById(R.id.tv_folate);
        tvFolicAcid = findViewById(R.id.tv_folic_acid);
        tvFolateDfe = findViewById(R.id.tv_folate_dfe);
        tvFolateFood = findViewById(R.id.tv_folate_food);
        tvFructose = findViewById(R.id.tv_fructose);
        tvGalactose = findViewById(R.id.tv_galactose);
        tvGlutamicAcid = findViewById(R.id.tv_glutamic_acid);
        tvGlucose = findViewById(R.id.tv_glucose);
        tvGlycine = findViewById(R.id.tv_glycine);
        tvHistidine = findViewById(R.id.tv_histidine);
        tvHydroxyproline = findViewById(R.id.tv_hydroxyproline);
        tvLactose = findViewById(R.id.tv_lactose);
        tvLeucine = findViewById(R.id.tv_leucine);
        tvLuteinZeaxanthin = findViewById(R.id.tv_Lutein_zeaxanthin);
        tvLycopene = findViewById(R.id.tv_lycopene);
        tvLysine = findViewById(R.id.tv_lysine);
        tvMaltose = findViewById(R.id.tv_maltose);
        tvMethionine = findViewById(R.id.tv_methionine);
        tvMagnesium = findViewById(R.id.tv_magnesium);
        tvMenaquinone = findViewById(R.id.tv_menaquinone);
        tvManganese = findViewById(R.id.tv_manganese);
        tvNiacin = findViewById(R.id.tv_niacin);
        tvVitaminE = findViewById(R.id.tv_vitamin_e);
        tvVitaminB12 = findViewById(R.id.tv_vitamin_b12);
        tvAdjustedProtein = findViewById(R.id.tv_adjusted_protein);
        tv221t = findViewById(R.id.tv_22_1t);
        tv221c = findViewById(R.id.tv_22_1c);
        tv183i = findViewById(R.id.tv_18_3i);
        tvnotNurtherDefined = findViewById(R.id.tv_not_further_defined);
        tv182i = findViewById(R.id.tv_18_2i);
        tvPhosphorus = findViewById(R.id.tv_phosphorus);
        tvPantothenicAcid = findViewById(R.id.tv_pantothenic);
        tvPhenylalanine = findViewById(R.id.tv_phenylalanine);
        tvPhytosterols = findViewById(R.id.tv_phytosterols);
        tvProline = findViewById(R.id.tv_proline);
        tvRetinol = findViewById(R.id.tv_retinol);
        tvRiboflavin = findViewById(R.id.tv_riboflavin);
        tvSelenium = findViewById(R.id.tv_selenium);
        tvSerine = findViewById(R.id.tv_serine);
        tvBetaSitosterol = findViewById(R.id.tv_beta_sitosterol);
        tvStarch = findViewById(R.id.tv_starch);
        tvStigmasterol = findViewById(R.id.tv_stigmasterol);
        tvSucrose = findViewById(R.id.tv_sucrose);
        tvTheobromine = findViewById(R.id.tv_theobromine);
        tvThiamin = findViewById(R.id.tv_thiamin);
        tvThreonine = findViewById(R.id.tv_threonine);
        tvVitaminEalphaTocopherol = findViewById(R.id.tv_vitamin_e_alpha_tocopherol);
        tvTocopherolBeta = findViewById(R.id.tv_tocopherol_beta);
        tvTocopherolDelta = findViewById(R.id.tv_tocopherol_delta);
        tvTocopherolGamma = findViewById(R.id.tv_tocopherol_gamma);
        tvTryptophan = findViewById(R.id.tv_tryptophan);
        tvTyrosine = findViewById(R.id.tv_tyrosine);
        tvValine = findViewById(R.id.tv_valine);
        tvVitaminAIU = findViewById(R.id.tv_vitamin_a_iu);
        tvVitaminARAE = findViewById(R.id.tv_vitamin_a_are);
        tvVitaminB12_2 = findViewById(R.id.tv_vitamin_b12_2);
        tvVitaminB6 = findViewById(R.id.tv_vitamin_b6);
        tvVitaminCtotalAscorbicAcid = findViewById(R.id.tv_vitamin_c_total_ascorbic_acid);
        tvVitaminD2andD3 = findViewById(R.id.tv_vitamin_d2_d3);
        tvVitaminK = findViewById(R.id.tv_vitamin_k);
        tvDihydrophylloquinone = findViewById(R.id.tv_dihydrophylloquinone);
        tvWater = findViewById(R.id.tv_water);
        tvZinc = findViewById(R.id.tv_zinc);


        quantityViewCustom = findViewById(R.id.quantityView_custom);
        quantityViewCustom.setOnQuantityChangeListener(this);
    }

    private void setActionBar() {
        ActionBar mToolbar;
        mToolbar = getSupportActionBar();
        String foodName = null;

        for (int i = 0; i < FoodListAdapter.mListItem.size(); i++) {
            foodName = FoodListAdapter.mListItem.get(i).getFood_name();
        }

        String foodNameCapitalizeFirstLetter = foodName.substring(0, 1).toUpperCase() + foodName.substring(1);
        mToolbar.setTitle(foodNameCapitalizeFirstLetter);

        // Create a TextView programmatically.
        TextView tv = new TextView(getApplicationContext());

        // Create a LayoutParams for TextView
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                android.app.ActionBar.LayoutParams.MATCH_PARENT, // Width of TextView
                android.app.ActionBar.LayoutParams.WRAP_CONTENT);

        // Apply the layout parameters to TextView widget
        tv.setLayoutParams(lp);

        // Set text to display in TextView
        tv.setText(mToolbar.getTitle());

        // Set the text color of TextView
        tv.setTextColor(Color.WHITE);

        //set the text size
        tv.setTextSize(20);

        // Set TextView text alignment to center
        tv.setGravity(Gravity.CENTER);

        mToolbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        //Set the newly created TextView as ActionBar custom view
        mToolbar.setCustomView(tv);
    }


    //Calculation the quantity + -
    public void onQuantityChanged(int oldQuantity, int newQuantity, boolean programmatically) {
        if (newQuantity == 0) {
            newQuantity = 1;
            quantityViewCustom.setQuantity(newQuantity);
        }
        getFoodInfo();
        calculateOnSpinnerChange();
    }


    @Override
    public void onLimitReached() {
        Log.d(getClass().getSimpleName(), "Limit reached");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    //Spinner Unit list
    @SuppressLint("LongLogTag")
    public void addItemsOnSpinner() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, FoodListAdapter.measure);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerServingUnit.setAdapter(dataAdapter);

        spinnerServingUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                spinnerSelectedItem = String.valueOf(spinnerServingUnit.getItemAtPosition(position));

                Log.d(TAG, "Spinner Item Position: " + spinnerServingUnit.getItemAtPosition(position));

                //Check if the position is change
                if (position > 0 || testOnce) {
                    testOnce = true;

                    quantityViewCustom.setQuantity(1);
                    calculateOnSpinnerChange();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @SuppressLint("LongLogTag")
    private void calculateOnSpinnerChange() {

        float kcal = 1, crabs = 1, protein = 1, fat = 1;
        try {
            float calAltMeasures = FoodListAdapter.measureMap.get(spinnerSelectedItem) / getServingWeightGrams() * quantityViewCustom.getQuantity();

            for (int i = 0; i < FoodListAdapter.mListItem.size(); i++) {

                kcal = FoodListAdapter.mListItem.get(i).getNf_calories();
                crabs = FoodListAdapter.mListItem.get(i).getNf_total_carbohydrate();
                protein = FoodListAdapter.mListItem.get(i).getNf_protein();
                fat = FoodListAdapter.mListItem.get(i).getNf_total_fat();
            }

            tvEnergy.setText(String.format(Locale.getDefault(), "%.0f Kcal", kcal * calAltMeasures));
            tvCrabs.setText(String.format(Locale.getDefault(), "%.2f g", crabs * calAltMeasures));
            tvProtein.setText(String.format(Locale.getDefault(), "%.2f g", protein * calAltMeasures));
            tvFat.setText(String.format(Locale.getDefault(), "%.2f g", fat * calAltMeasures));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private float getServingWeightGrams() {
        float servingWeight = 1;
        if(!FoodListAdapter.mListItem.isEmpty()) {
            for (int i = 0; i < FoodListAdapter.mListItem.size(); i++) {
                servingWeight = FoodListAdapter.mListItem.get(i).getServing_weight_grams();
            }
        }
        return servingWeight;
    }


    //get food info from searchFood
    private void getFoodInfo() {

        int quantity = quantityViewCustom.getQuantity();

        for (int i = 0; i < FoodListAdapter.mListItem.size(); i++) {
            String imgHigher = FoodListAdapter.mListItem.get(i).getPhoto().getHighres();

            Picasso.get().load(imgHigher).error(R.mipmap.ic_launcher).into(ivFoodImg);

            fKcal = FoodListAdapter.mListItem.get(i).getNf_calories();
            String energy = String.format(Locale.getDefault(), "%.0f", fKcal * quantity);
            this.tvEnergy.setText(energy + " kcal");
            this.tvEnergyScroll.setText("Energy: " + energy + " fKcal");


            fCrabs = FoodListAdapter.mListItem.get(i).getNf_total_carbohydrate();
            String crabs = String.format(Locale.getDefault(), "%.2f", fCrabs * quantity);
            this.tvCrabs.setText(crabs + " g");
            this.tvCrabsScroll.setText("Crabs " + crabs + " g");

            fProtein = FoodListAdapter.mListItem.get(i).getNf_protein();
            String protein = String.format(Locale.getDefault(), "%.2f", fProtein * quantity);
            this.tvProtein.setText(protein + " g");
            this.tvProteinScroll.setText("Protein: " + protein + " g");

            fFat = FoodListAdapter.mListItem.get(i).getNf_total_fat();
            String fat = String.format(Locale.getDefault(), "%.2f", fFat * quantity);
            this.tvFat.setText(fat + " g");
            this.tvFatScroll.setText("Fat: " + fat + " g");

            String weightGrams = String.format(Locale.getDefault(), "%.2f", FoodListAdapter.mListItem.get(i).getServing_weight_grams() * quantity);
            this.tvServingWeightGrams.setText("Weight grams: " + weightGrams + " g");

            fSaturatedFat = FoodListAdapter.mListItem.get(i).getNf_saturated_fat();
            String saturatedFat = String.format(Locale.getDefault(), "%.2f", fSaturatedFat * quantity);
            this.tvSaturatedFat.setText("Saturated fat: " + saturatedFat + " g");

            fCholesterol = FoodListAdapter.mListItem.get(i).getNf_cholesterol();
            String cholesterol = String.format(Locale.getDefault(), "%.2f", fCholesterol * quantity);
            this.tvCholesterol.setText("Cholesterol: " + cholesterol + " g");

            fSodium = FoodListAdapter.mListItem.get(i).getNf_sodium();
            String sodium = String.format(Locale.getDefault(), "%.2f", fSodium * quantity);
            this.tvSodium.setText("Sodium: " + sodium + " g");


            fDietaryFiber = FoodListAdapter.mListItem.get(i).getNf_dietary_fiber();
            String dietaryFiber = String.format(Locale.getDefault(), "%.2f", fDietaryFiber * quantity);
            this.tvDietaryFiber.setText("Dietary fiber: " + dietaryFiber + " g");

            fSugars = FoodListAdapter.mListItem.get(i).getNf_sugars();
            String sugars = String.format(Locale.getDefault(), "%.2f", fSugars * quantity);
            this.tvSugars.setText("Sugars: " + sugars + " g");

            fPotassium = FoodListAdapter.mListItem.get(i).getNf_potassium();
            String potassium = String.format(Locale.getDefault(), "%.2f", fPotassium * quantity);
            this.tvPotassium.setText("Potassium: " + potassium + " g");

            fNfP = FoodListAdapter.mListItem.get(i).getNf_p();
            String nf_p = String.format(Locale.getDefault(), "%.2f", fNfP * quantity);
            this.tvNfP.setText("Nutrition Facts Panel: " + nf_p + " g");


            for (int r = 0; r < FoodListAdapter.mListItem.get(i).getFull_nutrients().size(); r++) {

                String atterId = FoodListAdapter.mListItem.get(i).getFull_nutrients().get(r).getAttr_id();

                Float value = Float.valueOf(FoodListAdapter.mListItem.get(i).getFull_nutrients().get(r).getValue());

                switch (atterId) {
                    case "301":
                        String calcium = String.format(Locale.getDefault(), "Calcium: " + "%.2f", value * quantity);
                        this.tvCalcium.setText(calcium);
                        break;

                    case "205":
                        break;

                    case "601":
                        break;

                    case "208":
                        break;

                    case "606":
                        String totalSaturated = String.format(Locale.getDefault(), "Total Saturated: %.2f", value * quantity);
                        this.tvTotalSaturated.setText(totalSaturated);
                        break;

                    case "204":
                        String totalLipid = String.format(Locale.getDefault(), "Total lipid (fat): %.2f", value * quantity);
                        this.tvTotalLipid.setText(totalLipid);
                        break;

                    case "605":
                        String totalTrans = String.format(Locale.getDefault(), "Total Trans: %.2f", value * quantity);
                        this.tvTotalTrans.setText(totalTrans);
                        break;

                    case "303":
                        String iron = String.format(Locale.getDefault(), "Iron: %.2f", value * quantity);
                        this.tvIron.setText(iron);
                        break;

                    case "291":
                        String totalDietary = String.format(Locale.getDefault(), "Total Dietary: %.2f", value * quantity);
                        this.tvTotalDietary.setText(totalDietary);
                        break;

                    case "306":
                        break;

                    case "307":

                        break;

                    case "203":

                        break;

                    case "269":
                        //Sugars
                        break;

                    case "539":
                        String sugarsAdded = String.format(Locale.getDefault(), "Sugars Added: %.2f", value * quantity);
                        this.tvSugarsAdded.setText(sugarsAdded);
                        break;

                    case "324":
                        String vitaminD = String.format(Locale.getDefault(), "Vitamin D: %.2f", value * quantity);
                        this.tvVitaminD.setText(vitaminD);
                        break;

                    case "513":
                        String alanine = String.format(Locale.getDefault(), "Alanine: %.2f", value * quantity);
                        this.tvAlanine.setText(alanine);
                        break;

                    case "221":
                        String alcohol = String.format(Locale.getDefault(), "Alcohol: %.2f", value * quantity);
                        this.tvAlcohol.setText(alcohol);
                        break;

                    case "511":
                        String arginine = String.format(Locale.getDefault(), "Arginine: %.2f", value * quantity);
                        this.tvArginine.setText(arginine);
                        break;

                    case "207":
                        String ash = String.format(Locale.getDefault(), "Ash: %.2f", value * quantity);
                        this.tvAsh.setText(ash);
                        break;

                    case "514":
                        String aspartic = String.format(Locale.getDefault(), "Aspartic acid: %.2f", value * quantity);
                        this.tvAspartic.setText(aspartic);
                        break;

                    case "454":
                        String betaine = String.format(Locale.getDefault(), "Betaine: %.2f", value * quantity);
                        this.tvBetaine.setText(betaine);

                        break;

                    case "262":
                        String caffeine = String.format(Locale.getDefault(), "Caffeine: %.2f", value * quantity);
                        this.tvCaffeine.setText(caffeine);
                        break;

                    case "639":
                        String campesterol = String.format(Locale.getDefault(), "Campesterol: %.2f", value * quantity);
                        this.tvCampesterol.setText(campesterol);
                        break;

                    case "322":
                        String carotene = String.format(Locale.getDefault(), "Carotene, alpha: %.2f", value * quantity);
                        this.tvCarotene.setText(carotene);
                        break;

                    case "321":

                        break;

                    case "326":
                        String vitaminD3 = String.format(Locale.getDefault(), "Vitamin D3 (Cholecalciferol): %.2f", value * quantity);
                        this.tvVitaminD3.setText(vitaminD3);
                        break;

                    case "421":
                        String choline = String.format(Locale.getDefault(), "Choline: %.2f", value * quantity);
                        this.tvCholine.setText(choline);
                        break;

                    case "334":
                        String cryptoxanthin = String.format(Locale.getDefault(), "Cryptoxanthin, beta: %.2f", value * quantity);
                        this.tvCryptoxanthin.setText(cryptoxanthin);
                        break;

                    case "312":
                        String copper = String.format(Locale.getDefault(), "Copper: %.2f", value * quantity);
                        this.tvCopper.setText(copper);
                        break;

                    case "507":
                        String cystine = String.format(Locale.getDefault(), "Cystine: %.2f", value * quantity);
                        this.tvCystine.setText(cystine);
                        break;

                    case "268":
                        break;

                    case "325":
                        String vitaminD2 = String.format(Locale.getDefault(), "vitamin D2 (Ergocalciferol): %.2f", value * quantity);
                        this.tvVitaminD2.setText(vitaminD2);
                        break;

                    case "626":
                        String v161undifferentiated = String.format(Locale.getDefault(), "16:1 undifferentiated: %.2f", value * quantity);
                        this.tv161undifferentiated.setText(v161undifferentiated);
                        break;

                    case "673":
                        String palmitoleicAcid = String.format(Locale.getDefault(), "16:1 c (Palmitoleic Acid): %.2f", value * quantity);
                        this.tvPalmitoleicAcid.setText(palmitoleicAcid);
                        break;

                    case "662":
                        String v161t = String.format(Locale.getDefault(), "16:1 t: %.2f", value * quantity);
                        this.tv161t.setText(v161t);
                        break;

                    case "617":
                        String v181undifferentiated = String.format(Locale.getDefault(), "18:1 undifferentiated: %.2f", value * quantity);
                        this.tv181undifferentiated.setText(v181undifferentiated);
                        break;

                    case "674":
                        String vaccenicAcid = String.format(Locale.getDefault(), "18:1 c (Vaccenic acid): %.2f", value * quantity);
                        this.tvVaccenicAcid.setText(vaccenicAcid);
                        break;

                    case "663":
                        String v181t = String.format(Locale.getDefault(), "18:1 t: %.2f", value * quantity);
                        this.tv181t.setText(v181t);

                        break;

                    case "859":
                        String v18111t = String.format(Locale.getDefault(), "18:1-11t (18:1t n-7): %.2f", value * quantity);
                        this.tv18111t.setText(v18111t);
                        break;

                    case "618":
                        String v182undifferentiated = String.format(Locale.getDefault(), "18:2 undifferentiated: %.2f", value * quantity);
                        this.tv182undifferentiated.setText(v182undifferentiated);
                        break;

                    case "670":
                        String clas = String.format(Locale.getDefault(), "18:2 CLAs: %.2f", value * quantity);
                        this.tvClas.setText(clas);
                        break;

                    case "675":
                        String linoleicAcid = String.format(Locale.getDefault(), "18:2 n-6 c,c (Linoleic acid): %.2f", value * quantity);
                        this.tvLinoleicAcid.setText(linoleicAcid);
                        break;

                    case "669":
                        String v182t = String.format(Locale.getDefault(), "18:2 t,t: %.2f", value * quantity);
                        this.tv182t.setText(v182t);
                        break;

                    case "619":
                        String v183undifferentiated = String.format(Locale.getDefault(), "18:3 undifferentiated: %.2f", value * quantity);
                        this.tv183undifferentiated.setText(v183undifferentiated);
                        break;

                    case "851":
                        String ala = String.format(Locale.getDefault(), "18:3 n-3 c,c,c (ALA): %.2f", value * quantity);
                        this.tvAla.setText(ala);
                        break;

                    case "685":
                        String calendicAcid = String.format(Locale.getDefault(), "18:3 n-6 c,c,c (α-Calendic acid): %.2f", value * quantity);
                        this.tvCalendicAcid.setText(calendicAcid);
                        break;

                    case "672":
                        String eicosadienoicAcid = String.format(Locale.getDefault(), "20:2 n-6 c,c (Eicosadienoic acid): %.2f", value * quantity);
                        this.tvEicosadienoicAcid.setText(eicosadienoicAcid);
                        break;

                    case "689":
                        String v203undifferentiated = String.format(Locale.getDefault(), "20:3 undifferentiated: %.2f", value * quantity);
                        this.tv203undifferentiated.setText(v203undifferentiated);
                        break;

                    case "852":
                        String eicosatrienoicAcid = String.format(Locale.getDefault(), "20:3 n-3 (Eicosatrienoic acid (ETE): %.2f", value * quantity);
                        this.tvEicosatrienoicAcid.setText(eicosatrienoicAcid);
                        break;

                    case "853":
                        String dihomoGammaLinolenicAcid = String.format(Locale.getDefault(), "Dihomo-gamma-linolenic acid (DGLA): %.2f", value * quantity);
                        this.tvDihomoGammaLinolenicAcid.setText(dihomoGammaLinolenicAcid);
                        break;

                    case "620":
                        String v204undifferentiated = String.format(Locale.getDefault(), "20:4 undifferentiated: %.2f", value * quantity);
                        this.tv204undifferentiated.setText(v204undifferentiated);
                        break;

                    case "855":
                        String adrenicAcid = String.format(Locale.getDefault(), "20:4 n-6 (Adrenic acid (AdA)): %.2f", value * quantity);
                        this.tvAdrenicAcid.setText(adrenicAcid);
                        break;

                    case "629":
                        String epa = String.format(Locale.getDefault(), "20:5 n-3 (EPA): %.2f", value * quantity);
                        this.tvEpa.setText(epa);
                        break;

                    case "630":
                        String v221undifferentiated = String.format(Locale.getDefault(), "22:1 undifferentiated: %.2f", value * quantity);
                        this.tv221undifferentiated.setText(v221undifferentiated);
                        break;


                    case "631":
                        String dpa = String.format(Locale.getDefault(), "22:5 n-3 (DPA): %.2f", value * quantity);
                        this.tvDpa.setText(dpa);
                        break;

                    case "621":
                        String dha = String.format(Locale.getDefault(), "22:6 n-3 (DHA): %.2f", value * quantity);
                        this.tvDha.setText(dha);
                        break;

                    case "671":
                        String nervonicAcid = String.format(Locale.getDefault(), "24:1 c (Nervonic acid): %.2f", value * quantity);
                        this.tvNervonicAcid.setText(nervonicAcid);
                        break;

                    case "645":
                        String totalMonounsaturated = String.format(Locale.getDefault(), "Total Monounsaturated: %.2f", value * quantity);
                        this.tvTotalMonounsaturated.setText(totalMonounsaturated);
                        break;

                    case "646":
                        String totalPolyunsaturated = String.format(Locale.getDefault(), "Total Polyunsaturated: %.2f", value * quantity);
                        this.tvTotalPolyunsaturated.setText(totalPolyunsaturated);
                        break;

                    case "693":
                        String totalTransMonoenoic = String.format(Locale.getDefault(), "Total Trans Monoenoic: %.2f", value * quantity);
                        this.tvTotalTransMonoenoic.setText(totalTransMonoenoic);
                        break;

                    case "695":
                        String totalTransPolyenoic = String.format(Locale.getDefault(), "Total Trans Polyenoic: %.2f", value * quantity);
                        this.tvTotalTransPolyenoic.setText(totalTransPolyenoic);
                        break;

                    case "313":
                        String fluoride = String.format(Locale.getDefault(), "Fluoride: %.2f", value * quantity);
                        this.tvFluoride.setText(fluoride);
                        break;

                    case "417":
                        String folate = String.format(Locale.getDefault(), "Folate: %.2f", value * quantity);
                        this.tvFolate.setText(folate);
                        break;

                    case "431":
                        String folicAcid = String.format(Locale.getDefault(), "Folic Acid: %.2f", value * quantity);
                        this.tvFolicAcid.setText(folicAcid);
                        break;

                    case "435":
                        String folateDfe = String.format(Locale.getDefault(), "Folate Dfe: %.2f", value * quantity);
                        this.tvFolateDfe.setText(folateDfe);
                        break;

                    case "432":
                        String folateFood = String.format(Locale.getDefault(), "Folate Food: %.2f", value * quantity);
                        this.tvFolateFood.setText(folateFood);
                        break;

                    case "212":
                        String fructose = String.format(Locale.getDefault(), "Fructose: %.2f", value * quantity);
                        this.tvFructose.setText(fructose);
                        break;

                    case "287":
                        String galactose = String.format(Locale.getDefault(), "Galactose: %.2f", value * quantity);
                        this.tvGalactose.setText(galactose);
                        break;

                    case "515":
                        String glutamicAcid = String.format(Locale.getDefault(), "Glutamic Acid: %.2f", value * quantity);
                        this.tvGlutamicAcid.setText(glutamicAcid);
                        break;

                    case "211":
                        String glucose = String.format(Locale.getDefault(), "Glucose: %.2f", value * quantity);
                        this.tvGlucose.setText(glucose);

                        break;

                    case "516":
                        String glycine = String.format(Locale.getDefault(), "Glycine: %.2f", value * quantity);
                        this.tvGlycine.setText(glycine);
                        break;

                    case "512":
                        String histidine = String.format(Locale.getDefault(), "Histidine: %.2f", value * quantity);
                        this.tvHistidine.setText(histidine);
                        break;

                    case "521":
                        String hydroxyproline = String.format(Locale.getDefault(), "Hydroxyproline: %.2f", value * quantity);
                        this.tvHydroxyproline.setText(hydroxyproline);
                        break;

                    case "503":
                        // nutrientName = "Isoleucine";
                        break;

                    case "213":
                        String lactose = String.format(Locale.getDefault(), "Lactose: %.2f", value * quantity);
                        this.tvLactose.setText(lactose);
                        break;

                    case "504":
                        String leucine = String.format(Locale.getDefault(), "Leucine: %.2f", value * quantity);
                        this.tvLeucine.setText(leucine);
                        break;

                    case "338":
                        String luteinZeaxanthin = String.format(Locale.getDefault(), "Lutein + Zeaxanthin: %.2f", value * quantity);
                        this.tvLuteinZeaxanthin.setText(luteinZeaxanthin);
                        break;

                    case "337":
                        String lycopene = String.format(Locale.getDefault(), "Lycopene: %.2f", value * quantity);
                        this.tvLycopene.setText(lycopene);
                        break;

                    case "505":
                        String lysine = String.format(Locale.getDefault(), "Lysine: %.2f", value * quantity);
                        this.tvLysine.setText(lysine);
                        break;

                    case "214":
                        String maltose = String.format(Locale.getDefault(), "Maltose: %.2f", value * quantity);
                        this.tvMaltose.setText(maltose);
                        break;

                    case "506":
                        String methionine = String.format(Locale.getDefault(), "Methionine: %.2f", value * quantity);
                        this.tvMethionine.setText(methionine);
                        break;

                    case "304":
                        String magnesium = String.format(Locale.getDefault(), "Magnesium: %.2f", value * quantity);
                        this.tvMagnesium.setText(magnesium);
                        break;

                    case "428":
                        String menaquinone = String.format(Locale.getDefault(), "Menaquinone: %.2f", value * quantity);
                        this.tvMenaquinone.setText(menaquinone);
                        break;

                    case "315":
                        String manganese = String.format(Locale.getDefault(), "Manganese: %.2f", value * quantity);
                        this.tvManganese.setText(manganese);
                        break;

                    case "406":
                        String niacin = String.format(Locale.getDefault(), "Niacin: %.2f", value * quantity);
                        this.tvNiacin.setText(niacin);
                        break;

                    case "573":
                        String vitaminE = String.format(Locale.getDefault(), "Vitamin E: %.2f", value * quantity);
                        this.tvVitaminE.setText(vitaminE);
                        break;

                    case "578":
                        String vitaminB12 = String.format(Locale.getDefault(), "Vitamin B12: %.2f", value * quantity);
                        this.tvVitaminB12.setText(vitaminB12);
                        break;

                    case "257":
                        String adjustedProtein = String.format(Locale.getDefault(), "Adjusted Protein: %.2f", value * quantity);
                        this.tvAdjustedProtein.setText(adjustedProtein);
                        break;

                    case "664":
                        String v221t = String.format(Locale.getDefault(), "22:1 t: %.2f", value * quantity);
                        this.tv221t.setText(v221t);
                        break;

                    case "676":
                        String v221c = String.format(Locale.getDefault(), "22:1 c: %.2f", value * quantity);
                        this.tv221c.setText(v221c);
                        break;

                    case "856":
                        String v183i = String.format(Locale.getDefault(), "18:3i: %.2f", value * quantity);
                        this.tv183i.setText(v183i);
                        break;

                    case "665":
                        String notNurtherDefined = String.format(Locale.getDefault(), "18:2 t not further defined: %.2f", value * quantity);
                        this.tvnotNurtherDefined.setText(notNurtherDefined);
                        break;

                    case "666":
                        String v182i = String.format(Locale.getDefault(), "18:2 i: %.2f", value * quantity);
                        this.tv182i.setText(v182i);
                        break;

                    case "305":
                        String phosphorus = String.format(Locale.getDefault(), "Phosphorus, P: %.2f", value * quantity);
                        this.tvPhosphorus.setText(phosphorus);
                        break;

                    case "410":
                        String pantothenicAcid = String.format(Locale.getDefault(), "Pantothenic acid: %.2f", value * quantity);
                        this.tvPantothenicAcid.setText(pantothenicAcid);
                        break;

                    case "508":
                        String phenylalanine = String.format(Locale.getDefault(), "Phenylalanine: %.2f", value * quantity);
                        this.tvPhenylalanine.setText(phenylalanine);
                        break;

                    case "636":
                        String phytosterols = String.format(Locale.getDefault(), "Phytosterols: %.2f", value * quantity);
                        this.tvPhytosterols.setText(phytosterols);
                        break;

                    case "517":
                        String proline = String.format(Locale.getDefault(), "Proline: %.2f", value * quantity);
                        this.tvProline.setText(proline);
                        break;

                    case "319":
                        String retinol = String.format(Locale.getDefault(), "Retinol: %.2f", value * quantity);
                        this.tvRetinol.setText(retinol);
                        break;

                    case "405":
                        String riboflavin = String.format(Locale.getDefault(), "Riboflavin: %.2f", value * quantity);
                        this.tvRiboflavin.setText(riboflavin);
                        break;

                    case "317":
                        String selenium = String.format(Locale.getDefault(), "Selenium, Se: %.2f", value * quantity);
                        this.tvSelenium.setText(selenium);
                        break;

                    case "518":
                        String serine = String.format(Locale.getDefault(), "Serine: %.2f", value * quantity);
                        this.tvSerine.setText(serine);
                        break;

                    case "641":
                        String betaSitosterol = String.format(Locale.getDefault(), "Beta Sitosterol: %.2f", value * quantity);
                        this.tvBetaSitosterol.setText(betaSitosterol);
                        break;

                    case "209":
                        String starch = String.format(Locale.getDefault(), "Starch: %.2f", value * quantity);
                        this.tvStarch.setText(starch);
                        break;

                    case "638":
                        String stigmasterol = String.format(Locale.getDefault(), "Stigmasterol: %.2f", value * quantity);
                        this.tvStigmasterol.setText(stigmasterol);
                        break;

                    case "210":
                        String sucrose = String.format(Locale.getDefault(), "Sucrose: %.2f", value * quantity);
                        this.tvSucrose.setText(sucrose);
                        break;

                    case "263":
                        String theobromine = String.format(Locale.getDefault(), "Theobromine: %.2f", value * quantity);
                        this.tvTheobromine.setText(theobromine);
                        break;

                    case "404":
                        String thiamin = String.format(Locale.getDefault(), "Thiamin: %.2f", value * quantity);
                        this.tvThiamin.setText(thiamin);
                        break;

                    case "502":
                        String threonine = String.format(Locale.getDefault(), "Threonine: %.2f", value * quantity);
                        this.tvThreonine.setText(threonine);
                        break;

                    case "323":
                        String vitaminEalphaTocopherol = String.format(Locale.getDefault(), "Vitamin E (alpha-tocopherol): %.2f", value * quantity);
                        this.tvVitaminEalphaTocopherol.setText(vitaminEalphaTocopherol);
                        break;

                    case "341":
                        String tocopherolBeta = String.format(Locale.getDefault(), "Tocopherol, beta: %.2f", value * quantity);
                        this.tvTocopherolBeta.setText(tocopherolBeta);
                        break;

                    case "343":
                        String tocopherolDelta = String.format(Locale.getDefault(), "Tocopherol, delta: %.2f", value * quantity);
                        this.tvTocopherolDelta.setText(tocopherolDelta);
                        break;

                    case "342":
                        String tocopherolGamma = String.format(Locale.getDefault(), "Tocopherol, gamma: %.2f", value * quantity);
                        this.tvTocopherolGamma.setText(tocopherolGamma);

                        break;

                    case "501":
                        String tryptophan = String.format(Locale.getDefault(), "Tryptophan: %.2f", value * quantity);
                        this.tvTryptophan.setText(tryptophan);
                        break;

                    case "509":
                        String tyrosine = String.format(Locale.getDefault(), "Tyrosine: %.2f", value * quantity);
                        this.tvTyrosine.setText(tyrosine);
                        break;

                    case "510":
                        String valine = String.format(Locale.getDefault(), "Valine: %.2f", value * quantity);
                        this.tvValine.setText(valine);
                        break;

                    case "318":
                        String vitaminAIU = String.format(Locale.getDefault(), "Vitamin A, IU: %.2f", value * quantity);
                        this.tvVitaminAIU.setText(vitaminAIU);
                        break;

                    case "320":
                        String vitaminARAE = String.format(Locale.getDefault(), "Vitamin A, RAE: %.2f", value * quantity);
                        this.tvVitaminARAE.setText(vitaminARAE);

                        break;

                    case "418":
                        String vitaminB12_2 = String.format(Locale.getDefault(), "Vitamin B12 2: %.2f", value * quantity);
                        this.tvVitaminB12_2.setText(vitaminB12_2);
                        break;

                    case "415":
                        String vitaminB6 = String.format(Locale.getDefault(), "Vitamin B6: %.2f", value * quantity);
                        this.tvVitaminB6.setText(vitaminB6);
                        break;

                    case "401":
                        String vitaminCtotalAscorbicAcid = String.format(Locale.getDefault(), "Vitamin C, total ascorbic acid: %.2f", value * quantity);
                        this.tvVitaminCtotalAscorbicAcid.setText(vitaminCtotalAscorbicAcid);
                        break;

                    case "328":
                        String vitaminD2andD3 = String.format(Locale.getDefault(), "Vitamin D (D2 + D3): %.2f", value * quantity);
                        this.tvVitaminD2andD3.setText(vitaminD2andD3);
                        break;

                    case "430":
                        String vitaminK = String.format(Locale.getDefault(), "Vitamin K (phylloquinone): %.2f", value * quantity);
                        this.tvVitaminK.setText(vitaminK);
                        break;

                    case "429":
                        String dihydrophylloquinone = String.format(Locale.getDefault(), "Dihydrophylloquinone: %.2f", value * quantity);
                        this.tvDihydrophylloquinone.setText(dihydrophylloquinone);
                        break;

                    case "255":
                        String water = String.format(Locale.getDefault(), "Water: %.2f", value * quantity);
                        this.tvWater.setText(water);
                        break;

                    case "309":
                        String zinc = String.format(Locale.getDefault(), "Zinc, Zn: %.2f", value * quantity);
                        this.tvZinc.setText(zinc);
                        break;
                }
            }
        }
    }


    @SuppressLint("LongLogTag")
    private void saveDataToFirestore() {

        float measureMapIsNull = FoodListAdapter.measureMap.get(spinnerSelectedItem) == null ? 1 : FoodListAdapter.measureMap.get(spinnerSelectedItem);

        float calAltMeasures = measureMapIsNull / getServingWeightGrams() * quantityViewCustom.getQuantity();

        float kcal = 1, crabs = 1, protein = 1, fat = 1, cholesterol = 1, dietaryFiber = 1,
                nfp = 1, potassium = 1, saturatedFat = 1, sodium = 1, sugars = 1;

        for (int i = 0; i < FoodListAdapter.mListItem.size(); i++) {
            kcal = FoodListAdapter.mListItem.get(i).getNf_calories();
            crabs = FoodListAdapter.mListItem.get(i).getNf_total_carbohydrate();
            protein = FoodListAdapter.mListItem.get(i).getNf_protein();
            fat = FoodListAdapter.mListItem.get(i).getNf_total_fat();
            cholesterol = FoodListAdapter.mListItem.get(i).getNf_cholesterol();
            dietaryFiber = FoodListAdapter.mListItem.get(i).getNf_dietary_fiber();
            nfp = FoodListAdapter.mListItem.get(i).getNf_p();
            potassium = FoodListAdapter.mListItem.get(i).getNf_potassium();
            saturatedFat = FoodListAdapter.mListItem.get(i).getNf_saturated_fat();
            sodium = FoodListAdapter.mListItem.get(i).getNf_sodium();
            sugars = FoodListAdapter.mListItem.get(i).getNf_sugars();
        }

        try {
            for (int i = 0; i < FoodListAdapter.mListItem.size(); i++) {
                if (!FoodListAdapter.mListItem.get(i).getAlt_measures().isEmpty()) {
                    FoodListAdapter.mListItem.get(i).setServing_weight_grams(FoodListAdapter.measureMap.get(spinnerSelectedItem));
                    FoodListAdapter.mListItem.get(i).setServing_unit(spinnerSelectedItem);
                    FoodListAdapter.mListItem.get(i).setNf_calories(kcal * calAltMeasures);
                    FoodListAdapter.mListItem.get(i).setNf_total_carbohydrate(crabs * calAltMeasures);
                    FoodListAdapter.mListItem.get(i).setNf_protein(protein * calAltMeasures);
                    FoodListAdapter.mListItem.get(i).setNf_total_fat(fat * calAltMeasures);
                    FoodListAdapter.mListItem.get(i).setNf_cholesterol(cholesterol * calAltMeasures);
                    FoodListAdapter.mListItem.get(i).setNf_dietary_fiber(dietaryFiber * calAltMeasures);
                    FoodListAdapter.mListItem.get(i).setNf_p(nfp * calAltMeasures);
                    FoodListAdapter.mListItem.get(i).setNf_potassium(potassium * calAltMeasures);
                    FoodListAdapter.mListItem.get(i).setNf_saturated_fat(saturatedFat * calAltMeasures);
                    FoodListAdapter.mListItem.get(i).setNf_sodium(sodium * calAltMeasures);
                    FoodListAdapter.mListItem.get(i).setNf_sugars(sugars * calAltMeasures);
                    FoodListAdapter.mListItem.get(i).setServing_qty(quantityViewCustom.getQuantity());
                }
                for (int j = 0; j < FoodListAdapter.mListItem.get(i).getAlt_measures().size(); j++) {
                    String atterId = FoodListAdapter.mListItem.get(i).getFull_nutrients().get(j).getAttr_id();

                    if (atterId.equals(FoodListAdapter.mListItem.get(i).getFull_nutrients().get(j).getAttr_id())) {
                        Float value = Float.valueOf(FoodListAdapter.mListItem.get(i).getFull_nutrients().get(j).getValue());
                        FoodListAdapter.mListItem.get(i).getFull_nutrients().get(j).setValue(String.valueOf(value * calAltMeasures));
                    }
                }
            }

            //CollectionPatch -> get myEmail -> get myMeal -> get the dayDate
            db.collection(Foods.NUTRITION).document(FireBaseInit.getEmailRegister())
                    .collection(getMeal()).document(UserRegister.getTodayData())
                    .collection(Foods.FRUIT).add(FoodListAdapter.mListItem.get(FoodListAdapter.mItemPosition))
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @SuppressLint("LongLogTag")
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            FoodListAdapter.measure.clear();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document ", e);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //get Meal from SharedPreferences file
    public String getMeal() {

        SharedPreferences pref = getSharedPreferences(Foods.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);

        boolean breakfast = pref.getBoolean("dinner", false);
        boolean dinner = pref.getBoolean("breakfast", false);
        boolean lunch = pref.getBoolean("lunch", false);
        boolean snack = pref.getBoolean("snack", false);

        if (breakfast) {
            return "dinner";
        }
        if (dinner) {
            return "breakfast";
        }
        if (lunch) {
            return "lunch";
        }
        if (snack) {
            return "snack";
        } else {
            return null;
        }
    }

    private void onAddButtonClick() {
        btnSaveFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataToFirestore();

                //save activity to sharedPreferences
                SavePref savePref = new SavePref();
                savePref.createSharedPreferencesFiles(ShowFoodBeforeAddedActivity.this, "activity");
                savePref.saveData("fromActivity", "ShowFoodBeforeAddedActivity");
                //startActivity(new Intent(ShowFoodBeforeAddedActivity.this, FragmentNavigationActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FoodListAdapter.measure.clear();
    }

//    public String getEmailRegister() {
//        String email = null;
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        if (mAuth.getCurrentUser() != null) {
//            email = mAuth.getCurrentUser().getEmail();
//        }
//        return email;
//    }

//    public String getTodayDate() {
//        Date c = Calendar.getInstance().getTime();
//        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
//        return df.format(c);
//    }
}