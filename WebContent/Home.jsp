<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="Header.jsp" %>
<%@include file="SessionPopup.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" import="java.util.*,SwitchAndKeysModel.ProdottoBean" %>

<%

Collection<?> featuredProdotti = (Collection<?>) request.getAttribute("featuredProdotti");
if (featuredProdotti == null) {
    response.sendRedirect("./HomeServlet?page=home");
    return;
}

%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SwitchAndKeys</title>
    <link rel="stylesheet" type="text/css" href="./css/Home.css">
</head>
<body>
    <div class="main-content">
        <!-- Slideshow Container -->
        <div class="slideshow-container">
            <div class="mySlides fade">
               <a href="prodotto?action=read&id_prodotto=1"><img src="imgs/home/yearofdragon.png" style="width:100%"></a>
                <div class="text">MOD 007 V3 HE Year of Dragon - Akko</div>
            </div>
            <div class="mySlides fade">
                <a href="prodotto?action=read&id_prodotto=19"><img src="imgs/home/asuka.png" style="width:100%"></a>
                <div class="text">ROG Strix Scope RX EVA-02 Edition</div>
            </div>
            <div class="mySlides fade">
                <a href="prodotto?action=read&id_prodotto=20"><img src="imgs/home/keychronc1.png" style="width:100%"></a>
                <div class="text">Keychron C1 Pro</div>
            </div>
            <div class="mySlides fade">
                <a href="prodotto?action=read&id_prodotto=21"><img src="imgs/home/meletrix.png" style="width:100%"></a>
                <div class="text">Meletrix ZOOM65 V3</div>
            </div>
            <div class="mySlides fade">
                <a href="prodotto?action=read&id_prodotto=22"><img src="imgs/home/gamakay.png" style="width:100%"></a>
                <div class="text">Gamakay TK75 HE</div>
            </div>
            <div class="mySlides fade">
                <a href="prodotto?action=read&id_prodotto=23"><img src="imgs/home/octopuskeycaps.png" style="width:100%"></a>
                <div class="text">PolyCaps Octopus Double-shot PBT Keycaps</div>
            </div>
            
            <!-- Pulsanti di navigazione -->
            <a class="prev" onclick="plusSlides(-1)">&#10094;</a>
            <a class="next" onclick="plusSlides(1)">&#10095;</a>
        
        </div>

        <br>

        <section class="featured-products">
            <h2>Prodotti in evidenza</h2>
            <div class="product-grid">
                <% 
                    // Recupera i prodotti in evidenza dalla richiesta
                    if (featuredProdotti != null && !featuredProdotti.isEmpty()) {
                        for (Object obj : featuredProdotti) { 
                            ProdottoBean prodotto = (ProdottoBean) obj;
                %>
                    <div class="product-card">
                        <img class="product-image" src="Immagine?action=GetImmagine&rif_id_prodotto=<%= prodotto.getIdProdotto() %>" alt="<%= prodotto.getNome() %>" onerror="this.src='./imgs/Catalogo/No_image.jpeg'" >
                        <h3><%= prodotto.getNome() %></h3>
                        <p><%= prodotto.getPrezzoFormatted() %>€</p>
                        <a href="prodotto?action=read&id_prodotto=<%= prodotto.getIdProdotto() %>" class="cta-button">Vedi dettagli</a>
                    </div>
                <% 
                        } 
                    } else { 
                %>
                    <p>Nessun prodotto in evidenza al momento.</p>
                <% 
                    } 
                %>
            </div>
        </section>
        
        
        <%@include file="Footer.jsp" %>
        
    </div>
    
    <script src="./js/slideshow.js"></script>
</body>
</html>
