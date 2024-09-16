<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="Header.jsp" %>
<%@ include file="SessionPopup.jsp" %>
<%@ page import="SwitchAndKeysModel.UtenteBean" %>
<%@ page import="java.util.List" %>

<%
    // Verifica se l'utente è loggato e se è un amministratore
    if (user == null || !user.isAdmin()) {
        response.sendRedirect("403.jsp"); // Reindirizza alla pagina di accesso negato
        return;
    }

    // Recupera la lista degli utenti dalla richiesta
    List<UtenteBean> userList = (List<UtenteBean>) request.getAttribute("userList");
    if (userList == null) {
        response.sendRedirect("./Utente?action=viewUsers"); 
        return;
    }

%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestione Utenti</title>
	 <link rel="stylesheet" type="text/css" href="css/GestioneUtenti.css">
</head>
<body>

<h1>Gestione Utenti</h1>
<div class="table-container">
<table>
    <thead>
        <tr>
            <th>Email</th>
            <th>Nome</th>
            <th>Cognome</th>
            <th>Amministratore</th>
            <th>Azioni</th>
        </tr>
    </thead>
    <tbody>
        <% 
            // Itera sulla lista degli utenti e crea una riga per ciascuno
            if (userList != null && !userList.isEmpty()) {
                for (UtenteBean User : userList) {
                    String adminStatus = User.isAdmin() ? "Sì" : "No";
                    %>
                    <tr>
                        <td><%= User.getEmail() %></td>
                        <td><%= User.getNome() %></td>
                        <td><%= User.getCognome() %></td>
                        <td><%= adminStatus %></td>
                        <td>
                            <form action="Utente" method="post" style="display:inline;">
                                <input type="hidden" name="action" value="makeAdmin"/>
                                <input type="hidden" name="email" value="<%= User.getEmail() %>"/>
                                <% if (!User.isAdmin()) { %>
                                    <button type="submit">Rendi Admin</button>
                                <% } %>
                            </form>
                            <form action="Utente" method="post" style="display:inline;">
                                <input type="hidden" name="action" value="removeAdmin"/>
                                <input type="hidden" name="email" value="<%= User.getEmail() %>"/>
                                <% if (User.isAdmin()) { %>
                                    <button type="submit">Rimuovi Admin</button>
                                <% } %>
                            </form>
                        </td>
                    </tr>
                    <%
                }
            } else {
                %>
                <tr>
                    <td colspan="5">Nessun utente trovato.</td>
                </tr>
                <%
            }
        %>
    </tbody>
</table>
</div>
  <!-- Include Footer -->
        <%@include file="Footer.jsp" %>

</body>
</html>