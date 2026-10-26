<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="d-flex min-vh-100">
    <jsp:include page="/WEB-INF/views/common/customer-sidebar.jsp" />

    <div class="flex-grow-1 p-4 bg-light">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h3 class="fw-bold text-navy mb-0">Customer Service Requests</h3>
                <small class="text-muted">Request cheque books, address changes, or interest certificates</small>
            </div>
            <button class="btn btn-bank-gold" data-bs-toggle="modal" data-bs-target="#newSrModal"><i class="fas fa-plus me-1"></i> New Request</button>
        </div>

        <div class="card card-bank p-4">
            <div class="table-responsive">
                <table class="table table-hover table-bank mb-0">
                    <thead>
                        <tr>
                            <th>Ref Number</th>
                            <th>Request Type</th>
                            <th>Details</th>
                            <th>Status</th>
                            <th>Submitted Date</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="sr" items="${requests}">
                            <tr>
                                <td class="fw-bold text-navy">${sr.requestRef}</td>
                                <td><span class="badge bg-light text-dark border">${sr.requestType}</span></td>
                                <td class="small text-muted">${sr.details}</td>
                                <td><span class="badge ${sr.status == 'RESOLVED' ? 'badge-status-active' : 'badge-status-pending'}">${sr.status}</span></td>
                                <td class="small text-muted">${sr.createdAt}</td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty requests}">
                            <tr><td colspan="5" class="text-center text-muted py-4">No active service requests.</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<!-- Modal: New SR -->
<div class="modal fade" id="newSrModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header bg-navy text-white">
                <h5 class="modal-title fw-bold">Log Service Request</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
            </div>
            <form data-ajax="true" action="${pageContext.request.contextPath}/customer/create-service-request" method="post">
                <div class="modal-body">
                    <div class="form-alert"></div>
                    <div class="mb-3">
                        <label class="form-label small fw-bold">Request Type</label>
                        <select name="requestType" class="form-select" required>
                            <option value="CHEQUE_BOOK">New Cheque Book Request (25 Leaves)</option>
                            <option value="DEBIT_CARD">Debit Card Replacement</option>
                            <option value="ADDRESS_UPDATE">Residential Address Update</option>
                            <option value="INTEREST_CERTIFICATE">Annual Interest Certificate</option>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label class="form-label small fw-bold">Additional Details / Instructions</label>
                        <textarea name="details" class="form-control" rows="3" placeholder="Provide any specific instructions..."></textarea>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-bank-gold"><i class="fas fa-paper-plane me-1"></i> Submit Request</button>
                </div>
            </form>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
