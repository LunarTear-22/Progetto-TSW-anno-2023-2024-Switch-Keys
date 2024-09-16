package SwitchAndKeysControl;

import SwitchAndKeysModel.*;
import SwitchAndKeysUtil.PasswordUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;


@WebServlet({"/Utente"})
public class UtenteControl extends HttpServlet {
    private static final long serialVersionUID = 1L;
    UtenteModel model = new UtenteModel();
    AreaUtenteModel areaUtenteModel = new AreaUtenteModel();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession(false);

        if (session != null) {
            System.out.println("Session ID: " + session.getId());
            System.out.println("Max Inactive Interval: " + session.getMaxInactiveInterval());
            System.out.println("Logged In User: " + session.getAttribute("loggedInUser"));
        } else {
            System.out.println("No session found.");
        }

        if (action != null) {
            switch (action.toLowerCase()) {
                case "register":
                    String email = request.getParameter("email");
                    String plainPassword = request.getParameter("password");
                    String nome = request.getParameter("nome");
                    String cognome = request.getParameter("cognome");

                    if (email == null || plainPassword == null || nome == null || cognome == null) {
                        response.sendRedirect(request.getContextPath() + "/RegistrazioneView.jsp?error=true");
                        return;
                    }

                    email = sanitizeInput(email);
                    String encryptedPassword = PasswordUtil.encryptPassword(plainPassword);

                    try {
                        if (model.emailExists(email)) {
                            response.sendRedirect(request.getContextPath() + "/RegistrazioneView.jsp?error=emailExists");
                        } else {
                            UtenteBean newUser = new UtenteBean();
                            newUser.setEmail(email);
                            newUser.setPassword(encryptedPassword);
                            newUser.setNome(nome);
                            newUser.setCognome(cognome);
                            if (model.saveUser(newUser)) {
                                response.sendRedirect(request.getContextPath() + "/RegistrazioneView.jsp?success=true");
                            } else {
                                response.sendRedirect(request.getContextPath() + "/RegistrazioneView.jsp?error=true");
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        response.sendRedirect(request.getContextPath() + "/RegistrazioneView.jsp?error=true");
                    }
                    break;

                case "login":
                    String loginEmail = request.getParameter("email");
                    String loginPlainPassword = request.getParameter("password");

                    if (loginEmail == null || loginPlainPassword == null) {
                        response.sendRedirect(request.getContextPath() + "/LoginView.jsp?loginError=true");
                        return;
                    }

                    loginEmail = sanitizeInput(loginEmail);
                    String loginEncryptedPassword = PasswordUtil.encryptPassword(loginPlainPassword);

                    try {
                        UtenteBean user = model.getUserByEmail(loginEmail);
                        if (user != null && user.getPassword().equals(loginEncryptedPassword)) {
                            HttpSession loginSession = request.getSession();
                            loginSession.setAttribute("loggedInUser", user);

                            CarrelloBean carrello = (CarrelloBean) loginSession.getAttribute("carrello");
                          

                            if (carrello != null) {
                                CarrelloModel modelCart = new CarrelloModel();
                                modelCart.saveCart(carrello, user);
                                carrello = modelCart.getCart(user);
                                loginSession.setAttribute("carrello", carrello);
                            }

                            String redirectAfterLogin = (String) loginSession.getAttribute("redirectAfterLogin");
                            

                            String username = user.getEmail();
                            Collection<IndirizzoSpedizioneBean> addresses = areaUtenteModel.doRetrieveAllAddressByUser(username);
                            Collection<MetodoPagamentoBean> payMethods = areaUtenteModel.doRetrieveAllMethodByUser(username);

                            loginSession.setAttribute("Indirizzi", addresses);
                            loginSession.setAttribute("MetodiPagamento", payMethods);

                            
                            if (redirectAfterLogin != null) {
                                loginSession.removeAttribute("redirectAfterLogin");
                                response.sendRedirect(request.getContextPath() + "/" + redirectAfterLogin + ".jsp");
                            } else {
                                response.sendRedirect(request.getContextPath() + "/LoginView.jsp?loginSuccess=true");
                            }
                        } else {
                            response.sendRedirect(request.getContextPath() + "/LoginView.jsp?loginError=true");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        response.sendRedirect(request.getContextPath() + "/LoginView.jsp?loginError=true");
                    }

                    session = request.getSession();
                    System.out.println("Session ID: " + session.getId());
                    System.out.println("Max Inactive Interval: " + session.getMaxInactiveInterval());
                    System.out.println("Logged In User: " + session.getAttribute("loggedInUser"));
                    break;

                case "logout":
                    HttpSession logoutSession = request.getSession(false);
                    if (logoutSession != null) {
                        logoutSession.invalidate();
                    }
                    response.sendRedirect(request.getContextPath() + "/LoginView.jsp?logoutSuccess=true");
                    break;

                case "viewusers":
                    UtenteBean loggedInUser = (UtenteBean) session.getAttribute("loggedInUser");
                    if (loggedInUser != null && loggedInUser.isAdmin()) {
                        try {
                            List<UtenteBean> users = model.doRetrieveAllUser();
                            request.setAttribute("userList", users);
                            request.getRequestDispatcher("/GestioneUtenti.jsp").forward(request, response);
                        } catch (SQLException e) {
                            e.printStackTrace();
                            response.sendRedirect(request.getContextPath() + "/500.jsp");
                        }
                    } else {
                        response.sendRedirect(request.getContextPath() + "/403.jsp");
                    }
                    break;

                case "makeadmin":
                    UtenteBean adminUser = (UtenteBean) session.getAttribute("loggedInUser");
                    if (adminUser != null && adminUser.isAdmin()) {
                        String emailToPromote = request.getParameter("email");
                        try {
                            if (model.makeAdmin(emailToPromote)) {
                                response.sendRedirect(request.getContextPath() + "/GestioneUtenti.jsp?success=adminPromoted");
                            } else {
                                response.sendRedirect(request.getContextPath() + "/GestioneUtenti.jsp?error=promotionFailed");
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                            response.sendRedirect(request.getContextPath() + "/500.jsp");
                        }
                    } else {
                        response.sendRedirect(request.getContextPath() + "/403.jsp");
                    }
                    break;

                case "removeadmin":
                    UtenteBean removeAdminUser = (UtenteBean) session.getAttribute("loggedInUser");
                    if (removeAdminUser != null && removeAdminUser.isAdmin()) {
                        String emailToDemote = request.getParameter("email");
                        try {
                            if (model.removeAdmin(emailToDemote)) {
                                response.sendRedirect(request.getContextPath() + "/GestioneUtenti.jsp?success=adminRemoved");
                            } else {
                                response.sendRedirect(request.getContextPath() + "/GestioneUtenti.jsp?error=demotionFailed");
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                            response.sendRedirect(request.getContextPath() + "/500.jsp");
                        }
                    } else {
                        response.sendRedirect(request.getContextPath() + "/403.jsp");
                    }
                    break;

                default:
                    response.sendRedirect(request.getContextPath() + "/500.jsp");
                    break;
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/500.jsp");
        }
    }

    private String sanitizeInput(String input) {
        if (input == null) return null;
        return input.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
