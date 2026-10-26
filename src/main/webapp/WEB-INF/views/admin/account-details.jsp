<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="d-flex min-vh-100">
    <jsp:include page="/WEB-INF/views/common/admin-sidebar.jsp" />

    <div class="flex-grow-1 p-4 bg-light">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <a href="${pageContext.request.contextPath}/admin/accounts" class="small text-decoration-none text-muted mb-1 d-inline-block"><i class="fas fa-arrow-left me-1"></i> Back to Accounts</a>
                <h3 class="fw-bold text-navy mb-0">Account Control & Inspection: ${account.accountNumber}</h3>
                <small class="text-muted">Customer Name: ${owner.fullName} | Customer Number: ${owner.customerNumber}</small>
            </div>
            <div class="d-flex align-items-center gap-2">
                <span class="badge ${account.status == 'ACTIVE' ? 'badge-status-active' : 'badge-status-frozen'} px-3 py-2 fs-6 shadow-sm">Status: ${account.status}</span>
                <span class="badge bg-warning text-dark px-3 py-2 fs-6 shadow-sm"><i class="fas fa-user-shield me-1"></i> Admin Override Level-2</span>
            </div>
        </div>

        <!-- Section 1: Financial Balances & Controls -->
        <div class="card card-bank p-4 mb-4 border-0 shadow-sm">
            <div class="d-flex justify-content-between align-items-center mb-3 border-bottom pb-2">
                <h5 class="fw-bold text-navy mb-0"><i class="fas fa-landmark text-primary me-2"></i>Account Specification & Ledger Balances</h5>
                <div class="d-flex gap-2">
                    <button class="btn btn-sm btn-bank-gold fw-bold" data-bs-toggle="modal" data-bs-target="#changeProductModal"><i class="fas fa-cogs me-1"></i> Change Product</button>
                    <button class="btn btn-sm ${account.status == 'ACTIVE' ? 'btn-warning text-dark' : 'btn-success'} fw-bold" data-bs-toggle="modal" data-bs-target="#statusModal"><i class="fas ${account.status == 'ACTIVE' ? 'fa-lock' : 'fa-unlock'} me-1"></i> Toggle Status</button>
                </div>
            </div>

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
                    <div class="small text-muted mb-1">Branch Name</div>
                    <div class="fw-bold text-navy fs-6">${account.branchName}</div>
                </div>
                <div class="col-md-3 col-6">
                    <div class="small text-muted mb-1">IFSC Code</div>
                    <div class="fw-bold text-navy fs-6">${account.ifscCode}</div>
                </div>
                <div class="col-md-3 col-6">
                    <div class="small text-muted mb-1">Account Currency</div>
                    <div class="fw-bold text-navy fs-6">${account.currency}</div>
                </div>
                <div class="col-md-3 col-6">
                    <div class="small text-muted mb-1">Account Opened Date</div>
                    <div class="fw-bold text-navy fs-6">${account.openedAt}</div>
                </div>
            </div>
        </div>

        <!-- Section 2: Customer Personal & Identity Profile -->
        <div class="card card-bank p-4 mb-4 border-0 shadow-sm">
            <h5 class="fw-bold text-navy mb-3 border-bottom pb-2"><i class="fas fa-user-check text-warning me-2"></i>Full Customer Profile Details</h5>
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
                    <div class="small text-muted mb-1">Email Address</div>
                    <div class="fw-bold text-navy">${owner.email}</div>
                </div>
                <div class="col-md-3 col-6">
                    <div class="small text-muted mb-1">Mobile Number</div>
                    <div class="fw-bold text-navy">+91 ${owner.mobile}</div>
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
                    <div class="small text-muted mb-1">Aadhaar (Masked)</div>
                    <div class="fw-bold text-navy">${owner.aadhaarMasked}</div>
                </div>
                <div class="col-md-3 col-6">
                    <div class="small text-muted mb-1">PAN (Masked)</div>
                    <div class="fw-bold text-navy">${owner.panMasked}</div>
                </div>

                <div class="col-12">
                    <div class="small text-muted mb-1">Residential Address</div>
                    <div class="fw-bold text-navy">${owner.addressLine1}, ${owner.city}, ${owner.state} - ${owner.pincode}</div>
                </div>
            </div>
        </div>

        <!-- Section 3: Recent Transactions -->
        <div class="card card-bank p-4 border-0 shadow-sm">
            <h5 class="fw-bold text-navy mb-3 border-bottom pb-2"><i class="fas fa-history text-primary me-2"></i>Account Posted Ledger Transactions</h5>
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
                            <tr><td colspan="6" class="text-center text-muted py-3">No transactions found for this account.</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<!-- Modal 1: Change Product Type -->
<div class="modal fade" id="changeProductModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header bg-navy text-white">
                <h5 class="modal-title fw-bold"><i class="fas fa-cogs me-2 text-warning"></i>Change Account Product Type</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
            </div>
            <form data-ajax="true" action="${pageContext.request.contextPath}/admin/account-product" method="post">
                <input type="hidden" name="accountId" value="${account.id}">
                <div class="modal-body">
                    <div class="form-alert"></div>
                    <div class="mb-3">
                        <label class="form-label small fw-bold">Select New Product Category</label>
                        <select name="productId" class="form-select" required>
                            <option value="1" ${account.productId == 1 ? 'selected' : ''}>Zero Balance Basic Savings (BSBD)</option>
                            <option value="2" ${account.productId == 2 ? 'selected' : ''}>Regular Savings Account</option>
                            <option value="3" ${account.productId == 3 ? 'selected' : ''}>Premium Savings Account</option>
                            <option value="4" ${account.productId == 4 ? 'selected' : ''}>Standard Current Account</option>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label class="form-label small fw-bold">Authorization Reason / Override Remarks</label>
                        <input type="text" name="reason" class="form-control" required placeholder="Product upgrade requested by customer">
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-bank-gold"><i class="fas fa-check-circle me-1"></i> Update Product Category</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Modal 2: Change Account Status -->
<div class="modal fade" id="statusModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header bg-navy text-white">
                <h5 class="modal-title fw-bold"><i class="fas fa-shield-alt me-2 text-warning"></i>Account Authorization Control</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
            </div>
            <form data-ajax="true" action="${pageContext.request.contextPath}/admin/account-status" method="post">
                <input type="hidden" name="accountId" value="${account.id}">
                <div class="modal-body">
                    <div class="form-alert"></div>
                    <div class="mb-3">
                        <label class="form-label small fw-bold">Target Account Status</label>
                        <select name="status" class="form-select" required>
                            <option value="ACTIVE" ${account.status == 'ACTIVE' ? 'selected' : ''}>ACTIVE - Full Operational</option>
                            <option value="FROZEN" ${account.status == 'FROZEN' ? 'selected' : ''}>FROZEN - Restrict Debits & Transfers</option>
                            <option value="DORMANT" ${account.status == 'DORMANT' ? 'selected' : ''}>DORMANT - Inactivity Restrict</option>
                            <option value="CLOSED" ${account.status == 'CLOSED' ? 'selected' : ''}>CLOSED - Account Permanent Close</option>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label class="form-label small fw-bold">Authorization Reason / Audit Notes</label>
                        <input type="text" name="reason" class="form-control" required placeholder="Manager status override">
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-bank-gold"><i class="fas fa-check-circle me-1"></i> Apply Authorization Status</button>
                </div>
            </form>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
