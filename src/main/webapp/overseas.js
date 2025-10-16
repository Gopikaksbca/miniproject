// overseas.js

document.addEventListener("DOMContentLoaded", () => {
  const form = document.querySelector(".login-form");

  form.addEventListener("submit", (e) => {
    e.preventDefault(); // Stop default submission until validated

    const passport = document.getElementById("passport").value.trim();
    const country = document.getElementById("country").value.trim();
    const dob = document.getElementById("dob").value.trim();
    const voterFile = document.getElementById("voterFile").files[0];
    const passFile = document.getElementById("passFile").files[0];

    let errors = [];

    // Basic validations
    if (passport.length < 6) {
      errors.push("Passport number must be at least 6 characters.");
    }
    if (country === "") {
      errors.push("Country of residence is required.");
    }
    if (!dob) {
      errors.push("Date of Birth is required.");
    }
    if (!voterFile) {
      errors.push("Please upload your Voter ID.");
    }
    if (!passFile) {
      errors.push("Please upload your Passport file.");
    }

    if (errors.length > 0) {
      alert("⚠️ Please fix the following:\n\n" + errors.join("\n"));
      return;
    }

    // Confirmation
    const confirmLogin = confirm(
      `✅ Overseas Login Details:\n\nPassport: ${passport}\nCountry: ${country}\nDOB: ${dob}\n\nProceed to ballot?`
    );

    if (confirmLogin) {
      form.submit(); // continue to ballot.html
    }
  });
});
