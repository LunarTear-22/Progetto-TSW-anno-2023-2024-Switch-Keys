package SwitchAndKeysControl;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import SwitchAndKeysModel.*;

@WebServlet("/Checkout")
public class CheckoutControl extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private AreaUtenteModel AreaUtenteModel;
    private OrdineModel OrdineModel;
    private PagamentoModel PagamentoModel;
    private SpedizioneModel SpedizioneModel;
    private CarrelloModel CarrelloModel;
    private ProdottoModelDS ProdottoModel;
    private DettaglioOrdineModel DettaglioOrdineModel;
    private SpecificaModel SpecificaModel;

    public CheckoutControl() {
        super();
        AreaUtenteModel = new AreaUtenteModel();
        OrdineModel = new OrdineModel();
        PagamentoModel = new PagamentoModel();
        SpedizioneModel = new SpedizioneModel();
        CarrelloModel = new CarrelloModel();
        ProdottoModel = new ProdottoModelDS();
        DettaglioOrdineModel = new DettaglioOrdineModel();
        SpecificaModel = new SpecificaModel();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        UtenteBean user = (UtenteBean) request.getSession().getAttribute("loggedInUser");
        String username = user.getEmail();

        try {
            if (action != null) {
                switch (action) {
                    case "selectAddress":
                        int rif_id_indirizzo = Integer.parseInt(request.getParameter("rif_id_indirizzo"));
                        IndirizzoSpedizioneBean Indirizzo_Scelto = AreaUtenteModel.doRetrieveAddressByKey(rif_id_indirizzo, username);
                        request.getSession().setAttribute("indirizzo_scelto", Indirizzo_Scelto);
                        request.getRequestDispatcher("/Checkout.jsp").forward(request, response);
                        return;

                    case "selectPaymentMethod":
                        int rif_id_metodo = Integer.parseInt(request.getParameter("rif_id_metodo"));
                        MetodoPagamentoBean Metodo_Scelto = AreaUtenteModel.doRetrieveMethodByKey(rif_id_metodo, username);
                        request.getSession().setAttribute("metodo_scelto", Metodo_Scelto);
                        request.getRequestDispatcher("/Checkout.jsp").forward(request, response);
                        return;

                    case "confirmOrder":
                        double TotaleCarrello = Double.parseDouble(request.getParameter("totale").replace(",", "."));
                        double CostiSpedizione = Double.parseDouble(request.getParameter("costi_spedizione").replace(",", "."));
                        MetodoPagamentoBean Metodo_Pagamento = (MetodoPagamentoBean) request.getSession().getAttribute("metodo_scelto");
                        IndirizzoSpedizioneBean Indirizzo_Spedizione = (IndirizzoSpedizioneBean) request.getSession().getAttribute("indirizzo_scelto");
                        CarrelloBean carrello = (CarrelloBean) request.getSession().getAttribute("carrello");
                        Collection<ProdottoBean> lista_prodotti = carrello.getProdotti();
                        
                        Timestamp data_attuale = new Timestamp(System.currentTimeMillis());

                        PagamentoBean pagamento = new PagamentoBean();
                        pagamento.setImporto(TotaleCarrello);
                        pagamento.setData(data_attuale);
                        pagamento.setRifIdMetodo(Metodo_Pagamento.getIdMetodo());
                        pagamento.setRifUsernameCliente(username);
                        PagamentoModel.doSavePayment(pagamento);

                        SpedizioneBean spedizione = new SpedizioneBean();
                        spedizione.setIdIndirizzo(Indirizzo_Spedizione.getIdIndirizzo());
                        spedizione.setCosti(CostiSpedizione);
                        spedizione.setData(data_attuale);
                        spedizione.setRifUsernameCliente(username); 
                        SpedizioneModel.doSaveSpedizione(spedizione);

                        PagamentoBean Ultimo_Pagamento = PagamentoModel.doRetrieveLastPaymentByUser(username);
                        SpedizioneBean Ultima_Spedizione = SpedizioneModel.doRetrieveLastSpedizioneByUser(username);

                        OrdineBean Ordine = new OrdineBean();
                        Ordine.setImporto(TotaleCarrello);
                        Ordine.setData(data_attuale);
                        Ordine.setRifUsernameCliente(username);
                        Ordine.setRifIdPagamento(Ultimo_Pagamento.getIdPagamento());
                        Ordine.setRifIdSpedizione(Ultima_Spedizione.getIdSpedizione());
                        OrdineModel.doSaveOrder(Ordine);
                        
                        
                        OrdineBean OrdineEffettuato = OrdineModel.doRetrieveLastOrderByUser(username);
                        
                        DettaglioOrdineBean Dettaglio = new DettaglioOrdineBean();
                        Dettaglio.setQuantitaTotale(carrello.getQuantitaTotaleProdotti());
                        Dettaglio.setImportoTotale(TotaleCarrello);
                        Dettaglio.setCostiSpedizione(CostiSpedizione);
                        Dettaglio.setIndirizzoSpedizione(Indirizzo_Spedizione.getEstremo_Spedizione());
                        if(Metodo_Pagamento.getTipo().equals("PayPal")) {
                        	Dettaglio.setMetodoPagamento("PayPal");
                        }else if(Metodo_Pagamento.getTipo().equals("Carta di Credito")) {
                        	Dettaglio.setMetodoPagamento(Metodo_Pagamento.getUltimiQuattroNumeri());
                        }
                        Dettaglio.setRifIdOrdine(OrdineEffettuato.getIdOrdine());
                        
                        DettaglioOrdineModel.doSave(Dettaglio);
                        

                        
                        for(ProdottoBean p : lista_prodotti) {
                        	SpecificaBean ProdottoSpecificato = new SpecificaBean();
                        	ProdottoSpecificato.setRifIdProdotto(p.getIdProdotto());
                        	ProdottoSpecificato.setNome_prodotto(p.getNome());
                        	ProdottoSpecificato.setIvaApplicata(p.getIva());
                        	ProdottoSpecificato.setPrezzoUnitario(p.getPrezzo());
                        	ProdottoSpecificato.setQuantita(p.getQuantita());
                        	DettaglioOrdineBean DettaglioOrdine = DettaglioOrdineModel.doRetrieveDetailsForOrder(OrdineEffettuato.getIdOrdine());
                        	ProdottoSpecificato.setRifIdDettaglio(DettaglioOrdine.getIdDettaglio());
                        	SpecificaModel.doSave(ProdottoSpecificato);
                        	ProdottoModel.aggiornaQuantita(p.getIdProdotto(), p.getQuantita());
                        }
                        
                        CarrelloModel.emptyCart(user);
                        
                        
                        
                        request.setAttribute("orderConfirmed", true);
                        request.getRequestDispatcher("/Checkout.jsp").forward(request, response);
                        return;

                    default:
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore di database: " + e.getMessage());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
