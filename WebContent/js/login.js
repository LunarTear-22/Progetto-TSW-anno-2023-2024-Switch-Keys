document.addEventListener("DOMContentLoaded", function() {
    const params = new URLSearchParams(window.location.search);
    if (params.has('loginSuccess') && params.get('loginSuccess') === 'true') {
        document.getElementById('successPopup').style.display = 'block';
    } else if (params.has('loginError') && params.get('loginError') === 'true') {
        document.getElementById('errorPopup').style.display = 'block';
    }

    // Imposta il focus sul campo email
    document.forms["loginForm"]["email"].focus();
});

function closePopup(popupId) {
    document.getElementById(popupId).style.display = 'none';
}

function redirectToHome() {
    window.location.href = 'Home.jsp';
}



function validateForm() {
    const email = document.forms["loginForm"]["email"].value;
    const password = document.forms["loginForm"]["password"].value;

    if (!email || !password) {
        console.log("Tutti i campi sono obbligatori.");
        return false;
    }


    return true;
}
