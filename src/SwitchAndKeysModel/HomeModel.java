package SwitchAndKeysModel;

import java.sql.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class HomeModel {

    private static final Logger logger = Logger.getLogger(HomeModel.class.getName());
    private static DataSource ds;

    static {
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            ds = (DataSource) envCtx.lookup("jdbc/SwitchAndKeys");
            logger.info("DataSource initialized.");
        } catch (NamingException e) {
            logger.log(Level.SEVERE, "Error initializing DataSource", e);
        }
    }
    
    private static final String TABLE_NAME2 = "featured_products";
    private static final String TABLE_NAME3 = "prodotto";

    public synchronized boolean addProdottoFeatured(int prodottoId) throws SQLException {
        String insertSQL = "INSERT INTO " + TABLE_NAME2 + " (prodotto_id) VALUES (?)";
        logger.info("Executing addProdottoFeatured with prodottoId: " + prodottoId);
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setInt(1, prodottoId);
            int result = preparedStatement.executeUpdate();
            boolean success = result > 0;
            logger.info("addProdottoFeatured result: " + success);
            return success;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error while adding product to featured", e);
            throw new SQLException("Error while adding product to featured", e);
        }
    }

    // Rimuovi un prodotto dai prodotti in evidenza
    public synchronized boolean removeProdottoFeatured(int prodottoId) throws SQLException {
        String deleteSQL = "DELETE FROM " + TABLE_NAME2 + " WHERE prodotto_id = ?";
        logger.info("Executing removeProdottoFeatured with prodottoId: " + prodottoId);
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, prodottoId);
            int result = preparedStatement.executeUpdate();
            boolean success = result > 0;
            logger.info("removeProdottoFeatured result: " + success);
            return success;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error while removing product from featured", e);
            throw new SQLException("Error while removing product from featured", e);
        }
    }

    // Recupera tutti i prodotti in evidenza
    public synchronized Collection<ProdottoBean> getProdottiFeatured() throws SQLException {
        Collection<ProdottoBean> prodotti = new LinkedList<>();
        String query = "SELECT p.* FROM " + TABLE_NAME3 + " p INNER JOIN " + TABLE_NAME2 + " f ON p.id_prodotto = f.prodotto_id";
        logger.info("Executing getProdottiFeatured query.");
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                ProdottoBean bean = new ProdottoBean();
                bean.setIdProdotto(rs.getInt("id_prodotto"));
                bean.setRifIdCatalogo(rs.getInt("rif_id_catalogo"));
                bean.setNome(rs.getString("nome"));
                bean.setPrezzo(rs.getDouble("prezzo"));
                bean.setIva(rs.getDouble("iva"));
                bean.setQuantita(rs.getInt("quantita"));
                bean.setCategoria(rs.getString("categoria"));
                bean.setTipo(rs.getString("tipo"));
                bean.setMateriale(rs.getString("materiale"));
                bean.setMarca(rs.getString("marca"));
                bean.setCablata(rs.getBoolean("cablata"));
                bean.setWifi(rs.getBoolean("wifi"));
                bean.setBluetooth(rs.getBoolean("bluetooth"));
                bean.setDescrizione(rs.getString("descrizione"));
                bean.setDisponibile(rs.getBoolean("disponibile"));
                prodotti.add(bean);
            }
            logger.info("Retrieved " + prodotti.size() + " products for featured.");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error while retrieving featured products", e);
            throw new SQLException("Error while retrieving featured products", e);
        }
        return prodotti;
    }

    
    public synchronized void clearProdottiFeatured() throws SQLException {
        String deleteSQL = "DELETE FROM " + TABLE_NAME2;
        logger.info("Executing clearProdottiFeatured.");
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            int rowsDeleted = preparedStatement.executeUpdate();
            logger.info("Number of rows deleted from featured: " + rowsDeleted);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error while clearing featured products", e);
            throw new SQLException("Error while clearing featured products", e);
        }
    }
}
