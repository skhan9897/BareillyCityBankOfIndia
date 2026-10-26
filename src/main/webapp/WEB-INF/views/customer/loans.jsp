<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="d-flex min-vh-100">
    <jsp:include page="/WEB-INF/views/common/customer-sidebar.jsp" />

    <div class="flex-grow-1 p-4 bg-light">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h3 class="fw-bold text-navy mb-0">Loan Management & EMI Tracker</h3>
                <small class="text-muted">Apply for loans and track outstanding repayment balances</small>
            </div>
            <button class="btn btn-bank-gold" data-bs-toggle="modal" data-bs-target="#applyLoanModal"><i class="fas fa-hand-holding-usd me-1"></i> Apply for Loan</button>
        </div>

        <div class="row g-4 mb-4">
            <div class="col-lg-6">
                <div class="card card-bank p-4 h-100">
                    <h5 class="fw-bold text-navy mb-3"><i class="fas fa-file-signature text-warning me-2"></i>My Active Loans</h5>
                    <c:forEach var="l" items="${loans}">
                        <div class="p-3 border rounded mb-3 bg-white">
                            <div class="d-flex justify-content-between align-items-center mb-2">
                                <span class="fw-bold text-navy">${l.loanAccountNumber}</span>
                                <span class="badge badge-status-active">${l.status}</span>
                            </div>
                            <div class="row g-2 small mb-2">
                                <div class="col-6">Sanctioned: <strong>₹ ${l.sanctionedAmount}</strong></div>
                                <div class="col-6">Outstanding: <strong class="text-danger">₹ ${l.outstandingPrincipal}</strong></div>
                                <div class="col-6">Monthly EMI: <strong>₹ ${l.monthlyEmi}</strong></div>
                                <div class="col-6">Interest Rate: <strong>${l.interestRate}% p.a.</strong></div>
                            </div>
                            <button onclick="payEmi(${l.id}, ${l.monthlyEmi})" class="btn btn-sm btn-bank-primary w-100"><i class="fas fa-credit-card me-1"></i> Pay Monthly EMI (₹ ${l.monthlyEmi})</button>
                        </div>
                    </c:forEach>
                    <c:if test="${empty loans}">
                        <div class="text-muted small py-3 text-center">No active loans found.</div>
                    </c:if>
                </div>
            </div>

            <div class="col-lg-6">
                <div class="card card-bank p-4 h-100">
                    <h5 class="fw-bold text-navy mb-3"><i class="fas fa-tasks text-primary me-2"></i>Application Status</h5>
                    <div class="table-responsive">
                        <table class="table table-hover table-bank mb-0">
                            <thead>
                                <tr>
                                    <th>Application Ref</th>
                                    <th>Amount</th>
                                    <th>Status</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="app" items="${applications}">
                                    <tr>
                                        <td class="fw-bold text-navy small">${app.applicationRef}</td>
                                        <td>₹ ${app.requestedAmount}</td>
                                        <td><span class="badge ${app.status == 'DISBURSED' ? 'badge-status-active' : 'badge-status-pending'}">${app.status}</span></td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty applications}">
                                    <tr><td colspan="3" class="text-center text-muted py-3">No submitted loan applications.</td></tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Modal: Apply Loan -->
<div class="modal fade" id="applyLoanModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header bg-navy text-white">
                <h5 class="modal-title fw-bold">Apply for Personal / Home Loan</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
            </div>
            <form data-ajax="true" action="${pageContext.request.contextPath}/customer/apply-loan" method="post">
                <div class="modal-body">
                    <div class="form-alert"></div>
                    <div class="mb-3">
                        <label class="form-label small fw-bold">Loan Product</label>
                        <select name="productId" class="form-select" required>
                            <option value="1">Personal Loan (10.50% p.a.)</option>
                            <option value="2">Home Loan (8.40% p.a.)</option>
                            <option value="3">Education Loan (7.90% p.a.)</option>
                            <option value="4">Vehicle Loan (8.90% p.a.)</option>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label class="form-label small fw-bold">Requested Loan Amount (₹)</label>
                        <input type="number" name="amount" class="form-control" required min="10000" value="200000">
                    </div>
                    <div class="mb-3">
                        <label class="form-label small fw-bold">Tenure (Months)</label>
                        <input type="number" name="tenureMonths" class="form-control" required min="6" max="240" value="24">
                    </div>
                    <div class="mb-3">
                        <label class="form-label small fw-bold">Monthly Income (₹)</label>
                        <input type="number" name="monthlyIncome" class="form-control" required min="15000" value="75000">
                    </div>
                    <div class="mb-3">
                        <label class="form-label small fw-bold">Purpose of Loan</label>
                        <input type="text" name="purpose" class="form-control" placeholder="Home renovation, Medical, Education...">
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-bank-gold"><i class="fas fa-paper-plane me-1"></i> Submit Application</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
function payEmi(loanId, amount) {
    if (confirm("Confirm paying monthly EMI of ₹ " + amount + " from your savings account?")) {
        triggerQuickActionLoading(
            "Processing EMI Repayment",
            [
                "Validating linked savings account available balance...",
                "Debiting ₹ " + amount + " from savings account...",
                "Posting double-entry general ledger entry...",
                "Updating loan outstanding principal balance..."
            ],
            "${pageContext.request.contextPath}/customer/repay-emi",
            'loanId=' + loanId + '&amount=' + amount
        );
    }
}
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
