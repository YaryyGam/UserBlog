package com.jmc.medicalapplication.Controller.Admin;

import com.jmc.medicalapplication.Models.Model;
import com.jmc.medicalapplication.Views.WorkerCellFactory;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    public Label date_lbl;
    public ListView worker_listview;
    public Button refresh_btn;

    private final LocalDate date = LocalDate.now();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refresh_btn.setOnAction(e-> Model.getInstance().getDatabaseDriver().createDailyWorkRecords(date));
        initAllWorkers();
        date_lbl.setText((date.toString()));
        worker_listview.setCellFactory(e->new WorkerCellFactory());
        refresh_btn.setOnAction(e->updateWorkers());
    }

    private void initAllWorkers(){
        if(Model.getInstance().getAllWorkers().isEmpty()){
            Model.getInstance().setLatestWorkers();
        }
    }

    private void updateWorkers(){
        Model.getInstance().updateWorkers();
    }
}
