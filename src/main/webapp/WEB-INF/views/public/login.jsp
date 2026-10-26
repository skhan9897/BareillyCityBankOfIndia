<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="container my-5">
    <div class="row justify-content-center">
        <div class="col-lg-5 col-md-8">
            <div class="card card-bank p-4 p-md-5 shadow-lg border-0">
                <div class="text-center mb-4">
                    <div class="stat-icon stat-icon-gold mx-auto mb-2"><i class="fas fa-lock"></i></div>
                    <h3 class="fw-bold text-navy mb-1">Customer Internet Banking</h3>
                    <p class="small text-muted">Secure Customer Portal - BAREILLY CITY BANK OF INDIA</p>
                </div>

                <form data-ajax="true" action="${pageContext.request.contextPath}/login" method="post" autocomplete="off">
                    <input type="hidden" name="csrfToken" value="${csrfToken}">
                    <div class="form-alert"></div>

                    <c:if test="${not empty param.error}">
                        <div class="alert alert-danger small"><i class="fas fa-exclamation-triangle me-1"></i> Session expired or invalid session. Please login.</div>
                    </c:if>
                    <c:if test="${not empty param.message}">
                        <div class="alert alert-success small"><i class="fas fa-check-circle me-1"></i> You have logged out successfully.</div>
                    </c:if>

                    <div class="mb-3">
                        <label class="form-label small fw-bold text-navy"><i class="fas fa-user me-1 text-primary"></i> Customer Username or User ID</label>
                        <input type="text" name="username" class="form-control" required placeholder="Enter Customer Username" value="" autocomplete="username">
                    </div>

                    <div class="mb-3">
                        <div class="d-flex justify-content-between align-items-center mb-1">
                            <label class="form-label small fw-bold text-navy mb-0"><i class="fas fa-key me-1 text-warning"></i> Password</label>
                            <a href="${pageContext.request.contextPath}/forgot-password" class="small text-decoration-none">Forgot Password?</a>
                        </div>
                        <input type="password" name="password" class="form-control" required placeholder="Enter Password" value="" autocomplete="current-password">
                    </div>

                    <button type="submit" class="btn btn-bank-primary w-100 py-2 mt-2"><i class="fas fa-sign-in-alt me-2"></i> Login to Customer Portal</button>
                </form>

                <div class="border-top pt-3 mt-4 text-center">
                    <p class="small text-muted mb-1">New to Bareilly City Bank?</p>
                    <a href="${pageContext.request.contextPath}/register" class="fw-bold text-decoration-none text-navy"><i class="fas fa-user-plus me-1 text-warning"></i> Register for Internet Banking</a>
                </div>

                <div class="border-top pt-3 mt-3 text-center">
                    <a href="${pageContext.request.contextPath}/admin-login" class="text-navy text-decoration-none fw-bold small"><i class="fas fa-user-shield me-1 text-warning"></i> Bank Staff / Employee Portal Login &rarr;</a>
                </div>

                <div class="mt-4 p-3 bg-light rounded small border">
                    <div class="fw-bold text-navy mb-1"><i class="fas fa-shield-alt text-primary me-1"></i> Security Protection:</div>
                    <p class="text-muted mb-0" style="font-size: 0.82rem;">
                        Your credentials are protected with 256-bit SSL encryption and BCrypt password hashing. Never share your password or OTP with anyone.
                    </p>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
