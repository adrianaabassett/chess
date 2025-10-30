package dataaccess;

import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.ResponseException;
import model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseSqlUser implements UserDAO {

    public DatabaseSqlUser() throws ResponseException, DataAccessException {
        configureDatabase();
    }

    private void configureDatabase() throws ResponseException, DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new ResponseException("unable to configure database");
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  user (
              `Username` varchar(256) NOT NULL,
              `Password` varchar(256) NOT NULL,
              `Email` varchar(256),
              PRIMARY KEY (`Username`)
            ) 
            """
    };

    public void clear() throws DataAccessException{
        var statement = "TRUNCATE user";
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("failed to clear User", ex);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void createUser(UserData userData) throws DataAccessException {
        var statement = "INSERT INTO user (Username, Password, Email) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(statement)) {
                ps.setString(1,userData.username());
                ps.setString(2,userData.password());
                ps.setString(3,userData.email());
                ps.executeUpdate();
                //here is thr problem
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public UserData getUser(String username) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("SELECT Username, Password, Email FROM user WHERE Username=?")) {
                ps.setString(1,username);
                try (ResultSet rs = ps.executeQuery()) {
                    if(rs.next()){
                        return new UserData(rs.getString("Username"), rs.getString("Password"), rs.getString("Email"));
                    }
                    else{
                        return null;
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }


}
