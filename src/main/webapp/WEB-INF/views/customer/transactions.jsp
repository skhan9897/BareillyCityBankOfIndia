<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="d-flex min-vh-100">
    <jsp:include page="/WEB-INF/views/common/customer-sidebar.jsp" />

    <div class="flex-grow-1 p-4 bg-light">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h3 class="fw-bold text-navy mb-0">Transaction History</h3>
                <small class="text-muted">Real-time ledger posted transactions</small>
            </div>
            <div>
                <a href="${pageContext.request.contextPath}/customer/statement/pdf" class="btn btn-outline-danger btn-sm"><i class="fas fa-file-pdf me-1"></i> PDF</a>
                <a href="${pageContext.request.contextPath}/customer/statement/csv" class="btn btn-outline-success btn-sm"><i class="fas fa-file-csv me-1"></i> CSV</a>
            </div>
        </div>

        <div class="card card-bank p-4">
            <div class="table-responsive">
                <table class="table table-hover table-bank mb-0">
                    <thead>
                        <tr>
                            <th>Ref Number</th>
                            <th>Type</th>
                            <th>Amount</th>
                            <th>Description</th>
                            <th>Status</th>
                            <th>Date</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="tx" items="${transactions}">
                            <tr>
                                <td class="fw-bold text-navy small">${tx.transactionRef}</td>
                                <td><span class="badge bg-light text-dark border">${tx.transactionType}</span></td>
                                <td class="fw-bold text-success">₹ <fmt:formatNumber value="${tx.amount}" type="number" minFractionDigits="2"/></td>
                                <td class="small text-muted">${tx.description}</td>
                                <td><span class="badge badge-status-active">${tx.status}</span></td>
                                <td class="small text-muted">${tx.createdAt}</td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty transactions}">
                            <tr><td colspan="6" class="text-center text-muted py-4">No transactions found.</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
