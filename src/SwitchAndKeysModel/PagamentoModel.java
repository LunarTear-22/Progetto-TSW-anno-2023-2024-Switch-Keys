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

public class PagamentoModel {
    private static DataSource ds;
    private static final String TABLE_NAME = "Pagamento";


    static {
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            ds = (DataSource) envCtx.lookup("jdbc/SwitchAndKeys");
        } catch (NamingException e) {
            System.out.println("Error:" + e.getMessage());
        }
    }

    // Metodo per salvare un nuovo pagamento nel database
    public synchronized void doSavePayment(PagamentoBean pagamento) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        String insertSQL = "INSERT INTO " + TABLE_NAME + " (Importo, Data, rif_id_metodo, rif_username_cliente) VALUES (?, ?, ?, ?)";

        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setDouble(1, pagamento.getImporto());
            preparedStatement.setTimestamp(2, pagamento.getData()); 
            preparedStatement.setInt(3, pagamento.getRifIdMetodo());
            preparedStatement.setString(4, pagamento.getRifUsernameCliente());

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

    // Metodo per recuperare tutti i pagamenti dal database
    public synchronized List<PagamentoBean> doRetriveAllPayment() throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        List<PagamentoBean> pagamenti = new ArrayList<>();

        String selectSQL = "SELECT * FROM " + TABLE_NAME;

        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(selectSQL);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                PagamentoBean pagamento = new PagamentoBean();
                pagamento.setIdPagamento(rs.getInt("ID_Pagamento"));
                pagamento.setImporto(rs.getDouble("Importo"));
                pagamento.setData(rs.getTimestamp("Data")); 
                pagamento.setRifIdMetodo(rs.getInt("rif_id_metodo"));
                pagamento.setRifUsernameCliente(rs.getString("rif_username_cliente"));

                pagamenti.add(pagamento);
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

        return pagamenti;
    }

    // Metodo per recuperare tutti i pagamenti di un singolo cliente dal database
    public synchronized List<PagamentoBean> doRetrivePaymentByUser(String usernameCliente) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        List<PagamentoBean> pagamenti = new ArrayList<>();

        String selectSQL = "SELECT * FROM " + TABLE_NAME + " WHERE rif_username_cliente = ?";

        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, usernameCliente);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                PagamentoBean pagamento = new PagamentoBean();
                pagamento.setIdPagamento(rs.getInt("ID_Pagamento"));
                pagamento.setImporto(rs.getDouble("Importo"));
                pagamento.setData(rs.getTimestamp("Data")); 
                pagamento.setRifIdMetodo(rs.getInt("rif_id_metodo"));
                pagamento.setRifUsernameCliente(rs.getString("rif_username_cliente"));

                pagamenti.add(pagamento);
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

        return pagamenti;
    }

    // Metodo per recuperare l'ultimo pagamento effettuato da un utente
    public synchronized PagamentoBean doRetrieveLastPaymentByUser(String usernameCliente) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        PagamentoBean pagamento = null;

        String selectSQL = "SELECT * FROM " + TABLE_NAME + " WHERE rif_username_cliente = ? ORDER BY Data DESC LIMIT 1";

        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, usernameCliente);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                pagamento = new PagamentoBean();
                pagamento.setIdPagamento(rs.getInt("ID_Pagamento"));
                pagamento.setImporto(rs.getDouble("Importo"));
                pagamento.setData(rs.getTimestamp("Data")); 
                pagamento.setRifIdMetodo(rs.getInt("rif_id_metodo"));
                pagamento.setRifUsernameCliente(rs.getString("rif_username_cliente"));
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

        return pagamento;
    }
}
