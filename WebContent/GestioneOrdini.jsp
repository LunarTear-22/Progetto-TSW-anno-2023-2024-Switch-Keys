<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="Header.jsp" %>
<%@ page import="java.util.*, SwitchAndKeysModel.*, SwitchAndKeysUtil.*" %>

<%
    if (user == null || !user.isAdmin()) {
        response.sendRedirect("./403.jsp"); 
        return;
    }
    
    Collection<OrdineBean> lista_ordini = (Collection<OrdineBean>) request.getAttribute("lista_ordini");
    if(lista_ordini == null){
        response.sendRedirect("./Ordine");
        return;
    }
    
    // Prepare a list of users for the filter dropdown
    Collection<UtenteBean> utenti = (Collection<UtenteBean>) request.getAttribute("utenti");
    		
    // Imposta la data di inizio standard
    String defaultDataInizio = "2020-01-01";

    // Ottieni la data attuale nel formato YYYY-MM-DD
    java.time.LocalDate currentDate = java.time.LocalDate.now();
    String defaultDataFine = currentDate.toString();

    // Ottieni i valori passati dai parametri della richiesta (se presenti)
    String dataInizio = request.getParameter("data_inizio") != null ? request.getParameter("data_inizio") : defaultDataInizio;
    String dataFine = request.getParameter("data_fine") != null ? request.getParameter("data_fine") : defaultDataFine;

		
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestione Ordini</title>
    <link rel="stylesheet" type="text/css" href="css/GestioneOrdini.css">
</head>
<body>
<div class="admin-container">
    <h1 class="admin-title">Gestione Ordini</h1>

    <div class="filter-container">
        <form method="post" action="Ordine?action=FilterOrders">
            <label for="data_inizio">Data Inizio:</label>
            <input type="date" id="data_inizio" name="data_inizio" value="<%= dataInizio %>">
            
            <label for="data_fine">Data Fine:</label>
            <input type="date" id="data_fine" name="data_fine" value="<%= dataFine %>">
            
            <label for="utente">Utente:</label>
            <select id="utente" name="utente">
                <option value="tutti">Tutti gli utenti</option>
                <%
                    if (utenti != null) {
                        for (UtenteBean utente : utenti) {
                            String selected = "";
                            if (utente.getEmail().equals(request.getParameter("utente"))) {
                                selected = "selected";
                            }
                            %>
                            <option value="<%= utente.getEmail() %>" <%= selected %>><%= utente.getEmail() %></option>
                            <%
                        }
                    }
                %>
            </select>
            
            <button type="submit" class="filter-btn">Filtra</button>
            <a href="GestioneOrdini.jsp" class="reset-btn">Reset</a>
        </form>
    </div>

    <div class="orders-container">
        <table class="orders-table">
            <thead>
                <tr>
                    <th>ID Ordine</th>
                    <th>Data</th>
                    <th>Utente</th>
                    <th>Totale</th>
                </tr>
            </thead>
            <tbody>
                <%
                    if (lista_ordini != null && !lista_ordini.isEmpty()) {
                        for (OrdineBean ordine : lista_ordini) {
                %>
                            <tr>
                                <td>#<%= ordine.getIdOrdine() %></td>
                                <td><%= DateUtils.formatTimestamp(ordine.getData()) %></td>
                                <td><%= ordine.getRifUsernameCliente() %></td>
                                <td><%= ordine.getImportoFormatted()%> €</td>
                            </tr>
                <%
                        }
                    } else {
                %>
                    <tr>
                        <td colspan="4" class="no-orders">Nessun ordine trovato.</td>
                    </tr>
                <%
                    }
                %>
            </tbody>
        </table>
    </div>
</div>

<%@ include file="Footer.jsp" %>
</body>
</html>
