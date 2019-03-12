package fitness.albert.com.pumpit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fitness.albert.com.pumpit.Adapter.FoodListAdapter;
import fitness.albert.com.pumpit.Model.Alt_measures;
import fitness.albert.com.pumpit.Model.Foods;
import fitness.albert.com.pumpit.Model.Photo;
import fitness.albert.com.pumpit.Model.SavePref;
import me.himanshusoni.quantityview.QuantityView;

public class ShowFoodBeforeAddedActivity extends AppCompatActivity implements QuantityView.OnQuantityChangeListener {

    private ActionBar mToolbar;
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
    private Foods foods = new Foods();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_food_before_added);

        init();

        setActionBar();

        getExtras(1);

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


    //Calculation the quantity + -
    public void onQuantityChanged(int oldQuantity, int newQuantity, boolean programmatically) {
        if (newQuantity == 0) {
            newQuantity = 1;
        }
        getExtras(newQuantity);
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }





    //get food info from searchFood
    private void getExtras(int quantity) {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();


        if (bundle != null) {

            String imgHigher = String.valueOf(bundle.get("photoHighres"));
            String imgThumb = String.valueOf(bundle.get("thumb"));

            //load img into ivFoodImg
            Picasso.get().load(imgHigher).error(R.mipmap.ic_launcher).into(ivFoodImg);

            String foodName = String.valueOf(bundle.get("food_name"));


            String energy = String.format("%.2f", bundle.getFloat("nf_calories") * quantity);
            this.tvEnergy.setText(energy + " kcal");
            this.tvEnergyScroll.setText("Energy: " + energy + " kcal");


            String crabs = String.format("%.2f", bundle.getFloat("nf_total_carbohydrate") * quantity);
            this.tvCrabs.setText(crabs + " g");
            this.tvCrabsScroll.setText("Crabs " + crabs + " g");


            String protein = String.format("%.2f", bundle.getFloat("nf_protein") * quantity);
            this.tvProtein.setText(protein + " g");
            this.tvProteinScroll.setText("Protein: " + protein + " g");


            String fat = String.format("%.2f", bundle.getFloat("nf_total_fat") * quantity);
            this.tvFat.setText(fat + " g");
            this.tvFatScroll.setText("Fat: " + fat + " g");


            String weightGrams = String.format("%.2f", bundle.getFloat("serving_weight_grams") * quantity);
            this.tvServingWeightGrams.setText("Weight grams: " + weightGrams + " g");

            String saturatedFat = String.format("%.2f", bundle.getFloat("nf_saturated_fat") * quantity);
            this.tvSaturatedFat.setText("Saturated fat: " + saturatedFat + " g");

            String cholesterol = String.format("%.2f", bundle.getFloat("nf_cholesterol") * quantity);
            this.tvCholesterol.setText("Cholesterol: " + cholesterol + " g");

            String sodium = String.format("%.2f", bundle.getFloat("nf_sodium") * quantity);
            this.tvSodium.setText("Sodium: " + sodium + " g");

            String carbohydrate = String.format("%.2f", bundle.getFloat("nf_total_carbohydrate") * quantity);
            this.tvTotalCarbohydrate.setText("Carbohydrate: " + carbohydrate + " g");

            String dietaryFiber = String.format("%.2f", bundle.getFloat("nf_dietary_fiber") * quantity);
            this.tvDietaryFiber.setText("Dietary fiber: " + dietaryFiber + " g");

            String sugars = String.format("%.2f", bundle.getFloat("nf_sugars") * quantity);
            this.tvSugars.setText("Sugars: " + sugars + " g");

            String potassium = String.format("%.2f", bundle.getFloat("nf_potassium") * quantity);
            this.tvPotassium.setText("Potassium: " + potassium + " g");

            String nf_p = String.format("%.2f", bundle.getFloat("nf_p") * quantity);
            this.tvNfP.setText("Nutrition Facts Panel: " + nf_p + " g");


            String calcium = String.format("Calcium: " + "%.2f", bundle.getFloat("Calcium") * quantity);
            this.tvCalcium.setText(calcium);


            String totalSaturated = String.format("Total Saturated: " + "%.2f", bundle.getFloat("TotalSaturated") * quantity);
            this.tvTotalSaturated.setText(totalSaturated);

            String totalLipid = String.format("Total Lipid (fat): " + "%.2f", bundle.getFloat("TotalFat") * quantity);
            this.tvTotalLipid.setText(totalLipid);

            String totalTrans = String.format("Total Trans: " + "%.2f", bundle.getFloat("TotalTrans") * quantity);
            this.tvTotalTrans.setText(totalTrans);

            String iron = String.format("Iron: " + "%.2f", bundle.getFloat("Iron") * quantity);
            this.tvIron.setText(iron);

            String totalDietary = String.format("Total Dietary: " + "%.2f", bundle.getFloat("TotalDietary") * quantity);
            this.tvTotalDietary.setText(totalDietary);

            String sugarsAdded = String.format("Sugars Added: " + "%.2f", bundle.getFloat("SugarsAadded") * quantity);
            this.tvSugarsAdded.setText(sugarsAdded);

            String vitaminD = String.format("Vitamin D: " + "%.2f", bundle.getFloat("VitaminD") * quantity);
            this.tvVitaminD.setText(vitaminD);

            String alanine = String.format("Alanine: " + "%.2f", bundle.getFloat("Alanine") * quantity);
            this.tvAlanine.setText(alanine);

            String alcohol = String.format("Alcohol: " + "%.2f", bundle.getFloat("Alcohol") * quantity);
            this.tvAlcohol.setText(alcohol);

            String arginine = String.format("Arginine: " + "%.2f", bundle.getFloat("Arginine") * quantity);
            this.tvArginine.setText(arginine);

            String aspartic = String.format("Aspartic: " + "%.2f", bundle.getFloat("AsparticAcid") * quantity);
            this.tvAspartic.setText(aspartic);

            String betaine = String.format("Betaine: " + "%.2f", bundle.getFloat("Betaine") * quantity);
            this.tvBetaine.setText(betaine);

            String caffeine = String.format("Caffeine: " + "%.2f", bundle.getFloat("Caffeine") * quantity);
            this.tvCaffeine.setText(caffeine);

            String ash = String.format("Ash: " + "%.2f", bundle.getFloat("Ash") * quantity);
            this.tvAsh.setText(ash);

            String campesterol = String.format("Campesterol: " + "%.2f", bundle.getFloat("Campesterol") * quantity);
            this.tvCampesterol.setText(campesterol);

            String carotene = String.format("Carotene: " + "%.2f", bundle.getFloat("CaroteneAlpha") * quantity);
            this.tvCarotene.setText(carotene);

            String vitaminD3 = String.format("Vitamin D3 (Cholecalciferol): " + "%.2f", bundle.getFloat("VitaminD3") * quantity);
            this.tvVitaminD3.setText(vitaminD3);

            String choline = String.format("Choline: " + "%.2f", bundle.getFloat("Choline") * quantity);
            this.tvCholine.setText(choline);

            String cryptoxanthin = String.format("Cryptoxanthin, beta: " + "%.2f", bundle.getFloat("Cryptoxanthin") * quantity);
            this.tvCryptoxanthin.setText(cryptoxanthin);

            String copper = String.format("Copper: " + "%.2f", bundle.getFloat("Copper") * quantity);
            this.tvCopper.setText(copper);

            String cystine = String.format("Cystine: " + "%.2f", bundle.getFloat("Cystine") * quantity);
            this.tvCystine.setText(cystine);

            String vitaminD2 = String.format("vitamin D2 (Ergocalciferol): " + "%.2f", bundle.getFloat("VitaminD2") * quantity);
            this.tvVitaminD2.setText(vitaminD2);

            String v161undifferentiated = String.format("16:1 undifferentiated: " + "%.2f", bundle.getFloat("16:1undifferentiated") * quantity);
            this.tv161undifferentiated.setText(v161undifferentiated);

            String palmitoleicAcid = String.format("16:1 c (Palmitoleic Acid): " + "%.2f", bundle.getFloat("16:1c") * quantity);
            this.tvPalmitoleicAcid.setText(palmitoleicAcid);

            String v161t = String.format("16:1 t: " + "%.2f", bundle.getFloat("16:1t") * quantity);
            this.tv161t.setText(v161t);

            String v181undifferentiated = String.format("18:1 undifferentiated: " + "%.2f", bundle.getFloat("18:1undifferentiated") * quantity);
            this.tv181undifferentiated.setText(v181undifferentiated);

            String vaccenicAcid = String.format("18:1 c (Vaccenic acid): " + "%.2f", bundle.getFloat("18:1c") * quantity);
            this.tvVaccenicAcid.setText(vaccenicAcid);

            String v18111t = String.format("18:1-11t (18:1t n-7): " + "%.2f", bundle.getFloat("18:1-11t") * quantity);
            this.tv18111t.setText(v18111t);

            String v181t = String.format("18:1 t: " + "%.2f", bundle.getFloat("18:1t") * quantity);
            this.tv181t.setText(v181t);

            String v182undifferentiated = String.format("18:2 undifferentiated: " + "%.2f", bundle.getFloat("18:2undifferentiated") * quantity);
            this.tv182undifferentiated.setText(v182undifferentiated);

            String clas = String.format("18:2 CLAs: " + "%.2f", bundle.getFloat("18:2CLAs") * quantity);
            this.tvClas.setText(clas);

            String linoleicAcid = String.format("18:2 n-6 c,c (Linoleic acid): " + "%.2f", bundle.getFloat("18:2n-6") * quantity);
            this.tvLinoleicAcid.setText(linoleicAcid);

            String v182t = String.format("18:2 t,t: " + "%.2f", bundle.getFloat("18:2t") * quantity);
            this.tv182t.setText(v182t);

            String v183undifferentiated = String.format("18:3 undifferentiated: " + "%.2f", bundle.getFloat("18:3undifferentiated") * quantity);
            this.tv183undifferentiated.setText(v183undifferentiated);

            String ala = String.format("18:3 n-3 c,c,c (ALA): " + "%.2f", bundle.getFloat("18:3n-3") * quantity);
            this.tvAla.setText(ala);

            String calendicAcid = String.format("18:3 n-6 c,c,c (α-Calendic acid): " + "%.2f", bundle.getFloat("18:3n-6") * quantity);
            this.tvCalendicAcid.setText(calendicAcid);

            String eicosadienoicAcid = String.format("20:2 n-6 c,c (Eicosadienoic acid): " + "%.2f", bundle.getFloat("20:2n-6") * quantity);
            this.tvEicosadienoicAcid.setText(eicosadienoicAcid);

            String v203undifferentiated = String.format("20:3 undifferentiated: " + "%.2f", bundle.getFloat("20:3undifferentiated") * quantity);
            this.tv203undifferentiated.setText(v203undifferentiated);

            String eicosatrienoicAcid = String.format("20:3 n-3 (Eicosatrienoic acid (ETE): " + "%.2f", bundle.getFloat("20:3n-3") * quantity);
            this.tvEicosatrienoicAcid.setText(eicosatrienoicAcid);

            String dihomoGammaLinolenicAcid = String.format("Dihomo-gamma-linolenic acid (DGLA): " + "%.2f", bundle.getFloat("20:3n-6") * quantity);
            this.tvDihomoGammaLinolenicAcid.setText(dihomoGammaLinolenicAcid);

            String v204undifferentiated = String.format("20:4 undifferentiated: " + "%.2f", bundle.getFloat("20:4undifferentiate") * quantity);
            this.tv204undifferentiated.setText(v204undifferentiated);

            String adrenicAcid = String.format("20:4 n-6 (Adrenic acid (AdA)): " + "%.2f", bundle.getFloat("20:4n-6") * quantity);
            this.tvAdrenicAcid.setText(adrenicAcid);

            String epa = String.format("20:5 n-3 (EPA): " + "%.2f", bundle.getFloat("20:5n-3") * quantity);
            this.tvEpa.setText(epa);

            String v221undifferentiated = String.format("22:1 undifferentiated: " + "%.2f", bundle.getFloat("22:1undifferentiated") * quantity);
            this.tv221undifferentiated.setText(v221undifferentiated);

            String dpa = String.format("22:5 n-3 (DPA): " + "%.2f", bundle.getFloat("22:5n-3") * quantity);
            this.tvDpa.setText(dpa);

            String dha = String.format("22:6 n-3 (DHA): " + "%.2f", bundle.getFloat("22:6n-3") * quantity);
            this.tvDha.setText(dha);

            String nervonicAcid = String.format("24:1 c (Nervonic acid): " + "%.2f", bundle.getFloat("24:1c") * quantity);
            this.tvNervonicAcid.setText(nervonicAcid);

            String totalMonounsaturated = String.format("Total Monounsaturated: " + "%.2f", bundle.getFloat("TotalMnounsaturated") * quantity);
            this.tvTotalMonounsaturated.setText(totalMonounsaturated);

            String totalPolyunsaturated = String.format("Total Polyunsaturated: " + "%.2f", bundle.getFloat("TotalPolyunsaturated") * quantity);
            this.tvTotalPolyunsaturated.setText(totalPolyunsaturated);

            String totalTransMonoenoic = String.format("Total Trans Monoenoic: " + "%.2f", bundle.getFloat("TotalTransMonoenoic") * quantity);
            this.tvTotalTransMonoenoic.setText(totalTransMonoenoic);

            String totalTransPolyenoic = String.format("Total Trans Polyenoic: " + "%.2f", bundle.getFloat("TotalTransPolyenoic") * quantity);
            this.tvTotalTransPolyenoic.setText(totalTransPolyenoic);

            String fluoride = String.format("Fluoride: " + "%.2f", bundle.getFloat("Fluoride") * quantity);
            this.tvFluoride.setText(fluoride);

            String folate = String.format("Folate: " + "%.2f", bundle.getFloat("Folate") * quantity);
            this.tvFolate.setText(folate);

            String folicAcid = String.format("Folic Acid: " + "%.2f", bundle.getFloat("FolicAcid") * quantity);
            this.tvFolicAcid.setText(folicAcid);

            String folateDfe = String.format("Folate Dfe: " + "%.2f", bundle.getFloat("FolateDFE") * quantity);
            this.tvFolateDfe.setText(folateDfe);

            String folateFood = String.format("Folate Food: " + "%.2f", bundle.getFloat("FolateFood") * quantity);
            this.tvFolateFood.setText(folateFood);

            String fructose = String.format("Fructose: " + "%.2f", bundle.getFloat("Fructose") * quantity);
            this.tvFructose.setText(fructose);

            String galactose = String.format("Galactose: " + "%.2f", bundle.getFloat("Galactose") * quantity);
            this.tvGalactose.setText(galactose);

            String glutamicAcid = String.format("Glutamic Acid " + "%.2f", bundle.getFloat("GlutamicAcid") * quantity);
            this.tvGlutamicAcid.setText(glutamicAcid);

            String glucose = String.format("Glucose: " + "%.2f", bundle.getFloat("Glucose") * quantity);
            this.tvGlucose.setText(glucose);

            String glycine = String.format("Glycine: " + "%.2f", bundle.getFloat("Glycine") * quantity);
            this.tvGlycine.setText(glycine);

            String histidine = String.format("Histidine: " + "%.2f", bundle.getFloat("Histidine") * quantity);
            this.tvHistidine.setText(histidine);

            String lactose = String.format("Lactose: " + "%.2f", bundle.getFloat("Lactose") * quantity);
            this.tvLactose.setText(lactose);

            String hydroxyproline = String.format("Hydroxyproline: " + "%.2f", bundle.getFloat("Hydroxyproline") * quantity);
            this.tvHydroxyproline.setText(hydroxyproline);

            String leucine = String.format("Leucine: " + "%.2f", bundle.getFloat("Leucine") * quantity);
            this.tvLeucine.setText(leucine);

            String luteinZeaxanthin = String.format("Lutein + Zeaxanthin: " + "%.2f", bundle.getFloat("LuteinzZaxanthin") * quantity);
            this.tvLuteinZeaxanthin.setText(luteinZeaxanthin);

            String lycopene = String.format("Lycopene: " + "%.2f", bundle.getFloat("Lycopene") * quantity);
            this.tvLycopene.setText(lycopene);

            String lysine = String.format("Lysine: " + "%.2f", bundle.getFloat("Lysine") * quantity);
            this.tvLysine.setText(lysine);

            String maltose = String.format("Maltose: " + "%.2f", bundle.getFloat("Maltose") * quantity);
            this.tvMaltose.setText(maltose);

            String methionine = String.format("Methionine: " + "%.2f", bundle.getFloat("Methionine") * quantity);
            this.tvMethionine.setText(methionine);

            String magnesium = String.format("Magnesium: " + "%.2f", bundle.getFloat("Magnesium") * quantity);
            this.tvMagnesium.setText(magnesium);

            String menaquinone = String.format("Menaquinone: " + "%.2f", bundle.getFloat("Menaquinone4") * quantity);
            this.tvMenaquinone.setText(menaquinone);

            String manganese = String.format("Manganese: " + "%.2f", bundle.getFloat("Manganese") * quantity);
            this.tvManganese.setText(manganese);

            String niacin = String.format("Niacin: " + "%.2f", bundle.getFloat("Niacin") * quantity);
            this.tvNiacin.setText(niacin);

            String vitaminE = String.format("Vitamin E: " + "%.2f", bundle.getFloat("VitaminE") * quantity);
            this.tvVitaminE.setText(vitaminE);

            String vitaminB12 = String.format("Vitamin B12: " + "%.2f", bundle.getFloat("VitaminB12") * quantity);
            this.tvVitaminB12.setText(vitaminB12);

            String adjustedProtein = String.format("Adjusted Protein: " + "%.2f", bundle.getFloat("AdjustedProtein") * quantity);
            this.tvAdjustedProtein.setText(adjustedProtein);

            String v221t = String.format("22:1 t: " + "%.2f", bundle.getFloat("22:1t") * quantity);
            this.tv221t.setText(v221t);

            String v221c = String.format("22:1 c: " + "%.2f", bundle.getFloat("22:1c") * quantity);
            this.tv221c.setText(v221c);

            String v183i = String.format("18:3i: " + "%.2f", bundle.getFloat("18:3i") * quantity);
            this.tv183i.setText(v183i);

            String notNurtherDefined = String.format("18:2 t not further defined: " + "%.2f", bundle.getFloat("18:2t") * quantity);
            this.tvnotNurtherDefined.setText(notNurtherDefined);

            String v182i = String.format("18:2 i: " + "%.2f", bundle.getFloat("18:2i") * quantity);
            this.tv182i.setText(v182i);

            String phosphorus = String.format("Phosphorus, P: " + "%.2f", bundle.getFloat("Phosphorus") * quantity);
            this.tvPhosphorus.setText(phosphorus);

            String pantothenicAcid = String.format("Pantothenic acid: " + "%.2f", bundle.getFloat("PantothenicAcid") * quantity);
            this.tvPantothenicAcid.setText(pantothenicAcid);

            String phenylalanine = String.format("Phenylalanine: " + "%.2f", bundle.getFloat("Phenylalanine") * quantity);
            this.tvPhenylalanine.setText(phenylalanine);

            String phytosterols = String.format("Phytosterols: " + "%.2f", bundle.getFloat("Phytosterols") * quantity);
            this.tvPhytosterols.setText(phytosterols);

            String proline = String.format("Proline: " + "%.2f", bundle.getFloat("Proline") * quantity);
            this.tvProline.setText(proline);

            String retinol = String.format("Retinol: " + "%.2f", bundle.getFloat("Retinol") * quantity);
            this.tvRetinol.setText(retinol);

            String riboflavin = String.format("Riboflavin: " + "%.2f", bundle.getFloat("Riboflavin") * quantity);
            this.tvRiboflavin.setText(riboflavin);

            String selenium = String.format("Selenium, Se: " + "%.2f", bundle.getFloat("Selenium") * quantity);
            this.tvSelenium.setText(selenium);

            String serine = String.format("Serine: " + "%.2f", bundle.getFloat("Serine") * quantity);
            this.tvSerine.setText(serine);

            String betaSitosterol = String.format("Beta Sitosterol: " + "%.2f", bundle.getFloat("BetaSitosterol") * quantity);
            this.tvBetaSitosterol.setText(betaSitosterol);

            String starch = String.format("Starch: " + "%.2f", bundle.getFloat("Starch") * quantity);
            this.tvStarch.setText(starch);

            String stigmasterol = String.format("Stigmasterol: " + "%.2f", bundle.getFloat("Stigmasterol") * quantity);
            this.tvStigmasterol.setText(stigmasterol);

            String sucrose = String.format("Sucrose: " + "%.2f", bundle.getFloat("Sucrose") * quantity);
            this.tvSucrose.setText(sucrose);

            String theobromine = String.format("Theobromine: " + "%.2f", bundle.getFloat("Theobromine") * quantity);
            this.tvTheobromine.setText(theobromine);

            String thiamin = String.format("Thiamin: " + "%.2f", bundle.getFloat("Thiamin") * quantity);
            this.tvThiamin.setText(thiamin);

            String threonine = String.format("Threonine: " + "%.2f", bundle.getFloat("Threonine") * quantity);
            this.tvThreonine.setText(threonine);

            String vitaminEalphaTocopherol = String.format("Vitamin E (alpha-tocopherol): " + "%.2f", bundle.getFloat("VitaminE") * quantity);
            this.tvVitaminEalphaTocopherol.setText(vitaminEalphaTocopherol);

            String tocopherolBeta = String.format("Tocopherol, beta: " + "%.2f", bundle.getFloat("TocopherolBeta") * quantity);
            this.tvTocopherolBeta.setText(tocopherolBeta);

            String tocopherolDelta = String.format("Tocopherol, delta: " + "%.2f", bundle.getFloat("TocopherolDelta") * quantity);
            this.tvTocopherolDelta.setText(tocopherolDelta);

            String tocopherolGamma = String.format("Tocopherol, gamma: " + "%.2f", bundle.getFloat("TocopherolGamma") * quantity);
            this.tvTocopherolGamma.setText(tocopherolGamma);

            String tryptophan = String.format("Tryptophan: " + "%.2f", bundle.getFloat("Tryptophan") * quantity);
            this.tvTryptophan.setText(tryptophan);

            String tyrosine = String.format("Tyrosine: " + "%.2f", bundle.getFloat("Tyrosine") * quantity);
            this.tvThreonine.setText(tyrosine);

            String valine = String.format("Valine: " + "%.2f", bundle.getFloat("Valine") * quantity);
            this.tvValine.setText(valine);

            String vitaminAIU = String.format("Vitamin A, IU: " + "%.2f", bundle.getFloat("VitaminAIu") * quantity);
            this.tvVitaminAIU.setText(vitaminAIU);

            String vitaminARAE = String.format("Vitamin A, RAE: " + "%.2f", bundle.getFloat("VitaminARae") * quantity);
            this.tvVitaminARAE.setText(vitaminARAE);

            String vitaminB12_2 = String.format("Vitamin B12 2: " + "%.2f", bundle.getFloat("VitaminB12-2") * quantity);
            this.tvVitaminB12_2.setText(vitaminB12_2);

            String vitaminB6 = String.format("Vitamin B6: " + "%.2f", bundle.getFloat("VitaminB6") * quantity);
            this.tvVitaminB6.setText(vitaminB6);

            String vitaminCtotalAscorbicAcid = String.format("Vitamin C, total ascorbic acid: " + "%.2f", bundle.getFloat("VitaminC") * quantity);
            this.tvVitaminCtotalAscorbicAcid.setText(vitaminCtotalAscorbicAcid);

            String vitaminD2andD3 = String.format("Vitamin D (D2 + D3): " + "%.2f", bundle.getFloat("VitaminD") * quantity);
            this.tvVitaminD2andD3.setText(vitaminD2andD3);

            String vitaminK = String.format("Vitamin K (phylloquinone): " + "%.2f", bundle.getFloat("VitaminK") * quantity);
            this.tvVitaminK.setText(vitaminK);

            String dihydrophylloquinone = String.format("Dihydrophylloquinone: " + "%.2f", bundle.getFloat("Dihydrophylloquinone") * quantity);
            this.tvDihydrophylloquinone.setText(dihydrophylloquinone);

            String water = String.format("Water: " + "%.2f", bundle.getFloat("Water") * quantity);
            this.tvWater.setText(water);

            String zinc = String.format("Zinc, Zn: " + "%.2f", bundle.getFloat("Zinc") * quantity);
            this.tvZinc.setText(zinc);

            String measuresName = bundle.getString("measureName");
            String measuresQty = bundle.getString("measureQty");
            String measuresSeq = bundle.getString("measureSeq");
            String measuresServing_weight = bundle.getString("measureServing_weight");

            Photo photo = new Photo();
            photo.setHighres(imgHigher);
            photo.setThumb(imgThumb);


            Alt_measures altMeasures = new Alt_measures();
            altMeasures.setMeasure(measuresName);
            altMeasures.setQty(measuresQty);
            altMeasures.setSeq(measuresSeq);
            altMeasures.setServing_weight(measuresServing_weight);

            List<Alt_measures> altMeasuresList = new ArrayList<>();
            altMeasuresList.add(altMeasures);

            foods.setAlt_measures(altMeasuresList);

            foods.setPhoto(photo);
            foods.getPhoto().setThumb(imgThumb);
            foods.setImgUrl(imgHigher);
            foods.setThumb(imgThumb);
            foods.setFood_name(foodName);
            foods.setNf_calories(Float.parseFloat(energy));
            foods.setNf_total_carbohydrate(Float.parseFloat(crabs));
            foods.setNf_protein(Float.parseFloat(protein));
            foods.setNf_total_fat(Float.parseFloat(fat));
            foods.setServing_weight_grams(Float.parseFloat(weightGrams));
            foods.setNf_saturated_fat(Float.parseFloat(saturatedFat));
            foods.setNf_cholesterol(Float.parseFloat(cholesterol));
            foods.setNf_sodium(Float.parseFloat(sodium));
            foods.setNf_total_carbohydrate(Float.parseFloat(carbohydrate));
            foods.setNf_dietary_fiber(Float.parseFloat(dietaryFiber));
            foods.setNf_sugars(Float.parseFloat(sugars));
            foods.setNf_potassium(Float.parseFloat(potassium));
            foods.setNf_p(Float.parseFloat(nf_p));
            foods.setServing_qty(Integer.valueOf(quantity));
            foods.setServing_unit(String.valueOf(bundle.get("serving_unit")));


            foods.setValine(valine);
            foods.setVitaminAIU(vitaminAIU);
            foods.setVitaminARAE(vitaminARAE);
            foods.setVitaminB122(vitaminB12_2);
            foods.setVitaminB6(vitaminB6);
            foods.setVitaminC(vitaminCtotalAscorbicAcid);
            foods.setVitaminD2AndD3(vitaminD2andD3);
            foods.setVitaminK(vitaminK);
            foods.setDihydrophylloquinone(dihydrophylloquinone);
            foods.setAacohol(alcohol);
            foods.setWater(water);
            foods.setZinc(zinc);

        }
    }


    //  db.collection("users").document(userRegister.getEmail()).set(saveData)

    @SuppressLint("LongLogTag")
    private void saveDataToFirestore() {
        try {

            SearchFoodsActivity.mListItem.get(0).setServing_unit(spinnerSelectedItem);

            //CollectionPatch -> get myEmail -> get myMeal -> get the dayDate
            db.collection(Foods.nutrition).document(getEmailRegister())
                    .collection(getMeal()).document(getTodayDate())
                    .collection(Foods.fruit).add(FoodListAdapter.mListItem.get(FoodListAdapter.mItemPosition))
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @SuppressLint("LongLogTag")
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: ");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });

            Log.d(TAG, "Food data save successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //get Meal from SharedPreferences file
    public String getMeal() {

        SharedPreferences pref = getSharedPreferences(Foods.SharedPreferencesFile, Context.MODE_PRIVATE);

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
                savePref.saveData("FROM_ACTIVITY", "ShowFoodBeforeAddedActivity");
                startActivity(new Intent(ShowFoodBeforeAddedActivity.this, FragmentNavigationActivity.class));
                finish();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FoodListAdapter.measure.clear();
        //finish();
    }

    public String getEmailRegister() {
        String email = null;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            email = mAuth.getCurrentUser().getEmail();
        }
        return email;
    }

    public String getTodayDate() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        return df.format(c);
    }

    private void setActionBar() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mToolbar = getSupportActionBar();
        String foodName = null;


        if (bundle != null) {

            foodName = String.valueOf(bundle.get("food_name"));
        }
        String foodNameCapitalizeFirstLetter = foodName.substring(0,1).toUpperCase() + foodName.substring(1);
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
}