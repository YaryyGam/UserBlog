package com.jmc.medicalapplication.Controller.Admin;

import com.jmc.medicalapplication.Models.Model;
import com.jmc.medicalapplication.Views.AdminMenuOptions;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuPanelController implements Initializable {
    public Button workers_btn;
    public Button add_worker_btn;
    public Button search_by_date_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        workers_btn.setOnAction(e->onDashboard());
        add_worker_btn.setOnAction(e->onAddWorker());
        search_by_date_btn.setOnAction(e->onSearchByDate());
    }

    private void onDashboard(){Model.getInstance().getViewFactory().getAdminMenuOptionsItem().set(AdminMenuOptions.WORKERS);}
    private void onAddWorker(){Model.getInstance().getViewFactory().getAdminMenuOptionsItem().set(AdminMenuOptions.ADD_WORKER);}
    private void onSearchByDate(){Model.getInstance().getViewFactory().getAdminMenuOptionsItem().set(AdminMenuOptions.SEARCH_BY_DATE);}
}
