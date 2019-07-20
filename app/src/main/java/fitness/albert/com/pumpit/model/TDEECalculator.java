package fitness.albert.com.pumpit.model;

import java.util.HashMap;
import java.util.Map;



//BMR
// MEN BMR = 66 + (Weight) * 13.8 + (Height) * 5 - (age) * 6.8
// WOMAN BMR = 655 + (Weight) * 9.6 + (Height) * 1.8 - (age) * 4.7



//BEE
// Kcal day born calories
// set of the day = BMR * 1.2
// Active lightweight = BMR * 1.375
// Active medium = BMR * 1.55
// Very active = BMR * 1.725
// An unusual activist = BMR * 1.9


//Thermic Effect of Food-TEF
//BEE * 1.1


//Personal evaluation
// What the maximum / minimum weight we want to reach ?
// What was the lowest weight we had without a regular diet ?
// How we got to the weight we set for ourselves ?
// At what body weight and percentage of fat we perform most effectively ?


// חשב במשך 3 ימים,בתקופת אימונים שבה המשקל "נתקע", את כמויות הקלוריות שצרכנו ולעשות ממוצע.המספר שהגענו אליו הוא מספר הקלוריות שאיתו יש להתחיל את הדיאטה.
// לדוגמה: מתאמן אכל ביום א:3500 קלוריות יום ב:3000 קלוריות ויום ג:2500 קלוריות ולכן כמות הקלוריות היומית שממנה יש להתחיל את הדיאטה היא 3000 קלוריות.
// השיטה דורשת עבודת בית יסודית אך היא נותנת לנו הערכה שלעיתים יכולה להיות טובה יותר מאשר הנוסחאות כוון שהיא אישית לאותו אדם ולא כוללת ממוצעים כלליים לאוכ' כמו שאר הנוסחאות.

//NOTE:
// A drastic reduction in calories will cause decreased metabolic activity,
// decreased thyroid function, and decreased lean body mass. In the same way, a drastic
// increase in calorie intake will result in unnecessary increases in fat tissue.


//The American College of Sports Medicine does not recommend dropping below 1200 calories per woman
// and 1800 calories for men (per day)



//If our trainee from the previous example has a 3000 calorie TEE and wants to gain weight,
// then we will take a 20% increase and get him to consume 3600 calories to increase the
// maximum muscle and minimum fat increase.



public class TDEECalculator {

    private int age;
    private float bmi;
    private Double height;
    private Double weight;


    private boolean isMale;


    public TDEECalculator() {
    }

    public TDEECalculator(int age, Double height, Double weight, boolean isMale) {
        this.age = age;
        this.bmi = (float) (weight / (height*height));
        this.height = height;
        this.weight = weight;
        this.isMale = isMale;
    }

    public float setBmi(Double height, Double weight) {
        height = height/100;
        return this.bmi = (float) (weight / (height*height));
    }

    public float getBmi() {
        return bmi;
    }



    public void setBmi(float bmi) {
        this.bmi = bmi;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean male) {
        isMale = male;
    }


    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String bmiTable(Float bmi) {
        String bmiResult = null;
        if(this.bmi < 16) {
            return bmiResult = "Severe Thinness";
        }
        if(this.bmi > 16 && this.bmi <= 17) {
            return bmiResult = "Moderate Thinness";
        }
        if(this.bmi > 17 && this.bmi <= 18.5) {
            return bmiResult = "Mild Thinness";
        }
        if(this.bmi > 18.5 && this.bmi <= 25) {
            return bmiResult = "Normal";
        }
        if(this.bmi > 25 && this.bmi <= 30) {
            return bmiResult = "Overweight";
        }
        if(this.bmi > 30 && this.bmi <= 35) {
            return bmiResult = "Obese Class I";
        }
        if(this.bmi > 35 && this.bmi <= 40) {
            return bmiResult = "Obese Class II";
        }
        if(this.bmi > 40) {
            return bmiResult = "Obese Class III";
        }
        return bmiResult;
    }




    public int calculatorBMR() {
        return (int) (isMale() ?  (66 + (this.weight) * 13.8 + (this.height) * 5 - (this.age) * 6.8)
                :  (655 + (this.weight) * 9.6 + (this.height) * 1.8 - (this.age) * 4.7));
    }

    public Map<String, Integer> calculatorBEE() {

        Map<String, Integer> calcBEE = new HashMap<>();
        calcBEE.put("setOfTheDay", (int) (calculatorBMR() * 1.2));
        calcBEE.put("activeLightweight", (int) (calculatorBMR() * 1.375));
        calcBEE.put("activeMedium", (int) (calculatorBMR() * 1.55));
        calcBEE.put("anUnusualActivist", (int) (calculatorBMR() * 1.9));

        return calcBEE;
    }


    public int thermicEffect(String valueKey) {
        return (int) (calculatorBEE().get(valueKey) * 1.1);
    }


