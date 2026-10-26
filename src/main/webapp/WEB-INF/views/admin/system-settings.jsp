<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="d-flex min-vh-100">
    <jsp:include page="/WEB-INF/views/common/admin-sidebar.jsp" />

    <div class="flex-grow-1 p-4 bg-light">
        <h3 class="fw-bold text-navy mb-4">System Settings & Configurations</h3>

        <div class="card card-bank p-4">
            <div class="table-responsive">
                <table class="table table-hover table-bank mb-0">
                    <thead>
                        <tr>
                            <th>Setting Key</th>
                            <th>Setting Value</th>
                            <th>Description</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="s" items="${settings}">
                            <tr>
                                <td class="fw-bold text-navy">${s.settingKey}</td>
                                <td><input type="text" id="val_${s.settingKey}" class="form-control form-control-sm" value="${s.settingValue}"></td>
                                <td class="small text-muted">${s.description}</td>
                                <td><button onclick="updateSetting('${s.settingKey}')" class="btn btn-sm btn-bank-primary"><i class="fas fa-save me-1"></i> Save</button></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<script>
function updateSetting(key) {
    let val = document.getElementById('val_' + key).value;
    fetch("${pageContext.request.contextPath}/admin/settings", {
        method: 'POST',
        headers: {'Content-Type': 'application/x-www-form-urlencoded', 'X-Requested-With': 'XMLHttpRequest'},
        body: 'key=' + encodeURIComponent(key) + '&value=' + encodeURIComponent(val)
    }).then(r => r.json()).then(d => alert(d.message));
}
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
