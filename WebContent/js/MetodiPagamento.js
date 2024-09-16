function closePopup(popupId) {
    document.getElementById(popupId).style.display = 'none';
}

function confirmDeletePayment(paymentId) {
    const popup = document.getElementById('deletePaymentPopup');
    const confirmBtn = document.getElementById('confirmDeletePaymentBtn');
    confirmBtn.onclick = function() {
        window.location.href = 'AreaUtente?action=deletePaymentMethod&id_metodo=' + paymentId;
    };
    popup.style.display = 'block';
}

function cancelEditPayment(id) {
    var paymentEntry = document.getElementById('payment-' + id);
    paymentEntry.classList.remove('edit-mode');
    var viewModes = paymentEntry.getElementsByClassName('view-mode');
    var editModes = paymentEntry.getElementsByClassName('edit-mode');
    for (var i = 0; i < viewModes.length; i++) {
        viewModes[i].style.display = 'inline';
    }
    for (var i = 0; i < editModes.length; i++) {
        editModes[i].style.display = 'none';
    }
    paymentEntry.querySelector('.errorMessages').style.display = 'none';
    paymentEntry.querySelector('.btn-save').style.display = 'none';
    paymentEntry.querySelector('.btn-cancel').style.display = 'none';
    paymentEntry.querySelector('.btn-delete').style.display = 'inline';
    paymentEntry.querySelector('.btn-edit').style.display = 'inline';
}

function editPayment(id) {
    var paymentEntry = document.getElementById('payment-' + id);
    paymentEntry.classList.add('edit-mode');
    var viewModes = paymentEntry.getElementsByClassName('view-mode');
    var editModes = paymentEntry.getElementsByClassName('edit-mode');
    for (var i = 0; i < viewModes.length; i++) {
        viewModes[i].style.display = 'none';
    }
    for (var i = 0; i < editModes.length; i++) {
        editModes[i].style.display = 'inline';
    }
    paymentEntry.querySelector('.btn-save').style.display = 'inline';
    paymentEntry.querySelector('.btn-cancel').style.display = 'inline';
    paymentEntry.querySelector('.btn-delete').style.display = 'none';
    paymentEntry.querySelector('.btn-edit').style.display = 'none';
    validatePaymentForm(document.getElementById('form-'+id));
}


function updateFormFields() {
    const paymentTypeElement = document.getElementById("tipo");
    if (!paymentTypeElement) {
        console.error('Elemento con ID "tipo" non trovato.');
        return;
    }

    const paymentType = paymentTypeElement.value;
    const cardFields = document.getElementsByClassName("card-field");
    const paypalFields = document.getElementsByClassName("paypal-field");

    if (paymentType === "PayPal") {
        for (let i = 0; i < cardFields.length; i++) {
            cardFields[i].style.display = "none";
        }
        for (let i = 0; i < paypalFields.length; i++) {
            paypalFields[i].style.display = "block";
        }
        document.getElementById("numero_carta").removeAttribute("required");
        document.getElementById("nome_carta").removeAttribute("required");
        document.getElementById("cognome_carta").removeAttribute("required");
        document.getElementById("scadenza").removeAttribute("required");
        document.getElementById("cvv").removeAttribute("required");
        document.getElementById("mail_paypal").setAttribute("required", "required");
    } else if (paymentType === "Carta di Credito") {
        for (let i = 0; i < cardFields.length; i++) {
            cardFields[i].style.display = "block";
        }
        for (let i = 0; i < paypalFields.length; i++) {
            paypalFields[i].style.display = "none";
        }
        document.getElementById("numero_carta").setAttribute("required", "required");
        document.getElementById("nome_carta").setAttribute("required", "required");
        document.getElementById("cognome_carta").setAttribute("required", "required");
        document.getElementById("scadenza").setAttribute("required", "required");
        document.getElementById("cvv").setAttribute("required", "required");
        document.getElementById("mail_paypal").removeAttribute("required");
    } else {
        for (let i = 0; i < cardFields.length; i++) {
            cardFields[i].style.display = "none";
        }
        for (let i = 0; i < paypalFields.length; i++) {
            paypalFields[i].style.display = "none";
        }
        document.getElementById("numero_carta").removeAttribute("required");
        document.getElementById("nome_carta").removeAttribute("required");
        document.getElementById("cognome_carta").removeAttribute("required");
        document.getElementById("scadenza").removeAttribute("required");
        document.getElementById("cvv").removeAttribute("required");
        document.getElementById("mail_paypal").removeAttribute("required");
        
    }
    
}

window.onload = function() {
    updateFormFields();
}

function validatePaymentForm(form) {
    const fields = {
        numero_carta: /^\d{16}$/, // Exactly 16 digits for the card number
        nome_carta: /^[A-Za-zà-ùÀ-Ù '-]+$/, // Name on the card should only contain letters
        cognome_carta: /^[A-Za-zà-ùÀ-Ù '-]+$/, // Surname on the card should only contain letters
        scadenza: /^\d{4}-\d{2}-\d{2}$/, 
        cvv: /^\d{3}$/, // CVV should be exactly 3 digits
        mail_paypal: /^[a-zA-Z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$/ // PayPal email format
    };

    let hasErrors = false;
    let isPayPal = form.tipo.value === 'PayPal';

    for (const field in fields) {
    	

        const input = form[field];
        console.log("Payment type is PayPal: ", isPayPal);
    	console.log("Validating field: ", field, " with value: ", input.value);
        if (input) {
            // Skip validation for fields not required based on payment type
            if (isPayPal && field !== 'mail_paypal') {
                continue; // Skip validation for Credit Card fields when it's PayPal
            }
            if (!isPayPal && field === 'mail_paypal') {
                continue; // Skip validation for PayPal email when it's a Credit Card
            }

            // Perform validation on the relevant fields
            if (!fields[field].test(input.value)) {
                hasErrors = true;
                break; // Exit the loop on the first error
            }
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

// Function to handle form submit
function handlePaymentSubmit(event) {
    const form = event.target;
    if (!validatePaymentForm(form)) {
        event.preventDefault(); // Prevent form submission if validation fails
    }
}

// Attach event listener for form submit on payment forms
document.querySelectorAll('form').forEach(form => {
    form.addEventListener('submit', handlePaymentSubmit);
});
