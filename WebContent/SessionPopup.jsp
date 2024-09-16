<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- Stili CSS per il popup modale -->
    <style>
        .modal-session-expired {
            display: none;
            position: fixed;
            z-index: 9999;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            overflow: auto;
            background-color: rgba(0,0,0,0.4);
            padding-top: 60px;
            
        }

        .modal-content-session-expired {
            background-color: #fefefe;
            margin: 5% auto;
            padding: 20px;
            border: 1px solid #888;
            width: 80%;
            text-align: center;
            position: relative; 
            border-radius: 5px;
        }

        .close-session-expired {
            color: #aaa;
            float: right;
            font-size: 28px;
            font-weight: bold;
            cursor: pointer;
            position: absolute; 
            top: 10px;
            right: 20px;
        }

        .close-session-expired:hover,
        .close-session-expired:focus {
            color: black;
            text-decoration: none;
        }
        
        .no-scroll {
   			overflow: hidden;
		}
    </style>
</head>
<body>
    <!-- Popup modale per la sessione scaduta -->
    <div id="sessionExpiredModal" class="modal-session-expired">
        <div class="modal-content-session-expired">
            <span class="close-session-expired" onclick="closeModal()">&times;</span>
            <p>La tua sessione è scaduta. Verrai reindirizzato alla pagina di login.</p>
        </div>
    </div>

    <!-- Campo hidden per indicare se l'utente è loggato -->
    <input type="hidden" id="loggedInUser" value="<%= session.getAttribute("loggedInUser") != null ? "true" : "false" %>">

    <!-- Collegamento al file JavaScript di gestione della sessione -->
    <script src="js/session-management.js"></script>
</body>
</html>
