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

public class SpecificaModel {
	
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

    private static final String TABLE_NAME = "Specifica";

    
    public synchronized boolean doSave(SpecificaBean specifica) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        String insertSQL = "INSERT INTO " + TABLE_NAME + 
                " (rif_id_prodotto, nome_prodotto, IVA_Applicata, Prezzo_Unitario, Quantita_Ordine, rif_id_dettaglio) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setInt(1, specifica.getRifIdProdotto());
            preparedStatement.setString(2, specifica.getNome_prodotto());
            preparedStatement.setDouble(3, specifica.getIvaApplicata());
            preparedStatement.setDouble(4, specifica.getPrezzoUnitario());
            preparedStatement.setInt(5, specifica.getQuantita());
            preparedStatement.setInt(6, specifica.getRifIdDettaglio());

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

    
    public synchronized List<ProdottoBean> doRetrieveSpecifiedProducts(int rifIdDettaglio) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        List<ProdottoBean> products = new ArrayList<>();

        
        String selectSQL = "SELECT * FROM " + TABLE_NAME + " WHERE rif_id_dettaglio = ?";

        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setInt(1, rifIdDettaglio);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                ProdottoBean product = new ProdottoBean();

                
                product.setIdProdotto(rs.getInt("rif_id_prodotto"));             
                product.setNome(rs.getString("nome_prodotto"));
                product.setPrezzo(rs.getDouble("Prezzo_Unitario")); 
                product.setIva(rs.getDouble("IVA_Applicata")); 
                product.setQuantita(rs.getInt("Quantita_Ordine")); 

                products.add(product);
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

        return products;
    }

}
