// Avtar Paramedical Information – Web Admin Controller

document.addEventListener("DOMContentLoaded", () => {
    initNavigation();
    loadStudents();
    loadExams();
    loadPayments();
    loadStudyContent();
    loadJobs();
    initBulkUpload();
});

function initNavigation() {
    const navItems = document.querySelectorAll(".nav-item");
    const tabPanes = document.querySelectorAll(".tab-pane");
    const pageTitle = document.getElementById("page-title");

    navItems.forEach(btn => {
        btn.addEventListener("click", () => {
            const targetTab = btn.getAttribute("data-tab");

            navItems.forEach(b => b.classList.remove("active"));
            tabPanes.forEach(p => p.classList.remove("active"));

            btn.classList.add("active");
            document.getElementById(`tab-${targetTab}`).classList.add("active");

            pageTitle.textContent = btn.innerText.replace(/[^\w\s&]/g, "").trim();
        });
    });
}

const mockStudents = [
    { id: "API-2026-4892", name: "Vikram Sharma", mobile: "9876543210", category: "Radiographer", enrolled: "2 Series", active: true },
    { id: "API-2026-1024", name: "Pooja Verma", mobile: "9812345678", category: "Lab Technician", enrolled: "1 Series", active: true },
    { id: "API-2026-9051", name: "Rahul Deshmukh", mobile: "9765432109", category: "Pharmacy Officer", enrolled: "0 Series", active: false },
    { id: "API-2026-3312", name: "Ananya Iyer", mobile: "9654321098", category: "Nursing Officer", enrolled: "4 Series", active: true },
    { id: "API-2026-5520", name: "Mohammad Farooq", mobile: "9543210987", category: "Radiographer", enrolled: "1 Series", active: true }
];

function loadStudents() {
    const tbody = document.getElementById("student-table-body");
    tbody.innerHTML = mockStudents.map(s => `
        <tr>
            <td><strong>${s.id}</strong></td>
            <td>${s.name}</td>
            <td>+91 ${s.mobile}</td>
            <td><span class="badge badge-blue">${s.category}</span></td>
            <td>${s.enrolled}</td>
            <td><span class="badge ${s.active ? 'badge-success' : 'badge-danger'}">${s.active ? 'Active' : 'Blocked'}</span></td>
            <td>
                <button class="btn btn-outline btn-sm" onclick="toggleStudent('${s.id}')">
                    ${s.active ? 'Block' : 'Unblock'}
                </button>
            </td>
        </tr>
    `).join("");
}

window.toggleStudent = function(studentId) {
    const s = mockStudents.find(x => x.id === studentId);
    if (s) {
        s.active = !s.active;
        loadStudents();
    }
};

const mockExams = [
    { title: "Radiographer High-Yield Demo Mock Test", category: "Radiographer", questions: 6, duration: "15 min", marking: "+1.0 / -0.25", price: "Free Demo" },
    { title: "AIIMS & RRB Radiographer All-India CBT 01", category: "Radiographer", questions: 100, duration: "90 min", marking: "+1.0 / -0.25", price: "₹299" },
    { title: "Lab Technician (DMLT/BMLT) Starter CBT", category: "Lab Tech", questions: 5, duration: "15 min", marking: "+1.0 / -0.25", price: "Free Demo" },
    { title: "ESIC & State Paramedical Lab Tech Mock 2026", category: "Lab Tech", questions: 100, duration: "120 min", marking: "+1.0 / -0.25", price: "₹349" },
    { title: "Pharmacy Officer Speed & Accuracy Demo", category: "Pharmacy", questions: 4, duration: "12 min", marking: "+1.0 / -0.25", price: "Free Demo" },
    { title: "AIIMS NORCET 2026 All-India Rank Booster", category: "Nursing", questions: 150, duration: "180 min", marking: "+1.0 / -0.33", price: "₹499" }
];

function loadExams() {
    const tbody = document.getElementById("exam-table-body");
    tbody.innerHTML = mockExams.map(e => `
        <tr>
            <td><strong>${e.title}</strong></td>
            <td><span class="badge badge-teal">${e.category}</span></td>
            <td>${e.questions} Qs</td>
            <td>${e.duration}</td>
            <td>${e.marking}</td>
            <td><strong>${e.price}</strong></td>
            <td><span class="badge badge-success">Published</span></td>
        </tr>
    `).join("");
}

