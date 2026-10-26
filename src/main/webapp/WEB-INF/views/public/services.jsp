<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="container my-5">
    <div class="text-center mb-5">
        <h1 class="fw-bold text-navy">Our Financial Products & Services</h1>
        <p class="text-muted">Comprehensive solutions for savings, deposits, loans, and digital payments.</p>
    </div>

    <div class="row g-4">
        <div class="col-lg-6">
            <div class="card card-bank p-4 h-100">
                <div class="d-flex align-items-center mb-3">
                    <div class="stat-icon stat-icon-blue me-3"><i class="fas fa-piggy-bank"></i></div>
                    <div>
                        <h5 class="fw-bold mb-0">Regular Savings Account</h5>
                        <small class="text-muted">High liquidity & competitive return</small>
                    </div>
                </div>
                <ul class="text-muted small">
                    <li>3.50% p.a. interest calculated daily and credited quarterly.</li>
                    <li>₹ 1,000 minimum monthly balance.</li>
                    <li>Free virtual debit card and internet banking.</li>
                </ul>
                <a href="${pageContext.request.contextPath}/register" class="btn btn-outline-primary btn-sm mt-auto">Apply Now</a>
            </div>
        </div>

        <div class="col-lg-6">
            <div class="card card-bank p-4 h-100">
                <div class="d-flex align-items-center mb-3">
                    <div class="stat-icon stat-icon-gold me-3"><i class="fas fa-briefcase"></i></div>
                    <div>
                        <h5 class="fw-bold mb-0">Standard Current Account</h5>
                        <small class="text-muted">Tailored for merchants and businesses</small>
                    </div>
                </div>
                <ul class="text-muted small">
                    <li>High daily transfer limits (Up to ₹ 10,00,000/day).</li>
                    <li>Multi-user authorization and bulk payment simulation.</li>
                    <li>Detailed CSV statement export for tax accounting.</li>
                </ul>
                <a href="${pageContext.request.contextPath}/register" class="btn btn-outline-warning btn-sm mt-auto">Apply Now</a>
            </div>
        </div>

        <div class="col-lg-6">
            <div class="card card-bank p-4 h-100">
                <div class="d-flex align-items-center mb-3">
                    <div class="stat-icon stat-icon-green me-3"><i class="fas fa-chart-line"></i></div>
                    <div>
                        <h5 class="fw-bold mb-0">Fixed Deposits (FD) & RD</h5>
                        <small class="text-muted">Guaranteed returns up to 7.50% p.a.</small>
                    </div>
                </div>
                <ul class="text-muted small">
                    <li>Tenures ranging from 12 months to 10 years.</li>
                    <li>Premature closure facility available with standard 1% penalty.</li>
                    <li>Auto-renewal and maturity alerts via SMS and email.</li>
                </ul>
                <a href="${pageContext.request.contextPath}/login" class="btn btn-outline-success btn-sm mt-auto">Create Deposit</a>
            </div>
        </div>

        <div class="col-lg-6">
            <div class="card card-bank p-4 h-100">
                <div class="d-flex align-items-center mb-3">
                    <div class="stat-icon stat-icon-red me-3"><i class="fas fa-home"></i></div>
                    <div>
                        <h5 class="fw-bold mb-0">Home & Personal Loans</h5>
                        <small class="text-muted">Low interest rates & fast approval</small>
                    </div>
                </div>
                <ul class="text-muted small">
                    <li>Home Loan rates starting at 8.40% p.a.</li>
                    <li>Personal Loans up to ₹ 10,00,000 without collateral.</li>
                    <li>Transparent EMI tracking and online repayment schedule.</li>
                </ul>
                <a href="${pageContext.request.contextPath}/login" class="btn btn-outline-danger btn-sm mt-auto">Apply Loan</a>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
