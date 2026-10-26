<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="d-flex min-vh-100">
    <jsp:include page="/WEB-INF/views/common/admin-sidebar.jsp" />

    <div class="flex-grow-1 p-4 bg-light">
        <h3 class="fw-bold text-navy mb-4">Master Ledger Transactions</h3>

        <div class="card card-bank p-4">
            <div class="table-responsive">
                <table class="table table-hover table-bank mb-0">
                    <thead>
                        <tr>
                            <th>Ref No</th>
                            <th>From Account</th>
                            <th>To Account</th>
                            <th>Type</th>
                            <th>Amount</th>
                            <th>Status</th>
                            <th>Posted At</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="tx" items="${transactions}">
                            <tr>
                                <td class="fw-bold text-navy small">${tx.transactionRef}</td>
                                <td>${tx.fromAccountNumber}</td>
                                <td>${tx.toAccountNumber}</td>
                                <td><span class="badge bg-light text-dark border">${tx.transactionType}</span></td>
                                <td class="fw-bold text-success">₹ <fmt:formatNumber value="${tx.amount}" type="number" minFractionDigits="2"/></td>
                                <td><span class="badge badge-status-active">${tx.status}</span></td>
                                <td class="small text-muted">${tx.createdAt}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
