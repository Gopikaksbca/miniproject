// about.js

document.addEventListener("DOMContentLoaded", () => {
  // 1. Highlight the active navigation link
  const currentPage = window.location.pathname.split("/").pop();
  document.querySelectorAll(".nav-links a").forEach(link => {
    if (link.getAttribute("href") === currentPage) {
      link.classList.add("active");
    } else {
      link.classList.remove("active");
    }
  });

  // 2. Fade-in animation for sections
  const revealElements = document.querySelectorAll(".hero h1, .hero h2, .hero p, .hero ul, .hero li");

  const revealOnScroll = () => {
    const triggerBottom = window.innerHeight * 0.85;

    revealElements.forEach(el => {
      const boxTop = el.getBoundingClientRect().top;

      if (boxTop < triggerBottom) {
        el.style.opacity = 1;
        el.style.transform = "translateY(0)";
        el.style.transition = "all 0.8s ease-out";
      }
    });
  };

  // Initial hidden state
  revealElements.forEach(el => {
    el.style.opacity = 0;
    el.style.transform = "translateY(20px)";
  });

  window.addEventListener("scroll", revealOnScroll);
  revealOnScroll(); // trigger once on load
});
