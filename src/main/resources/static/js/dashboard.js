// --- Config ---
const API_BASE_URLS = {
  SUPERADMIN: 'http://localhost:8080/superadmin/api',
  MANAGER: 'http://localhost:8080/manager/api',
  ADMIN: 'http://localhost:8080/admin/api',
  STUDENT: 'http://localhost:8080/student/api'
};

let jwtToken = localStorage.getItem('jwt_token');
let currentRole = localStorage.getItem('user_role');

if (!jwtToken || !currentRole) {
  alert('Unauthorized! Please login.');
  window.location.href = '/login.html';
}

// --- Dictionary of role-based menus ---
const menus = {
  SUPERADMIN: [
    { id: "dashboard", name: "Dashboard" },
    { id: "manageManagers", name: "Manage Managers" },
    { id: "manageAdmins", name: "Manage Admins" },
    { id: "manageStudents", name: "Manage Students" },
    { id: "assignTasks", name: "Assign Tasks" },
    { id: "logout", name: "Logout" }
  ],
  MANAGER: [
    { id: "profile", name: "Profile" },
    { id: "assignUsers", name: "Assign Users" },
    { id: "tracking", name: "Tracking" },
    { id: "logout", name: "Logout" }
  ],
  ADMIN: [
    { id: "profile", name: "Profile" },
    { id: "studentManagement", name: "Student Management" },
    { id: "tracking", name: "Tracking" },
    { id: "logout", name: "Logout" }
  ],
  STUDENT: [
    { id: "profile", name: "Profile" },
    { id: "trackStatus", name: "Track Status" },
    { id: "uploadDocs", name: "Upload Documents" },
    { id: "logout", name: "Logout" }
  ]
};

// --- Utility functions ---
function capitalize(s) {
  return s.charAt(0).toUpperCase() + s.slice(1).toLowerCase();
}

function setHeader(text) {
  document.getElementById("topbarTitle").textContent = text;
}

function clearMainContent() {
  document.getElementById("mainContent").innerHTML = "";
}

function renderMessage(msg) {
  document.getElementById("mainContent").innerHTML = `<p>${msg}</p>`;
}

function setActiveMenu(menuId) {
  menus[currentRole].forEach(menu => {
    const el = document.getElementById("nav-" + menu.id);
    if (el) el.classList.toggle("active", menu.id === menuId);
  });
}

// --- Render sidebar ---
function renderSidebar() {
  const sidebar = document.getElementById("sidebarMenu");
  sidebar.innerHTML = `<h3>${capitalize(currentRole)}</h3><ul></ul>`;
  const ul = sidebar.querySelector("ul");
  menus[currentRole].forEach(menu => {
    const li = document.createElement("li");
    li.textContent = menu.name;
    li.id = "nav-" + menu.id;
    li.onclick = () => loadPanel(menu.id);
    ul.appendChild(li);
  });
  setActiveMenu(menus[currentRole][0].id);
  loadPanel(menus[currentRole][0].id);
}

// --- Load panel content based on menu ---
function loadPanel(id) {
  clearMainContent();
  setActiveMenu(id);
  setHeader(capitalize(id));

  switch (id) {
    // Superadmin
    case "dashboard":
      loadDashboard(); break;
    case "manageManagers":
      loadUserPanel("manager"); break;
    case "manageAdmins":
      loadUserPanel("admin"); break;
    case "manageStudents":
      loadUserPanel("student"); break;
    case "assignTasks":
      loadAssignTasks(); break;

    // Manager
    case "profile":
      loadProfile(); break;
    case "assignUsers":
      loadAssignUsers(); break;
    case "tracking":
      loadTracking(); break;

    // Admin
    case "studentManagement":
      loadStudentManagement(); break;

    // Student
    case "trackStatus":
      loadTrackStatus(); break;
    case "uploadDocs":
      loadUploadDocuments(); break;

    // Logout
    case "logout":
      showLogoutDialog(); break;

    default:
      renderMessage("Page not implemented.");
  }
}

// --- Panel implementations ---

// 1. Dashboard
function loadDashboard() {
  renderMessage(`Welcome, ${capitalize(currentRole)}!`);
}

// 2. User Management Panel (Superadmin)
function loadUserPanel(role) {
  const content = `
    <div class="panel">
      <h2>All ${capitalize(role)}s</h2>
      <form id="addUserForm">
        <input type="text" id="usernameInput" placeholder="Username" required>
        <input type="password" id="passwordInput" placeholder="Password" required>
        <input type="email" id="emailInput" placeholder="Email" required>
        <input type="text" id="fullNameInput" placeholder="Full Name" required>
        <button type="submit">Add ${capitalize(role)}</button>
      </form>
      <div id="addUserMessage"></div>
      <table id="usersTable">
        <thead><tr><th>ID</th><th>Username</th><th>Email</th><th>Full Name</th><th>Action</th></tr></thead>
        <tbody></tbody>
      </table>
    </div>`;
  document.getElementById("mainContent").innerHTML = content;

  fetchUsers(role);

  document.getElementById("addUserForm").onsubmit = function (e) {
    e.preventDefault();
    addUser(role);
  };
}

