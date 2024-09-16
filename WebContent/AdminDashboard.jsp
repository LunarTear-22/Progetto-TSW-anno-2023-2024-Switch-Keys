<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="Header.jsp" %>
<%@include file="SessionPopup.jsp" %>

<%

if (user == null || !user.isAdmin()) {
    response.sendRedirect("./403.jsp");
    return;
}

%>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="css/AdminDashboard.css">
    <title>Admin Dashboard</title>
</head>
<body>
    <div class="dashboard-container">
        <h1>Admin Dashboard</h1>
        <div class="button-container">
            <a href="SetHeaderServlet?action=NotHeader" class="dashboard-button">Gestione Catalogo</a>
            <a href="GestioneOrdini.jsp" class="dashboard-button">Gestione Ordini</a>
            <a href="GestioneHomePage.jsp" class="dashboard-button">Gestione HomePage</a>
            <a href="GestioneUtenti.jsp" class="dashboard-button">Gestione Utenti</a>
            <a href="Utente?action=logout" class="dashboard-button">Logout</a>
            
        </div>
    </div>
     <%@include file="Footer.jsp" %>
</body>
</html>
