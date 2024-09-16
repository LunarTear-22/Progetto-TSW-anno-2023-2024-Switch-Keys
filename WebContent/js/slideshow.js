let slideIndex = 0;
let slideInterval;
let isNavigating = false;

document.addEventListener("DOMContentLoaded", function() {
    showSlides();
});

function showSlides() {
    let slides = document.getElementsByClassName("mySlides");

    for (let i = 0; i < slides.length; i++) {
        slides[i].style.display = "none";  
    }
    slideIndex++;
    if (slideIndex > slides.length) {
        slideIndex = 1;
    }

    slides[slideIndex - 1].style.display = "block";

    slideInterval = setTimeout(showSlides, 5000); // Cambia immagine ogni 5 secondi
}

function plusSlides(n) {
    if (isNavigating) return; // Se stiamo già navigando, non fare nulla
    isNavigating = true; // Imposta il flag di navigazione

    clearTimeout(slideInterval); // Ferma l'animazione automatica
    showCurrentSlide(slideIndex + n);
    setTimeout(() => isNavigating = false, 1000); // Tempo di debounce di 1 secondo

    // Riavvia l'animazione dopo 10 secondi
    setTimeout(() => {
        clearTimeout(slideInterval); // Cancella il timeout corrente per evitare sovrapposizioni
        slideInterval = setTimeout(showSlides, 5000); // Riprendi l'animazione automatica
    }, 10000); // Ritardo di 10 secondi
}

function showCurrentSlide(n) {
    let slides = document.getElementsByClassName("mySlides");

    if (n > slides.length) { 
        slideIndex = 1;
    } else if (n < 1) { 
        slideIndex = slides.length;
    } else {
        slideIndex = n;
    }

    for (let i = 0; i < slides.length; i++) {
        slides[i].style.display = "none";  
    }

    slides[slideIndex - 1].style.display = "block";
}
