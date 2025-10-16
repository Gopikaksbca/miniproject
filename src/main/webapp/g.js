
    document.getElementById("voteForm").addEventListener("submit", async (e) => {
      e.preventDefault();
      const choice = document.querySelector("input[name='candidate']:checked").value;

      // send to backend
      try {
        const res = await fetch("/submit-vote", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ candidate: choice })
        });

        const data = await res.json();
        if (data.success) {
          alert("✅ Your vote has been successfully submitted!");
          // Optionally redirect
          // window.location.href = "thankyou.html";
        } else {
          alert("❌ Vote submission failed: " + data.message);
        }
      } catch (err) {
        console.error(err);
        alert("⚠️ Error submitting vote. Please try again.");
      }
    });
  