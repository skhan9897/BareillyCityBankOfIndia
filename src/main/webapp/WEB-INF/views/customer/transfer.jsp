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
                        <div class="stat-icon stat-icon-gold me-3"><i class="fas fa-paper-plane"></i></div>
                        <div>
                            <h4 class="fw-bold text-navy mb-0">Fund Transfer</h4>
                            <small class="text-muted">Instant double-entry ledger transfer protected by Idempotency Key</small>
                        </div>
                    </div>

                    <form data-ajax="true" action="${pageContext.request.contextPath}/customer/transfer" method="post">
                        <input type="hidden" name="idempotencyKey" id="idemKey">
                        <div class="form-alert"></div>

                        <div class="mb-3">
                            <label class="form-label small fw-bold">From Account</label>
                            <select name="fromAccountId" class="form-select" required>
                                <c:forEach var="a" items="${accounts}">
                                    <option value="${a.id}">${a.accountNumber} - ${a.accountType} (Balance: ₹ ${a.availableBalance})</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="mb-3">
                            <label class="form-label small fw-bold">Destination Account Number</label>
                            <input type="text" name="toAccountNumber" class="form-control" required placeholder="e.g. 100020003003">
                        </div>

                        <div class="mb-3">
                            <label class="form-label small fw-bold">Transfer Amount (₹)</label>
                            <input type="number" name="amount" step="0.01" min="1" class="form-control" required placeholder="5000">
                        </div>

                        <div class="mb-3">
                            <label class="form-label small fw-bold">Description / Purpose</label>
                            <input type="text" name="description" class="form-control" placeholder="Rent, Fees, Gift, Family Support...">
                        </div>

                        <button type="submit" class="btn btn-bank-gold w-100 py-2 mt-3"><i class="fas fa-paper-plane me-2"></i> Confirm & Transfer Instantly</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
document.getElementById('idemKey').value = 'IDEM-' + Date.now() + '-' + Math.random().toString(36).substring(2, 8);
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
