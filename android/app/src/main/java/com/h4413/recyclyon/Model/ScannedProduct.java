package com.h4413.recyclyon.Model;

import java.util.Arrays;
import java.util.List;

public class ScannedProduct {

    public final static List<String> GLASS_TAGS = Arrays.asList("verre", "bocal");
    public final static List<String> CARTON_TAGS = Arrays.asList("papier", "carton", "metal", "conserve", "bouteille-plastique", "bouchon-plastique", "pehd", "pp", "brique", "aluminium", "canette");
    public final static List<String> NORMAL_TAGS = Arrays.asList("céramique", "porcelaine", "polystyrène", "plastique", "bois", "sachet", "film", "barquette");

    public String nom;
    public List<String> emballage;
    public String image;

    public ScannedProduct(String nom, String image, List<String> emballage) {
        this.nom = nom;
        this.emballage = emballage;
        this.image = image;
    }

}
