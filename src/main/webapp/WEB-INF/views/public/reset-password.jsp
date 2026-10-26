<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="container my-5">
    <div class="row justify-content-center">
        <div class="col-lg-5 col-md-8">
            <div class="card card-bank p-4 p-md-5 shadow-lg">
                <div class="text-center mb-4">
                    <div class="stat-icon stat-icon-green mx-auto mb-2"><i class="fas fa-shield-alt"></i></div>
                    <h3 class="fw-bold text-navy mb-1">Reset Password</h3>
                    <p class="small text-muted">Enter the OTP sent to your email along with your new password</p>
                </div>

                <form data-ajax="true" action="${pageContext.request.contextPath}/reset-password" method="post">
                    <input type="hidden" name="csrfToken" value="${csrfToken}">
                    <div class="form-alert"></div>

                    <c:if test="${not empty successMessage}">
                        <div class="alert alert-info small mb-3"><i class="fas fa-info-circle me-1"></i> ${successMessage}</div>
                    </c:if>

                    <div class="mb-3">
                        <label class="form-label small fw-bold">Email Address</label>
                        <input type="email" name="email" class="form-control" value="${email}" required readonly>
                    </div>

                    <div class="mb-3">
                        <label class="form-label small fw-bold">6-Digit OTP</label>
                        <input type="text" name="otp" class="form-control" value="${otpDemo}" required placeholder="123456">
                    </div>

                    <div class="mb-3">
                        <label class="form-label small fw-bold">New Password</label>
                        <input type="password" name="newPassword" class="form-control" required placeholder="NewStrongPass@123">
                    </div>

                    <button type="submit" class="btn btn-bank-primary w-100 py-2 mt-2"><i class="fas fa-check-circle me-2"></i> Update Password</button>
                </form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
