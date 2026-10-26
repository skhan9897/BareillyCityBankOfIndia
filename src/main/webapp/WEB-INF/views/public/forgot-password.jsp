<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="container my-5">
    <div class="row justify-content-center">
        <div class="col-lg-5 col-md-8">
            <div class="card card-bank p-4 p-md-5 shadow-lg">
                <div class="text-center mb-4">
                    <div class="stat-icon stat-icon-gold mx-auto mb-2"><i class="fas fa-key"></i></div>
                    <h3 class="fw-bold text-navy mb-1">Forgot Password</h3>
                    <p class="small text-muted">Enter your registered email address to receive an OTP</p>
                </div>

                <form data-ajax="false" action="${pageContext.request.contextPath}/forgot-password" method="post">
                    <input type="hidden" name="csrfToken" value="${csrfToken}">

                    <div class="mb-3">
                        <label class="form-label small fw-bold text-navy"><i class="fas fa-envelope me-1 text-primary"></i> Registered Email</label>
                        <input type="email" name="email" class="form-control" required placeholder="rahul.kumar@gmail.com">
                    </div>

                    <button type="submit" class="btn btn-bank-primary w-100 py-2"><i class="fas fa-paper-plane me-2"></i> Send OTP</button>
                </form>

                <div class="text-center mt-4">
                    <a href="${pageContext.request.contextPath}/login" class="small text-decoration-none text-navy fw-bold"><i class="fas fa-arrow-left me-1"></i> Back to Login</a>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
