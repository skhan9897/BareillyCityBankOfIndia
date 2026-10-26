<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="d-flex min-vh-100">
    <jsp:include page="/WEB-INF/views/common/admin-sidebar.jsp" />

    <div class="flex-grow-1 p-4 bg-light">
        <h3 class="fw-bold text-navy mb-4">Loan Applications Approval Workflow</h3>

        <div class="card card-bank p-4">
            <div class="table-responsive">
                <table class="table table-hover table-bank mb-0">
                    <thead>
                        <tr>
                            <th>Application Ref</th>
                            <th>Customer Name</th>
                            <th>Product</th>
                            <th>Requested Amount</th>
                            <th>Tenure</th>
                            <th>Income</th>
                            <th>Status</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="app" items="${applications}">
                            <tr>
                                <td class="fw-bold text-navy">${app.applicationRef}</td>
                                <td>${app.customerName}</td>
                                <td>${app.productName}</td>
                                <td class="fw-bold text-navy">₹ ${app.requestedAmount}</td>
                                <td>${app.tenureMonths} Mos</td>
                                <td>₹ ${app.monthlyIncome}</td>
                                <td><span class="badge ${app.status == 'DISBURSED' ? 'badge-status-active' : 'badge-status-pending'}">${app.status}</span></td>
                                <td>
                                    <c:if test="${app.status == 'PENDING'}">
                                        <button onclick="approveLoan(${app.id})" class="btn btn-sm btn-success"><i class="fas fa-check me-1"></i> Approve & Disburse</button>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<script>
function approveLoan(appId) {
    if (confirm("Approve loan application and disburse funds to customer's savings account?")) {
        fetch("${pageContext.request.contextPath}/admin/approve-loan", {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded', 'X-Requested-With': 'XMLHttpRequest'},
            body: 'applicationId=' + appId
        }).then(r => r.json()).then(d => { alert(d.message); window.location.reload(); });
    }
}
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
