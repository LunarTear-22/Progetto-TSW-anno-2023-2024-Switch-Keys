package SwitchAndKeysModel;

public class SpecificaBean{
    
    private int rifIdProdotto;
    private String nome_prodotto;
    private double ivaApplicata;
    private double prezzoUnitario;
    private int quantita;
    private int rifIdDettaglio;

    // Costruttore vuoto
    public SpecificaBean() {}

    // Costruttore parametrizzato
    public SpecificaBean(int rifIdProdotto, String nome_prodotto, double ivaApplicata, double prezzoUnitario, int quantita, int rifIdDettaglio) {
        this.rifIdProdotto = rifIdProdotto;
        this.nome_prodotto = nome_prodotto;
        this.ivaApplicata = ivaApplicata;
        this.prezzoUnitario = prezzoUnitario;
        this.quantita = quantita;
        this.rifIdDettaglio = rifIdDettaglio;
    }

    // Getter e setter per rifIdProdotto
    public int getRifIdProdotto() {
        return rifIdProdotto;
    }

    public void setRifIdProdotto(int rifIdProdotto) {
        this.rifIdProdotto = rifIdProdotto;
    }

    // Getter e setter per ivaApplicata
    public double getIvaApplicata() {
        return ivaApplicata;
    }

    public void setIvaApplicata(double ivaApplicata) {
        this.ivaApplicata = ivaApplicata;
    }

    // Getter e setter per prezzoUnitario
    public double getPrezzoUnitario() {
        return prezzoUnitario;
    }

    public void setPrezzoUnitario(double prezzoUnitario) {
        this.prezzoUnitario = prezzoUnitario;
    }

    // Getter e setter per quantita
    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    // Getter e setter per rifIdDettaglio
    public int getRifIdDettaglio() {
        return rifIdDettaglio;
    }

    public void setRifIdDettaglio(int rifIdDettaglio) {
        this.rifIdDettaglio = rifIdDettaglio;
    }

    // Sovrascrivi il metodo toString per una facile rappresentazione
    @Override
    public String toString() {
        return "SpecificaBean{" +
                "rifIdProdotto=" + rifIdProdotto +
                ", ivaApplicata=" + ivaApplicata +
                ", prezzoUnitario=" + prezzoUnitario +
                ", quantita=" + quantita +
                ", rifIdDettaglio=" + rifIdDettaglio +
                '}';
    }

	public String getNome_prodotto() {
		return nome_prodotto;
	}

	public void setNome_prodotto(String nome_prodotto) {
		this.nome_prodotto = nome_prodotto;
	}
}
