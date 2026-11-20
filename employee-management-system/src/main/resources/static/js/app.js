
const API_URL = "/api/v1/employees";

let pendingDeleteEmployee = null;

// Initialize application
document.addEventListener("DOMContentLoaded", () => {
    loadEmployees();

    document.getElementById("employeeForm")
        .addEventListener("submit", saveEmployee);

    document.getElementById("confirmDeleteBtn")
        .addEventListener("click", confirmDeleteEmployee);

    document.getElementById("deleteModal")
        .addEventListener("hidden.bs.modal", () => {
            if (pendingDeleteEmployee?.feedback) {
                const feedback = pendingDeleteEmployee.feedback;
                pendingDeleteEmployee = null;

                showFeedback(
                    feedback.title,
                    feedback.message,
                    feedback.details,
                    feedback.type
                );
            }
        });
});


// Escape HTML to prevent unsafe employee data from becoming HTML
function escapeHtml(value) {
    return String(value ?? "").replace(/[&<>"']/g, character => ({
        "&": "&amp;",
        "<": "&lt;",
        ">": "&gt;",
        '"': "&quot;",
        "'": "&#039;"
    })[character]);
}


// Display custom success or error modal
function showFeedback(title, message, details = "", type = "success") {
    document.getElementById("feedbackTitle").textContent = title;
    document.getElementById("feedbackMessage").textContent = message;

    const icon = document.getElementById("feedbackIcon");

    if (type === "success") {
        icon.innerHTML = '<span class="text-success fs-1">✓</span>';
        document.getElementById("feedbackTitle").className =
            "modal-title text-success";
    } else {
        icon.innerHTML = '<span class="text-danger fs-1">✕</span>';
        document.getElementById("feedbackTitle").className =
            "modal-title text-danger";
    }

    const detailsElement = document.getElementById("feedbackDetails");
    detailsElement.innerHTML = details
        ? `<p class="mb-0">${details}</p>`
        : "";

    detailsElement.style.display = details ? "block" : "none";

    bootstrap.Modal.getOrCreateInstance(
        document.getElementById("feedbackModal")
    ).show();
}


// Generate employee details for feedback modal
function getEmployeeDetails(employee) {
    return `
        <strong>Employee ID:</strong> ${escapeHtml(employee.empno)}<br>
        <strong>Name:</strong> ${escapeHtml(employee.empName)}<br>
        <strong>Date of Joining:</strong> ${escapeHtml(employee.doj)}<br>
        <strong>Gender:</strong> ${escapeHtml(employee.gender)}<br>
        <strong>Basic Salary:</strong> ₹${Number(employee.bsalary).toFixed(2)}
    `;
}


// Read errors returned by backend
async function getErrorMessage(response) {
    try {
        const error = await response.json();

        if (error.error) return error.error;
        if (error.message) return error.message;

        return Object.values(error).join(", ") || "Something went wrong.";
    } catch {
        return "Something went wrong. Please try again.";
    }
}


// Load all employees
async function loadEmployees() {
    try {
        const response = await fetch(API_URL);

        if (!response.ok) {
            throw new Error(await getErrorMessage(response));
        }

        const employees = await response.json();
        displayEmployees(employees);

    } catch (error) {
        showFeedback("Error", error.message, "", "error");
    }
}


// Display employees in table
function displayEmployees(employees) {
    const tableBody = document.getElementById("employeeTableBody");
    tableBody.innerHTML = "";

    if (!employees || employees.length === 0) {
        tableBody.innerHTML = `
            <tr>
                <td colspan="6" class="text-center text-muted">
                    No employee records found.
                </td>
            </tr>`;
        return;
    }

    employees.forEach(employee => {
        const row = document.createElement("tr");

        row.innerHTML = `
            <td>${escapeHtml(employee.empno)}</td>
            <td>${escapeHtml(employee.empName)}</td>
            <td>${escapeHtml(employee.doj)}</td>
            <td>${escapeHtml(employee.gender)}</td>
            <td>₹ ${Number(employee.bsalary).toFixed(2)}</td>
            <td>
                <button class="btn btn-sm btn-warning me-1"
                    onclick="editEmployee(${Number(employee.empno)})">
                    Edit
                </button>

                <button class="btn btn-sm btn-danger"
                    onclick="deleteEmployee(${Number(employee.empno)})">
                    Delete
                </button>
            </td>`;

        tableBody.appendChild(row);
    });
}


// Open Add Employee modal
function openAddModal() {
    document.getElementById("employeeForm").reset();
    document.getElementById("empno").value = "";
    document.getElementById("modalTitle").textContent = "Add Employee";

    bootstrap.Modal.getOrCreateInstance(
        document.getElementById("employeeModal")
    ).show();
}


