package SwitchAndKeysControl;

import SwitchAndKeysModel.ImmaginiModel;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet({"/CaricaImmagine"})
@MultipartConfig
public class CaricaImmagine extends HttpServlet {
  private static final long serialVersionUID = 1L;
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    response.setContentType("text/plain");
    out.write("Error: GET method is used but POST method is required");
    out.close();
  }
  
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String SAVE_DIR = "/uploadTemp";
    String appPath = request.getServletContext().getRealPath("");
    String savePath = String.valueOf(appPath) + File.separator + SAVE_DIR;
    int rif_id_prodotto = Integer.parseInt(request.getParameter("rif_id_prodotto"));
    File fileSaveDir = new File(savePath);
    if (!fileSaveDir.exists())
      fileSaveDir.mkdir(); 
    for (Part part : request.getParts()) {
      String fileName = extractFileName(part);
      if (fileName != null && !fileName.equals("")) {
        part.write(String.valueOf(savePath) + File.separator + fileName);
        try {
          ImmaginiModel.updatePhoto(rif_id_prodotto, String.valueOf(savePath) + File.separator + fileName);
        } catch (SQLException sqlException) {
          System.out.println(sqlException);
        } 
      } 
    } 
    RequestDispatcher view = request.getRequestDispatcher("/Catalogo.jsp");
    view.forward((ServletRequest)request, (ServletResponse)response);
  }
  
  private String extractFileName(Part part) {
    String contentDisp = part.getHeader("content-disposition");
    String[] items = contentDisp.split(";");
    byte b;
    int i;
    String[] arrayOfString1;
    for (i = (arrayOfString1 = items).length, b = 0; b < i; ) {
      String s = arrayOfString1[b];
      if (s.trim().startsWith("filename"))
        return s.substring(s.indexOf("=") + 2, s.length() - 1); 
      b++;
    } 
    return "";
  }
}
