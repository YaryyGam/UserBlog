package com.jmc.medicalapplication.Models;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Worker {
    private final StringProperty lName;
    private final StringProperty fName;
    private final StringProperty sName;
    private final DoubleProperty timeOfBegin;
    private final StringProperty currentPosition;
    private final ObjectProperty<LocalDate> date;

    public Worker(String lName, String fName, String sName, double timeOfBegin, String position, LocalDate date){
        this.lName = new SimpleStringProperty(this, "Last Name", lName);
        this.fName = new SimpleStringProperty(this, "First Name", fName);
        this.sName = new SimpleStringProperty(this, "Second Name", sName);
        this.timeOfBegin = new SimpleDoubleProperty(this, "Time Of Begin", timeOfBegin);
        this.currentPosition = new SimpleStringProperty(this, "Position", position);
        this.date = new SimpleObjectProperty<>(this, "Date", date);
    }

    public StringProperty lNameProperty() {return this.lName;}
    public StringProperty fNameProperty() {return this.fName;}
    public StringProperty sNameProperty() {return this.sName;}
    public DoubleProperty timeOfBeginProperty() {return this.timeOfBegin;}
    public StringProperty currentPositionProperty() {return this.currentPosition;}
    public ObjectProperty<LocalDate> dateProperty() {return this.date;}
}
