package SwitchAndKeysModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class AreaUtenteModel {
	
    private static final Logger LOGGER = Logger.getLogger(AreaUtenteModel.class.getName());
    private static DataSource ds;
    private static final String TABLE_NAME = "Indirizzo_Spedizione";
    private static final String TABLE_NAME2 = "Metodo_di_Pagamento";
    
    static {
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            ds = (DataSource) envCtx.lookup("jdbc/SwitchAndKeys");
        } catch (NamingException e) {
            LOGGER.log(Level.SEVERE, "Error initializing DataSource", e);
        }
    }
    
    public synchronized Collection<IndirizzoSpedizioneBean> doRetrieveAllAddressByUser(String username_cliente) {
        Collection<IndirizzoSpedizioneBean> addresses = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE username_cliente = ?";
        
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
             
            preparedStatement.setString(1, username_cliente);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                IndirizzoSpedizioneBean address = new IndirizzoSpedizioneBean();
                address.setId_indirizzo(resultSet.getInt("id_indirizzo"));
                address.setUsername_cliente(resultSet.getString("username_cliente"));
                address.setNome_destinatario(resultSet.getString("nome_destinatario"));
                address.setCognome_destinatario(resultSet.getString("cognome_destinatario"));
                address.setTelefono_destinatario(resultSet.getString("telefono_destinatario"));
                address.setProvincia(resultSet.getString("provincia"));
                address.setCitta(resultSet.getString("citta"));
                address.setVia(resultSet.getString("via"));
                address.setN_civico(resultSet.getInt("n_civico"));
                address.setCap(resultSet.getString("cap"));
                addresses.add(address);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving addresses", e);
        }
        return addresses;
    }
    
    public synchronized boolean addIndirizzo(IndirizzoSpedizioneBean address) {
        String query = "INSERT INTO " + TABLE_NAME + 
        		"(username_cliente, nome_destinatario, cognome_destinatario, telefono_destinatario, provincia, citta, via, n_civico, cap) "
        		+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
             
            preparedStatement.setString(1, address.getUsername_cliente());
            preparedStatement.setString(2, address.getNome_destinatario());
            preparedStatement.setString(3, address.getCognome_destinatario());
            preparedStatement.setString(4, address.getTelefono_destinatario());
            preparedStatement.setString(5, address.getProvincia());
            preparedStatement.setString(6, address.getCitta());
            preparedStatement.setString(7, address.getVia());
            preparedStatement.setInt(8, address.getN_civico());
            preparedStatement.setString(9, address.getCap());
            int rows = preparedStatement.executeUpdate();
            
            return rows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding address", e);
            return false;
        }
    }
    
    public synchronized boolean removeIndirizzo(int id_indirizzo, String username_cliente) {
        String query = "DELETE FROM " + TABLE_NAME + " WHERE id_indirizzo = ? AND username_cliente = ?";
        
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
             
            preparedStatement.setInt(1, id_indirizzo);
            preparedStatement.setString(2, username_cliente);
            int rows = preparedStatement.executeUpdate();
            
            return rows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error removing address", e);
            return false;
        }
    }
    
    public synchronized boolean modifyIndirizzo(IndirizzoSpedizioneBean address) {
        String query = "UPDATE " + TABLE_NAME + " SET nome_destinatario = ?, cognome_destinatario = ?, telefono_destinatario = ?, "
        		+ "provincia = ?, citta = ?, via = ?, n_civico = ?, cap = ? WHERE id_indirizzo = ? AND username_cliente = ?";
        
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
             
            preparedStatement.setString(1, address.getNome_destinatario());
            preparedStatement.setString(2, address.getCognome_destinatario());
            preparedStatement.setString(3, address.getTelefono_destinatario());
            preparedStatement.setString(4, address.getProvincia());
            preparedStatement.setString(5, address.getCitta());
            preparedStatement.setString(6, address.getVia());
            preparedStatement.setInt(7, address.getN_civico());
            preparedStatement.setString(8, address.getCap());
            preparedStatement.setInt(9, address.getIdIndirizzo());
            preparedStatement.setString(10, address.getUsername_cliente());
            int rows = preparedStatement.executeUpdate();
            
            return rows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error modifying address", e);
            return false;
        }
    }
    
    public synchronized Collection<MetodoPagamentoBean> doRetrieveAllMethodByUser(String username_cliente) {
        Collection<MetodoPagamentoBean> methods = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME2 + " WHERE username_cliente = ?";
        
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
             
            preparedStatement.setString(1, username_cliente);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                MetodoPagamentoBean method = new MetodoPagamentoBean();
                method.setIdMetodo(resultSet.getInt("id_metodo"));
                method.setNumeroCarta(resultSet.getString("numero_carta"));
                method.setUsernameCliente(resultSet.getString("username_cliente"));
                method.setNomeCarta(resultSet.getString("nome_carta"));
                method.setCognomeCarta(resultSet.getString("cognome_carta"));
                method.setScadenza(resultSet.getDate("scadenza"));
                method.setCvv(resultSet.getString("cvv"));
                method.setTipo(resultSet.getString("tipo"));
                method.setMailPaypal(resultSet.getString("mail_paypal"));
                methods.add(method);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving payment methods", e);
        }
        return methods;
    }
    
    public synchronized boolean addMetodoPagamento(MetodoPagamentoBean metodo) {
     
        String query = "INSERT INTO " + TABLE_NAME2 + " (id_metodo, username_cliente, tipo";
        String values = "VALUES (?, ?, ?";

        
        if ("Carta di Credito".equals(metodo.getTipo())) {
            query += ", numero_carta, nome_carta, cognome_carta, scadenza, cvv";
            values += ", ?, ?, ?, ?, ?";
        } else if ("PayPal".equals(metodo.getTipo())) {
            query += ", mail_paypal";
            values += ", ?";
        }

    
        query += ") " + values + ")";

        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            int parameterIndex = 1;

            preparedStatement.setInt(parameterIndex++, metodo.getIdMetodo());
            preparedStatement.setString(parameterIndex++, metodo.getUsernameCliente());
            preparedStatement.setString(parameterIndex++, metodo.getTipo());

            if ("Carta di Credito".equals(metodo.getTipo())) {
                preparedStatement.setString(parameterIndex++, metodo.getNumeroCarta());
                preparedStatement.setString(parameterIndex++, metodo.getNomeCarta());
                preparedStatement.setString(parameterIndex++, metodo.getCognomeCarta());
                preparedStatement.setDate(parameterIndex++, new java.sql.Date(metodo.getScadenza().getTime()));
                preparedStatement.setString(parameterIndex++, metodo.getCvv());
            } else if ("PayPal".equals(metodo.getTipo())) {
                preparedStatement.setString(parameterIndex++, metodo.getMailPaypal());
            }

            int rows = preparedStatement.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding payment method", e);
            return false;
        }
    }
    
    public synchronized boolean removeMetodoPagamento(int id_metodo, String username_cliente) {
        String query = "DELETE FROM " + TABLE_NAME2 + " WHERE id_metodo = ? AND username_cliente = ?";
        
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
             
            preparedStatement.setInt(1, id_metodo);
            preparedStatement.setString(2, username_cliente);
            int rows = preparedStatement.executeUpdate();
            
            return rows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error removing payment method", e);
            return false;
        }
    }
    
    public synchronized boolean modifyMetodoPagamento(MetodoPagamentoBean metodo) {
        
        String query = "UPDATE " + TABLE_NAME2 + " SET ";
        
        
        if ("Carta di Credito".equals(metodo.getTipo())) {
            query += "numero_carta = ?, nome_carta = ?, cognome_carta = ?, scadenza = ?, cvv = ?, ";
        } else if ("PayPal".equals(metodo.getTipo())) {
            query += "mail_paypal = ?, ";
        }
        
        query += "tipo = ? WHERE id_metodo = ? AND username_cliente = ?";

        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            int parameterIndex = 1;

            if ("Carta di Credito".equals(metodo.getTipo())) {
                
                preparedStatement.setString(parameterIndex++, metodo.getNumeroCarta());
                preparedStatement.setString(parameterIndex++, metodo.getNomeCarta());
                preparedStatement.setString(parameterIndex++, metodo.getCognomeCarta());
                preparedStatement.setDate(parameterIndex++, new java.sql.Date(metodo.getScadenza().getTime()));
                preparedStatement.setString(parameterIndex++, metodo.getCvv());
            } else if ("PayPal".equals(metodo.getTipo())) {
            
                preparedStatement.setString(parameterIndex++, metodo.getMailPaypal());
            }
            
            
            preparedStatement.setString(parameterIndex++, metodo.getTipo());
            preparedStatement.setInt(parameterIndex++, metodo.getIdMetodo());
            preparedStatement.setString(parameterIndex, metodo.getUsernameCliente());
            
           
            int rows = preparedStatement.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error modifying payment method", e);
            return false;
        }
    }
    
    public synchronized IndirizzoSpedizioneBean doRetrieveAddressByKey(int id_indirizzo, String username_cliente) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE id_indirizzo = ? AND username_cliente = ?";
        IndirizzoSpedizioneBean address = null;

        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id_indirizzo);
            preparedStatement.setString(2, username_cliente);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                address = new IndirizzoSpedizioneBean();
                address.setId_indirizzo(resultSet.getInt("id_indirizzo"));
                address.setUsername_cliente(resultSet.getString("username_cliente"));
                address.setNome_destinatario(resultSet.getString("nome_destinatario"));
                address.setCognome_destinatario(resultSet.getString("cognome_destinatario"));
                address.setTelefono_destinatario(resultSet.getString("telefono_destinatario"));
                address.setProvincia(resultSet.getString("provincia"));
                address.setCitta(resultSet.getString("citta"));
                address.setVia(resultSet.getString("via"));
                address.setN_civico(resultSet.getInt("n_civico"));
                address.setCap(resultSet.getString("cap"));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving address by key", e);
        }
        return address;
    }

    public synchronized MetodoPagamentoBean doRetrieveMethodByKey(int id_metodo, String username_cliente) {
        String query = "SELECT * FROM " + TABLE_NAME2 + " WHERE id_metodo = ? AND username_cliente = ?";
        MetodoPagamentoBean method = null;

        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id_metodo);
            preparedStatement.setString(2, username_cliente);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                method = new MetodoPagamentoBean();
                method.setIdMetodo(resultSet.getInt("id_metodo"));
                method.setNumeroCarta(resultSet.getString("numero_carta"));
                method.setUsernameCliente(resultSet.getString("username_cliente"));
                method.setNomeCarta(resultSet.getString("nome_carta"));
                method.setCognomeCarta(resultSet.getString("cognome_carta"));
                method.setScadenza(resultSet.getDate("scadenza"));
                method.setCvv(resultSet.getString("cvv"));
                method.setTipo(resultSet.getString("tipo"));
                method.setMailPaypal(resultSet.getString("mail_paypal"));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving method by key", e);
        }
        return method;
    }
}
