package SwitchAndKeysModel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MetodoPagamentoBean {
    private int idMetodo;
    private String numeroCarta;
    private String usernameCliente;
    private String nomeCarta;
    private String cognomeCarta;
    private Date scadenza;
    private String cvv;
    private String tipo;
    private String mailPaypal;

   
    public int getIdMetodo() {
        return idMetodo;
    }

    public void setIdMetodo(int idMetodo) {
        this.idMetodo = idMetodo;
    }

    public String getNumeroCarta() {
        return numeroCarta;
    }

    public void setNumeroCarta(String numeroCarta) {
        this.numeroCarta = numeroCarta;
    }

    public String getUsernameCliente() {
        return usernameCliente;
    }

    public void setUsernameCliente(String usernameCliente) {
        this.usernameCliente = usernameCliente;
    }

    public String getNomeCarta() {
        return nomeCarta;
    }

    public void setNomeCarta(String nomeCarta) {
        this.nomeCarta = nomeCarta;
    }

    public String getCognomeCarta() {
        return cognomeCarta;
    }

    public void setCognomeCarta(String cognomeCarta) {
        this.cognomeCarta = cognomeCarta;
    }

    public Date getScadenza() {
        return scadenza;
    }

    public void setScadenza(Date scadenza) {
        this.scadenza = scadenza;
    }
    
    public String getScadenzaFormattata() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(scadenza);
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMailPaypal() {
        return mailPaypal;
    }

    public void setMailPaypal(String mailPaypal) {
        this.mailPaypal = mailPaypal;
    }
    
    public String getUltimiQuattroNumeri() {
        if (numeroCarta != null && numeroCarta.length() >= 4) {
            return numeroCarta.substring(numeroCarta.length() - 4);
        } else {
            return "";
        }
    }

    @Override
    public String toString() {
    	if(this.tipo.equals("PayPal")) {
	        return "MetodoDiPagamento{ Paypal Username='" + mailPaypal + "}";               
    	}else {
    		return "MetodoDiPagamento{numeroCarta= " + numeroCarta +
                ", usernameCliente= " + usernameCliente +
                ", Proprietario carta= " + nomeCarta + cognomeCarta +
                ", scadenza=" + scadenza +
                "}";
    	}
    }	
}

