<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="container my-5">
    <div class="row g-5">
        <div class="col-lg-5">
            <h2 class="fw-bold text-navy mb-3">Contact Bank Support</h2>
            <p class="text-muted">Our customer service representatives are available 24x7 to assist you with account queries, transfer issues, and card blocking.</p>

            <div class="card card-bank p-4 mb-3">
                <div class="d-flex align-items-center mb-2">
                    <i class="fas fa-building text-primary fa-lg me-3"></i>
                    <div>
                        <h6 class="fw-bold mb-0">Head Office</h6>
                        <small class="text-muted">Civil Lines, Bareilly, UP - 243001</small>
                    </div>
                </div>
            </div>

            <div class="card card-bank p-4 mb-3">
                <div class="d-flex align-items-center mb-2">
                    <i class="fas fa-phone-alt text-success fa-lg me-3"></i>
                    <div>
                        <h6 class="fw-bold mb-0">Toll Free Helpline</h6>
                        <small class="text-muted">1800-123-4567 (24x7 Support)</small>
                    </div>
                </div>
            </div>

            <div class="card card-bank p-4">
                <div class="d-flex align-items-center mb-2">
                    <i class="fas fa-envelope text-warning fa-lg me-3"></i>
                    <div>
                        <h6 class="fw-bold mb-0">Email Support</h6>
                        <small class="text-muted">support@bareillybank.com</small>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-lg-7">
            <div class="card card-bank p-4 shadow-sm">
                <h4 class="fw-bold text-navy mb-4"><i class="fas fa-paper-plane me-2 text-warning"></i>Send Us a Message</h4>
                <form data-ajax="false" action="${pageContext.request.contextPath}/contact" method="post">
                    <div class="row g-3">
                        <div class="col-md-6">
                            <label class="form-label small fw-bold">Full Name</label>
                            <input type="text" class="form-control" required placeholder="Rahul Kumar">
                        </div>
                        <div class="col-md-6">
                            <label class="form-label small fw-bold">Email Address</label>
                            <input type="email" class="form-control" required placeholder="rahul@gmail.com">
                        </div>
                        <div class="col-12">
                            <label class="form-label small fw-bold">Subject</label>
                            <input type="text" class="form-control" required placeholder="Account enquiry">
                        </div>
                        <div class="col-12">
                            <label class="form-label small fw-bold">Message</label>
                            <textarea class="form-control" rows="4" required placeholder="Describe your query..."></textarea>
                        </div>
                        <div class="col-12">
                            <button type="submit" class="btn btn-bank-primary w-100"><i class="fas fa-paper-plane me-2"></i> Submit Inquiry</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
