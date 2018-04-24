package com.h4413.recyclyon.Model;

import java.io.Serializable;

public class Association implements Serializable {

    public int id;
    public String nom;
    public String description;
    public String url;

    public Association(int id, String nom, String description, String url) {
        this.url = url;
        this.id = id;
        this.description = description;
        this.nom = nom;
    }
}
