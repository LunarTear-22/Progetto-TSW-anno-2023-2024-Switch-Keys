<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8" import="java.util.*,SwitchAndKeysModel.ProdottoBean,SwitchAndKeysModel.*"%>
<%@include file="Header.jsp" %>
<%@include file="SessionPopup.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Catalogo Prodotti</title>
    <link rel="stylesheet" type="text/css" href="css/Shop.css">
</head>
<body>
    <h2 class="catalog-title">Catalogo Prodotti</h2>
    <div class="container">
        <div class="main-grid">
            <button class="filter-toggle" onclick="toggleFilters()">Filtri</button>
            <div class="filters">
                <h3>Filtri</h3>
                <form id="filterForm">
                    <label><input type="checkbox" name="category" value="Tastiera"> Tastiere</label><br>
                    <div class="sub-filters">
                        <label><input type="checkbox" name="size" value="65%"> 65%</label><br>
                        <label><input type="checkbox" name="size" value="75%"> 75%</label><br>
                        <label><input type="checkbox" name="size" value="80%"> 80%</label><br>
                        <label><input type="checkbox" name="size" value="100%"> 100%</label><br>
                    </div>
                    <label><input type="checkbox" name="category" value="Keycaps"> Keycaps</label><br>
                    <label><input type="checkbox" name="category" value="Switch"> Switch</label><br>
                    <label><input type="checkbox" name="category" value="Accessori"> Accessori</label><br>

                    <label for="priceRange">Prezzo: 0 - 500 €</label>
                    <input type="range" id="priceRange" name="prezzoMax" min="0" max="500" value="500" oninput="this.nextElementSibling.value = this.value">
                    <output>500</output> €<br>

                    <input type="submit" value="Applica Filtri">
                </form>
            </div>
            <div class="catalog-container">
                <div class="catalog-grid">
                    <% 
                    Collection<?> prodotti = (Collection<?>) request.getAttribute("prodotti");
                    if (prodotti != null && prodotti.size() != 0) {
                        Iterator<?> it = prodotti.iterator();
                        while (it.hasNext()) {
                            ProdottoBean bean = (ProdottoBean) it.next();
                    %>
                    <div class="product-card">
                        <a href="prodotto?action=read&id_prodotto=<%=bean.getIdProdotto()%>">
                            <img class="product-image" src="Immagine?action=GetImmagine&rif_id_prodotto=<%=bean.getIdProdotto()%>" onerror="this.src='./imgs/Catalogo/No_image.jpeg'" alt="<%= bean.getNome() %>">
                        </a>
                        <h3><%= bean.getNome() %></h3>
                        <p class="price"><%= bean.getPrezzoFormatted() %> €</p>

                        <% if (bean.getQuantita() > 0) { %>
                            <a href="prodotto?action=addC&id_prodotto=<%=bean.getIdProdotto()%>" class="btn">Aggiungi al Carrello</a>
                        <% } else { %>
                            <a href="#" class="btn disabled" onclick="return false;" title="Prodotto esaurito">Esaurito</a>
                        <% } %>
                    </div>
                    <% } %>
                    <% } else { %>
                    
                        <p class="no-product">Non sono presenti prodotti nel catalogo</p>
                    <% } %>
                </div>
            </div>
        </div>
    </div>
    <%@include file="Footer.jsp" %>
    
    <script src="js/filtriAjax.js"></script>
</body>
</html>
