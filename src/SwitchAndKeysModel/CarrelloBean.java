package SwitchAndKeysModel;

import java.util.ArrayList;
import java.util.Collection;

public class CarrelloBean {
  private Collection<ProdottoBean> prodotti = new ArrayList<>();
  
  public void setCarrello(Collection<ProdottoBean> prodotti) {
	  this.prodotti=prodotti;
  }
  
  public void AggiungiProdotto(ProdottoBean product, int quantita) {
	    // Inizializza la lista se è null
	    if (prodotti == null) {
	        prodotti = new ArrayList<>();
	    }
	    
	    // Flag per tenere traccia se il prodotto esiste già nella lista
	    boolean prodottoEsistente = false;

	    // Itera attraverso la lista per verificare se il prodotto esiste già
	    for (ProdottoBean prod : prodotti) {
	        if (prod.getIdProdotto() == product.getIdProdotto()) {
	            // Se il prodotto esiste già, aggiorna la quantità
	            prod.addQuantita(quantita);
	            prodottoEsistente = true;
	            break; // Esce dal loop perché il prodotto è stato trovato
	        }
	    }

	    // Se il prodotto non esiste nella lista, aggiungilo
	    if (!prodottoEsistente) {
	        product.setQuantita(quantita); // Imposta la quantità iniziale del nuovo prodotto
	        prodotti.add(product);
	    }
	}

  public boolean rimuoviProdottoCompletamente(int idProdotto) {
      // Itera attraverso la lista dei prodotti per trovare quello con l'ID specificato
      for (ProdottoBean prodotto : prodotti) {
          if (prodotto.getIdProdotto() == idProdotto) {
              // Rimuove il prodotto dalla lista
              prodotti.remove(prodotto);
              return true; // Indica che il prodotto è stato rimosso con successo
          }
      }
      return false; // Indica che il prodotto non è stato trovato e quindi non è stato rimosso
  }
  
  public void EliminaProdotto(ProdottoBean product, int quantita) {
	    for (ProdottoBean prod : prodotti) {
	        if (prod.getIdProdotto() == product.getIdProdotto()) {
	            // Sottrae la quantità specificata dal prodotto nel carrello
	            prod.addQuantita(-quantita); 

	            // Se la quantità del prodotto è zero o inferiore, rimuovilo dalla lista
	            if (prod.getQuantita() <= 0) {
	                prodotti.remove(prod);
	                System.out.println("Prodotto rimosso dal carrello: " + prod.getNome());
	            } else {
	                System.out.println("Quantità aggiornata per il prodotto: " + prod.getNome() + ", Nuova quantità: " + prod.getQuantita());
	            }
	            break; // Esce dal ciclo una volta trovato e gestito il prodotto
	        }
	    }
	}


  
  
  
  public Collection<ProdottoBean> getProdotti() {
    return this.prodotti;
  }
  
  public int size() {
    return this.prodotti.size();
  }
  
  public double getPrezzoTotale() {
	  double tot=0;
	  for (ProdottoBean prod : this.prodotti) {
		  tot=tot+prod.getPrezzoConIva()*prod.getQuantita();
	  }
	  return tot;
  }
  public String getPrezzoTotaleFormatted() {
	    return String.format("%.2f", new Object[] { Double.valueOf(getPrezzoTotale()) });
  }
  
  public String getPrezzoTotaleFormatted(double prezzo) {
	    return String.format("%.2f", new Object[] { Double.valueOf(prezzo) });
  }
  
  public int getQuantitaCarrello(ProdottoBean prodotto) {
	  int quantitaTot=0;
	  return quantitaTot+=prodotto.getQuantita();
  }
  
  public int getQuantitaProdotto(int idProdotto) {
      for (ProdottoBean prodotto : prodotti) {
          if (prodotto.getIdProdotto() == idProdotto) {
              return prodotto.getQuantita();
          }
      }
      return 0; // Se il prodotto non è presente nel carrello, restituisci 0
  }
  
  public int getQuantitaTotaleProdotti() {
	    int quantitaTotale = 0;

	    // Itera attraverso la lista di prodotti e somma le quantità
	    for (ProdottoBean prodotto : prodotti) {
	        quantitaTotale += prodotto.getQuantita();
	    }

	    return quantitaTotale;
	}
  
  @Override
  public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("Carrello:\n");
      
      if (prodotti.isEmpty()) {
          sb.append("Il carrello è vuoto.\n");
      } else {
          for (ProdottoBean prodotto : prodotti) {
              sb.append(prodotto.toString()); // Supponendo che ProdottoBean abbia un buon toString()
              sb.append(" - Quantità: ").append(prodotto.getQuantita()).append("\n");
          }
      }
      
      sb.append("Prezzo totale: ").append(getPrezzoTotaleFormatted()).append("\n");
      
      return sb.toString();
  }

}
