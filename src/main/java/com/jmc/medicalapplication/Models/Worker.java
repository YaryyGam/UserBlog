package com.jmc.medicalapplication.Models;

import javafx.beans.property.*;

import java.time.LocalDate;
import java.time.LocalTime;

public class Worker {
    private final StringProperty lName;
    private final StringProperty fName;
    private final StringProperty sName;
    private final LocalTime timeOfBegin;
    private final StringProperty currentPosition;
    private final LocalDate date;

    public Worker(String lName, String fName, String sName, LocalTime timeOfBegin, String position, LocalDate date){
        this.lName = new SimpleStringProperty(this, "Last Name", lName);
        this.fName = new SimpleStringProperty(this, "First Name", fName);
        this.sName = new SimpleStringProperty(this, "Second Name", sName);
        this.timeOfBegin = timeOfBegin;
        this.currentPosition = new SimpleStringProperty(this, "Position", position);
        this.date = date;
    }

    public StringProperty lNameProperty() {return this.lName;}
    public StringProperty fNameProperty() {return this.fName;}
    public StringProperty sNameProperty() {return this.sName;}
    public LocalTime timeOfBeginProperty() {return this.timeOfBegin;}
    public StringProperty currentPositionProperty() {return this.currentPosition;}
    public LocalDate dateProperty() {return this.date;}
}
