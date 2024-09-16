package SwitchAndKeysModel;

import java.sql.Timestamp;

public class SpedizioneBean {

    private int idSpedizione;
    private int idIndirizzo;
    private double costi;
    private Timestamp data;
    private String rifUsernameCliente; // Nuovo campo

    // Costruttore vuoto
    public SpedizioneBean() {}

    // Costruttore parametrizzato
    public SpedizioneBean(int idSpedizione, int idIndirizzo, double costi, Timestamp data, String rifUsernameCliente) {
        this.idSpedizione = idSpedizione;
        this.idIndirizzo = idIndirizzo;
        this.costi = costi;
        this.data = data;
        this.rifUsernameCliente = rifUsernameCliente; // Inizializzazione del nuovo campo
    }

    // Getter e setter per idSpedizione
    public int getIdSpedizione() {
        return idSpedizione;
    }

    public void setIdSpedizione(int idSpedizione) {
        this.idSpedizione = idSpedizione;
    }

    // Getter e setter per idIndirizzo
    public int getIdIndirizzo() {
        return idIndirizzo;
    }

    public void setIdIndirizzo(int idIndirizzo) {
        this.idIndirizzo = idIndirizzo;
    }

    // Getter e setter per costi
    public double getCosti() {
        return costi;
    }

    public void setCosti(double costi) {
        this.costi = costi;
    }

    // Getter e setter per data
    public Timestamp getData() {
        return data;
    }

    public void setData(Timestamp data) {
        this.data = data;
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
        return "SpedizioneBean{" +
                "idSpedizione=" + idSpedizione +
                ", idIndirizzo=" + idIndirizzo +
                ", costi=" + costi +
                ", data=" + data +
                ", rifUsernameCliente='" + rifUsernameCliente + '\'' +
                '}';
    }
}
