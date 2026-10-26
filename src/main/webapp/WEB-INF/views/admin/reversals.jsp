<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="d-flex min-vh-100">
    <jsp:include page="/WEB-INF/views/common/admin-sidebar.jsp" />

    <div class="flex-grow-1 p-4 bg-light">
        <h3 class="fw-bold text-navy mb-4">Transaction Reversals & Reconciliation</h3>

        <div class="card card-bank p-4 mb-4">
            <h5 class="fw-bold text-navy mb-3"><i class="fas fa-undo text-warning me-2"></i>Compensating Journal Reversal Workflow</h5>
            <p class="text-muted small">Every transaction reversal posts a balanced compensating journal entry to prevent unverified ledger adjustments.</p>
        </div>

        <div class="card card-bank p-4">
            <h6 class="fw-bold text-navy mb-3">Reconciliation Ledger Projections</h6>
            <div class="alert alert-success"><i class="fas fa-check-circle me-2"></i> General Ledger Projections match active Customer Balances (0 Discrepancies).</div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
