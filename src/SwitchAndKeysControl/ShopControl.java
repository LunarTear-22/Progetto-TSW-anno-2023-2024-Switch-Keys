package SwitchAndKeysControl;

import java.io.IOException;
import java.util.Collection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import SwitchAndKeysModel.ProdottoBean;
import SwitchAndKeysModel.ProdottoModelDS;

@WebServlet("/Filtra")
public class ShopControl extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Recupera i parametri dalla richiesta
        String[] categories = request.getParameterValues("category");
        String[] sizes = request.getParameterValues("size");
        String prezzoMaxStr = request.getParameter("prezzoMax");
        int prezzoMax = (prezzoMaxStr != null && !prezzoMaxStr.isEmpty()) ? Integer.parseInt(prezzoMaxStr) : 500;

        // Crea un'istanza del modello per recuperare i dati
        ProdottoModelDS prodottoModel = new ProdottoModelDS();
        Collection<ProdottoBean> prodotti = prodottoModel.doRetrieveByFilters(categories, sizes, prezzoMax);

        // Imposta i prodotti come attributo della richiesta
        request.setAttribute("prodotti", prodotti);

        // Gestione della risposta basata sul tipo di richiesta
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            // Richiesta AJAX: restituisce solo il contenuto filtrato
            request.getRequestDispatcher("/ProdottiFiltrati.jsp").forward(request, response);
        } else {
            // Richiesta normale: restituisce la pagina completa
            request.getRequestDispatcher("/Shop.jsp").forward(request, response);
        }
    }
}
