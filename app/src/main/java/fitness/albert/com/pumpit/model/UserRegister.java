package fitness.albert.com.pumpit.model;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class UserRegister {

    //    public static final String NAME_KEY = "name";
//    public static final String EMAIL_KEY ="email";
    // public static final String SETTINGS_PREFERENCES_FILE = "user_info";
   // public static final String SETTINGS_PREFERENCES_FILE = "fitness.albert.com.pumpit_preferences";

    public static final String FIRE_BASE_USERS = "users";

    private int age;
    private String imagesRefPath;  //profile img url
    private String fullName;
    private String firstName;
    private String lestName;
    private String email;
    private String dateOfBirth;
    //private int age;
    private boolean isMale;
    private int height;
    private float weight;
    private String currentBodyFat;
    private String targetBodyFat;
    private int meter;
    private int cm;
    private String myProgram;

    private String bodyFat;
    private String fatTarget;
    private String activityLevel;
    private final Map<String, Object> dataToSave = new HashMap<>();


    public enum TrainingType {
        BEGINNER,
        INTERMEDIATE,
        ADVANCED
    }

    public UserRegister() {
    }

    public UserRegister(String fullName, String email, String dateOfBirth, boolean isMale, int height, float weight, String currentBodyFat, String targetBodyFat, String myProgram) {
        this.fullName = fullName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.isMale = isMale;
        this.height = height;
        this.weight = weight;
        this.currentBodyFat = currentBodyFat;
        this.targetBodyFat = targetBodyFat;
        this.myProgram = myProgram;
    }

    public UserRegister(String firstName, String lestName) {
        this.firstName = firstName;
        this.lestName = lestName;
    }


    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getMeter() {
        return meter;
    }

    public void setMeter(int meter) {
        this.meter = meter;
    }

    public int getCm() {
        return cm;
    }

    public void setCm(int cm) {
        this.cm = cm;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLestName() {
        return lestName;
    }

    public void setLestName(String lestName) {
        this.lestName = lestName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getCurrentBodyFat() {
        return currentBodyFat;
    }

    public void setCurrentBodyFat(String currentBodyFat) {
        this.currentBodyFat = currentBodyFat;
    }

    public String getTargetBodyFat() {
        return targetBodyFat;
    }

    public void setTargetBodyFat(String targetBodyFat) {
        this.targetBodyFat = targetBodyFat;
    }

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean male) {
        isMale = male;
    }

    public String getMyProgram() {
        return myProgram;
    }

    public void setMyProgram(String myProgram) {
        this.myProgram = myProgram;
    }

    public String getBodyFat() {
        return bodyFat;
    }

    public void setBodyFat(String bodyFat) {
        this.bodyFat = bodyFat;
    }

    public String getFatTarget() {
        return fatTarget;
    }

    public void setFatTarget(String fatTarget) {
        this.fatTarget = fatTarget;
    }

    public String getImagesRefPath() {
        return imagesRefPath;
    }

    public void setImagesRefPath(String imagesRefPath) {
        this.imagesRefPath = imagesRefPath;
    }

    public String getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(String activityLevel) {
        this.activityLevel = activityLevel;
    }

    public Map<String, Object> getDataToSave() {
        return dataToSave;
    }


    public boolean splitName(String fullName) {
        this.firstName = fullName.split(" ")[0];
        try {
            this.lestName = fullName.split(" ")[1];
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public float customFloatNum(int num1, int num2) {
        return Float.parseFloat(num1 + "." + num2);
        // return floatNum;
    }


    public int date(String date) {
        String[] splitDate = date.split("/");
        int month = Integer.valueOf(splitDate[0]);
        int day = Integer.valueOf(splitDate[1]);
        int year = Integer.valueOf(splitDate[2]);
        return getAge(year, month, day);
    }

    public int getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        @SuppressLint("UseValueOf")
        Integer ageInt = new Integer(age);
        return ageInt;
    }


    public int calculatorBMR() {
        return (int) (isMale() ? (66 + (this.weight) * 13.8 + (this.height) * 5 - (this.age) * 6.8)
                : (655 + (this.weight) * 9.6 + (this.height) * 1.8 - (this.age) * 4.7));
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

    public static String getTodayData() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
        return df.format(c);
    }


    @Override
    public String toString() {
        return "UserRegister{" +
                ", dateOfBirth=" + dateOfBirth +
                ", height=" + height +
                +'}';
    }


//    public String getEmailRegister() {
//        String email = null;
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        if (mAuth.getCurrentUser() != null) {
//            email = mAuth.getCurrentUser().getEmail();
//        }
//        return email;
//    }


}
