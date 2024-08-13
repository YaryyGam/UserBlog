package com.jmc.medicalapplication.Controller.Admin;

import com.jmc.medicalapplication.Models.Worker;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class WorkerCellController implements Initializable {
    public Label date_lbl;
    public Label fname_lbl;
    public Label sname_lbl;
    public Label lname_lbl;
    public Label time_lbl;
    public TextField hours_fld;

    private final Worker worker;

    public WorkerCellController(Worker worker){this.worker = worker;}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        date_lbl.textProperty().bind(worker.dateProperty().asString());
        fname_lbl.textProperty().bind(worker.lNameProperty());
        sname_lbl.textProperty().bind(worker.sNameProperty());
        lname_lbl.textProperty().bind(worker.lNameProperty());
        time_lbl.textProperty().bind(worker.timeOfBeginProperty().asString());
        hours_fld.setOnAction(e->{

        });
    }
}
