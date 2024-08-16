package com.jmc.medicalapplication.Controller.Admin;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;


import java.net.URL;

import java.util.ResourceBundle;

public class SearchByDateController implements Initializable {
    public ListView workers_listview;
    public TextField search_fld;
    public Button search_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        search_btn.setOnAction(event -> {

        });
    }
}
