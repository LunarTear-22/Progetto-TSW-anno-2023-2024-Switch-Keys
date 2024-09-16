package SwitchAndKeysModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DettaglioOrdineModel {
	
	private static DataSource ds;

    static {
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            ds = (DataSource) envCtx.lookup("jdbc/SwitchAndKeys");
        } catch (NamingException e) {
        	System.out.println("Error: " + e.getMessage());
        }
    }

    private static final String TABLE_NAME = "Dettaglio_Ordine";

    
    public synchronized boolean doSave(DettaglioOrdineBean dettaglioOrdine) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        String insertSQL = "INSERT INTO " + TABLE_NAME + 
                " (Quantita_Totale, Importo_Totale, costi_spedizione, indirizzo_spedizione, metodo_pagamento, rif_id_ordine) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setInt(1, dettaglioOrdine.getQuantitaTotale());
            preparedStatement.setDouble(2, dettaglioOrdine.getImportoTotale());
            preparedStatement.setDouble(3, dettaglioOrdine.getCostiSpedizione());
            preparedStatement.setString(4, dettaglioOrdine.getIndirizzoSpedizione());
            preparedStatement.setString(5, dettaglioOrdine.getMetodoPagamento());
            preparedStatement.setInt(6, dettaglioOrdine.getRifIdOrdine());

            int result = preparedStatement.executeUpdate();

            return (result > 0);

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    
	public synchronized DettaglioOrdineBean doRetrieveDetailsForOrder(int rifIdOrdine) {
	    Connection connection = null;
	    PreparedStatement preparedStatement = null;
	
	    DettaglioOrdineBean dettaglioOrdine = null;
	
	    String selectSQL = "SELECT * FROM " + TABLE_NAME + " WHERE rif_id_ordine = ?";
	
	    try {
	        connection = ds.getConnection();
	        preparedStatement = connection.prepareStatement(selectSQL);
	        preparedStatement.setInt(1, rifIdOrdine);
	
	        ResultSet rs = preparedStatement.executeQuery();
	
	        if (rs.next()) {
	            dettaglioOrdine = new DettaglioOrdineBean();
	            dettaglioOrdine.setIdDettaglio(rs.getInt("ID_Dettaglio"));
	            dettaglioOrdine.setQuantitaTotale(rs.getInt("Quantita_Totale"));
	            dettaglioOrdine.setImportoTotale(rs.getDouble("Importo_Totale"));
	            dettaglioOrdine.setCostiSpedizione(rs.getDouble("costi_spedizione"));
	            dettaglioOrdine.setIndirizzoSpedizione(rs.getString("indirizzo_spedizione"));
	            dettaglioOrdine.setMetodoPagamento(rs.getString("metodo_pagamento"));
	            dettaglioOrdine.setRifIdOrdine(rs.getInt("rif_id_ordine"));
	        }
	
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (preparedStatement != null)
	                preparedStatement.close();
	            if (connection != null)
	                connection.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	
	    return dettaglioOrdine;
	}
    
}
