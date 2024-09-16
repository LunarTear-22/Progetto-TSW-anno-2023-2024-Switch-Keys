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

public class SpedizioneModel {
    private static DataSource ds;
    private static final String TABLE_NAME = "Spedizione";
    
    static {
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            ds = (DataSource) envCtx.lookup("jdbc/SwitchAndKeys");
        } catch (NamingException e) {
            System.out.println("Error:" + e.getMessage());
        }
    }

    // Metodo per salvare una nuova spedizione nel database
    public synchronized void doSaveSpedizione(SpedizioneBean spedizione) throws SQLException {
        String insertSQL = "INSERT INTO " + TABLE_NAME + " (id_indirizzo, costi, data, rif_username_cliente) VALUES (?, ?, ?, ?)";

        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            
            preparedStatement.setInt(1, spedizione.getIdIndirizzo());
            preparedStatement.setDouble(2, spedizione.getCosti());
            preparedStatement.setTimestamp(3, spedizione.getData()); 
            preparedStatement.setString(4, spedizione.getRifUsernameCliente()); 

            preparedStatement.executeUpdate();
        }
    }

    // Metodo per recuperare tutte le spedizioni dal database
    public synchronized List<SpedizioneBean> doRetrieveAllSpedizioni() throws SQLException {
        List<SpedizioneBean> spedizioni = new ArrayList<>();
        String selectSQL = "SELECT * FROM " + TABLE_NAME;

        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
             ResultSet rs = preparedStatement.executeQuery()) {
            
            while (rs.next()) {
                SpedizioneBean spedizione = new SpedizioneBean();
                spedizione.setIdSpedizione(rs.getInt("id_spedizione"));
                spedizione.setIdIndirizzo(rs.getInt("id_indirizzo"));
                spedizione.setCosti(rs.getDouble("costi"));
                spedizione.setData(rs.getTimestamp("data"));
                spedizione.setRifUsernameCliente(rs.getString("rif_username_cliente"));

                spedizioni.add(spedizione);
            }
        }
        return spedizioni;
    }

    // Metodo per recuperare tutte le spedizioni effettuate a un determinato indirizzo
    public synchronized List<SpedizioneBean> doRetrieveSpedizioniByIndirizzo(int idIndirizzo) throws SQLException {
        List<SpedizioneBean> spedizioni = new ArrayList<>();
        String selectSQL = "SELECT * FROM " + TABLE_NAME + " WHERE id_indirizzo = ?";

        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            
            preparedStatement.setInt(1, idIndirizzo);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    SpedizioneBean spedizione = new SpedizioneBean();
                    spedizione.setIdSpedizione(rs.getInt("id_spedizione"));
                    spedizione.setIdIndirizzo(rs.getInt("id_indirizzo"));
                    spedizione.setCosti(rs.getDouble("costi"));
                    spedizione.setData(rs.getTimestamp("data"));
                    spedizione.setRifUsernameCliente(rs.getString("rif_username_cliente")); 

                    spedizioni.add(spedizione);
                }
            }
        }
        return spedizioni;
    }
    
    // Metodo per recuperare l'ultima spedizione effettuata da un determinato utente
    public synchronized SpedizioneBean doRetrieveLastSpedizioneByUser(String usernameCliente) throws SQLException {
        String selectSQL = "SELECT id_spedizione, id_indirizzo, costi, data " +
                           "FROM " + TABLE_NAME + " " +
                           "WHERE rif_username_cliente = ? " +
                           "ORDER BY data DESC LIMIT 1";

        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            
            preparedStatement.setString(1, usernameCliente);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    SpedizioneBean spedizione = new SpedizioneBean();
                    spedizione.setIdSpedizione(rs.getInt("id_spedizione"));
                    spedizione.setIdIndirizzo(rs.getInt("id_indirizzo"));
                    spedizione.setCosti(rs.getDouble("costi"));
                    spedizione.setData(rs.getTimestamp("data")); 
                    return spedizione;
                } else {
                    return null; 
                }
            }
        }
    }
}
