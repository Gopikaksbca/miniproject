document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("voteForm");

  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const choiceEl = document.querySelector("input[name='candidate']:checked");
    if (!choiceEl) {
      alert("⚠️ Please select a candidate before submitting.");
      return;
    }

    const choice = choiceEl.value;

    // Send to backend API
    try {
      const res = await fetch("/submit-vote", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ candidate: choice })
      });

      const data = await res.json();

      if (data.success) {
        alert("✅ Your vote has been successfully submitted!");
        // Optionally redirect to a thank you page
        // window.location.href = "thankyou.html";
      } else {
        alert("❌ Vote submission failed: " + data.message);
      }
    } catch (err) {
      console.error("Error submitting vote:", err);
      alert("⚠️ Error submitting vote. Please try again.");
    }
  });
});
