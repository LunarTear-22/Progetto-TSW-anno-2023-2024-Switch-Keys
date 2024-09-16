<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8" import="java.util.*,SwitchAndKeysModel.*, SwitchAndKeysUtil.*"%>
<%@include file="Header.jsp" %>
<%@include file="SessionPopup.jsp" %>

<%
    if(user==null){
        response.sendRedirect("./403.jsp");
        return;
    }

    DettaglioOrdineBean DettaglioOrdine = (DettaglioOrdineBean) request.getAttribute("dettaglio_ordine");
    Collection<ProdottoBean> ProdottiOrdine = (Collection<ProdottoBean>) request.getAttribute("lista_dettaglio");
    System.out.println(ProdottiOrdine);
    OrdineBean Ordine = (OrdineBean) request.getAttribute("ordine");

    if(DettaglioOrdine == null || ProdottiOrdine == null || Ordine == null){
        response.sendRedirect("./DettaglioOrdine");
        return;
    }

    // Calcolo dei totali
    double totaleIVA = 0.0;
    double totaleEsclusaIVA = 0.0;

    for(ProdottoBean prodotto : ProdottiOrdine){
        totaleIVA += prodotto.getImportoIva() * prodotto.getQuantita();
        totaleEsclusaIVA += prodotto.getPrezzo()* prodotto.getQuantita();
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dettagli Ordine</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="css/DettagliOrdine.css"> 
</head>
<body>
<div class="main-container">
    <h1>Dettagli Ordine</h1>
    <div class="order-details-container">
	    <div class="order">
	        <p><strong>Ordine effettuato in data: </strong><%=DateUtils.formatTimestamp(Ordine.getData())%></p>
	        <p><strong>Numero Ordine: </strong><%=Ordine.getIdOrdine()%></p>
           	<a href="Fattura?action=view-invoice&rif_id_ordine=<%=Ordine.getIdOrdine()%>" class="view-invoice">Visualizza Fattura</a>
	    </div>
        <div class="order-info">
            <div class="info-spedizione">
                <h3>Informazioni di Spedizione</h3>
                <p><%= DettaglioOrdine.getIndirizzoSpedizione() %></p>
            </div>
            
            <div class="info-metodo-pagamento">
                <%if(DettaglioOrdine.getMetodoPagamento().length()==4){ %>
                    <h3>Metodo di Pagamento </h3>
                    <p>Carta di Credito terminate con: <%=DettaglioOrdine.getMetodoPagamento()%></p>
                <%}else if(DettaglioOrdine.getMetodoPagamento().length()==6){ %>
                    <h3>Metodo di Pagamento </h3>
                    <p><%=DettaglioOrdine.getMetodoPagamento()%></p>
                <%} %>
            </div>
            
            <div class="order-summary">
            	<h3>Riepilogo Ordine</h3>
            	<div class="order-summary-content">	                
	                <p><strong>Subtotale:</strong> <%=String.format("%.2f", DettaglioOrdine.getImportoTotale() - DettaglioOrdine.getCostiSpedizione())%> €</p>
	                <p><strong>Subtotale IVA esclusa:</strong> <%=String.format("%.2f", totaleEsclusaIVA) %> €</p>
	                <p><strong>IVA:</strong> <%=String.format("%.2f", totaleIVA) %> €</p>
	                <p><strong>Costi di Spedizione: </strong><%=DettaglioOrdine.getCostiSpedizioneFormatted() %> €</p>
	                <p><strong>Totale: </strong><%=DettaglioOrdine.getImportoTotaleFormatted() %> €</p>
                </div>
            </div>
        </div>

        <div class="order-items">
            <h3>Prodotti Acquistati</h3>
            <div class="items-container">
            <%for(ProdottoBean prodotto : ProdottiOrdine){ %>
                <div class="item">
                    <div class="image-container">
	                    <a href="prodotto?action=read&id_prodotto=<%=prodotto.getIdProdotto()%>">
	                        <img src="Immagine?action=GetImmagine&rif_id_prodotto=<%= prodotto.getIdProdotto() %>" onerror="this.src='./imgs/Catalogo/icons8-non-disponibile-100.png'" alt="">
	                    </a>
                    </div>
                    <div class="info-prodotto">  
                        <p><strong>Nome: </strong><%= prodotto.getNome() %></p>
                        <p><strong>Prezzo Unitario: </strong><%= prodotto.getPrezzoNettoFormatted()%> €</p>
                        <p><strong>IVA (<%=prodotto.getIva()%>%): </strong><%= prodotto.getImportoIvaFormatted()%> €</p>
                        <p><strong>Prezzo Unitario IVA inclusa: </strong><%= prodotto.getPrezzoFormatted()%> €</p>
                        <p><strong>Quantità: </strong><%= prodotto.getQuantita()%></p>
                        <p><strong>Prezzo Totale: </strong><%= String.format("%.2f", prodotto.getPrezzoConIva() * prodotto.getQuantita()) %> €</p>
                    </div>
                </div>
            <%} %> 
            </div>
        </div>
    </div>
</div>
    <%@include file="Footer.jsp" %>
</body>
</html>
