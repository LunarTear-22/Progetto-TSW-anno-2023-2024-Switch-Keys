package SwitchAndKeysControl;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/SetHeaderServlet")
public class HeaderControl extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession();

        if (action != null) {
            switch (action) {
                case "Header":
                    session.setAttribute("isHeader", Boolean.TRUE);
                    response.sendRedirect("prodotto?Catalogo");
                    break;
                case "NotHeader":
                    session.setAttribute("isHeader", Boolean.FALSE);
                    response.sendRedirect("prodotto?Catalogo");
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Azione non riconosciuta: " + action);
                    break;
            }
        } else {
            
            session.setAttribute("isHeader", Boolean.TRUE);
            response.sendRedirect("prodotto?Catalogo");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
