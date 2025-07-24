document.addEventListener("DOMContentLoaded", () => {
  fetch("http://localhost:8080/api/workers/with-attendance")
    .then(response => {
      if (!response.ok) {
        throw new Error("HTTP error " + response.status);
      }
      return response.json();
    })
    .then(workers => {
      const tbody = document.getElementById("workersTableBody");
      tbody.innerHTML = "";

      if (!Array.isArray(workers) || workers.length === 0) {
        tbody.innerHTML = "<tr><td colspan='6'>No workers found.</td></tr>";
        return;
      }

      workers.forEach((worker, index) => {
        const row = document.createElement("tr");

        row.innerHTML = `
          <td>${index + 1}</td>
          <td>${worker.idNumber}</td>
          <td>${worker.name}</td>
          <td>${worker.workerType || "-"}</td>
          <td>${worker.attendance || "0/0"}</td>
          <td>
            <a href="worker-details.html?email=${encodeURIComponent(worker.email)}">
              <i class="fas fa-eye"></i> View
            </a>
          </td>
        `;

        tbody.appendChild(row);
      });
    })
    .catch(error => {
      console.error("Error loading workers with attendance:", error);
      const tbody = document.getElementById("workersTableBody");
      tbody.innerHTML = "<tr><td colspan='6'>Failed to load workers.</td></tr>";
    });
});
