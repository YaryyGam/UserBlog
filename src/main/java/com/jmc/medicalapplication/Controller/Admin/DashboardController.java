package com.jmc.medicalapplication.Controller.Admin;

import com.jmc.medicalapplication.Models.Model;
import com.jmc.medicalapplication.Views.WorkerCellFactory;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    public Label date_lbl;
    public ListView worker_listview;
    public Button refresh_btn;

    private final Date date = Date.valueOf(LocalDate.now());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initAllWorkers();
        updateDate();
        // Set date_lbl text
        date_lbl.setText(date.toString());
        worker_listview.setItems(Model.getInstance().getAllWorkers());
        worker_listview.setCellFactory(e -> new WorkerCellFactory());
        getWorkersLog(Date.valueOf(date_lbl.getText()));
        refresh_btn.setOnAction(e -> {
            Model.getInstance().getDatabaseDriver().createDailyWorkRecords(date);
            updateWorkers();
        });
    }

    private void initAllWorkers() {
        if (Model.getInstance().getAllWorkers().isEmpty()) {
            Model.getInstance().setLatestWorkers();
        }
    }

    private void updateDate(){
        Model.getInstance().getDatabaseDriver().updateWorkerDate();
    }

    private void updateWorkers() {
        Model.getInstance().updateWorkers();
    }

    private void getWorkersLog(Date date) {
        try {
            Model.getInstance().getWorkerLog(date);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();  // Handle or log the error
        }
    }
}
