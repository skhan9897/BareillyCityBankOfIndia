<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="sidebar-bank py-3 flex-shrink-0" style="background-color: #0A2540; min-width: 250px;">
    <div class="px-4 mb-3 text-center border-bottom border-secondary pb-3">
        <div class="bg-warning text-dark fw-bold rounded-circle d-flex align-items-center justify-content-center mx-auto mb-2 shadow" style="width: 52px; height: 52px; font-size: 1.25rem;">
            <i class="fas fa-user-shield"></i>
        </div>
        <h6 class="mb-0 fw-bold text-white">${sessionScope.LOGGED_IN_USER.username}</h6>
        <span class="badge bg-warning text-dark fw-bold mt-1">ADMINISTRATOR</span>
    </div>

    <div class="px-3 mb-3">
        <a class="btn btn-bank-gold w-100 py-2.5 fw-bold shadow-sm d-flex align-items-center justify-content-center" href="${pageContext.request.contextPath}/register" target="_blank">
            <i class="fas fa-user-plus me-2"></i> Open New Account
        </a>
    </div>

    <ul class="nav flex-column">
        <li class="nav-item">
            <a class="nav-link text-white-50" href="${pageContext.request.contextPath}/admin/dashboard"><i class="fas fa-tachometer-alt me-2 text-warning"></i> Executive Overview</a>
        </li>
        <li class="nav-item">
            <a class="nav-link text-white-50" href="${pageContext.request.contextPath}/admin/customers"><i class="fas fa-users me-2 text-info"></i> Customer Management</a>
        </li>
        <li class="nav-item">
            <a class="nav-link text-white-50" href="${pageContext.request.contextPath}/admin/accounts"><i class="fas fa-landmark me-2 text-success"></i> Account Controller</a>
        </li>

        <!-- Operational Action Shortcuts -->
        <li class="px-3 pt-3 pb-1 text-uppercase small text-warning fw-bold" style="font-size: 0.7rem; letter-spacing: 1px;">Teller Cash Terminals</li>
        <li class="nav-item">
            <a class="nav-link text-white-50" href="${pageContext.request.contextPath}/admin/dashboard#cashDepositModal" onclick="quickModal('cashDepositModal')"><i class="fas fa-cash-register me-2 text-success"></i> Cash Deposit</a>
        </li>
        <li class="nav-item">
            <a class="nav-link text-white-50" href="${pageContext.request.contextPath}/admin/dashboard#cashWithdrawModal" onclick="quickModal('cashWithdrawModal')"><i class="fas fa-money-bill-wave me-2 text-danger"></i> Cash Withdrawal</a>
        </li>
        <li class="nav-item">
            <a class="nav-link text-white-50" href="${pageContext.request.contextPath}/admin/dashboard#adminTransferModal" onclick="quickModal('adminTransferModal')"><i class="fas fa-random me-2 text-warning"></i> Fund Transfer</a>
        </li>

        <li class="px-3 pt-3 pb-1 text-uppercase small text-warning fw-bold" style="font-size: 0.7rem; letter-spacing: 1px;">Compliance & Audits</li>
        <li class="nav-item">
            <a class="nav-link text-white-50" href="${pageContext.request.contextPath}/admin/transactions"><i class="fas fa-exchange-alt me-2 text-info"></i> Transaction Controller</a>
        </li>
        <li class="nav-item">
            <a class="nav-link text-white-50" href="${pageContext.request.contextPath}/admin/reversals"><i class="fas fa-undo me-2 text-danger"></i> Reversals & Reconcile</a>
        </li>
        <li class="nav-item">
            <a class="nav-link text-white-50" href="${pageContext.request.contextPath}/admin/loans"><i class="fas fa-file-signature me-2 text-primary"></i> Loan Approvals</a>
        </li>
        <li class="nav-item">
            <a class="nav-link text-white-50" href="${pageContext.request.contextPath}/admin/reports"><i class="fas fa-chart-line me-2 text-success"></i> Reports & Audits</a>
        </li>
        <li class="nav-item">
            <a class="nav-link text-white-50" href="${pageContext.request.contextPath}/admin/audit-logs"><i class="fas fa-clipboard-list me-2 text-secondary"></i> System Audit Logs</a>
        </li>
        <li class="nav-item">
            <a class="nav-link text-white-50" href="${pageContext.request.contextPath}/admin/settings"><i class="fas fa-cogs me-2 text-warning"></i> System Settings</a>
        </li>
    </ul>
</div>

<script>
function quickModal(modalId) {
    if (window.location.pathname.endsWith('/admin/dashboard')) {
        var m = new bootstrap.Modal(document.getElementById(modalId));
        m.show();
    } else {
        window.location.href = '${pageContext.request.contextPath}/admin/dashboard';
    }
}
</script>
