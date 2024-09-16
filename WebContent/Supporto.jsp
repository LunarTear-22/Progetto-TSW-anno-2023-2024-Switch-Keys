<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="Header.jsp" %>
<%@include file="SessionPopup.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Supporto - SwitchAndKeys</title>
    <link rel="stylesheet" type="text/css" href="./css/Supporto.css">
</head>
<body>
    <div class="support-container">
        <h1>Supporto</h1>
        <div class="contact-section">
            <h2>Contattaci</h2>
            <p>Se hai bisogno di assistenza per il tracciamento del tuo ordine o per qualsiasi domanda sui nostri prodotti, siamo qui per aiutarti!</p>
            <p>Email: <a href="mailto:support@switchandkeys.com">support@switchandkeys.com</a></p>
            <p>Telefono: <a href="tel:+390123456789">+39 0123 456 789</a></p>
        </div>
        <div class="form-section">
            <h2>Richiedi Assistenza</h2>
            <form id="supportForm">
                <label for="name">Nome:</label>
                <input type="text" id="name" name="name" required>
                
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" required>
                
                <label for="orderNumber">Numero Ordine:</label>
                <input type="text" id="orderNumber" name="orderNumber" required>
                
                <label for="message">Messaggio:</label>
                <textarea id="message" name="message" rows="5" required></textarea>
                
                <button type="submit" class="cta-button">Invia Richiesta</button>
            </form>
        </div>
    </div>

    <!-- Popup di conferma -->
    <div id="support-success-popup" class="support-popup">
        <div class="support-popup-content">
            <span class="support-popup-close" onclick="closeSupportPopup()">&times;</span>
            <p>Richiesta inviata con successo!</p>
        </div>
    </div>
    
    <%@include file="Footer.jsp" %>

    <!-- Inserisci lo script alla fine del body -->
    <script src="js/Supporto.js"></script>
</body>
</html>