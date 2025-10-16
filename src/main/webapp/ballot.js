document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("voteForm");

  form.addEventListener("submit", async (e) => {
    e.preventDefault(); // prevent default to handle submission manually

    const choiceEl = document.querySelector("input[name='candidate']:checked");
    if (!choiceEl) {
      alert("⚠️ Please select a candidate before submitting.");
      return;
    }

    const candidate = choiceEl.value;
    const voterId = document.getElementById("voterId").value;
    const voterType = document.getElementById("voterType").value;

    const formData = new FormData();
    formData.append("candidate", candidate);
    formData.append("voterId", voterId);
    formData.append("voterType", voterType);

    try {
      const res = await fetch("BallotServlet", { method: "POST", body: formData });
      const text = await res.text();
      alert(text); // show servlet response
      window.location.href = "index.html"; // redirect to home
    } catch (err) {
      console.error("Vote submission failed:", err);
      alert("⚠️ Vote submission failed. Please try again.");
    }
  });
});
