package SwitchAndKeysModel;

public class DettaglioOrdineBean{
       
    private int idDettaglio;
    private int quantitaTotale;
    private double importoTotale;
    private double costiSpedizione;
    private String indirizzoSpedizione;
    private String metodoPagamento;
    private int rifIdOrdine;

    // Costruttore vuoto
    public DettaglioOrdineBean() {}

    // Costruttore parametrizzato
    public DettaglioOrdineBean(int idDettaglio, int quantitaTotale, double importoTotale, double costiSpedizione, 
                               String indirizzoSpedizione, String metodoPagamento, int rifIdOrdine) {
        this.idDettaglio = idDettaglio;
        this.quantitaTotale = quantitaTotale;
        this.importoTotale = importoTotale;
        this.costiSpedizione = costiSpedizione;
        this.indirizzoSpedizione = indirizzoSpedizione;
        this.metodoPagamento = metodoPagamento;
        this.rifIdOrdine = rifIdOrdine;
    }

    // Getter e setter per idDettaglio
    public int getIdDettaglio() {
        return idDettaglio;
    }

    public void setIdDettaglio(int idDettaglio) {
        this.idDettaglio = idDettaglio;
    }

    // Getter e setter per quantitaTotale
    public int getQuantitaTotale() {
        return quantitaTotale;
    }

    public void setQuantitaTotale(int quantitaTotale) {
        this.quantitaTotale = quantitaTotale;
    }

    // Getter e setter per importoTotale
    public double getImportoTotale() {
        return importoTotale;
    }
    
    public String getImportoTotaleFormatted() {
        return String.format("%.2f", importoTotale);
    }
    

    public void setImportoTotale(double importoTotale) {
        this.importoTotale = importoTotale;
    }

    // Getter e setter per costiSpedizione
    public double getCostiSpedizione() {
        return costiSpedizione;
    }
    
    public String getCostiSpedizioneFormatted() {
        return String.format("%.2f", costiSpedizione);
    }

    public void setCostiSpedizione(double costiSpedizione) {
        this.costiSpedizione = costiSpedizione;
    }

    // Getter e setter per indirizzoSpedizione
    public String getIndirizzoSpedizione() {
        return indirizzoSpedizione;
    }

    public void setIndirizzoSpedizione(String indirizzoSpedizione) {
        this.indirizzoSpedizione = indirizzoSpedizione;
    }

    // Getter e setter per metodoPagamento
    public String getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(String metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    // Getter e setter per rifIdOrdine
    public int getRifIdOrdine() {
        return rifIdOrdine;
    }

    public void setRifIdOrdine(int rifIdOrdine) {
        this.rifIdOrdine = rifIdOrdine;
    }

    // Sovrascrivi il metodo toString per una facile rappresentazione
    @Override
    public String toString() {
        return "DettaglioOrdineBean{" +
                "idDettaglio=" + idDettaglio +
                ", quantitaTotale=" + quantitaTotale +
                ", importoTotale=" + importoTotale +
                ", costiSpedizione=" + costiSpedizione +
                ", indirizzoSpedizione='" + indirizzoSpedizione + '\'' +
                ", metodoPagamento='" + metodoPagamento + '\'' +
                ", rifIdOrdine=" + rifIdOrdine +
                '}';
    }
}
