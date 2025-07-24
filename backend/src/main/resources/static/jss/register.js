document.getElementById("registerForm").addEventListener("submit", async function (e) {
  e.preventDefault();

  const name = document.getElementById("name").value;
  const email = document.getElementById("email").value.trim().toLowerCase();
  const password = document.getElementById("password").value;
  const confirmPassword = document.getElementById("confirmPassword").value;
  const mobileNumber = document.getElementById("mobileNumber").value;
  const address = document.getElementById("address").value;
  const idNumber = document.getElementById("idNumber").value;
  const shift = document.getElementById("shift").value;
  const joiningDate = document.getElementById("joiningDate").value;

  if (password !== confirmPassword) {
    alert("❌ Passwords do not match");
    return;
  }

  const data = {
    name,
    email,
    password,
    confirmPassword,
    mobileNumber,
    address,
    idNumber,
    shift,
    joiningDate
  };

  try {
    const response = await fetch("http://localhost:8080/api/auth/register", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(data)
    });

    if (response.ok) {
      // ✅ Save email and redirect to profile
      localStorage.setItem("workerEmail", email);
      window.location.href = "worker-profile.html";
    } else {
      const error = await response.text();
      alert("❌ " + error);
    }
  } catch (err) {
    console.error("Registration error:", err);
    alert("❌ Error during registration");
  }
});
