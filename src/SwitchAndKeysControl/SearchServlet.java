package SwitchAndKeysControl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;

import SwitchAndKeysModel.ProdottoBean;
import SwitchAndKeysModel.ProdottoModelDS;

@WebServlet("/SearchServlet")
public class SearchServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    public void init() throws ServletException {
        super.init();
        System.out.println("SearchServlet initialized");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchQuery = request.getParameter("query");
        System.out.println("Query received: " + searchQuery);

        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            System.out.println("Bad request: query is null or empty");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        ProdottoModelDS prodottoModel = new ProdottoModelDS();
        Collection<ProdottoBean> prodotti;

        try {
            System.out.println("Searching for products...");
            prodotti = prodottoModel.searchProducts(searchQuery);
            System.out.println("Products found: " + prodotti.size());
        } catch (Exception e) {
            System.out.println("Error during product search");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
            return;
        }

        String action = request.getParameter("action");
        
        if ("readByName".equals(action)) {
            request.removeAttribute("prodotti");
            request.setAttribute("prodotti", prodotti);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/Shop.jsp");
            dispatcher.forward(request, response);
        } else {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            Gson gson = new Gson();
            String json = gson.toJson(prodotti);
            out.print(json);
            out.flush();
            System.out.println("Response sent successfully");
        }
    }
}
