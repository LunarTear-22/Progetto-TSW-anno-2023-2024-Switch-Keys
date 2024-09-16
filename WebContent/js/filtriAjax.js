    function toggleFilters() {
        var filters = document.querySelector('.filters');
        filters.classList.toggle('active');
    }

document.getElementById('filterForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Impedisce il comportamento di sottomissione del form

    var formData = new FormData(this);

    var xhr = new XMLHttpRequest();
    xhr.open('GET', 'Filtra?' + new URLSearchParams(formData).toString(), true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
        	// Utilizza DOMParser per analizzare la risposta HTML
            var parser = new DOMParser();
            var doc = parser.parseFromString(xhr.responseText, 'text/html');
            // Seleziona solo il contenitore catalog-grid
            var newCatalogGrid = doc.querySelector('.catalog-grid').innerHTML;
            // Aggiorna il contenitore catalog-grid nella pagina
            document.querySelector('.catalog-grid').innerHTML = newCatalogGrid;
        }
    };
    xhr.send();
});
