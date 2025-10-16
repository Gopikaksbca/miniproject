document.addEventListener("DOMContentLoaded", () => {
  // Highlight active nav link
  const currentPage = window.location.pathname.split("/").pop();
  const navLinks = document.querySelectorAll(".nav-links a");

  navLinks.forEach(link => {
    link.classList.toggle("active", link.getAttribute("href") === currentPage);
  });

  // Smooth scroll
  document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener("click", function (e) {
      e.preventDefault();
      const target = document.querySelector(this.getAttribute("href"));
      if (target) target.scrollIntoView({ behavior: "smooth" });
    });
  });

  // Welcome popup for index.html
  if (currentPage === "index.html") {
    setTimeout(() => {
      alert("Welcome to the Voting Portal!");
    }, 1000);
  }
});
