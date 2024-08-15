package com.jmc.medicalapplication.Models;

import com.jmc.medicalapplication.Views.ViewFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class Model {
    private static Model model;
    private final ViewFactory viewFactory;
    private final DatabaseDriver databaseDriver;
    private final Worker worker;
    private final ObservableList<Worker> allWorkers;

    private Model(){
        this.viewFactory = new ViewFactory();
        this.databaseDriver = new DatabaseDriver();
        this.worker = new Worker("", "", "", null, "", null);
        this.allWorkers = FXCollections.observableArrayList();
    }

    public static synchronized Model getInstance(){
        if(model == null){
            model = new Model();
        }
        return model;
    }

    public ViewFactory getViewFactory() {return viewFactory;}

    public DatabaseDriver getDatabaseDriver() {return databaseDriver;}

    private void prepareWorkers(ObservableList<Worker> workers){
        ResultSet resultSet = databaseDriver.getWorker(this.worker.lNameProperty().get());
        try {
            while (resultSet.next()){
                String lName = resultSet.getString("LastName");
                String fName = resultSet.getString("FirstName");
                String sName = resultSet.getString("SecondName");
                LocalTime timeOfBegin = resultSet.getTime("TimeOfBegin").toLocalTime();
                String position = resultSet.getString("CurrentPosition");
                LocalDate date = resultSet.getDate("Date").toLocalDate();

                workers.add(new Worker(lName, fName, sName, timeOfBegin, position, date));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void setLatestWorkers(){prepareWorkers(this.allWorkers);}

    public ObservableList<Worker> getAllWorkers(){return allWorkers;}

    public void updateWorkers(){
        allWorkers.clear();
        prepareWorkers(allWorkers);
    }


}
