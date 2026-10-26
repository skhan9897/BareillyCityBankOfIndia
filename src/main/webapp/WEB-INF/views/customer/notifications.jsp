<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="d-flex min-vh-100">
    <jsp:include page="/WEB-INF/views/common/customer-sidebar.jsp" />

    <div class="flex-grow-1 p-4 bg-light">
        <!-- Header -->
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h3 class="fw-bold text-navy mb-0">Transaction Alerts & SMS Inbox</h3>
                <small class="text-muted">Real-time payment credit and debit notification alerts</small>
            </div>
            <span class="badge bg-success px-3 py-2 fs-6 shadow-sm"><i class="fas fa-bell me-1"></i> SMS Alerts: Active</span>
        </div>

        <!-- Notification List -->
        <div class="card card-bank p-4 border-0 shadow-sm">
            <h5 class="fw-bold text-navy mb-3 border-bottom pb-2"><i class="fas fa-inbox text-warning me-2"></i>Inbox Alerts (${notifications.size()})</h5>

            <div class="list-group list-group-flush">
                <c:forEach var="n" items="${notifications}">
                    <div class="list-group-item p-3 border-bottom bg-white rounded my-1 shadow-sm">
                        <div class="d-flex justify-content-between align-items-start mb-2">
                            <div class="d-flex align-items-center gap-2">
                                <span class="stat-icon ${n.type == 'SUCCESS' ? 'stat-icon-green' : 'stat-icon-blue'} d-inline-flex align-items-center justify-content-center" style="width: 36px; height: 36px;">
                                    <i class="fas ${n.type == 'SUCCESS' ? 'fa-arrow-down' : 'fa-arrow-up'}"></i>
                                </span>
                                <h6 class="fw-bold text-navy mb-0">${n.title}</h6>
                            </div>
                            <small class="text-muted"><i class="fas fa-clock me-1"></i> ${n.createdAt}</small>
                        </div>
                        <p class="mb-1 text-dark small bg-light p-2.5 rounded border" style="font-family: monospace;">
                            <i class="fas fa-comment-dots text-primary me-2"></i>${n.message}
                        </p>
                    </div>
                </c:forEach>
                <c:if test="${empty notifications}">
                    <div class="text-center py-5 text-muted">
                        <i class="fas fa-bell-slash fa-3x mb-3 text-secondary d-block"></i>
                        No transaction alert messages received yet.
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
