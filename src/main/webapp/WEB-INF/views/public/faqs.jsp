<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="container my-5">
    <div class="text-center mb-5">
        <h1 class="fw-bold text-navy">Frequently Asked Questions (FAQs)</h1>
        <p class="text-muted">Find quick answers to common Internet Banking queries and security policies.</p>
    </div>

    <div class="accordion" id="faqAccordion">
        <div class="accordion-item mb-3 border rounded shadow-sm">
            <h2 class="accordion-header">
                <button class="accordion-button fw-bold text-navy" type="button" data-bs-toggle="collapse" data-bs-target="#faq1">
                    How do I register for Bareilly City Bank Internet Banking?
                </button>
            </h2>
            <div id="faq1" class="accordion-collapse collapse show" data-bs-parent="#faqAccordion">
                <div class="accordion-body text-muted">
                    Click on the "Open Account" or "Register" button on the top menu. Enter your Personal Details, Verified Mobile Number, Aadhaar Number, and PAN Number. Upon submitting, your Savings Account will be auto-generated and funded with a welcome balance.
                </div>
            </div>
        </div>

        <div class="accordion-item mb-3 border rounded shadow-sm">
            <h2 class="accordion-header">
                <button class="accordion-button collapsed fw-bold text-navy" type="button" data-bs-toggle="collapse" data-bs-target="#faq2">
                    What happens if I enter an incorrect password multiple times?
                </button>
            </h2>
            <div id="faq2" class="accordion-collapse collapse" data-bs-parent="#faqAccordion">
                <div class="accordion-body text-muted">
                    For security purposes, after 5 consecutive failed login attempts, your account will be temporarily locked for 15 minutes to prevent brute-force attacks.
                </div>
            </div>
        </div>

        <div class="accordion-item mb-3 border rounded shadow-sm">
            <h2 class="accordion-header">
                <button class="accordion-button collapsed fw-bold text-navy" type="button" data-bs-toggle="collapse" data-bs-target="#faq3">
                    Are online money transfers protected against duplicate requests?
                </button>
            </h2>
            <div id="faq3" class="accordion-collapse collapse" data-bs-parent="#faqAccordion">
                <div class="accordion-body text-muted">
                    Yes! All transfer transactions utilize unique Idempotency Keys and atomic database row locks to guarantee that double-clicking or network retries will never charge your account twice.
                </div>
            </div>
        </div>

        <div class="accordion-item mb-3 border rounded shadow-sm">
            <h2 class="accordion-header">
                <button class="accordion-button collapsed fw-bold text-navy" type="button" data-bs-toggle="collapse" data-bs-target="#faq4">
                    How do I download my official Account Statement?
                </button>
            </h2>
            <div id="faq4" class="accordion-collapse collapse" data-bs-parent="#faqAccordion">
                <div class="accordion-body text-muted">
                    Log in to your Customer Portal, navigate to "Statements", choose your account and date range, and click "Export PDF Statement" or "Export CSV".
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
