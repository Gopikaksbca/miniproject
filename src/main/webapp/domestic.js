// domestic.js

// Open and close modal
const faceModal = document.getElementById("faceModal");
const captureBtn = document.getElementById("captureBtn");
const retakeBtn = document.getElementById("retakeBtn");
const confirmBtn = document.getElementById("confirmBtn");
const cancelBtn = document.getElementById("cancelScanBtn");

const video = document.getElementById("video");
const canvas = document.getElementById("captureCanvas");
const context = canvas.getContext("2d");
const scanNotice = document.getElementById("scanNotice");

// Open face scan when login form is submitted
const loginForm = document.querySelector(".login-form");
loginForm.addEventListener("submit", function (e) {
  e.preventDefault(); // stop normal form submission

  // Show face scan modal
  faceModal.style.display = "flex";
  startCamera();
});

// Start camera
function startCamera() {
  navigator.mediaDevices
    .getUserMedia({ video: true })
    .then((stream) => {
      video.srcObject = stream;
    })
    .catch((err) => {
      scanNotice.style.display = "block";
      scanNotice.textContent = "Camera access denied: " + err.message;
    });
}

// Capture image
captureBtn.addEventListener("click", () => {
  context.drawImage(video, 0, 0, canvas.width, canvas.height);
  video.style.display = "none";
  canvas.style.display = "block";

  captureBtn.style.display = "none";
  retakeBtn.style.display = "inline-block";
  confirmBtn.style.display = "inline-block";
});

// Retake image
retakeBtn.addEventListener("click", () => {
  canvas.style.display = "none";
  video.style.display = "block";

  captureBtn.style.display = "inline-block";
  retakeBtn.style.display = "none";
  confirmBtn.style.display = "none";
});

// Confirm and submit
confirmBtn.addEventListener("click", () => {
  alert("✅ Face captured successfully! Now proceeding to ballot.");
  loginForm.submit(); // continue to ballot.html
});

// Cancel scan
cancelBtn.addEventListener("click", () => {
  faceModal.style.display = "none";
  stopCamera();
});

// Stop camera
function stopCamera() {
  if (video.srcObject) {
    video.srcObject.getTracks().forEach((track) => track.stop());
  }
}
