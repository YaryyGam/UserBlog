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

    public Time getTimeOfEnd(String lastName, LocalDate date) {
        String query = "SELECT TimeOfEnd FROM WorkRecords WHERE LastName = ? AND Date = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, lastName);
            statement.setDate(2, java.sql.Date.valueOf(date));

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getTime("TimeOfEnd");
            } else {
                System.out.println("Record not found for " + lastName + " on " + date);
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResultSet getWorker(String lName){
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

    public ResultSet getWorkRecords(String lName){
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            String sql = "SELECT * FROM WorkRecords WHERE LastName=?";
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

    public boolean isEndTimeSet(String lastName, LocalDate date) {
        String query = "SELECT TimeOfEnd FROM WorkRecords WHERE LastName = ? AND Date = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, lastName);
            statement.setDate(2, java.sql.Date.valueOf(date));

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Time timeOfEnd = resultSet.getTime("TimeOfEnd");
                return timeOfEnd != null; // Повертає true, якщо час завершення роботи встановлений
            } else {
                System.out.println("Record not found for " + lastName + " on " + date);
                return false; // Повертає false, якщо запису немає
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Повертає false у випадку помилки
        }
    }

    public void updateWorkerEndTime(String lastName, LocalTime timeOfEnd, LocalDate date) {
        try {
            String query = "UPDATE WorkRecords SET TimeOfEnd = ? WHERE LastName = ? AND Date = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setTime(1, Time.valueOf(timeOfEnd));
            preparedStatement.setString(2, lastName);
            preparedStatement.setDate(3, Date.valueOf(date));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createDailyWorkRecords(LocalDate date) {
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
                    insertStatement.setDate(4, Date.valueOf(date));

                    insertStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