    public void dailyNutritionalValues() {
        float vitaminA, thiaminB1, riboflavinB2, niacinB3, pantothenicB5, vitaminB6, polatB9,
                vitaminB12, vitaminC, vitaminD, vitaminE, vitaminK, zinic, iron, magnesium, calcium,
                selenium, ala, cla, sodium, potassium, nutritionalFiber, cholesterol;


        if(this.isMale) {
            if(this.age>= 19 && this.age <=50) {
                 vitaminA = 900;
                 thiaminB1 = 1.2f;
                 riboflavinB2 = 1.3f;
                 niacinB3 = 16;
                 pantothenicB5 = 5;
                 vitaminB6 = 1.3f;
                 polatB9 = 400;
                 vitaminB12 = 2.4f;
                 vitaminC = 90;
                 vitaminD = 5;
                 vitaminE = 15;
                 vitaminK = 120;
                 zinic = 11;
                 iron = 8;
                 magnesium = 420;
                 calcium = 1000;
                 selenium = 55;
                 ala = 1.6f;
                 cla = 17;
                 sodium = 1.5f;
                 potassium = 4.7f;
                 nutritionalFiber = 48;
                 cholesterol = 300;
            }
            if(this.age>=51 && this.age <=70) {
                vitaminA = 900;
                thiaminB1 = 1.2f ;
                riboflavinB2 = 1.3f;
                niacinB3 = 16;
                pantothenicB5 = 5;
                vitaminB6 = 1.7f;
                polatB9 = 400;
                vitaminB12 = 2.4f;
                vitaminC = 90;
                vitaminD = 10;
                vitaminE = 15;
                vitaminK = 120;
                zinic = 11;
                iron = 8;
                magnesium = 420;
                calcium = 1200;
                selenium = 55;
                ala = 1.6f;
                cla = 14;
                sodium = 1.3f;
                potassium = 4.7f;
                nutritionalFiber = 30;
                cholesterol = 300;
            }
            if(this.age>70) {
                vitaminA = 900;
                thiaminB1 = 1.2f ;
                riboflavinB2 = 1.3f;
                niacinB3 = 16;
                pantothenicB5 = 5;
                vitaminB6 = 1.7f;
                polatB9 = 400;
                vitaminB12 = 2.4f;
                vitaminC = 90;
                vitaminD = 15;
                vitaminE = 15;
                vitaminK = 120;
                zinic = 11;
                iron = 8;
                magnesium = 420;
                calcium = 1200;
                selenium = 55;
                ala = 1.6f;
                cla = 14;
                sodium = 1.2f;
                potassium = 4.7f;
                nutritionalFiber = 30;
                cholesterol = 300;
            }
        } else {
            if(this.age>=19 && this.age <=50) {
                vitaminA = 700;
                thiaminB1 = 1.1f ;
                riboflavinB2 = 1.1f;
                niacinB3 = 14;
                pantothenicB5 = 5;
                vitaminB6 = 1.3f;
                polatB9 = 400;
                vitaminB12 = 2.4f;
                vitaminC = 75;
                vitaminD = 5;
                vitaminE = 15;
                vitaminK = 90;
                zinic = 8;
                iron = 18;
                magnesium = 320;
                calcium = 1000;
                selenium = 55;
                ala = 1.1f;
                cla = 12;
                sodium = 1.5f;
                potassium = 4.7f;
                nutritionalFiber = 25;
                cholesterol = 300;
            }

            if(this.age >= 51 && this.age <=70) {
                vitaminA = 700;
                thiaminB1 = 1.1f ;
                riboflavinB2 = 1.1f;
                niacinB3 = 14;
                pantothenicB5 = 5;
                vitaminB6 = 1.5f;
                polatB9 = 400;
                vitaminB12 = 2.4f;
                vitaminC = 75;
                vitaminD = 10;
                vitaminE = 15;
                vitaminK = 90;
                zinic = 8;
                iron = 8;
                magnesium = 320;
                calcium = 1200;
                selenium = 55;
                ala = 1.1f;
                cla = 11;
                sodium = 1.3f;
                potassium = 4.7f;
                nutritionalFiber = 21;
                cholesterol = 300;
            }

            if (this.age > 70) {
                vitaminA = 700;
                thiaminB1 = 1.1f ;
                riboflavinB2 = 1.1f;
                niacinB3 = 14;
                pantothenicB5 = 5;
                vitaminB6 = 1.5f;
                polatB9 = 400;
                vitaminB12 = 2.4f;
                vitaminC = 75;
                vitaminD = 15;
                vitaminE = 15;
                vitaminK = 90;
                zinic = 8;
                iron = 8;
                magnesium = 320;
                calcium = 1200;
                selenium = 55;
                ala = 1.1f;
                cla = 11;
                sodium = 1.2f;
                potassium = 4.7f;
                nutritionalFiber = 21;
                cholesterol = 300;
            }
        }
    }



}
