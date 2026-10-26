<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="d-flex min-vh-100">
    <jsp:include page="/WEB-INF/views/common/admin-sidebar.jsp" />

    <div class="flex-grow-1 p-4 bg-light">
        <!-- Page Header -->
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h3 class="fw-bold text-navy mb-0">Customer Directory & KYC Compliance Portal</h3>
                <small class="text-muted">Manage bank customer profiles, identity records & fast-track KYC approvals</small>
            </div>
            <div>
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-outline-navy fw-bold"><i class="fas fa-arrow-left me-1"></i> Dashboard</a>
            </div>
        </div>

        <!-- Metric KPI Summary Bar -->
        <div class="row g-4 mb-4">
            <div class="col-md-6 col-lg-3">
                <div class="card card-bank p-4 border-0 shadow-sm">
                    <div class="d-flex align-items-center">
                        <div class="stat-icon stat-icon-blue me-3"><i class="fas fa-users"></i></div>
                        <div>
                            <div class="small text-muted">Total Customers</div>
                            <h3 class="fw-bold text-navy mb-0">${customers.size()}</h3>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-md-6 col-lg-3">
                <div class="card card-bank p-4 border-0 shadow-sm">
                    <div class="d-flex align-items-center">
                        <div class="stat-icon stat-icon-green me-3"><i class="fas fa-user-check"></i></div>
                        <div>
                            <div class="small text-muted">KYC Approved</div>
                            <h3 class="fw-bold text-success mb-0">
                                <c:set var="approvedCount" value="0"/>
                                <c:forEach var="cust" items="${customers}">
                                    <c:if test="${cust.kycStatus == 'APPROVED'}"><c:set var="approvedCount" value="${approvedCount + 1}"/></c:if>
                                </c:forEach>
                                ${approvedCount}
                            </h3>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-md-6 col-lg-3">
                <div class="card card-bank p-4 border-0 shadow-sm">
                    <div class="d-flex align-items-center">
                        <div class="stat-icon stat-icon-gold me-3"><i class="fas fa-hourglass-half"></i></div>
                        <div>
                            <div class="small text-muted">KYC Pending Action</div>
                            <h3 class="fw-bold text-warning mb-0">
                                <c:set var="pendingCount" value="0"/>
                                <c:forEach var="cust" items="${customers}">
                                    <c:if test="${cust.kycStatus == 'PENDING'}"><c:set var="pendingCount" value="${pendingCount + 1}"/></c:if>
                                </c:forEach>
                                ${pendingCount}
                            </h3>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-md-6 col-lg-3">
                <div class="card card-bank p-4 border-0 shadow-sm">
                    <div class="d-flex align-items-center">
                        <div class="stat-icon stat-icon-red me-3"><i class="fas fa-shield-alt"></i></div>
                        <div>
                            <div class="small text-muted">Core Compliance</div>
                            <h3 class="fw-bold text-primary mb-0">100% Active</h3>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Search Directory Card -->
        <div class="card card-bank p-4 mb-4 border-0 shadow-sm">
            <form method="get" action="${pageContext.request.contextPath}/admin/customers">
                <div class="input-group input-group-lg">
                    <span class="input-group-text bg-white text-navy border-end-0"><i class="fas fa-search"></i></span>
                    <input type="text" name="q" class="form-control border-start-0" placeholder="Search by Customer ID, Name, Email, Mobile, or Aadhaar..." value="${param.q}">
                    <button type="submit" class="btn btn-bank-primary px-4 fw-bold"><i class="fas fa-filter me-1"></i> Search Directory</button>
                    <a href="${pageContext.request.contextPath}/admin/customers" class="btn btn-outline-secondary px-3"><i class="fas fa-redo"></i> Reset</a>
                </div>
            </form>
        </div>

        <!-- Customer Directory Table -->
        <div class="card card-bank p-4 border-0 shadow-sm">
            <div class="d-flex justify-content-between align-items-center mb-3 border-bottom pb-2">
                <h5 class="fw-bold text-navy mb-0"><i class="fas fa-list text-primary me-2"></i>Customer Directory Records</h5>
                <small class="text-muted">Showing ${customers.size()} records</small>
            </div>

            <div class="table-responsive">
                <table class="table table-hover table-bank mb-0">
                    <thead>
                        <tr>
                            <th>Customer Info</th>
                            <th>Contact Info</th>
                            <th>Identity Proof</th>
                            <th>Residential City</th>
                            <th>KYC Status</th>
                            <th>Fast Actions & Review</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="c" items="${customers}">
                            <tr>
                                <td>
                                    <div class="d-flex align-items-center gap-2">
                                        <div class="bg-navy text-warning fw-bold rounded-circle d-flex align-items-center justify-content-center shadow-sm" style="width: 42px; height: 42px; font-size: 1.1rem;">
                                            ${c.firstName.substring(0, 1)}${c.lastName.substring(0, 1)}
                                        </div>
                                        <div>
                                            <div class="fw-bold text-navy">${c.fullName}</div>
                                            <div class="small text-muted">ID: <strong class="text-dark">${c.customerNumber}</strong></div>
                                        </div>
                                    </div>
                                </td>
                                <td>
                                    <div class="small fw-bold text-navy"><i class="fas fa-envelope text-muted me-1"></i> ${c.email}</div>
                                    <div class="small text-muted"><i class="fas fa-phone text-muted me-1"></i> +91 ${c.mobile}</div>
                                </td>
                                <td>
                                    <div class="small">Aadhaar: <strong class="text-navy">${c.aadhaarMasked}</strong></div>
                                    <div class="small">PAN: <strong class="text-navy">${c.panMasked}</strong></div>
                                </td>
                                <td>
                                    <div class="small text-navy fw-bold"><i class="fas fa-map-marker-alt text-danger me-1"></i> ${c.city}</div>
                                    <div class="small text-muted">${c.state}</div>
                                </td>
                                <td>
                                    <span class="badge ${c.kycStatus == 'APPROVED' ? 'badge-status-active' : 'badge-status-pending'} px-3 py-2 fs-6">
                                        <i class="fas ${c.kycStatus == 'APPROVED' ? 'fa-check-circle' : 'fa-hourglass-half'} me-1"></i> ${c.kycStatus}
                                    </span>
                                </td>
                                <td>
                                    <div class="d-flex gap-1">
                                        <a href="${pageContext.request.contextPath}/admin/customer-details?id=${c.id}" class="btn btn-sm btn-bank-primary px-2.5 py-1.5" title="View Full Profile"><i class="fas fa-user-circle me-1"></i> View Profile</a>
                                        <c:if test="${c.kycStatus == 'PENDING'}">
                                            <button onclick="approveKyc(${c.id})" class="btn btn-sm btn-success px-2.5 py-1.5 fw-bold"><i class="fas fa-check me-1"></i> Approve</button>
                                            <button onclick="rejectKyc(${c.id})" class="btn btn-sm btn-outline-danger px-2.5 py-1.5 fw-bold"><i class="fas fa-times me-1"></i> Reject</button>
                                        </c:if>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty customers}">
                            <tr><td colspan="6" class="text-center text-muted py-4">No customers found matching search query.</td></tr>
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
