<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="Header.jsp" %>
<%@include file="SessionPopup.jsp" %>

<%@ page contentType="text/html; charset=UTF-8" import="java.util.*,SwitchAndKeysModel.ProdottoBean,SwitchAndKeysModel.*"%>

<%
	if (user == null || !user.isAdmin()) {
	    response.sendRedirect("./403.jsp");
	    return;
	}
    ProdottoBean prodotto = (ProdottoBean) request.getSession().getAttribute("ProdottoDaModificare");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Modifica prodotto</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="css/ModificaProdotto.css">
</head>
<body>
    <%
        if (prodotto != null) {
    %>
    <h2>Modifica il prodotto</h2>
    <form id="myForm" action="prodotto" method="post" enctype="multipart/form-data">
        <input type="hidden" name="action" value="modify"> 
        <input type="hidden" name="id_prodotto" value="<%= prodotto.getIdProdotto() %>">
        <input type="hidden" name="rif_id_catalogo" value="<%= prodotto.getRifIdCatalogo() %>">
        
        <label for="nome">Nome del prodotto:</label>
        <input name="nome" type="text" maxlength="50" value="<%= prodotto.getNome()%> " required placeholder="Inserisci il nome del prodotto"><br>
        
        <label for="dati_immagine">Immagine:</label>
        <input id="fileInput" class="file" type="file" name="dati_immagine" maxlength="255" accept="image/*"><br>
        <p id="error-message" style="color: red;"></p>
             
        <label for="prezzo">Prezzo (NO IVA):</label>
        <input name="prezzo" type="number" min="0" step="0.01" value="<%= prodotto.getPrezzo()%>" required><br>
        
        <label for="iva">Iva:</label>
        <input name="iva" type="number" min="0" value="<%= prodotto.getIva()%>" required><br>
        
        <label for="quantita">Quantità:</label>
        <input name="quantita" type="number" min="0" value="<%= prodotto.getQuantita()%>" required><br>
        
        <label for="categoria">Categoria:</label>
        <select id="categoria" class="myselect" name="categoria" required>
            <option value="" disabled selected><%= prodotto.getCategoria()%></option>
            <option value="Tastiera">Tastiera</option>
            <option value="Keycaps">Keycaps</option>
            <option value="Switch">Switch</option>
            <option value="Accessori">Accessori</option>
        </select><br>
    
        <div id="tipo-container" class="hidden">
            <label for="tipo-select">Tipo:</label>
            <select id="tipo-select" class="myselect" name="tipo-select">
                <!-- Opzioni verranno aggiunte dinamicamente -->
            </select>
        </div>
    	
        <div id="accessori-input" class="hidden">
            <label for="tipo-input">Tipo:</label>
            <input id="tipo-input" class="myinput" name="tipo-input" maxlength="20" value="<%= prodotto.getTipo()%>"></input>
        </div> 

		 
        <% if(prodotto.getMateriale() == null) { %>
            <label for="materiale">Materiale:</label>
            <input name="materiale" class="myinput" maxlength="20" placeholder="Inserisci il materiale"></input><br>
        <% } else if(prodotto.getMateriale() != null) { %>
            <label for="materiale">Materiale:</label>
            <input name="materiale" class="myinput" maxlength="20" value="<%= prodotto.getMateriale()%>"></input><br>
        <% } %>
        
        <label for="marca">Marca:</label>
        <input name="marca" class="myinput" maxlength="15" required value="<%= prodotto.getMarca()%>"></input><br>
        
        <div id="tecnologie-sezione" class="hidden">
		    <br>
		    <p>Connettività:</p>
		    <label for="cablata">Cablata:
		        <input name="cablata" id="cablata" type="checkbox" value="true" <%= prodotto.isCablata() != null && prodotto.isCablata() ? "checked" : "" %>>
		    </label>
		    <label for="wifi">Wifi:
		        <input name="wifi" id="wifi" type="checkbox" value="true" <%= prodotto.isWifi() != null && prodotto.isWifi() ? "checked" : "" %>>
		    </label>
		    <label for="bluetooth">Bluetooth:
		        <input name="bluetooth" id="bluetooth" type="checkbox" value="true" <%= prodotto.isBluetooth() != null && prodotto.isBluetooth() ? "checked" : "" %>>
		    </label>
		</div>
			<span id="tecnologie-error" style="color: red; display: none;">Seleziona almeno una opzione tra Wifi, Cablata o Bluetooth.</span>
        
        <label for="descrizione">Descrizione:</label>
        <textarea name="descrizione" maxlength="100" rows="5" required placeholder="Inserisci la descrizione del prodotto"><%= prodotto.getDescrizione()%></textarea><br>
    	
		<div class="errorMessages" style="color: red; margin:5px 0;"></div>
    	
    	
        <input type="submit" value="Modifica">
        <a class="reset" href="SetHeaderServlet?action=NotHeader" >Cancella</a>
    </form>
    
    <%
        } else {
    %>
        Impossibile modificare il prodotto.
    <%
        }
    %>
    <%@include file="Footer.jsp" %>
    
    <script>
        // Passa le variabili JSP a JavaScript
        const tipoSelezionato = "<%= prodotto.getTipo() %>";
        const categoriaSelezionata = "<%= prodotto.getCategoria() %>";
    </script>
    <script src="js/modificaprodotto.js"></script>
</body>
</html>
