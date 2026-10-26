<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="d-flex min-vh-100">
    <jsp:include page="/WEB-INF/views/common/admin-sidebar.jsp" />

    <div class="flex-grow-1 p-4 bg-light">
        <!-- Breadcrumb Header -->
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <a href="${pageContext.request.contextPath}/admin/customers" class="small text-decoration-none text-muted mb-1 d-inline-block"><i class="fas fa-arrow-left me-1"></i> Back to Customer Directory</a>
                <h3 class="fw-bold text-navy mb-0">Customer Inspection Profile: ${customer.fullName}</h3>
                <small class="text-muted">Customer Number: ${customer.customerNumber} | Branch: Civil Lines Main (BCBI0000001)</small>
            </div>
            <div class="d-flex gap-2">
                <span class="badge ${customer.kycStatus == 'APPROVED' ? 'badge-status-active' : 'badge-status-pending'} px-3 py-2 fs-6 shadow-sm"><i class="fas fa-shield-alt me-1"></i> KYC Status: ${customer.kycStatus}</span>
                <c:if test="${customer.kycStatus == 'PENDING'}">
                    <button onclick="approveKyc(${customer.id})" class="btn btn-success btn-sm fw-bold px-3"><i class="fas fa-check me-1"></i> Approve KYC</button>
                    <button onclick="rejectKyc(${customer.id})" class="btn btn-danger btn-sm fw-bold px-3"><i class="fas fa-times me-1"></i> Reject KYC</button>
                </c:if>
            </div>
        </div>

        <!-- Section 1: Customer Personal & Contact Profile -->
        <div class="card card-bank p-4 mb-4 border-0 shadow-sm">
            <h5 class="fw-bold text-navy mb-3 border-bottom pb-2"><i class="fas fa-id-card text-warning me-2"></i>Personal & Verification Profile</h5>
            <div class="row g-4">
                <div class="col-md-3 col-6">
                    <div class="small text-muted mb-1">Full Name</div>
                    <div class="fw-bold text-navy fs-6">${customer.fullName}</div>
                </div>
                <div class="col-md-3 col-6">
                    <div class="small text-muted mb-1">Customer ID Number</div>
                    <div class="fw-bold text-navy fs-6">${customer.customerNumber}</div>
                </div>
                <div class="col-md-3 col-6">
                    <div class="small text-muted mb-1">Registered Email</div>
                    <div class="fw-bold text-navy fs-6">${customer.email} <span class="badge bg-success-subtle text-success ms-1"><i class="fas fa-check-circle"></i> Verified</span></div>
                </div>
                <div class="col-md-3 col-6">
                    <div class="small text-muted mb-1">Mobile Number</div>
                    <div class="fw-bold text-navy fs-6">+91 ${customer.mobile} <span class="badge bg-success-subtle text-success ms-1"><i class="fas fa-check-circle"></i> Verified</span></div>
                </div>

                <div class="col-md-3 col-6">
                    <div class="small text-muted mb-1">Date of Birth</div>
                    <div class="fw-bold text-navy">${customer.dob}</div>
                </div>
                <div class="col-md-3 col-6">
                    <div class="small text-muted mb-1">Gender</div>
                    <div class="fw-bold text-navy">${customer.gender}</div>
                </div>
                <div class="col-md-3 col-6">
                    <div class="small text-muted mb-1">Aadhaar Card (Masked)</div>
                    <div class="fw-bold text-navy">${customer.aadhaarMasked}</div>
                </div>
                <div class="col-md-3 col-6">
                    <div class="small text-muted mb-1">PAN Card (Masked)</div>
                    <div class="fw-bold text-navy">${customer.panMasked}</div>
                </div>

                <div class="col-12">
                    <div class="small text-muted mb-1">Residential Street Address</div>
                    <div class="fw-bold text-navy">${customer.addressLine1}, ${customer.city}, ${customer.state} - ${customer.pincode}</div>
                </div>
            </div>
        </div>

        <!-- Section 2: Linked Customer Bank Accounts Table -->
        <div class="card card-bank p-4 mb-4 border-0 shadow-sm">
            <h5 class="fw-bold text-navy mb-3 border-bottom pb-2"><i class="fas fa-university text-primary me-2"></i>Linked Bank Accounts</h5>
            <div class="table-responsive">
                <table class="table table-hover table-bank mb-0">
                    <thead>
                        <tr>
                            <th>Account Number</th>
                            <th>Product Type</th>
                            <th>Available Balance</th>
                            <th>Ledger Balance</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="acc" items="${accounts}">
                            <tr>
                                <td class="fw-bold text-navy">${acc.accountNumber}</td>
                                <td><span class="badge bg-light text-dark border">${acc.productName}</span></td>
                                <td class="fw-bold text-success">₹ <fmt:formatNumber value="${acc.availableBalance}" type="number" minFractionDigits="2"/></td>
                                <td class="fw-bold text-dark">₹ <fmt:formatNumber value="${acc.ledgerBalance}" type="number" minFractionDigits="2"/></td>
                                <td><span class="badge ${acc.status == 'ACTIVE' ? 'badge-status-active' : 'badge-status-frozen'}">${acc.status}</span></td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/admin/account-details?id=${acc.id}" class="btn btn-sm btn-navy text-white px-3"><i class="fas fa-eye me-1"></i> Inspect Account</a>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty accounts}">
                            <tr><td colspan="6" class="text-center text-muted py-3">No bank accounts linked to this customer.</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- Section 3: Recent Transactions for Customer -->
        <div class="card card-bank p-4 border-0 shadow-sm">
            <h5 class="fw-bold text-navy mb-3 border-bottom pb-2"><i class="fas fa-history text-primary me-2"></i>Customer Transactions Ledger</h5>
            <div class="table-responsive">
                <table class="table table-hover table-bank mb-0">
                    <thead>
                        <tr>
                            <th>Transaction Ref</th>
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
                            <tr><td colspan="6" class="text-center text-muted py-3">No transaction history found for this customer.</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<script>
function approveKyc(id) {
    if (confirm("Approve KYC compliance for customer ID " + id + "?")) {
        triggerQuickActionLoading(
            "Approving Customer KYC Compliance",
            [
                "Verifying Aadhaar & PAN document numbers...",
                "Updating customer KYC status to APPROVED...",
                "KYC Approved successfully!"
            ],
            "${pageContext.request.contextPath}/admin/approve-kyc",
            'customerId=' + id + '&remarks=Approved+by+Compliance+Officer'
        );
    }
}

function rejectKyc(id) {
    let r = prompt("Enter reason for rejecting KYC compliance:");
    if (r) {
        triggerQuickActionLoading(
            "Rejecting Customer KYC Compliance",
            [
                "Logging rejection remarks...",
                "Updating customer KYC status to REJECTED...",
                "KYC Rejected."
            ],
            "${pageContext.request.contextPath}/admin/reject-kyc",
            'customerId=' + id + '&remarks=' + encodeURIComponent(r)
        );
    }
}
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
