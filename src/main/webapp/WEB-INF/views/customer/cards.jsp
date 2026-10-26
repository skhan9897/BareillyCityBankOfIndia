<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="d-flex min-vh-100">
    <jsp:include page="/WEB-INF/views/common/customer-sidebar.jsp" />

    <div class="flex-grow-1 p-4 bg-light">
        <h3 class="fw-bold text-navy mb-4">Virtual Debit Cards</h3>

        <div class="row g-4">
            <div class="col-lg-5">
                <c:if test="${not empty card}">
                    <div class="debit-card-widget mb-4">
                        <div class="d-flex justify-content-between align-items-center mb-4">
                            <span class="fw-bold fs-6">BAREILLY CITY BANK</span>
                            <span class="badge ${card.status == 'ACTIVE' ? 'bg-success' : 'bg-danger'}">${card.status}</span>
                        </div>
                        <div class="chip-icon mb-3"></div>
                        <div class="fs-4 fw-mono mb-3" style="letter-spacing: 2px;">${card.cardNumberMasked}</div>
                        <div class="d-flex justify-content-between align-items-end text-uppercase small">
                            <div>
                                <div class="text-white-50" style="font-size: 0.65rem;">Card Holder</div>
                                <div class="fw-bold">PRIMARY ACCOUNT HOLDER</div>
                            </div>
                            <div>
                                <div class="text-white-50" style="font-size: 0.65rem;">Expires</div>
                                <div class="fw-bold">${card.expiryMonth}/${card.expiryYear}</div>
                            </div>
                            <i class="fab fa-cc-visa fa-2x text-warning"></i>
                        </div>
                    </div>
                </c:if>
            </div>

            <div class="col-lg-7">
                <div class="card card-bank p-4">
                    <h5 class="fw-bold text-navy mb-3"><i class="fas fa-cogs me-2 text-primary"></i>Card Controls & Limits</h5>

                    <c:if test="${not empty card}">
                        <div class="mb-4">
                            <label class="form-label small fw-bold">Daily Transaction Limit (₹)</label>
                            <div class="input-group">
                                <input type="number" id="limitInput" class="form-control" value="${card.dailyLimit}" min="1000" max="500000">
                                <button onclick="updateLimit(${card.id})" class="btn btn-bank-primary">Update Limit</button>
                            </div>
                        </div>

                        <div class="d-flex gap-3">
                            <c:choose>
                                <c:when test="${card.status == 'ACTIVE'}">
                                    <button onclick="blockCard(${card.id})" class="btn btn-danger flex-fill py-2"><i class="fas fa-ban me-1"></i> Block Card Instantly</button>
                                </c:when>
                                <c:otherwise>
                                    <button onclick="unblockCard(${card.id})" class="btn btn-success flex-fill py-2"><i class="fas fa-check-circle me-1"></i> Unblock Card</button>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
function blockCard(id) {
    if (confirm("Block your virtual debit card?")) {
        triggerQuickActionLoading(
            "Blocking Virtual Debit Card",
            [
                "Communicating with Card Switch Gateway...",
                "Updating card security status to BLOCKED...",
                "Card blocked successfully!"
            ],
            "${pageContext.request.contextPath}/customer/block-card",
            'cardId=' + id
        );
    }
}
function unblockCard(id) {
    triggerQuickActionLoading(
        "Unblocking Virtual Debit Card",
        [
            "Authenticating unblock request...",
            "Updating card security status to ACTIVE...",
            "Card unblocked successfully!"
        ],
        "${pageContext.request.contextPath}/customer/unblock-card",
        'cardId=' + id
    );
}
function updateLimit(id) {
    let limit = document.getElementById('limitInput').value;
    triggerQuickActionLoading(
        "Updating Card Daily Limit",
        [
            "Validating requested daily limit...",
            "Updating card limit to ₹ " + limit + "...",
            "Limit updated successfully!"
        ],
        "${pageContext.request.contextPath}/customer/card-limit",
        'cardId=' + id + '&dailyLimit=' + limit
    );
}
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
