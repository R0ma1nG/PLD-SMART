package com.h4413.recyclyon.Model;

import java.io.Serializable;
import java.util.Date;

public class HistoricEntry implements Serializable {
    public Date date;
    public float montant;
    public String idAssoc;

    public HistoricEntry(Date date, float montant, String idAssoc) {
        this.date = date;
        this.montant = montant;
        this.idAssoc = idAssoc;
    }
}
