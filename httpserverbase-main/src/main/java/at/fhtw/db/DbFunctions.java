package at.fhtw.db;
import at.fhtw.users.model.Users;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbFunctions {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/swen1db";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "pwd123456";

    public DbFunctions() {
        // Konstruktor ohne automatische Verbindungsherstellung
    }

    public Connection connectToDatabase() throws SQLException {
        Connection conn;
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            if (conn != null) {
                System.out.println("Verbindung zur Datenbank hergestellt");
            } else {
                System.out.println("Konnte keine Verbindung zur Datenbank herstellen");
            }
        } catch (SQLException e) {
            System.out.println("Fehler bei der Verbindung zur Datenbank: " + e.getMessage());
            throw new SQLException(e);
        }
        return conn;
    }
    public List<Users> readFromDatabase(Connection conn, String tableName) throws SQLException {
        Statement statement;
        ResultSet rs = null;
        List<Users> userList = new ArrayList<>(); // Liste für Benutzerdaten

        try {
            String query = String.format("SELECT * FROM %s", tableName);
            statement = conn.createStatement();
            rs = statement.executeQuery(query);

            while (rs.next()) {
                Users user = new Users(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password")
                );
                userList.add(user); // Benutzer zur Liste hinzufügen
            }

        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            if (rs != null) {
                rs.close(); // ResultSet schließen
            }
        }

        return userList; // Die Liste der Benutzerdaten zurückgeben
    }

}