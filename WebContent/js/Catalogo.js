// Catalogo.js

document.addEventListener("DOMContentLoaded", function() {
    var modal = document.getElementById("confirmModal");
    var deleteButtons = document.querySelectorAll(".delete-button");
    var confirmYes = document.getElementById("confirmYes");
    var confirmNo = document.getElementById("confirmNo");
    var productIdToDelete = null;

    // Funzione per aprire il modale
    function openModal() {
        modal.style.display = "block";
        document.body.classList.add('no-scroll'); // Disabilita lo scorrimento della pagina
    }

    // Funzione per chiudere il modale
    function closeModal() {
        modal.style.display = "none";
        document.body.classList.remove('no-scroll'); // Riabilita lo scorrimento della pagina
    }

    deleteButtons.forEach(function(button) {
        button.addEventListener("click", function(event) {
            event.preventDefault();
            productIdToDelete = this.getAttribute("data-id");
            openModal();
        });
    });

    confirmYes.onclick = function() {
        if (productIdToDelete) {
            window.location.href = "prodotto?action=delete&id_prodotto=" + productIdToDelete;
        }
    }

    confirmNo.onclick = closeModal;

  //  document.querySelector(".modal .close").onclick = closeModal;

    window.onclick = function(event) {
        if (event.target == modal) {
            closeModal();
        }
    }
});



