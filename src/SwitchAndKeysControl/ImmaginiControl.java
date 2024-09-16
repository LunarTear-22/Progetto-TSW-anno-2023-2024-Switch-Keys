package SwitchAndKeysControl;

import SwitchAndKeysModel.ImmaginiModel;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.apache.catalina.connector.ClientAbortException;

@WebServlet({"/Immagine"})
@MultipartConfig
public class ImmaginiControl extends HttpServlet {
  private static final long serialVersionUID = 1L;
  
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    int rif_id_prodotto = Integer.parseInt(request.getParameter("rif_id_prodotto"));
    String action = request.getParameter("action");
    try {
      if (action != null) {
        switch (action) {
          case "GetImmagine":
            try {
              byte[] bt = ImmaginiModel.load(rif_id_prodotto);
              if (bt != null) {
                response.setContentType("image/jpeg");
                try (ServletOutputStream out = response.getOutputStream()) {
                  out.write(bt);
                }
              }
            } catch (SQLException e) {
              e.printStackTrace();
            }
            break;
        }
      }
    } catch (ClientAbortException e) {
      System.err.println("Client aborted the connection: " + e.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String action = request.getParameter("action");
    try {
      if (action != null) {
        String SAVE_DIR;
        String appPath;
        String savePath;
        int rif_id_prodotto;
        File fileSaveDir;
        RequestDispatcher view;
        switch (action) {
          case "AggiornaImmagineDB":
            SAVE_DIR = "uploadTemp";
            appPath = request.getServletContext().getRealPath("");
            savePath = appPath + File.separator + SAVE_DIR;
            rif_id_prodotto = Integer.parseInt(request.getParameter("rif_id_prodotto"));
            fileSaveDir = new File(savePath);
            if (!fileSaveDir.exists())
              fileSaveDir.mkdir();
            for (Part part : request.getParts()) {
              String fileName = extractFileName(part);
              if (fileName != null && !fileName.isEmpty()) {
                String filePath = savePath + File.separator + fileName;
                part.write(filePath);
                try {
                  ImmaginiModel.updatePhoto(rif_id_prodotto, filePath);
                } catch (SQLException sqlException) {
                  System.out.println(sqlException);
                }
              }
            }
            view = request.getRequestDispatcher("/Catalogo.jsp");
            view.forward(request, response);
            break;
          case "CaricaDB":
            SAVE_DIR = "uploadTemp";
            appPath = request.getServletContext().getRealPath("");
            savePath = appPath + File.separator + SAVE_DIR;
            rif_id_prodotto = Integer.parseInt(request.getParameter("rif_id_prodotto"));
            fileSaveDir = new File(savePath);
            if (!fileSaveDir.exists())
              fileSaveDir.mkdir();
            for (Part part : request.getParts()) {
              String fileName = extractFileName(part);
              if (fileName != null && !fileName.isEmpty()) {
                String filePath = savePath + File.separator + fileName;
                part.write(filePath);
                try {
                  ImmaginiModel.insertPhoto(rif_id_prodotto, filePath);
                } catch (SQLException sqlException) {
                  System.out.println(sqlException);
                }
              }
            }
            view = request.getRequestDispatcher("/Catalogo.jsp");
            view.forward(request, response);
            break;
        }
      }
    } catch (ClientAbortException e) {
      System.err.println("Client aborted the connection: " + e.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  private String extractFileName(Part part) {
    String contentDisp = part.getHeader("content-disposition");
    String[] items = contentDisp.split(";");
    for (String s : items) {
      if (s.trim().startsWith("filename")) {
        return s.substring(s.indexOf("=") + 2, s.length() - 1);
      }
    }
    return "";
  }
}