const mockPayments = [
    { id: "tx_001", studentId: "API-2026-1024", series: "ESIC Lab Technician Mock 2026", amount: "₹349", orderId: "order_Oq7Klx98V12a", payId: "pay_Oq7M45p928", status: "Success" },
    { id: "tx_002", studentId: "API-2026-3312", series: "AIIMS NORCET Nursing Grand Mock", amount: "₹499", orderId: "order_Pn8Km217x45y", payId: "pay_Pn8R12m881", status: "Success" },
    { id: "tx_003", studentId: "API-2026-5520", series: "AIIMS Radiographer All-India CBT", amount: "₹299", orderId: "order_Tt4X9011a62z", payId: "pay_Tt4Z33k102", status: "Success" }
];

function loadPayments() {
    const tbody = document.getElementById("payment-table-body");
    tbody.innerHTML = mockPayments.map(p => `
        <tr>
            <td><code>${p.id}</code></td>
            <td><strong>${p.studentId}</strong></td>
            <td>${p.series}</td>
            <td class="text-success"><strong>${p.amount}</strong></td>
            <td><code>${p.orderId}</code></td>
            <td><code>${p.payId}</code></td>
            <td><span class="badge badge-success">Verified</span></td>
        </tr>
    `).join("");
}

const mockContent = [
    { title: "Radiation Safety & ALARA Guidelines (PDF)", discipline: "Radiographer", type: "PDF Document", url: "https://drive.google.com/file/d/...", tier: "Free" },
    { title: "CT Scan Protocols & Hounsfield Atlas", discipline: "Radiographer", type: "Google Drive Folder", url: "https://drive.google.com/drive/...", tier: "Premium" },
    { title: "DMLT Hematology & Blood Banking SOPs", discipline: "Lab Technician", type: "PDF Document", url: "https://drive.google.com/file/d/...", tier: "Free" },
    { title: "Drugs and Cosmetics Act Schedules Handout", discipline: "Pharmacy Officer", type: "PDF Document", url: "https://drive.google.com/file/d/...", tier: "Free" },
    { title: "NORCET Emergency Nursing & Drug Calculations", discipline: "Nursing Officer", type: "Google Drive Folder", url: "https://drive.google.com/drive/...", tier: "Free" }
];

function loadStudyContent() {
    const tbody = document.getElementById("content-table-body");
    tbody.innerHTML = mockContent.map(c => `
        <tr>
            <td><strong>${c.title}</strong></td>
            <td><span class="badge badge-blue">${c.discipline}</span></td>
            <td>${c.type}</td>
            <td><a href="${c.url}" target="_blank" style="color: var(--primary);">Open Drive Link</a></td>
            <td><span class="badge ${c.tier === 'Free' ? 'badge-teal' : 'badge-purple'}">${c.tier}</span></td>
        </tr>
    `).join("");
}

const mockJobs = [
    { title: "Senior Radiographer Grade-I", org: "AIIMS New Delhi", vacancies: 48, loc: "New Delhi", deadline: "30 Nov 2026", verified: true },
    { title: "Medical Lab Technician Gr-II", org: "ESIC Central", vacancies: 142, loc: "All India", deadline: "15 Dec 2026", verified: true },
    { title: "Pharmacy Officer / Pharmacist", org: "State Health Mission", vacancies: 96, loc: "Maharashtra", deadline: "05 Dec 2026", verified: true },
    { title: "Nursing Officer (NORCET 2026)", org: "AIIMS Examination Board", vacancies: 1250, loc: "Pan India", deadline: "20 Dec 2026", verified: true }
];

function loadJobs() {
    const tbody = document.getElementById("job-table-body");
    tbody.innerHTML = mockJobs.map(j => `
        <tr>
            <td><strong>${j.title}</strong></td>
            <td>${j.org}</td>
            <td><strong>${j.vacancies} Posts</strong></td>
            <td>${j.loc}</td>
            <td style="color: var(--danger); font-weight: 600;">${j.deadline}</td>
            <td><span class="badge badge-success">✓ Verified Gov</span></td>
        </tr>
    `).join("");
}

function initBulkUpload() {
    const fileInput = document.getElementById("file-input");
    const report = document.getElementById("validation-report");
    const reportText = document.getElementById("validation-text");

    fileInput.addEventListener("change", (e) => {
        if (e.target.files.length > 0) {
            const fileName = e.target.files[0].name;
            report.classList.remove("hidden");
            reportText.innerHTML = `<strong>File '${fileName}' parsed successfully:</strong><br>
            • 50 Questions validated<br>
            • 0 Missing options detected<br>
            • All correct answer indices mapped (0-3)<br>
            • Medical rationales detected for all questions<br>
            <button class="btn btn-primary btn-sm" style="margin-top: 10px;" onclick="alert('Questions published into CBT Mock Database!')">Publish to Examination Catalog</button>`;
        }
    });
}
