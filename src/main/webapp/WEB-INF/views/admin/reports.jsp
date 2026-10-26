<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="d-flex min-vh-100">
    <jsp:include page="/WEB-INF/views/common/admin-sidebar.jsp" />

    <div class="flex-grow-1 p-4 bg-light">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h3 class="fw-bold text-navy mb-0">Master Audit & Financial Reports</h3>
                <small class="text-muted">Export master transaction records and ledger summaries</small>
            </div>
            <div>
                <a href="${pageContext.request.contextPath}/admin/reports/csv" class="btn btn-success"><i class="fas fa-file-csv me-1"></i> Export Master CSV Report</a>
            </div>
        </div>

        <div class="card card-bank p-4">
            <h5 class="fw-bold text-navy mb-3">Master System Transactions</h5>
            <div class="table-responsive">
                <table class="table table-hover table-bank mb-0">
                    <thead>
                        <tr>
                            <th>Ref</th>
                            <th>Type</th>
                            <th>Amount</th>
                            <th>Status</th>
                            <th>Date</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="t" items="${transactions}">
                            <tr>
                                <td class="fw-bold text-navy">${t.transactionRef}</td>
                                <td>${t.transactionType}</td>
                                <td class="fw-bold text-success">₹ <fmt:formatNumber value="${t.amount}" type="number" minFractionDigits="2"/></td>
                                <td><span class="badge badge-status-active">${t.status}</span></td>
                                <td>${t.createdAt}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
