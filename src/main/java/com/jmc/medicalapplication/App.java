package com.jmc.medicalapplication;

import com.jmc.medicalapplication.Models.Model;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Model.getInstance().getViewFactory().showUserWindow();
    }

    public static void main(String[] args) {
        launch();
    }
}
