package com.jmc.medicalapplication.Models;

import com.jmc.medicalapplication.Views.ViewFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

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
        ResultSet resultSet = databaseDriver.getAllWorkers();
        try {
            while (resultSet.next()){
                String lName = resultSet.getString("LastName");
                String fName = resultSet.getString("FirstName");
                String sName = resultSet.getString("SecondName");
                String timeOfBeginStr = resultSet.getString("TimeOfBegin");
                String position = resultSet.getString("CurrentPosition");
                String dateStr = resultSet.getString("Date");

                // Convert string to LocalTime
                Time timeOfBegin = Time.valueOf(timeOfBeginStr);

                // Convert string to LocalDate
                Date date = Date.valueOf(dateStr);

                workers.add(new Worker(lName, fName, sName, timeOfBegin, position, date));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DateTimeParseException e) {
            System.err.println("Error parsing date or time: " + e.getMessage());
        }
    }

    public void getWorkerLog(Date date){
        Model.getInstance().getDatabaseDriver().createDailyWorkRecords(date);
    }

    public void setLatestWorkers(){prepareWorkers(this.allWorkers);}

    public ObservableList<Worker> getAllWorkers(){return allWorkers;}

    public void updateWorkers(){
        allWorkers.clear();
        prepareWorkers(allWorkers);
    }


}
