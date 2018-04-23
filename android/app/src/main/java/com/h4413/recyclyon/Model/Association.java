package com.h4413.recyclyon.Model;

import java.io.Serializable;

public class Association implements Serializable {

    public String id;
    public String nom;
    public String description;
    public String url;

    public Association(String id, String nom, String description, String url) {
        this.url = url;
        this.id = id;
        this.description = description;
        this.nom = nom;
    }
}
