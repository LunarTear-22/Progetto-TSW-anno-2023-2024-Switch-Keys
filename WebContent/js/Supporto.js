// Funzione per aprire il popup
function openSupportPopup() {
    document.getElementById("support-success-popup").style.display = "block";
}

// Funzione per chiudere il popup
function closeSupportPopup() {
    document.getElementById("support-success-popup").style.display = "none";
}

// Gestione dell'evento di submit del form
document.getElementById("supportForm").addEventListener("submit", function(event) {
    event.preventDefault(); // Previene il comportamento di submit di default
    openSupportPopup(); // Mostra il popup
});
