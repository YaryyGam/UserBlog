package com.jmc.medicalapplication.Controller.Admin;

import com.jmc.medicalapplication.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class WorkerController implements Initializable {

    public BorderPane worker_parent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getViewFactory().getAdminMenuOptionsItem().addListener((observableValue, oldVal, newVal) -> {
            switch (newVal){
                case WORKERS -> worker_parent.setCenter(Model.getInstance().getViewFactory().getDashboardView());
                case ADD_WORKER -> worker_parent.setCenter(Model.getInstance().getViewFactory().getAddWorkerView());
                default -> worker_parent.setCenter(Model.getInstance().getViewFactory().getDashboardView());
            }
        });
    }
}
