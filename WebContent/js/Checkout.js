// Function to toggle visibility of sections
function toggleSection(contentId, iconId) {
    const content = document.getElementById(contentId);
    const icon = document.getElementById(iconId);
    if (content.classList.contains('hidden')) {
        content.classList.remove('hidden');
        icon.classList.add('rotated');
    } else {
        content.classList.add('hidden');
        icon.classList.remove('rotated');
    }
}

document.addEventListener("DOMContentLoaded", function() {
    console.log("DOM fully loaded and parsed.");

    // Selettori per i popup e gli overlay
    const addAddressLink = document.querySelector("#add-address-link");
    const addPaymentLink = document.querySelector("#add-payment-link");
    const addressOverlay = document.querySelector("#address-overlay");
    const paymentOverlay = document.querySelector("#payment-overlay");
    const addressCloseBtn = addressOverlay ? addressOverlay.querySelector(".close-btn") : null;
    const paymentCloseBtn = paymentOverlay ? paymentOverlay.querySelector(".close-btn") : null;

    console.log("Selectors initialized.");

    // Funzione per mostrare un popup e disabilitare lo scorrimento
    function showPopup(overlay) {
        if (overlay) {
            console.log("Showing popup for:", overlay.id);
            overlay.style.display = "flex";
            document.body.classList.add('no-scroll');
        }
    }

    // Funzione per nascondere un popup e abilitare lo scorrimento
    function hidePopup(overlay) {
        if (overlay) {
            console.log("Hiding popup for:", overlay.id);
            overlay.style.display = "none";
            document.body.classList.remove('no-scroll');
        }
    }

    // Mostra il popup per aggiungere un indirizzo
    if (addAddressLink) {
        addAddressLink.addEventListener("click", function(e) {
            e.preventDefault();
            console.log("Add address link clicked.");
            showPopup(addressOverlay);
        });
    }

    // Mostra il popup per aggiungere un metodo di pagamento
    if (addPaymentLink) {
        addPaymentLink.addEventListener("click", function(e) {
            e.preventDefault();
            console.log("Add payment link clicked.");
            showPopup(paymentOverlay);
        });
    }

    // Chiudi il popup per aggiungere un indirizzo
    if (addressCloseBtn) {
        addressCloseBtn.addEventListener("click", function() {
            console.log("Close button clicked for address popup.");
            hidePopup(addressOverlay);
        });
    }

    // Chiudi il popup per aggiungere un metodo di pagamento
    if (paymentCloseBtn) {
        paymentCloseBtn.addEventListener("click", function() {
            console.log("Close button clicked for payment popup.");
            hidePopup(paymentOverlay);
        });
    }

    // Chiudi il popup se si clicca fuori dal contenuto del popup
    window.addEventListener("click", function(e) {
        if (e.target === addressOverlay) {
            console.log("Clicked outside address popup.");
            hidePopup(addressOverlay);
        }
        if (e.target === paymentOverlay) {
            console.log("Clicked outside payment popup.");
            hidePopup(paymentOverlay);
        }
    });

    // Funzione per aggiornare i campi del modulo in base al tipo di pagamento selezionato
    function updateFormFields() {
        const paymentTypeElement = document.getElementById("tipo");
        if (!paymentTypeElement) {
            console.error('Elemento con ID "tipo" non trovato.');
            return;
        }

        const paymentType = paymentTypeElement.value;
        console.log("Payment type selected:", paymentType);
        
        const cardFields = document.querySelector(".card-field");
        const paypalFields = document.querySelector(".paypal-field");

        if (paymentType === "PayPal") {
            if (cardFields) {
                cardFields.style.display = "none";
            }
            if (paypalFields) {
                paypalFields.style.display = "block";
            }
            setRequiredAttributes({
                "numero_carta": false,
                "nome_carta": false,
                "cognome_carta": false,
                "scadenza": false,
                "cvv": false,
                "mail_paypal": true
            });
        } else if (paymentType === "Carta di Credito") {
            if (cardFields) {
                cardFields.style.display = "block";
            }
            if (paypalFields) {
                paypalFields.style.display = "none";
            }
            setRequiredAttributes({
                "numero_carta": true,
                "nome_carta": true,
                "cognome_carta": true,
                "scadenza": true,
                "cvv": true,
                "mail_paypal": false
            });
        } else {
            if (cardFields) {
                cardFields.style.display = "none";
            }
            if (paypalFields) {
                paypalFields.style.display = "none";
            }
            setRequiredAttributes({
                "numero_carta": false,
                "nome_carta": false,
                "cognome_carta": false,
                "scadenza": false,
                "cvv": false,
                "mail_paypal": false
            });
        }
    }

    // Funzione per impostare o rimuovere l'attributo 'required' dai campi del modulo
    function setRequiredAttributes(fields) {
        for (const [id, required] of Object.entries(fields)) {
            const element = document.getElementById(id);
            if (element) {
                if (required) {
                    element.setAttribute("required", "required");
                    console.log("Set required attribute for:", id);
                } else {
                    element.removeAttribute("required");
                    console.log("Removed required attribute for:", id);
                }
            }
        }
    }

    // Aggiungi l'evento change al selettore del tipo di pagamento
    const paymentTypeElement = document.getElementById("tipo");
    if (paymentTypeElement) {
        paymentTypeElement.addEventListener("change", updateFormFields);
    }

    // Aggiorna i campi al caricamento della pagina
    updateFormFields();

    // Selettori per il popup di conferma e overlay
    const confirmationOverlay = document.querySelector("#confirmation-overlay");
    const confirmationPopup = document.querySelector("#confirmation-popup");

    // Mostra il popup di conferma se l'ordine è confermato
    const orderConfirmed = document.body.getAttribute('data-order-confirmed') === 'true';
    if (orderConfirmed) {
        console.log("Order confirmed. Showing confirmation popup.");
        showPopup(confirmationOverlay);
        if (confirmationPopup) {
            confirmationPopup.classList.add('active');
        }
    }

    // Chiudi il popup di conferma quando si clicca sull'overlay
    if (confirmationOverlay) {
        confirmationOverlay.addEventListener("click", function(e) {
            if (e.target === confirmationOverlay) {
                console.log("Clicked outside confirmation popup.");
                hidePopup(confirmationOverlay);
                if (confirmationPopup) {
                    confirmationPopup.classList.remove('active');
                }
            }
        });
    }

    
});



