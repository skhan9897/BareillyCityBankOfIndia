<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<!-- Official Bank Wide Hero Banner -->
<div class="container-fluid px-0 mb-4">
    <div class="position-relative">
        <img src="${pageContext.request.contextPath}/assets/images/banner.svg" alt="Bareilly City Bank Official Banner" class="w-100 banner-hero-img">
    </div>
</div>

<div class="container mb-5">
    <div class="row align-items-center g-4">
        <div class="col-lg-7">
            <span class="badge bg-warning text-dark fw-bold px-3 py-2 mb-3 rounded-pill"><i class="fas fa-shield-alt me-1"></i> ISO 27001 SECURED DIGITAL BANKING</span>
            <h1 class="display-5 fw-extrabold text-navy mb-3">Modern, Safe & Instant Banking for Bareilly</h1>
            <p class="lead text-muted mb-4">Experience next-generation banking with instant fund transfers, high-yield fixed deposits, instant loan approvals, and zero balance account opening.</p>
            <div class="d-flex flex-wrap gap-3">
                <a href="${pageContext.request.contextPath}/register" class="btn btn-bank-gold btn-lg shadow-sm"><i class="fas fa-user-plus me-2"></i> Open Zero Balance Account</a>
                <a href="${pageContext.request.contextPath}/login" class="btn btn-bank-primary btn-lg shadow-sm"><i class="fas fa-sign-in-alt me-2"></i> Internet Banking Login</a>
            </div>
        </div>
        <div class="col-lg-5">
            <div class="card card-bank p-4 text-dark shadow-lg">
                <h5 class="fw-bold text-navy mb-3"><i class="fas fa-calculator text-warning me-2"></i>FD Interest Calculator</h5>
                <form oninput="calcFd()">
                    <div class="mb-3">
                        <label class="form-label small fw-bold">Deposit Amount (₹)</label>
                        <input type="number" id="fdAmt" class="form-control" value="100000" min="1000">
                    </div>
                    <div class="mb-3">
                        <label class="form-label small fw-bold">Tenure (Months)</label>
                        <select id="fdTenure" class="form-select">
                            <option value="12">12 Months (7.10% p.a.)</option>
                            <option value="24">24 Months (7.50% p.a.)</option>
                            <option value="36">36 Months (7.50% p.a.)</option>
                        </select>
                    </div>
                    <div class="p-3 bg-light rounded text-center border">
                        <div class="small text-muted">Estimated Maturity Amount</div>
                        <h3 class="fw-bold text-success mb-0" id="fdRes">₹ 107,290</h3>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<div class="container my-5">
    <div class="text-center mb-5">
        <h2 class="fw-bold text-navy">Core Banking Features</h2>
        <p class="text-muted">Designed for individual citizens, MSMEs, and commercial enterprises.</p>
    </div>
    <div class="row g-4">
        <div class="col-md-4">
            <div class="card card-bank p-4 h-100 text-center">
                <div class="stat-icon stat-icon-blue mx-auto mb-3"><i class="fas fa-wallet"></i></div>
                <h5 class="fw-bold">Savings & Current Accounts</h5>
                <p class="small text-muted">Earn competitive interest with zero balance restrictions for BSBD accounts and instant virtual debit card issuance.</p>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card card-bank p-4 h-100 text-center">
                <div class="stat-icon stat-icon-gold mx-auto mb-3"><i class="fas fa-paper-plane"></i></div>
                <h5 class="fw-bold">Instant Money Transfer</h5>
                <p class="small text-muted">NEFT, RTGS, IMPS, and internal transfer with double-entry ledger balance protection and instant receipts.</p>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card card-bank p-4 h-100 text-center">
                <div class="stat-icon stat-icon-green mx-auto mb-3"><i class="fas fa-hand-holding-usd"></i></div>
                <h5 class="fw-bold">Quick Loans</h5>
                <p class="small text-muted">Apply for Personal, Home, Vehicle, or Education loans with transparent EMI schedules and low processing fees.</p>
            </div>
        </div>
    </div>
</div>

<script>
function calcFd() {
    let P = parseFloat(document.getElementById('fdAmt').value) || 0;
    let tenure = parseInt(document.getElementById('fdTenure').value);
    let r = tenure >= 12 ? 0.071 : 0.065;
    let t = tenure / 12.0;
    let res = P * Math.pow(1 + (r/4), 4*t);
    document.getElementById('fdRes').innerText = '₹ ' + Math.round(res).toLocaleString('en-IN');
}
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
