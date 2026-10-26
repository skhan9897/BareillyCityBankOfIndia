<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="d-flex min-vh-100">
    <jsp:include page="/WEB-INF/views/common/customer-sidebar.jsp" />

    <div class="flex-grow-1 p-4 bg-light">
        <div class="row justify-content-center">
            <div class="col-lg-8">
                <div class="card card-bank p-4 p-md-5">
                    <div class="d-flex align-items-center mb-4">
                        <div class="stat-icon stat-icon-green me-3"><i class="fas fa-id-card"></i></div>
                        <div>
                            <h4 class="fw-bold text-navy mb-0">KYC Verification</h4>
                            <small class="text-muted">Current Verification Status: <span class="badge badge-status-active">${customer.kycStatus}</span></small>
                        </div>
                    </div>

                    <form data-ajax="true" action="${pageContext.request.contextPath}/customer/kyc" method="post">
                        <div class="form-alert"></div>

                        <div class="mb-3">
                            <label class="form-label small fw-bold">Select Document Type</label>
                            <select name="documentType" class="form-select" required>
                                <option value="AADHAAR">Aadhaar Card</option>
                                <option value="PAN">PAN Card</option>
                                <option value="PASSPORT">Passport</option>
                                <option value="VOTER_ID">Voter ID Card</option>
                            </select>
                        </div>

                        <div class="mb-3">
                            <label class="form-label small fw-bold">Document Identification Number</label>
                            <input type="text" name="documentNumber" class="form-control" required placeholder="Enter Aadhaar (12 digits) or PAN (10 chars)">
                        </div>

                        <button type="submit" class="btn btn-bank-gold w-100 py-2"><i class="fas fa-upload me-2"></i> Submit Document for Verification</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
