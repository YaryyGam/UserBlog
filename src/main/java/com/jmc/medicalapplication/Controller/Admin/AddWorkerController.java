package com.jmc.medicalapplication.Controller.Admin;

import com.jmc.medicalapplication.Models.DatabaseDriver;
import com.jmc.medicalapplication.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class AddWorkerController implements Initializable {
    public TextField fname_fld;
    public TextField sname_fld;
    public TextField lname_fld;
    public TextField startJob_fld;
    public TextField position_fld;
    public Label status_lbl;
    public Button add_user_btn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        add_user_btn.setOnAction(e->createWorker());
    }

    private void createWorker(){
        if(fname_fld.getText().isEmpty() || sname_fld.getText().isEmpty() || lname_fld.getText().isEmpty() || startJob_fld.getText().isEmpty()) {
            status_lbl.setText("Not Enough Data");
            clearFields();
        }else {
            Model.getInstance().getDatabaseDriver().createWorker(lname_fld.getText(), fname_fld.getText(), sname_fld.getText(), java.sql.Time.valueOf(LocalTime.parse(startJob_fld.getText().trim())), position_fld.getText(), java.sql.Date.valueOf(LocalDate.now()));
            status_lbl.setText("User Created Successfully");
            clearFields();
        }
    }

    private void clearFields(){
        fname_fld.setText("");
        sname_fld.setText("");
        lname_fld.setText("");
        startJob_fld.setText("");
        position_fld.setText("");
    }
}
