package com.jmc.medicalapplication.Controller.Admin;

import com.jmc.medicalapplication.Models.Model;
import com.jmc.medicalapplication.Views.WorkerCellFactory;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class SearchByDateController implements Initializable {
    public ListView workers_listview;
    public TextField search_fld;
    public Button search_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        search_btn.setOnAction(event -> searchWorkers());

        // Додати можливість запуску пошуку при натисканні клавіші Enter
        search_fld.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                searchWorkers();
            }
        });
    }

    private void searchWorkers() {
        // Отримання дати з поля введення
        String dateStr = search_fld.getText();
        try {
            LocalDate localDate = LocalDate.parse(dateStr);
            Date sqlDate = Date.valueOf(localDate);

            // Виконати пошук робітників за даною датою
            Model.getInstance().FindWorkersByDate(sqlDate);

            // Оновити ListView
            workers_listview.setItems(Model.getInstance().getAllWorkers());
            workers_listview.setCellFactory(e -> new WorkerCellFactory());
        } catch (Exception e) {
            // Обробка помилки, якщо введена дата недійсна
            System.err.println("Invalid date format: " + e.getMessage());
        }
    }
}
