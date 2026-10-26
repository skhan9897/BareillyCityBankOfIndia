<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="d-flex min-vh-100">
    <jsp:include page="/WEB-INF/views/common/admin-sidebar.jsp" />

    <div class="flex-grow-1 p-4 bg-light">
        <!-- Header Bar -->
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h3 class="fw-bold text-navy mb-0">Executive Operations Command Center</h3>
                <small class="text-muted">Real-Time Core Banking Ledger, Cash Terminal & Account Controller</small>
            </div>
            <div class="d-flex gap-2">
                <span class="badge bg-navy text-warning px-3 py-2 fs-6 shadow-sm"><i class="fas fa-server me-1"></i> Core Ledger: ONLINE</span>
                <span class="badge bg-success px-3 py-2 fs-6 shadow-sm"><i class="fas fa-shield-alt me-1"></i> Double-Entry Active</span>
            </div>
        </div>

        <!-- Fully Clickable Metric KPI Stat Cards -->
        <div class="row g-4 mb-4">
            <div class="col-md-6 col-lg-3">
                <a href="${pageContext.request.contextPath}/admin/customers" class="text-decoration-none">
                    <div class="card card-bank p-4 border-0 shadow-sm card-hover-clickable">
                        <div class="d-flex align-items-center">
                            <div class="stat-icon stat-icon-blue me-3"><i class="fas fa-users"></i></div>
                            <div>
                                <div class="small text-muted">Total Bank Customers</div>
                                <h3 class="fw-bold text-navy mb-0">${stats.totalCustomers}</h3>
                                <div class="small text-primary fw-bold mt-1">View Directory <i class="fas fa-arrow-right ms-1"></i></div>
                            </div>
                        </div>
                    </div>
                </a>
            </div>

            <div class="col-md-6 col-lg-3">
                <a href="${pageContext.request.contextPath}/admin/accounts" class="text-decoration-none">
                    <div class="card card-bank p-4 border-0 shadow-sm card-hover-clickable">
                        <div class="d-flex align-items-center">
                            <div class="stat-icon stat-icon-gold me-3"><i class="fas fa-landmark"></i></div>
                            <div>
                                <div class="small text-muted">Active Bank Accounts</div>
                                <h3 class="fw-bold text-navy mb-0">${stats.totalAccounts}</h3>
                                <div class="small text-warning fw-bold mt-1">Manage Accounts <i class="fas fa-arrow-right ms-1"></i></div>
                            </div>
                        </div>
                    </div>
                </a>
            </div>

            <div class="col-md-6 col-lg-3">
                <a href="${pageContext.request.contextPath}/admin/reports" class="text-decoration-none">
                    <div class="card card-bank p-4 border-0 shadow-sm card-hover-clickable">
                        <div class="d-flex align-items-center">
                            <div class="stat-icon stat-icon-green me-3"><i class="fas fa-vault"></i></div>
                            <div>
                                <div class="small text-muted">Total Bank Ledger Deposits</div>
                                <h4 class="fw-bold text-success mb-0">₹ <fmt:formatNumber value="${stats.totalDeposits}" type="number" minFractionDigits="0"/></h4>
                                <div class="small text-success fw-bold mt-1">Ledger Audits <i class="fas fa-arrow-right ms-1"></i></div>
                            </div>
                        </div>
                    </div>
                </a>
            </div>

            <div class="col-md-6 col-lg-3">
                <a href="${pageContext.request.contextPath}/admin/transactions" class="text-decoration-none">
                    <div class="card card-bank p-4 border-0 shadow-sm card-hover-clickable">
                        <div class="d-flex align-items-center">
                            <div class="stat-icon stat-icon-red me-3"><i class="fas fa-exchange-alt"></i></div>
                            <div>
                                <div class="small text-muted">Posted Transactions</div>
                                <h3 class="fw-bold text-navy mb-0">${stats.totalTransactions}</h3>
                                <div class="small text-danger fw-bold mt-1">Transaction History <i class="fas fa-arrow-right ms-1"></i></div>
                            </div>
                        </div>
                    </div>
                </a>
            </div>
        </div>

        <!-- Executive Operations Toolbar (Deposit, Withdraw, Transfer, Account Controls) -->
        <div class="card card-bank p-4 mb-4 border-0 shadow-sm" style="background: linear-gradient(135deg, #0A2540 0%, #1A5276 100%); color: white;">
            <h5 class="fw-bold text-warning mb-3"><i class="fas fa-cogs me-2"></i>Executive Banking Operational Controls</h5>
            <div class="row g-3">
                <div class="col-md-3">
                    <button class="btn btn-bank-gold w-100 py-3 fw-bold shadow-sm" data-bs-toggle="modal" data-bs-target="#cashDepositModal">
                        <i class="fas fa-cash-register fa-lg d-block mb-1"></i> Cash Deposit Terminal
                    </button>
                </div>
                <div class="col-md-3">
                    <button class="btn btn-outline-light w-100 py-3 fw-bold shadow-sm" data-bs-toggle="modal" data-bs-target="#cashWithdrawModal">
                        <i class="fas fa-money-bill-wave fa-lg d-block mb-1"></i> Cash Withdrawal Terminal
                    </button>
                </div>
                <div class="col-md-3">
                    <button class="btn btn-warning w-100 py-3 text-dark fw-bold shadow-sm" data-bs-toggle="modal" data-bs-target="#adminTransferModal">
                        <i class="fas fa-random fa-lg d-block mb-1"></i> Admin Fund Transfer
                    </button>
                </div>
                <div class="col-md-3">
                    <a href="${pageContext.request.contextPath}/admin/customers" class="btn btn-light w-100 py-3 text-navy fw-bold shadow-sm">
                        <i class="fas fa-user-check fa-lg d-block mb-1"></i> Fast-Track KYC & Loans
                    </a>
                </div>
            </div>
        </div>

        <!-- Section: Customer Accounts Management Controller Table -->
        <div class="card card-bank p-4 mb-4 border-0 shadow-sm">
            <div class="d-flex justify-content-between align-items-center mb-3 border-bottom pb-2">
                <h5 class="fw-bold text-navy mb-0"><i class="fas fa-sliders-h text-primary me-2"></i>Account Controller & Status Manager</h5>
                <a href="${pageContext.request.contextPath}/admin/accounts" class="btn btn-sm btn-outline-primary fw-bold">Manage All Accounts</a>
            </div>

            <div class="table-responsive">
                <table class="table table-hover table-bank mb-0">
                    <thead>
                        <tr>
                            <th>Account Number</th>
                            <th>Customer Name</th>
                            <th>Product Type</th>
                            <th>Available Balance</th>
                            <th>Status</th>
                            <th>Actions & Block/Suspend Controls</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="acc" items="${allAccounts}">
                            <tr>
                                <td class="fw-bold text-navy">${acc.accountNumber}</td>
                                <td>${acc.customerName}</td>
                                <td><span class="badge bg-light text-dark border">${acc.productName}</span></td>
                                <td class="fw-bold text-success">₹ <fmt:formatNumber value="${acc.availableBalance}" type="number" minFractionDigits="2"/></td>
                                <td>
                                    <span class="badge ${acc.status == 'ACTIVE' ? 'badge-status-active' : (acc.status == 'SUSPENDED' ? 'bg-danger text-white' : 'badge-status-frozen')}">${acc.status}</span>
                                </td>
                                <td>
                                    <div class="d-flex gap-1">
                                        <button onclick="quickDeposit('${acc.accountNumber}')" class="btn btn-sm btn-success px-2 py-1" title="Deposit Cash"><i class="fas fa-plus-circle me-1"></i> Deposit</button>
                                        <button onclick="quickWithdraw('${acc.accountNumber}')" class="btn btn-sm btn-outline-danger px-2 py-1" title="Withdraw Cash"><i class="fas fa-minus-circle me-1"></i> Withdraw</button>
                                        <c:choose>
                                            <c:when test="${acc.status == 'ACTIVE'}">
                                                <button onclick="toggleAccountAction(${acc.id}, 'block')" class="btn btn-sm btn-warning text-dark px-2 py-1" title="Block / Freeze Account"><i class="fas fa-ban me-1"></i> Block</button>
                                                <button onclick="toggleAccountAction(${acc.id}, 'suspend')" class="btn btn-sm btn-danger px-2 py-1" title="Suspend Account"><i class="fas fa-user-slash me-1"></i> Suspend</button>
                                            </c:when>
                                            <c:otherwise>
                                                <button onclick="toggleAccountAction(${acc.id}, 'unfreeze')" class="btn btn-sm btn-success px-2 py-1" title="Unblock / Reactivate Account"><i class="fas fa-check-circle me-1"></i> Unblock</button>
                                            </c:otherwise>
                                        </c:choose>
                                        <a href="${pageContext.request.contextPath}/admin/account-details?id=${acc.id}" class="btn btn-sm btn-navy text-white px-2 py-1"><i class="fas fa-eye"></i> Details</a>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty allAccounts}">
                            <tr><td colspan="6" class="text-center text-muted py-3">No active customer accounts found.</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- Section: Daily Volume Chart & Pending Approvals -->
        <div class="row g-4">
            <div class="col-lg-8">
                <div class="card card-bank p-4 border-0 shadow-sm">
                    <h5 class="fw-bold text-navy mb-3"><i class="fas fa-chart-area me-2 text-primary"></i>Daily Transaction Volume Chart</h5>
                    <div style="height: 220px;">
                        <canvas id="txChart"></canvas>
                    </div>
                </div>
            </div>

            <div class="col-lg-4">
                <div class="card card-bank p-4 border-0 shadow-sm h-100">
                    <h5 class="fw-bold text-navy mb-3"><i class="fas fa-exclamation-circle text-warning me-2"></i>Pending Approvals</h5>
                    <div class="list-group list-group-flush">
                        <a href="${pageContext.request.contextPath}/admin/loans" class="list-group-item list-group-item-action d-flex justify-content-between align-items-center px-0">
                            <div>
                                <div class="fw-bold text-navy">Pending Loan Applications</div>
                                <small class="text-muted">Awaiting credit officer review</small>
                            </div>
                            <span class="badge bg-warning text-dark fs-6 px-3 py-2">${stats.pendingLoans}</span>
                        </a>
                        <a href="${pageContext.request.contextPath}/admin/customers" class="list-group-item list-group-item-action d-flex justify-content-between align-items-center px-0">
                            <div>
                                <div class="fw-bold text-navy">Pending Service Requests</div>
                                <small class="text-muted">Awaiting branch action</small>
                            </div>
                            <span class="badge bg-primary text-white fs-6 px-3 py-2">${stats.pendingRequests}</span>
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Modal 1: Cash Deposit Terminal -->
<div class="modal fade" id="cashDepositModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header bg-navy text-white">
                <h5 class="modal-title fw-bold"><i class="fas fa-cash-register me-2 text-warning"></i>Cash Deposit Terminal</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
            </div>
            <form data-ajax="true" action="${pageContext.request.contextPath}/admin/deposit" method="post">
                <input type="hidden" name="csrfToken" value="${csrfToken}">
                <div class="modal-body">
                    <div class="form-alert"></div>
                    <div class="mb-3">
                        <label class="form-label small fw-bold">Target Customer Account Number</label>
                        <input type="text" id="depAccountNum" name="accountNumber" class="form-control" required placeholder="100012345678">
                    </div>
                    <div class="mb-3">
                        <label class="form-label small fw-bold">Deposit Amount (₹)</label>
                        <input type="number" name="amount" class="form-control" required min="1" value="5000">
                    </div>
                    <div class="mb-3">
                        <label class="form-label small fw-bold">Remarks / Narration</label>
                        <input type="text" name="description" class="form-control" value="Cash Deposit at Branch Counter">
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-bank-gold"><i class="fas fa-check-circle me-1"></i> Post Cash Deposit</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Modal 2: Cash Withdrawal Terminal -->
<div class="modal fade" id="cashWithdrawModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header bg-navy text-white">
                <h5 class="modal-title fw-bold"><i class="fas fa-money-bill-wave me-2 text-danger"></i>Cash Withdrawal Terminal</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
            </div>
            <form data-ajax="true" action="${pageContext.request.contextPath}/admin/withdraw" method="post">
                <input type="hidden" name="csrfToken" value="${csrfToken}">
                <div class="modal-body">
                    <div class="form-alert"></div>
                    <div class="mb-3">
                        <label class="form-label small fw-bold">Customer Account Number</label>
                        <input type="text" id="wthAccountNum" name="accountNumber" class="form-control" required placeholder="100012345678">
                    </div>
                    <div class="mb-3">
                        <label class="form-label small fw-bold">Withdrawal Amount (₹)</label>
                        <input type="number" name="amount" class="form-control" required min="1" value="1000">
                    </div>
                    <div class="mb-3">
                        <label class="form-label small fw-bold">Remarks / Narration</label>
                        <input type="text" name="description" class="form-control" value="Cash Withdrawal at Teller Window">
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-danger"><i class="fas fa-check-circle me-1"></i> Post Cash Withdrawal</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Modal 3: Admin Transfer / Adjustment -->
<div class="modal fade" id="adminTransferModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header bg-navy text-white">
                <h5 class="modal-title fw-bold"><i class="fas fa-random me-2 text-warning"></i>Admin Inter-Account Transfer</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
            </div>
            <form data-ajax="true" action="${pageContext.request.contextPath}/admin/transfer" method="post">
                <input type="hidden" name="csrfToken" value="${csrfToken}">
                <div class="modal-body">
                    <div class="form-alert"></div>
                    <div class="mb-3">
                        <label class="form-label small fw-bold">Source Account Number</label>
                        <input type="text" name="fromAccount" class="form-control" required placeholder="100012345678">
                    </div>
                    <div class="mb-3">
                        <label class="form-label small fw-bold">Destination Account Number</label>
                        <input type="text" name="toAccount" class="form-control" required placeholder="100087654321">
                    </div>
                    <div class="mb-3">
                        <label class="form-label small fw-bold">Transfer Amount (₹)</label>
                        <input type="number" name="amount" class="form-control" required min="1" value="2000">
                    </div>
                    <div class="mb-3">
                        <label class="form-label small fw-bold">Adjustment Narration</label>
                        <input type="text" name="description" class="form-control" value="Admin Manual Adjustment Transfer">
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-bank-gold"><i class="fas fa-check-circle me-1"></i> Execute Transfer</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
function quickDeposit(accNum) {
    document.getElementById('depAccountNum').value = accNum;
    var modal = new bootstrap.Modal(document.getElementById('cashDepositModal'));
    modal.show();
}

