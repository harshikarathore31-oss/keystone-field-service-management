import { useEffect, useState } from "react";
import "./App.css";
import Login from "./Login";

import {
  getWorkOrders,
  getTechnicians,
  getCustomers,
  getSLAs,
  createWorkOrder,
} from "./api_fixed.js";

const API_BASE_URL = "http://localhost:8081/api";

function App() {
  // =====================================================
  // LOGIN STATE
  // =====================================================

  const [loggedIn, setLoggedIn] = useState(
    Boolean(localStorage.getItem("token"))
  );

  const [user, setUser] = useState(null);

  // =====================================================
  // PAGE STATE
  // =====================================================

  const [currentPage, setCurrentPage] = useState("dashboard");

  // =====================================================
  // CREATE WORK ORDER STATE
  // =====================================================

  const [showCreateForm, setShowCreateForm] = useState(false);

  const [newWorkOrder, setNewWorkOrder] = useState({
    title: "",
    description: "",
    priority: "HIGH",
    status: "NEW",
  });

  // =====================================================
  // EDIT WORK ORDER STATE
  // =====================================================

  const [editingWorkOrder, setEditingWorkOrder] = useState(null);

  // =====================================================
  // DATA STATE
  // =====================================================

  const [workOrders, setWorkOrders] = useState([]);
  const [technicians, setTechnicians] = useState([]);
  const [customers, setCustomers] = useState([]);
  const [slas, setSlas] = useState([]);

  // =====================================================
  // LOADING / ERROR
  // =====================================================

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  // =====================================================
  // AUTH HEADERS
  // =====================================================

  const getAuthHeaders = () => {
    const token = localStorage.getItem("token");

    return {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    };
  };

  // =====================================================
  // LOGIN
  // =====================================================

  const handleLogin = (loginData) => {
    setUser(loginData);
    setLoggedIn(true);
    setCurrentPage("dashboard");
  };

  // =====================================================
  // LOGOUT
  // =====================================================

  const handleLogout = () => {
    localStorage.removeItem("token");

    setLoggedIn(false);
    setUser(null);

    setWorkOrders([]);
    setTechnicians([]);
    setCustomers([]);
    setSlas([]);

    setCurrentPage("dashboard");

    setShowCreateForm(false);
    setEditingWorkOrder(null);

    setError("");
  };

  // =====================================================
  // LOAD DASHBOARD DATA
  // =====================================================

  useEffect(() => {
    if (!loggedIn) {
      return;
    }

    const loadDashboardData = async () => {
      setLoading(true);
      setError("");

      try {
        const [
          workOrdersData,
          techniciansData,
          customersData,
          slasData,
        ] = await Promise.all([
          getWorkOrders(),
          getTechnicians(),
          getCustomers(),
          getSLAs(),
        ]);

        setWorkOrders(workOrdersData);
        setTechnicians(techniciansData);
        setCustomers(customersData);
        setSlas(slasData);
      } catch (err) {
        console.error("Dashboard loading error:", err);

        if (err.message && err.message.includes("401")) {
          handleLogout();
          return;
        }

        setError("Unable to load dashboard data.");
      } finally {
        setLoading(false);
      }
    };

    loadDashboardData();
  }, [loggedIn]);

  // =====================================================
  // CREATE WORK ORDER
  // =====================================================

  const handleCreateWorkOrder = async (e) => {
    e.preventDefault();

    setError("");

    try {
      const createdOrder = await createWorkOrder(newWorkOrder);

      setWorkOrders((prev) => [
        ...prev,
        createdOrder,
      ]);

      setNewWorkOrder({
        title: "",
        description: "",
        priority: "HIGH",
        status: "NEW",
      });

      setShowCreateForm(false);

      setCurrentPage("workorders");
    } catch (err) {
      console.error(
        "Create work order error:",
        err
      );

      setError(
        err.message ||
          "Failed to create work order."
      );
    }
  };

  // =====================================================
  // OPEN CREATE WORK ORDER
  // =====================================================

  const openCreateWorkOrder = () => {
    setError("");

    setEditingWorkOrder(null);

    setCurrentPage("workorders");

    setShowCreateForm(true);
  };

  // =====================================================
  // CANCEL CREATE WORK ORDER
  // =====================================================

  const cancelCreateWorkOrder = () => {
    setShowCreateForm(false);

    setNewWorkOrder({
      title: "",
      description: "",
      priority: "HIGH",
      status: "NEW",
    });

    setError("");
  };

  // =====================================================
  // OPEN EDIT WORK ORDER
  // =====================================================

  const openEditWorkOrder = (order) => {
    console.log("EDIT CLICKED:", order);

    setError("");

    setShowCreateForm(false);

    setEditingWorkOrder({
      id: order.id,
      title: order.title || "",
      description: order.description || "",
      priority: order.priority || "HIGH",
      status: order.status || "NEW",
    });
  };

  // =====================================================
  // CANCEL EDIT
  // =====================================================

  const cancelEditWorkOrder = () => {
    setEditingWorkOrder(null);

    setError("");
  };

  // =====================================================
  // UPDATE WORK ORDER
  // =====================================================

  const handleUpdateWorkOrder = async (e) => {
    e.preventDefault();

    if (!editingWorkOrder) {
      return;
    }

    setError("");

    try {
      const response = await fetch(
        `${API_BASE_URL}/workorders/${editingWorkOrder.id}`,
        {
          method: "PUT",
          headers: getAuthHeaders(),
          body: JSON.stringify({
            title: editingWorkOrder.title,
            description: editingWorkOrder.description,
            priority: editingWorkOrder.priority,
            status: editingWorkOrder.status,
          }),
        }
      );

      if (!response.ok) {
        const message = await response.text();

        throw new Error(
          `Failed to update work order: ${response.status} ${message}`
        );
      }

      const updatedOrder = await response.json();

      setWorkOrders((prev) =>
        prev.map((order) =>
          order.id === updatedOrder.id
            ? updatedOrder
            : order
        )
      );

      setEditingWorkOrder(null);

      setError("");
    } catch (err) {
      console.error(
        "Update work order error:",
        err
      );

      setError(
        err.message ||
          "Failed to update work order."
      );
    }
  };

  // =====================================================
  // DELETE WORK ORDER
  // =====================================================

  const handleDeleteWorkOrder = async (id) => {
    const confirmed = window.confirm(
      "Are you sure you want to delete this work order?"
    );

    if (!confirmed) {
      return;
    }

    setError("");

    try {
      const response = await fetch(
        `${API_BASE_URL}/workorders/${id}`,
        {
          method: "DELETE",
          headers: getAuthHeaders(),
        }
      );

      if (!response.ok) {
        const message = await response.text();

        throw new Error(
          `Failed to delete work order: ${response.status} ${message}`
        );
      }

      setWorkOrders((prev) =>
        prev.filter(
          (order) => order.id !== id
        )
      );

      if (
        editingWorkOrder &&
        editingWorkOrder.id === id
      ) {
        setEditingWorkOrder(null);
      }

      setError("");
    } catch (err) {
      console.error(
        "Delete work order error:",
        err
      );

      setError(
        err.message ||
          "Failed to delete work order."
      );
    }
  };

  // =====================================================
  // LOGIN PAGE
  // =====================================================

  if (!loggedIn) {
    return <Login onLogin={handleLogin} />;
  }

  // =====================================================
  // MAIN APPLICATION
  // =====================================================

  return (
    <div className="app">

      {/* ================================================= */}
      {/* NAVBAR */}
      {/* ================================================= */}

      <header className="navbar">

        <div className="logo">

          <span className="logo-icon">
            K
          </span>

          <span>
            Keystone
          </span>

        </div>

        <div className="user-section">

          <span>
            {user?.firstName
              ? `${user.firstName} ${
                  user.lastName || ""
                }`
              : "Manager"}
          </span>

          <span className="role">
            {user?.role || "MANAGER"}
          </span>

          <button onClick={handleLogout}>
            Logout
          </button>

        </div>

      </header>

      {/* ================================================= */}
      {/* LAYOUT */}
      {/* ================================================= */}

      <div className="layout">

        {/* ================================================= */}
        {/* SIDEBAR */}
        {/* ================================================= */}

        <aside className="sidebar">

          <nav>

            <a
              className={
                currentPage === "dashboard"
                  ? "active"
                  : ""
              }
              onClick={() => {
                setCurrentPage("dashboard");
                setShowCreateForm(false);
                setEditingWorkOrder(null);
              }}
            >
              Dashboard
            </a>

            <a
              className={
                currentPage === "workorders"
                  ? "active"
                  : ""
              }
              onClick={() => {
                setCurrentPage("workorders");
              }}
            >
              Work Orders
            </a>

            <a
              className={
                currentPage === "customers"
                  ? "active"
                  : ""
              }
              onClick={() => {
                setCurrentPage("customers");
                setShowCreateForm(false);
                setEditingWorkOrder(null);
              }}
            >
              Customers
            </a>

            <a
              className={
                currentPage === "sites"
                  ? "active"
                  : ""
              }
              onClick={() => {
                setCurrentPage("sites");
                setShowCreateForm(false);
                setEditingWorkOrder(null);
              }}
            >
              Sites
            </a>

            <a
              className={
                currentPage === "technicians"
                  ? "active"
                  : ""
              }
              onClick={() => {
                setCurrentPage("technicians");
                setShowCreateForm(false);
                setEditingWorkOrder(null);
              }}
            >
              Technicians
            </a>

            <a
              className={
                currentPage === "parts"
                  ? "active"
                  : ""
              }
              onClick={() => {
                setCurrentPage("parts");
                setShowCreateForm(false);
                setEditingWorkOrder(null);
              }}
            >
              Parts
            </a>

            <a
              className={
                currentPage === "timelogs"
                  ? "active"
                  : ""
              }
              onClick={() => {
                setCurrentPage("timelogs");
                setShowCreateForm(false);
                setEditingWorkOrder(null);
              }}
            >
              Time Logs
            </a>

            <a
              className={
                currentPage === "sla"
                  ? "active"
                  : ""
              }
              onClick={() => {
                setCurrentPage("sla");
                setShowCreateForm(false);
                setEditingWorkOrder(null);
              }}
            >
              SLA
            </a>

          </nav>

        </aside>

        {/* ================================================= */}
        {/* MAIN CONTENT */}
        {/* ================================================= */}

        <main className="content">

          {/* ================================================= */}
          {/* PAGE HEADER */}
          {/* ================================================= */}

          <div className="page-header">

            <div>

              <h1>
                {currentPage === "dashboard"
                  ? "Dashboard"
                  : currentPage === "workorders"
                  ? "Work Orders"
                  : currentPage === "customers"
                  ? "Customers"
                  : currentPage === "sites"
                  ? "Sites"
                  : currentPage === "technicians"
                  ? "Technicians"
                  : currentPage === "parts"
                  ? "Parts"
                  : currentPage === "timelogs"
                  ? "Time Logs"
                  : currentPage === "sla"
                  ? "SLA"
                  : "Dashboard"}
              </h1>

              <p>
                Keystone Field Service Management
              </p>

            </div>

          </div>

          {/* ================================================= */}
          {/* LOADING */}
          {/* ================================================= */}

          {loading && (
            <div className="loading">
              Loading dashboard data...
            </div>
          )}

          {/* ================================================= */}
          {/* ERROR */}
          {/* ================================================= */}

          {error && (
            <div className="error">
              {error}
            </div>
          )}

          {/* ================================================= */}
          {/* DASHBOARD */}
          {/* ================================================= */}

          {currentPage === "dashboard" && (
            <>

              <div className="cards">

                <div className="card">

                  <h3>
                    Work Orders
                  </h3>

                  <strong>
                    {workOrders.length}
                  </strong>

                  <p>
                    Total work orders
                  </p>

                </div>

                <div className="card">

                  <h3>
                    Open Work Orders
                  </h3>

                  <strong>
                    {
                      workOrders.filter(
                        (order) =>
                          order.status !==
                            "COMPLETED" &&
                          order.status !==
                            "CANCELLED" &&
                          order.status !==
                            "CLOSED"
                      ).length
                    }
                  </strong>

                  <p>
                    Currently open
                  </p>

                </div>

                <div className="card">

                  <h3>
                    Technicians
                  </h3>

                  <strong>
                    {technicians.length}
                  </strong>

                  <p>
                    Registered technicians
                  </p>

                </div>

                <div className="card">

                  <h3>
                    Active SLAs
                  </h3>

                  <strong>
                    {
                      slas.filter(
                        (sla) => sla.active
                      ).length
                    }
                  </strong>

                  <p>
                    Active SLA policies
                  </p>

                </div>

              </div>

              <section className="welcome">

                <h2>
                  Welcome to Keystone
                </h2>

                <p>
                  Manage work orders,
                  technicians, customers,
                  parts, time logs and SLA
                  policies from one place.
                </p>

              </section>

              <section className="quick-actions">

                <h2>
                  Quick Actions
                </h2>

                <div className="action-grid">

                  <button
                    onClick={
                      openCreateWorkOrder
                    }
                  >
                    Create Work Order
                  </button>

                  <button
                    onClick={() => {
                      setCurrentPage(
                        "workorders"
                      );
                      setShowCreateForm(false);
                      setEditingWorkOrder(
                        null
                      );
                    }}
                  >
                    View Work Orders
                  </button>

                  <button
                    onClick={() => {
                      setCurrentPage(
                        "technicians"
                      );
                      setShowCreateForm(false);
                      setEditingWorkOrder(
                        null
                      );
                    }}
                  >
                    Manage Technicians
                  </button>

                  <button
                    onClick={() => {
                      setCurrentPage("sla");
                      setShowCreateForm(false);
                      setEditingWorkOrder(
                        null
                      );
                    }}
                  >
                    Manage SLAs
                  </button>

                </div>

              </section>

              <section className="summary">

                <h2>
                  System Summary
                </h2>

                <p>
                  Customers:{" "}
                  <strong>
                    {customers.length}
                  </strong>
                </p>

                <p>
                  Technicians:{" "}
                  <strong>
                    {technicians.length}
                  </strong>
                </p>

                <p>
                  Work Orders:{" "}
                  <strong>
                    {workOrders.length}
                  </strong>
                </p>

                <p>
                  Active SLAs:{" "}
                  <strong>
                    {
                      slas.filter(
                        (sla) => sla.active
                      ).length
                    }
                  </strong>
                </p>

              </section>

            </>
          )}

          {/* ================================================= */}
          {/* WORK ORDERS */}
          {/* ================================================= */}

          {currentPage === "workorders" && (
            <section className="page-section">

              <div className="work-orders-header">

                <h2>
                  Work Orders
                </h2>

                <button
                  onClick={
                    openCreateWorkOrder
                  }
                >
                  + Create Work Order
                </button>

              </div>

              {/* ================================================= */}
              {/* CREATE FORM */}
              {/* ================================================= */}

              {showCreateForm && (
                <form
                  onSubmit={
                    handleCreateWorkOrder
                  }
                  className="create-work-order-form"
                >

                  <h3>
                    Create Work Order
                  </h3>

                  <label>
                    Title

                    <input
                      type="text"
                      value={
                        newWorkOrder.title
                      }
                      onChange={(e) =>
                        setNewWorkOrder({
                          ...newWorkOrder,
                          title:
                            e.target.value,
                        })
                      }
                      placeholder="Enter work order title"
                      required
                    />

                  </label>

                  <label>
                    Description

                    <textarea
                      value={
                        newWorkOrder.description
                      }
                      onChange={(e) =>
                        setNewWorkOrder({
                          ...newWorkOrder,
                          description:
                            e.target.value,
                        })
                      }
                      placeholder="Enter work order description"
                      rows="4"
                      required
                    />

                  </label>

                  <label>
                    Priority

                    <select
                      value={
                        newWorkOrder.priority
                      }
                      onChange={(e) =>
                        setNewWorkOrder({
                          ...newWorkOrder,
                          priority:
                            e.target.value,
                        })
                      }
                    >

                      <option value="LOW">
                        LOW
                      </option>

                      <option value="MEDIUM">
                        MEDIUM
                      </option>

                      <option value="HIGH">
                        HIGH
                      </option>

                    </select>

                  </label>

                  <label>
                    Status

                    <select
                      value={
                        newWorkOrder.status
                      }
                      onChange={(e) =>
                        setNewWorkOrder({
                          ...newWorkOrder,
                          status:
                            e.target.value,
                        })
                      }
                    >

                      <option value="NEW">
                        NEW
                      </option>

                      <option value="ASSIGNED">
                        ASSIGNED
                      </option>

                      <option value="IN_PROGRESS">
                        IN_PROGRESS
                      </option>

                      <option value="COMPLETED">
                        COMPLETED
                      </option>

                      <option value="CLOSED">
                        CLOSED
                      </option>

                    </select>

                  </label>

                  <div className="form-buttons">

                    <button type="submit">
                      Create
                    </button>

                    <button
                      type="button"
                      onClick={
                        cancelCreateWorkOrder
                      }
                    >
                      Cancel
                    </button>

                  </div>

                </form>
              )}

              {/* ================================================= */}
              {/* EDIT FORM */}
              {/* ================================================= */}

              {editingWorkOrder && (
                <form
                  onSubmit={
                    handleUpdateWorkOrder
                  }
                  className="create-work-order-form"
                >

                  <h3>
                    Edit Work Order #
                    {editingWorkOrder.id}
                  </h3>

                  <label>
                    Title

                    <input
                      type="text"
                      value={
                        editingWorkOrder.title
                      }
                      onChange={(e) =>
                        setEditingWorkOrder({
                          ...editingWorkOrder,
                          title:
                            e.target.value,
                        })
                      }
                      required
                    />

                  </label>

                  <label>
                    Description

                    <textarea
                      value={
                        editingWorkOrder.description
                      }
                      onChange={(e) =>
                        setEditingWorkOrder({
                          ...editingWorkOrder,
                          description:
                            e.target.value,
                        })
                      }
                      rows="4"
                      required
                    />

                  </label>

                  <label>
                    Priority

                    <select
                      value={
                        editingWorkOrder.priority
                      }
                      onChange={(e) =>
                        setEditingWorkOrder({
                          ...editingWorkOrder,
                          priority:
                            e.target.value,
                        })
                      }
                    >

                      <option value="LOW">
                        LOW
                      </option>

                      <option value="MEDIUM">
                        MEDIUM
                      </option>

                      <option value="HIGH">
                        HIGH
                      </option>

                    </select>

                  </label>

                  <label>
                    Status

                    <select
                      value={
                        editingWorkOrder.status
                      }
                      onChange={(e) =>
                        setEditingWorkOrder({
                          ...editingWorkOrder,
                          status:
                            e.target.value,
                        })
                      }
                    >

                      <option value="NEW">
                        NEW
                      </option>

                      <option value="ASSIGNED">
                        ASSIGNED
                      </option>

                      <option value="IN_PROGRESS">
                        IN_PROGRESS
                      </option>

                      <option value="COMPLETED">
                        COMPLETED
                      </option>

                      <option value="CLOSED">
                        CLOSED
                      </option>

                    </select>

                  </label>

                  <div className="form-buttons">

                    <button type="submit">
                      Update
                    </button>

                    <button
                      type="button"
                      onClick={
                        cancelEditWorkOrder
                      }
                    >
                      Cancel
                    </button>

                  </div>

                </form>
              )}

              {/* ================================================= */}
              {/* WORK ORDER LIST */}
              {/* ================================================= */}

              {workOrders.length === 0 ? (
                <p>
                  No work orders found.
                </p>
              ) : (
                <div className="work-order-list">

                  {workOrders.map(
                    (order) => (

                      <div
                        className="work-order-card"
                        key={order.id}
                      >

                        <h3>
                          Work Order #
                          {order.id}
                        </h3>

                        <p>
                          <strong>
                            Title:
                          </strong>{" "}
                          {order.title ||
                            "N/A"}
                        </p>

                        <p>
                          <strong>
                            Status:
                          </strong>{" "}
                          {order.status ||
                            "N/A"}
                        </p>

                        <p>
                          <strong>
                            Priority:
                          </strong>{" "}
                          {order.priority ||
                            "N/A"}
                        </p>

                        <p>
                          <strong>
                            Description:
                          </strong>{" "}
                          {order.description ||
                            "N/A"}
                        </p>

                        <div className="form-buttons">

                          <button
                            type="button"
                            onClick={() =>
                              openEditWorkOrder(
                                order
                              )
                            }
                          >
                            Edit
                          </button>

                          <button
                            type="button"
                            onClick={() =>
                              handleDeleteWorkOrder(
                                order.id
                              )
                            }
                          >
                            Delete
                          </button>

                        </div>

                      </div>

                    )
                  )}

                </div>
              )}

            </section>
          )}

          {/* ================================================= */}
          {/* CUSTOMERS */}
          {/* ================================================= */}

          {currentPage === "customers" && (
            <section className="page-section">

              <h2>
                Customers
              </h2>

              <p>
                Customers:{" "}
                <strong>
                  {customers.length}
                </strong>
              </p>

              <p>
                Customer management module is
                ready for further implementation.
              </p>

            </section>
          )}

          {/* ================================================= */}
          {/* TECHNICIANS */}
          {/* ================================================= */}

          {currentPage === "technicians" && (
            <section className="page-section">

              <h2>
                Technicians
              </h2>

              <p>
                Registered Technicians:{" "}
                <strong>
                  {technicians.length}
                </strong>
              </p>

              {technicians.length > 0 && (
                <div className="work-order-list">

                  {technicians.map(
                    (technician) => (

                      <div
                        className="work-order-card"
                        key={technician.id}
                      >

                        <h3>
                          {technician.firstName ||
                            ""}{" "}
                          {technician.lastName ||
                            ""}
                        </h3>

                        <p>
                          Email:{" "}
                          {technician.email ||
                            "N/A"}
                        </p>

                      </div>

                    )
                  )}

                </div>
              )}

            </section>
          )}

          {/* ================================================= */}
          {/* SLA */}
          {/* ================================================= */}

          {currentPage === "sla" && (
            <section className="page-section">

              <h2>
                SLA Policies
              </h2>

              <p>
                Active SLA Policies:{" "}
                <strong>
                  {
                    slas.filter(
                      (sla) => sla.active
                    ).length
                  }
                </strong>
              </p>

              {slas.length > 0 && (
                <div className="work-order-list">

                  {slas.map(
                    (sla) => (

                      <div
                        className="work-order-card"
                        key={sla.id}
                      >

                        <h3>
                          {sla.name ||
                            `SLA #${sla.id}`}
                        </h3>

                        <p>
                          Status:{" "}
                          {sla.active
                            ? "ACTIVE"
                            : "INACTIVE"}
                        </p>

                      </div>

                    )
                  )}

                </div>
              )}

            </section>
          )}

          {/* ================================================= */}
          {/* SITES */}
          {/* ================================================= */}

          {currentPage === "sites" && (
            <section className="page-section">

              <h2>
                Sites
              </h2>

              <p>
                Site management module.
              </p>

            </section>
          )}

          {/* ================================================= */}
          {/* PARTS */}
          {/* ================================================= */}

          {currentPage === "parts" && (
            <section className="page-section">

              <h2>
                Parts
              </h2>

              <p>
                Parts management module.
              </p>

            </section>
          )}

          {/* ================================================= */}
          {/* TIME LOGS */}
          {/* ================================================= */}

          {currentPage === "timelogs" && (
            <section className="page-section">

              <h2>
                Time Logs
              </h2>

              <p>
                Time log management module.
              </p>

            </section>
          )}

        </main>

      </div>

    </div>
  );
}

export default App;