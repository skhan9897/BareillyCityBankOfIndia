<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="bank-sidebar py-3 flex-shrink-0">
    <div class="px-4 mb-4 text-center">
        <div class="stat-icon stat-icon-gold mx-auto mb-2"><i class="fas fa-user-check"></i></div>
        <h6 class="mb-0 fw-bold text-white">${sessionScope.LOGGED_IN_USER.username}</h6>
        <span class="badge badge-status-active mt-1">CUSTOMER</span>
    </div>
    <ul class="nav flex-column">
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/customer/dashboard"><i class="fas fa-chart-pie me-2"></i> Dashboard</a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/customer/notifications"><i class="fas fa-bell me-2 text-warning"></i> SMS Alerts & Inbox</a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/customer/accounts"><i class="fas fa-wallet me-2"></i> Bank Accounts</a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/customer/transfer"><i class="fas fa-paper-plane me-2"></i> Fund Transfer</a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/customer/beneficiaries"><i class="fas fa-address-book me-2"></i> Beneficiaries</a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/customer/transactions"><i class="fas fa-history me-2"></i> Transactions</a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/customer/statement"><i class="fas fa-file-invoice me-2"></i> Statements</a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/customer/deposits"><i class="fas fa-piggy-bank me-2"></i> FD & RD Deposits</a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/customer/loans"><i class="fas fa-hand-holding-usd me-2"></i> Loans</a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/customer/cards"><i class="fas fa-credit-card me-2"></i> Virtual Cards & UPI</a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/customer/kyc"><i class="fas fa-id-card me-2"></i> KYC Verification</a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/customer/service-requests"><i class="fas fa-headset me-2"></i> Service Requests</a>
        </li>
    </ul>
</div>
