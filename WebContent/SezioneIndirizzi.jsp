<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8" import="java.util.*, SwitchAndKeysModel.*"%>
<%@include file="Header.jsp" %>
<%@ include file="SessionPopup.jsp" %>

<%
	Collection<IndirizzoSpedizioneBean> Indirizzi=null;
	if(user!=null){
		Indirizzi = (Collection<IndirizzoSpedizioneBean>) request.getAttribute("Indirizzi");
        if (Indirizzi == null) {
            response.sendRedirect("./AreaUtente");    
            return;
		}
	}else{
		response.sendRedirect("./403.jsp");  
	}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" type="text/css" href="css/AreaUtente.css">
<title>Indirizzi di Spedizione</title>
</head>
<body>

<div class="section">
    <h1 class="title">Indirizzi di Spedizione</h1>
    <div class="container">
    <div class="sections-container">
    <div class="address-card">
        <% if (Indirizzi != null && !Indirizzi.isEmpty()) { %>
            <% for (IndirizzoSpedizioneBean ind : Indirizzi) { %>
                <div class="address-entry" id="address-<%= ind.getIdIndirizzo() %>">
                  <form class="address" id="form-<%= ind.getIdIndirizzo() %>" method="post" action="AreaUtente?action=updateAddress">
				    <p><strong style="font-size:20px">Indirizzo</strong></p>
				    	<input type="hidden" name="id_indirizzo" value="<%= ind.getIdIndirizzo() %>">
    					<input type="hidden" name="username_cliente" value="<%= ind.getUsername_cliente() %>">
				    <p><strong>Nome destinatario:</strong> 
				        <span class="view-mode"><%= ind.getNome_destinatario() %></span>
				        <input type="text" class="edit-mode" name="nome_destinatario" value="<%= ind.getNome_destinatario() %>" style="display: none;" maxlength="25">
				    </p>
				    <p><strong>Cognome destinatario:</strong> 
				        <span class="view-mode"><%= ind.getCognome_destinatario() %></span>
				        <input type="text" class="edit-mode" name="cognome_destinatario" value="<%= ind.getCognome_destinatario() %>" style="display: none;" maxlength="25">
				    </p>
				    <p><strong>Contatto destinatario:</strong> 
				        <span class="view-mode"><%= ind.getTelefono_destinatario() %></span>
				        <input type="text" class="edit-mode" name="telefono_destinatario" value="<%= ind.getTelefono_destinatario() %>" style="display: none;" minlength="10" maxlength="10" pattern=".{10}" title="Devi inserire esattamente 10 caratteri">
				    </p>
				    <p class="view-mode"><strong>Indirizzo di spedizione:</strong> 
				        <span class="view-mode"><%= ind.getFullAddress() %></span>
				        <input type="text" class="edit-mode" name="fullAddress" value="<%= ind.getFullAddress() %>" style="display: none;">
				    </p>
				    <label for="via" class="edit-mode" style="display: none;">Via:</label>
				    <input type="text" id="via" class="edit-mode" name="via" value="<%= ind.getVia() %>" style="display: none;" required maxlength="30">
				    
				    <label for="n_civico" class="edit-mode" style="display: none;">Numero Civico:</label>
				    <input type="number" class="edit-mode" id="n_civico" name="n_civico" value="<%= ind.getN_civico() %>" style="display: none;" required>
				    
				    <label for="citta" class="edit-mode" style="display: none;">Città:</label>
				    <input type="text" id="citta" class="edit-mode" name="citta" value="<%= ind.getCitta() %>" style="display: none;" required maxlength="60">
				    
				    <label for="cap" class="edit-mode" style="display: none;">CAP:</label>
				    <input type="text" id="cap" name="cap" class="edit-mode" value="<%= ind.getCap() %>" style="display: none;" minlength="5" maxlength="5" pattern=".{5}" title="Devi inserire esattamente 5 caratteri" required>
				    
				    <label for="provincia" class="edit-mode" style="display: none;">Provincia:</label>
				    <input type="text" id="provincia" class="edit-mode" name="provincia" value="<%= ind.getProvincia() %>" style="display: none;" minlength="2" maxlength="2" pattern=".{2}" title="Devi inserire esattamente 2 caratteri" required>
				    
				    <div class="errorMessages" style="color: red; margin:5px 0;"></div>
				    
				    <div class="button-container">
				        <a onclick="editAddress('<%= ind.getIdIndirizzo() %>')" class="btn-edit">
                            <img src="imgs/AreaUtente/icons8-modifica-file-di-testo-48.png" alt="Modifica Indirizzo" title="Modifica Indirizzo" style="width: 24px; height: 24px;">
                        </a>
				        <a onclick="confirmDeleteAddress('<%= ind.getIdIndirizzo() %>')" class="btn-delete">
                            <img src="imgs/AreaUtente/icons8-cestino-64.png" alt="Elimina Indirizzo" title="Elimina Indirizzo" style="width: 24px; height: 24px;">
                        </a>
				        <button type="button" onclick="cancelEditAddress('<%= ind.getIdIndirizzo() %>')" class="btn-cancel" style="display: none;">Annulla</button>
				        <button type="submit" class="btn-save" style="display: none;">Salva</button>
				    </div>
				   </form>
				</div>
            <% } %>
        <% } else { %>
            <p><strong>Non sono presenti indirizzi di spedizione.</strong></p>
        <% } %>
    </div>


    <form action="AreaUtente" method="post" class="add-address-form">
    	<input type="hidden" name="action" value="addAddress">
    	<input type="hidden" name="page" value="SezioneIndirizzi"> 
        <h3>Aggiungi Nuovo Indirizzo</h3>
        <% int newId = (Indirizzi != null) ? Indirizzi.size() + 1 : 1;%>
        <input type="hidden" id="id_indirizzo" name="id_indirizzo" value="<%= newId %>">
        
        <input type="hidden" id="username_cliente" name="username_cliente" value="<%= user.getEmail() %>" required>
        
        <label for="nome_destinatario">Nome Destinatario:</label>
        <input type="text" id="nome_destinatario" name="nome_destinatario" required maxlength="25">
        
        <label for="cognome_destinatario">Cognome Destinatario:</label>
        <input type="text" id="cognome_destinatario" name="cognome_destinatario" required maxlength="25">
        
        <label for="telefono_destinatario">Contatto Destinatario:</label>
        <input type="text" id="telefono_destinatario" name="telefono_destinatario" minlength="10" maxlength="10" pattern=".{10}" title="Devi inserire esattamente 10 caratteri" required>
                      
        <label for="via">Via:</label>
        <input type="text" id="via" name="via" required maxlength="30">
        
        <label for="n_civico">Numero Civico:</label>
        <input type="number" id="n_civico" name="n_civico" required>
        
        <label for="citta">Città:</label>
        <input type="text" id="citta" name="citta" required maxlength="60">
 
        <label for="cap">CAP:</label>
        <input type="text" id="cap" name="cap" minlength="5" maxlength="5" pattern=".{5}" title="Devi inserire esattamente 5 caratteri" required>
        
        <label for="provincia">Provincia:</label>
        <input type="text" id="provincia" name="provincia" minlength="2" maxlength="2" pattern=".{2}" title="Devi inserire esattamente 2 caratteri"  required>
     	
     	<div class="errorMessages" style="color: red; margin:5px 0;"></div>
     	   
        <button type="submit" class="btn">Aggiungi</button>
    </form>
</div>
</div>
</div>

<!-- Popup per conferma eliminazione indirizzo -->
<div id="deleteAddressPopup" class="popup">
    <div class="popup-content">
        <span class="close" onclick="closePopup('deleteAddressPopup')">&times;</span>
        <p>Sicuro di voler eliminare l'indirizzo?</p>
        <div class="popup-buttons">
            <button id="confirmDeleteAddressBtn" class="btn">Sì</button>
            <button class="btn" onclick="closePopup('deleteAddressPopup')">No</button>
        </div>
    </div>
</div>

<%@include file="Footer.jsp" %>
<script src="js/indirizzi.js"></script>
</body>
</html>
