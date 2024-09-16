package SwitchAndKeysControl;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import SwitchAndKeysModel.ProdottoBean;
import SwitchAndKeysModel.ProdottoModelDS;
import SwitchAndKeysModel.UtenteBean;

@MultipartConfig
public class ProdottoControl extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String SAVE_DIR = "uploadTemp";
    private ProdottoModelDS model;

    public ProdottoControl() {
        super();
        model = new ProdottoModelDS();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ProdottoBean bean = (ProdottoBean) request.getSession().getAttribute("prodotto");
        if (bean == null) {
            bean = new ProdottoBean();
        }

        String action = request.getParameter("action");

        try {
            if (action != null) {
                switch (action) {
                    case "addC":
                        // Redirect to CarrelloControl for adding product
                        int id_prodottoAdd = Integer.parseInt(request.getParameter("id_prodotto"));
                        response.sendRedirect(request.getContextPath() + "/Carrello?action=addC&id_prodotto=" + id_prodottoAdd);
                        return;

                    case "deleteC":
                        // Redirect to CarrelloControl for deleting product
                        int id_prodottoDelete = Integer.parseInt(request.getParameter("id_prodotto"));
                        response.sendRedirect(request.getContextPath() + "/Carrello?action=deleteC&id_prodotto=" + id_prodottoDelete);
                        return;

                    case "read":
                        int id_prodottoRead = Integer.parseInt(request.getParameter("id_prodotto"));
                        request.removeAttribute("prodotto");
                        request.getSession().setAttribute("prodotto", model.doRetrieveByKey(id_prodottoRead));
                        response.sendRedirect(request.getContextPath() + "/Dettagli.jsp");
                        return;

                    case "delete":
                        int id_prodotto = Integer.parseInt(request.getParameter("id_prodotto"));
                        model.doDelete(id_prodotto);
                        response.sendRedirect(request.getContextPath() + "/Catalogo.jsp");
                        return;

                    case "insert":
                    	
                    	try {
                            String id_prodottoInsertParam = request.getParameter("id_prodotto");
                            String rif_id_catalogoParam = request.getParameter("rif_id_catalogo");
                            String nome = request.getParameter("nome");
                            String prezzoParam = request.getParameter("prezzo");
                            String ivaParam = request.getParameter("iva");
                            String quantitaParam = request.getParameter("quantita");
                            String categoria = request.getParameter("categoria");
                            String tipo = null;
                            if (request.getParameter("tipo-input") != null && !request.getParameter("tipo-input").isEmpty()) {
                                tipo = request.getParameter("tipo-input");
                            } else if (request.getParameter("tipo-select") != null && !request.getParameter("tipo-select").isEmpty()) {
                                tipo = request.getParameter("tipo-select");
                            }
                            String materiale = request.getParameter("materiale");
                            if(materiale == null || materiale.isEmpty()) {
                            	materiale = null;
                            }else {
                            	materiale = request.getParameter("materiale");
                            }
                            
                            String marca = request.getParameter("marca");
                                                       
                            String cablataParam = request.getParameter("cablata");
                            String wifiParam = request.getParameter("wifi");
                            String bluetoothParam = request.getParameter("bluetooth");
                            
                            String descrizione = request.getParameter("descrizione");
                            
                            Boolean cablata;
                            Boolean wifi;
                            Boolean bluetooth;
                            if ("Tastiera".equals(categoria) || "Accessori".equals(categoria)) {
                                                             
                                cablata = cablataParam != null ? Boolean.valueOf(cablataParam) : Boolean.FALSE;
                                wifi = wifiParam != null ? Boolean.valueOf(wifiParam) : Boolean.FALSE;
                                bluetooth = bluetoothParam != null ? Boolean.valueOf(bluetoothParam) : Boolean.FALSE;
                                
                            } else {
                                                                
                                cablata = cablataParam != null ? Boolean.valueOf(cablataParam) : null;
                                wifi = wifiParam != null ? Boolean.valueOf(wifiParam) : null;
                                bluetooth = bluetoothParam != null ? Boolean.valueOf(bluetoothParam) : null;
                            
                            }

                            int id_prodottoInsert = Integer.parseInt(id_prodottoInsertParam);
                            int rif_id_catalogo = Integer.parseInt(rif_id_catalogoParam);
                            double prezzo = Double.parseDouble(prezzoParam);
                            double iva = Double.parseDouble(ivaParam);
                            int quantita = Integer.parseInt(quantitaParam);
                            

                            Part filePartInsert = request.getPart("dati_immagine");
                            String imagePathInsert = saveImage(filePartInsert, id_prodottoInsert, request);

                            bean.setIdProdotto(id_prodottoInsert);
                            bean.setRifIdCatalogo(rif_id_catalogo);
                            bean.setNome(nome);
                            bean.setPrezzo(prezzo);
                            bean.setIva(iva);
                            bean.setQuantita(quantita);
                            bean.setCategoria(categoria);
                            bean.setTipo(tipo);
                            bean.setMateriale(materiale);
                            bean.setMarca(marca);
                            bean.setCablata(cablata);
                            bean.setWifi(wifi);
                            bean.setBluetooth(bluetooth);
                            bean.setDescrizione(descrizione);
                            bean.setDisponibile(quantita > 0);

                            if(model.doSave(bean, imagePathInsert)) {
                                // Aggiungi l'attributo di successo alla richiesta
                                request.setAttribute("success", true);
                            }
                            response.sendRedirect(request.getContextPath() + "/Catalogo.jsp");
                        } catch (NumberFormatException e) {
                            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Formato dei parametri non valido");
                            e.printStackTrace();
                        } catch (Exception e) {
                            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore durante l'inserimento del prodotto");
                            e.printStackTrace();
                        }
                       
                        return;

                    case "showModifyForm":
                        int id_prodottoShowModify = Integer.parseInt(request.getParameter("id_prodotto"));
                        request.getSession().setAttribute("ProdottoDaModificare", model.doRetrieveByKey(id_prodottoShowModify));
                        response.sendRedirect(request.getContextPath() + "/ModificaProdotto.jsp");
                        return;

                    case "modify":
                        int id_prodottoModify = Integer.parseInt(request.getParameter("id_prodotto"));
                        int rif_id_catalogoModify = Integer.parseInt(request.getParameter("rif_id_catalogo"));
                        String nomeModify = request.getParameter("nome");
                        double prezzoModify = Double.parseDouble(request.getParameter("prezzo"));
                        double ivaModify = Double.parseDouble(request.getParameter("iva"));
                        int quantitaModify = Integer.parseInt(request.getParameter("quantita"));
                        String categoriaModify = request.getParameter("categoria");
                        String tipoModify = null;
                        if (request.getParameter("tipo-input") != null && !request.getParameter("tipo-input").equals("")) {
                            tipoModify = request.getParameter("tipo-input");
                        } else if (request.getParameter("tipo-select") != null && !request.getParameter("tipo-select").equals("")) {
                            tipoModify = request.getParameter("tipo-select");
                        }

                        String materialeModify = request.getParameter("materiale");
                        if (materialeModify == null || materialeModify.isEmpty()) {
                            materialeModify = null;
                        } else {
                            materialeModify = request.getParameter("materiale");
                        }

                        String marcaModify = request.getParameter("marca");

                        String cablataParam = request.getParameter("cablata");
                        String wifiParam = request.getParameter("wifi");
                        String bluetoothParam = request.getParameter("bluetooth");
                        String descrizioneModify = request.getParameter("descrizione");

                        Boolean cablataModify;
                        Boolean wifiModify;
                        Boolean bluetoothModify;

                        if ("Tastiera".equals(categoriaModify) || "Accessori".equals(categoriaModify)) {
                            cablataModify = cablataParam != null ? Boolean.valueOf(cablataParam) : Boolean.FALSE;
                            wifiModify = wifiParam != null ? Boolean.valueOf(wifiParam) : Boolean.FALSE;
                            bluetoothModify = bluetoothParam != null ? Boolean.valueOf(bluetoothParam) : Boolean.FALSE;
                        } else {
                            cablataModify = cablataParam != null ? Boolean.valueOf(cablataParam) : null;
                            wifiModify = wifiParam != null ? Boolean.valueOf(wifiParam) : null;
                            bluetoothModify = bluetoothParam != null ? Boolean.valueOf(bluetoothParam) : null;
                        }

                        ProdottoBean newProdotto = new ProdottoBean();

                        newProdotto.setIdProdotto(id_prodottoModify);
                        newProdotto.setRifIdCatalogo(rif_id_catalogoModify);
                        newProdotto.setNome(nomeModify);
                        newProdotto.setPrezzo(prezzoModify);
                        newProdotto.setIva(ivaModify);
                        newProdotto.setQuantita(quantitaModify);
                        newProdotto.setCategoria(categoriaModify);
                        newProdotto.setTipo(tipoModify);
                        newProdotto.setMateriale(materialeModify);
                        newProdotto.setMarca(marcaModify);
                        newProdotto.setCablata(cablataModify);
                        newProdotto.setWifi(wifiModify);
                        newProdotto.setBluetooth(bluetoothModify);
                        newProdotto.setDescrizione(descrizioneModify);
                        newProdotto.setDisponibile(quantitaModify > 0);

                        Part modifyFilePart = request.getPart("dati_immagine");
                        String modifyImagePath = saveImage(modifyFilePart, id_prodottoModify, request);

                        model.doModify(newProdotto, id_prodottoModify, modifyImagePath);
                        response.sendRedirect(request.getContextPath() + "/Catalogo.jsp");
                        return;
                        
                    case "setProdotti":
                        // Recupera tutti i prodotti dal database
                        Collection<ProdottoBean> prodotti = model.doRetrieveAll(null);

                        // Salva i prodotti nella sessione
                        HttpSession session = request.getSession();
                        session.setAttribute("prodotti", prodotti);

                        // Reindirizza alla pagina di gestione
                        response.sendRedirect(request.getContextPath() + "/GestioneHomePage.jsp");
                        return;
                        
                    default:
                    	String sort = request.getParameter("sort");
                        request.removeAttribute("prodotti");
                        request.setAttribute("prodotti", model.doRetrieveAll(sort));
                        Boolean isHeader = (Boolean) request.getSession().getAttribute("isHeader");
                        RequestDispatcher dispatcher;
                        if (isHeader != null && isHeader) {
                            dispatcher = getServletContext().getRequestDispatcher("/Shop.jsp");
                        } else {
                            UtenteBean user = (UtenteBean) request.getSession().getAttribute("loggedInUser");
                            dispatcher = (user != null && user.isAdmin())
                                    ? getServletContext().getRequestDispatcher("/Catalogo.jsp")
                                    : getServletContext().getRequestDispatcher("/LoginView.jsp");
                        }
                        dispatcher.forward(request, response);
                        break;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error:" + e.getMessage());
        }

        String sort = request.getParameter("sort");
        request.removeAttribute("prodotti");
        try {
			request.setAttribute("prodotti", model.doRetrieveAll(sort));
		} catch (SQLException e) {
			e.printStackTrace();
		}
        Boolean isHeader = (Boolean) request.getSession().getAttribute("isHeader");
        RequestDispatcher dispatcher;
        if (isHeader != null && isHeader) {
            dispatcher = getServletContext().getRequestDispatcher("/Shop.jsp");
        } else {
            UtenteBean user = (UtenteBean) request.getSession().getAttribute("loggedInUser");
            dispatcher = (user != null && user.isAdmin())
                    ? getServletContext().getRequestDispatcher("/Catalogo.jsp")
                    : getServletContext().getRequestDispatcher("/LoginView.jsp");
        }
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private String saveImage(Part filePart, int idProdotto, HttpServletRequest request) throws IOException {
        String appPath = request.getServletContext().getRealPath("");
        String savePath = appPath + File.separator + SAVE_DIR;

        File fileSaveDir = new File(savePath);
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdir();
        }

        if (filePart != null && filePart.getSize() > 0) {
            String fileName = extractFileName(filePart);
            if (fileName != null && !fileName.isEmpty()) {
                String filePath = savePath + File.separator + fileName;
                filePart.write(filePath);
                return filePath;
            }
        }
        return null;
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
