<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="Header.jsp" %>
<%@ include file="SessionPopup.jsp" %>
<%@ page import="java.util.*, SwitchAndKeysModel.*" %>

<%
    // Recupero carrello, indirizzi e metodi di pagamento dalla sessione
    CarrelloBean carrello = (CarrelloBean) session.getAttribute("carrello");
    Collection<IndirizzoSpedizioneBean> indirizzi = (Collection<IndirizzoSpedizioneBean>) session.getAttribute("Indirizzi");
    Collection<MetodoPagamentoBean> metodiPagamento = (Collection<MetodoPagamentoBean>) session.getAttribute("MetodiPagamento");
	
    IndirizzoSpedizioneBean indirizzo_scelto = (IndirizzoSpedizioneBean) session.getAttribute("indirizzo_scelto");
   
    MetodoPagamentoBean metodo_scelto = (MetodoPagamentoBean) session.getAttribute("metodo_scelto");
   

    boolean orderConfirmed = request.getAttribute("orderConfirmed") != null && (boolean) request.getAttribute("orderConfirmed");

    
    if (user == null) {
        response.sendRedirect("./403.jsp");
        return;
    }
    
    if (carrello == null || carrello.getProdotti() == null || carrello.getProdotti().isEmpty()) {
        response.sendRedirect("./Carrello");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Checkout</title>
    <link rel="stylesheet" type="text/css" href="css/Checkout.css">
</head>
<body data-order-confirmed="<%= orderConfirmed %>" data-metodoScelto="<%= metodo_scelto%>" data-indirizzoScelto="<%= indirizzo_scelto%>">
<div class="checkout_body">

<h1 class="checkout-title">Checkout</h1>

<div class="checkout-container">
    
    <div class="Estremi_ordine">
    	
	    <!-- Sezione Indirizzi di Spedizione -->
	    <div class="shipping-address">
	    	
	        <h2>Indirizzo di Spedizione</h2>
	        
	        <div class="Indirizzo_scelto">
	    		<% if(indirizzo_scelto == null){ %>
	    			<p class="estremo_selezionato_vuoto">Non hai selezionato nessun indirizzo</p>
	    		<%}else if(indirizzo_scelto != null){ %>
	    			<div class="address-entry">
	    				
	    				<input type="hidden" name="rif_id_indirizzo_scelto" value="<%= indirizzo_scelto.getIdIndirizzo() %>">
		              
	                   <div class="info_estremo">
	                   		<h3><strong>Indirizzo Scelto:</strong></h3>
		                    <p><strong>Nome destinatario:</strong> <span><%= indirizzo_scelto.getNome_destinatario() %></span></p>
		                    <p><strong>Cognome destinatario:</strong> <span><%= indirizzo_scelto.getCognome_destinatario() %></span></p>
		                    <p><strong>Contatto destinatario:</strong> <span><%= indirizzo_scelto.getTelefono_destinatario() %></span></p>
		                    <p><strong>Indirizzo di spedizione:</strong> <span><%= indirizzo_scelto.getFullAddress() %></span></p>
		               </div>
	               </div>
	           <%} %>
    		</div>
    		
    		<button class="toggle-btn" onclick="toggleSection('shipping-address-content', 'shipping-icon')">
                    Altri indirizzi di spedizione 
                    <span id="shipping-icon" class="toggle-icon">&#9660;</span>
            </button>
	        <div id="shipping-address-content" class="hidden">
	        <% if (indirizzi != null && !indirizzi.isEmpty()) { %>
	            <form method="post" action="Checkout?action=selectAddress">
	                <% for (IndirizzoSpedizioneBean indirizzo : indirizzi) { %>
	                    <div class="address-entry">
	                    	<div class="radio_container">
	                        	<input type="radio" name="rif_id_indirizzo" id="indirizzo-<%= indirizzo.getIdIndirizzo() %>" value="<%= indirizzo.getIdIndirizzo() %>" required>
	                        </div>
	                        <div class="info_estremo">
		                        <p><strong>Nome destinatario:</strong> <span><%= indirizzo.getNome_destinatario() %></span></p>
		                        <p><strong>Cognome destinatario:</strong> <span><%= indirizzo.getCognome_destinatario() %></span></p>
		                        <p><strong>Contatto destinatario:</strong> <span><%= indirizzo.getTelefono_destinatario() %></span></p>
		                        <p><strong>Indirizzo di spedizione:</strong> <span><%= indirizzo.getFullAddress() %></span></p>
	                    	</div>
	                    </div>
	                <% } %>
	                <a class="add_estremo" href="#" id="add-address-link">Aggiungi un indirizzo</a>
	                <button type="submit" class="select-btn">Seleziona Indirizzo</button>
	            </form>
	        <% } else { %>
	            <p class="estremi_assenti">Non ci sono indirizzi salvati.</p>
	            <a class="add_estremo" href="#" id="add-address-link">Aggiungi un indirizzo</a>
	        <% } %>
	    	</div>
	    </div>
	
	    <!-- Sezione Metodi di Pagamento -->
	    <div class="payment-methods">
	    	
	       <h2>Metodo di Pagamento</h2>
	        
	        <div class="Metodo_scelto">
	    		<% if(metodo_scelto == null){ %>
	    			<p class="estremo_selezionato_vuoto">Non hai selezionato nessun Metodo di Pagamento</p>
	    		<%}else if(metodo_scelto != null){ %>
	    			<div class="payment-entry">
	    				
	    				<input type="hidden" name="rif_id_metodo_scelto" value="<%= metodo_scelto.getIdMetodo() %>">
		              
	                   	<div class="info_estremo">
	                        <label for="metodo-<%= metodo_scelto.getIdMetodo() %>">
	                        	<h3><strong>Metodo di Pagamento Scelto:</strong></h3>
	                            <% if (metodo_scelto.getTipo().equals("Carta di Credito")) { %>
	                                <strong style="font-size:20px"><%= metodo_scelto.getTipo() %></strong> <br>
	                                <strong>Numero Carta:</strong><%= metodo_scelto.getNumeroCarta() %><br>
	                                <strong>Nome:</strong> <%= metodo_scelto.getNomeCarta() %><br>
	                                <strong>Cognome:</strong> <%= metodo_scelto.getCognomeCarta() %><br>
	                                <strong>Scadenza:</strong> <%= metodo_scelto.getScadenzaFormattata() %>
	                            <% } else if (metodo_scelto.getTipo().equals("PayPal")) { %>
	                                <strong style="font-size:20px"><%= metodo_scelto.getTipo() %></strong><br>
	                                <strong>Email PayPal:</strong> <%= metodo_scelto.getMailPaypal() %>
	                            <% } %>
	                        </label>
                    	</div>
	               </div>
	           <%} %>
    		</div>
	        
	        <button class="toggle-btn" onclick="toggleSection('payment-methods-content', 'payment-icon')">
                    Altri metodi di pagamento 
                    <span id="payment-icon" class="toggle-icon">&#9660;</span>
            </button>
	        <div id="payment-methods-content" class="hidden">
	        <% if (metodiPagamento != null && !metodiPagamento.isEmpty()) { %>
	            <form method="post" action="Checkout?action=selectPaymentMethod">
	                <% for (MetodoPagamentoBean metodo : metodiPagamento) { %>
	                    <div class="payment-entry">
	                    	<div class="radio_container">
	                        	<input type="radio" name="rif_id_metodo" id="metodo-<%= metodo.getIdMetodo() %>" value="<%= metodo.getIdMetodo() %>" required>
	                        </div>
	                        <div class="info_estremo">
		                        <label for="metodo-<%= metodo.getIdMetodo() %>">
		                            <% if (metodo.getTipo().equals("Carta di Credito")) { %>
		                                <strong style="font-size:20px"><%= metodo.getTipo() %></strong><br>
		                                <strong>Numero Carta:</strong> <%= metodo.getNumeroCarta() %><br>
		                                <strong>Nome:</strong> <%= metodo.getNomeCarta() %><br>
		                                <strong>Cognome:</strong> <%= metodo.getCognomeCarta() %><br>
		                                <strong>Scadenza:</strong> <%= metodo.getScadenzaFormattata() %>
		                            <% } else if (metodo.getTipo().equals("PayPal")) { %>
		                                <strong style="font-size:20px"><%= metodo.getTipo() %></strong><br>
		                                <strong>Email PayPal:</strong> <%= metodo.getMailPaypal() %>
		                            <% } %>
		                        </label>
	                    	</div>
	                    </div>
	                <% } %>
	                <a class="add_estremo" href="#" id="add-payment-link">Aggiungi un metodo di pagamento</a>
	                <button type="submit" class="select-btn">Seleziona Metodo</button>
	            </form>
	        <% } else { %>
	            <p class="estremi_assenti">Non ci sono metodi di pagamento salvati.</p>
	            <a class="add_estremo" href="#" id="add-payment-link">Aggiungi un metodo di pagamento</a>
	        <% } %>
	        </div>
	    </div>
    </div>

	<div class="conferma_ordine">
	    <!-- Riepilogo Ordine -->
	    <div class="order-summary">
	        <h2>Riepilogo Ordine</h2>
	        <ul class="cart-summary-list">
	            <% for (ProdottoBean prodotto : carrello.getProdotti()) { %>
	                <li>
	                	<div class="img_prodotto">
	                    	<span><img src="Immagine?action=GetImmagine&rif_id_prodotto=<%= prodotto.getIdProdotto() %>" onerror="this.src='./imgs/Catalogo/No_image.jpeg'" class="cart-image"></span>
	                    </div>
	                    <div class="info_prodotto">
		                    <span><strong><%= prodotto.getNome() %></strong></span><br>
		                    <span><strong>Quantità: </strong><%= prodotto.getQuantita() %></span><br>
		                    <span><strong>Prezzo Unitario: </strong><%= prodotto.getPrezzoFormatted() %> €</span><br>
	                	</div>
	                </li>
	            <% } %>
	        </ul>
	        <p><strong>Sub-Totale: </strong><%= carrello.getPrezzoTotaleFormatted() %> €</p>
	        <p><strong>Costi di Spedizione: </strong>5.00 €</p>
	        <p><strong>Totale: </strong><%= carrello.getPrezzoTotaleFormatted(carrello.getPrezzoTotale() + 5) %> €</p>
	    </div>
	    
	
	    <!-- Conferma Ordine -->
	    <form id="Checkout-form" action="Checkout" method="post">
	        <input type="hidden" name="action" value="confirmOrder">
	        <input type="hidden" name="totale" value="<%= carrello.getPrezzoTotaleFormatted(carrello.getPrezzoTotale() + 5) %>">
	        <input type="hidden" name="costi_spedizione" value="<%= 5.00 %>">
			<% session.setAttribute("Metodo_Pagamento", metodo_scelto); %>
			<% session.setAttribute("Indirizzo_Spedizione", indirizzo_scelto); %>
			 
	        <button type="submit" class="checkout-btn">Conferma Ordine</button>
	    </form>
	    <div id="error-message" style="color: red; display: none;">
        	<p>Scegli gli estremi per effettuare l'ordine.</p>
    	</div>
    </div>

</div>

<!-- Overlay per il popup di conferma -->
<div id="confirmation-overlay" class="confirmation-overlay">
    <div id="confirmation-popup" class="confirmation-popup">
        <h2>Ordine Confermato</h2>
        <p>Grazie per il tuo acquisto!</p>
        <a class="link-to-page" href= "Home.jsp"><button class="home">Torna alla Home</button></a>
    </div>
</div>

<!-- Overlay and Popup for adding a new address -->
<div class="overlay" id="address-overlay">
    <div class="popup">
        <form id="add-address-form" action="AreaUtente" method="post" class="add-address-form">
            <input type="hidden" name="action" value="addAddress">
            <input type="hidden" name="page" value="Checkout">  
            <h3>Aggiungi Nuovo Indirizzo</h3>
            <div class="form_content">
	            <%
	                int newId_address = (indirizzi != null) ? indirizzi.size() + 1 : 1;
	            %>
	            <input type="hidden" id="id_indirizzo" name="id_indirizzo" value="<%= newId_address %>">
	            <input type="hidden" id="username_cliente" name="username_cliente" value="<%= user.getEmail() %>" required>
	            
	            <span>
		            <label for="nome_destinatario">Nome Destinatario:</label>
		            <input type="text" id="nome_destinatario" name="nome_destinatario" required maxlength="25">
	            </span>
	            
	            <span>
		            <label for="cognome_destinatario">Cognome Destinatario:</label>
		            <input type="text" id="cognome_destinatario" name="cognome_destinatario" required required maxlength="25">
	            </span>
	            
	            <span>
		            <label for="telefono_destinatario">Contatto Destinatario:</label>
		            <input type="text" id="telefono_destinatario" name="telefono_destinatario" minlength="10" maxlength="10" pattern="\d{10}" title="Devi inserire esattamente 10 caratteri numerici" required>
	            </span>
	            
	            <span>           
		            <label for="via">Via:</label>
		            <input type="text" id="via" name="via" required maxlength="30">
	            </span>
	            
	            <span>
		            <label for="n_civico">Numero Civico:</label>
		            <input type="number" id="n_civico" name="n_civico" required>
	            </span>
	            
	            <span>
		            <label for="citta">Città:</label>
		            <input type="text" id="citta" name="citta" required required maxlength="60">
	            </span>
	            
	     		<span>
		            <label for="cap">CAP:</label>
		            <input type="text" id="cap" name="cap" minlength="5" maxlength="5" pattern="\d{5}" title="Devi inserire esattamente 5 caratteri numerici" required>
	            </span>
	            
	            <span>
		            <label for="provincia">Provincia:</label>
		            <input type="text" id="provincia" name="provincia" minlength="2" maxlength="2" pattern="[A-Za-z]{2}" title="Devi inserire esattamente 2 caratteri alfabetici" required>
	            </span>
            </div>
            <span>
            	<span class="errorMessages" style="color: red; margin:5px 0;"></span>
	            <button type="submit" class="btn">Aggiungi</button>
	            <button type="button" class="close-btn" onclick="closePopup('address-overlay')">Chiudi</button>
            </span>
        </form>
    </div>
</div>
</div>
<!-- Overlay and Popup for adding a new payment method -->
<div class="overlay" id="payment-overlay">
    <div class="popup">
        <form id="add-payment-form" action="AreaUtente" method="post" class="add-payment-form">
            <input type="hidden" name="action" value="addPayMethod"> 
            <input type="hidden" name="page" value="Checkout">  
            <h3>Aggiungi Nuovo Metodo di Pagamento</h3>
            <div class="form_content">
	            <%
	                int newId_payment = (metodiPagamento != null) ? metodiPagamento.size() + 1 : 1;
	            %>
	            <input type="hidden" id="id_metodo" name="id_metodo" value="<%= newId_payment %>">
	            <input type="hidden" id="username_cliente" name="username_cliente" value="<%= user.getEmail() %>" required>
				
				<span>
		            <label for="tipo"><strong>Tipo:</strong></label>
		            <select id="tipo" name="tipo" onchange="updateFormFields()" required>
		                <option value="" disabled selected>Seleziona il Tipo di Pagamento</option>
		                <option value="Carta di Credito">Carta di Credito</option>
		                <option value="PayPal">PayPal</option>
		            </select><br>
				</span>
				
	            <!-- Campi per Carta di Credito -->
	            <div class="card-field">
	            <span>
	                <label for="numero_carta">Numero Carta:</label>
	                <input type="text" id="numero_carta" name="numero_carta" minlength="16" maxlength="16" pattern="\d{16}" title="Devi inserire esattamente 16 caratteri numerici">
	            </span>
	            
	            <span>    
	                <label for="nome_carta">Nome Carta:</label>
	                <input type="text" id="nome_carta" name="nome_carta" maxlength="25">
	            </span>
	            
	            <span>   
	                <label for="cognome_carta">Cognome Carta:</label>
	                <input type="text" id="cognome_carta" name="cognome_carta" maxlength="25">
	           	</span>
	           	
	            <span>   
	                <label for="scadenza">Scadenza:</label>
	                <input type="date" id="scadenza" name="scadenza">
	            </span>
	            
	            <span>    
	                <label for="cvv">CVV:</label>
	                <input type="text" id="cvv" name="cvv" minlength="3" maxlength="3" pattern="\d{3}" title="Devi inserire esattamente 3 caratteri numerici">
	            </span>
	            </div>
	
	            <!-- Campi per PayPal -->
	            <div class="paypal-field" style="display: none;">
	            <span>
	                <label for="mail_paypal">Email PayPal:</label>
	                <input type="email" id="mail_paypal" name="mail_paypal" maxlength="254">
	            </span>
	            </div>
            </div>
            <span>
            	<span class="errorMessages" style="color: red; margin:5px 0;"></span>
	            <button type="submit" class="btn">Aggiungi</button>
	            <button type="button" class="close-btn" onclick="closePopup('payment-overlay')">Chiudi</button>
        	</span>
        </form>
    </div>
</div>

<script src="js/Checkout.js"></script>
<script type="text/javascript">
	window.metodo_scelto = <%= metodo_scelto != null ? metodo_scelto.getIdMetodo() : null %>;
	window.indirizzo_scelto = <%= indirizzo_scelto != null ? indirizzo_scelto.getIdIndirizzo() : null %>;
</script>

<%@ include file="Footer.jsp" %>
</body >
</html>
