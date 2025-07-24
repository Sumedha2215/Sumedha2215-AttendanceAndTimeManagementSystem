document.addEventListener('DOMContentLoaded', () => {
    const profileSection = document.getElementById('profileSection');
    const profileContent = document.getElementById('profileContent');
    const profileIcon = document.getElementById('profileIcon');
    const helpSection = document.getElementById('helpSection');

    const profileBtn = document.getElementById('profileBtn');
    const helpBtn = document.getElementById('helpBtn');
    const logoutBtn = document.getElementById('logoutBtn');
    const profileBackBtn = document.getElementById('profileBackBtn');
    const helpBackBtn = document.getElementById('helpBackBtn');

    const email = localStorage.getItem("workerEmail");

    function loadProfileData() {
        if (!email) {
            console.error("No worker email found in session. Redirecting to login.");
            alert("No worker email found in session. Please login again.");
            window.location.href = "index.html";
            return;
        }

        fetch(`http://localhost:8080/api/workers/${email}`)
            .then(response => {
                if (!response.ok) {
                    if (response.status === 404) {
                        throw new Error("Worker not found. Please check the email or contact support.");
                    }
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                const defaultImage = "https://cdn-icons-png.flaticon.com/512/149/149071.png";

                profileContent.innerHTML = `
                    <div class="profile-field"><strong>Name:</strong> ${data.name || 'N/A'}</div>
                    <div class="profile-field"><strong>Email:</strong> ${data.email || 'N/A'}</div>
                    <div class="profile-field"><strong>Phone:</strong> ${data.mobileNumber || 'N/A'}</div>
                    <div class="profile-field"><strong>Address:</strong> ${data.address || 'N/A'}</div>
                    <div class="profile-field"><strong>Shift:</strong> ${data.shift || 'N/A'}</div>
                    <div class="profile-field"><strong>Joining Date:</strong> ${data.joiningDate || 'N/A'}</div>
                    <div class="profile-field"><strong>Role:</strong> ${data.role || 'N/A'}</div>
                    <div class="profile-field"><strong>ID Number:</strong> ${data.idNumber || 'N/A'}</div>
                    <div class="profile-field"><strong>Type:</strong> ${data.workerType || 'N/A'}</div>
                    <div class="profile-field"><strong>Contract Days:</strong> ${data.contractDays != null ? data.contractDays : 'N/A'}</div>

                    <div class="profile-avatar-section">
                        <label for="uploadInput" style="margin-top: 10px;"><strong>Upload Profile Picture:</strong></label>
                        <input type="file" id="uploadInput" accept="image/*" />
                        <button id="removeImageBtn" style="margin-left: 10px; padding: 5px 10px;">Remove Image</button>
                        <br/>
                        <img id="avatar" src="" style="width:100px;height:100px;border-radius:50%;margin-top:10px;" />
                    </div>
                `;

                const savedImage = localStorage.getItem(`profileImage_${email}`);
                const avatarImg = document.getElementById("avatar");

                avatarImg.src = savedImage || defaultImage;
                if (profileIcon) profileIcon.src = savedImage || defaultImage;

                const uploadInputEle = document.getElementById("uploadInput");
                if (uploadInputEle) {
                    uploadInputEle.addEventListener("change", (event) => {
                        const file = event.target.files[0];
                        if (file) {
                            const reader = new FileReader();
                            reader.onload = (e) => {
                                const imageData = e.target.result;
                                avatarImg.src = imageData;
                                if (profileIcon) profileIcon.src = imageData;
                                localStorage.setItem(`profileImage_${email}`, imageData);
                            };
                            reader.readAsDataURL(file);
                        }
                    });
                }

                const removeImageBtn = document.getElementById("removeImageBtn");
                if (removeImageBtn) {
                    removeImageBtn.addEventListener("click", () => {
                        avatarImg.src = defaultImage;
                        if (profileIcon) profileIcon.src = defaultImage;
                        localStorage.removeItem(`profileImage_${email}`);
                    });
                }
            })
            .catch(error => {
                console.error("Error fetching worker details:", error);
                if (profileContent) {
                    profileContent.innerHTML = `<div style="color:red;">Error loading worker details: ${error.message}. Please try again later.</div>`;
                }
            });
    }

    function showProfile() {
        const attendanceSummary = document.querySelector('.attendance-summary');
        if (attendanceSummary) attendanceSummary.style.display = 'none';
        if (profileSection && helpSection) {
            profileSection.style.display = 'block';
            helpSection.style.display = 'none';
            loadProfileData();
            window.scrollTo({ top: 0, behavior: 'smooth' });
        }
    }

    function hideProfile() {
        if (profileSection) {
            profileSection.style.display = 'none';
            const attendanceSummary = document.querySelector('.attendance-summary');
            if (attendanceSummary) attendanceSummary.style.display = 'block';
        }
    }

    function showHelp() {
        const attendanceSummary = document.querySelector('.attendance-summary');
        if (attendanceSummary) attendanceSummary.style.display = 'none';
        if (helpSection && profileSection) {
            helpSection.style.display = 'block';
            profileSection.style.display = 'none';
            window.scrollTo({ top: 0, behavior: 'smooth' });
        }
    }

    function hideHelp() {
        if (helpSection) {
            helpSection.style.display = 'none';
            const attendanceSummary = document.querySelector('.attendance-summary');
            if (attendanceSummary) attendanceSummary.style.display = 'block';
        }
    }

    function logout() {
        localStorage.removeItem("workerEmail");
        alert("You have been logged out.");
        window.location.href = 'index.html';
    }

    if (profileBtn) profileBtn.addEventListener('click', showProfile);
    if (helpBtn) helpBtn.addEventListener('click', showHelp);
    if (logoutBtn) logoutBtn.addEventListener('click', logout);
    if (profileBackBtn) profileBackBtn.addEventListener('click', hideProfile);
    if (helpBackBtn) helpBackBtn.addEventListener('click', hideHelp);

    if (profileIcon && email) {
        const saved = localStorage.getItem(`profileImage_${email}`);
        profileIcon.src = saved || "https://cdn-icons-png.flaticon.com/512/149/149071.png";
    }

    // Attendance Summary Block: No change needed (still works or shows fallback)
    const progressRing = document.getElementById('progressRing');
    const progressText = document.getElementById('progressText');
    const attendanceSummaryContainer = document.querySelector('.attendance-summary');

    if (profileSection) profileSection.style.display = 'none';
    if (helpSection) helpSection.style.display = 'none';
    if (attendanceSummaryContainer) attendanceSummaryContainer.style.display = 'block';

    if (progressRing && progressText) {
        progressRing.style.setProperty('--progress', 0);
        progressRing.style.setProperty('--progress-color', '#eee');
        progressText.innerHTML = `<strong>0/0</strong><br>Attendance: 0%`;
    }

    if (progressRing && progressText && attendanceSummaryContainer && email) {
        fetch(`http://localhost:8080/api/attendance/summary/${email}`)
            .then(response => {
                if (!response.ok) {
                    console.warn("Attendance data not found or API error, setting to 0%.");
                    throw new Error("Attendance data not found.");
                }
                return response.json();
            })
            .then(data => {
                const totalDays = data.totalDays || 0;
                const presentDays = data.presentDays || 0;
                const percentage = totalDays > 0 ? ((presentDays / totalDays) * 100).toFixed(1) : 0;

                let progressColor = '#eee';
                if (percentage >= 85) progressColor = '#1abc9c';
                else if (percentage >= 50) progressColor = '#f39c12';
                else if (percentage > 0) progressColor = '#e74c3c';

                progressRing.style.setProperty('--progress', percentage);
                progressRing.style.setProperty('--progress-color', progressColor);
                progressText.innerHTML = `<strong>${presentDays}/${totalDays}</strong><br>Attendance: ${percentage}%`;
            })
            .catch(error => {
                console.error("Error loading attendance summary:", error);
            });
    }
});
