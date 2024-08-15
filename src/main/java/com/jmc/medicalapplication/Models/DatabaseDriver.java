package com.jmc.medicalapplication.Models;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

public class DatabaseDriver {
    private Connection connection;

    public DatabaseDriver(){
        try {
            this.connection = DriverManager.getConnection("jdbc:sqlite:medicalworkers.db");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void createWorker(String lName, String fName, String sName, Time timeOfBegin, String currentPosition, Date date) {
        PreparedStatement statement;
        try {
            String query = "INSERT INTO Workers (LastName, FirstName, SecondName, TimeOfBegin, CurrentPosition, Date) VALUES (?, ?, ?, ?, ?, ?)";
            statement = this.connection.prepareStatement(query);
            statement.setString(1, lName);
            statement.setString(2, fName);
            statement.setString(3, sName);
            statement.setString(4, timeOfBegin.toString());
            statement.setString(5, currentPosition);
            statement.setString(6, date.toString());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getWorkersByDate(Date date) {
        String query = "SELECT * FROM WorkRecords WHERE Date = ?";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setDate(1, date);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateWorkerDate() {
        LocalDate currentDate = LocalDate.now(); // Отримання актуальної дати
        String currentDateStr = currentDate.toString(); // Формат YYYY-MM-DD

        String query = "UPDATE Workers SET Date = ? WHERE Date IS NOT ?"; // Оновлює дату лише для записів, де дата не є актуальною

        try (PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
            preparedStatement.setString(1, currentDateStr);
            preparedStatement.setString(2, currentDateStr); // Не оновлювати записи, де вже стоїть актуальна дата

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("Updated rows: " + rowsAffected);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean workerExists(String lName) {
        PreparedStatement statement;
        ResultSet resultSet;
        try {
            String query = "SELECT COUNT(*) FROM Workers WHERE LastName=?";
            statement = this.connection.prepareStatement(query);
            statement.setString(1, lName);

            resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void dailyWorkUpdate(String lName, Time startTime,Time endWork, Date date) {
        if (workerExists(lName)) {
            updateWorkerEndTime(lName, startTime, endWork, date);
        }
    }

    public ResultSet getAllWorkers(){
        String query = "SELECT * FROM Workers";
        try {
            Statement statement = this.connection.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Time getTimeOfEnd(String lastName, LocalDate date) {
        String query = "SELECT TimeOfEnd FROM WorkRecords WHERE LastName = ? AND Date = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, lastName);
            statement.setString(2, date.toString()); // Формат YYYY-MM-DD

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String timeStr = resultSet.getString("TimeOfEnd"); // Отримуємо рядок
                if (timeStr != null && !timeStr.isEmpty()) {
                    return Time.valueOf(timeStr); // Конвертуємо рядок у Time
                } else {
                    return null; // Повертаємо null, якщо час закінчення відсутній
                }
            } else {
                return null; // Повертаємо null, якщо запис не знайдено
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateWorkerEndTime(String lastName, Time timeOfBegin, Time timeOfEnd, Date date) {
        try {
            String query = "UPDATE WorkRecords SET TimeOfEnd = ?, TimeOfBegin = ? WHERE LastName = ? AND Date = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, timeOfEnd.toString()); // формат HH:MM:SS
            preparedStatement.setString(2, timeOfBegin.toString()); // формат HH:MM:SS
            preparedStatement.setString(3, lastName);
            preparedStatement.setString(4, date.toString()); // формат YYYY-MM-DD

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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

    public boolean isEndTimeSet(String lastName, Date date) {
        String query = "SELECT TimeOfEnd FROM WorkRecords WHERE LastName = ? AND Date = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, lastName);
            statement.setString(2, date.toString());

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Time timeOfEnd = resultSet.getTime("TimeOfEnd");
                return timeOfEnd != null;
            } else {
                System.out.println("Record not found for " + lastName + " on " + date);
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void createDailyWorkRecords(Date date) {
        try {
            // Створення PreparedStatement для перевірки наявності записів
            String countQuery = "SELECT COUNT(*) FROM WorkRecords WHERE Date = ?";
            try (PreparedStatement countStatement = this.connection.prepareStatement(countQuery)) {
                countStatement.setString(1, date.toString()); // Формат YYYY-MM-DD
                try (ResultSet resultSet = countStatement.executeQuery()) {
                    resultSet.next();
                    int count = resultSet.getInt(1);

                    // Якщо немає записів для цієї дати
                    if (count == 0) {
                        // Отримання всіх робітників
                        String workersQuery = "SELECT LastName, FirstName, SecondName, TimeOfBegin FROM Workers";
                        try (Statement workersStatement = this.connection.createStatement();
                             ResultSet workers = workersStatement.executeQuery(workersQuery)) {

                            while (workers.next()) {
                                String lastName = workers.getString("LastName");
                                String timeOfBegin = workers.getString("TimeOfBegin");

                                // Вставка нових записів у таблицю WorkRecords
                                String insertQuery = "INSERT INTO WorkRecords (LastName, TimeOfBegin, TimeOfEnd, Date) VALUES (?, ?, ?, ?)";
                                try (PreparedStatement insertStatement = this.connection.prepareStatement(insertQuery)) {
                                    insertStatement.setString(1, lastName);
                                    insertStatement.setString(2, timeOfBegin);
                                    insertStatement.setNull(3, Types.VARCHAR); // Запис TimeOfEnd як NULL
                                    insertStatement.setString(4, date.toString()); // Формат YYYY-MM-DD

                                    insertStatement.executeUpdate();
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
