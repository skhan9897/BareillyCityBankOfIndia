<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="d-flex min-vh-100">
    <jsp:include page="/WEB-INF/views/common/admin-sidebar.jsp" />

    <div class="flex-grow-1 p-4 bg-light">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h3 class="fw-bold text-navy mb-0">Master Account Controller</h3>
                <small class="text-muted">General Ledger Customer Account Management</small>
            </div>
            <div>
                <a href="${pageContext.request.contextPath}/register" target="_blank" class="btn btn-bank-gold fw-bold shadow-sm"><i class="fas fa-user-plus me-1"></i> Open New Account</a>
            </div>
        </div>

        <div class="card card-bank p-4 border-0 shadow-sm">
            <div class="table-responsive">
                <table class="table table-hover table-bank mb-0">
                    <thead>
                        <tr>
                            <th>Account Number</th>
                            <th>Customer Name</th>
                            <th>Type</th>
                            <th>Available Balance</th>
                            <th>Status</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="a" items="${accounts}">
                            <tr>
                                <td class="fw-bold text-navy">${a.accountNumber}</td>
                                <td>${a.customerName}</td>
                                <td><span class="badge bg-light text-dark border">${a.accountType}</span></td>
                                <td class="fw-bold text-success">₹ <fmt:formatNumber value="${a.availableBalance}" type="number" minFractionDigits="2"/></td>
                                <td><span class="badge ${a.status == 'ACTIVE' ? 'badge-status-active' : 'badge-status-frozen'}">${a.status}</span></td>
                                <td><a href="${pageContext.request.contextPath}/admin/account-details?id=${a.id}" class="btn btn-sm btn-navy text-white px-3"><i class="fas fa-eye me-1"></i> Inspect Account</a></td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty accounts}">
                            <tr><td colspan="6" class="text-center text-muted py-3">No active customer accounts found.</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
