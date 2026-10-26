<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="d-flex min-vh-100">
    <jsp:include page="/WEB-INF/views/common/customer-sidebar.jsp" />

    <div class="flex-grow-1 p-4 bg-light">
        <!-- Top Profile Welcome & Net Worth Banner -->
        <div class="card card-bank p-4 mb-4 text-white shadow-lg border-0" style="background: linear-gradient(135deg, #0A2540 0%, #1A5276 60%, #051428 100%);">
            <div class="row align-items-center g-3">
                <div class="col-md-7 col-lg-8">
                    <div class="d-flex align-items-center gap-3">
                        <div class="bg-warning text-dark fw-bold rounded-circle d-flex align-items-center justify-content-center shadow" style="width: 58px; height: 58px; font-size: 1.5rem;">
                            ${customer.firstName.substring(0, 1)}${customer.lastName.substring(0, 1)}
                        </div>
                        <div>
                            <h4 class="fw-bold mb-1">Welcome back, ${customer.fullName}!</h4>
                            <div class="small text-white-50">
                                Customer ID: <strong class="text-warning">${customer.customerNumber}</strong> | Branch: <strong>Civil Lines Main (BCBI0000001)</strong>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-5 col-lg-4 text-md-end">
                    <div class="d-inline-flex flex-column align-items-md-end gap-2">
                        <span class="badge ${customer.kycStatus == 'APPROVED' ? 'bg-success' : 'bg-warning text-dark'} px-3 py-2 fs-6 shadow-sm">
                            <i class="fas ${customer.kycStatus == 'APPROVED' ? 'fa-check-circle' : 'fa-hourglass-half'} me-1"></i> KYC Verification: ${customer.kycStatus}
                        </span>
                        <a href="${pageContext.request.contextPath}/customer/kyc" class="small text-warning text-decoration-none"><i class="fas fa-id-card me-1"></i> View KYC Profile</a>
                    </div>
                </div>
            </div>

            <!-- Financial Net Worth Bar -->
            <div class="row g-3 mt-3 pt-3 border-top border-secondary">
                <div class="col-md-3 col-6">
                    <div class="small text-white-50">Total Net Balance</div>
                    <div class="fw-bold fs-4 text-warning">₹ <fmt:formatNumber value="${accounts[0].availableBalance}" type="number" minFractionDigits="2"/></div>
                </div>
                <div class="col-md-3 col-6">
                    <div class="small text-white-50">Ledger Reserve Balance</div>
                    <div class="fw-bold fs-5 text-white">₹ <fmt:formatNumber value="${accounts[0].ledgerBalance}" type="number" minFractionDigits="2"/></div>
                </div>
                <div class="col-md-3 col-6">
                    <div class="small text-white-50">Daily Transfer Limit</div>
                    <div class="fw-bold fs-5 text-white">₹ 1,00,000.00</div>
                </div>
                <div class="col-md-3 col-6">
                    <div class="small text-white-50">Security Encryption</div>
                    <div class="fw-bold fs-6 text-success"><i class="fas fa-shield-alt me-1"></i> 256-Bit SSL Active</div>
                </div>
            </div>
        </div>

        <!-- Section 1: Linked Bank Accounts Carousel / Cards -->
        <h5 class="fw-bold text-navy mb-3"><i class="fas fa-university text-primary me-2"></i>My Accounts Summary</h5>
        <div class="row g-4 mb-4">
            <c:forEach var="acc" items="${accounts}">
                <div class="col-md-6 col-lg-6">
                    <div class="card card-bank p-4 border-0 shadow-sm h-100 position-relative" style="background: linear-gradient(135deg, #FFFFFF 0%, #F8FAFC 100%); border-left: 5px solid #FFC107 !important;">
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <div>
                                <span class="badge bg-navy text-white fw-bold px-3 py-1 me-2">${acc.productName}</span>
                                <span class="badge bg-light text-navy border">${acc.accountType}</span>
                            </div>
                            <span class="badge ${acc.status == 'ACTIVE' ? 'badge-status-active' : 'badge-status-frozen'}">${acc.status}</span>
                        </div>

                        <div class="row g-3 mb-3">
                            <div class="col-6">
                                <div class="small text-muted">Account Number</div>
                                <div class="fw-bold text-navy fs-6">${acc.accountNumber}</div>
                            </div>
                            <div class="col-6">
                                <div class="small text-muted">IFSC Code</div>
                                <div class="fw-bold text-navy fs-6">${acc.ifscCode}</div>
                            </div>
                            <div class="col-6">
                                <div class="small text-muted">Available Balance</div>
                                <div class="fw-bold fs-4 text-success">₹ <fmt:formatNumber value="${acc.availableBalance}" type="number" minFractionDigits="2"/></div>
                            </div>
                            <div class="col-6">
                                <div class="small text-muted">Ledger Balance</div>
                                <div class="fw-bold fs-5 text-dark">₹ <fmt:formatNumber value="${acc.ledgerBalance}" type="number" minFractionDigits="2"/></div>
                            </div>
                        </div>

                        <div class="d-flex gap-2 pt-2 border-top">
                            <a href="${pageContext.request.contextPath}/customer/account-details?id=${acc.id}" class="btn btn-bank-primary btn-sm flex-fill py-2"><i class="fas fa-eye me-1"></i> Full View Details</a>
                            <a href="${pageContext.request.contextPath}/customer/statement?accountId=${acc.id}" class="btn btn-outline-secondary btn-sm flex-fill py-2"><i class="fas fa-file-invoice me-1"></i> Statement</a>
                            <a href="${pageContext.request.contextPath}/customer/transfer" class="btn btn-bank-gold btn-sm flex-fill py-2 fw-bold"><i class="fas fa-paper-plane me-1"></i> Send Money</a>
                        </div>
                    </div>
                </div>
            </c:forEach>
            <c:if test="${empty accounts}">
                <div class="col-12">
                    <div class="alert alert-warning shadow-sm"><i class="fas fa-exclamation-circle me-2"></i> No bank accounts linked to your customer profile yet.</div>
                </div>
            </c:if>
        </div>

        <!-- Section 2: Quick Banking Actions Grid -->
        <h5 class="fw-bold text-navy mb-3"><i class="fas fa-th-large text-warning me-2"></i>Instant Digital Banking Services</h5>
        <div class="row g-3 mb-4">
            <div class="col-6 col-md-4 col-lg-2">
                <a href="${pageContext.request.contextPath}/customer/transfer" class="card card-bank p-3 text-center text-decoration-none h-100 shadow-sm border-0">
                    <div class="stat-icon stat-icon-blue mx-auto mb-2"><i class="fas fa-paper-plane"></i></div>
                    <div class="fw-bold text-navy small">Send Money</div>
                    <div class="text-muted" style="font-size: 0.72rem;">IMPS / NEFT / RTGS</div>
                </a>
            </div>
            <div class="col-6 col-md-4 col-lg-2">
                <a href="${pageContext.request.contextPath}/customer/beneficiaries" class="card card-bank p-3 text-center text-decoration-none h-100 shadow-sm border-0">
                    <div class="stat-icon stat-icon-green mx-auto mb-2"><i class="fas fa-qrcode"></i></div>
                    <div class="fw-bold text-navy small">UPI & QR Pay</div>
                    <div class="text-muted" style="font-size: 0.72rem;">Instant VPA Payments</div>
                </a>
            </div>
            <div class="col-6 col-md-4 col-lg-2">
                <a href="${pageContext.request.contextPath}/customer/deposits" class="card card-bank p-3 text-center text-decoration-none h-100 shadow-sm border-0">
                    <div class="stat-icon stat-icon-gold mx-auto mb-2"><i class="fas fa-piggy-bank"></i></div>
                    <div class="fw-bold text-navy small">Open FD & RD</div>
                    <div class="text-muted" style="font-size: 0.72rem;">High Yield 7.50%</div>
                </a>
            </div>
            <div class="col-6 col-md-4 col-lg-2">
                <a href="${pageContext.request.contextPath}/customer/loans" class="card card-bank p-3 text-center text-decoration-none h-100 shadow-sm border-0">
                    <div class="stat-icon stat-icon-red mx-auto mb-2"><i class="fas fa-hand-holding-usd"></i></div>
                    <div class="fw-bold text-navy small">Apply for Loan</div>
                    <div class="text-muted" style="font-size: 0.72rem;">Instant Credit Review</div>
                </a>
            </div>
            <div class="col-6 col-md-4 col-lg-2">
                <a href="${pageContext.request.contextPath}/customer/cards" class="card card-bank p-3 text-center text-decoration-none h-100 shadow-sm border-0">
                    <div class="stat-icon stat-icon-blue mx-auto mb-2"><i class="fas fa-credit-card"></i></div>
                    <div class="fw-bold text-navy small">Debit Card</div>
                    <div class="text-muted" style="font-size: 0.72rem;">Block & Set Limits</div>
                </a>
            </div>
            <div class="col-6 col-md-4 col-lg-2">
                <a href="${pageContext.request.contextPath}/customer/statement" class="card card-bank p-3 text-center text-decoration-none h-100 shadow-sm border-0">
                    <div class="stat-icon stat-icon-gold mx-auto mb-2"><i class="fas fa-file-pdf"></i></div>
                    <div class="fw-bold text-navy small">Download PDF</div>
                    <div class="text-muted" style="font-size: 0.72rem;">Official Passbook</div>
                </a>
            </div>
        </div>

        <!-- Section 3: Analytics Chart & Recent Posted Transactions -->
        <div class="row g-4">
            <!-- Left Column: Virtual Card Widget & Financial Analytics -->
            <div class="col-lg-5">
                <c:if test="${not empty card}">
                    <div class="debit-card-widget mb-4 shadow">
                        <div class="d-flex justify-content-between align-items-center mb-4">
                            <span class="fw-bold fs-6">BAREILLY CITY BANK</span>
                            <span class="badge ${card.status == 'ACTIVE' ? 'bg-success' : 'bg-danger'}">${card.status}</span>
                        </div>
                        <div class="chip-icon mb-3"></div>
                        <div class="fs-4 fw-mono mb-3" style="letter-spacing: 2px;">${card.cardNumberMasked}</div>
                        <div class="d-flex justify-content-between align-items-end text-uppercase small">
                            <div>
                                <div class="text-white-50" style="font-size: 0.65rem;">Card Holder</div>
                                <div class="fw-bold">${customer.fullName}</div>
                            </div>
                            <div>
                                <div class="text-white-50" style="font-size: 0.65rem;">Expires</div>
                                <div class="fw-bold">${card.expiryMonth}/${card.expiryYear}</div>
                            </div>
                            <i class="fab fa-cc-visa fa-2x text-warning"></i>
                        </div>
                    </div>
                </c:if>

                <div class="card card-bank p-4 mb-4">
                    <h5 class="fw-bold text-navy mb-3"><i class="fas fa-chart-line text-primary me-2"></i>Account Activity Summary</h5>
                    <div style="height: 180px;">
                        <canvas id="dashboardActivityChart"></canvas>
                    </div>
                </div>
            </div>

            <!-- Right Column: Recent Posted Transactions Table -->
            <div class="col-lg-7">
                <div class="card card-bank p-4 h-100">
                    <div class="d-flex justify-content-between align-items-center mb-3 border-bottom pb-2">
                        <h5 class="fw-bold text-navy mb-0"><i class="fas fa-history me-2 text-primary"></i>Recent Transactions</h5>
                        <a href="${pageContext.request.contextPath}/customer/transactions" class="btn btn-sm btn-outline-primary fw-bold">View Full History</a>
                    </div>
                    <div class="table-responsive">
                        <table class="table table-hover table-bank mb-0">
                            <thead>
                                <tr>
                                    <th>Ref Number</th>
                                    <th>Type</th>
                                    <th>Amount</th>
                                    <th>Status</th>
                                    <th>Date</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="tx" items="${transactions}">
                                    <tr>
                                        <td class="fw-bold text-navy small">${tx.transactionRef}</td>
                                        <td><span class="badge bg-light text-navy border">${tx.transactionType}</span></td>
                                        <td class="fw-bold text-success">₹ <fmt:formatNumber value="${tx.amount}" type="number" minFractionDigits="2"/></td>
                                        <td><span class="badge badge-status-active">${tx.status}</span></td>
                                        <td class="small text-muted">${tx.createdAt}</td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty transactions}">
                                    <tr>
                                        <td colspan="5" class="text-center text-muted py-4">
                                            <i class="fas fa-receipt fa-2x text-muted mb-2 d-block"></i>
                                            No transactions recorded yet for this account.
                                        </td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
document.addEventListener('DOMContentLoaded', function () {
    const ctx = document.getElementById('dashboardActivityChart');
    if (ctx) {
        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
                datasets: [{
                    label: 'Account Deposits / Transfers (₹)',
                    data: [12000, 19000, 3000, 5000, 20000, 30000, 45000],
                    backgroundColor: 'rgba(10, 37, 64, 0.8)',
                    borderColor: '#0A2540',
                    borderWidth: 1,
                    borderRadius: 6
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: { legend: { display: false } },
                scales: {
                    y: { beginAtZero: true, grid: { color: '#E2E8F0' } },
                    x: { grid: { display: false } }
                }
            }
        });
    }
});
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