function quickWithdraw(accNum) {
    document.getElementById('wthAccountNum').value = accNum;
    var modal = new bootstrap.Modal(document.getElementById('cashWithdrawModal'));
    modal.show();
}

function toggleAccountAction(accId, action) {
    let title = action === 'block' ? 'Blocking / Freezing Account' : (action === 'suspend' ? 'Suspending Customer Account' : 'Unblocking & Reactivating Account');
    let steps = [
        "Validating administrative override key...",
        "Updating account status in database to " + action.toUpperCase() + "...",
        "Account status updated successfully!"
    ];

    let csrfToken = '${csrfToken}';

    if (confirm("Confirm " + action + "ing account ID " + accId + "?")) {
        triggerQuickActionLoading(
            title,
            steps,
            "${pageContext.request.contextPath}/admin/" + action + "-account",
            'accountId=' + accId + '&reason=Admin Dashboard Direct Action&csrfToken=' + encodeURIComponent(csrfToken)
        );
    }
}

document.addEventListener('DOMContentLoaded', function() {
    const ctx = document.getElementById('txChart');
    if (ctx) {
        new Chart(ctx, {
            type: 'line',
            data: {
                labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
                datasets: [{
                    label: 'Transaction Volume (₹ Lakhs)',
                    data: [12, 19, 15, 25, 22, 30, 28],
                    borderColor: '#0A2540',
                    backgroundColor: 'rgba(10, 37, 64, 0.1)',
                    fill: true,
                    tension: 0.3
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: { legend: { display: false } },
                scales: {
                    y: { beginAtZero: true, grid: { color: '#E2E8F0' } },
                    x: { grid: { display: false } }
                }
            }
        });
    }
});
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
