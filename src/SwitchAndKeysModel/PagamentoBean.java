package SwitchAndKeysModel;

import java.sql.Timestamp;

public class PagamentoBean {

    private int idPagamento;
    private double importo;
    private Timestamp data;
    private int rifIdMetodo;
    private String rifUsernameCliente;

    // Costruttore vuoto
    public PagamentoBean() {}

    // Costruttore parametrizzato
    public PagamentoBean(int idPagamento, double importo, Timestamp data, int rifIdMetodo, String rifUsernameCliente) {
        this.idPagamento = idPagamento;
        this.importo = importo;
        this.data = data;
        this.rifIdMetodo = rifIdMetodo;
        this.rifUsernameCliente = rifUsernameCliente;
    }

    // Getter e setter per idPagamento
    public int getIdPagamento() {
        return idPagamento;
    }

    public void setIdPagamento(int idPagamento) {
        this.idPagamento = idPagamento;
    }

    // Getter e setter per importo
    public double getImporto() {
        return importo;
    }

    public void setImporto(double importo) {
        this.importo = importo;
    }

    // Getter e setter per data
    public Timestamp getData() {
        return data;
    }

    public void setData(Timestamp data) {
        this.data = data;
    }

    // Getter e setter per rifIdMetodo
    public int getRifIdMetodo() {
        return rifIdMetodo;
    }

    public void setRifIdMetodo(int rifIdMetodo) {
        this.rifIdMetodo = rifIdMetodo;
    }

    // Getter e setter per rifUsernameCliente
    public String getRifUsernameCliente() {
        return rifUsernameCliente;
    }

    public void setRifUsernameCliente(String rifUsernameCliente) {
        this.rifUsernameCliente = rifUsernameCliente;
    }

    // Sovrascrivi il metodo toString per una facile rappresentazione
    @Override
    public String toString() {
        return "PagamentoBean{" +
                "idPagamento=" + idPagamento +
                ", importo=" + importo +
                ", data=" + data +
                ", rifIdMetodo=" + rifIdMetodo +
                ", rifUsernameCliente='" + rifUsernameCliente + '\'' +
                '}';
    }
}
