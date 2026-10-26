<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="d-flex min-vh-100">
    <jsp:include page="/WEB-INF/views/common/admin-sidebar.jsp" />

    <div class="flex-grow-1 p-4 bg-light">
        <h3 class="fw-bold text-navy mb-4">Immutable Administrative Audit Logs</h3>

        <div class="card card-bank p-4">
            <div class="table-responsive">
                <table class="table table-hover table-bank mb-0">
                    <thead>
                        <tr>
                            <th>Action Type</th>
                            <th>Entity</th>
                            <th>Entity ID</th>
                            <th>Details</th>
                            <th>User ID</th>
                            <th>IP Address</th>
                            <th>Timestamp</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="a" items="${logs}">
                            <tr>
                                <td class="fw-bold text-navy"><span class="badge bg-light text-dark border">${a.actionType}</span></td>
                                <td>${a.entityName}</td>
                                <td>${a.entityId}</td>
                                <td class="small text-muted">${a.details}</td>
                                <td>${a.userId}</td>
                                <td class="small text-muted">${a.ipAddress}</td>
                                <td class="small text-muted">${a.createdAt}</td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty logs}">
                            <tr><td colspan="7" class="text-center text-muted py-4">No audit log entries recorded.</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
