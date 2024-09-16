package SwitchAndKeysModel;

import java.util.ArrayList;
import java.util.Collection;

public class UtenteBean {
	
	private String nome;
	private String cognome;
    private String password;
    private String email;
    private boolean isAdmin;
    private Collection<IndirizzoSpedizioneBean> Indirizzi = new ArrayList<>();
    private Collection<MetodoPagamentoBean> MetodiDiPagamento = new ArrayList<>();

    public UtenteBean() {
    	nome="";
    	cognome="";   
        password = "";
        email = "";
        isAdmin = false;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @Override
    public String toString() {
        return "Utente{" +
               "Nome= " + nome +
               ", cognome= " + cognome +
                ", email= " + email + 
                ", isAdmin= " + isAdmin +
                '}';
    }

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public Collection<IndirizzoSpedizioneBean> getIndirizzi() {
		return Indirizzi;
	}

	public void setIndirizzi(Collection<IndirizzoSpedizioneBean> indirizzi) {
		Indirizzi = indirizzi;
	}
	
	public IndirizzoSpedizioneBean getIndirizzo(int idIndirizzoCercato){
	    if (Indirizzi == null || Indirizzi.isEmpty()) {
	        return null; // Restituisce null se la lista è vuota o null
	    }
	    for (IndirizzoSpedizioneBean ind : Indirizzi) {
	        if (ind.getIdIndirizzo() == idIndirizzoCercato) {
	            return ind;
	        }
	    }
	    return null; // Restituisce null se non trova l'indirizzo con l'ID specificato
	}
	
	public boolean AddIndirizzo(IndirizzoSpedizioneBean indirizzo) {
		if(Indirizzi.add(indirizzo)) {
			return true;
		}
		return false;
	}


	public Collection<MetodoPagamentoBean> getMetodiDiPagamento() {
		return MetodiDiPagamento;
	}

	public void setMetodiDiPagamento(Collection<MetodoPagamentoBean> metodiDiPagamento) {
		MetodiDiPagamento = metodiDiPagamento;
	}
}
