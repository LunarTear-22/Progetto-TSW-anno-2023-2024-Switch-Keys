<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="Header.jsp" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Registrazione</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="./css/reg.css">
</head>
<body>
<div class="main-wrapper">
    <div class="content-wrapper">
        <div class="container">
            <div class="item">
                <h2 class="text-logo">SwitchAndKeys</h2>
                <div class="text-item">
                    <h2>Benvenuti <br><span> nel nostro Store!</span></h2>
                    <p> Creazione Account </p>
                </div>
            </div>
        </div>

        <div class="register-section">
            <div class="form-box_register">
                <form id="registrationForm" action="Utente" method="post" onsubmit="return validateForm()">
                    <input type="hidden" name="action" value="register">
                    <h2> Registrati </h2>
                    
                    <!-- Messaggi di errore -->
                    <div  id="errorMessages">
                        <% 
                            String error = request.getParameter("error");
                            if ("emailExists".equals(error)) {
                                out.print("<p class='error-message' id='emailExistsError'>L'email inserita è già in uso.</p>");
                            } else if ("true".equals(error)) {
                                out.print("<p class='error-message' id='generalError'>Errore durante la registrazione. Riprova.</p>");
                            }
                        %>
                    </div>

                    <div class="input-box">
					    
					    <div class="input-container">
					        <input id="email" name="email" type="email" required placeholder="Email" aria-describedby="emailError">
					        <div class="input-icon"><img alt="Email" src="imgs/login-reg/icons8-messaggio-64.png"></div>
					    </div>
					    	<span class="error-message" id="emailError"></span>
					</div>
					
					<div class="input-box">
						    
						    <div class="input-container">
						        <input id="nome" name="nome" type="text" maxlength="25" required placeholder="Nome" aria-describedby="nomeError">
						       	<div class="input-icon"><img alt="Nome" src="imgs/login-reg/icons8-pencil-48.png"></div>
						    </div>
						    <span class="error-message" id="nomeError"></span>
						</div>
						<div class="input-box">
						    
						    <div class="input-container">
						        <input id="cognome" name="cognome" type="text" maxlength="25" required placeholder="Cognome" aria-describedby="cognomeError">
						        <div class="input-icon"><img alt="Cognome" src="imgs/login-reg/icons8-pencil-48.png"></div>
						    </div>
						    <span class="error-message" id="cognomeError"></span>
						</div>
						<div class="input-box-pass">
						    
						    <div class="input-container">
						        <input id="password" name="password" type="password" maxlength="16" required placeholder="Password" aria-describedby="passwordError">
						        <div class="input-icon"><img alt="Password" src="imgs/login-reg/icons8-chiave-64.png"></div>
						    </div>
						     <span class="error-message" id="passwordError"></span>
						</div>

                    <button class="btn">Registrati</button>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- Pop-up per la registrazione avvenuta con successo -->
<div id="successPopup" class="popup">
    <div class="popup-content">
        <span class="close" onclick="closePopup('successPopup')">&times;</span>
        <p>Registrazione avvenuta con successo!</p>
        <button onclick="redirectToLogin()">Vai al Login</button>
    </div>
</div>

<!-- Pop-up per errore durante la registrazione -->
<div id="errorPopup" class="popup">
    <div class="popup-content">
        <span class="close" onclick="closePopup('errorPopup')">&times;</span>
        <p>Errore durante la registrazione. Riprova.</p>
        <button onclick="closePopup('errorPopup')">Chiudi</button>
    </div>
</div>

<%@ include file="Footer.jsp" %>

<script src="js/registrazione.js"></script>
</body>
</html>
