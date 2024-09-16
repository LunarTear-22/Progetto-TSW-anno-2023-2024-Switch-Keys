function showModal() {
    console.log('Mostra il modale'); // Aggiungi il log per il debug
    document.getElementById('sessionExpiredModal').style.display = "block";
    document.body.classList.add('no-scroll'); // Disabilita lo scorrimento della pagina
}

function closeModal() {
    console.log('Chiudi il modale'); // Aggiungi il log per il debug
    document.getElementById('sessionExpiredModal').style.display = "none";
    document.body.classList.remove('no-scroll'); // Riabilita lo scorrimento della pagina
    window.location.href = 'Utente?action=logout'; // Reindirizza alla pagina di logout
}

function renewSession() {
    console.log('Tentativo di rinnovo della sessione'); // Aggiungi il log per il debug
    var xhr = new XMLHttpRequest();
    xhr.open('POST', 'renew-session', true);
    xhr.withCredentials = true; // Necessario per inviare i cookie di sessione
    xhr.onload = function() {
        if (xhr.status >= 200 && xhr.status < 300) { // 200 e 300 status code
            console.log('Sessione rinnovata con successo');
        } else {
            console.log('Sessione non valida, mostrando modale');
            showModal();
        }
    };
    xhr.onerror = function() {
        console.error('Errore durante il rinnovo della sessione');
        showModal();
    };
    xhr.send();
}

function setupSessionTimeout(isLoggedIn) {
    if (!isLoggedIn) return;

    const sessionTimeout = 15 * 60 * 1000; // 1 minuto per test
    let timeout;

    function resetTimeout() {
        if (timeout) {
            clearTimeout(timeout);
        }
        timeout = setTimeout(() => {
            console.log('Timeout scaduto, mostrando modale'); // Aggiungi il log per il debug
            showModal(); // Mostra il popup modale alla scadenza della sessione
        }, sessionTimeout);
    }

    // Aggiungi listener per le azioni dell'utente per resettare il timeout
    document.addEventListener('mousemove', resetTimeout);
    document.addEventListener('keypress', resetTimeout);
    document.addEventListener('click', resetTimeout);
    document.addEventListener('scroll', resetTimeout);
    document.addEventListener('touchstart', resetTimeout);

    // Aggiungi listener per il rinnovo della sessione
    document.addEventListener('mousemove', renewSession);
    document.addEventListener('keypress', renewSession);
    document.addEventListener('click', renewSession);
    document.addEventListener('scroll', renewSession);
    document.addEventListener('touchstart', renewSession);

    // Imposta il timeout iniziale
    resetTimeout();
}

// Esegui la funzione di configurazione al caricamento della pagina
window.onload = function() {
    const loggedInUser = document.getElementById('loggedInUser');
    const isLoggedIn = loggedInUser ? loggedInUser.value === 'true' : false;
    console.log('Utente loggato:', isLoggedIn); // Aggiungi il log per il debug
    setupSessionTimeout(isLoggedIn);
};
