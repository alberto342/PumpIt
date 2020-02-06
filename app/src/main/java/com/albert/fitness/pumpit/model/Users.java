package com.albert.fitness.pumpit.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class Users extends BaseObservable {

    private String fullName;
    private String email;
    private String dateOfBirth;
    private boolean isMale;
    private int height;
    private float weight;
    private String bodyFat;
    private String fatTarget;
    private String myProgram;

    public Users() {
    }

    public Users(String fullName, String email, String dateOfBirth, boolean isMale, int height, float weight, String bodyFat, String fatTarget, String myProgram) {
        this.fullName = fullName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.isMale = isMale;
        this.height = height;
        this.weight = weight;
        this.bodyFat = bodyFat;
        this.fatTarget = fatTarget;
        this.myProgram = myProgram;
    }



    @Bindable
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
        notifyPropertyChanged(fitness.albert.com.pumpit.BR.fullName);
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(fitness.albert.com.pumpit.BR.email);
    }

    @Bindable
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        notifyPropertyChanged(fitness.albert.com.pumpit.BR.dateOfBirth);
    }

    @Bindable
    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean male) {
        isMale = male;
        notifyPropertyChanged(fitness.albert.com.pumpit.BR.male);
    }

    @Bindable
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
        notifyPropertyChanged(fitness.albert.com.pumpit.BR.height);
    }

    @Bindable
    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
        notifyPropertyChanged(fitness.albert.com.pumpit.BR.weight);
    }

    @Bindable
    public String getBodyFat() {
        return bodyFat;
    }

    public void setBodyFat(String bodyFat) {
        this.bodyFat = bodyFat;
        notifyPropertyChanged(fitness.albert.com.pumpit.BR.bodyFat);
    }

    @Bindable
    public String getFatTarget() {
        return fatTarget;
    }

    public void setFatTarget(String fatTarget) {
        this.fatTarget = fatTarget;
        notifyPropertyChanged(fitness.albert.com.pumpit.BR.fatTarget);
    }

    @Bindable
    public String getMyProgram() {
        return myProgram;
    }

    public void setMyProgram(String myProgram) {
        this.myProgram = myProgram;
        notifyPropertyChanged(fitness.albert.com.pumpit.BR.myProgram);
    }


}
