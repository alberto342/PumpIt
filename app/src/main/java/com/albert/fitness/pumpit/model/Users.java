package com.albert.fitness.pumpit.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Users extends RealmObject {

    @PrimaryKey
    private int id;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean male) {
        isMale = male;
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

    public String getMyProgram() {
        return myProgram;
    }

    public void setMyProgram(String myProgram) {
        this.myProgram = myProgram;
    }


}
