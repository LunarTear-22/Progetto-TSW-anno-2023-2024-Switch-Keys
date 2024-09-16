package SwitchAndKeysModel;

import java.sql.Timestamp;

public class OrdineBean {

    private int idOrdine;
    private double importo;
    private Timestamp data;
    private int rifIdSpedizione;
    private int rifIdPagamento;
    private String rifUsernameCliente;

    // Costruttore vuoto
    public OrdineBean() {}

    // Costruttore parametrizzato
    public OrdineBean(int idOrdine, double importo, Timestamp data, int rifIdSpedizione, int rifIdPagamento, String rifUsernameCliente) {
        this.idOrdine = idOrdine;
        this.importo = importo;
        this.data = data;
        this.rifIdSpedizione = rifIdSpedizione;
        this.rifIdPagamento = rifIdPagamento;
        this.rifUsernameCliente = rifUsernameCliente;
    }

    // Getter e setter per idOrdine
    public int getIdOrdine() {
        return idOrdine;
    }

    public void setIdOrdine(int idOrdine) {
        this.idOrdine = idOrdine;
    }

    // Getter e setter per importo
    public double getImporto() {
        return importo;
    }

    public void setImporto(double importo) {
        this.importo = importo;
    }
    
    public String getImportoFormatted() {
        return String.format("%.2f", importo);
    }

    // Getter e setter per data
    public Timestamp getData() {
        return data;
    }

    public void setData(Timestamp data) {
        this.data = data;
    }

    // Getter e setter per rifIdSpedizione
    public int getRifIdSpedizione() {
        return rifIdSpedizione;
    }

    public void setRifIdSpedizione(int rifIdSpedizione) {
        this.rifIdSpedizione = rifIdSpedizione;
    }

    // Getter e setter per rifIdPagamento
    public int getRifIdPagamento() {
        return rifIdPagamento;
    }

    public void setRifIdPagamento(int rifIdPagamento) {
        this.rifIdPagamento = rifIdPagamento;
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
        return "OrdineBean{" +
                "idOrdine=" + idOrdine +
                ", importo=" + importo +
                ", data=" + data +
                ", rifIdSpedizione=" + rifIdSpedizione +
                ", rifIdPagamento=" + rifIdPagamento +
                ", rifUsernameCliente='" + rifUsernameCliente + '\'' +
                '}';
    }
}
