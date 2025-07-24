// === Dropdown: Toggle + Auto Close ===
const hamburger = document.getElementById("hamburger");
const dropdown = document.getElementById("dropdownMenu");

hamburger.addEventListener("click", () => {
  dropdown.classList.toggle("show");
});

window.addEventListener("click", function (event) {
  if (!dropdown.contains(event.target) && !hamburger.contains(event.target)) {
    dropdown.classList.remove("show");
  }
});

// === Logout handler ===
document.getElementById("logoutBtn").addEventListener("click", () => {
  dropdown.classList.remove("show");
  localStorage.removeItem("managerEmail");
  window.location.href = "index.html";
});

// === Help handler ===
document.getElementById("helpBtn").addEventListener("click", () => {
  dropdown.classList.remove("show");
  alert("Admin Name: Admin\nEmail: admin@example.com\nPhone: +91-9876543210");
});

// === Profile Modal Open/Close ===
const profileBtn = document.getElementById("profileBtn");
const profileModal = document.getElementById("profileModal");
const closeModal = document.getElementById("closeModal");

profileBtn.addEventListener("click", () => {
  dropdown.classList.remove("show");
  profileModal.style.display = "block";
  loadManagerProfile();
});

closeModal.addEventListener("click", () => {
  profileModal.style.display = "none";
});

window.addEventListener("click", function (event) {
  if (event.target === profileModal) {
    profileModal.style.display = "none";
  }
});

// === Load Manager Profile ===
function loadManagerProfile() {
  document.getElementById("managerName").textContent = "Manager 1";
  document.getElementById("managerEmail").textContent = "manager1@example.com";
  document.getElementById("managerShift").textContent = "Morning";
}

// === Image Upload and Storage ===
const profileImage = document.getElementById("profileImage");
const imageInput = document.getElementById("imageInput");
const removeImageBtn = document.getElementById("removeImageBtn");

imageInput.addEventListener("change", function () {
  const file = imageInput.files[0];
  if (file) {
    const reader = new FileReader();
    reader.onload = function (e) {
      profileImage.src = e.target.result;
      localStorage.setItem("managerProfileImage", e.target.result);
    };
    reader.readAsDataURL(file);
  }
});

removeImageBtn.addEventListener("click", () => {
  const defaultImg = "images/default-avatar.png";
  profileImage.src = defaultImg;
  localStorage.removeItem("managerProfileImage");
});

// === Load Profile Image on Page Load ===
window.addEventListener("load", () => {
  const savedImg = localStorage.getItem("managerProfileImage");
  profileImage.src = savedImg ? savedImg : "images/default-avatar.png";

  // ✅ View Workers handler
  document.getElementById("viewWorkersBtn").addEventListener("click", () => {
    dropdown.classList.remove("show");
    window.location.href = "all-workers.html";
  });

  // ✅ Mark Attendance handler
  const markAttendanceBtn = document.getElementById("markAttendanceBtn");
  if (markAttendanceBtn) {
    markAttendanceBtn.addEventListener("click", () => {
      dropdown.classList.remove("show");
      window.location.href = "mark-attendance.html";
    });
  }
});
