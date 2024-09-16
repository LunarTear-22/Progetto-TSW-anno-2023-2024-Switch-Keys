package SwitchAndKeysModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class OrdineModel {
    private static DataSource ds;
    private static final String TABLE_NAME = "Ordine";
    private static final String TABLE_NAME2 = "Indirizzo_Spedizione";
    private static final String TABLE_NAME3 = "Spedizione";


    static {
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            ds = (DataSource) envCtx.lookup("jdbc/SwitchAndKeys");
        } catch (NamingException e) {
            System.out.println("Error:" + e.getMessage());
        }
    }

    // Metodo per salvare un nuovo ordine nel database
    public synchronized void doSaveOrder(OrdineBean ordine) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        String insertSQL = "INSERT INTO " + TABLE_NAME +
                           " (importo, data, rif_id_spedizione, rif_id_pagamento, rif_username_cliente) " +
                           "VALUES (?, ?, ?, ?, ?)";

        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setDouble(1, ordine.getImporto());
            preparedStatement.setTimestamp(2, new Timestamp(ordine.getData().getTime())); // Usa Timestamp
            preparedStatement.setInt(3, ordine.getRifIdSpedizione());
            preparedStatement.setInt(4, ordine.getRifIdPagamento());
            preparedStatement.setString(5, ordine.getRifUsernameCliente());

            preparedStatement.executeUpdate();
        } finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
            } finally {
                if (connection != null)
                    connection.close();
            }
        }
    }

 // Metodo per recuperare tutti gli ordini dal database
    public synchronized List<OrdineBean> doRetrieveAllOrders() throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        List<OrdineBean> ordini = new ArrayList<>();

        String selectSQL = "SELECT * FROM " + TABLE_NAME + " ORDER BY data DESC";

        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(selectSQL);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                OrdineBean ordine = new OrdineBean();
                ordine.setIdOrdine(rs.getInt("id_ordine"));
                ordine.setImporto(rs.getDouble("importo"));
                ordine.setData(rs.getTimestamp("data")); 
                ordine.setRifIdSpedizione(rs.getInt("rif_id_spedizione"));
                ordine.setRifIdPagamento(rs.getInt("rif_id_pagamento"));
                ordine.setRifUsernameCliente(rs.getString("rif_username_cliente"));

                ordini.add(ordine);
            }
        } finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
            } finally {
                if (connection != null)
                    connection.close();
            }
        }

        return ordini;
    }

    // Metodo per recuperare tutti gli ordini effettuati da un determinato cliente
    public synchronized List<OrdineBean> doRetrieveOrdersByUser(String usernameCliente) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        List<OrdineBean> ordini = new ArrayList<>();

        String selectSQL = "SELECT * FROM " + TABLE_NAME + 
                           " WHERE rif_username_cliente = ? ORDER BY data DESC";

        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, usernameCliente);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                OrdineBean ordine = new OrdineBean();
                ordine.setIdOrdine(rs.getInt("id_ordine"));
                ordine.setImporto(rs.getDouble("importo"));
                ordine.setData(rs.getTimestamp("data"));
                ordine.setRifIdSpedizione(rs.getInt("rif_id_spedizione"));
                ordine.setRifIdPagamento(rs.getInt("rif_id_pagamento"));
                ordine.setRifUsernameCliente(rs.getString("rif_username_cliente"));

                ordini.add(ordine);
            }
        } finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
            } finally {
                if (connection != null)
                    connection.close();
            }
        }

        return ordini;
    }

    // Metodo per recuperare tutti gli ordini effettuati in un anno specifico
    public synchronized List<OrdineBean> doRetrieveOrdersByYear(int anno) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        List<OrdineBean> ordini = new ArrayList<>();

        String selectSQL = "SELECT * FROM " + TABLE_NAME + 
                           " WHERE YEAR(data) = ? ORDER BY data DESC";

        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setInt(1, anno); 

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                OrdineBean ordine = new OrdineBean();
                ordine.setIdOrdine(rs.getInt("id_ordine"));
                ordine.setImporto(rs.getDouble("importo"));
                ordine.setData(rs.getTimestamp("data")); 
                ordine.setRifIdSpedizione(rs.getInt("rif_id_spedizione"));
                ordine.setRifIdPagamento(rs.getInt("rif_id_pagamento"));
                ordine.setRifUsernameCliente(rs.getString("rif_username_cliente"));

                ordini.add(ordine);
            }
        } finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
            } finally {
                if (connection != null)
                    connection.close();
            }
        }

        return ordini;
    }

    // Metodo per recuperare l'indirizzo di spedizione di un ordine
    public synchronized IndirizzoSpedizioneBean getIndirizzoByIdSpedizioneAndUsername(OrdineBean order, String usernameCliente) throws SQLException {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        if (usernameCliente == null) {
            throw new IllegalArgumentException("UsernameCliente cannot be null");
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        IndirizzoSpedizioneBean indirizzo = null;
        int idSpedizione = order.getRifIdSpedizione();

        String selectSQL = "SELECT i.* FROM " + TABLE_NAME2 + " i " +
                           "JOIN " + TABLE_NAME3 + " s ON i.id_indirizzo = s.id_indirizzo " +
                           "WHERE s.id_spedizione = ? AND i.username_cliente = ? ORDER BY s.data DESC";

        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(selectSQL);

            preparedStatement.setInt(1, idSpedizione);
            preparedStatement.setString(2, usernameCliente);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                indirizzo = new IndirizzoSpedizioneBean();
                indirizzo.setId_indirizzo(rs.getInt("id_indirizzo"));
                indirizzo.setUsername_cliente(rs.getString("username_cliente"));
                indirizzo.setNome_destinatario(rs.getString("nome_destinatario"));
                indirizzo.setCognome_destinatario(rs.getString("cognome_destinatario"));
                indirizzo.setTelefono_destinatario(rs.getString("telefono_destinatario"));
                indirizzo.setProvincia(rs.getString("provincia"));
                indirizzo.setCitta(rs.getString("citta"));
                indirizzo.setVia(rs.getString("via"));
                indirizzo.setN_civico(rs.getInt("n_civico"));
                indirizzo.setCap(rs.getString("cap"));
            }
        } finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
            } finally {
                if (connection != null)
                    connection.close();
            }
        }

        return indirizzo;
    }

    // Metodo per recuperare gli ordini effettuati in un intervallo di date e, opzionalmente, da un determinato utente
    public synchronized List<OrdineBean> doRetrieveOrdersByDateRangeAndUser(Date dataInizio, Date dataFine, String usernameCliente) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        List<OrdineBean> ordini = new ArrayList<>();

        StringBuilder selectSQL = new StringBuilder("SELECT * FROM " + TABLE_NAME + 
                                                    " WHERE data BETWEEN ? AND ?");

        if (usernameCliente != null) {
            selectSQL.append(" AND rif_username_cliente = ?");
        }

        selectSQL.append(" ORDER BY data DESC");

        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(selectSQL.toString());

            preparedStatement.setTimestamp(1, new Timestamp(dataInizio.getTime())); 
            preparedStatement.setTimestamp(2, new Timestamp(dataFine.getTime()));   

            if (usernameCliente != null) {
                preparedStatement.setString(3, usernameCliente);
            }

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                OrdineBean ordine = new OrdineBean();
                ordine.setIdOrdine(rs.getInt("id_ordine"));
                ordine.setImporto(rs.getDouble("importo"));
                ordine.setData(rs.getTimestamp("data")); 
                ordine.setRifIdSpedizione(rs.getInt("rif_id_spedizione"));
                ordine.setRifIdPagamento(rs.getInt("rif_id_pagamento"));
                ordine.setRifUsernameCliente(rs.getString("rif_username_cliente"));

                ordini.add(ordine);
            }
        } finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
            } finally {
                if (connection != null)
                    connection.close();
            }
        }

        return ordini;
    }
    
 // Metodo per recuperare l'ultimo ordine effettuato da un utente specifico
    public synchronized OrdineBean doRetrieveLastOrderByUser(String usernameCliente) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        OrdineBean ordine = null;

        String selectSQL = "SELECT * FROM " + TABLE_NAME + 
                           " WHERE rif_username_cliente = ? " +
                           " ORDER BY data DESC LIMIT 1";

        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, usernameCliente);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                ordine = new OrdineBean();
                ordine.setIdOrdine(rs.getInt("id_ordine"));
                ordine.setImporto(rs.getDouble("importo"));
                ordine.setData(rs.getTimestamp("data")); 
                ordine.setRifIdSpedizione(rs.getInt("rif_id_spedizione"));
                ordine.setRifIdPagamento(rs.getInt("rif_id_pagamento"));
                ordine.setRifUsernameCliente(rs.getString("rif_username_cliente"));
            }
        } finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
            } finally {
                if (connection != null)
                    connection.close();
            }
        }

        return ordine;
    }
    
    public synchronized OrdineBean doRetrieveOrderById(int idOrdine) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        OrdineBean ordine = null;

        String selectSQL = "SELECT * FROM " + TABLE_NAME + " WHERE id_ordine = ?";

        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setInt(1, idOrdine);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                ordine = new OrdineBean();
                ordine.setIdOrdine(rs.getInt("id_ordine"));
                ordine.setImporto(rs.getDouble("importo"));
                ordine.setData(rs.getTimestamp("data")); 
                ordine.setRifIdSpedizione(rs.getInt("rif_id_spedizione"));
                ordine.setRifIdPagamento(rs.getInt("rif_id_pagamento"));
                ordine.setRifUsernameCliente(rs.getString("rif_username_cliente"));
            }
        } finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
            } finally {
                if (connection != null)
                    connection.close();
            }
        }

        return ordine;
    }


}
