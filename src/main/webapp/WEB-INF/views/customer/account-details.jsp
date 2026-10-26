<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="d-flex min-vh-100">
    <jsp:include page="/WEB-INF/views/common/customer-sidebar.jsp" />

    <div class="flex-grow-1 p-4 bg-light">
        <!-- Top Header & Breadcrumb -->
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <a href="${pageContext.request.contextPath}/customer/accounts" class="small text-decoration-none text-muted mb-1 d-inline-block"><i class="fas fa-arrow-left me-1"></i> Back to Accounts</a>
                <h3 class="fw-bold text-navy mb-0">Account Details: ${account.accountNumber}</h3>
                <small class="text-muted">Customer Name: ${owner.fullName} | Customer ID: ${owner.customerNumber}</small>
            </div>
            <div class="d-flex align-items-center gap-2">
                <span class="badge ${account.status == 'ACTIVE' ? 'badge-status-active' : 'badge-status-frozen'} px-3 py-2 fs-6"><i class="fas fa-shield-alt me-1"></i> Account Status: ${account.status}</span>
                <span class="badge bg-primary px-3 py-2 fs-6"><i class="fas fa-id-card me-1"></i> KYC: ${owner.kycStatus}</span>
            </div>
        </div>

        <!-- Section 1: Financial Balances & Account Specification -->
        <div class="card card-bank p-4 mb-4">
            <h5 class="fw-bold text-navy mb-3 border-bottom pb-2"><i class="fas fa-wallet text-primary me-2"></i>Account Specification & Balances</h5>
            <div class="row g-4">
                <div class="col-md-3 col-6">
                    <div class="small text-muted mb-1">Product Name</div>
                    <div class="fw-bold text-navy fs-6">${account.productName}</div>
                </div>
                <div class="col-md-3 col-6">
                    <div class="small text-muted mb-1">Account Type</div>
                    <div class="fw-bold text-navy fs-6"><span class="badge bg-light text-navy border">${account.accountType}</span></div>
                </div>
                <div class="col-md-3 col-6">
                    <div class="small text-muted mb-1">Available Balance</div>
                    <div class="fw-bold text-success fs-4">₹ <fmt:formatNumber value="${account.availableBalance}" type="number" minFractionDigits="2"/></div>
                </div>
                <div class="col-md-3 col-6">
                    <div class="small text-muted mb-1">Ledger Balance</div>
                    <div class="fw-bold text-dark fs-5">₹ <fmt:formatNumber value="${account.ledgerBalance}" type="number" minFractionDigits="2"/></div>
                </div>

                <div class="col-md-3 col-6">
                    <div class="small text-muted mb-1">Home Branch</div>
                    <div class="fw-bold text-navy fs-6">${account.branchName}</div>
                </div>
                <div class="col-md-3 col-6">
                    <div class="small text-muted mb-1">IFSC Code</div>
                    <div class="fw-bold text-navy fs-6">${account.ifscCode}</div>
                </div>
                <div class="col-md-3 col-6">
                    <div class="small text-muted mb-1">Minimum Balance Req.</div>
                    <div class="fw-bold text-navy fs-6">₹ 1,000.00</div>
                </div>
                <div class="col-md-3 col-6">
                    <div class="small text-muted mb-1">Daily Transfer Limit</div>
                    <div class="fw-bold text-navy fs-6">₹ 1,00,000.00</div>
                </div>
            </div>
        </div>

        <!-- Section 2: Full Customer Personal & Contact Profile -->
        <div class="card card-bank p-4 mb-4">
            <h5 class="fw-bold text-navy mb-3 border-bottom pb-2"><i class="fas fa-user-check text-warning me-2"></i>Full Customer Profile & Contact Details</h5>
            <div class="row g-4">
                <div class="col-md-3 col-6">
                    <div class="small text-muted mb-1">Full Name</div>
                    <div class="fw-bold text-navy">${owner.fullName}</div>
                </div>
                <div class="col-md-3 col-6">
                    <div class="small text-muted mb-1">Customer Number</div>
                    <div class="fw-bold text-navy">${owner.customerNumber}</div>
                </div>
                <div class="col-md-3 col-6">
                    <div class="small text-muted mb-1">Registered Email</div>
                    <div class="fw-bold text-navy">${owner.email} <span class="badge bg-success-subtle text-success ms-1"><i class="fas fa-check-circle"></i> Verified</span></div>
                </div>
                <div class="col-md-3 col-6">
                    <div class="small text-muted mb-1">Mobile Number</div>
                    <div class="fw-bold text-navy">+91 ${owner.mobile} <span class="badge bg-success-subtle text-success ms-1"><i class="fas fa-check-circle"></i> Verified</span></div>
                </div>

                <div class="col-md-3 col-6">
                    <div class="small text-muted mb-1">Date of Birth</div>
                    <div class="fw-bold text-navy">${owner.dob}</div>
                </div>
                <div class="col-md-3 col-6">
                    <div class="small text-muted mb-1">Gender</div>
                    <div class="fw-bold text-navy">${owner.gender}</div>
                </div>
                <div class="col-md-3 col-6">
                    <div class="small text-muted mb-1">Aadhaar Card (Masked)</div>
                    <div class="fw-bold text-navy">${owner.aadhaarMasked}</div>
                </div>
                <div class="col-md-3 col-6">
                    <div class="small text-muted mb-1">PAN Card (Masked)</div>
                    <div class="fw-bold text-navy">${owner.panMasked}</div>
                </div>

                <div class="col-12">
                    <div class="small text-muted mb-1">Registered Residential Address</div>
                    <div class="fw-bold text-navy">${owner.addressLine1}, ${owner.city}, ${owner.state} - ${owner.pincode}</div>
                </div>
            </div>
        </div>

        <!-- Section 3: Linked Debit Card & Digital Services -->
        <div class="row g-4 mb-4">
            <div class="col-lg-6">
                <div class="card card-bank p-4 h-100">
                    <h5 class="fw-bold text-navy mb-3 border-bottom pb-2"><i class="fas fa-credit-card text-success me-2"></i>Linked Virtual Debit Card</h5>
                    <c:choose>
                        <c:when test="${not empty card}">
                            <div class="row g-3">
                                <div class="col-6">
                                    <div class="small text-muted">Card Number (Masked)</div>
                                    <div class="fw-bold text-navy">${card.cardNumberMasked}</div>
                                </div>
                                <div class="col-6">
                                    <div class="small text-muted">Card Status</div>
                                    <span class="badge ${card.status == 'ACTIVE' ? 'badge-status-active' : 'badge-status-frozen'}">${card.status}</span>
                                </div>
                                <div class="col-6">
                                    <div class="small text-muted">Expiry Date</div>
                                    <div class="fw-bold text-navy">${card.expiryMonth}/${card.expiryYear}</div>
                                </div>
                                <div class="col-6">
                                    <div class="small text-muted">Daily Card Limit</div>
                                    <div class="fw-bold text-navy">₹ <fmt:formatNumber value="${card.dailyLimit}" type="number" minFractionDigits="2"/></div>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="text-muted small py-3">No virtual card issued yet for this account.</div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <div class="col-lg-6">
                <div class="card card-bank p-4 h-100">
                    <h5 class="fw-bold text-navy mb-3 border-bottom pb-2"><i class="fas fa-mobile-alt text-info me-2"></i>Digital Banking & UPI</h5>
                    <div class="row g-3">
                        <div class="col-6">
                            <div class="small text-muted">Virtual UPI Handle</div>
                            <div class="fw-bold text-navy">${owner.username}@bcbi</div>
                        </div>
                        <div class="col-6">
                            <div class="small text-muted">Net Banking User ID</div>
                            <div class="fw-bold text-navy">${owner.username}</div>
                        </div>
                        <div class="col-12">
                            <div class="small text-muted">Instant Payments</div>
                            <div class="fw-bold text-success"><i class="fas fa-check-circle me-1"></i> UPI QR & IMPS Active</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Section 4: Recent Transactions Table -->
        <div class="card card-bank p-4 mb-4">
            <h5 class="fw-bold text-navy mb-3 border-bottom pb-2"><i class="fas fa-history text-primary me-2"></i>Recent Transactions for Account ${account.accountNumber}</h5>
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
                            <tr><td colspan="6" class="text-center text-muted py-3">No recent transactions recorded for this account.</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- Section 5: Action Button Toolbar -->
        <div class="card card-bank p-3">
            <div class="d-flex flex-wrap gap-3 justify-content-between align-items-center">
                <div class="d-flex gap-2">
                    <a href="${pageContext.request.contextPath}/customer/statement?accountId=${account.id}" class="btn btn-bank-primary"><i class="fas fa-file-invoice me-1"></i> View Full Statement</a>
                    <a href="${pageContext.request.contextPath}/customer/statement/pdf?accountId=${account.id}" class="btn btn-outline-danger"><i class="fas fa-file-pdf me-1"></i> PDF Download</a>
                    <a href="${pageContext.request.contextPath}/customer/statement/csv?accountId=${account.id}" class="btn btn-outline-success"><i class="fas fa-file-csv me-1"></i> Export CSV</a>
                </div>
                <div>
                    <a href="${pageContext.request.contextPath}/customer/transfer" class="btn btn-bank-gold"><i class="fas fa-paper-plane me-1"></i> Transfer Funds</a>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
