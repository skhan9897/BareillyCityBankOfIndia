<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="d-flex min-vh-100">
    <jsp:include page="/WEB-INF/views/common/customer-sidebar.jsp" />

    <div class="flex-grow-1 p-4 bg-light">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h3 class="fw-bold text-navy mb-0">Beneficiary Management</h3>
                <small class="text-muted">Manage trusted accounts for quick fund transfer</small>
            </div>
            <button class="btn btn-bank-gold" data-bs-toggle="modal" data-bs-target="#addBenModal"><i class="fas fa-plus me-1"></i> Add Beneficiary</button>
        </div>

        <div class="card card-bank p-4">
            <div class="table-responsive">
                <table class="table table-hover table-bank mb-0">
                    <thead>
                        <tr>
                            <th>Beneficiary Name</th>
                            <th>Account Number</th>
                            <th>IFSC Code</th>
                            <th>Status</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td class="fw-bold text-navy">Priya Singh</td>
                            <td>100020003003</td>
                            <td>BCBI0000001</td>
                            <td><span class="badge badge-status-active">ACTIVE</span></td>
                            <td><a href="${pageContext.request.contextPath}/customer/transfer?to=100020003003" class="btn btn-sm btn-outline-primary"><i class="fas fa-paper-plane me-1"></i> Transfer</a></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
