package com.raichess.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import com.raichess.model.Color;
import com.raichess.model.Country;

public class PersonRequest {

    @NotBlank(message = "Имя не может быть пустым")
    private String name;

    @NotNull(message = "Нужно выбрать координаты")
    private Integer coordinatesId;

    private Color eyeColor;

    @NotNull(message = "Цвет волос обязателен")
    private Color hairColor;

    @NotNull(message = "Нужно выбрать локацию")
    private Integer locationId;

    @NotNull(message = "Рост обязателен")
    @Positive(message = "Рост должен быть больше 0")
    private Float height;

    private Country nationality;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCoordinatesId() {
        return coordinatesId;
    }

    public void setCoordinatesId(Integer coordinatesId) {
        this.coordinatesId = coordinatesId;
    }

    public Color getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(Color eyeColor) {
        this.eyeColor = eyeColor;
    }

    public Color getHairColor() {
        return hairColor;
    }

    public void setHairColor(Color hairColor) {
        this.hairColor = hairColor;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    public Country getNationality() {
        return nationality;
    }

    public void setNationality(Country nationality) {
        this.nationality = nationality;
    }
}
