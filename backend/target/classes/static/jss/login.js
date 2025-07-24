document.getElementById("hamburger").addEventListener("click", () => {
  const dropdown = document.getElementById("dropdownMenu");
  dropdown.classList.toggle("show");
});

document.getElementById("logoutBtn").addEventListener("click", () => {
  localStorage.removeItem("managerEmail");
  window.location.href = "index.html";
});

document.getElementById("helpBtn").addEventListener("click", () => {
  alert("Admin Name: Admin\nEmail: admin@example.com\nPhone: +91-9876543210");
});

const profileBtn = document.getElementById("profileBtn");
const profileModal = document.getElementById("profileModal");
const closeModal = document.getElementById("closeModal");

profileBtn.addEventListener("click", () => {
  profileModal.style.display = "block";
  loadManagerProfile();
});

closeModal.addEventListener("click", () => {
  profileModal.style.display = "none";
});

window.onclick = function (event) {
  if (event.target === profileModal) {
    profileModal.style.display = "none";
  }
};

function loadManagerProfile() {
  document.getElementById("managerName").textContent = "Manager 1";
  document.getElementById("managerEmail").textContent = "manager1@example.com";
  document.getElementById("managerShift").textContent = "Morning";
}

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
  const defaultImg = "https://via.placeholder.com/40x40?text=👤";
  profileImage.src = defaultImg;
  localStorage.removeItem("managerProfileImage");
});

window.addEventListener("load", () => {
  const savedImg = localStorage.getItem("managerProfileImage");
  if (savedImg) {
    profileImage.src = savedImg;
  }
});
