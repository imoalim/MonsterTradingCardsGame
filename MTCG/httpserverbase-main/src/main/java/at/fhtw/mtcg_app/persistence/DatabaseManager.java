package at.fhtw.mtcg_app.persistence;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public enum DatabaseManager {
    /*Concept: Singleton Pattern
The code is an implementation of the Singleton design pattern using an enum in Java.
A singleton is a design pattern that restricts the instantiation of a class to one single instance.
This is useful when exactly one object is needed to coordinate actions across the system.

In Java, the simplest way to implement a singleton is using an enum with a single element.
Here, DatabaseManager is an enum with a single element INSTANCE. This ensures that there is only one instance of DatabaseManager throughout the Java application.

*/
    INSTANCE;

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/swen1db",
                    "postgres",
                    "pwd123456");
        } catch (SQLException e) {
            throw new DataAccessException("Datenbankverbindungsaufbau nicht erfolgreich", e);
        }
    }
}
