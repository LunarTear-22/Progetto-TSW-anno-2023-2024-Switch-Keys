// Funzione per gestire l'apertura/chiusura della barra di ricerca
function toggleSearchBar() {
    const searchContainer = document.getElementById('search-container');
    const searchBar = document.getElementById('search-bar');

    
    const isActive = searchContainer.classList.contains('active');

    
    if (isActive) {
        searchContainer.classList.remove('active');
        searchBar.style.width = '0';
        searchBar.style.opacity = '0';
        clearSuggestions(); 
    } else {
        // Se non è attiva, apri la barra di ricerca
        searchContainer.classList.add('active');
        searchBar.style.width = '200px'; 
        searchBar.style.opacity = '1';
        searchBar.focus(); 
    }
}

// Funzione per gestire il clic sulla finestra
function handleClickOutside(event) {
    const searchContainer = document.getElementById('search-container');

    // Verifica se il clic è avvenuto all'interno della barra di ricerca
    if (searchContainer.contains(event.target)) {
        return; // Clic all'interno della barra di ricerca, non fare nulla
    }

    // Clic al di fuori della barra di ricerca, chiudi la barra se è aperta
    if (searchContainer.classList.contains('active')) {
        toggleSearchBar(); // Chiama la funzione per chiudere la barra di ricerca
        clearSuggestions(); // Cancella i suggerimenti
    }
}

// Aggiungi un gestore di eventi per intercettare i clic sulla finestra
document.addEventListener('click', handleClickOutside);

// Funzione per cancellare i suggerimenti
function clearSuggestions() {
    document.getElementById("suggestions").innerHTML = "";
    document.getElementById("suggestions").style.display = 'none';
}

let debounceTimer;

function fetchSuggestions(event) {
    clearTimeout(debounceTimer);
    debounceTimer = setTimeout(() => {
        let query = document.getElementById("search-bar").value.trim();
        if (query.length < 2) {
            clearSuggestions(); // Cancella i suggerimenti se la query è troppo breve
            return;
        }

        let xhr = new XMLHttpRequest();
        xhr.open("GET", `SearchServlet?query=${encodeURIComponent(query)}`, true);

        xhr.onreadystatechange = function () {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    try {
                        let suggestions = JSON.parse(xhr.responseText);
                        displaySuggestions(suggestions);
                    } catch (e) {
                        console.error('Failed to parse JSON response', e);
                    }
                } else {
                    console.error('Failed to fetch suggestions', xhr.status, xhr.statusText);
                }
            }
        };

        xhr.onerror = function () {
            console.error('Network error occurred');
        };

        xhr.send();
    }, 300); 

    
    if (event.key === 'Enter') {
        let query = document.getElementById("search-bar").value.trim();
        if (query.length > 0) {
            window.location.href = `SearchServlet?action=readByName&query=${encodeURIComponent(query)}`;
        }
    }
}

function displaySuggestions(suggestions) {
    let suggestionsContainer = document.getElementById("suggestions");
    suggestionsContainer.innerHTML = "";
    if (suggestions.length > 0) {
        suggestions.forEach(function (suggestion) {
            let suggestionItem = document.createElement("div");
            suggestionItem.className = "suggestion-item";
            suggestionItem.innerHTML = `<a href="prodotto?action=read&id_prodotto=${suggestion.idProdotto}">${suggestion.nome} - ${suggestion.prezzo} €</a>`;
            suggestionItem.addEventListener('click', function () {
                window.location.href = `prodotto?action=read&id_prodotto=${suggestion.idProdotto}`;
            });
            suggestionsContainer.appendChild(suggestionItem);
        });
        suggestionsContainer.style.display = 'block';
    } else {
        suggestionsContainer.style.display = 'none';
    }
}


document.getElementById('search-bar').addEventListener('keyup', fetchSuggestions);