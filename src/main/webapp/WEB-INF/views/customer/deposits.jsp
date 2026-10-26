<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="d-flex min-vh-100">
    <jsp:include page="/WEB-INF/views/common/customer-sidebar.jsp" />

    <div class="flex-grow-1 p-4 bg-light">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h3 class="fw-bold text-navy mb-0">Fixed & Recurring Deposits</h3>
                <small class="text-muted">High-yield guaranteed investment deposits</small>
            </div>
            <div>
                <button class="btn btn-bank-gold" data-bs-toggle="modal" data-bs-target="#newFdModal"><i class="fas fa-plus me-1"></i> Open FD</button>
                <button class="btn btn-bank-primary" data-bs-toggle="modal" data-bs-target="#newRdModal"><i class="fas fa-plus me-1"></i> Open RD</button>
            </div>
        </div>

        <div class="row g-4">
            <div class="col-lg-6">
                <div class="card card-bank p-4">
                    <h5 class="fw-bold text-navy mb-3"><i class="fas fa-lock text-warning me-2"></i>Fixed Deposits (FD)</h5>
                    <c:forEach var="fd" items="${fds}">
                        <div class="p-3 border rounded mb-3 bg-white">
                            <div class="d-flex justify-content-between align-items-center mb-2">
                                <span class="fw-bold text-navy">${fd.fdNumber}</span>
                                <span class="badge ${fd.status == 'ACTIVE' ? 'badge-status-active' : 'badge-status-frozen'}">${fd.status}</span>
                            </div>
                            <div class="row g-2 small">
                                <div class="col-6">Principal: <strong>₹ ${fd.principalAmount}</strong></div>
                                <div class="col-6">Interest Rate: <strong>${fd.interestRate}% p.a.</strong></div>
                                <div class="col-6">Maturity Amt: <strong class="text-success">₹ ${fd.maturityAmount}</strong></div>
                                <div class="col-6">Tenure: <strong>${fd.tenureMonths} Months</strong></div>
                            </div>
                            <c:if test="${fd.status == 'ACTIVE'}">
                                <button onclick="closeFd(${fd.id})" class="btn btn-sm btn-outline-danger w-100 mt-2"><i class="fas fa-times me-1"></i> Premature Close</button>
                            </c:if>
                        </div>
                    </c:forEach>
                    <c:if test="${empty fds}">
                        <div class="text-muted small py-3 text-center">No active Fixed Deposits.</div>
                    </c:if>
                </div>
            </div>

            <div class="col-lg-6">
                <div class="card card-bank p-4">
                    <h5 class="fw-bold text-navy mb-3"><i class="fas fa-redo text-primary me-2"></i>Recurring Deposits (RD)</h5>
                    <c:forEach var="rd" items="${rds}">
                        <div class="p-3 border rounded mb-3 bg-white">
                            <div class="d-flex justify-content-between align-items-center mb-2">
                                <span class="fw-bold text-navy">${rd.rdNumber}</span>
                                <span class="badge badge-status-active">${rd.status}</span>
                            </div>
                            <div class="row g-2 small">
                                <div class="col-6">Monthly Installment: <strong>₹ ${rd.monthlyInstallment}</strong></div>
                                <div class="col-6">Installments Paid: <strong>${rd.installmentsPaid}/${rd.tenureMonths}</strong></div>
                                <div class="col-6">Expected Maturity: <strong class="text-success">₹ ${rd.maturityAmount}</strong></div>
                                <div class="col-6">Interest Rate: <strong>${rd.interestRate}% p.a.</strong></div>
                            </div>
                        </div>
                    </c:forEach>
                    <c:if test="${empty rds}">
                        <div class="text-muted small py-3 text-center">No active Recurring Deposits.</div>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Modal: Open FD -->
<div class="modal fade" id="newFdModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header bg-navy text-white">
                <h5 class="modal-title fw-bold">Open Fixed Deposit</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
            </div>
            <form data-ajax="true" action="${pageContext.request.contextPath}/customer/create-fd" method="post">
                <div class="modal-body">
                    <div class="form-alert"></div>
                    <div class="mb-3">
                        <label class="form-label small fw-bold">Source Account</label>
                        <select name="accountId" class="form-select" required>
                            <c:forEach var="a" items="${accounts}">
                                <option value="${a.id}">${a.accountNumber} (Balance: ₹ ${a.availableBalance})</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label class="form-label small fw-bold">Principal Deposit Amount (₹)</label>
                        <input type="number" name="principalAmount" class="form-control" required min="5000" value="50000">
                    </div>
                    <div class="mb-3">
                        <label class="form-label small fw-bold">Tenure (Months)</label>
                        <select name="tenureMonths" class="form-select" required>
                            <option value="12">12 Months (7.10% p.a.)</option>
                            <option value="24">24 Months (7.50% p.a.)</option>
                            <option value="36">36 Months (7.50% p.a.)</option>
                        </select>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-bank-gold"><i class="fas fa-check me-1"></i> Open Fixed Deposit</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Modal: Open RD -->
<div class="modal fade" id="newRdModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header bg-navy text-white">
                <h5 class="modal-title fw-bold">Open Recurring Deposit</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
            </div>
            <form data-ajax="true" action="${pageContext.request.contextPath}/customer/create-rd" method="post">
                <div class="modal-body">
                    <div class="form-alert"></div>
                    <div class="mb-3">
                        <label class="form-label small fw-bold">Source Account</label>
                        <select name="accountId" class="form-select" required>
                            <c:forEach var="a" items="${accounts}">
                                <option value="${a.id}">${a.accountNumber} (Balance: ₹ ${a.availableBalance})</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label class="form-label small fw-bold">Monthly Installment Amount (₹)</label>
                        <input type="number" name="monthlyInstallment" class="form-control" required min="500" value="2000">
                    </div>
                    <div class="mb-3">
                        <label class="form-label small fw-bold">Tenure (Months)</label>
                        <select name="tenureMonths" class="form-select" required>
                            <option value="12">12 Months (6.80% p.a.)</option>
                            <option value="24">24 Months (7.10% p.a.)</option>
                        </select>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-bank-primary"><i class="fas fa-check me-1"></i> Open Recurring Deposit</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
function closeFd(id) {
    if (confirm("Are you sure you want to prematurely close this FD? Standard 1% penalty will apply.")) {
        triggerQuickActionLoading(
            "Closing Fixed Deposit Prematurely",
            [
                "Calculating premature interest & penalty rate...",
                "Refunding principal + accrued interest to savings account...",
                "Updating Fixed Deposit status to Premature Closed..."
            ],
            "${pageContext.request.contextPath}/customer/close-fd",
            'fdId=' + id
        );
    }
}
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