// Fetch users of given role (Superadmin APIs)
function fetchUsers(role) {
  fetch(`${API_BASE_URLS.SUPERADMIN}/users?role=${role}`, {
    headers: { Authorization: "Bearer " + jwtToken },
  })
    .then((res) => res.json())
    .then((users) => {
      const tbody = document.querySelector("#usersTable tbody");
      tbody.innerHTML = "";
      users.forEach((user) => {
        let tr = document.createElement("tr");
        tr.innerHTML = `
          <td>${user.id}</td>
          <td>${user.username}</td>
          <td>${user.email}</td>
          <td>${user.fullName}</td>
          <td><button onclick="deleteUser('${role}', ${user.id})">Delete</button></td>`;
        tbody.appendChild(tr);
      });
    })
    .catch(() => {
      renderMessage("Failed to load users.");
    });
}

// Add user
function addUser(role) {
  const data = {
    username: document.getElementById("usernameInput").value,
    password: document.getElementById("passwordInput").value,
    email: document.getElementById("emailInput").value,
    fullName: document.getElementById("fullNameInput").value,
    role: role.toUpperCase(),
  };

  fetch(`${API_BASE_URLS.SUPERADMIN}/users`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + jwtToken,
    },
    body: JSON.stringify(data),
  })
    .then((res) => res.text())
    .then((msg) => {
      document.getElementById("addUserMessage").textContent = msg;
      fetchUsers(role); // refresh user list
    })
    .catch(() => {
      document.getElementById("addUserMessage").textContent = "Failed to add user.";
    });
}

// Delete user
function deleteUser(role, userId) {
  fetch(`${API_BASE_URLS.SUPERADMIN}/users/${capitalize(role)}/${userId}`, {
    method: "DELETE",
    headers: { Authorization: "Bearer " + jwtToken },
  })
    .then(() => fetchUsers(role))
    .catch(() => alert("Failed to delete user."));
}

// 3. Assign Tasks (Superadmin)
function loadAssignTasks() {
  const content = `
    <div class="panel">
      <h2>Assign Task</h2>
      <form id="assignTaskForm">
        <input type="text" id="taskDescription" placeholder="Task Description" required />
        <select id="taskRole">
          <option value="manager">Manager</option>
          <option value="admin">Admin</option>
          <option value="student">Student</option>
        </select>
        <input type="number" id="taskAssignedTo" placeholder="User ID" required />
        <button type="submit">Assign</button>
      </form>
      <div id="assignTaskMessage"></div>
    </div>`;
  document.getElementById("mainContent").innerHTML = content;

  document.getElementById("assignTaskForm").onsubmit = function (e) {
    e.preventDefault();
    assignTask();
  };
}

function assignTask() {
  const role = document.getElementById("taskRole").value;
  const urlMap = {
    manager: "/tasks/assign-to-manager",
    admin: "/tasks/assign-to-admin",
    student: "/tasks/assign-to-student",
  };
  const url = API_BASE_URLS.SUPERADMIN + urlMap[role];
  const data = {
    description: document.getElementById("taskDescription").value,
    assignedToId: Number(document.getElementById("taskAssignedTo").value),
  };

  fetch(url, {
    method: "POST",
    headers: { "Content-Type": "application/json", Authorization: "Bearer " + jwtToken },
    body: JSON.stringify(data),
  })
    .then((res) => res.text())
    .then((msg) => {
      document.getElementById("assignTaskMessage").textContent = msg;
    })
    .catch(() => {
      document.getElementById("assignTaskMessage").textContent = "Failed to assign task.";
    });
}

// 4. Profile Panel (Manager/Admin/Student)
function loadProfile() {
  fetchProfileData();
}

function fetchProfileData() {
  let url;
  switch (currentRole) {
    case "SUPERADMIN":
      url = API_BASE_URLS.SUPERADMIN + "/profile";
      break;
    case "MANAGER":
      url = API_BASE_URLS.MANAGER + "/profile";
      break;
    case "ADMIN":
      url = API_BASE_URLS.ADMIN + "/profile";
      break;
    case "STUDENT":
      url = API_BASE_URLS.STUDENT + "/profile";
      break;
  }

  fetch(url, { headers: { Authorization: "Bearer " + jwtToken } })
    .then((r) => r.json())
    .then((data) => {
      document.getElementById("mainContent").innerHTML = `
        <div class="panel">
          <h2>My Profile</h2>
          <p><b>Name:</b> ${data.fullName}</p>
          <p><b>Email:</b> ${data.email}</p>
          <p><b>Username:</b> ${data.username}</p>
        </div>
      `;
    })
    .catch(() => {
      renderMessage("Failed to load profile.");
    });
}

// 5. Assign Users (Manager)
function loadAssignUsers() {
  const content = `
    <div class="panel">
      <h2>Assign User to Admin</h2>
      <form id="assignUserForm">
        <input type="number" id="assignedToId" placeholder="Admin ID" required/>
        <input type="number" id="userId" placeholder="User ID" required/>
        <button type="submit">Assign</button>
      </form>
      <div id="assignUserMessage"></div>
    </div>`;
  document.getElementById("mainContent").innerHTML = content;

  document.getElementById("assignUserForm").onsubmit = function (e) {
    e.preventDefault();
    assignUserToAdmin();
  };
}

