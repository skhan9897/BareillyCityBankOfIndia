<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="d-flex min-vh-100">
    <jsp:include page="/WEB-INF/views/common/customer-sidebar.jsp" />

    <div class="flex-grow-1 p-4 bg-light">
        <h3 class="fw-bold text-navy mb-4">Virtual UPI Sandbox</h3>

        <div class="row g-4">
            <div class="col-lg-5 text-center">
                <div class="card card-bank p-4">
                    <h5 class="fw-bold text-navy mb-2">My UPI ID</h5>
                    <div class="badge bg-navy text-warning fs-6 px-3 py-2 mb-3">rahulkumar@bcbi</div>
                    <div class="p-3 bg-white border rounded d-inline-block shadow-sm">
                        <img src="https://api.qrserver.com/v1/create-qr-code/?size=180x180&data=upi://pay?pa=rahulkumar@bcbi&pn=Rahul%20Kumar" alt="UPI QR Code" class="img-fluid">
                    </div>
                    <p class="small text-muted mt-3 mb-0">Scan to receive instant UPI payments into your primary savings account.</p>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
