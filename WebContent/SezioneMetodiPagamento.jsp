<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8" import="java.util.*, SwitchAndKeysModel.*"%>
<%@include file="Header.jsp" %>
<%@ include file="SessionPopup.jsp" %>

<%
    Collection<MetodoPagamentoBean> Metodi = null;
    if(user != null) {
        Metodi = (Collection<MetodoPagamentoBean>) request.getAttribute("MetodiPagamento");
        if (Metodi == null) {
            response.sendRedirect("./AreaUtente");    
            return;
        }
    } else {
        response.sendRedirect("./403.jsp");  
    }
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" type="text/css" href="css/AreaUtente.css">
<title>Metodi di Pagamento</title>
</head>
<body>

<div class="section">
    <h1 class="title">Metodi di Pagamento</h1>
    <div class="container">
    <div class="sections-container">
    <div class="payment-card">
        <% if (Metodi != null && !Metodi.isEmpty()) { %>
            <% for (MetodoPagamentoBean met : Metodi) { %>
                <div class="payment-entry" id="payment-<%= met.getIdMetodo() %>">
                  <form class="method" id="form-<%= met.getIdMetodo() %>" method="post" action="AreaUtente?action=updatePayMethod">                
                  <%if(met.getTipo().equals("PayPal")){ %>
                    <p><strong style="font-size:20px">Metodo di Pagamento</strong></p>
                        <input type="hidden" name="id_metodo" value="<%= met.getIdMetodo() %>">
                        <input type="hidden" name="username_cliente" value="<%= met.getUsernameCliente() %>">
                        <input type="hidden" name="tipo" value="<%= met.getTipo() %>" >                
                    <p><strong>PayPal:</strong> 
                        <span class="view-mode"><%= met.getMailPaypal() %></span>
                        <input type="email" class="edit-mode" name="mail_paypal" value="<%= met.getMailPaypal() %>" style="display: none;" maxlength="254">
                    </p>
                    <% }else if(met.getTipo().equals("Carta di Credito")){%>
                    <p><strong style="font-size:20px">Metodo di Pagamento</strong></p>
                        <input type="hidden" name="id_metodo" value="<%= met.getIdMetodo() %>">
                        <input type="hidden" name="username_cliente" value="<%= met.getUsernameCliente() %>" >
                        <input type="hidden" name="tipo" value="<%= met.getTipo() %>" >
                    <p><strong>Numero Carta:</strong> 
                        <span class="view-mode"><%= met.getNumeroCarta() %></span>
                        <input type="text" class="edit-mode" name="numero_carta" value="<%= met.getNumeroCarta() %>" style="display: none;" minlength="16" maxlength="16" pattern=".{16}" title="Devi inserire esattamente 16 caratteri">
                    </p>
                    <p><strong>Nome Carta:</strong> 
                        <span class="view-mode"><%= met.getNomeCarta() %></span>
                        <input type="text" class="edit-mode" name="nome_carta" value="<%= met.getNomeCarta() %>" style="display: none;" maxlength="25">
                    </p>
                    <p><strong>Cognome Carta:</strong> 
                        <span class="view-mode"><%= met.getCognomeCarta() %></span>
                        <input type="text" class="edit-mode" name="cognome_carta" value="<%= met.getCognomeCarta() %>" style="display: none;" maxlength="25">
                    </p>
                    <p><strong>Scadenza:</strong> 
                        <span class="view-mode"><%= met.getScadenzaFormattata() %></span>
                        <input type="date" class="edit-mode" name="scadenza" value="<%= met.getScadenza() %>" style="display: none;">
                    </p>
                    <p><strong>CVV:</strong> 
                        <span class="view-mode"><%= met.getCvv() %></span>
                        <input type="text" class="edit-mode" name="cvv" value="<%= met.getCvv() %>" style="display: none;" minlength="3" maxlength="3" pattern=".{3}" title="Devi inserire esattamente 3 caratteri">
                    </p>
                    <%} %>
                    
                    <div class="errorMessages" style="color: red; margin:5px 0;"></div>
                    
                    <div class="button-container">
                        <a onclick="editPayment('<%= met.getIdMetodo() %>')" class="btn-edit">
                            <img src="imgs/AreaUtente/icons8-modifica-file-di-testo-48.png" alt="Modifica Metodo" title="Modifica Metodo" style="width: 24px; height: 24px;">
                        </a>
                        <a onclick="confirmDeletePayment('<%= met.getIdMetodo() %>')" class="btn-delete">
                            <img src="imgs/AreaUtente/icons8-cestino-64.png" alt="Elimina Metodo" title="Elimina Metodo" style="width: 24px; height: 24px;">
                        </a>
                        <button type="button" onclick="cancelEditPayment('<%= met.getIdMetodo() %>')" class="btn-cancel" style="display: none;">Annulla</button>
                        <button type="submit" class="btn-save" style="display: none;">Salva</button>
                    </div>
                   </form>
                </div>

            <% } %>
        <% } else { %>
            <p><strong>Non sono presenti metodi di pagamento.</strong></p>
        <% } %>
    </div>

