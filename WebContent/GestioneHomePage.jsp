<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="Header.jsp" %>
<%@include file="SessionPopup.jsp" %>

<%@ page contentType="text/html; charset=UTF-8" import="java.util.*, SwitchAndKeysModel.ProdottoBean" %>

<%
	if (user == null || !user.isAdmin()) {
	    response.sendRedirect("./403.jsp");
	    return;
	}
    // Recupera i prodotti dalla sessione
    Collection<ProdottoBean> prodotti = (Collection<ProdottoBean>) session.getAttribute("prodotti");
    if (prodotti == null) {
        response.sendRedirect("./prodotto?action=setProdotti");
        return;
    }

    // Recupera i prodotti in evidenza dalla richiesta (tramite la servlet)
    List<ProdottoBean> featuredProdotti = (List<ProdottoBean>) request.getAttribute("featuredProdotti");

    if (featuredProdotti == null){
            response.sendRedirect("./HomeServlet?page=GestioneHomePage");
            return;
        }

    // Creazione di set per prodotti in evidenza
    Set<Integer> featuredProdottiIds = new HashSet<>();

    for (ProdottoBean p : featuredProdotti) {
        featuredProdottiIds.add(p.getIdProdotto());
    }
   
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestione Homepage</title>
    <link rel="stylesheet" href="css/GestioneHomePage.css">
    <script>
        function updateProductIds() {
            // Recupera i checkbox e i campi nascosti
            const featuredCheckboxes = document.querySelectorAll("input[name='featured']");
            const featuredIds = [];           

            featuredCheckboxes.forEach(checkbox => {
                if (checkbox.checked) {
                    featuredIds.push(checkbox.value);
                }
            });

            document.getElementById("featuredIds").value = featuredIds.join(",");
        }
    </script>
</head>
<body>
    <h1>Gestione Homepage</h1>

    <form action="HomeServlet" method="post" enctype="multipart/form-data" onsubmit="updateProductIds()">
        <input type="hidden" name="action" value="updateHomepage">
        <table border="1">
            <thead>
                <tr>
                    <th>Immagine</th>
                    <th>Codice del prodotto</th>
                    <th>Nome</th>
                    <th>Descrizione</th>
                    <th>Prezzo</th>
                    <th>In Evidenza</th>
                </tr>
            </thead>
            <tbody>
                <% if (prodotti != null && prodotti.size() != 0) {
                    for (ProdottoBean prodotto : prodotti) { %>
                        <tr>
                            <td>
                                <img src="Immagine?action=GetImmagine&rif_id_prodotto=<%= prodotto.getIdProdotto() %>"
                                     onerror="this.src='./imgs/Catalogo/No_image.jpeg'"
                                     style="width:100px;height:100px;">
                            </td>
                            <td><%= prodotto.getIdProdotto() %></td>
                            <td><%= prodotto.getNome() %></td>
                            <td><%= prodotto.getDescrizione() %></td>
                            <td><%= prodotto.getPrezzoFormatted() %> €</td>
                            <td>
                                <input type="checkbox" name="featured" value="<%= prodotto.getIdProdotto() %>" 
                                    <%= featuredProdottiIds.contains(prodotto.getIdProdotto()) ? "checked" : "" %>>
                            </td>
                        </tr>
                <% } } else { %>
                        <tr>
                            <td colspan="6">Non sono presenti prodotti nel catalogo</td>
                        </tr>
                <% } %>
            </tbody>
        </table>

        <!-- Campi nascosti per passare gli ID dei prodotti -->
        <input type="hidden" id="featuredIds" name="featuredIds" value="">
    
        <button type="submit">Aggiorna Homepage</button>
    </form>

    <%@include file="Footer.jsp" %>
</body>
</html>
