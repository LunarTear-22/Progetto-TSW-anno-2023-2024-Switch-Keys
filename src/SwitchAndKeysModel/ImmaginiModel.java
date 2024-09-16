package SwitchAndKeysModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ImmaginiModel {

    private static DataSource ds;

    static {
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            ds = (DataSource) envCtx.lookup("jdbc/SwitchAndKeys");
        } catch (NamingException e) {
            System.out.println("Error:" + e.getMessage());
        }
    }

    private static final String TABLE_NAME = "immagine";

    public synchronized static byte[] load(int rif_id_prodotto) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        byte[] bt = null;

        try {
            connection = ds.getConnection();
            connection.setAutoCommit(false);

            String sql = "SELECT dati_immagine FROM " + ImmaginiModel.TABLE_NAME + " WHERE rif_id_prodotto = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, rif_id_prodotto);
            rs = stmt.executeQuery();

            if (rs.next()) {
                bt = rs.getBytes("dati_immagine");
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException);
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (SQLException sqlException) {
                System.out.println(sqlException);
            } finally {
                try {
                    if (stmt != null)
                        stmt.close();
                } catch (SQLException sqlException) {
                    System.out.println(sqlException);
                } finally {
                    if (connection != null)
                        connection.close();
                }
            }
        }

        return bt;
    }

    public synchronized static void updatePhoto(int rif_id_prodotto, String dati_immagine) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        FileInputStream fis = null;

        try {
            con = ds.getConnection();
            con.setAutoCommit(false);

            stmt = con.prepareStatement("UPDATE " + ImmaginiModel.TABLE_NAME + " SET dati_immagine = ? WHERE rif_id_prodotto = ?");
            File file = new File(dati_immagine);
            fis = new FileInputStream(file);

            stmt.setBinaryStream(1, fis, fis.available());
            stmt.setInt(2, rif_id_prodotto);

            System.out.println(stmt);

            stmt.executeUpdate();
            con.commit();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            try {
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
                System.out.println(e);
            } finally {
                try {
                    if (stmt != null)
                        stmt.close();
                } catch (SQLException sqlException) {
                    System.out.println(sqlException);
                } finally {
                    if (con != null)
                        con.close();
                }
            }
        }
    }

    public synchronized static void insertPhoto(int rif_id_prodotto, String dati_immagine) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        FileInputStream fis = null;

        try {
            con = ds.getConnection();
            con.setAutoCommit(false);

            stmt = con.prepareStatement("INSERT INTO " + ImmaginiModel.TABLE_NAME + " (rif_id_prodotto, dati_immagine) VALUES (?, ?)");
            File file = new File(dati_immagine);
            fis = new FileInputStream(file);

            stmt.setInt(1, rif_id_prodotto);
            stmt.setBinaryStream(2, fis, fis.available());

            System.out.println(stmt);

            stmt.executeUpdate();
            con.commit();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            try {
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
                System.out.println(e);
            } finally {
                try {
                    if (stmt != null)
                        stmt.close();
                } catch (SQLException sqlException) {
                    System.out.println(sqlException);
                } finally {
                    if (con != null)
                        con.close();
                }
            }
        }
    }
}
