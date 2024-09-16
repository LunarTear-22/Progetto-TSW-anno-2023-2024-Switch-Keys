package SwitchAndKeysModel;

import java.util.ArrayList;
import java.util.Collection;

public class CarrelloBean {
  private Collection<ProdottoBean> prodotti = new ArrayList<>();
  
  public void setCarrello(Collection<ProdottoBean> prodotti) {
	  this.prodotti=prodotti;
  }
  
  public void AggiungiProdotto(ProdottoBean product, int quantita) {
	    // Inizializza la lista se � null
	    if (prodotti == null) {
	        prodotti = new ArrayList<>();
	    }
	    
	    // Flag per tenere traccia se il prodotto esiste gi� nella lista
	    boolean prodottoEsistente = false;

	    // Itera attraverso la lista per verificare se il prodotto esiste gi�
	    for (ProdottoBean prod : prodotti) {
	        if (prod.getIdProdotto() == product.getIdProdotto()) {
	            // Se il prodotto esiste gi�, aggiorna la quantit�
	            prod.addQuantita(quantita);
	            prodottoEsistente = true;
	            break; // Esce dal loop perch� il prodotto � stato trovato
	        }
	    }

	    // Se il prodotto non esiste nella lista, aggiungilo
	    if (!prodottoEsistente) {
	        product.setQuantita(quantita); // Imposta la quantit� iniziale del nuovo prodotto
	        prodotti.add(product);
	    }
	}

  public boolean rimuoviProdottoCompletamente(int idProdotto) {
      // Itera attraverso la lista dei prodotti per trovare quello con l'ID specificato
      for (ProdottoBean prodotto : prodotti) {
          if (prodotto.getIdProdotto() == idProdotto) {
              // Rimuove il prodotto dalla lista
              prodotti.remove(prodotto);
              return true; // Indica che il prodotto � stato rimosso con successo
          }
      }
      return false; // Indica che il prodotto non � stato trovato e quindi non � stato rimosso
  }
  
  public void EliminaProdotto(ProdottoBean product, int quantita) {
	    for (ProdottoBean prod : prodotti) {
	        if (prod.getIdProdotto() == product.getIdProdotto()) {
	            // Sottrae la quantit� specificata dal prodotto nel carrello
	            prod.addQuantita(-quantita); 

	            // Se la quantit� del prodotto � zero o inferiore, rimuovilo dalla lista
	            if (prod.getQuantita() <= 0) {
	                prodotti.remove(prod);
	                System.out.println("Prodotto rimosso dal carrello: " + prod.getNome());
	            } else {
	                System.out.println("Quantit� aggiornata per il prodotto: " + prod.getNome() + ", Nuova quantit�: " + prod.getQuantita());
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
      return 0; // Se il prodotto non � presente nel carrello, restituisci 0
  }
  
  public int getQuantitaTotaleProdotti() {
	    int quantitaTotale = 0;

	    // Itera attraverso la lista di prodotti e somma le quantit�
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
          sb.append("Il carrello � vuoto.\n");
      } else {
          for (ProdottoBean prodotto : prodotti) {
              sb.append(prodotto.toString()); // Supponendo che ProdottoBean abbia un buon toString()
              sb.append(" - Quantit�: ").append(prodotto.getQuantita()).append("\n");
          }
      }
      
      sb.append("Prezzo totale: ").append(getPrezzoTotaleFormatted()).append("\n");
      
      return sb.toString();
  }

}