document.addEventListener("DOMContentLoaded", function () {
    console.log("La pagina è stata caricata.");
    
    const addressForm = document.getElementById("add-address-form");
    const PaymentForm = document.getElementById("add-payment-form")
    const checkoutForm = document.getElementById("Checkout-form");
    const errorMessage = document.getElementById("error-message");

    // Funzione di validazione per il form degli indirizzi
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
                input.classList.add("input-error");  // Aggiungi una classe di errore
                console.log(`Errore nella validazione del campo: ${field}`);
            } else if (input) {
                input.classList.remove("input-error");  // Rimuovi la classe se valido
            }
        }

        const errorMessages = form.querySelector('.errorMessages');
        if (hasErrors) {
            errorMessages.innerHTML = "Alcuni campi dell'indirizzo non contengono caratteri validi. Verifica e riprova.";
            return false;
        } else {
            errorMessages.innerHTML = '';
            return true;
        }
    }

    // Funzione di validazione per il form dei pagamenti
    function validatePaymentForm(form) {
        const fields = {
            nome_carta: /^[A-Za-zà-ùÀ-Ù '-]+$/,  // Nome sulla carta
            cognome_carta: /^[A-Za-zà-ùÀ-Ù '-]+$/,  // Cognome sulla carta
            numero_carta: /^\d{16}$/,
            scadenza: /^\d{4}-\d{2}-\d{2}$/,  
            cvv: /^\d{3}$/,
            mail_paypal: /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/
        };

        const paymentType = form["tipo"].value;
        let hasErrors = false;
        
        // Se il metodo di pagamento è PayPal, verifica solo il campo email PayPal
        if (paymentType === "PayPal") {
            const input = form['mail_paypal'];
            if (input && !fields['mail_paypal'].test(input.value)) {
                hasErrors = true;
                input.classList.add("input-error");
                console.log(`Errore nella validazione del campo mail_paypal`);
            } else if (input) {
                input.classList.remove("input-error");
            }
        } else if (paymentType === "Carta di Credito") {
            // Se è carta di credito, verifica i campi della carta
            ['nome_carta', 'cognome_carta', 'numero_carta', 'scadenza', 'cvv'].forEach(field => {
                const input = form[field];
                if (input && !fields[field].test(input.value)) {
                    hasErrors = true;
                    input.classList.add("input-error");
                    console.log(`Errore nella validazione del campo: ${field}`);
                } else if (input) {
                    input.classList.remove("input-error");
                }
            });
        }

        const errorMessages = form.querySelector('.errorMessages');
        if (hasErrors) {
            errorMessages.innerHTML = "Alcuni campi del pagamento non contengono caratteri validi. Verifica e riprova.";
            return false;
        } else {
            errorMessages.innerHTML = '';
            return true;
        }
    }
    
    addressForm.addEventListener("submit", function (event) {
    	 console.log("Evento di submit intercettato.");
    	// Esegui le validazioni sui form degli indirizzi e dei pagamenti
    	    const isAddressValid = validateAddressForm(addressForm);
    	    if(!isAddressValid){
    	    	event.preventDefault();
    	    }
    });
    
    PaymentForm.addEventListener("submit", function (event) {
   	 	console.log("Evento di submit intercettato.");
   	 	// Esegui le validazioni sui form degli indirizzi e dei pagamenti
   	 	const isPaymentValid = validatePaymentForm(PaymentForm);
 	    if(!isPaymentValid){
 	    	event.preventDefault();
 	    }
    });
    
    

    // Gestione del submit del form di checkout
    checkoutForm.addEventListener("submit", function (event) {
        console.log("Evento di submit intercettato.");

        // Verifica il metodo di pagamento e l'indirizzo selezionato
        const metodoScelto = window.metodo_scelto;  // Supponendo che siano passati come variabili globali
        const indirizzoScelto = window.indirizzo_scelto;

        console.log("Metodo scelto:", metodoScelto);
        console.log("Indirizzo scelto:", indirizzoScelto);

        

        if (!metodoScelto || !indirizzoScelto) {
            console.log("Validazione fallita: metodo, indirizzo o dati non validi.");

            // Mostra un messaggio di errore se necessario
            errorMessage.style.display = "block";
            errorMessage.textContent = "Per favore, seleziona un indirizzo di spedizione, un metodo di pagamento.";
            event.preventDefault(); // Blocca l'invio del form se la validazione fallisce
        } else {
            console.log("Validazione passata: form inviato correttamente.");
            errorMessage.style.display = "none"; // Nasconde il messaggio di errore
        }
    });
});
