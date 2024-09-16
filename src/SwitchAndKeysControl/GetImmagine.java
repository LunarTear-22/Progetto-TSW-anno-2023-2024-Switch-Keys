package SwitchAndKeysControl;

import SwitchAndKeysModel.ImmaginiModel;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet({"/GetImmagine"})
public class GetImmagine extends HttpServlet {
  private static final long serialVersionUID = 1L;
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    int rif_id_prodotto = Integer.parseInt(request.getParameter("rif_id_prodotto"));
    try {
      byte[] bt = ImmaginiModel.load(rif_id_prodotto);
      ServletOutputStream out = response.getOutputStream();
      if (bt != null) {
        out.write(bt);
        response.setContentType("image/jpeg");
      } 
      out.close();
    } catch (SQLException e) {
      e.printStackTrace();
    } 
  }
  
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doGet(request, response);
  }
}
