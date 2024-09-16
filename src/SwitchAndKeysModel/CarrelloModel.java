package SwitchAndKeysModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class CarrelloModel {

    private static final Logger LOGGER = Logger.getLogger(CarrelloModel.class.getName());
    private static DataSource ds;

    static {
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            ds = (DataSource) envCtx.lookup("jdbc/SwitchAndKeys");
        } catch (NamingException e) {
            LOGGER.log(Level.SEVERE, "Error initializing DataSource", e);
        }
    }

    private static final String TABLE_NAME = "ProdottiCarrello";
    private static final String TABLE_NAME2 = "Prodotto";

    public synchronized void saveCart(CarrelloBean cart, UtenteBean user) throws SQLException {
        Connection connection = null;
        PreparedStatement psSelectProduct = null;
        PreparedStatement psUpdateProduct = null;
        PreparedStatement psInsertProduct = null;
        PreparedStatement psSelectIva = null;

        try {
            connection = ds.getConnection();
            connection.setAutoCommit(false); // Start transaction

            // Itera su ogni prodotto nel carrello
            for (ProdottoBean prodotto : cart.getProdotti()) {

                // Recupera l'IVA dalla tabella Prodotto
                String selectIvaSQL = "SELECT iva FROM " + TABLE_NAME2 + " WHERE id_prodotto = ?";
                psSelectIva = connection.prepareStatement(selectIvaSQL);
                psSelectIva.setInt(1, prodotto.getIdProdotto());
                ResultSet rsIva = psSelectIva.executeQuery();
                double iva = 0;
                if (rsIva.next()) {
                    iva = rsIva.getDouble("iva");
                }

                // Controlla se il prodotto è già nel carrello
                String selectProductSQL = "SELECT quantita FROM " + TABLE_NAME + " WHERE rif_username_cliente = ? AND rif_id_prodotto = ?";
                psSelectProduct = connection.prepareStatement(selectProductSQL);
                psSelectProduct.setString(1, user.getEmail());
                psSelectProduct.setInt(2, prodotto.getIdProdotto());
                ResultSet rs = psSelectProduct.executeQuery();

                if (rs.next()) {
                    // Il prodotto esiste già, aggiorna la quantità
                    int currentQuantity = rs.getInt("quantita");
                    String updateProductSQL = "UPDATE " + TABLE_NAME + " SET quantita = ?, iva = ? WHERE rif_username_cliente = ? AND rif_id_prodotto = ?";
                    psUpdateProduct = connection.prepareStatement(updateProductSQL);
                    psUpdateProduct.setInt(1, currentQuantity + prodotto.getQuantita());
                    psUpdateProduct.setDouble(2, iva);
                    psUpdateProduct.setString(3, user.getEmail());
                    psUpdateProduct.setInt(4, prodotto.getIdProdotto());
                    psUpdateProduct.executeUpdate();
                    LOGGER.log(Level.INFO, "Updated quantity of product ID {0} for user {1}", new Object[]{prodotto.getIdProdotto(), user.getEmail()});
                } else {
                    // Il prodotto non esiste, inserisci un nuovo record
                    String insertProductSQL = "INSERT INTO " + TABLE_NAME + " (rif_id_carrello, rif_username_cliente, rif_id_prodotto, quantita, iva) VALUES (?, ?, ?, ?, ?)";
                    psInsertProduct = connection.prepareStatement(insertProductSQL);
                    psInsertProduct.setInt(1, 1); // Assumi che rif_id_carrello sia costante o debba essere generato
                    psInsertProduct.setString(2, user.getEmail());
                    psInsertProduct.setInt(3, prodotto.getIdProdotto());
                    psInsertProduct.setInt(4, prodotto.getQuantita());
                    psInsertProduct.setDouble(5, iva);
                    psInsertProduct.executeUpdate();
                    LOGGER.log(Level.INFO, "Inserted product ID {0} for user {1}", new Object[]{prodotto.getIdProdotto(), user.getEmail()});
                }

                // Chiudi il ResultSet di IVA per ogni iterazione
                rsIva.close();
            }

            connection.commit();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error saving cart", e);
            if (connection != null) {
                try {
                    connection.rollback(); 
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Error during rollback", ex);
                }
            }
            throw e;
        } finally {
            closeResources(psSelectProduct, psUpdateProduct, psInsertProduct, psSelectIva, connection);
        }
    }


    public synchronized CarrelloBean getCart(UtenteBean user) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        Collection<ProdottoBean> prodotti = new LinkedList<>();
        CarrelloBean cart = new CarrelloBean();

        try {
            connection = ds.getConnection();
            String selectCartSQL = "SELECT p.id_prodotto, p.nome, p.prezzo, pc.quantita, pc.iva " +
                                   "FROM " + TABLE_NAME + " pc " +
                                   "JOIN " + TABLE_NAME2 + " p ON pc.rif_id_prodotto = p.id_prodotto " +
                                   "WHERE pc.rif_username_cliente = ?";
            ps = connection.prepareStatement(selectCartSQL);
            ps.setString(1, user.getEmail());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ProdottoBean prodotto = new ProdottoBean();
                prodotto.setIdProdotto(rs.getInt("id_prodotto"));
                prodotto.setNome(rs.getString("nome"));
                prodotto.setPrezzo(rs.getDouble("prezzo"));
                prodotto.setQuantita(rs.getInt("quantita"));
                prodotto.setIva(rs.getDouble("iva"));
                prodotti.add(prodotto);
                LOGGER.log(Level.INFO, "Added product ID {0} to cart for user {1}", new Object[]{prodotto.getIdProdotto(), user.getEmail()});
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving cart", e);
            throw e;
        } finally {
            closeResources(ps, null, null, null, connection);
        }

        cart.setCarrello(prodotti);

        return cart;
    }
    
    public synchronized void emptyCart(UtenteBean user) throws SQLException {
        Connection connection = null;
        PreparedStatement psDelete = null;

        try {
            connection = ds.getConnection();
            String deleteSQL = "DELETE FROM " + TABLE_NAME + " WHERE rif_username_cliente = ?";
            psDelete = connection.prepareStatement(deleteSQL);
            psDelete.setString(1, user.getEmail());

            int rowsAffected = psDelete.executeUpdate();
            LOGGER.log(Level.INFO, "Deleted {0} products from the cart for user {1}", new Object[]{rowsAffected, user.getEmail()});

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error emptying the cart", e);
            throw e;
        } finally {
            if (psDelete != null) {
                try {
                    psDelete.close();
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Error closing PreparedStatement", e);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Error closing Connection", e);
                }
            }
        }
    }
    
    public synchronized void addProductToCart(int idProdotto, UtenteBean user, int change) throws SQLException {
        Connection connection = null;
        PreparedStatement psUpdate = null;
        PreparedStatement psInsert = null;
        PreparedStatement psSelect = null;
        PreparedStatement psSelectIva = null;

        try {
            connection = ds.getConnection();
            connection.setAutoCommit(false); // Start transaction

            // Recupera l'IVA dalla tabella Prodotto
            String selectIvaSQL = "SELECT iva FROM " + TABLE_NAME2 + " WHERE id_prodotto = ?";
            psSelectIva = connection.prepareStatement(selectIvaSQL);
            psSelectIva.setInt(1, idProdotto);
            ResultSet rsIva = psSelectIva.executeQuery();
            double iva = 0;
            if (rsIva.next()) {
                iva = rsIva.getDouble("iva");
            }

            // Check if the product already exists in the cart
            String selectSQL = "SELECT quantita FROM " + TABLE_NAME + " WHERE rif_id_prodotto = ? AND rif_username_cliente = ?";
            psSelect = connection.prepareStatement(selectSQL);
            psSelect.setInt(1, idProdotto);
            psSelect.setString(2, user.getEmail());
            ResultSet rs = psSelect.executeQuery();

            if (rs.next()) {
                // Update quantity
                int currentQuantity = rs.getInt("quantita") + change;
                if (currentQuantity <= 0) {
                    
                    String deleteSQL = "DELETE FROM " + TABLE_NAME + " WHERE rif_id_prodotto = ? AND rif_username_cliente = ?";
                    psUpdate = connection.prepareStatement(deleteSQL);
                    psUpdate.setInt(1, idProdotto);
                    psUpdate.setString(2, user.getEmail());
                    psUpdate.executeUpdate();
                    LOGGER.log(Level.INFO, "Removed product ID {0} from cart for user {1}", new Object[]{idProdotto, user.getEmail()});
                } else {
                    String updateSQL = "UPDATE " + TABLE_NAME + " SET quantita = ?, iva = ? WHERE rif_id_prodotto = ? AND rif_username_cliente = ?";
                    psUpdate = connection.prepareStatement(updateSQL);
                    psUpdate.setInt(1, currentQuantity);
                    psUpdate.setDouble(2, iva);
                    psUpdate.setInt(3, idProdotto);
                    psUpdate.setString(4, user.getEmail());
                    psUpdate.executeUpdate();
                    LOGGER.log(Level.INFO, "Updated quantity of product ID {0} for user {1}", new Object[]{idProdotto, user.getEmail()});
                }
            } else {
                
                String insertSQL = "INSERT INTO " + TABLE_NAME + " (rif_id_carrello, rif_username_cliente, rif_id_prodotto, quantita, iva) VALUES (?, ?, ?, ?, ?)";
                psInsert = connection.prepareStatement(insertSQL);
                psInsert.setInt(1, 1); 
                psInsert.setString(2, user.getEmail());
                psInsert.setInt(3, idProdotto);
                psInsert.setInt(4, change);
                psInsert.setDouble(5, iva);
                psInsert.executeUpdate();
                LOGGER.log(Level.INFO, "Inserted product ID {0} for user {1}", new Object[]{idProdotto, user.getEmail()});
            }

            connection.commit(); // Commit transaction

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding product to cart", e);
            if (connection != null) {
                try {
                    connection.rollback(); // Rollback transaction on error
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Error during rollback", ex);
                }
            }
            throw e;
        } finally {
            closeResources(psUpdate, psInsert, psSelect, psSelectIva, connection);
        }
    }

    public synchronized void removeProductFromCart(int idProdotto, UtenteBean user, int quantityToRemove) throws SQLException {
        Connection connection = null;
        PreparedStatement psUpdate = null;
        PreparedStatement psDelete = null;
        PreparedStatement psSelect = null;

        try {
            connection = ds.getConnection();
            connection.setAutoCommit(false); // Start transaction

            // Check if the product exists in the cart
            String selectSQL = "SELECT quantita FROM " + TABLE_NAME + " WHERE rif_id_prodotto = ? AND rif_username_cliente = ?";
            psSelect = connection.prepareStatement(selectSQL);
            psSelect.setInt(1, idProdotto);
            psSelect.setString(2, user.getEmail());
            ResultSet rs = psSelect.executeQuery();

            if (rs.next()) {
                int currentQuantity = rs.getInt("quantita") - quantityToRemove;

                if (currentQuantity <= 0) {
                    
                    String deleteSQL = "DELETE FROM " + TABLE_NAME + " WHERE rif_id_prodotto = ? AND rif_username_cliente = ?";
                    psDelete = connection.prepareStatement(deleteSQL);
                    psDelete.setInt(1, idProdotto);
                    psDelete.setString(2, user.getEmail());
                    psDelete.executeUpdate();
                    LOGGER.log(Level.INFO, "Removed product ID {0} from cart for user {1}", new Object[]{idProdotto, user.getEmail()});
                } else {
                    
                    String updateSQL = "UPDATE " + TABLE_NAME + " SET quantita = ? WHERE rif_id_prodotto = ? AND rif_username_cliente = ?";
                    psUpdate = connection.prepareStatement(updateSQL);
                    psUpdate.setInt(1, currentQuantity);
                    psUpdate.setInt(2, idProdotto);
                    psUpdate.setString(3, user.getEmail());
                    psUpdate.executeUpdate();
                    LOGGER.log(Level.INFO, "Updated quantity of product ID {0} for user {1}", new Object[]{idProdotto, user.getEmail()});
                }
            }

            connection.commit(); 

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error removing product from cart", e);
            if (connection != null) {
                try {
                    connection.rollback(); 
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Error during rollback", ex);
                }
            }
            throw e;
        } finally {
            closeResources(psUpdate, psDelete, psSelect, null, connection);
        }
    }
    
    public synchronized void removeProductCompletelyFromCart(int idProdotto, UtenteBean user) throws SQLException {
        Connection connection = null;
        PreparedStatement psDelete = null;

        try {
            connection = ds.getConnection();
            connection.setAutoCommit(false); 

            
            String deleteSQL = "DELETE FROM " + TABLE_NAME + " WHERE rif_id_prodotto = ? AND rif_username_cliente = ?";
            psDelete = connection.prepareStatement(deleteSQL);
            psDelete.setInt(1, idProdotto);
            psDelete.setString(2, user.getEmail());

            int rowsAffected = psDelete.executeUpdate();
            LOGGER.log(Level.INFO, "Completely removed product ID {0} from cart for user {1}", new Object[]{idProdotto, user.getEmail()});

            connection.commit(); 

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error completely removing product from cart", e);
            if (connection != null) {
                try {
                    connection.rollback(); 
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Error during rollback", ex);
                }
            }
            throw e;
        } finally {
            if (psDelete != null) {
                try {
                    psDelete.close();
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Error closing PreparedStatement", e);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Error closing Connection", e);
                }
            }
        }
    }


    public synchronized int getQuantitaProdotto(int idProdotto, UtenteBean user) throws SQLException {
        Connection connection = null;
        PreparedStatement psSelect = null;
        int quantita = 0;

        try {
            connection = ds.getConnection();

           
            String selectSQL = "SELECT quantita FROM " + TABLE_NAME + " WHERE rif_id_prodotto = ? AND rif_username_cliente = ?";
            psSelect = connection.prepareStatement(selectSQL);
            psSelect.setInt(1, idProdotto);
            psSelect.setString(2, user.getEmail());
            ResultSet rs = psSelect.executeQuery();

            
            if (rs.next()) {
                quantita = rs.getInt("quantita");
                LOGGER.log(Level.INFO, "Retrieved quantity {0} for product ID {1} and user {2}", 
                            new Object[]{quantita, idProdotto, user.getEmail()});
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving quantity of product", e);
            throw e;
        } finally {
            if (psSelect != null) {
                try {
                    psSelect.close();
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Error closing PreparedStatement", e);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Error closing Connection", e);
                }
            }
        }

        return quantita;
    }

    
    private synchronized void closeResources(PreparedStatement psUpdate, PreparedStatement psInsert, PreparedStatement psSelect, PreparedStatement psSelectIva, Connection connection) {
        if (psUpdate != null) {
            try {
                psUpdate.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Error closing PreparedStatement", e);
            }
        }
        if (psInsert != null) {
            try {
                psInsert.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Error closing PreparedStatement", e);
            }
        }
        if (psSelect != null) {
            try {
                psSelect.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Error closing PreparedStatement", e);
            }
        }
        if (psSelectIva != null) {
            try {
                psSelectIva.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Error closing PreparedStatement", e);
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Error closing Connection", e);
            }
        }
    }
}
