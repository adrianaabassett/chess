package dataaccess;

import com.google.gson.Gson;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.ResponseException;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

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
            throw new DataAccessException("unable to configure database");//response exception was here
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  userTable (
              `Username` varchar(256) NOT NULL,
              `Password` varchar(256) NOT NULL,
              `Email` varchar(256),
              PRIMARY KEY (`Username`)
            ) 
            """
    };

    public void clear() throws DataAccessException{
        var statement = "TRUNCATE userTable";
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("failed to clear userTable", ex);
        } catch (DataAccessException e) {
            throw new DataAccessException("failed to get connection");
        }
    }

    public void createUser(UserData userData) throws DataAccessException {
        var statement = "INSERT INTO userTable (Username, Password, Email) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(statement)) {
                ps.setString(1,userData.username());
                String safePassword = BCrypt.hashpw(userData.password(), BCrypt.gensalt());
                ps.setString(2,safePassword);
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
            try (PreparedStatement ps = connection.prepareStatement("SELECT Username, Password, Email FROM userTable WHERE Username=?")) {
                ps.setString(1,username);
                try (ResultSet rs = ps.executeQuery()) {
                    if(rs.next()){
                        String decryptedPass =  rs.getString("Password");
                        return new UserData(rs.getString("Username"),decryptedPass, rs.getString("Email"));
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
