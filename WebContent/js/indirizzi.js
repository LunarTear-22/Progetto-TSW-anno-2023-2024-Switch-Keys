function confirmDeleteAddress(id_indirizzo) {
    document.getElementById('confirmDeleteAddressBtn').onclick = function() {
        window.location.href = 'AreaUtente?action=deleteAddress&id_indirizzo=' + id_indirizzo;
    };
    document.getElementById('deleteAddressPopup').style.display = 'block';
}

function closePopup(popupId) {
    document.getElementById(popupId).style.display = 'none';
}


function editAddress(id) {
    var addressEntry = document.getElementById('address-' + id);
    addressEntry.classList.add('edit-mode');
    var viewModes = addressEntry.getElementsByClassName('view-mode');
    var editModes = addressEntry.getElementsByClassName('edit-mode');
    for (var i = 0; i < viewModes.length; i++) {
        viewModes[i].style.display = 'none';
    }
    for (var i = 0; i < editModes.length; i++) {
        editModes[i].style.display = 'inline';
    }
    addressEntry.querySelector('.btn-save').style.display = 'inline';
    addressEntry.querySelector('.btn-cancel').style.display = 'inline';
    addressEntry.querySelector('.btn-delete').style.display = 'none';
    addressEntry.querySelector('.btn-edit').style.display = 'none';
    validateAddressForm(document.getElementById('form-'+id));
}

function cancelEditAddress(id) {
    var addressEntry = document.getElementById('address-' + id);
    addressEntry.classList.remove('edit-mode');
    var viewModes = addressEntry.getElementsByClassName('view-mode');
    var editModes = addressEntry.getElementsByClassName('edit-mode');
    for (var i = 0; i < viewModes.length; i++) {
        viewModes[i].style.display = 'inline';
    }
    for (var i = 0; i < editModes.length; i++) {
        editModes[i].style.display = 'none';
    }
    
    addressEntry.querySelector('.errorMessages').style.display = 'none';
    addressEntry.querySelector('.btn-save').style.display = 'none';
    addressEntry.querySelector('.btn-cancel').style.display = 'none';
    addressEntry.querySelector('.btn-delete').style.display = 'inline';
    addressEntry.querySelector('.btn-edit').style.display = 'inline';
}


function validateAddressForm(form) {
    const fields = {
        nome_destinatario: /^[A-Za-zà-ùÀ-Ù '-]+$/,
        cognome_destinatario: /^[A-Za-zà-ùÀ-Ù '-]+$/,
        telefono_destinatario: /^\d{10}$/,
        via: /^[A-Za-z0-9\s,.'-]+$/,
        n_civico: /^\d+$/,
        citta: /^[A-Za-z\s]+$/,
        cap: /^\d{5}$/,
        provincia: /^[A-Za-z]{2}$/
    };

    let hasErrors = false;

    for (const field in fields) {
        const input = form[field];
        if (input && !fields[field].test(input.value)) {
            hasErrors = true;
            break; // Esci dal ciclo al primo errore
        }
    }

    const errorMessages = form.querySelector('.errorMessages');
    if (hasErrors) {
        errorMessages.innerHTML = "I campi contengono caratteri non validi. Controlla i dati inseriti.";
        return false;
    } else {
        errorMessages.innerHTML = '';
        return true;
    }
}



// Funzione per gestire il submit del modulo
function handleSubmit(event) {
    const form = event.target;
    if (!validateAddressForm(form)) {
        event.preventDefault(); // Blocca l'invio del modulo se la validazione fallisce
    }
}

// Aggiungi l'event listener per il submit su tutti i moduli
document.querySelectorAll('form').forEach(form => {
    form.addEventListener('submit', handleSubmit);
});



