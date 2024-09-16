<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8" import="java.util.*, SwitchAndKeysModel.*, SwitchAndKeysUtil.*"%>
<%@include file="Header.jsp" %>
<%@ include file="SessionPopup.jsp" %>

<%    
    if (user == null) {
        response.sendRedirect("./403.jsp");
        return;
    }
	
	
    Collection<OrdineBean> lista_ordini = (Collection<OrdineBean>) request.getAttribute("lista_ordini");
    Collection<OrdineBean> lista_ordini_filtrati = (Collection<OrdineBean>) request.getAttribute("lista_ordini_filtrati");
    
    if(lista_ordini == null){
        response.sendRedirect("./AreaUtente");
        return;
    }

    // Check if 'showAll' parameter is set to display all orders
    boolean showAll = "true".equals(request.getAttribute("showAll"));

%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Area Utente</title>
    <link rel="stylesheet" type="text/css" href="./css/AreaUtente.css">
</head>
<body>
<div class="area-utente-body">
    <div class="container">
        <h1>Area Utente</h1>
        
        <div class="user-details">
            <p><strong style="font-size:20px">Benvenuto!</strong> <%= user.getEmail() %></p>
        </div>
        
        <div class="content">
            <!-- Lista degli ordini a sinistra -->
            <div class="orders-list">
                <h3>I Tuoi Ordini</h3>
                
                <!-- Filtro per anno -->
                <form method="post" action="AreaUtente" class="date-filter-form">
                    <input type="hidden" name="action" value="FilterByYear">
                    <label for="order-year">Filtra per anno:</label>
                    <select id="order-year" name="order-year">
                        <option value="">Tutti gli anni</option>
                        <%
                            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                            for (int year = currentYear; year >= 2000; year--) {
                        %>
                        <option value="<%= year %>" <%= request.getParameter("order-year") != null && request.getParameter("order-year").equals(String.valueOf(year)) ? "selected" : "" %>><%= year %></option>
                        <%
                            }
                        %>
                    </select>
                    <button type="submit" class="btn">Filtra</button>
                    <a href="AreaUtente.jsp" class="reset-btn">Reset</a>
                </form>
                
                <%
                    if (lista_ordini != null && !lista_ordini.isEmpty()) {
                        Map<Integer, String> indirizziMap = (Map<Integer, String>) request.getAttribute("indirizziMap");
                        int count = 0;
                        for (OrdineBean Order : lista_ordini) {
                            // Display the first 3 orders or all if 'showAll' is true
                            if (!showAll && count >= 3) {
                                break;
                            }
                %>
                            <div class="order-item">
                                <h4>Ordine: #<%= Order.getIdOrdine() %></h4>
                                <p>Ordine effettuato in data: <%= DateUtils.formatTimestamp(Order.getData()) %></p>                                
                                <p>Totale: <%= Order.getImportoFormatted() %>€</p>
                                <label for="Indirizzo_spedizione">
                                    <p>Spedito a: <%= indirizziMap.get(Order.getIdOrdine()) %> </p>
                                </label>
                                <div class="btn-container">
                                	<a href="DettaglioOrdine?action=showDetails&rif_id_ordine=<%=Order.getIdOrdine()%>" class="btn view-order">Visualizza Ordine</a>                        
                               		<!-- Aggiungi qui il link per visualizzare la fattura -->
                                	<a href="Fattura?action=view-invoice&rif_id_ordine=<%=Order.getIdOrdine()%>" class="view-invoice">Visualizza Fattura</a>
                               </div>
                            </div>
                <%
                            count++;
                        }

                        // Show the "View All Orders" button if there are more than 3 orders and 'showAll' is not set
                        if (!showAll && lista_ordini.size() > 3) {
                %>
                        <form method="post" action="AreaUtente">
                            <input type="hidden" name="action" value="showAllOrders">
                            <button type="submit" class="btn">Mostra tutti gli ordini</button>
                        </form>
                <%
                        }
                    } else {
                %>
                    <span>Non Sono stati effettuati ordini</span>
                <%
                    }
                %>
                
            </div>

            <!-- Azioni dell'utente a destra -->
            <div class="user-actions">
                <a class="User-Area-btn" href="AreaUtente?action=viewAddress">Indirizzi di Spedizione</a>
                <a class="User-Area-btn" href="AreaUtente?action=viewPaymentMethod">Metodi di Pagamento</a>
                <a class="User-Area-btn" href="Utente?action=logout">Log Out</a>
            </div>
        </div>
    </div>   
</div>    
    <%@include file="Footer.jsp" %>
</body>
</html>
