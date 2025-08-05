const API_BASE = ''; // set to '' or your backend base URL

function showMessage(msg, error = false) {
  const messageDiv = document.getElementById('message');
  if (!messageDiv) return;
  messageDiv.textContent = msg;
  messageDiv.style.color = error ? 'red' : 'green';
}

// Registration Logic
if (document.getElementById('registerForm')) {
  const roleSelect = document.getElementById('role');
  const mgrLabel = document.getElementById('managerIdLabel');
  roleSelect.addEventListener('change', () => {
    mgrLabel.style.display = roleSelect.value === 'admin' ? 'block' : 'none';
    document.getElementById('managerId').required = roleSelect.value === 'admin';
  });

  document.getElementById('registerForm').addEventListener('submit', async e => {
    e.preventDefault();
    const data = {
      username: document.getElementById('username').value.trim(),
      password: document.getElementById('password').value,
      email: document.getElementById('email').value.trim(),
      fullName: document.getElementById('fullName').value.trim(),
      role: roleSelect.value
    };
    if (data.role === 'admin') {
      data.managerId = Number(document.getElementById('managerId').value);
    }

    const endpointMap = {
      student: '/register/student',
      admin: '/register/admin',
      manager: '/register/manager',
      superadmin: '/register/superadmin'
    };
    try {
      const res = await fetch(API_BASE + endpointMap[data.role], {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
      });
      if (!res.ok) {
        let text = await res.text();
        throw new Error(text || 'Registration failed');
      }
      showMessage('Registration successful! Please login.');
    } catch (err) {
      showMessage(err.message, true);
    }
  });
}

// Login Logic
if(document.getElementById('loginForm')) {
  document.getElementById('loginForm').addEventListener('submit', async e => {
    e.preventDefault();
    const data = {
      username: document.getElementById('username').value.trim(),
      password: document.getElementById('password').value
    };
    try {
      const res = await fetch(API_BASE + '/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
      });
      if(!res.ok) {
        let text = await res.text();
        throw new Error(text || 'Login failed');
      }
      const response = await res.json();
      localStorage.setItem('jwt_token', response.token);
      localStorage.setItem('user_role', response.role.toUpperCase());
      window.location.href = '/dashboard.html';
    } catch (err) {
      showMessage(err.message, true);
    }
  });
}
