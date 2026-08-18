const API_BASE_URL = "http://localhost:8081/api";

function getAuthHeaders() {
  const token = localStorage.getItem("token");

  return {
    "Content-Type": "application/json",
    Authorization: `Bearer ${token}`,
  };
}

export async function getWorkOrders() {
  const response = await fetch(`${API_BASE_URL}/workorders`, {
    headers: getAuthHeaders(),
  });

  if (!response.ok) {
    throw new Error(`Failed to fetch work orders: ${response.status}`);
  }

  return response.json();
}

export async function getTechnicians() {
  const response = await fetch(`${API_BASE_URL}/auth/technicians`, {
    headers: getAuthHeaders(),
  });

  if (!response.ok) {
    throw new Error(`Failed to fetch technicians: ${response.status}`);
  }

  return response.json();
}

export async function getCustomers() {
  const response = await fetch(`${API_BASE_URL}/customers`, {
    headers: getAuthHeaders(),
  });

  if (!response.ok) {
    throw new Error(`Failed to fetch customers: ${response.status}`);
  }

  return response.json();
}

export async function getSLAs() {
  const response = await fetch(`${API_BASE_URL}/slas`, {
    headers: getAuthHeaders(),
  });

  if (!response.ok) {
    throw new Error(`Failed to fetch SLAs: ${response.status}`);
  }

  return response.json();
}