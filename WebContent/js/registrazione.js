document.addEventListener("DOMContentLoaded", function() {
    const params = new URLSearchParams(window.location.search);
    if (params.has('success') && params.get('success') === 'true') {
        document.getElementById('successPopup').style.display = 'block';
    } else if (params.has('error')) {
        if (params.get('error') === 'true') {
            document.getElementById('generalError').classList.remove('hidden');
        } else if (params.get('error') === 'emailExists') {
            document.getElementById('emailExistsError').classList.remove('hidden');
        }
    }
    
    document.forms["registrationForm"]["email"].focus();

    // Aggiungi un listener per l'evento 'input' a tutti i campi di input
    document.querySelectorAll('input').forEach(input => {
        input.addEventListener('input', function() {
            switch (this.name) {
                case 'email':
                    validateEmail();
                    break;
                case 'nome':
                    validateNome();
                    break;
                case 'cognome':
                    validateCognome();
                    break;
                case 'password':
                    validatePassword();
                    break;
            }
        });
    });
});

function closePopup(popupId) {
    document.getElementById(popupId).style.display = 'none';
}

function redirectToLogin() {
    window.location.href = 'LoginView.jsp';
}

function validateEmail() {
    const email = document.forms["registrationForm"]["email"].value;
    const emailError = document.getElementById('emailError');

    emailError.textContent = '';

    if (!email) {
        emailError.textContent = 'Email è obbligatoria.';
        return false;
    } else {
        emailError.textContent = ''; // Nasconde l'errore se il campo è valido
    }

    return true;
}

function validateNome() {
    const nome = document.forms["registrationForm"]["nome"].value;
    const nomeError = document.getElementById('nomeError');

    nomeError.textContent = '';

    const validNamePattern = /^[a-zA-ZÀ-ÖØ-öø-ÿ\s'-]+$/; // Aggiungi i caratteri consentiti

    if (!nome) {
        nomeError.textContent = 'Nome è obbligatorio.';
        return false;
    } else if (!validNamePattern.test(nome)) {
        nomeError.textContent = 'Nome contiene caratteri non validi.';
        return false;
    } else {
        nomeError.textContent = ''; // Nasconde l'errore se il campo è valido
    }

    return true;
}

function validateCognome() {
    const cognome = document.forms["registrationForm"]["cognome"].value;
    const cognomeError = document.getElementById('cognomeError');

    cognomeError.textContent = '';

    const validSurnamePattern = /^[a-zA-ZÀ-ÖØ-öø-ÿ\s'-]+$/; // Aggiungi i caratteri consentiti

    if (!cognome) {
        cognomeError.textContent = 'Cognome è obbligatorio.';
        return false;
    } else if (!validSurnamePattern.test(cognome)) {
        cognomeError.textContent = 'Cognome contiene caratteri non validi.';
        return false;
    } else {
        cognomeError.textContent = ''; // Nasconde l'errore se il campo è valido
    }

    return true;
}

function validatePassword() {
    const password = document.forms["registrationForm"]["password"].value;
    const passwordError = document.getElementById('passwordError');

    passwordError.textContent = '';

    if (!password) {
        passwordError.textContent = 'Password è obbligatoria.';
        return false;
    } else {
        let passwordErrors = [];
        if (password.length < 8 || password.length > 16) {
            passwordErrors.push('La password deve essere tra 8 e 16 caratteri.');
        }
        if (!/[A-Z]/.test(password)) {
            passwordErrors.push('La password deve contenere almeno una lettera maiuscola.');
        }
        if (!/[0-9]/.test(password)) {
            passwordErrors.push('La password deve contenere almeno un numero.');
        }
        if (!/[?!@#\$%\^\&*\)\(+=._-]/.test(password)) {
            passwordErrors.push('La password deve contenere almeno un carattere speciale.');
        }
        if (passwordErrors.length > 0) {
            passwordError.innerHTML = passwordErrors.join('<br>');
            return false;
        } else {
            passwordError.textContent = ''; // Nasconde l'errore se il campo è valido
        }
    }

    return true;
}

function validateForm() {
    const isEmailValid = validateEmail();
    const isNomeValid = validateNome();
    const isCognomeValid = validateCognome();
    const isPasswordValid = validatePassword();

    return isEmailValid && isNomeValid && isCognomeValid && isPasswordValid;
}
