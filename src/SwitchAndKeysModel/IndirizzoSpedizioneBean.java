package SwitchAndKeysModel;

public class IndirizzoSpedizioneBean {
	
	private int id_indirizzo;           
    private String username_cliente;       
    private String nome_destinatario;      
    private String cognome_destinatario;  
    private String telefono_destinatario;
    private String provincia;
    private int	n_civico;
    private String citta;                  
    private String via;                    
    private String cap;
    
	public void setId_indirizzo(int id_indirizzo) {
		this.id_indirizzo = id_indirizzo;
		
	}
    
	public int getIdIndirizzo() {
		return this.id_indirizzo;
	}


	public String getNome_destinatario() {
		return nome_destinatario;
	}


	public void setNome_destinatario(String nome_destinatario) {
		this.nome_destinatario = nome_destinatario;
	}


	public String getUsername_cliente() {
		return username_cliente;
	}


	public void setUsername_cliente(String username_cliente) {
		this.username_cliente = username_cliente;
	}


	public String getCognome_destinatario() {
		return cognome_destinatario;
	}


	public void setCognome_destinatario(String cognome_destinatario) {
		this.cognome_destinatario = cognome_destinatario;
	}


	public String getTelefono_destinatario() {
		return telefono_destinatario;
	}


	public void setTelefono_destinatario(String telefono_destinatario) {
		this.telefono_destinatario = telefono_destinatario;
	}

	public String getCitta() {
		return citta;
	}


	public void setCitta(String citta) {
		this.citta = citta;
	}


	public String getVia() {
		return via;
	}


	public void setVia(String via) {
		this.via = via;
	}


	public String getCap() {
		return cap;
	}


	public void setCap(String cap) {
		this.cap = cap;
	}
	
	public String getProvincia() {
		return provincia;
	}


	public void setProvincia(String provincia) {
		this.provincia = provincia.toUpperCase();
	}
	
	@Override
	 public String toString() {
        return "Indirizzo{" +
               "Nome Destinatario= " + nome_destinatario +
               ", Cognome Destinatario= " + cognome_destinatario +
                ", Contatto telefonico= " + telefono_destinatario + 
                ", Indirizzo= " + via + " " + n_civico + ", "+ citta + ", "+ cap + ", ("+ provincia.toUpperCase() +
                ")}";
    }


	public int getN_civico() {
		return n_civico;
	}


	public void setN_civico(int n_civico) {
		this.n_civico = n_civico;
	}

	public String getFullAddress(){
		return via + " " + n_civico + ", " + citta + ", " + cap + " (" + provincia + ")";
	}

	public String getEstremo_Spedizione(){
		return nome_destinatario + " " + cognome_destinatario + ", " + via + " " + n_civico + ", " + citta + ", " + cap + " (" + provincia + ")";
	}


}