<form action="AreaUtente" method="post" class="add-payment-form">
        <input type="hidden" name="action" value="addPayMethod">
        <input type="hidden" name="page" value="SezioneMetodiPagamento"> 
        <h3>Aggiungi Nuovo Metodo di Pagamento</h3>
        <% int newId = (Metodi != null) ? Metodi.size() + 1 : 1; %>
        <input type="hidden" id="id_metodo" name="id_metodo" value="<%= newId %>">
        <input type="hidden" id="username_cliente" name="username_cliente" value="<%= user.getEmail() %>" required>

        <label for="tipo"><strong>Tipo:</strong></label><br>
        <select id="tipo" name="tipo" onchange="updateFormFields()" required>
            <option value="" disabled selected>Seleziona il Tipo di Pagamento</option>
            <option value="Carta di Credito">Carta di Credito</option>
            <option value="PayPal">PayPal</option>
        </select><br>

        <!-- Campi per Carta di Credito -->
        <div class="card-field">
            <label for="numero_carta">Numero Carta:</label>
            <input type="text" id="numero_carta" name="numero_carta" minlength="16" maxlength="16" pattern=".{16}" title="Devi inserire esattamente 16 caratteri">
            
            <label for="nome_carta">Nome Carta:</label>
            <input type="text" id="nome_carta" name="nome_carta" maxlength="25">
            
            <label for="cognome_carta">Cognome Carta:</label>
            <input type="text" id="cognome_carta" name="cognome_carta" maxlength="25">
            
            <label for="scadenza">Scadenza:</label>
            <input type="date" id="scadenza" name="scadenza">
            
            <label for="cvv">CVV:</label>
            <input type="text" id="cvv" name="cvv" minlength="3" maxlength="3" pattern=".{3}" title="Devi inserire esattamente 3 caratteri">
        </div>

        <!-- Campi per PayPal -->
        <div class="paypal-field" style="display: none;">
            <label for="mail_paypal">Email PayPal:</label>
            <input type="email" id="mail_paypal" name="mail_paypal">
        </div>
        
        <div class="errorMessages" style="color: red; margin:5px 0;"></div>
        
        <button type="submit" class="btn">Aggiungi</button>
    </form>
    </div>
    </div>
</div>

<!-- Popup per conferma eliminazione metodo di pagamento -->
<div id="deletePaymentPopup" class="popup">
    <div class="popup-content">
        <span class="close" onclick="closePopup('deletePaymentPopup')">&times;</span>
        <p>Sicuro di voler eliminare il metodo di pagamento?</p>
        <div class="popup-buttons">
            <button id="confirmDeletePaymentBtn" class="btn">Sì</button>
            <button class="btn" onclick="closePopup('deletePaymentPopup')">No</button>
        </div>
    </div>
</div>

<%@include file="Footer.jsp" %>
<script src="js/MetodiPagamento.js"></script>
</body>
</html>
