<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="Header.jsp" %>
<%@ include file="SessionPopup.jsp" %>
<%@ page import="java.util.*,SwitchAndKeysModel.*" %>

<%
    CarrelloBean carrello;
    if (user != null) {
        // Utente loggato
        carrello = (CarrelloBean) request.getAttribute("carrello");
        if (carrello == null) {
            response.sendRedirect("./Carrello");    
            return;
        }
    } else {
        // Utente non loggato, utilizza carrello di sessione
        carrello = (CarrelloBean) session.getAttribute("carrello");
        if (carrello == null) {
            carrello = new CarrelloBean();
            session.setAttribute("carrello", carrello);
        }
    }

    // Recupera la mappa che indica se un prodotto è non disponibile
    Map<Integer, Boolean> noMoreProductMap = (Map<Integer, Boolean>) request.getSession().getAttribute("NoMoreProductMap");
    if (noMoreProductMap == null) {
        noMoreProductMap = new HashMap<>();
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Carrello</title>
    <link rel="stylesheet" type="text/css" href="css/Carrello.css">
</head>
<body>
<div class="cart-container">
    <!-- Carrello -->
    <div class="cart-items">
        <h2 style="font-size:32px">Carrello</h2>
        <% if (carrello != null && carrello.getProdotti() != null && !carrello.getProdotti().isEmpty()) { %>
            <form action="Carrello" method="post">
                <input type="hidden" name="action" value="updateQuantity">
                <table class="cart-table">
                    <%
                        Collection<ProdottoBean> prodottiCarrello = carrello.getProdotti();
                        for (ProdottoBean prodottoCarrello : prodottiCarrello) {
                            // Controlla se il prodotto è contrassegnato come non disponibile per l'acquisto
                            Boolean noMoreProduct = noMoreProductMap.getOrDefault(prodottoCarrello.getIdProdotto(), false);
                    %>
                    <tr>
                        <td><img src="Immagine?action=GetImmagine&rif_id_prodotto=<%= prodottoCarrello.getIdProdotto() %>" onerror="this.src='./imgs/Catalogo/No_image.jpeg'" class="cart-image"></td>
                        <td><span class="product-name"><%= prodottoCarrello.getNome() %></span></td>
                        <td>
                            <div class="quantity-container">
                                <button class="quantity-btn" type="submit" 
                                        formaction="Carrello?action=deleteC&id_prodotto=<%= prodottoCarrello.getIdProdotto() %>">-</button>
                                <input name="quantita_carrello_<%= prodottoCarrello.getIdProdotto() %>" type="text" class="quantity-input" value="<%= prodottoCarrello.getQuantita() %>" readonly>
                                
                                <% if (!noMoreProduct) { %>
                                    <button class="quantity-btn" type="submit" 
                                            formaction="Carrello?action=addC&id_prodotto=<%= prodottoCarrello.getIdProdotto() %>">+</button>
                                <% } else { %>       
                                    <button id="quantity-btn-disabled-<%= prodottoCarrello.getIdProdotto() %>" type="submit" 
                                            formaction="Carrello?action=addC&id_prodotto=<%= prodottoCarrello.getIdProdotto() %>"
                                            disabled title="Prodotto non più disponibile per l'acquisto.">+</button>
                                <% } %>
                            </div>
                        </td>
                        <td class="price-container"><%= prodottoCarrello.getPrezzoFormatted() %> €</td>
                        <td><a class="remove-btn" href="Carrello?action=remove&id_prodotto=<%= prodottoCarrello.getIdProdotto() %>">Rimuovi</a></td>
                    </tr>
                    <% } %>
                </table>
            </form>
        <% } else { %>
            <div class="cart-table">
                <p class="carrello_vuoto"><strong>Il Carrello è vuoto</strong></p>
            </div>
        <% } %>
    </div>
    
    <% if (carrello != null && carrello.getProdotti() != null && !carrello.getProdotti().isEmpty()) { %>
        <!-- Riepilogo carrello -->
        <div class="cart-summary">
            <form action="Carrello" method="post">
                <input type="hidden" name="action" value="Checkout">
                <h3 style="font-size:22px">Riepilogo Ordine</h3>
                <p><strong style="font-size:18px">Sub-Totale: </strong><span class="price-container" id="total-price"><%= carrello != null ? carrello.getPrezzoTotaleFormatted() : "0.00" %> €</span></p>
                <p><strong style="font-size:18px">Costi Spedizione: </strong><span class="price-container" id="shipping-cost">5.00 €</span></p>
                <p><strong style="font-size:18px">Totale: </strong><span class="price-container" id="final-total"><%= carrello != null ? carrello.getPrezzoTotaleFormatted(carrello.getPrezzoTotale() + 5) : "5.00" %> €</span></p>
                
                <% session.setAttribute("carrello", carrello); %>
                
                <button type="submit" class="checkout-btn">Checkout</button>
            </form>
        </div>
    <% } %>
</div>

<%@ include file="Footer.jsp" %>
</body>
</html>
