<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="container my-5">
    <div class="row justify-content-center">
        <div class="col-md-7 col-lg-5">
            <div class="card card-bank p-4 p-md-5 shadow-lg border-2 border-warning" style="background: linear-gradient(145deg, #0A2540 0%, #083B6F 100%); color: white;">
                <div class="text-center mb-4">
                    <img src="${pageContext.request.contextPath}/assets/images/logo.svg" alt="Bareilly Bank Logo" height="80" width="80" class="mb-3 rounded-circle border border-warning shadow">
                    <h4 class="fw-bold text-white mb-1">Bank Staff & Admin Portal</h4>
                    <span class="badge bg-warning text-dark fw-bold px-3 py-2 mt-2"><i class="fas fa-user-shield me-1"></i> RESTRICTED ACCESS - STAFF ONLY</span>
                </div>

                <form id="adminLoginForm" data-ajax="true" data-loading-title="Authenticating Staff Portal Access" action="${pageContext.request.contextPath}/admin-login" method="post" autocomplete="off">
                    <input type="hidden" name="csrfToken" value="${csrfToken}">
                    <div class="form-alert"></div>

                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger mb-3"><i class="fas fa-exclamation-triangle me-2"></i>${errorMessage}</div>
                    </c:if>

                    <div class="mb-3">
                        <label class="form-label small fw-bold text-warning">Employee Username / Staff ID</label>
                        <div class="input-group">
                            <span class="input-group-text bg-dark text-warning border-secondary"><i class="fas fa-user-shield"></i></span>
                            <input type="text" name="username" class="form-control bg-dark text-white border-secondary" required placeholder="Enter Employee Username" value="" autocomplete="off">
                        </div>
                    </div>

                    <div class="mb-4">
                        <label class="form-label small fw-bold text-warning">Employee Access Password</label>
                        <div class="input-group">
                            <span class="input-group-text bg-dark text-warning border-secondary"><i class="fas fa-key"></i></span>
                            <input type="password" name="password" class="form-control bg-dark text-white border-secondary" required placeholder="Enter Password" value="" autocomplete="new-password">
                        </div>
                    </div>

                    <div class="alert alert-dark border border-secondary small text-white-50 mb-4">
                        <i class="fas fa-shield-alt text-warning me-1"></i> <strong>Security Notice:</strong> This terminal is protected with 256-bit SSL encryption & BCrypt hash security. All access attempts are monitored and logged.
                    </div>

                    <button type="submit" class="btn btn-bank-gold w-100 py-2.5 fw-bold shadow"><i class="fas fa-sign-in-alt me-2"></i> Staff Portal Login</button>
                </form>

                <div class="text-center mt-4 border-top border-secondary pt-3">
                    <a href="${pageContext.request.contextPath}/" class="small text-white-50 text-decoration-none"><i class="fas fa-arrow-left me-1"></i> Return to Main Website</a>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
