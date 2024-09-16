<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="Header.jsp" %>
<%@include file="SessionPopup.jsp" %>

<%@ page contentType="text/html; charset=UTF-8" import="java.util.*,SwitchAndKeysModel.ProdottoBean,SwitchAndKeysModel.*"%>

<%
	Collection<?> prodotti = (Collection<?>) request.getAttribute("prodotti");
	if(prodotti == null) {
		response.sendRedirect("./prodotto");	
		return;
	}
	
	ProdottoBean prodotto = (ProdottoBean) request.getAttribute("prodotto");
%>
<!DOCTYPE html>
<html>
<head>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link rel="stylesheet" href="./css/Catalogo.css">
	<title>Catalogo SwitchAndKeys</title>
</head>
<body>
	<h1>Catalogo</h1>
	<a href="prodotto"></a>
	<table border="1">
		<tr>
			<th>Immagine</th>
			<th>Codice del prodotto <a href="prodotto?sort=id_prodotto">Ordina</a></th>
			<th>Nome <a href="prodotto?sort=nome">Ordina</a></th>
			<th>Descrizione <a href="prodotto?sort=descrizione">Ordina</a></th>
			<th>Action</th>
		</tr>
		<%
			if (prodotti != null && prodotti.size() != 0) {
				Iterator<?> it = prodotti.iterator();
				while (it.hasNext()) {
					ProdottoBean bean = (ProdottoBean) it.next();
		%>
		<tr>
			<td><img src="Immagine?action=GetImmagine&rif_id_prodotto=<%=bean.getIdProdotto()%>" onerror="this.src='./imgs/Catalogo/No_image.jpeg'" style="width:100px;height:100px"></td>
			<td><%=bean.getIdProdotto()%></td>
			<td><%=bean.getNome()%></td>
			<td><%=bean.getDescrizione()%></td>
			<td>
				<a href="#" class="delete-button" data-id="<%=bean.getIdProdotto()%>">Elimina Prodotto</a><br>
				<a href="prodotto?action=showModifyForm&id_prodotto=<%=bean.getIdProdotto()%>">Modifica Prodotto</a><br>
				<a href="prodotto?action=read&id_prodotto=<%=bean.getIdProdotto()%>">Visualizza Dettagli Prodotto</a><br>
				<a href="prodotto?action=addC&id_prodotto=<%=bean.getIdProdotto()%>">Aggiungi al carrello</a>
			</td>
		</tr>
		<%
				}
			} else {
		%>
		<tr>
			<td colspan="6">Non sono presenti prodotti nel catalogo</td>
		</tr>
		<%
			}
		%>
	</table>
	<div class="add-product-section">
    <h2>Inserisci un nuovo prodotto nel catalogo</h2>
    <form id="myForm" action="prodotto" method="post" enctype="multipart/form-data">
        <input type="hidden" name="action" value="insert">
        <input type="hidden" name="rif_id_catalogo" value="1">
        <%int idProdotto = (prodotti != null ? prodotti.size() +1 : 1); %> 
        <input type="hidden" name="id_prodotto" value="<%=idProdotto %>">
		<div class= "form-content">
			<div>
	        	<label for="nome">Nome del prodotto:</label>
	        	<input name="nome" type="text" maxlength="50" required placeholder="Inserisci il nome del prodotto"><br>
			</div>
			
			<div>
	        	<label for="dati_immagine">Immagine:</label>
	        	<input class="file" id="fileInput" type="file" name="dati_immagine" maxlength="255" accept="image/*"><br>
	        	<p id="error-message" style="color: red;"></p>
			</div>
			
			<div>
	        	<label for="prezzo">Prezzo:</label>
	        	<input name="prezzo" type="number" min="0" step="0.01" required placeholder="Inserisci il prezzo del prodotto (Senza l'IVA)"><br>
			</div>
	
			<div>
	        	<label for="iva">Iva:</label>
	        	<input name="iva" type="number" min="0" required placeholder="Inserisci l'Iva del prodotto"><br>
			</div>
			
			<div>
	        	<label for="quantita">Quantità:</label>
	        	<input name="quantita" type="number" min="0" required placeholder="Inserisci la quantità disponibile di prodotto"><br>
			</div>
			
			<div>
		        <label for="categoria">Categoria:</label>
		        <select class="myselect"  id="categoria" name="categoria" required>
		            <option value="" disabled selected>Seleziona una categoria</option>
		            <option value="Tastiera">Tastiera</option>
		            <option value="Keycaps">Keycaps</option>
		            <option value="Switch">Switch</option>
		            <option value="Accessori">Accessori</option>
		        </select><br>
			</div>
			
			<div id="tipo-div" class="hidden">
		        <div id="tipo-container" class="hidden">
		            <label for="tipo-select">Tipo:</label>
		            <select class="myselect" id="tipo-select" name="tipo-select">
		                <!-- Opzioni verranno aggiunte dinamicamente -->
		            </select>
		        </div>
		
		        <div id="accessori-input" class="hidden">
		            <label for="tipo-input">Tipo di Accessorio:</label>
		            <input class="myinput" id="tipo-input" name="tipo-input" maxlength="20" placeholder="Inserisci il tipo di accessorio">
		        </div>
		        <p id="error-message-input" style="color: red;"></p>
			</div>
			
			<div>
		        <label for="materiale">Materiale:</label>
		        <input class="myinput" name="materiale" maxlength="20" placeholder="Inserisci il materiale"><br>
			</div>
			
			<div>
	        	<label for="marca">Marca:</label>
	        	<input class="myinput" name="marca" maxlength="15" required placeholder="Inserisci la marca del prodotto"><br>
			</div>
			
	        <div id="tecnologie-sezione" class="hidden">
	            <br><p>Connettività:</p>
	            <label for="cablata">Cablata:<input id="cablata" name="cablata" type="checkbox" value="true" ></label>
	            <label for="wifi">Wifi:<input id="wifi" name="wifi" type="checkbox" value="true"></label>
	            <label for="bluetooth">Bluetooth:<input id="bluetooth" name="bluetooth" type="checkbox" value="true"></label>
	            <span id="tecnologie-error" style="color: red; display: none;">Seleziona almeno una opzione tra Wifi, Cablata o Bluetooth.</span>
	        </div>
			
			<div>
		        <label for="descrizione">Descrizione:</label>
		        <textarea name="descrizione" maxlength="100" rows="5" required placeholder="Inserisci la descrizione del prodotto"></textarea><br>
			</div>
			
			<div class="errorMessages" style="color: red; margin:5px 0;"></div>
	    </div>
	    <div class="btn-container">
		    <input type="submit" value="Aggiungi">	        
	        <a class="reset" href="SetHeaderServlet?action=NotHeader" >Cancella</a>
	    </div>
    </form>
    </div>
    

    <div id="confirmModal" class="modal">
        <div class="modal-content">
            <p>Eliminare il prodotto?</p>
            <div class="modal-buttons">
                <button id="confirmYes">Sì</button>
                <button id="confirmNo">No</button>
            </div>
        </div>
    </div>
  
	<div id="successModal" class="modal">
	    <div class="modal-content">

	        <p>Prodotto inserito con successo</p>
	    </div>
	</div>
    
    <%@include file="Footer.jsp" %>
    
    <script src="js/Catalogo.js"></script>
    <script src="js/aggiungiprodotto.js"></script>
    <script>
        // Passa le variabili JSP a JavaScript
        const tipoSelezionato = "categoria";
    </script>
</body>
</html>
