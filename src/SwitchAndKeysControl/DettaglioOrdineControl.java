package SwitchAndKeysControl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import SwitchAndKeysModel.*;


@WebServlet("/DettaglioOrdine")
public class DettaglioOrdineControl extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    private DettaglioOrdineModel DettaglioOrdineModel;
    private SpecificaModel SpecificaModel;
    private OrdineModel OrdineModel;
    
    public DettaglioOrdineControl(){
    	super();
    	DettaglioOrdineModel = new DettaglioOrdineModel();
    	SpecificaModel = new SpecificaModel();
    	OrdineModel = new OrdineModel();
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doGet(request,response);
    }
    
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    	String action = request.getParameter("action");
    	
    	 if (action != null) {
		     switch (action) {
		         case "showDetails":
		        	 
		        	 int RifIdOrdine = Integer.parseInt(request.getParameter("rif_id_ordine"));
		        	 OrdineBean Ordine = new OrdineBean();
		        	 DettaglioOrdineBean DettaglioOrdine = DettaglioOrdineModel.doRetrieveDetailsForOrder(RifIdOrdine);                     	
		     		 request.setAttribute("dettaglio_ordine", DettaglioOrdine);
		     		 Collection<ProdottoBean> Lista_Prodotti = SpecificaModel.doRetrieveSpecifiedProducts(DettaglioOrdine.getIdDettaglio());
		        	 request.setAttribute("lista_dettaglio", Lista_Prodotti);
					 try {
					 	Ordine = OrdineModel.doRetrieveOrderById(RifIdOrdine);
					 } catch (SQLException e) {
						e.printStackTrace();
					 }
					 request.setAttribute("ordine", Ordine);
		        	 request.getRequestDispatcher("./DettagliOrdine.jsp").forward(request, response);
		             return;
		             
		         default:
		             break;
		     }
		 }
    }
}
