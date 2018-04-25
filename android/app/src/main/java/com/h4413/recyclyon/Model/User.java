package com.h4413.recyclyon.Model;

import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    public String idUtilisateur;
    public String nom;
    public String mail;
    public String motDePasse;
    public String adresse;
    public int sexe;
    public Date dateNaissance;

    public String idAssoc;

    public User(String id, String nom, String mail, String mdp, String adresse, int sexe, String assocId, Date date) {
        this.idUtilisateur = id;
        this.nom = nom;
        this.idAssoc = assocId;
        this.mail = mail;
        this.motDePasse = mdp;
        this.adresse = adresse;
        this.sexe = sexe;
        this.dateNaissance = date;
    }

    public User() {

    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this, User.class);
    }
}
