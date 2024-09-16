package SwitchAndKeysModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class UtenteModel {

    private static DataSource ds;
    private static final String TABLE_NAME = "Cliente";
    private static final String TABLE_NAME2 = "Carrello";

    static {
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            ds = (DataSource) envCtx.lookup("jdbc/SwitchAndKeys");
        } catch (NamingException e) {
            System.out.println("Error:" + e.getMessage());
        }
    }

    // Verifica se l'email esiste già nel database
    public synchronized boolean emailExists(String email) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        boolean exists = false;

        String query = "SELECT COUNT(*) FROM " + UtenteModel.TABLE_NAME + " WHERE email = ?";

        try {
            connection = ds.getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    exists = resultSet.getInt(1) > 0;
                }
            }

        } finally {
            if (statement != null)
                statement.close();
            if (connection != null)
                connection.close();
        }

        return exists;
    }

    // Controlla le credenziali
    public synchronized boolean checkCredentials(String email, String password) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        boolean res = false;

        String query = "SELECT * FROM " + UtenteModel.TABLE_NAME + " WHERE email = ? AND password = ?";

        try {
            connection = ds.getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                res = resultSet.next();
            }

        } finally {
            if (statement != null)
                statement.close();
            if (connection != null)
                connection.close();
        }

        return res;
    }

    // Salva un nuovo utente
    public synchronized boolean saveUser(UtenteBean user) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        PreparedStatement statement2 = null;
        int res1 = 0, res2 = 0;
        boolean res = false;

        String query = "INSERT INTO " + UtenteModel.TABLE_NAME +
                " (email, password, nome, cognome, amministratore)" +
                " VALUES (?, ?, ?, ?, ?)";
        
        String query2 = "INSERT INTO " + UtenteModel.TABLE_NAME2 +
                " (id_carrello, rif_username_cliente)" +
                " VALUES (?,?)";
        
        try {
            connection = ds.getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getNome());
            statement.setString(4, user.getCognome());
            statement.setBoolean(5, user.isAdmin());
            
            res1 = statement.executeUpdate();
            
            statement2 = connection.prepareStatement(query2);
            statement2.setInt(1, 1);
            statement2.setString(2, user.getEmail());
            
            res2 = statement2.executeUpdate();           
            
            res = (res1 != 0) && (res2 != 0);
            
        } finally {
            if (statement != null)
                statement.close();
            if (statement2 != null)
                statement2.close();
            if (connection != null)
                connection.close();
        }

        return res;
    }

    // Aggiorna un utente per renderlo amministratore
    public synchronized boolean makeAdmin(String email) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        int res = 0;

        String query = "UPDATE " + UtenteModel.TABLE_NAME +
                " SET amministratore = true" +
                " WHERE email = ?";

        try {
            connection = ds.getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, email);

            res = statement.executeUpdate();

        } finally {
            if (statement != null)
                statement.close();
            if (connection != null)
                connection.close();
        }

        return res != 0;
    }
    
    public synchronized boolean removeAdmin(String email) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        int res = 0;

        String query = "UPDATE " + UtenteModel.TABLE_NAME +
                " SET amministratore = false" +
                " WHERE email = ?";

        try {
            connection = ds.getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, email);

            res = statement.executeUpdate();

        } finally {
            if (statement != null)
                statement.close();
            if (connection != null)
                connection.close();
        }

        return res != 0;
    }

    // Ottieni un utente per email
    public synchronized UtenteBean getUserByEmail(String email) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        UtenteBean user = null;

        String query = "SELECT * FROM " + UtenteModel.TABLE_NAME + " WHERE email = ?";

        try {
            connection = ds.getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user = new UtenteBean();
                    user.setEmail(resultSet.getString("email"));
                    user.setPassword(resultSet.getString("password"));
                    user.setNome(resultSet.getString("nome"));
                    user.setCognome(resultSet.getString("cognome"));
                    user.setAdmin(resultSet.getBoolean("amministratore"));
                }
            }

        } finally {
            if (statement != null)
                statement.close();
            if (connection != null)
                connection.close();
        }

        return user;
    }

    // Recupera tutti gli utenti dal database
    public synchronized List<UtenteBean> doRetrieveAllUser() throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<UtenteBean> userList = new ArrayList<>();

        String query = "SELECT * FROM " + UtenteModel.TABLE_NAME;

        try {
            connection = ds.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                UtenteBean user = new UtenteBean();
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setNome(resultSet.getString("nome"));
                user.setCognome(resultSet.getString("cognome"));
                user.setAdmin(resultSet.getBoolean("amministratore"));
                userList.add(user);
            }

        } finally {
            if (resultSet != null)
                resultSet.close();
            if (statement != null)
                statement.close();
            if (connection != null)
                connection.close();
        }
        System.out.println("Lista Da DB " + userList);
        return userList;
    }
}
