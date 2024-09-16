<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="Header.jsp" %>
<%@ include file="SessionPopup.jsp" %>


<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="./css/log.css">
</head>
<body>
<div class="log-container">
    <div class="main-wrapper">
        <div class="content-wrapper">
            <div class="container">
                <div class="item">
                    <h2 class="text-logo">SwitchAndKeys</h2>
                    <div class="text-item">
                        <h2>Benvenuti <br><span>nel nostro Store!</span></h2>
                    </div>
                </div>
            </div>

            <div class="login-section">
                <div class="form-box login">
                    <form name="loginForm" action="Utente" method="post" onsubmit="return validateForm()">
                        <input type="hidden" name="action" value="login">
                        <h2>Accedi</h2>
                        <div class="input-box">
                            <input id="emailInput" name="email" type="email" required placeholder="Email">
                            <span class="icon"><img alt="Username" src="imgs/login-reg/icons8-user-64.png"></span>                           
                        </div>
                        <div class="input-box">
                            <input id="passwordInput" name="password" type="password" maxlength="16" required placeholder="Password" >
                            <span class="icon"><img alt="Password" src="imgs/login-reg/icons8-chiave-64.png"></span>
                        </div>
                        <button class="btn">Accedi</button>
                        <div class="create-account">
                            <p>Non sei registrato? <a href="RegistrazioneView.jsp">Crea un nuovo account</a></p>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Popup di successo -->
<div id="successPopup" class="popup">
    <div class="popup-content">
        <span class="close" onclick="closePopup('successPopup')">&times;</span>
        <h2>Login effettuato con successo!</h2>
        <button onclick="redirectToHome()">Vai alla Home</button>
    </div>
</div>

<!-- Popup di errore -->
<div id="errorPopup" class="popup">
    <div class="popup-content">
        <span class="close" onclick="closePopup('errorPopup')">&times;</span>
        <h2>Login fallito. Riprova.</h2>
    </div>
</div>

<%@include file="Footer.jsp" %>

<script src="js/login.js"></script>
</body>
</html>
