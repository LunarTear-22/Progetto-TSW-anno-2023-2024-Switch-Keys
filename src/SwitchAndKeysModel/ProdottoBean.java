package SwitchAndKeysModel;

import java.io.Serializable;

public class ProdottoBean implements Serializable {
    private static final long serialVersionUID = 1L;

    // Proprietà del prodotto
    private int idProdotto = -1;
    private int rifIdCatalogo;
    private String nome = "";
    private double prezzo;       // Prezzo senza IVA
    private double iva;          // Percentuale IVA
    private int quantita;
    private String categoria = "";
    private String tipo = "";
    private String materiale;
    private String marca = "";
    private Boolean cablata;
    private Boolean wifi;
    private Boolean bluetooth;
    private String descrizione = "";
    private boolean disponibile;

    // Getter e Setter per idProdotto
    public int getIdProdotto() {
        return idProdotto;
    }

    public void setIdProdotto(int idProdotto) {
        this.idProdotto = idProdotto;
    }

    // Getter e Setter per rifIdCatalogo
    public int getRifIdCatalogo() {
        return rifIdCatalogo;
    }

    public void setRifIdCatalogo(int rifIdCatalogo) {
        this.rifIdCatalogo = rifIdCatalogo;
    }

    // Getter e Setter per nome
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    // Getter e Setter per prezzo
    public double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    // Formattazione del prezzo totale inclusivo di IVA
    public String getPrezzoFormatted() {
        double prezzoConIva = getPrezzoConIva();
        return String.format("%.2f", prezzoConIva);
    }

    // Formattazione del prezzo netto (se il prezzo memorizzato è comprensivo di IVA)
    public String getPrezzoNettoFormatted() {
        return String.format("%.2f", prezzo);
    }

    // Calcolo del prezzo totale inclusivo di IVA
    public double getPrezzoConIva() {
        return prezzo * (1 + iva / 100);
    }



    // Getter e Setter per iva
    public double getIva() {
        return iva;
    }
    
    public String getIvaFormatted() {
        return String.format("%.2f", iva);
    }
    
 // Calcolo dell'importo dell'IVA
    public double getImportoIva() {
        return prezzo * (iva / 100);
    }

    // Formattazione dell'importo dell'IVA
    public String getImportoIvaFormatted() {
        return String.format("%.2f", getImportoIva());
    }


    public void setIva(double iva) {
        this.iva = iva;
    }

    // Getter e Setter per quantita
    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    public void addQuantita(int quantita) {
        this.quantita += quantita;
    }

    // Getter e Setter per categoria
    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    // Getter e Setter per tipo
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    // Getter e Setter per materiale
    public String getMateriale() {
        return materiale;
    }

    public void setMateriale(String materiale) {
        this.materiale = materiale;
    }

    // Getter e Setter per marca
    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    // Getter e Setter per cablata
    public Boolean isCablata() {
        return cablata;
    }

    public void setCablata(Boolean cablata) {
        this.cablata = cablata;
    }

    // Getter e Setter per wifi
    public Boolean isWifi() {
        return wifi;
    }

    public void setWifi(Boolean wifi) {
        this.wifi = wifi;
    }

    // Getter e Setter per bluetooth
    public Boolean isBluetooth() {
        return bluetooth;
    }

    public void setBluetooth(Boolean bluetooth) {
        this.bluetooth = bluetooth;
    }

    // Getter e Setter per descrizione
    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    // Getter e Setter per disponibile
    public boolean isDisponibile() {
        return disponibile;
    }

    public void setDisponibile(boolean disponibile) {
        this.disponibile = disponibile;
    }

   
    @Override
    public String toString() {
        return String.format("%s (%d), Prezzo: %s, Quantità: %d. %s",
                nome, idProdotto, getPrezzoFormatted(), quantita, descrizione);
    }
}
