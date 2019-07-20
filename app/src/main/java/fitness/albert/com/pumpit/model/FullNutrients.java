package fitness.albert.com.pumpit.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class FullNutrients {

    @SerializedName("attr_id")
    private int attrId;
    private float value;

    public FullNutrients() {
    }

    public FullNutrients(int attr_id, float value) {
        this.attrId = attr_id;
        this.value = value;
    }

    public int getAttrId() {
        return attrId;
    }

    public void setAttrId(int attrId) {
        this.attrId = attrId;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getNutrients(int atterId) {

        String nutrients = "";

        switch (atterId) {
            case 208:
                nutrients += "Energy: " + this.value + "\n";
                break;
            case 203:
                nutrients += "Protein: " + this.value + "\n";
                break;
            case 204:
                nutrients += "Fat: " + this.value + "\n";
                break;
            case 205:
                nutrients += "Carbs: " + this.value + "\n";
                break;
            case 301:
                nutrients += "Calcium: " + this.value + "\n";
                break;
            case 601:
                nutrients += "Cholesterol: " + this.value + "\n";
                break;
            case 606:
                nutrients += "Total Saturated: " + this.value + "\n";
                break;
            case 605:
                nutrients += "Total Trans: " + this.value + "\n";
                break;

            case 303:
                nutrients += "Iron: " + this.value + "\n";
                break;

            case 291:
                nutrients += "Total Dietary: " + this.value + "\n";
                break;

            case 306:
                nutrients += "Potassium: " + this.value + "\n";
                break;

            case 307:
                nutrients += "Sodium: " + this.value + "\n";
                break;
            case 269:
                nutrients += "Sugars: " + this.value + "\n";
                break;

            case 539:
                nutrients += "Sugars Added: " + this.value + "\n";
                break;

            case 318:
                nutrients += "Vitamin A, IU: " + this.value + "\n";
                break;

            case 320:
                nutrients += "Vitamin A, RAE: " + this.value + "\n";
                break;
            case 578:
                nutrients += "Vitamin B12: " + this.value + "\n";
                break;
            case 418:
                nutrients += "Vitamin B12 2: " + this.value + "\n";
                break;

            case 415:
                nutrients += "Vitamin B6: " + this.value + "\n";
                break;
            case 401:
                nutrients += "Vitamin C, total ascorbic acid: " + this.value + "\n";
                break;
            case 324:
                nutrients += "Vitamin D: " + this.value + "\n";
                break;
            case 328:
                nutrients += "Vitamin D (D2 + D3): " + this.value + "\n";
                break;
            case 325:
                nutrients += "vitamin D2 (Ergocalciferol): " + this.value + "\n";
                break;
            case 326:
                nutrients += "Vitamin D3 (Cholecalciferol): " + this.value + "\n";
                break;
            case 573:
                nutrients += "Vitamin E: " + this.value + "\n";
                break;
            case 323:
                nutrients += "Vitamin E (alpha-tocopherol): " + this.value + "\n";
                break;
            case 430:
                nutrients += "Vitamin K (phylloquinone): " + this.value + "\n";
                break;
            case 309:
                nutrients += "Zinc: " + this.value + "\n";
                break;
            case 513:
                nutrients += "Alanine: " + this.value + "\n";
                break;
            case 221:
                nutrients += "Alcohol: " + this.value + "\n";
                break;

            case 511:
                nutrients += "Arginine: " + this.value + "\n";
                break;

            case 207:
                nutrients += "Ash: " + this.value + "\n";
                break;

            case 514:
                nutrients += "Aspartic acid: " + this.value + "\n";
                break;

            case 454:
                nutrients += "Betaine: " + this.value + "\n";
                break;

            case 262:
                nutrients += "Caffeine: " + this.value + "\n";
                break;
            case 639:
                nutrients += "Campesterol: " + this.value + "\n";
                break;
            case 322:
                nutrients += "Carotene: " + this.value + "\n";
                break;
            case 321:
                nutrients += "Carotene: " + this.value + "\n";
                break;
            case 421:
                nutrients += "Choline: " + this.value + "\n";
                break;
            case 334:
                nutrients += "Cryptoxanthin, beta: " + this.value + "\n";
                break;
            case 312:
                nutrients += "Copper: " + this.value + "\n";
                break;
            case 507:
                nutrients += "Cystine: " + this.value + "\n";
                break;
            case 268:
                break;
            case 626:
                nutrients += "16:1 undifferentiated: " + this.value + "\n";
                break;

            case 673:
                nutrients += "16:1 c (Palmitoleic Acid): " + this.value + "\n";
                break;

            case 662:
                nutrients += "16:1 t: " + this.value + "\n";
                break;

            case 617:
                nutrients += "18:1 undifferentiated: " + this.value + "\n";
                break;

            case 674:
                nutrients += "18:1 c (Vaccenic acid): " + this.value + "\n";
                break;

            case 663:
                nutrients += "18:1 t: " + this.value + "\n";
                break;

            case 859:
                nutrients += "18:1-11t (18:1t n-7): " + this.value + "\n";
                break;

            case 618:
                nutrients += "18:2 undifferentiated: " + this.value + "\n";
                break;

            case 670:
                nutrients += "18:2 CLAs: " + this.value + "\n";
                break;

            case 675:
                nutrients += "18:2 n-6 c,c (Linoleic acid): " + this.value + "\n";
                break;

            case 669:
                nutrients += "18:2 t,t: " + this.value + "\n";
                break;

            case 619:
                nutrients += "18:3 undifferentiated: " + this.value + "\n";
                break;

            case 851:
                nutrients += "18:3 n-3 c,c,c (ALA): " + this.value + "\n";
                break;

            case 685:
                nutrients += "18:3 n-6 c,c,c (α-Calendic acid): " + this.value + "\n";
                break;

            case 672:
                nutrients += "20:2 n-6 c,c (Eicosadienoic acid): " + this.value + "\n";
                break;

            case 689:
                nutrients += "20:3 undifferentiated: " + this.value + "\n";
                break;

            case 852:
                nutrients += "20:3 n-3 (Eicosatrienoic acid (ETE): " + this.value + "\n";
                break;

            case 853:
                nutrients += "Dihomo-gamma-linolenic acid (DGLA): " + this.value + "\n";
                break;

            case 620:
                nutrients += "20:4 undifferentiated: " + this.value + "\n";
                break;

            case 855:
                nutrients += "20:4 n-6 (Adrenic acid (AdA)): " + this.value + "\n";
                break;

            case 629:
                nutrients += "20:5 n-3 (EPA: " + this.value + "\n";
                break;

            case 630:
                nutrients += "22:1 undifferentiated: " + this.value + "\n";
                break;


            case 631:
                nutrients += "22:5 n-3 (DPA): " + this.value + "\n";
                break;

            case 621:
                nutrients += "22:6 n-3 (DHA): " + this.value + "\n";
                break;

            case 671:
                nutrients += "24:1 c (Nervonic acid): " + this.value + "\n";
                break;

            case 645:
                nutrients += "Total Monounsaturated: " + this.value + "\n";
                break;

            case 646:
                nutrients += "Total Polyunsaturated: " + this.value + "\n";
                break;

            case 693:
                nutrients += "Total Trans Monoenoic: " + this.value + "\n";
                break;

            case 695:
                nutrients += "Total Trans Polyenoic: " + this.value + "\n";
                break;

            case 313:
                nutrients += "Fluoride: " + this.value + "\n";
                break;

            case 417:
                nutrients += "Folate: " + this.value + "\n";
                break;

            case 431:
                nutrients += "Folic Acid: " + this.value + "\n";
                break;

            case 435:
                nutrients += "Folate Dfe: " + this.value + "\n";
                break;

            case 432:
                nutrients += "Folate Food: " + this.value + "\n";
                break;

            case 212:
                nutrients += "Fructose: " + this.value + "\n";
                break;

            case 287:
                nutrients += "Galactose: " + this.value + "\n";
                break;

            case 515:
                nutrients += "Glutamic Acid: " + this.value + "\n";
                break;

            case 211:
                nutrients += "Glucose: " + this.value + "\n";
                break;

            case 516:
                nutrients += "Glycine: " + this.value + "\n";
                break;

            case 512:
                nutrients += "Histidine: " + this.value + "\n";
                break;

            case 521:
                nutrients += "Hydroxyproline: " + this.value + "\n";
                break;

            case 503:
                nutrients += "Isoleucine: " + this.value + "\n";
                break;

            case 213:
                nutrients += "Lactose: " + this.value + "\n";
                break;

            case 504:
                nutrients += "Leucine: " + this.value + "\n";
                break;

            case 338:
                nutrients += "Lutein + Zeaxanthin: " + this.value + "\n";
                break;

            case 337:
                nutrients += "Lycopene: " + this.value + "\n";
                break;

            case 505:
                nutrients += "Lysine: " + this.value + "\n";
                break;

            case 214:
                nutrients += "Maltose: " + this.value + "\n";
                break;

            case 506:
                nutrients += "Methionine: " + this.value + "\n";
                break;

            case 304:
                nutrients += "Magnesium: " + this.value + "\n";
                break;

            case 428:
                nutrients += "Menaquinone: " + this.value + "\n";
                break;

            case 315:
                nutrients += "Manganese: " + this.value + "\n";
                break;

            case 406:
                nutrients += "Niacin: " + this.value + "\n";
                break;
            case 257:
                nutrients += "Adjusted Protein: " + this.value + "\n";
                break;

            case 664:
                nutrients += "22:1 t: " + this.value + "\n";
                break;

            case 676:
                nutrients += "22:1 c: " + this.value + "\n";
                break;

            case 856:
                nutrients += "18:3i: " + this.value + "\n";
                break;

            case 665:
                nutrients += "18:2 t not further defined: " + this.value + "\n";
                break;

            case 666:
                nutrients += "18:2 i: " + this.value + "\n";
                break;

            case 305:
                nutrients += "Phosphorus, P: " + this.value + "\n";
                break;

            case 410:
                nutrients += "Pantothenic acid: " + this.value + "\n";
                break;

            case 508:
                nutrients += "Phenylalanine: " + this.value + "\n";
                break;

            case 636:
                nutrients += "Phytosterols: " + this.value + "\n";
                break;

            case 517:
                nutrients += "Proline: " + this.value + "\n";
                break;

            case 319:
                nutrients += "Retinol: " + this.value + "\n";
                break;

            case 405:
                nutrients += "Riboflavin: " + this.value + "\n";
                break;

            case 317:
                nutrients += "Selenium, Se: " + this.value + "\n";
                break;

            case 518:
                nutrients += "Serine: " + this.value + "\n";
                break;

            case 641:
                nutrients += "Beta Sitosterol: " + this.value + "\n";
                break;

            case 209:
                nutrients += "Starch: " + this.value + "\n";
                break;

            case 638:
                nutrients += "Stigmasterol: " + this.value + "\n";
                break;

            case 210:
                nutrients += "Sucrose: " + this.value + "\n";
                break;

            case 263:
                nutrients += "Theobromine: " + this.value + "\n";
                break;

            case 404:
                nutrients += "Thiamin: " + this.value + "\n";
                break;

            case 502:
                nutrients += "Threonine: " + this.value + "\n";
                break;



            case 341:
                nutrients += "Tocopherol, beta: " + this.value + "\n";
                break;

            case 343:
                nutrients += "Tocopherol, delta: " + this.value + "\n";
                break;

            case 342:
                nutrients += "Tocopherol, gamma: " + this.value + "\n";
                break;

            case 501:
                nutrients += "Tryptophan: " + this.value + "\n";
                break;

            case 509:
                nutrients += "Tyrosine: " + this.value + "\n";
                break;

            case 510:
                nutrients += "Valine: " + this.value + "\n";
                break;
            case 429:
                nutrients += "Dihydrophylloquinone: " + this.value + "\n";
                break;

            case 255:
                nutrients += "Water: " + this.value + "\n";
        }
        return nutrients;
    }

    @NonNull
    @Override
    public String toString() {
        return "ClassPojo [attrId = " + attrId + ", value = " + value + "]";
    }
}
