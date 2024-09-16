package SwitchAndKeysModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ProdottoModelDS implements ProdottoModel {

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

    private static final String TABLE_NAME = "prodotto";
    private static final String TABLE_NAME2 = "immagine";

    @Override
    public synchronized boolean doSave(ProdottoBean product, String imagePath) throws SQLException {
        String insertProductSQL = "INSERT INTO " + TABLE_NAME
                + " (id_prodotto, rif_id_catalogo, nome, prezzo, iva, quantita, categoria, tipo, materiale, marca, cablata, wifi, bluetooth, descrizione, disponibile) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String insertImageSQL = "INSERT INTO " + TABLE_NAME2 + " (rif_id_prodotto, dati_immagine) VALUES (?, ?)";
        
        System.out.print(insertProductSQL);

        
        
        try (Connection connection = ds.getConnection();
             PreparedStatement productStmt = connection.prepareStatement(insertProductSQL);
             PreparedStatement imageStmt = connection.prepareStatement(insertImageSQL)) {

            connection.setAutoCommit(false);

            productStmt.setInt(1, product.getIdProdotto());
            productStmt.setInt(2, product.getRifIdCatalogo());
            productStmt.setString(3, product.getNome());
            productStmt.setDouble(4, product.getPrezzo());
            productStmt.setDouble(5, product.getIva());
            productStmt.setInt(6, product.getQuantita());
            productStmt.setString(7, product.getCategoria());
            productStmt.setString(8, product.getTipo());
            productStmt.setString(9, product.getMateriale());
            productStmt.setString(10, product.getMarca());
            productStmt.setObject(11, product.isCablata(), java.sql.Types.BOOLEAN);
            productStmt.setObject(12, product.isWifi(), java.sql.Types.BOOLEAN);
            productStmt.setObject(13, product.isBluetooth(), java.sql.Types.BOOLEAN);
            productStmt.setString(14, product.getDescrizione());
            productStmt.setBoolean(15, product.isDisponibile());

            int productResult = productStmt.executeUpdate();
            System.out.print(productResult);
            if (productResult > 0) {
                
            	 if (imagePath != null && !imagePath.isEmpty()) {
                    File file = new File(imagePath);
                    try (FileInputStream fis = new FileInputStream(file)) {
                        imageStmt.setInt(1, product.getIdProdotto());
                        imageStmt.setBinaryStream(2, fis, (int) file.length());
                        imageStmt.executeUpdate();
                    } catch (FileNotFoundException e) {
                        throw new SQLException("File not found: " + imagePath, e);
                    } catch (IOException e) {
                        throw new SQLException("Error reading file: " + imagePath, e);
                    }
                    
            	 }else {                    	
                        imageStmt.setInt(1, product.getIdProdotto());
                        imageStmt.setBinaryStream(2, null, 0);
                        imageStmt.executeUpdate();
                 }
                
                connection.commit();
                return true;
            } else {
                connection.rollback();
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException("Error while saving product", e);
        }
    }

    @Override
    public synchronized boolean doModify(ProdottoBean newProduct, int idProdotto, String imagePath) throws SQLException {
        String updateProductSQL = "UPDATE " + TABLE_NAME + " SET "
                + "nome = ?, prezzo = ?, iva = ?, quantita = ?, categoria = ?, tipo = ?, materiale = ?, marca = ?, "
                + "cablata = ?, wifi = ?, bluetooth = ?, descrizione = ?, disponibile = ? WHERE id_prodotto = ?";
        String updateImageSQL = "UPDATE " + TABLE_NAME2 + " SET dati_immagine = ? WHERE rif_id_prodotto = ?";

        try (Connection connection = ds.getConnection();
             PreparedStatement productStmt = connection.prepareStatement(updateProductSQL);
             PreparedStatement imageStmt = connection.prepareStatement(updateImageSQL)) {

            connection.setAutoCommit(false);

            productStmt.setString(1, newProduct.getNome());
            productStmt.setDouble(2, newProduct.getPrezzo());
            productStmt.setDouble(3, newProduct.getIva());
            productStmt.setInt(4, newProduct.getQuantita());
            productStmt.setString(5, newProduct.getCategoria());
            productStmt.setString(6, newProduct.getTipo());
            productStmt.setString(7, newProduct.getMateriale());
            productStmt.setString(8, newProduct.getMarca());
            productStmt.setObject(9, newProduct.isCablata(), java.sql.Types.BOOLEAN);
            productStmt.setObject(10, newProduct.isWifi(), java.sql.Types.BOOLEAN);
            productStmt.setObject(11, newProduct.isBluetooth(), java.sql.Types.BOOLEAN);
            productStmt.setString(12, newProduct.getDescrizione());
            productStmt.setBoolean(13, newProduct.isDisponibile());
            productStmt.setInt(14, idProdotto);

            productStmt.executeUpdate();

            if (imagePath != null && !imagePath.isEmpty()) {
                File file = new File(imagePath);
                try (FileInputStream fis = new FileInputStream(file)) {
                    imageStmt.setBinaryStream(1, fis, (int) file.length());
                    imageStmt.setInt(2, idProdotto);
                    imageStmt.executeUpdate();
                } catch (FileNotFoundException e) {
                    throw new SQLException("File not found: " + imagePath, e);
                } catch (IOException e) {
                    throw new SQLException("Error reading file: " + imagePath, e);
                }
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            throw new SQLException("Error while modifying product", e);
        }
    }

    @Override
    public synchronized ProdottoBean doRetrieveByKey(int idProdotto) throws SQLException {
        String selectSQL = "SELECT * FROM " + TABLE_NAME + " WHERE id_prodotto = ?";
        ProdottoBean bean = new ProdottoBean();

        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setInt(1, idProdotto);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
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
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error while retrieving product by key", e);
        }
        return bean;
    }

    public Collection<ProdottoBean> searchProducts(String query) {
        Collection<ProdottoBean> prodotti = new LinkedList<>();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE nome LIKE ?";

        try (Connection conn = ds.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + query + "%");
            ResultSet rs = ps.executeQuery();

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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return prodotti;
    }

    @Override
    public synchronized boolean doDelete(int idProdotto) throws SQLException {
        String deleteProductSQL = "DELETE FROM " + TABLE_NAME + " WHERE id_prodotto = ?";
        String deleteImageSQL = "DELETE FROM " + TABLE_NAME2 + " WHERE rif_id_prodotto = ?";
        
        System.out.println("id: "+idProdotto);
        
        try (Connection connection = ds.getConnection();
             PreparedStatement deleteProductStmt = connection.prepareStatement(deleteProductSQL);
             PreparedStatement deleteImageStmt = connection.prepareStatement(deleteImageSQL)) {

            connection.setAutoCommit(false);

            deleteProductStmt.setInt(1, idProdotto);
            deleteProductStmt.executeUpdate();

            deleteImageStmt.setInt(1, idProdotto);
            deleteImageStmt.executeUpdate();

            connection.commit();
            return true;
        } catch (SQLException e) {
            throw new SQLException("Error while deleting product", e);
        }
    }

    public Collection<ProdottoBean> doRetrieveByFilters(String[] categories, String[] sizes, int prezzoMax) {
        Collection<ProdottoBean> prodotti = new LinkedList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM " + TABLE_NAME + " WHERE 1=1");
        int paramIndex = 1;

       
        if (categories != null && categories.length > 0) {
            String placeholders = String.join(",", Collections.nCopies(categories.length, "?"));
            query.append(" AND categoria IN (").append(placeholders).append(")");
        }

        if (sizes != null && sizes.length > 0) {
            String placeholders = String.join(",", Collections.nCopies(sizes.length, "?"));
            query.append(" AND tipo IN (").append(placeholders).append(")");
        }

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(query.toString())) {

            if (categories != null && categories.length > 0) {
                for (String category : categories) {
                    ps.setString(paramIndex++, category);
                }
            }

            if (sizes != null && sizes.length > 0) {
                for (String size : sizes) {
                    ps.setString(paramIndex++, size);
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    double prezzo = rs.getDouble("prezzo");
                    double iva = rs.getDouble("iva");
                    double prezzoConIva = prezzo + (prezzo * iva / 100); // Calcola il prezzo con IVA

                    // Verifica che il prezzo con IVA non superi il prezzo massimo
                    if (prezzoConIva <= prezzoMax) {
                        ProdottoBean bean = new ProdottoBean();
                        bean.setIdProdotto(rs.getInt("id_prodotto"));
                        bean.setRifIdCatalogo(rs.getInt("rif_id_catalogo"));
                        bean.setNome(rs.getString("nome"));
                        bean.setPrezzo(prezzo); // Imposta il prezzo senza IVA
                        bean.setIva(iva);       // Imposta l'IVA
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
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prodotti;
    }


    @Override
    public synchronized Collection<ProdottoBean> doRetrieveAll(String order) throws SQLException {
        String selectSQL = "SELECT * FROM " + TABLE_NAME;
        if (order != null && !order.isEmpty()) {
            selectSQL += " ORDER BY " + order;
        }

        Collection<ProdottoBean> products = new LinkedList<>();

        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
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
                products.add(bean);
            }
        } catch (SQLException e) {
            throw new SQLException("Error while retrieving all products", e);
        }
        return products;
    }
    
    @Override
    public synchronized boolean aggiornaQuantita(int idProdotto, int quantitaAcquistata) throws SQLException {
        
        String updateQuantitySQL = "UPDATE " + TABLE_NAME + " SET quantita = quantita - ?, disponibile = CASE WHEN quantita - ? <= 0 THEN false ELSE disponibile END WHERE id_prodotto = ?";

        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(updateQuantitySQL)) {

            connection.setAutoCommit(false);

            
            ps.setInt(1, quantitaAcquistata);
            ps.setInt(2, quantitaAcquistata);
            ps.setInt(3, idProdotto);

            
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                connection.commit();
                return true;
            } else {
                connection.rollback();
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException("Error while updating product quantity", e);
        }
    }
	
}
    
