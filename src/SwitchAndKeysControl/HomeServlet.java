package SwitchAndKeysControl;

import SwitchAndKeysModel.HomeModel;
import SwitchAndKeysModel.ProdottoBean;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

@MultipartConfig
@WebServlet("/HomeServlet")
public class HomeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(HomeServlet.class.getName());

    private HomeModel homeModel;

    @Override
    public void init() throws ServletException {
        super.init();
        homeModel = new HomeModel();
        logger.info("HomeServlet initialized.");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String featuredIdsParam = request.getParameter("featuredIds");
        String action = request.getParameter("action");

        logger.info("doPost action: " + action);
        logger.info("featuredIdsParam: " + featuredIdsParam);

        try {
            if ("updateHomepage".equals(action)) {
                logger.info("Updating homepage.");

                // Clear existing featured products
                homeModel.clearProdottiFeatured();

                // Add products to featured
                if (featuredIdsParam != null && !featuredIdsParam.isEmpty()) {
                    String[] featuredIds = featuredIdsParam.split(",");
                    for (String idStr : featuredIds) {
                        int prodottoId = Integer.parseInt(idStr);
                        logger.info("Adding product to featured: " + prodottoId);
                        homeModel.addProdottoFeatured(prodottoId);
                    }
                }

                // Redirect to the homepage management page
                response.sendRedirect("GestioneHomePage.jsp"); 

            } else if ("addToFeatured".equals(action)) {
                int prodottoId = Integer.parseInt(request.getParameter("prodottoId"));
                logger.info("Adding product to featured: " + prodottoId);
                homeModel.addProdottoFeatured(prodottoId);
                response.sendRedirect("GestioneHomePage.jsp"); 

            } else if ("removeFromFeatured".equals(action)) {
                int prodottoId = Integer.parseInt(request.getParameter("prodottoId"));
                logger.info("Removing product from featured: " + prodottoId);
                homeModel.removeProdottoFeatured(prodottoId);
                response.sendRedirect("GestioneHomePage.jsp");

            } else {
                logger.warning("Unknown action: " + action);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error", e);
            throw new ServletException("Database error", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Collection<ProdottoBean> featuredProdotti = homeModel.getProdottiFeatured();
            logger.info("Featured products count: " + featuredProdotti.size());
            request.setAttribute("featuredProdotti", featuredProdotti);

            // Determine the target JSP based on some criteria or request parameter
            String targetPage = request.getParameter("page");
            if ("home".equals(targetPage)) {
                request.getRequestDispatcher("/Home.jsp").forward(request, response); 
            } else if ("GestioneHomePage".equals(targetPage)) {
                request.getRequestDispatcher("/GestioneHomePage.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error", e);
            throw new ServletException("Database error", e);
        }
    }
}
