<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<footer class="bg-dark text-white pt-5 pb-4 mt-auto">
    <div class="container">
        <div class="row g-4">
            <div class="col-lg-4 col-md-6">
                <h5 class="text-warning font-weight-bold"><i class="fas fa-university me-2"></i>BAREILLY CITY BANK OF INDIA</h5>
                <p class="small text-muted mt-3">Empowering citizens with secure, seamless digital banking services across Bareilly and India. ISO 27001 Certified Banking Simulation Platform.</p>
                <div class="d-flex gap-3 text-warning mt-3 fs-5">
                    <i class="fab fa-facebook"></i>
                    <i class="fab fa-twitter"></i>
                    <i class="fab fa-linkedin"></i>
                    <i class="fab fa-instagram"></i>
                </div>
            </div>
            <div class="col-lg-2 col-md-6">
                <h6 class="text-white text-uppercase font-weight-bold mb-3">Quick Links</h6>
                <ul class="list-unstyled text-muted small">
                    <li class="mb-2"><a href="${pageContext.request.contextPath}/about" class="text-muted text-decoration-none">About Bank</a></li>
                    <li class="mb-2"><a href="${pageContext.request.contextPath}/services" class="text-muted text-decoration-none">Savings & Deposits</a></li>
                    <li class="mb-2"><a href="${pageContext.request.contextPath}/services" class="text-muted text-decoration-none">Loan Products</a></li>
                    <li class="mb-2"><a href="${pageContext.request.contextPath}/faqs" class="text-muted text-decoration-none">Security Tips</a></li>
                </ul>
            </div>
            <div class="col-lg-3 col-md-6">
                <h6 class="text-white text-uppercase font-weight-bold mb-3">Customer Care</h6>
                <ul class="list-unstyled text-muted small">
                    <li class="mb-2"><i class="fas fa-phone-alt me-2 text-warning"></i>Toll Free: 1800-123-4567</li>
                    <li class="mb-2"><i class="fas fa-envelope me-2 text-warning"></i>support@bareillybank.com</li>
                    <li class="mb-2"><i class="fas fa-map-marker-alt me-2 text-warning"></i>Civil Lines, Bareilly - 243001</li>
                </ul>
            </div>
            <div class="col-lg-3 col-md-6">
                <h6 class="text-white text-uppercase font-weight-bold mb-3">Security & Compliance</h6>
                <p class="small text-muted">Protected by 256-bit SSL encryption, double-entry accounting ledger, and multi-factor authorization safeguards.</p>
            </div>
        </div>
        <hr class="border-secondary my-4">
        <div class="row align-items-center small text-muted">
            <div class="col-md-6 text-center text-md-start">
                &copy; 2026 BAREILLY CITY BANK OF INDIA. All rights reserved.
            </div>
            <div class="col-md-6 text-center text-md-end">
                <span>Privacy Policy</span> | <span>Terms of Service</span> | <a href="${pageContext.request.contextPath}/admin-login" class="text-secondary text-decoration-none ms-2"><i class="fas fa-user-shield me-1"></i> Staff Portal</a>
            </div>
        </div>
    </div>
</footer>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<!-- Custom Main JS -->
<script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
</body>
</html>
