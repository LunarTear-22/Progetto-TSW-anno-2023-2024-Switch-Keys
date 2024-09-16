<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*, SwitchAndKeysModel.ProdottoBean" %>
<link rel="stylesheet" type="text/css" href="css/Shop.css">
<div class="catalog-grid">
    <% 
    Collection<?> prodotti = (Collection <?>) request.getAttribute("prodotti");
    if (prodotti != null && prodotti.size() != 0) {
		Iterator<?> it = prodotti.iterator();
		while (it.hasNext()) {
			ProdottoBean bean = (ProdottoBean) it.next();
    %>
    <div class="product-card">
        <a href="prodotto?action=read&id_prodotto=<%=bean.getIdProdotto()%>">
            <img src="./GetImmagine?rif_id_prodotto=<%=bean.getIdProdotto()%>" onerror="this.src='./imgs/Catalogo/No_image.jpeg'" alt="<%= bean.getNome() %>">
        </a>
        <h3><%= bean.getNome() %></h3>
        <p class="price"><%= bean.getPrezzoFormatted() %> €</p>
        <a href="prodotto?action=addC&id_prodotto=<%=bean.getIdProdotto()%>" class="btn">Aggiungi al Carrello</a>
    </div>
    <% 
        }
    } else {
    %>
    	<p>Non sono presenti prodotti nel catalogo</p>
    <% 
    } 
    %>
</div>
