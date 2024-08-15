package com.jmc.medicalapplication.Controller.Admin;

import com.jmc.medicalapplication.Models.Model;
import com.jmc.medicalapplication.Models.Worker;
import javafx.beans.binding.StringBinding;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Time;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

public class WorkerCellController implements Initializable {
    public Label date_lbl;
    public Label fname_lbl;
    public Label sname_lbl;
    public Label lname_lbl;
    public Label time_lbl;
    public TextField hours_fld;

    private final Worker worker;
    public Button update_btn;
    public Label end_time_lbl;

    public WorkerCellController(Worker worker){this.worker = worker;}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        date_lbl.setText(worker.dateProperty().toString());
        bindFirstSecondName();
        getTimeOfEnd();
        lname_lbl.textProperty().bind(worker.lNameProperty());
        time_lbl.setText(worker.timeOfBeginProperty().toString());
        update_btn.setOnAction(e->{
            handleUpdateButton();
        });
    }

    public void getTimeOfEnd() {
        Time endTime = Model.getInstance().getDatabaseDriver().getTimeOfEnd(worker.lNameProperty().get(), worker.dateProperty().toLocalDate());
        if (endTime != null) {
            end_time_lbl.setText(endTime.toString());
        } else {
            end_time_lbl.setText(""); // або інше значення за замовчуванням
            System.out.println("Record not found for " + worker.lNameProperty().get() + " on " + worker.dateProperty());
        }
    }

    private void handleUpdateButton() {
        try {
            // Extract hours and minutes from the text field
            double hoursWorked = Double.parseDouble(hours_fld.getText());
            int hours = (int) hoursWorked;
            int minutes = (int) ((hoursWorked - hours) * 60);

            // Convert Strings to LocalTime and Date
            LocalTime startLocalTime = LocalTime.parse(time_lbl.getText());
            LocalTime endLocalTime = startLocalTime.plusHours(hours).plusMinutes(minutes);
            Time start = Time.valueOf(startLocalTime);
            Time end = Time.valueOf(endLocalTime);
            Date date = Date.valueOf(date_lbl.getText());

            dailyWorkUpdate(lname_lbl.getText(), start, end, date);

            // Save the end time
            saveWorkEndTime(worker.lNameProperty().get(), startLocalTime, endLocalTime, LocalDate.parse(date_lbl.getText()));
            getTimeOfEnd();
        } catch (NumberFormatException e) {
            // Handle possible exceptions (e.g., invalid number format)
            e.printStackTrace();
        } catch (DateTimeParseException e) {
            // Handle parsing exceptions for LocalTime or LocalDate
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // Handle the case where Time.valueOf() fails
            e.printStackTrace();
        }
    }

    private void bindFirstSecondName(){
        fname_lbl.textProperty().bind(new StringBinding() {
            {
                super.bind(worker.fNameProperty());
            }

            @Override
            protected String computeValue() {
                String fullName = worker.fNameProperty().get();
                return (fullName.isEmpty()) ? "" : fullName.substring(0, 1) + ".";
            }
        });
        sname_lbl.textProperty().bind(new StringBinding() {
            {
                super.bind(worker.sNameProperty());
            }

            @Override
            protected String computeValue() {
                String fullName = worker.sNameProperty().get();
                return (fullName.isEmpty()) ? "" : fullName.substring(0, 1) + ".";
            }
        });
    }

    private void dailyWorkUpdate(String lName, Time start,Time end, Date date){
        Model.getInstance().getDatabaseDriver().dailyWorkUpdate(lName, start, end, date);
    }

    public void saveWorkEndTime(String lastName, LocalTime timeOfBegin,LocalTime timeOfEnd, LocalDate date) {
        // Convert LocalTime to Time if needed by your database
        Time endTime = Time.valueOf(timeOfEnd);
        Time startTime = Time.valueOf(timeOfBegin);
        Date sqlDate = Date.valueOf(date);
        Model.getInstance().getDatabaseDriver().updateWorkerEndTime(lastName, startTime,endTime, sqlDate);
    }
}
