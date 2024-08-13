package com.jmc.medicalapplication.Models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseDriver {
    private Connection connection;

    public DatabaseDriver(){
        try {
            this.connection = DriverManager.getConnection("jdbc:sqlite:medicalworkers.db");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
