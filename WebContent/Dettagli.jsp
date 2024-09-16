<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="Header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" import="java.util.*,SwitchAndKeysModel.ProdottoBean,SwitchAndKeysModel.*"%>
<%@include file="SessionPopup.jsp" %>
<%
    ProdottoBean prodotto = (ProdottoBean) request.getSession().getAttribute("prodotto");
	
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dettagli prodotto</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="css/Dettagli.css"> 
</head>
<body>
<div class="details-body">
    <div class="product-details-container">
        <%
            if (prodotto != null && prodotto.getIdProdotto() != -1) {
        %>
        <div class="product-image">
            <img src="Immagine?action=GetImmagine&rif_id_prodotto=<%=prodotto.getIdProdotto()%>" onerror="this.src='./imgs/Catalogo/No_image.jpeg'" alt="<%=prodotto.getNome()%>">
        </div>
        <div class="product-info">
            <h2><%=prodotto.getNome()%></h2>
            <p><strong>Tipo:</strong> <%=prodotto.getCategoria() +" "+ prodotto.getTipo()%></p>
            <p><strong>Marca:</strong> <%=prodotto.getMarca()%></p>
            <%
                if (prodotto.getMateriale() != null && !prodotto.getMateriale().isEmpty()) {
            %>
            <p><strong>Materiale:</strong> <%=prodotto.getMateriale()%></p>
            <%
                }
            %>
           <%
			    List<String> connessioni = new ArrayList<>();
			    if (Boolean.TRUE.equals(prodotto.isCablata())) connessioni.add("Cablata");
			    if (Boolean.TRUE.equals(prodotto.isWifi())) connessioni.add("WiFi");
			    if (Boolean.TRUE.equals(prodotto.isBluetooth())) connessioni.add("Bluetooth");
			    
			    if (!connessioni.isEmpty()) {
			%>
			    <p><strong>Connettività:</strong> <%= String.join(" / ", connessioni) %></p>
			<%
			    }
			%>
            <p><strong>Descrizione:</strong> <%=prodotto.getDescrizione()%></p>
            <p><strong>Prezzo:</strong> <%=prodotto.getPrezzoFormatted()%>€</p>
            <p><strong>Disponibilità:</strong> <%=prodotto.getQuantita() > 0 ? "Disponibile" : "Non disponibile"%></p>
            <a href="prodotto?action=addC&id_prodotto=<%= prodotto.getIdProdotto() %>" class="cta-button" 
               <%= prodotto.getQuantita() <= 0 ? "style='pointer-events: none; background-color: #cccccc;'" : "" %>>
               <%= prodotto.getQuantita() > 0 ? "Aggiungi al carrello" : "Esaurito" %>
            </a>
        </div>
        <%
            }else{
        %>
        	<div class="prodotto-non-esistente">
        		<p>Il prodotto non è più presente nel nostro store</p>
       		    <a href="Home.jsp" class="cta-button" >Torna alla Home</a>   	
        	</div>
        <%
            }
        %>
    </div>
    </div>
    <%@include file="Footer.jsp" %>
</body>
</html>
