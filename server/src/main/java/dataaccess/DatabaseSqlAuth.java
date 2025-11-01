package dataaccess;

import com.google.gson.Gson;
import com.mysql.cj.x.protobuf.MysqlxCrud;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.ResponseException;
import model.AuthData;
import model.UserData;

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
        Connection conn = DatabaseManager.getConnection();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("INSERT INTO auth (AuthToken, Username) VALUES (?, ?)");
            ps.setString(1, authData.authToken());
            ps.setString(2, authData.username());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT Username, AuthToken FROM auth WHERE AuthToken=?")) {
                //capitalize
                ps.setString(1, authToken);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new AuthData(authToken, rs.getString("Username"));
                    } else {
                        throw new DataAccessException("error nothing found in auth");

                    }
                    /// gets string from column labeled Username
                } catch (Exception e) {
                    throw new DataAccessException("error getting Auth from database");
                }
            } catch (Exception e) {
                throw new DataAccessException("error getting Auth from database");
            }
        } catch (Exception e) {
            throw new DataAccessException("error getting Auth from database");
        }
    }

    public void deleteAuth(String authToken) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT Username, AuthToken FROM auth WHERE AuthToken=?")) {
                ps.setString(1, authToken);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new DataAccessException("doesnt exist and cant be deleted");
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            var statement = "DELETE FROM auth WHERE AuthToken=?";
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, authToken);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}