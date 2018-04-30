package com.h4413.recyclyon.Model;

import java.util.List;

public class ScannedProduct {
    public String nom;
    public List<String> emballage;
    public String image;

    public ScannedProduct(String nom, String image, List<String> emballage) {
        this.nom = nom;
        this.emballage = emballage;
        this.image = image;
    }

}
