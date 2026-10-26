<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="d-flex min-vh-100">
    <jsp:include page="/WEB-INF/views/common/customer-sidebar.jsp" />

    <div class="flex-grow-1 p-4 bg-light">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h3 class="fw-bold text-navy mb-0">Bank Accounts</h3>
                <small class="text-muted">Manage your Savings and Current Accounts</small>
            </div>
            <button class="btn btn-bank-gold" data-bs-toggle="modal" data-bs-target="#newAccountModal"><i class="fas fa-plus me-1"></i> Open Additional Account</button>
        </div>

        <div class="row g-4">
            <c:forEach var="acc" items="${accounts}">
                <div class="col-lg-6">
                    <div class="card card-bank p-4">
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <h5 class="fw-bold text-navy mb-0"><i class="fas fa-university me-2 text-primary"></i>${acc.productName}</h5>
                            <span class="badge ${acc.status == 'ACTIVE' ? 'badge-status-active' : 'badge-status-frozen'}">${acc.status}</span>
                        </div>
                        <div class="row g-3 mb-3">
                            <div class="col-6">
                                <div class="small text-muted">Account Number</div>
                                <div class="fw-bold fs-6">${acc.accountNumber}</div>
                            </div>
                            <div class="col-6">
                                <div class="small text-muted">IFSC Code</div>
                                <div class="fw-bold fs-6">${acc.ifscCode}</div>
                            </div>
                            <div class="col-6">
                                <div class="small text-muted">Available Balance</div>
                                <div class="fw-bold fs-5 text-success">₹ <fmt:formatNumber value="${acc.availableBalance}" type="number" minFractionDigits="2"/></div>
                            </div>
                            <div class="col-6">
                                <div class="small text-muted">Ledger Balance</div>
                                <div class="fw-bold fs-5 text-dark">₹ <fmt:formatNumber value="${acc.ledgerBalance}" type="number" minFractionDigits="2"/></div>
                            </div>
                        </div>
                        <div class="d-flex gap-2">
                            <a href="${pageContext.request.contextPath}/customer/account-details?id=${acc.id}" class="btn btn-outline-primary btn-sm flex-fill"><i class="fas fa-eye me-1"></i> Details</a>
                            <a href="${pageContext.request.contextPath}/customer/statement?accountId=${acc.id}" class="btn btn-outline-secondary btn-sm flex-fill"><i class="fas fa-file-invoice me-1"></i> Statement</a>
                            <a href="${pageContext.request.contextPath}/customer/transfer" class="btn btn-bank-primary btn-sm flex-fill"><i class="fas fa-paper-plane me-1"></i> Transfer</a>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</div>

<!-- Modal: Open New Account -->
<div class="modal fade" id="newAccountModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header bg-navy text-white">
                <h5 class="modal-title fw-bold">Open Additional Account</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
            </div>
            <form data-ajax="true" action="${pageContext.request.contextPath}/customer/create-account" method="post">
                <div class="modal-body">
                    <div class="form-alert"></div>
                    <div class="mb-3">
                        <label class="form-label small fw-bold">Select Account Product Type</label>
                        <select name="productId" class="form-select" required>
                            <option value="1">Zero Balance Basic Savings (₹0 Min Bal | ₹0 Maintenance Fee)</option>
                            <option value="2">Regular Savings Account (₹1,000 Min Bal | ₹50/mo Maintenance)</option>
                            <option value="3">Premium Savings Account (₹10,000 Min Bal | ₹150/mo Maintenance)</option>
                            <option value="4">Standard Current Account (Commercial | ₹5,000 Min Bal)</option>
                        </select>
                    </div>
                    <div class="alert alert-light border small text-muted">
                        <i class="fas fa-info-circle text-primary me-1"></i> Account opening is <strong>Zero Initial Balance (₹ 0.00)</strong>. Your balance remains 0 until funds are explicitly deposited or transferred into the account.
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-bank-primary"><i class="fas fa-check me-1"></i> Create Account</button>
                </div>
            </form>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
