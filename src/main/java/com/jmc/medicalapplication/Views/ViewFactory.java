package com.jmc.medicalapplication.Views;

import com.jmc.medicalapplication.Controller.Admin.WorkerController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ViewFactory {

    private AnchorPane dashboardView;
    private AnchorPane addWorkerView;
    private final ObjectProperty<AdminMenuOptions> adminMenuOptionsItem;

    public ViewFactory() {
        this.adminMenuOptionsItem = new SimpleObjectProperty<>();
    }

    public ObjectProperty<AdminMenuOptions> getAdminMenuOptionsItem(){return adminMenuOptionsItem;}

    public AnchorPane getDashboardView(){
        if(dashboardView == null){
            try {
                dashboardView = new FXMLLoader(getClass().getResource("/com/jmc/medicalapplication/FXML/DashboardView.fxml")).load();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return dashboardView;
    }

    public AnchorPane getAddWorkerView(){
        if(addWorkerView == null){
            try {
                addWorkerView = new FXMLLoader(getClass().getResource("/com/jmc/medicalapplication/FXML/AddWorker.fxml")).load();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return addWorkerView;
    }

    public void showUserWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Admin.fxml"));
        WorkerController workerController = new WorkerController();
        loader.setController(workerController);
        createStage(loader);
    }

    public void createStage(FXMLLoader loader){
        Scene scene = null;
        try{
            scene = new Scene(loader.load());
        }catch (Exception e){
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        //stage.getIcons().add(new Image());
        stage.setResizable(false);
        stage.setTitle("MedApp");
        stage.show();
    }

    public void closeStage(Stage stage){
        stage.close();
    }
}
