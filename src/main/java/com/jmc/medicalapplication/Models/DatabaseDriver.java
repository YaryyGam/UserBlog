package com.jmc.medicalapplication.Models;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class DatabaseDriver {
    private Connection connection;

    public DatabaseDriver(){
        try {
            this.connection = DriverManager.getConnection("jdbc:sqlite:medicalworkers.db");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void createWorker(String lName, String fName, String sName, LocalTime timeOfBegin, String currentPosition, LocalDate date) {
        PreparedStatement statement;
        try {
            String query = "INSERT INTO Workers (LastName, FirstName, SecondName, TimeOfBegin, CurrentPosition, Date) VALUES (?, ?, ?, ?, ?, ?)";
            statement = this.connection.prepareStatement(query);
            statement.setString(1, lName);
            statement.setString(2, fName);
            statement.setString(3, sName);
            statement.setTime(4, java.sql.Time.valueOf(timeOfBegin));
            statement.setString(5, currentPosition);
            statement.setDate(6, java.sql.Date.valueOf(date));

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getAllWorkers(){
        Statement statement;
        ResultSet resultSet=null;
        try {
            statement=this.connection.createStatement();
            resultSet=statement.executeQuery("SELECT * FROM Workers;");
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet getWorkerTime(String lName){
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            String sql = "SELECT * FROM Workers WHERE LastName=?";
            preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setString(1, lName);
            resultSet = preparedStatement.executeQuery();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return resultSet;
    }

    /*
    Utility methods
    *
    * */

    public void createDailyWorkRecords(LocalDate date, LocalTime timeOfBegin, LocalTime timeOfEnd) {
        Statement statement;
        ResultSet resultSet;

        try {
            statement = this.connection.createStatement();

            // Перевірка, чи існують записи для цієї дати
            resultSet = statement.executeQuery("SELECT COUNT(*) AS count FROM WorkRecords WHERE Date = '" + date.toString() + "'");
            resultSet.next();
            int count = resultSet.getInt("count");

            // Якщо записів немає, створюємо їх
            if (count == 0) {
                // Отримуємо всіх працівників
                ResultSet workers = statement.executeQuery("SELECT LastName, FirstName, SecondName FROM Workers");
                while (workers.next()) {
                    String lastName = workers.getString("LastName");
                    String firstName = workers.getString("FirstName");
                    String secondName = workers.getString("SecondName");

                    // Створення запису про робочі години
                    String insertQuery = "INSERT INTO WorkRecords (LastName, TimeOfBegin, TimeOfEnd, Date) " +
                            "VALUES (?, ?, ?, ?)";
                    PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                    insertStatement.setString(1, lastName);
                    insertStatement.setTime(2, Time.valueOf(timeOfBegin));
                    insertStatement.setTime(3, Time.valueOf(timeOfEnd));
                    insertStatement.setDate(4, Date.valueOf(date));

                    insertStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
