<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, SwitchAndKeysModel.UtenteBean"%>
<%@include file="SessionPopup.jsp" %>
<%
    UtenteBean user = (UtenteBean) session.getAttribute("loggedInUser");
	 
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SwitchAndKeys</title>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Reddit+Sans:ital,wght@0,200..900;1,200..900&display=swap">
    <link rel="stylesheet" href="css/Header.css">
    <link rel="icon" type="image/x-icon" href="/favicon.ico">
</head>
<body>
<header>
    <div class="nav-left">
        <ul class="navbar">
            <li><a href="Home.jsp"><img src="imgs/logo4.jpg" alt="Logo" class="logo"></a></li>
            <li>
                <div class="dropdown">
                    <button onclick="generalMenuFunction()" class="dropbtn">≡</button>
                    <div id="myDropdown" class="dropdown-content">
                        <a class="animated-link" href="SetHeaderServlet?action=Header">Catalogo</a>
                        <a class="animated-link" href="Supporto.jsp">Supporto</a>
                    </div>
                </div>
            </li>
            <li><a class="animated-link" href="SetHeaderServlet?action=Header">Catalogo</a></li>
            <li><a class="animated-link" href="Supporto.jsp">Supporto</a></li>
        </ul>
    </div>
    <div class="nav-right">
        <ul class="navbar">

			<li>
			    <div class="search-container" id="search-container">
			    	<div>
				    	<input type="text" name="search-bar" id="search-bar" placeholder="Cerca.." autocomplete="off" onkeyup="fetchSuggestions()">
				        <div id="suggestions" class="suggestions"></div>			    	
			    	</div>
			        <img alt="Ricerca" onclick="toggleSearchBar()" src="imgs/barra/icons8-ricerca-128.png" class="search-icon">			        
			    </div>
			</li>


            <li><a href="Carrello.jsp"><img alt="Carrello" src="imgs/barra/icons8-carrello-64.png" class="icon"></a></li>
            <%
               
                if (user != null) {
                    if (!user.isAdmin()) {
            %>
                        <li><a href="AreaUtente.jsp"><img alt="Area Utente" src="imgs/barra/icons8-utente-64.png" class="icon"></a></li>
            <%
                    } else {
            %>
                        <li><a href="AdminDashboard.jsp"><img alt="Area Utente" src="imgs/barra/icons8-utente-64.png" class="icon"></a></li>
            <%
                    }
                } else {
            %>
                    <li><a href="LoginView.jsp"><img alt="Area Utente" src="imgs/barra/icons8-utente-64.png" class="icon"></a></li>
            <%
                }
            %>
        </ul>
    </div>
</header>


<script src="js/mobile-nav.js"></script>
<script src="js/search-bar.js"></script>

</body>
</html>
