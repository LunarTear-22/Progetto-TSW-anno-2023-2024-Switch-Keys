package SwitchAndKeysControl;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import SwitchAndKeysModel.*;
import SwitchAndKeysUtil.DateUtils;

@WebServlet("/Ordine")
@MultipartConfig
public class OrdineControl extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private OrdineModel ordineModel;
    private UtenteModel utenteModel;

    public OrdineControl() {
        ordineModel = new OrdineModel();
        utenteModel = new UtenteModel();
    }

   
	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UtenteBean user = (UtenteBean) request.getSession().getAttribute("loggedInUser");

        Collection<OrdineBean> lista_ordini = null;
        Collection<UtenteBean> lista_utenti = null;
        
        try {
                lista_ordini = ordineModel.doRetrieveAllOrders();
                request.setAttribute("lista_ordini", lista_ordini);
            
        } catch (SQLException e) {
            log("Errore durante il recupero degli ordini", e);
            response.sendRedirect("./500.jsp");
            return;
        }
        
        request.setAttribute("lista_ordini", lista_ordini);
        
        try {
			lista_utenti = utenteModel.doRetrieveAllUser();
		} catch (SQLException e) {
			e.printStackTrace();
		}
        System.out.println("Utenti: " + lista_utenti);
        request.setAttribute("utenti", lista_utenti);
        

        // Gestione delle azioni con switch
        String action = request.getParameter("action");
        if (action != null) {
            switch (action) {
            case "FilterOrders":
            	 // Ottieni i parametri dal form di filtraggio
                String dataInizioParam = request.getParameter("data_inizio");
                String dataFineParam = request.getParameter("data_fine");
                String utenteParam = request.getParameter("utente");
                if ("tutti".equals(utenteParam)) {
                    utenteParam = null;
                }

                // Parsing delle date usando DateUtils
                LocalDate dataInizio = dataInizioParam != null && !dataInizioParam.isEmpty() ? LocalDate.parse(dataInizioParam) : null;
                LocalDate dataFine = dataFineParam != null && !dataFineParam.isEmpty() ? LocalDate.parse(dataFineParam) : null;

                // Converti LocalDate a Date per interagire con il database
                Date dataInizioDate = dataInizio != null ? DateUtils.toDate(dataInizio) : null;
                Date dataFineDate = dataFine != null ? DateUtils.toDate(dataFine) : null;

                try {
                    // Recupero degli ordini filtrati
                    lista_ordini = ordineModel.doRetrieveOrdersByDateRangeAndUser(dataInizioDate, dataFineDate, utenteParam);
                    request.setAttribute("lista_ordini", lista_ordini);
                } catch (SQLException e) {
                    log("Errore durante il filtraggio degli ordini", e);
                    response.sendRedirect("./500.jsp");
                    return;
                }

                request.getRequestDispatcher("/GestioneOrdini.jsp").forward(request, response);
                return;

               

                default:
                    // Azione di default se nessuna corrisponde
                    log("Azione non gestita: " + action);
                    response.sendRedirect("./404.jsp");
                    break;
            }
        }
        

        
        request.getRequestDispatcher("/GestioneOrdini.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
