package com.jmc.medicalapplication.Views;

import com.jmc.medicalapplication.Controller.Admin.WorkerCellController;
import com.jmc.medicalapplication.Models.Worker;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;

public class WorkerCellFactory extends ListCell<Worker> {

    @Override
    protected void updateItem(Worker worker, boolean empty) {
        super.updateItem(worker, empty);
        if(empty){
            setText(null);
            setGraphic(null);
        }else{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/WorkerCell.fxml"));
            WorkerCellController controller = new WorkerCellController(worker);
            loader.setController(controller);
            setText(null);
            try {
                setGraphic(loader.load());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
