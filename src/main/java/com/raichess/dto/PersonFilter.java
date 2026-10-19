package com.raichess.dto;

public class PersonFilter {
    private String name;
    private String eyeColor;
    private String hairColor;
    private String nationality;
    private String locationName;
    private Float heightMin;
    private Float heightMax;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(String eyeColor) {
        this.eyeColor = eyeColor;
    }

    public String getHairColor() {
        return hairColor;
    }

    public void setHairColor(String hairColor) {
        this.hairColor = hairColor;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Float getHeightMin() {
        return heightMin;
    }

    public void setHeightMin(Float heightMin) {
        this.heightMin = heightMin;
    }

    public Float getHeightMax() {
        return heightMax;
    }

    public void setHeightMax(Float heightMax) {
        this.heightMax = heightMax;
    }
}
