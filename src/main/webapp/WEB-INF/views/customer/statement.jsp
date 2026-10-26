<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="d-flex min-vh-100">
    <jsp:include page="/WEB-INF/views/common/customer-sidebar.jsp" />

    <div class="flex-grow-1 p-4 bg-light">
        <h3 class="fw-bold text-navy mb-4">Official Bank Account Statement</h3>

        <div class="card card-bank p-4 mb-4">
            <form method="get" action="${pageContext.request.contextPath}/customer/statement">
                <div class="row g-3 align-items-end">
                    <div class="col-md-4">
                        <label class="form-label small fw-bold">Select Account</label>
                        <select name="accountId" class="form-select">
                            <c:forEach var="a" items="${accounts}">
                                <option value="${a.id}" ${a.id == selectedAccount.id ? 'selected' : ''}>${a.accountNumber} (${a.accountType})</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-3">
                        <button type="submit" class="btn btn-bank-primary w-100"><i class="fas fa-search me-1"></i> View Statement</button>
                    </div>
                    <div class="col-md-5 text-end">
                        <a href="${pageContext.request.contextPath}/customer/statement/pdf?accountId=${selectedAccount.id}" class="btn btn-danger"><i class="fas fa-file-pdf me-1"></i> Download PDF</a>
                        <a href="${pageContext.request.contextPath}/customer/statement/csv?accountId=${selectedAccount.id}" class="btn btn-success"><i class="fas fa-file-csv me-1"></i> Export CSV</a>
                    </div>
                </div>
            </form>
        </div>

        <div class="card card-bank p-4">
            <h5 class="fw-bold text-navy mb-3">Statement Summary for Account: ${selectedAccount.accountNumber}</h5>
            <div class="table-responsive">
                <table class="table table-striped table-bank">
                    <thead>
                        <tr>
                            <th>Date</th>
                            <th>Reference ID</th>
                            <th>Transaction Type</th>
                            <th>Amount</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="t" items="${transactions}">
                            <tr>
                                <td>${t.createdAt}</td>
                                <td class="fw-bold text-navy">${t.transactionRef}</td>
                                <td>${t.transactionType}</td>
                                <td class="fw-bold text-success">₹ <fmt:formatNumber value="${t.amount}" type="number" minFractionDigits="2"/></td>
                                <td><span class="badge badge-status-active">${t.status}</span></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
