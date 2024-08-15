package com.jmc.medicalapplication.Controller.Admin;

import com.jmc.medicalapplication.Models.DatabaseDriver;
import com.jmc.medicalapplication.Models.Model;
import com.jmc.medicalapplication.Models.Worker;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
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
        fname_lbl.textProperty().bind(worker.lNameProperty());
        sname_lbl.textProperty().bind(worker.sNameProperty());
        lname_lbl.textProperty().bind(worker.lNameProperty());
        time_lbl.setText(worker.timeOfBeginProperty().toString());
        update_btn.setOnAction(e->{
            // Get the value of hours worked from the text field
            double hoursWorked = Double.parseDouble(hours_fld.getText());

            // Extract whole hours and minutes from the fractional value
            int hours = (int) hoursWorked;
            int minutes = (int) ((hoursWorked - hours) * 60);

            // Calculate the end time by adding hours and minutes
            LocalTime timeOfEnd = worker.timeOfBeginProperty().plusHours(hours).plusMinutes(minutes);

            // Зберігаємо значення в базу даних
            saveWorkEndTime(worker.lNameProperty().toString(), timeOfEnd, worker.dateProperty());
            getTimeOfEnd();
        });
    }

    public void getTimeOfEnd(){
        if(Model.getInstance().getDatabaseDriver().isEndTimeSet(worker.lNameProperty().toString(), worker.dateProperty())){
            if(end_time_lbl.getText().isEmpty()){
                end_time_lbl.setText(Model.getInstance().getDatabaseDriver().getTimeOfEnd(worker.lNameProperty().toString(), worker.dateProperty()).toString());
            }
        }else {
            end_time_lbl.setText("");
        }
    }

    public void saveWorkEndTime(String lastName, LocalTime timeOfEnd, LocalDate date) {
        Model.getInstance().getDatabaseDriver().updateWorkerEndTime(lastName, timeOfEnd, date);
    }
}