// Add or Update employee
async function saveEmployee(event) {
    event.preventDefault();

    const empNo = document.getElementById("empno").value;

    const employee = {
        empName: document.getElementById("empName").value.trim(),
        doj: document.getElementById("doj").value,
        gender: document.getElementById("gender").value,
        bsalary: Number(document.getElementById("bsalary").value)
    };

    const url = empNo ? `${API_URL}/${empNo}` : API_URL;
    const method = empNo ? "PUT" : "POST";

    try {
        const response = await fetch(url, {
            method: method,
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(employee)
        });

        if (!response.ok) {
            throw new Error(await getErrorMessage(response));
        }

        const savedEmployee = await response.json();

        bootstrap.Modal.getInstance(
            document.getElementById("employeeModal")
        ).hide();

        document.getElementById("employeeForm").reset();
        document.getElementById("empno").value = "";

        loadEmployees();

        showFeedback(
            "Success",
            empNo
                ? "Employee updated successfully!"
                : "Employee added successfully!",
            getEmployeeDetails(savedEmployee)
        );

    } catch (error) {
        showFeedback("Error", error.message, "", "error");
    }
}


// Edit employee
async function editEmployee(empNo) {
    try {
        const response = await fetch(`${API_URL}/${empNo}`);

        if (!response.ok) {
            throw new Error(await getErrorMessage(response));
        }

        const employee = await response.json();

        document.getElementById("empno").value = employee.empno;
        document.getElementById("empName").value = employee.empName;
        document.getElementById("doj").value = employee.doj;
        document.getElementById("gender").value = employee.gender;
        document.getElementById("bsalary").value = employee.bsalary;

        document.getElementById("modalTitle").textContent =
            "Edit Employee";

        bootstrap.Modal.getOrCreateInstance(
            document.getElementById("employeeModal")
        ).show();

    } catch (error) {
        showFeedback("Error", error.message, "", "error");
    }
}


// Open Delete Confirmation Modal
async function deleteEmployee(empNo) {
    try {
        const response = await fetch(`${API_URL}/${empNo}`);

        if (!response.ok) {
            throw new Error(await getErrorMessage(response));
        }

        const employee = await response.json();

        pendingDeleteEmployee = employee;

        document.getElementById("deleteEmpNo").textContent = employee.empno;
        document.getElementById("deleteEmpName").textContent = employee.empName;
        document.getElementById("deleteDoj").textContent = employee.doj;
        document.getElementById("deleteGender").textContent = employee.gender;
        document.getElementById("deleteSalary").textContent =
            Number(employee.bsalary).toFixed(2);

        bootstrap.Modal.getOrCreateInstance(
            document.getElementById("deleteModal")
        ).show();

    } catch (error) {
        showFeedback("Error", error.message, "", "error");
    }
}


// Confirm and execute deletion
async function confirmDeleteEmployee() {
    const button = document.getElementById("confirmDeleteBtn");

    if (!pendingDeleteEmployee || button.disabled) return;

    const employee = pendingDeleteEmployee;
    button.disabled = true;

    try {
        const response = await fetch(
            `${API_URL}/${employee.empno}`,
            { method: "DELETE" }
        );

        if (!response.ok) {
            throw new Error(await getErrorMessage(response));
        }

        loadEmployees();

        pendingDeleteEmployee = {
            feedback: {
                title: "Success",
                message: "Employee deleted successfully!",
                details: getEmployeeDetails(employee),
                type: "success"
            }
        };

        bootstrap.Modal.getInstance(
            document.getElementById("deleteModal")
        ).hide();

    } catch (error) {
        pendingDeleteEmployee = {
            feedback: {
                title: "Error",
                message: error.message,
                details: "",
                type: "error"
            }
        };

        bootstrap.Modal.getInstance(
            document.getElementById("deleteModal")
        ).hide();

    } finally {
        button.disabled = false;
    }
}


// Search employees by name prefix

async function searchByPrefix() {
    const letter = document.getElementById("prefixInput").value.trim();

    if (!letter) {
        showFeedback("Input Required", "Please enter a name prefix.", "", "error");
        return;
    }

    searchEmployees(`/reports?letter=${encodeURIComponent(letter)}`);
}


async function searchByExperience() {
    const years = document.getElementById("experienceInput").value;

    if (years === "" || Number(years) < 0) {
        showFeedback("Invalid Input", "Please enter valid years of experience.", "", "error");
        return;
    }

    searchEmployees(`/reports?years=${encodeURIComponent(years)}`);
}


async function searchBySalary() {
    const salary = document.getElementById("salaryInput").value;

    if (salary === "" || Number(salary) <= 0) {
        showFeedback("Invalid Input", "Please enter a valid minimum salary.", "", "error");
        return;
    }

    searchEmployees(`/reports?minSalary=${encodeURIComponent(salary)}`);
}


// Execute report search
async function searchEmployees(endpoint) {
    try {
        const response = await fetch(`${API_URL}${endpoint}`);

        if (!response.ok) {
            throw new Error(await getErrorMessage(response));
        }

        const employees = await response.json();
        displayEmployees(employees);

    } catch (error) {
        showFeedback("Report Error", error.message, "", "error");
    }
}
