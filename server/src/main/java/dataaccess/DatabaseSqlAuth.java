package dataaccess;

import com.google.gson.Gson;
import com.mysql.cj.x.protobuf.MysqlxCrud;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.ResponseException;
import model.AuthData;

import java.sql.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class DatabaseSqlAuth implements AuthDAO {

    public DatabaseSqlAuth() throws ResponseException, DataAccessException {
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
            throw new ResponseException("Error with configuring the database");
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  auth (
              `AuthToken` varchar(256) NOT NULL,
              `Username` varchar(256) NOT NULL,
              PRIMARY KEY (`AuthToken`)
            )
            """
    };

    public void clear() throws DataAccessException {
        var statement = "TRUNCATE auth";
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("failed to clear Auth", ex);
        }
    }

    public void createAuth(AuthData authData) throws DataAccessException {
        var statement = "INSERT INTO auth (AuthToken, Username) VALUES (?, ?)";
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("failed to create Auth", ex);
        }
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT Username, AuthToken FROM auth WHERE AuthToken=?")) {
                //capitalize
                try (ResultSet rs = ps.executeQuery()) {
                    if(rs.next()){
                        return new AuthData(authToken, rs.getString("Username"));
                    }
                    else{
                        return null;
                    }
                    /// gets string from column labeled Username
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("error getting Auth from database");
        }
    }

    public void deleteAuth(String authToken) throws DataAccessException {
        var statement = "DELETE FROM pet WHERE AuthToken=?";
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("failed to delete using AuthToken", ex);
        }
    }


    private void getAuthExecuteUpdate(String statement, Object... params) throws ResponseException, SQLException {
        try (var conn = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "monkeypie")) {
            conn.setCatalog("pet_store");
            try (var preparedStatement = conn.prepareStatement("SELECT id, name, type from pet")) {
                try (var rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        var id = rs.getInt("id");
                        var name = rs.getString("name");
                        var type = rs.getString("type");
                        System.out.printf("id: %d, name: %s, type: %s%n", id, name, type);
                    }
                }
            }
        }
    }
}
