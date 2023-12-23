package at.fhtw.mtcg_app.persistence;

import at.fhtw.mtcg_app.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UnitOfWork implements AutoCloseable {

    /*
    Purpose of UnitOfWork
     The UnitOfWork pattern is commonly used in database operations to maintain a list of changes affecting the database and then execute them as a single transaction.
     This class handles the lifecycle of a database transaction, including starting the transaction, committing it, rolling it back if necessary, and closing the connection when done.
    */

    private Connection connection;

    public UnitOfWork() {
        this.connection = DatabaseManager.INSTANCE.getConnection();
        try {
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new at.fhtw.sampleapp.persistence.DataAccessException("Autocommit nicht deaktivierbar", e);
        }
    }

    public void commitTransaction() {
        if (this.connection != null) {
            try {
                this.connection.commit();
            } catch (SQLException e) {
                throw new at.fhtw.sampleapp.persistence.DataAccessException("Commit der Transaktion nicht erfolgreich", e);
            }
        }
    }

    public void setAutoCommit(boolean autoCommit) {
        if (this.connection != null) {
            try {
                this.connection.setAutoCommit(autoCommit);
            } catch (SQLException e) {
                throw new at.fhtw.sampleapp.persistence.DataAccessException("setting autoCommit failed", e);
            }
        }
    }

    public void rollbackTransaction() {
        if (this.connection != null) {
            try {
                this.connection.rollback();
            } catch (SQLException e) {
                throw new at.fhtw.sampleapp.persistence.DataAccessException("Rollback der Transaktion nicht erfolgreich", e);
            }
        }
    }

    public void finishWork() {
        if (this.connection != null) {
            try {
                this.connection.close();
                this.connection = null;
            } catch (SQLException e) {
                throw new at.fhtw.sampleapp.persistence.DataAccessException("Schließen der Connection nicht erfolgreich", e);
            }
        }
    }

    public PreparedStatement prepareStatement(String sql) {
        if (this.connection != null) {
            try {
                return this.connection.prepareStatement(sql);
            } catch (SQLException e) {
                throw new at.fhtw.sampleapp.persistence.DataAccessException("Erstellen eines PreparedStatements nicht erfolgreich", e);
            }
        }
        throw new DataAccessException("UnitOfWork hat keine aktive Connection zur Verfügung");
    }

    public List<User> readUsersFromDB(String tableName, String whereClause) throws SQLException {
        Statement statement;
        ResultSet rs = null;
        List<User> userList = new ArrayList<>(); // Liste für Benutzerdaten
        if (this.connection != null) {
            try {
                String query = String.format("SELECT * FROM %s WHERE %s", tableName, whereClause);
                statement = this.connection.createStatement();
                rs = statement.executeQuery(query);

                while (rs.next()) {
                    User user = new User(
                            rs.getString("token"),
                            rs.getString("user_id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getInt("coins")
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
            return userList;
        }

        throw new DataAccessException("UnitOfWork hat keine aktive Connection zur Verfügung");
    }

    public List<User> readAllUsersFromDB(String tableName) throws SQLException {
        return readUsersFromDB(tableName, "1=1"); // 1=1 ist immer wahr, daher werden alle Datensätze ausgewählt
    }

    public List<User> readSpecificUserFromDB(String tableName, String userName) throws SQLException {
        String whereClause = String.format("username = '%s'", userName);
        return readUsersFromDB(tableName, whereClause);
    }

    @Override
    public void close() throws Exception {
        this.finishWork();
    }
}
