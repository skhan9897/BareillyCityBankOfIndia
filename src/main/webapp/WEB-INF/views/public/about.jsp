<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="container my-5">
    <div class="row align-items-center g-5">
        <div class="col-lg-6">
            <span class="badge bg-primary px-3 py-2 text-uppercase mb-2">About Our Institution</span>
            <h1 class="fw-bold text-navy mb-3">BAREILLY CITY BANK OF INDIA</h1>
            <p class="lead text-muted">Established to serve the banking, financial, and digital payment needs of Bareilly District and Uttar Pradesh.</p>
            <p class="text-muted">Our core architecture relies on double-entry ledger security, strict maker-checker compliance, BCrypt credential vaulting, and seamless web/mobile accessibility.</p>
            <div class="row g-3 mt-3">
                <div class="col-6">
                    <div class="p-3 border rounded bg-white">
                        <h3 class="fw-bold text-primary mb-1">3+</h3>
                        <div class="small text-muted">Key Branches in Bareilly</div>
                    </div>
                </div>
                <div class="col-6">
                    <div class="p-3 border rounded bg-white">
                        <h3 class="fw-bold text-success mb-1">₹ 25 Cr+</h3>
                        <div class="small text-muted">Simulated Assets Under Management</div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-lg-6">
            <div class="card card-bank p-4 bg-navy text-white shadow-lg">
                <h4 class="fw-bold text-warning mb-3"><i class="fas fa-bullseye me-2"></i>Our Core Vision</h4>
                <p>To provide accessible, transparent, and ultra-secure banking tools to every household, agricultural enterprise, and business in Bareilly.</p>
                <hr class="border-secondary">
                <h5 class="fw-bold text-white mb-2"><i class="fas fa-shield-alt me-2 text-success"></i>Security Architecture</h5>
                <ul class="small text-white-50">
                    <li>Double-Entry General Ledger Accounting Engine</li>
                    <li>BCrypt 10-round salted password vaulting</li>
                    <li>Idempotency Key validation against double-debiting</li>
                    <li>Automated CSRF Token and Rate-limiting Throttling</li>
                </ul>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
