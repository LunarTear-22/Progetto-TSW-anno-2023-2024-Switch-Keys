package SwitchAndKeysControl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import SwitchAndKeysModel.AreaUtenteModel;
import SwitchAndKeysModel.CarrelloBean;
import SwitchAndKeysModel.CarrelloModel;
import SwitchAndKeysModel.IndirizzoSpedizioneBean;
import SwitchAndKeysModel.MetodoPagamentoBean;
import SwitchAndKeysModel.ProdottoBean;
import SwitchAndKeysModel.ProdottoModelDS;
import SwitchAndKeysModel.UtenteBean;

@WebServlet("/Carrello")
public class CarrelloControl extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private CarrelloModel carrelloModel;
    private ProdottoModelDS prodottoModel;
    private AreaUtenteModel AreaUtenteModel;
    
    public CarrelloControl() {
        super();
        carrelloModel = new CarrelloModel();
        prodottoModel = new ProdottoModelDS();
        AreaUtenteModel = new AreaUtenteModel();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
        CarrelloBean carrello = (CarrelloBean) request.getSession().getAttribute("carrello");
        
        if (carrello == null) {
            carrello = new CarrelloBean();
            request.getSession().setAttribute("carrello", carrello);
        }

        String action = request.getParameter("action");
        UtenteBean user = (UtenteBean) request.getSession().getAttribute("loggedInUser");
        request.getSession().setAttribute("NoMoreProduct", false);
        
        try {
            if (action != null) {
                switch (action) {
                case "addC":
                    // Aggiungi prodotto al carrello
                    int idProdottoAdd = Integer.parseInt(request.getParameter("id_prodotto"));
                    ProdottoBean prodottoAdd = prodottoModel.doRetrieveByKey(idProdottoAdd);

                    // Debug: Stampare l'ID del prodotto e le informazioni sul prodotto
                    System.out.println("DEBUG: Tentativo di aggiungere il prodotto al carrello. ID Prodotto: " + idProdottoAdd);
                    System.out.println("DEBUG: Dettagli Prodotto: " + prodottoAdd.toString());

                    int quantitaInCarrello;
                    int quantitaDisponibile = prodottoAdd.getQuantita();

                    // Recupera la mappa NoMoreProductMap dalla sessione o creane una nuova
                    Map<Integer, Boolean> noMoreProductMap = (Map<Integer, Boolean>) request.getSession().getAttribute("NoMoreProductMap");
                    if (noMoreProductMap == null) {
                        noMoreProductMap = new HashMap<>();
                    }

                    if (user != null) {
                        quantitaInCarrello = carrelloModel.getQuantitaProdotto(idProdottoAdd, user);

                        // Debug: Stampare le quantità
                        System.out.println("DEBUG: Utente loggato. Quantità in carrello: " + quantitaInCarrello + ", Quantità disponibile: " + quantitaDisponibile);

                        if (quantitaInCarrello + 1 <= quantitaDisponibile) {
                            carrelloModel.addProductToCart(idProdottoAdd, user, 1);
                            System.out.println("DEBUG: Prodotto aggiunto al carrello per l'utente loggato.");
                        } else {
                            System.out.println("DEBUG: Non è possibile aggiungere più prodotti al carrello per l'utente loggato.");
                        }
                    } else {
                        quantitaInCarrello = carrello.getQuantitaProdotto(idProdottoAdd);

                        // Debug: Stampare le quantità
                        System.out.println("DEBUG: Utente non loggato. Quantità in carrello: " + quantitaInCarrello + ", Quantità disponibile: " + quantitaDisponibile);

                        if (quantitaInCarrello + 1 <= quantitaDisponibile) {
                            carrello.AggiungiProdotto(prodottoAdd, 1);
                            request.getSession().setAttribute("carrello", carrello);
                            System.out.println("DEBUG: Prodotto aggiunto al carrello per l'utente non loggato.");
                        } else {
                            System.out.println("DEBUG: Non è possibile aggiungere più prodotti al carrello per l'utente non loggato.");
                        }
                    }

                    // Aggiorna la mappa NoMoreProductMap per l'ID del prodotto specifico
                    boolean noMoreProduct = (quantitaInCarrello + 1) >= quantitaDisponibile;
                    noMoreProductMap.put(idProdottoAdd, noMoreProduct);
                    
                    // Salva la mappa aggiornata nella sessione
                    request.getSession().setAttribute("NoMoreProductMap", noMoreProductMap);

                    response.sendRedirect(request.getContextPath() + "/Carrello.jsp");
                    return;


                case "deleteC":
                    
                    // Rimuovi prodotto dal carrello
                    int idProdottoDelete = Integer.parseInt(request.getParameter("id_prodotto"));
                    System.out.println("ID prodotto da eliminare: " + idProdottoDelete);

                    ProdottoBean prodottoDelete = prodottoModel.doRetrieveByKey(idProdottoDelete);
                    if (prodottoDelete != null) {
                        System.out.println("Prodotto recuperato: " + prodottoDelete.getNome());
                    } else {
                        System.out.println("Errore: prodotto non trovato con ID: " + idProdottoDelete);
                    }

                    int quantitaInCarrelloDel=0;
                    int quantitaDisponibileDel = prodottoDelete.getQuantita();
                    System.out.println("Quantità disponibile del prodotto: " + quantitaDisponibileDel);

                    // Recupera la mappa NoMoreProductMap dalla sessione o creane una nuova
                    Map<Integer, Boolean> noMoreProductMapDel = (Map<Integer, Boolean>) request.getSession().getAttribute("NoMoreProductMap");
                    if (noMoreProductMapDel == null) {
                        noMoreProductMapDel = new HashMap<>();
                        System.out.println("NoMoreProductMap non trovato in sessione, creata nuova mappa.");
                    }

                    if (user != null) {
                        // Aggiorna il carrello dell'utente loggato
                        System.out.println("Utente loggato, rimozione prodotto dal carrello nel database.");
                        carrelloModel.removeProductFromCart(idProdottoDelete, user, 1);
                        quantitaInCarrelloDel = carrelloModel.getQuantitaProdotto(idProdottoDelete, user);
                        System.out.println("Quantità del prodotto nel carrello dell'utente loggato: " + quantitaInCarrelloDel);
                    } else {
                        System.out.println("Utente non loggato, rimozione prodotto dal carrello di sessione.");
                        if (carrello != null) {
                            carrello.EliminaProdotto(prodottoDelete, 1);
                            request.getSession().setAttribute("carrello", carrello);
                            quantitaInCarrelloDel = carrello.getQuantitaProdotto(idProdottoDelete);
                            System.out.println("Quantità del prodotto nel carrello di sessione: " + quantitaInCarrelloDel);
                        }
                    }

                    // Se la quantità in carrello è ora inferiore alla quantità disponibile,
                    // aggiorna la mappa NoMoreProductMap per abilitare il pulsante
                    boolean noMoreProductDel = quantitaInCarrelloDel >= quantitaDisponibileDel;
                    noMoreProductMapDel.put(idProdottoDelete, noMoreProductDel);
                    System.out.println("Aggiornata NoMoreProductMap per il prodotto ID " + idProdottoDelete + ": " + noMoreProductDel);

                    // Salva la mappa aggiornata nella sessione
                    request.getSession().setAttribute("NoMoreProductMap", noMoreProductMapDel);
                    System.out.println("NoMoreProductMap salvato in sessione.");

                    // Redirect al carrello
                    response.sendRedirect(request.getContextPath() + "/Carrello.jsp");
                    System.out.println("Redirect a /Carrello.jsp eseguito.");
                    return;


                        
                    case "remove":
                    	int idProdottoRemove = Integer.parseInt(request.getParameter("id_prodotto"));
                    	
                    	if (user != null) {
                            carrelloModel.removeProductCompletelyFromCart(idProdottoRemove, user);
                        } else {
                            carrello.rimuoviProdottoCompletamente(idProdottoRemove);
                            request.getSession().setAttribute("carrello", carrello);
                        }

                        response.sendRedirect(request.getContextPath() + "/Carrello.jsp");
                        return;
                     
                    case "Checkout":
                    	Collection<IndirizzoSpedizioneBean> addresses;
                        Collection<MetodoPagamentoBean> payMethods;
                        String username;                       
                    	                   	
                    	request.getSession().setAttribute("carrello", carrello);
                    	
                    	if(user!=null) {
	                    	username = user.getEmail();
			                addresses = AreaUtenteModel.doRetrieveAllAddressByUser(username);
			                // Salva gli indirizzi e i metodi di pagamento nella sessione
			                request.getSession().setAttribute("Indirizzi", addresses);
			                payMethods = AreaUtenteModel.doRetrieveAllMethodByUser(username);
			                request.getSession().setAttribute("MetodiPagamento", payMethods);
                    	}
                    	
                    	if(user == null) { 
                    		request.getSession().setAttribute("redirectAfterLogin", "Checkout");
                    		request.getRequestDispatcher("/LoginView.jsp").forward(request, response);
                    		return;
                    	}
                    	 request.getRequestDispatcher("/Checkout.jsp").forward(request, response);
                     	return;
                     	
                    default:
                        break;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error:" + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore di database");
        }

        // Se nessuna azione è stata specificata, redirigi alla pagina del carrello
        if (user != null) {
            try {
                request.removeAttribute("carrello");
                request.setAttribute("carrello", carrelloModel.getCart(user));
            } catch (SQLException e) {
                System.out.println("Error:" + e.getMessage());
            }
        }
        request.getRequestDispatcher("/Carrello.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}
