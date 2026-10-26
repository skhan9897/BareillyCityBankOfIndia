<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>BAREILLY CITY BANK OF INDIA</title>
    <!-- Favicon / Logo -->
    <link rel="icon" type="image/svg+xml" href="${pageContext.request.contextPath}/assets/images/logo.svg">
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- FontAwesome Icons -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link href="${pageContext.request.contextPath}/assets/css/style.css" rel="stylesheet">
    <!-- Chart.js -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>

<!-- 2 to 3 Seconds Animated App Splash Screen -->
<div class="app-splash-screen" id="appSplashScreen">
    <div class="splash-card text-center">
        <div class="splash-logo-container mb-3">
            <img src="${pageContext.request.contextPath}/assets/images/logo.svg" alt="Bareilly City Bank Emblem Logo" class="splash-logo-img">
        </div>
        <h3 class="fw-bold text-white mb-1" style="letter-spacing: 1px;">BAREILLY CITY BANK OF INDIA</h3>
        <p class="small text-warning fw-bold mb-4" style="letter-spacing: 2px;">YOUR TRUST OUR STRENGTH</p>

        <div class="progress mx-auto mb-3" style="width: 260px; height: 6px; background-color: rgba(255,255,255,0.2);">
            <div class="progress-bar bg-warning progress-bar-striped progress-bar-animated" id="splashProgressBar" style="width: 15%;"></div>
        </div>
        <div class="small text-white-50" id="splashStatusText">Initializing Safe & Secure Banking...</div>
    </div>
</div>

<script>
// Handle 2.5-second Splash Screen Loader
document.addEventListener('DOMContentLoaded', function () {
    const splash = document.getElementById('appSplashScreen');
    const pBar = document.getElementById('splashProgressBar');
    const statusText = document.getElementById('splashStatusText');

    if (splash) {
        setTimeout(() => {
            if (pBar) pBar.style.width = '45%';
            if (statusText) statusText.innerText = 'Connecting to Core Ledger Server...';
        }, 600);

        setTimeout(() => {
            if (pBar) pBar.style.width = '80%';
            if (statusText) statusText.innerText = 'Verifying Security Tokens & Encrypted Channel...';
        }, 1300);

        setTimeout(() => {
            if (pBar) pBar.style.width = '100%';
            if (statusText) statusText.innerText = 'Welcome to Bareilly City Bank!';
        }, 2000);

        setTimeout(() => {
            splash.style.opacity = '0';
            splash.style.visibility = 'hidden';
            setTimeout(() => { splash.style.display = 'none'; }, 500);
        }, 2500);
    }
});
</script>

<nav class="navbar navbar-expand-lg bank-navbar navbar-dark sticky-top">
    <div class="container-fluid px-4">
        <a class="navbar-brand d-flex align-items-center" href="${pageContext.request.contextPath}/">
            <img src="${pageContext.request.contextPath}/assets/images/logo.svg" alt="Bareilly Bank Emblem Logo" height="48" width="48" class="me-2 rounded-circle border border-warning shadow-sm">
            <span class="bank-logo-text">BAREILLY CITY <span>BANK OF INDIA</span></span>
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#bankNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="bankNav">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0 ms-lg-4">
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/">Home</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/about">About Us</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/services">Services</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/faqs">FAQs</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/contact">Contact</a></li>
            </ul>
            <div class="d-flex align-items-center gap-2">
                <c:choose>
                    <c:when test="${not empty sessionScope.LOGGED_IN_USER}">
                        <c:choose>
                            <c:when test="${sessionScope.LOGGED_IN_USER.userType == 'EMPLOYEE'}">
                                <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-warning btn-sm fw-bold"><i class="fas fa-user-shield me-1"></i> Admin Portal</a>
                            </c:when>
                            <c:otherwise>
                                <a href="${pageContext.request.contextPath}/customer/dashboard" class="btn btn-warning btn-sm fw-bold"><i class="fas fa-user-circle me-1"></i> My Dashboard</a>
                            </c:otherwise>
                        </c:choose>
                        <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-light btn-sm"><i class="fas fa-sign-out-alt me-1"></i> Logout</a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/login" class="btn btn-outline-light btn-sm"><i class="fas fa-lock me-1"></i> Internet Banking</a>
                        <a href="${pageContext.request.contextPath}/admin-login" class="btn btn-warning btn-sm text-dark fw-bold"><i class="fas fa-user-shield me-1"></i> Admin Login</a>
                        <a href="${pageContext.request.contextPath}/register" class="btn btn-outline-warning btn-sm text-white fw-bold"><i class="fas fa-user-plus me-1"></i> Open Account</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</nav>
