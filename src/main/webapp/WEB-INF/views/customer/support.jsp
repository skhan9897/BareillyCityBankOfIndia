<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="d-flex min-vh-100">
    <jsp:include page="/WEB-INF/views/common/customer-sidebar.jsp" />

    <div class="flex-grow-1 p-4 bg-light">
        <h3 class="fw-bold text-navy mb-4">Customer Care & Support</h3>

        <div class="row g-4">
            <div class="col-lg-6">
                <div class="card card-bank p-4">
                    <h5 class="fw-bold text-navy mb-3"><i class="fas fa-headset text-warning me-2"></i>24x7 Banking Support</h5>
                    <div class="p-3 bg-light border rounded mb-3">
                        <div class="fw-bold text-navy"><i class="fas fa-phone-alt me-2 text-success"></i>Toll Free Helpline</div>
                        <div class="fs-5 text-dark font-mono mt-1">1800-123-4567</div>
                    </div>
                    <div class="p-3 bg-light border rounded">
                        <div class="fw-bold text-navy"><i class="fas fa-envelope me-2 text-primary"></i>Email Support</div>
                        <div class="fs-6 text-muted mt-1">support@bareillybank.com</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
