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

export async function createWorkOrder(workOrder) {
  const response = await fetch(`${API_BASE_URL}/workorders`, {
    method: "POST",
    headers: getAuthHeaders(),
    body: JSON.stringify(workOrder),
  });

  if (!response.ok) {
    const message = await response.text();

    throw new Error(
      `Failed to create work order: ${response.status} ${message}`
    );
  }

  return response.json();
}
export async function updateWorkOrder(id, workOrder) {
  const response = await fetch(`${API_BASE_URL}/workorders/${id}`, {
    method: "PUT",
    headers: getAuthHeaders(),
    body: JSON.stringify(workOrder),
  });

  if (!response.ok) {
    const message = await response.text();

    throw new Error(
      `Failed to update work order: ${response.status} ${message}`
    );
  }

  return response.json();
}

export async function deleteWorkOrder(id) {
  const response = await fetch(`${API_BASE_URL}/workorders/${id}`, {
    method: "DELETE",
    headers: getAuthHeaders(),
  });

  if (!response.ok) {
    const message = await response.text();

    throw new Error(
      `Failed to delete work order: ${response.status} ${message}`
    );
  }

  return true;
}

export async function assignTechnician(workOrderId, technicianId) {
  const response = await fetch(
    `${API_BASE_URL}/workorders/${workOrderId}/assign`,
    {
      method: "PATCH",
      headers: getAuthHeaders(),
      body: JSON.stringify({
        technicianId: Number(technicianId),
      }),
    }
  );

  if (!response.ok) {
    const message = await response.text();

    throw new Error(
      `Failed to assign technician: ${response.status} ${message}`
    );
  }

  return response.json();
}
