package fitness.albert.com.pumpit.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import fitness.albert.com.pumpit.Model.Foods;
import fitness.albert.com.pumpit.R;
import fitness.albert.com.pumpit.ShowFoodBeforeAddedActivity;

public class FoodListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    public static ArrayList<Foods> mListItem;


    private ClickListener clickListener;
    private final String TAG = "FoodListAdapter";
    public static List<String> measure = new ArrayList<>();


    public FoodListAdapter(Context ctx, ArrayList<Foods> mListItem) {
        this.mContext = ctx;
        this.mListItem = mListItem;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_food, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindViews((ViewHolder) holder, position);
    }


    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;

    }

    public interface ClickListener {
        void itemClicked(View view, int position);

    }

    private void bindViews(final ViewHolder holder, final int position) {

        Picasso.get()
                .load(mListItem.get(position).getPhoto().getThumb())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.ivImage);

        holder.tvFoodName.setText(mListItem.get(position).getFood_name());
        holder.tvCalories.setText(mContext.getString(R.string.calories) + ": " + mListItem.get(position).getNf_calories());
        holder.tvServingQuantity.setText(mContext.getString(R.string.serving_qty) + ": " + mListItem.get(position).getServing_qty());
        holder.tvServingUnit.setText(mContext.getString(R.string.serving_unit) + ": " + mListItem.get(position).getServing_unit());
        holder.llSelectedItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, ShowFoodBeforeAddedActivity.class);
                putExtra(intent, position);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mListItem.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView ivImage;
        private TextView tvFoodName;
        private TextView tvCalories;
        private TextView tvServingQuantity;
        private TextView tvServingUnit;
        private LinearLayout llSelectedItem;


        public ViewHolder(View rowView) {
            super(rowView);
            rowView.setOnClickListener(this);

            ivImage = rowView.findViewById(R.id.ivImage);
            tvFoodName = rowView.findViewById(R.id.tvFoodName);
            tvCalories = rowView.findViewById(R.id.tvCalories);
            tvServingQuantity = rowView.findViewById(R.id.tvServingQuantity);
            tvServingUnit = rowView.findViewById(R.id.tvServingUnit);
            llSelectedItem = rowView.findViewById(R.id.item_food_selected);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.itemClicked(view, getAdapterPosition());
            }
        }
    }


    private void putExtra(Intent intent, final int position) {

        Log.d(TAG, "onClick: clicked on: " + mListItem.get(position));

        int pos = position;

        for (int i = 0; i < mListItem.get(position).getFull_nutrients().size(); i++) {
//            Log.d(TAG, "full nutrients getAtterId " + String.valueOf(mListItem.get(position).getFull_nutrients().get(i).getAttr_id()));
//            Log.d(TAG, "full nutrients getValue " + String.valueOf(mListItem.get(position).getFull_nutrients().get(i).getValue()));


            String nutrientName;

            String atterId = mListItem.get(position).getFull_nutrients().get(i).getAttr_id();

            Float value = Float.valueOf(mListItem.get(position).getFull_nutrients().get(i).getValue());



            switch (atterId) {
                case "301":
                    intent.putExtra("Calcium", value);

                    break;

                case "205":
                    nutrientName = "Carbohydrate";
                    intent.putExtra("Carbohydrate", value);

                    break;

                case "601":
                    nutrientName = "Cholesterol";
                    intent.putExtra("Cholesterol", value);

                    break;

                case "208":
                    nutrientName = "Energy";
                    intent.putExtra("Energy", value);

                    break;

                case "606":
                    nutrientName = "Total saturated";
                    intent.putExtra("TotalSaturated", value);

                    break;

                case "204":
                    nutrientName = "Total lipid (fat)";
                    intent.putExtra("TotalFat", value);

                    break;

                case "605":
                    nutrientName = "Total trans";
                    intent.putExtra("TotalTrans", value);

                    break;

                case "303":
                    nutrientName = "Iron";
                    intent.putExtra("Iron", value);

                    break;

                case "291":
                    nutrientName = "Total dietary";
                    intent.putExtra("TotalDietary", value);

                    break;

                case "306":
                    nutrientName = "Potassium";
                    intent.putExtra("Potassium", value);

                    break;

                case "307":
                    nutrientName = "Sodium";
                    intent.putExtra("Sodium", value);

                    break;

                case "203":
                    nutrientName = "Protein";
                    intent.putExtra("Protein", value);
                    break;

                case "269":
                    nutrientName = "Sugars";
                    intent.putExtra("Sugars", value);
                    break;

                case "539":
                    nutrientName = "Sugars, added";
                    intent.putExtra("SugarsAadded", value);
                    break;

                case "324":
                    nutrientName = "Vitamin D";
                    intent.putExtra("VitaminD", value);
                    break;

                case "513":
                    nutrientName = "Alanine";
                    intent.putExtra("Alanine", value);
                    break;

                case "221":
                    nutrientName = "Alcohol";
                    intent.putExtra("Alcohol", value);

                    break;

                case "511":
                    nutrientName = "Arginine";
                    intent.putExtra("Arginine", value);
                    break;

                case "207":
                    nutrientName = "Ash";
                    intent.putExtra("Ash", value);
                    break;

                case "514":
                    nutrientName = "Aspartic acid";
                    intent.putExtra("AsparticAcid", value);
                    break;

                case "454":
                    nutrientName = "Betaine";
                    intent.putExtra("Betaine", value);

                    break;

                case "262":
                    nutrientName = "Caffeine";
                    intent.putExtra("Caffeine", value);
                    break;

                case "639":
                    nutrientName = "Campesterol";
                    intent.putExtra("Campesterol", value);
                    break;

                case "322":
                    nutrientName = "Carotene, alpha";
                    intent.putExtra("CaroteneAlpha", value);
                    break;

                case "321":
                    nutrientName = "Carotene, beta";
                    intent.putExtra("Carotene", value);
                    break;

                case "326":
                    nutrientName = "Vitamin D3 (cholecalciferol)";
                    intent.putExtra("VitaminD3", value);
                    break;

                case "421":
                    nutrientName = "Choline";
                    intent.putExtra("Choline", value);
                    break;

                case "334":
                    nutrientName = "Cryptoxanthin, beta";
                    intent.putExtra("Cryptoxanthin", value);
                    break;

                case "312":
                    nutrientName = "Copper, Cu";
                    intent.putExtra("Copper", value);
                    break;

                case "507":
                    nutrientName = "Cystine";
                    intent.putExtra("Cystine", value);
                    break;

                case "268":
                    nutrientName = "Energy";
                    intent.putExtra("Energy", value);
                    break;

                case "325":
                    nutrientName = "Vitamin D2 (ergocalciferol)";
                    intent.putExtra("VitaminD2", value);
                    break;

                case "610":
                    nutrientName = "10:00:00 AM";
                    intent.putExtra("10AM", value);
                    break;

                case "611":
                    nutrientName = "12:00:00 PM";
                    intent.putExtra("12PM", value);
                    break;

                case "696":
                    nutrientName = "1:00:00 PM";
                    intent.putExtra("1PM", value);
                    break;

                case "612":
                    nutrientName = "2:00:00 PM";
                    intent.putExtra("2PM", value);
                    break;

                case "625":
                    nutrientName = "2:01:00 PM";
                    intent.putExtra("201PM", value);
                    break;

                case "652":
                    nutrientName = "3:00:00 PM";
                    intent.putExtra("3PM", value);
                    break;

                case "697":
                    nutrientName = "3:01:00 PM";
                    intent.putExtra("301PM", value);
                    break;

                case "613":
                    nutrientName = "4:00:00 PM";
                    intent.putExtra("4PM", value);
                    break;

                case "626":
                    nutrientName = "16:1 undifferentiated";
                    intent.putExtra("16:1undifferentiated", value);
                    break;

                case "673":
                    nutrientName = "16:1 c (Palmitoleic acid)" ;
                    intent.putExtra("16:1c", value);
                    break;

                case "662":
                    nutrientName = "16:1 t";
                    intent.putExtra("16:1t", value);
                    break;

                case "653":
                    nutrientName = "5:00:00 PM\n";
                    intent.putExtra("5PM", value);
                    break;

                case "687":
                    nutrientName = "5:01:00 PM\n";
                    intent.putExtra("501PM", value);
                    break;

                case "614":
                    nutrientName = "6:00:00 PM\n";
                    intent.putExtra("6PM", value);
                    break;

                case "617":
                    nutrientName = "18:1 undifferentiated";
                    intent.putExtra("18:1undifferentiated", value);
                    break;

                case "674":
                    nutrientName = "18:1 c (Vaccenic acid)";
                    intent.putExtra("18:1c", value);
                    break;

                case "663":
                    nutrientName = "18:1 t";
                    intent.putExtra("18:1t", value);
                    break;

                case "859":
                    nutrientName = "18:1-11t (18:1t n-7)";
                    intent.putExtra("18:1-11t", value);
                    break;

                case "618":
                    nutrientName = "18:2 undifferentiated";
                    intent.putExtra("18:2undifferentiated", value);
                    break;

                case "670":
                    nutrientName = "18:2 CLAs";
                    intent.putExtra("18:2CLAs", value);
                    break;

                case "675":
                    nutrientName = "18:2 n-6 c,c (Linoleic acid)";
                    intent.putExtra("18:2n-6", value);
                    break;

                case "669":
                    nutrientName = "18:2 t,t";
                    intent.putExtra("18:2t", value);
                    break;

                case "619":
                    nutrientName = "18:3 undifferentiated";
                    intent.putExtra("18:3undifferentiated", value);
                    break;

                case "851":
                    nutrientName = "18:3 n-3 c,c,c (ALA)";
                    intent.putExtra("18:3n-3", value);
                    break;

                case "685":
                    nutrientName = "18:3 n-6 c,c,c (α-Calendic acid)";
                    intent.putExtra("18:3n-6", value);
                    break;

                case "627":
                    nutrientName = "6:04:00 PM";
                    intent.putExtra("604PM", value);
                    break;

                case "615":
                    nutrientName = "8:00:00 PM";
                    intent.putExtra("8PM", value);
                    break;

                case "628":
                    nutrientName = "8:01:00 PM";
                    intent.putExtra("801PM", value);
                    break;

                case "672":
                    nutrientName = "20:2 n-6 c,c (Eicosadienoic acid)";
                    intent.putExtra("20:2n-6", value);
                    break;

                case "689":
                    nutrientName = "20:3 undifferentiated";
                    intent.putExtra("20:3undifferentiated", value);
                    break;

                case "852":
                    nutrientName = "20:3 n-3 (Eicosatrienoic acid (ETE)";
                    intent.putExtra("20:3n-3", value);
                    break;

                case "853":
                    nutrientName = "20:3 n-6 (Dihomo-gamma-linolenic acid (DGLA)";
                    intent.putExtra("20:3n-6", value);
                    break;

                case "620":
                    nutrientName = "20:4 undifferentiated";
                    intent.putExtra("20:4undifferentiate", value);
                    break;

                case "855":
                    nutrientName = "20:4 n-6 (Adrenic acid (AdA))";
                    intent.putExtra("20:4n-6", value);
                    break;

                case "629":
                    nutrientName = "20:5 n-3 (EPA)";
                    intent.putExtra("20:5n-3", value);
                    break;

                case "857":
                    nutrientName = "9:05:00 PM";
                    intent.putExtra("905PM", value);
                    break;

                case "624":
                    nutrientName = "10:00:00 PM";
                    intent.putExtra("10PM", value);
                    break;

                case "630":
                    nutrientName = "22:1 undifferentiated";
                    intent.putExtra("22:1undifferentiated", value);
                    break;

                case "858":
                    nutrientName = "10:04:00 PM\n";
                    intent.putExtra("1004PM", value);
                    Log.d("TESTI: ", nutrientName + "hello " + value);
                    break;

                case "631":
                    nutrientName = "22:5 n-3 (DPA)";
                    intent.putExtra("22:5n-3", value);
                    break;

                case "621":
                    nutrientName = "22:6 n-3 (DHA)";
                    intent.putExtra("22:6n-3", value);
                    break;

                case "654":
                    nutrientName = "24:00:00.000";
                    intent.putExtra("2400", value);
                    break;

                case "671":
                    nutrientName = "24:1 c (Nervonic acid)";
                    intent.putExtra("24:1c", value);
                    break;

                case "607":
                    nutrientName = "4:00:00 AM";
                    intent.putExtra("4AM", value);
                    break;

                case "608":
                    nutrientName = "6:00:00 AM";
                    intent.putExtra("6AM", value);
                    break;

                case "609":
                    nutrientName = "8:00:00 AM";
                    intent.putExtra("8AM", value);
                    break;

                case "645":
                    nutrientName = "Total monounsaturated";
                    intent.putExtra("TotalMnounsaturated", value);
                    break;

                case "646":
                    nutrientName = "Total polyunsaturated";
                    intent.putExtra("TotalPolyunsaturated", value);
                    break;

                case "693":
                    nutrientName = "Total trans-monoenoic";
                    intent.putExtra("TotalTransMonoenoic", value);
                    break;

                case "695":
                    nutrientName = "Total trans-polyenoic";
                    intent.putExtra("TotalTransPolyenoic", value);
                    break;

                case "313":
                    nutrientName = "Fluoride";
                    intent.putExtra("Fluoride", value);
                    break;

                case "417":
                    nutrientName = "Folate";
                    intent.putExtra("Folate", value);
                    break;

                case "431":
                    nutrientName = "Folic acid";
                    intent.putExtra("FolicAcid", value);
                    break;

                case "435":
                    nutrientName = "Folate, DFE";
                    intent.putExtra("FolateDFE", value);
                    break;

                case "432":
                    nutrientName = "Folate, food";
                    intent.putExtra("FolateFood", value);
                    break;

                case "212":
                    nutrientName = "Fructose";
                    intent.putExtra("Fructose", value);
                    break;

                case "287":
                    nutrientName = "Galactose";
                    intent.putExtra("Galactose", value);
                    break;

                case "515":
                    nutrientName = "Glutamic acid";
                    intent.putExtra("GlutamicAcid", value);
                    break;

                case "211":
                    nutrientName = "Glucose (dextrose)";
                    intent.putExtra("Glucose", value);
                    break;

                case "516":
                    nutrientName = "Glycine";
                    intent.putExtra("Glycine", value);
                    break;

                case "512":
                    nutrientName = "Histidine";
                    intent.putExtra("Histidine", value);
                    break;

                case "521":
                    nutrientName = "Hydroxyproline";
                    intent.putExtra("Hydroxyproline", value);
                    break;

                case "503":
                    nutrientName = "Isoleucine";
                    intent.putExtra("Isoleucine", value);
                    break;

                case "213":
                    nutrientName = "Lactose";
                    intent.putExtra("Lactose", value);
                    break;

                case "504":
                    nutrientName = "Leucine";
                    intent.putExtra("Leucine", value);
                    break;

                case "338":
                    nutrientName = "Lutein + zeaxanthin";
                    intent.putExtra("LuteinzZaxanthin", value);
                    break;

                case "337":
                    nutrientName = "Lycopene";
                    intent.putExtra("Lycopene", value);
                    break;

                case "505":
                    nutrientName = "Lysine";
                    intent.putExtra("Lysine", value);
                    break;

                case "214":
                    nutrientName = "Maltose";
                    intent.putExtra("Maltose", value);
                    break;

                case "506":
                    nutrientName = "Methionine";
                    intent.putExtra("Methionine", value);
                    break;

                case "304":
                    nutrientName = "Magnesium, Mg";
                    intent.putExtra("Magnesium", value);
                    break;

                case "428":
                    nutrientName = "Menaquinone-4";
                    intent.putExtra("Menaquinone4", value);
                    break;

                case "315":
                    nutrientName = "Manganese, Mn";
                    intent.putExtra("Manganese", value);
                    break;

                case "406":
                    nutrientName = "Niacin";
                    intent.putExtra("Niacin", value);
                    break;

                case "573":
                    nutrientName = "Vitamin E";
                    intent.putExtra("VitaminE", value);
                    break;

                case "578":
                    nutrientName = "Vitamin B-12";
                    intent.putExtra("VitaminB12", value);
                    break;

                case "257":
                    nutrientName = "Adjusted Protein";
                    intent.putExtra("AdjustedProtein", value);
                    break;

                case "664":
                    nutrientName = "22:1 t";
                    intent.putExtra("22:1t", value);
                    break;

                case "676":
                    nutrientName = "22:1 c";
                    intent.putExtra("22:1c", value);
                    break;

                case "856":
                    nutrientName = "18:3i";
                    intent.putExtra("18:3i", value);
                    break;

                case "665":
                    nutrientName = "18:2 t not further defined";
                    intent.putExtra("18:2t", value);
                    break;

                case "666":
                    nutrientName = "18:2 i";
                    intent.putExtra("18:2i", value);
                    break;

                case "305":
                    nutrientName = "Phosphorus, P";
                    intent.putExtra("Phosphorus", value);
                    break;

                case "410":
                    nutrientName = "Pantothenic acid";
                    intent.putExtra("PantothenicAcid", value);
                    break;

                case "508":
                    nutrientName = "Phenylalanine";
                    intent.putExtra("Phenylalanine", value);
                    break;

                case "636":
                    nutrientName = "Phytosterols";
                    intent.putExtra("Phytosterols", value);
                    break;

                case "517":
                    nutrientName = "Proline";
                    intent.putExtra("Proline", value);
                    break;

                case "319":
                    nutrientName = "Retinol";
                    intent.putExtra("Retinol", value);
                    break;

                case "405":
                    nutrientName = "Riboflavin";
                    intent.putExtra("Riboflavin", value);
                    break;

                case "317":
                    nutrientName = "Selenium, Se";
                    intent.putExtra("Selenium", value);
                    break;

                case "518":
                    nutrientName = "Serine";
                    intent.putExtra("Serine", value);
                    break;

                case "641":
                    nutrientName = "Beta-sitosterol";
                    intent.putExtra("BetaSitosterol", value);
                    break;

                case "209":
                    nutrientName = "Starch";
                    intent.putExtra("Starch", value);
                    break;

                case "638":
                    nutrientName = "Stigmasterol";
                    intent.putExtra("Stigmasterol", value);
                    break;

                case "210":
                    nutrientName = "Sucrose";
                    intent.putExtra("Sucrose", value);
                    break;

                case "263":
                    nutrientName = "Theobromine";
                    intent.putExtra("Theobromine", value);
                    break;

                case "404":
                    nutrientName = "Thiamin";
                    intent.putExtra("Thiamin", value);
                    break;

                case "502":
                    nutrientName = "Threonine";
                    intent.putExtra("Threonine", value);
                    break;

                case "323":
                    nutrientName = "Vitamin E (alpha-tocopherol)\n";
                    intent.putExtra("VitaminE", value);
                    break;

                case "341":
                    nutrientName = "Tocopherol, beta";
                    intent.putExtra("TocopherolBeta", value);
                    break;

                case "343":
                    nutrientName = "Tocopherol, delta";
                    intent.putExtra("TocopherolDelta", value);
                    break;

                case "342":
                    nutrientName = "Tocopherol, gamma";
                    intent.putExtra("TocopherolGamma", value);
                    break;

                case "501":
                    nutrientName = "Tryptophan";
                    intent.putExtra("Tryptophan", value);
                    break;

                case "509":
                    nutrientName = "Tyrosine";
                    intent.putExtra("Tyrosine", value);
                    break;

                case "510":
                    nutrientName = "Valine";
                    intent.putExtra("Valine", value);
                    break;

                case "318":
                    nutrientName = "Vitamin A, IU";
                    intent.putExtra("VitaminAIu", value);
                    break;

                case "320":
                    nutrientName = "Vitamin A, RAE";
                    intent.putExtra("VitaminARae", value);
                    break;

                case "418":
                    nutrientName = "Vitamin B-12";
                    intent.putExtra("VitaminB12-2", value);
                    break;

                case "415":
                    nutrientName = "Vitamin B-6";
                    intent.putExtra("VitaminB6", value);
                    break;

                case "401":
                    nutrientName = "Vitamin C, total ascorbic acid";
                    intent.putExtra("VitaminC", value);
                    break;

                case "328":
                    nutrientName = "Vitamin D (D2 + D3)";
                    intent.putExtra("VitaminD", value);
                    break;

                case "430":
                    nutrientName = "Vitamin K (phylloquinone)";
                    intent.putExtra("VitaminK", value);
                    break;

                case "429":
                    nutrientName = "Dihydrophylloquinone";
                    intent.putExtra("Dihydrophylloquinone", value);
                    break;

                case "255":
                    nutrientName = "Water";
                    intent.putExtra("Water", value);
                    break;

                case "309":
                    nutrientName = "Zinc, Zn";
                    intent.putExtra("Zinc", value);
                    break;


            }
        }

        if(mListItem.get(position).getAlt_measures() != null) {
            for (int i = 0; i < mListItem.get(position).getAlt_measures().size(); i++) {
                if(!(mListItem.get(pos).getFood_name().equals(mListItem.get(position).getAlt_measures().get(i).getMeasure()))) {
                    measure.add(String.valueOf(mListItem.get(position).getAlt_measures().get(i).getMeasure()));
                }


                Log.d(TAG, "Alt_measures getQty" + String.valueOf(mListItem.get(position).getAlt_measures().get(i).getQty()));
                Log.d(TAG, "Alt_measures getMeasure " + String.valueOf(mListItem.get(position).getAlt_measures().get(i).getMeasure()));
                Log.d(TAG, "Alt_measures getSeq " + String.valueOf(mListItem.get(position).getAlt_measures().get(i).getSeq()));
                Log.d(TAG, "Alt_measures getServing_weight " + String.valueOf(mListItem.get(position).getAlt_measures().get(i).getServing_weight()));
            }
        }







        intent.putExtra("food_name", mListItem.get(position).getFood_name());
        intent.putExtra("nf_calories", mListItem.get(position).getNf_calories());
        intent.putExtra("serving_qty", mListItem.get(position).getServing_qty());
        intent.putExtra("serving_unit", mListItem.get(position).getServing_unit());
        intent.putExtra("nf_total_fat", mListItem.get(position).getNf_total_fat());
        intent.putExtra("serving_weight_grams", mListItem.get(position).getServing_weight_grams());
        intent.putExtra("nf_cholesterol", mListItem.get(position).getNf_cholesterol());
        intent.putExtra("nf_saturated_fat", mListItem.get(position).getNf_saturated_fat());
        intent.putExtra("nf_sodium", mListItem.get(position).getNf_sodium());
        intent.putExtra("nf_total_carbohydrate", mListItem.get(position).getNf_total_carbohydrate());
        intent.putExtra("nf_dietary_fiber", mListItem.get(position).getNf_dietary_fiber());
        intent.putExtra("nf_sugars", mListItem.get(position).getNf_sugars());
        intent.putExtra("nf_protein", mListItem.get(position).getNf_protein());
        intent.putExtra("nf_potassium", mListItem.get(position).getNf_potassium());
        intent.putExtra("nf_p", mListItem.get(position).getNf_p());
        intent.putExtra("thumb", mListItem.get(position).getPhoto().getThumb());
        intent.putExtra("photoHighres", mListItem.get(position).getPhoto().getHighres());

        mContext.startActivity(intent);
    }

    @Override
    public long getItemId(int position) {
        //Auto-generated method stub
        return position;
    }


}
