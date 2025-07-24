// jss/profile.js

document.addEventListener("DOMContentLoaded", () => {
  const email = localStorage.getItem("workerEmail");

  if (!email) {
    document.getElementById("profileDetails").innerHTML = "<p>Email not found. Please login again.</p>";
    return;
  }

  fetch(`http://localhost:8080/api/workers/email?email=${encodeURIComponent(email)}`)
    .then(response => {
      if (!response.ok) throw new Error("Worker not found");
      return response.json();
    })
    .then(worker => {
      const formattedDate = new Date(worker.joiningDate).toLocaleDateString();

      const html = `
        <div class="info"><strong>Name:</strong> ${worker.name}</div>
        <div class="info"><strong>Email:</strong> ${worker.email}</div>
        <div class="info"><strong>Mobile:</strong> ${worker.mobileNumber}</div>
        <div class="info"><strong>Address:</strong> ${worker.address}</div>
        <div class="info"><strong>ID Number:</strong> ${worker.idNumber}</div>
        <div class="info"><strong>Shift:</strong> ${worker.shift}</div>
        <div class="info"><strong>Joining Date:</strong> ${formattedDate}</div>
      `;

      document.getElementById("profileDetails").innerHTML = html;
    })
    .catch(err => {
      document.getElementById("profileDetails").innerHTML = `<p>Error: ${err.message}</p>`;
    });
});
