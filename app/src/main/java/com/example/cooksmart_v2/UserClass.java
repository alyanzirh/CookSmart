package com.example.cooksmart_v2;

public class UserClass {

    private String dietType;
    private String key;
    private String targetCals;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTargetCals() {
        return targetCals;
    }

    public void setTargetCals(String targetCals) {
        this.targetCals = targetCals;
    }

    public String getDietType() {
        return dietType;
    }

    public UserClass(String dietType) {
        this.dietType = dietType;
    }
    public UserClass(){

    }
}
