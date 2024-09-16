package SwitchAndKeysControl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponseWrapper;
import org.xhtmlrenderer.pdf.ITextRenderer;
import SwitchAndKeysModel.DettaglioOrdineBean;
import SwitchAndKeysModel.DettaglioOrdineModel;
import SwitchAndKeysModel.OrdineBean;
import SwitchAndKeysModel.OrdineModel;
import SwitchAndKeysModel.ProdottoBean;
import SwitchAndKeysModel.SpecificaModel;

@WebServlet("/Fattura")
public class FatturaControl extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private DettaglioOrdineModel dettaglioOrdineModel;
    private SpecificaModel specificaModel;
    private OrdineModel ordineModel;

    public FatturaControl() {
        super();
        dettaglioOrdineModel = new DettaglioOrdineModel();
        specificaModel = new SpecificaModel();
        ordineModel = new OrdineModel();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action != null && action.equals("view-invoice")) {
            int rifIdOrdine = Integer.parseInt(request.getParameter("rif_id_ordine"));
            OrdineBean ordine = new OrdineBean();
            DettaglioOrdineBean dettaglioOrdine = dettaglioOrdineModel.doRetrieveDetailsForOrder(rifIdOrdine);
            request.setAttribute("dettaglio_ordine", dettaglioOrdine);
            Collection<ProdottoBean> listaProdotti = specificaModel.doRetrieveSpecifiedProducts(dettaglioOrdine.getIdDettaglio());
            request.setAttribute("lista_dettaglio", listaProdotti);
            try {
                ordine = ordineModel.doRetrieveOrderById(rifIdOrdine);
            } catch (SQLException e) {
                throw new ServletException("Database error", e);
            }
            request.setAttribute("ordine", ordine);

            // Converti l'output della JSP in HTML
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            PrintWriter writer = new PrintWriter(byteArrayOutputStream);
            HttpServletResponseWrapper responseWrapper = new HttpServletResponseWrapper(response) {
                @Override
                public PrintWriter getWriter() {
                    return writer;
                }
            };

            // Include la pagina JSP nel wrapper
            RequestDispatcher dispatcher = request.getRequestDispatcher("./Fattura.jsp");
            dispatcher.include(request, responseWrapper);
            writer.flush();

            // Ottieni l'HTML generato dalla JSP
            String htmlContent = byteArrayOutputStream.toString();

            // Utilizza ITextRenderer per convertire l'HTML in PDF
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);

            // Imposta il percorso delle risorse (CSS, immagini, etc.)
            String url = request.getRequestURL().toString();
            String baseURL = url.substring(0, url.lastIndexOf("/"));
            renderer.getSharedContext().setBaseURL(baseURL);

            renderer.layout();

            // Imposta il tipo di contenuto della risposta e invia il PDF al client
            response.setContentType("application/pdf");
            ServletOutputStream outputStream = response.getOutputStream();
            try {
                renderer.createPDF(outputStream);
            } catch (com.lowagie.text.DocumentException e) {
				e.printStackTrace();
			} finally {
                outputStream.close();
            }
        }
    }
}