function assignUserToAdmin() {
  const data = {
    assignedToId: Number(document.getElementById("assignedToId").value),
    userId: Number(document.getElementById("userId").value),
  };
  fetch(API_BASE_URLS.MANAGER + "/tasks/assign-to-admin", {
    method: "POST",
    headers: { "Content-Type": "application/json", Authorization: "Bearer " + jwtToken },
    body: JSON.stringify(data),
  })
    .then((r) => r.text())
    .then((msg) => {
      document.getElementById("assignUserMessage").textContent = msg;
    })
    .catch(() => {
      document.getElementById("assignUserMessage").textContent = "Failed to assign user.";
    });
}

// 6. Tracking Panel (Manager & Admin)
function loadTracking() {
  const content = `
    <div class="panel">
      <h2>Tracking</h2>
      <button id="assignedBtn" onclick="loadTrackingData('assigned')" class="active">Assigned</button>
      <button id="closedBtn" onclick="loadTrackingData('closed')">File Closed</button>
      <div id="trackingTableContainer"></div>
    </div>`;
  document.getElementById("mainContent").innerHTML = content;
  loadTrackingData("assigned");
}

function loadTrackingData(type) {
  // Adjust API path and role permissions
  let url = API_BASE_URLS.MANAGER + `/tracking/${type}`;
  fetch(url, { headers: { Authorization: "Bearer " + jwtToken } })
    .then((r) => r.json())
    .then((data) => {
      renderTrackingTable(data);
      document.getElementById("assignedBtn").classList.toggle("active", type === "assigned");
      document.getElementById("closedBtn").classList.toggle("active", type === "closed");
    });
}

function renderTrackingTable(data) {
  if (data.length === 0) {
    document.getElementById("trackingTableContainer").innerHTML = "<p>No records found.</p>";
    return;
  }
  let html = `<table>
    <thead>
      <tr>
        <th>ID</th><th>Name</th><th>Status</th>
      </tr>
    </thead>
    <tbody>`;
  data.forEach((item) => {
    html += `<tr>
      <td>${item.id}</td>
      <td>${item.name}</td>
      <td>${item.status}</td>
    </tr>`;
  });
  html += `</tbody></table>`;
  document.getElementById("trackingTableContainer").innerHTML = html;
}

// 7. Student Management (Admin)
function loadStudentManagement() {
  // Similar to loadUserPanel but limit to student API via admin
  fetch(API_BASE_URLS.ADMIN + "/students", { headers: { Authorization: "Bearer " + jwtToken } })
    .then((r) => r.json())
    .then((students) => {
      let html = `<div class="panel"><h2>My Students</h2><table><thead><tr><th>ID</th><th>Name</th><th>Email</th></tr></thead><tbody>`;
      students.forEach((s) => {
        html += `<tr><td>${s.id}</td><td>${s.fullName}</td><td>${s.email}</td></tr>`;
      });
      html += `</tbody></table></div>`;
      document.getElementById("mainContent").innerHTML = html;
    });
}

// 8. Student Track Status Panel
function loadTrackStatus() {
  document.getElementById("mainContent").innerHTML = `<div class="panel"><h2>Track My Status</h2><p>Connect this UI to your backend student tracking APIs.</p></div>`;
}

// 9. Student Upload Documents
function loadUploadDocuments() {
  const content = `
    <div class="panel">
      <h2>Upload Documents</h2>
      <form id="uploadDocsForm" enctype="multipart/form-data">
        <input type="file" id="fileInput" required />
        <button type="submit">Upload</button>
      </form>
      <div id="uploadResult"></div>
    </div>`;
  document.getElementById("mainContent").innerHTML = content;

  document.getElementById("uploadDocsForm").onsubmit = async function (e) {
    e.preventDefault();
    const fileInput = document.getElementById("fileInput");
    if (!fileInput.files.length) return alert("Select a file");
    const formData = new FormData();
    formData.append("file", fileInput.files[0]);

    try {
      const res = await fetch(API_BASE_URLS.STUDENT + "/documents/upload", {
        method: "POST",
        headers: { Authorization: "Bearer " + jwtToken },
        body: formData,
      });
      if (!res.ok) throw new Error("Upload failed");
      document.getElementById("uploadResult").textContent = "Upload successful";
    } catch {
      document.getElementById("uploadResult").textContent = "Upload failed";
    }
  };
}

// 10. Logout panel
function showLogoutDialog() {
  document.getElementById("mainContent").innerHTML = `
    <div class="panel">
      <h2>Logout</h2>
      <p>Are you sure you want to logout?</p>
      <button onclick="confirmLogout()">Logout</button>
      <button onclick="loadPanel(menus[currentRole][0].id)">Cancel</button>
    </div>`;
}

function confirmLogout() {
  localStorage.removeItem("jwt_token");
  localStorage.removeItem("user_role");
  window.location.href = "/login.html";
}

// Initialize dashboard UI
window.onload = () => {
  renderSidebar();
};
