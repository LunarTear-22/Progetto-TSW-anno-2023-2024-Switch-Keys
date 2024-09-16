document.addEventListener('DOMContentLoaded', () => {
    console.log("DOM content loaded");

    // Ottieni riferimenti agli elementi della pagina
    const categoriaSelect = document.getElementById('categoria');
    const tipo_div = document.getElementById('tipo-div');
    const tipoSelect = document.getElementById('tipo-select');
    const tipoContainer = document.getElementById('tipo-container');
    const accessoriInput = document.getElementById('accessori-input');
    const tipoInput = document.getElementById('tipo-input');
    const materialeInput = document.querySelector('input[name="materiale"]');
    const marcaInput = document.querySelector('input[name="marca"]');
    const tecnologieSezione = document.getElementById('tecnologie-sezione');
    const cablata = document.getElementById('cablata');
    const wifi = document.getElementById('wifi');
    const bluetooth = document.getElementById('bluetooth');
    const tecnologieError = document.getElementById('tecnologie-error');
    const fileInput = document.getElementById('fileInput');
    const errorMessage = document.getElementById('error-message');
    const errorMessageInput = document.getElementById('error-message-input');

    // Verifica che tutti gli elementi siano stati trovati
    if (!tipo_div || !categoriaSelect || !tipoSelect || !tipoContainer || !accessoriInput || !tipoInput || !materialeInput || !marcaInput || !tecnologieSezione || !cablata || !wifi || !bluetooth || !tecnologieError || !fileInput || !errorMessage) {
        console.error("Uno o più elementi non sono stati trovati nel DOM.");
        return;
    }

    // Definisci le opzioni per ciascuna categoria
    const opzioniTipo = {
        'Tastiera': ['65%', '75%', '80%', '100%'],
        'Keycaps': ['Cherry', 'OEM', 'XDA', 'MDA', 'MAO'],
        'Switch': ['Tattili', 'Lineari', 'Clicky'],
    };

    // Funzione per aggiornare le opzioni del tipo
    const aggiornaTipo = (categoria) => {
        tipoSelect.innerHTML = '';
        tipoContainer.classList.add('hidden');
        accessoriInput.classList.add('hidden');
        tecnologieSezione.classList.add('hidden');

        if (categoria) {
            tipo_div.classList.remove('hidden');
            if (categoria === 'Accessori') {
                accessoriInput.classList.remove('hidden');
                tipoInput.value = "";
                tecnologieSezione.classList.remove('hidden');
            } else if (categoria in opzioniTipo) {
                const defaultPlaceholder = document.createElement('option');
                defaultPlaceholder.value = "";
                defaultPlaceholder.textContent = 'Seleziona il tipo';
                defaultPlaceholder.disabled = true;
                defaultPlaceholder.selected = true;
                tipoSelect.appendChild(defaultPlaceholder);

                opzioniTipo[categoria].forEach(tipo => {
                    const option = document.createElement('option');
                    option.value = tipo;
                    option.textContent = tipo;
                    tipoSelect.appendChild(option);
                });

                tipoContainer.classList.remove('hidden');
                if (categoria === 'Tastiera' || categoria === 'Accessori') {
                    tecnologieSezione.classList.remove('hidden');
                }
            }
        }
    };

    // Gestisci il cambiamento della selezione della categoria
    categoriaSelect.addEventListener('change', function () {
        aggiornaTipo(this.value);
    });

    // Forza il trigger dell'evento per settare lo stato iniziale
    aggiornaTipo(categoriaSelect.value);

    // Aggiungi event listener per sincronizzare tipoSelect e tipoInput
    tipoSelect.addEventListener('change', function () {
        if (tipoSelect.value) {
            tipoInput.removeAttribute('required');
            accessoriInput.classList.add('hidden');
            tipoInput.value = tipoSelect.value;
        } else {
            tipoInput.setAttribute('required', 'required');
            accessoriInput.classList.remove('hidden');
        }
    });

    tipoInput.addEventListener('input', function () {
        if (tipoInput.value) {
            tipoSelect.removeAttribute('required');
        } else {
            tipoSelect.setAttribute('required', 'required');
        }
    });

    // Funzione per validare il file
    const validaFile = () => {
        const file = fileInput.files[0];
        const allowedTypes = ['image/jpeg', 'image/png'];
        const maxSize = 5 * 1024 * 1024; // 5MB in bytes

        if (file) {
            if (!allowedTypes.includes(file.type)) {
                errorMessage.textContent = 'File non valido. Sono accettate solo immagini JPG, PNG.';
                return false;
            }
            if (file.size > maxSize) {
                errorMessage.textContent = 'Il file è troppo grande. La dimensione massima è 5MB.';
                return false;
            }
            if (!/^[\w,\s-]+\.(jpg|jpeg|png)$/i.test(file.name)) {
                errorMessage.textContent = 'Nome file non valido.';
                return false;
            }
            errorMessage.textContent = '';
            return true;
        }
        return true; // Se non c'è nessun file, consideriamo la validazione come passata
    };

    // Funzione per aprire il modale
    function openSuccessModal() {
        var modal = document.getElementById("successModal");

        modal.style.display = "block";
        document.body.classList.add('no-scroll'); // Disabilita lo scorrimento della pagina

        // Chiudi il modale automaticamente dopo un certo tempo (es. 2 secondi)
        setTimeout(function() {
            modal.style.display = "none";
            document.body.classList.remove('no-scroll'); // Riabilita lo scorrimento della pagina
        }, 2000); // Modifica 2000 per il tempo desiderato in millisecondi
    }

    // Unico event listener per gestire il submit
    document.getElementById('myForm').addEventListener('submit', function (event) {
        let errorMessageText = '';
        let formIsValid = true;
        const nome = document.querySelector('input[name="nome"]').value;
        const prezzo = document.querySelector('input[name="prezzo"]').value;
        const iva = document.querySelector('input[name="iva"]').value;
        const quantita = document.querySelector('input[name="quantita"]').value;
        const descrizione = document.querySelector('textarea[name="descrizione"]').value;

        // Controllo presenza di caratteri pericolosi
        const dangerousPattern = /<script>|<\/script>|<|>|alert|SELECT|INSERT|DELETE|UPDATE/i;

        // Validazione nome prodotto
        if (dangerousPattern.test(nome)) {
            errorMessageText += 'Il nome del prodotto contiene caratteri non validi.\n';
            formIsValid = false;
        }

        // Validazione prezzo (controllo se è un numero)
        if (isNaN(prezzo) || prezzo <= 0) {
            errorMessageText += 'Inserisci un prezzo valido.\n';
            formIsValid = false;
        }

        // Validazione IVA
        if (isNaN(iva) || iva < 0) {
            errorMessageText += 'Inserisci un valore valido per l\'IVA.\n';
            formIsValid = false;
        }

        // Validazione quantità
        if (isNaN(quantita) || quantita < 0) {
            errorMessageText += 'Inserisci una quantità valida.\n';
            formIsValid = false;
        }

        // Validazione descrizione
        if (dangerousPattern.test(descrizione)) {
            errorMessageText += 'La descrizione contiene caratteri non validi.\n';
            formIsValid = false;
        }

        // Validazione tipo-input
        if (dangerousPattern.test(tipoInput.value)) {
            errorMessageText += 'Il Tipo contiene caratteri non validi.\n';
            formIsValid = false;
        }

        // Validazione materiale
        if (materialeInput && dangerousPattern.test(materialeInput.value)) {
            errorMessageText += 'Il Materiale contiene caratteri non validi.\n';
            formIsValid = false;
        }

        // Validazione marca
        if (marcaInput && dangerousPattern.test(marcaInput.value)) {
            errorMessageText += 'La Marca contiene caratteri non validi.\n';
            formIsValid = false;
        }

        // Verifica selezione tecnologie
        if (categoriaSelect.value === 'Tastiera' && !cablata.checked && !wifi.checked && !bluetooth.checked) {
            tecnologieError.style.display = 'block';
            formIsValid = false;
        } else {
            tecnologieError.style.display = 'none';
        }

        // Verifica selezione tipo
        if (!tipoSelect.value && !tipoInput.value) {
            errorMessageInput.textContent = 'Inserisci il tipo.';
            formIsValid = false;
        } else {
            errorMessageInput.textContent = '';
        }

        // Verifica file
        if (!validaFile()) {
            formIsValid = false;
        }

        // Se ci sono errori, blocca l'invio del form e mostra i messaggi di errore
        if (!formIsValid) {
            event.preventDefault();
            document.querySelector('.errorMessages').innerText = errorMessageText;
        } else {
            event.preventDefault(); // Blocca l'invio immediato del modulo
            openSuccessModal(); // Mostra il popup

            setTimeout(() => {
                document.getElementById('myForm').submit(); // Invia il modulo dopo aver mostrato il modale
            }, 2000); // Modifica questo valore se necessario
        }
    });

    // Verifica file ogni volta che viene cambiato
    fileInput.addEventListener('change', function () {
        validaFile();
    });
});


