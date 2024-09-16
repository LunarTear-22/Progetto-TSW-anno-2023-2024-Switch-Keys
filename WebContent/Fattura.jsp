<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*,SwitchAndKeysModel.*,SwitchAndKeysUtil.*"%>
<%
    UtenteBean user = (UtenteBean) session.getAttribute("loggedInUser");
    if (user == null) {
        response.sendRedirect("./403.jsp");
        return;
    }

    DettaglioOrdineBean dettaglioOrdine = (DettaglioOrdineBean) request.getAttribute("dettaglio_ordine");
    Collection<ProdottoBean> prodottiOrdine = (Collection<ProdottoBean>) request.getAttribute("lista_dettaglio");
    OrdineBean ordine = (OrdineBean) request.getAttribute("ordine");

    if (dettaglioOrdine == null || prodottiOrdine == null || ordine == null) {
        response.sendRedirect("./Fattura");
        return;
    }

    double totaleIVA = 0.0;
    double totaleEsclusaIVA = 0.0;

    for (ProdottoBean prodotto : prodottiOrdine) {
        totaleIVA += prodotto.getImportoIva() * prodotto.getQuantita();
        totaleEsclusaIVA += prodotto.getPrezzo() * prodotto.getQuantita();
    }
%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" lang="it">
<head>
    <meta charset="UTF-8" />
    <title>Fattura</title>
    <link rel="stylesheet" type="text/css" href="http://localhost/SwitchAndKeys/css/Fattura.css" />
</head>
<body>
    <div class="invoice-container">
        <div class="invoice-header">
            <h1>Fattura</h1>
            <img src="http://localhost/SwitchAndKeys/imgs/fattura/logofattura.png" alt="Logo" class="invoice-logo" />
        </div>

        <div class="invoice-details-container">
            <div class="invoice-details">
                <h3>Dati Cliente</h3>
                <p>
                    <%
                        String indirizzo = dettaglioOrdine.getIndirizzoSpedizione();
                        indirizzo = indirizzo.replaceFirst(",", "<br />");
                        indirizzo = indirizzo.replaceFirst(",", "<br />");
                    %>
                    <%= indirizzo %>
                </p>
            </div>

            <div class="invoice-details">
                <h3>Dettagli Ordine</h3>
                <p>Ordine: # <%= ordine.getIdOrdine() %><br />Data: <%= DateUtils.formatTimestamp(ordine.getData()) %></p>
            </div>

            <div class="invoice-details">
                <h3>Venduto da</h3>
                <p>SwitchAndKeys<br />Corso Vittorio Emanuele 120,<br />84122 Salerno,<br />Italia</p>
            </div>
        </div>

        <table>
            <thead>
                <tr>
                    <th>Prodotto</th>
                    <th>Quantità</th>
                    <th>Prezzo Unitario</th>
                    <th>Totale</th>
                </tr>
            </thead>
            <tbody>
                <% for (ProdottoBean prodotto : prodottiOrdine) { %>
                <tr>
                    <td class="name"><%= prodotto.getNome() %></td>
                    <td class="quantity"><%= prodotto.getQuantita() %></td>
                    <td class="price"><%= prodotto.getPrezzoFormatted() %> &euro;</td>
                    <td class="price"><%= String.format("%.2f", prodotto.getPrezzoConIva() * prodotto.getQuantita()) %> &euro;</td>
                </tr>
                <% } %>
            </tbody>
        </table>

        <div class="order-summary-content">
            <p><strong>Subtotale:</strong> <%= String.format("%.2f", dettaglioOrdine.getImportoTotale() - dettaglioOrdine.getCostiSpedizione()) %> &euro;</p>
            <p><strong>Subtotale IVA esclusa:</strong> <%= String.format("%.2f", totaleEsclusaIVA) %> &euro;</p>
            <p><strong>IVA:</strong> <%= String.format("%.2f", totaleIVA) %> &euro;</p>
            <p><strong>Costi di Spedizione:</strong> <%= dettaglioOrdine.getCostiSpedizioneFormatted() %> &euro;</p>
            <p><strong>Totale:</strong> <%= dettaglioOrdine.getImportoTotaleFormatted() %> &euro;</p>
        </div>
    </div>
</body>
</html>
