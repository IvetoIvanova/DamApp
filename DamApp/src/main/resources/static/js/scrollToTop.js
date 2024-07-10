document.addEventListener("DOMContentLoaded", function() {
    let scrollToTopButton = document.getElementById("scrollToTop");

    window.addEventListener("scroll", function() {
        if (window.scrollY > 100) {
            scrollToTopButton.style.display = "flex";
        } else {
            scrollToTopButton.style.display = "none";
        }
    });

    scrollToTopButton.addEventListener("click", function() {
        window.scrollTo({
            top: 0,
            behavior: "smooth"
        });
    });
});


