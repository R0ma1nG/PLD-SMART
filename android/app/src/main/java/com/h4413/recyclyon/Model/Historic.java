package com.h4413.recyclyon.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Historic implements Serializable{
    public List<HistoricEntry> depots;

    public Historic() {
        depots = new ArrayList<>();
    }
}

