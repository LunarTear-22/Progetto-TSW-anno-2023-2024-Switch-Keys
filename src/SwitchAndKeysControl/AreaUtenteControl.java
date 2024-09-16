package SwitchAndKeysControl;

import SwitchAndKeysModel.*;
import SwitchAndKeysUtil.DateUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@MultipartConfig
@WebServlet({"/AreaUtente"})
public class AreaUtenteControl extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AreaUtenteModel model = new AreaUtenteModel();
    private OrdineModel ordineModel = new OrdineModel();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        UtenteBean user = (UtenteBean) request.getSession().getAttribute("loggedInUser");
        
        
        Collection<OrdineBean> lista_ordini = null;
        Map<Integer, String> indirizziMap = new HashMap<>();
        
        if(user!=null) {
	        try {
				lista_ordini = ordineModel.doRetrieveOrdersByUser(user.getEmail());
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
	            request.setAttribute("lista_ordini", lista_ordini);
	            request.setAttribute("showAll", "false");
	
	            // Creazione della mappa degli indirizzi
	            for (OrdineBean order : lista_ordini) {
	                try {
	                    IndirizzoSpedizioneBean indirizzo = ordineModel.getIndirizzoByIdSpedizioneAndUsername(order, user.getEmail());
	                    indirizziMap.put(order.getIdOrdine(), indirizzo.getEstremo_Spedizione());
	                } catch (SQLException e) {
	                    log("Errore nel recupero dell'indirizzo per l'ordine #" + order.getIdOrdine(), e);
	                }
	            }
	
	            // Passa la mappa degli indirizzi alla JSP
	            request.setAttribute("indirizziMap", indirizziMap);
        }
            
        Collection<IndirizzoSpedizioneBean> addresses;
        Collection<MetodoPagamentoBean> payMethods;
        String username;
        int id_indirizzo, id_metodo;
        if (action != null) {
	        switch (action) {
	            case "viewAddress":
	            		
	            		if(user==null) {
	                    	response.sendRedirect("./403.jsp");
	                    }else {
	                    	username = user.getEmail();
			                addresses = model.doRetrieveAllAddressByUser(username);
			                request.setAttribute("Indirizzi", addresses);
			                request.getRequestDispatcher("/SezioneIndirizzi.jsp").forward(request, response);
	                    }
		                return;
	            case "deleteAddress":
		            	if(user==null) {
	                    	response.sendRedirect("./403.jsp");
	                    }else {
			            	id_indirizzo = Integer.parseInt(request.getParameter("id_indirizzo"));
			                username = user.getEmail();
			                
			                model.removeIndirizzo(id_indirizzo, username);
			                response.sendRedirect("AreaUtente?action=viewAddress");
	                    }
		                return;
		                
	            case "viewPaymentMethod":
		            	if(user==null) {
	                    	response.sendRedirect("./403.jsp");
	                    }else {
			            	username = user.getEmail();
			                payMethods = model.doRetrieveAllMethodByUser(username);
			                request.setAttribute("MetodiPagamento", payMethods);
			                request.getRequestDispatcher("/SezioneMetodiPagamento.jsp").forward(request, response);
	                    }
		                return;
	            case "deletePaymentMethod":
		            	if(user==null) {
	                    	response.sendRedirect("./403.jsp");
	                    }else {
			            	id_metodo = Integer.parseInt(request.getParameter("id_metodo"));
			                username = user.getEmail();
		                
			                model.removeMetodoPagamento(id_metodo, username);
			                response.sendRedirect("AreaUtente?action=viewPaymentMethod");
	                    }
		                return;
	
	            default:
	            	
	                break;
	        }
        }
        request.getRequestDispatcher("/AreaUtente.jsp").forward(request, response);

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String page = request.getParameter("page");

        IndirizzoSpedizioneBean address = new IndirizzoSpedizioneBean();
        MetodoPagamentoBean payMethod = new MetodoPagamentoBean();

        UtenteBean user = (UtenteBean) request.getSession().getAttribute("loggedInUser");
        if (user == null) {
            response.sendRedirect("./403.jsp");
            return;
        }

        HttpSession loginSession = request.getSession();
        loginSession.setAttribute("loggedInUser", user);

        Collection<OrdineBean> lista_ordini = null;
        Map<Integer, String> indirizziMap = new HashMap<>();

        try {
            lista_ordini = ordineModel.doRetrieveOrdersByUser(user.getEmail());
            request.setAttribute("lista_ordini", lista_ordini);
            request.setAttribute("showAll", "false");

            // Creazione della mappa degli indirizzi
            for (OrdineBean order : lista_ordini) {
                try {
                    IndirizzoSpedizioneBean indirizzo = ordineModel.getIndirizzoByIdSpedizioneAndUsername(order, user.getEmail());
                    indirizziMap.put(order.getIdOrdine(), indirizzo.getEstremo_Spedizione());
                } catch (SQLException e) {
                    log("Errore nel recupero dell'indirizzo per l'ordine #" + order.getIdOrdine(), e);
                }
            }
            request.setAttribute("indirizziMap", indirizziMap);

        } catch (SQLException e1) {
            e1.printStackTrace();
        }

        String username = user.getEmail();

        if (action != null) {
            try {
                switch (action) {
                    case "addAddress":
                        address.setId_indirizzo(Integer.parseInt(request.getParameter("id_indirizzo")));
                        address.setUsername_cliente(request.getParameter("username_cliente"));
                        address.setNome_destinatario(request.getParameter("nome_destinatario"));
                        address.setCognome_destinatario(request.getParameter("cognome_destinatario"));
                        address.setTelefono_destinatario(request.getParameter("telefono_destinatario"));
                        address.setProvincia(request.getParameter("provincia"));
                        address.setCitta(request.getParameter("citta"));
                        address.setVia(request.getParameter("via"));
                        address.setN_civico(Integer.parseInt(request.getParameter("n_civico")));
                        address.setCap(request.getParameter("cap"));

                        model.addIndirizzo(address);
                        if (page.equals("SezioneIndirizzi")) {
                            response.sendRedirect("AreaUtente?action=viewAddress");
                        } else if (page.equals("Checkout")) {
                            Collection<IndirizzoSpedizioneBean> addresses = model.doRetrieveAllAddressByUser(username);
                            loginSession.setAttribute("Indirizzi", addresses);
                            response.sendRedirect("./Checkout.jsp");
                        }
                        return;

                    case "updateAddress":
                        address.setId_indirizzo(Integer.parseInt(request.getParameter("id_indirizzo")));
                        address.setUsername_cliente(request.getParameter("username_cliente"));
                        address.setNome_destinatario(request.getParameter("nome_destinatario"));
                        address.setCognome_destinatario(request.getParameter("cognome_destinatario"));
                        address.setTelefono_destinatario(request.getParameter("telefono_destinatario"));
                        address.setProvincia(request.getParameter("provincia"));
                        address.setCitta(request.getParameter("citta"));
                        address.setVia(request.getParameter("via"));
                        address.setN_civico(Integer.parseInt(request.getParameter("n_civico")));
                        address.setCap(request.getParameter("cap"));

                        model.modifyIndirizzo(address);
                        response.sendRedirect("AreaUtente?action=viewAddress");
                        return;

                    case "addPayMethod":
                        payMethod.setIdMetodo(Integer.parseInt(request.getParameter("id_metodo")));
                        payMethod.setUsernameCliente(request.getParameter("username_cliente"));
                        payMethod.setTipo(request.getParameter("tipo"));

                        if ("Carta di Credito".equals(payMethod.getTipo())) {
                            payMethod.setNumeroCarta(request.getParameter("numero_carta"));
                            payMethod.setNomeCarta(request.getParameter("nome_carta"));
                            payMethod.setCognomeCarta(request.getParameter("cognome_carta"));                            
                            String scadenzaString = request.getParameter("scadenza");
                            LocalDate localDate = LocalDate.parse(scadenzaString);
                            payMethod.setScadenza(DateUtils.toDate(localDate));
                            payMethod.setCvv(request.getParameter("cvv"));
                        } else if ("PayPal".equals(payMethod.getTipo())) {
                            payMethod.setMailPaypal(request.getParameter("mail_paypal"));
                        }

                        model.addMetodoPagamento(payMethod);
                        if (page.equals("SezioneMetodiPagamento")) {
                            response.sendRedirect("AreaUtente?action=viewPaymentMethod");
                        } else if (page.equals("Checkout")) {
                            Collection<MetodoPagamentoBean> payMethods = model.doRetrieveAllMethodByUser(username);
                            loginSession.setAttribute("MetodiPagamento", payMethods);
                            response.sendRedirect("./Checkout.jsp");
                        }
                        return;

                    case "updatePayMethod":
                        payMethod.setIdMetodo(Integer.parseInt(request.getParameter("id_metodo")));
                        payMethod.setUsernameCliente(request.getParameter("username_cliente"));
                        payMethod.setTipo(request.getParameter("tipo"));

                        if ("Carta di Credito".equals(payMethod.getTipo())) {
                            payMethod.setNumeroCarta(request.getParameter("numero_carta"));
                            payMethod.setNomeCarta(request.getParameter("nome_carta"));
                            payMethod.setCognomeCarta(request.getParameter("cognome_carta"));
                            String scadenzaString = request.getParameter("scadenza");
                            LocalDate localDate = LocalDate.parse(scadenzaString);
                            payMethod.setScadenza(DateUtils.toDate(localDate));
                            payMethod.setCvv(request.getParameter("cvv"));
                        } else if ("PayPal".equals(payMethod.getTipo())) {
                            payMethod.setMailPaypal(request.getParameter("mail_paypal"));
                        }

                        model.modifyMetodoPagamento(payMethod);
                        response.sendRedirect("AreaUtente?action=viewPaymentMethod");
                        return;

                    case "FilterByYear":
                        String annoStr = request.getParameter("order-year");
                        request.setAttribute("showAll", "true");
                        if (annoStr != null && !annoStr.isEmpty()) {
                            try {
                                int anno = Integer.parseInt(annoStr);
                                lista_ordini = ordineModel.doRetrieveOrdersByYear(anno);
                            } catch (NumberFormatException | SQLException e) {
                                e.printStackTrace();
                            }
                        } else {
                            lista_ordini = ordineModel.doRetrieveOrdersByUser(user.getEmail());
                        }

                        request.setAttribute("lista_ordini", lista_ordini);

                        for (OrdineBean order : lista_ordini) {
                            try {
                                IndirizzoSpedizioneBean indirizzo = ordineModel.getIndirizzoByIdSpedizioneAndUsername(order, user.getEmail());
                                indirizziMap.put(order.getIdOrdine(), indirizzo.getEstremo_Spedizione());
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                        request.setAttribute("indirizziMap", indirizziMap);
                        request.getRequestDispatcher("/AreaUtente.jsp").forward(request, response);
                        return;

                    case "showAllOrders":
                        request.setAttribute("showAll", "true");
                        lista_ordini = ordineModel.doRetrieveOrdersByUser(user.getEmail());

                        request.setAttribute("lista_ordini", lista_ordini);

                        for (OrdineBean order : lista_ordini) {
                            try {
                                IndirizzoSpedizioneBean indirizzo = ordineModel.getIndirizzoByIdSpedizioneAndUsername(order, user.getEmail());
                                indirizziMap.put(order.getIdOrdine(), indirizzo.getEstremo_Spedizione());
                            } catch (SQLException e) {
                                log("Errore nel recupero dell'indirizzo per l'ordine #" + order.getIdOrdine(), e);
                            }
                        }

                        request.setAttribute("indirizziMap", indirizziMap);
                        request.getRequestDispatcher("/AreaUtente.jsp").forward(request, response);
                        return;

                    default:
                        response.sendRedirect("AreaUtente.jsp");
                        return;
                }
            } catch (SQLException e) {
                log("Database error occurred", e);
                response.sendRedirect("500.jsp");
            }
        }

        response.sendRedirect("AreaUtente.jsp");
    }


}
    