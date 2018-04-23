package com.h4413.recyclyon.Model;

import java.io.Serializable;

public class User implements Serializable {
    public String id;
    public String nom;

    public String associationId;

    public User(String id, String nom, String assocId) {
        this.id = id;
        this.nom = nom;
        this.associationId = assocId;
    }
}
