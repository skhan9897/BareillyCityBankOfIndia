<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="container my-5">
    <div class="row justify-content-center">
        <div class="col-lg-9 col-xl-8">
            <div class="card card-bank p-4 p-md-5 shadow-lg border-0">
                <div class="text-center mb-4">
                    <div class="stat-icon stat-icon-green mx-auto mb-2"><i class="fas fa-university"></i></div>
                    <h3 class="fw-bold text-navy mb-1">Open Bank Account Online</h3>
                    <p class="small text-muted">BAREILLY CITY BANK OF INDIA - Digital Account Onboarding</p>
                </div>

                <!-- Stepper Progress Bar -->
                <div class="stepper-wrapper">
                    <div class="stepper-progress-bar" id="stepperBar" style="width: 0%;"></div>
                    <div class="stepper-item active" id="stepIndicator1">
                        <div class="step-counter">1</div>
                        <div class="step-name">Personal Details</div>
                    </div>
                    <div class="stepper-item" id="stepIndicator2">
                        <div class="step-counter">2</div>
                        <div class="step-name">KYC & Address</div>
                    </div>
                    <div class="stepper-item" id="stepIndicator3">
                        <div class="step-counter">3</div>
                        <div class="step-name">Account & Credentials</div>
                    </div>
                </div>

                <form id="multiStepForm" action="${pageContext.request.contextPath}/register" method="post" novalidate>
                    <input type="hidden" name="csrfToken" value="${csrfToken}">
                    <div class="form-alert" id="formAlertBox"></div>

                    <!-- STEP 1: Personal Details -->
                    <div class="form-step active" id="step1">
                        <h5 class="fw-bold text-navy mb-3 border-bottom pb-2"><i class="fas fa-user text-primary me-2"></i>Step 1: Personal & Contact Information</h5>
                        <div class="row g-3">
                            <div class="col-md-6">
                                <label class="form-label small fw-bold">First Name <span class="text-danger">*</span></label>
                                <input type="text" id="firstName" name="firstName" class="form-control" placeholder="Rahul">
                            </div>
                            <div class="col-md-6">
                                <label class="form-label small fw-bold">Last Name <span class="text-danger">*</span></label>
                                <input type="text" id="lastName" name="lastName" class="form-control" placeholder="Kumar">
                            </div>

                            <div class="col-md-6">
                                <label class="form-label small fw-bold">Date of Birth <span class="text-danger">*</span></label>
                                <input type="date" id="dob" name="dob" class="form-control" value="1995-05-15">
                            </div>
                            <div class="col-md-6">
                                <label class="form-label small fw-bold">Gender <span class="text-danger">*</span></label>
                                <select id="gender" name="gender" class="form-select">
                                    <option value="Male">Male</option>
                                    <option value="Female">Female</option>
                                    <option value="Other">Other</option>
                                </select>
                            </div>

                            <div class="col-md-6">
                                <label class="form-label small fw-bold">Mobile Number (10 Digits) <span class="text-danger">*</span></label>
                                <div class="input-group">
                                    <span class="input-group-text bg-light fw-bold">+91</span>
                                    <input type="text" id="mobile" name="mobile" class="form-control" placeholder="9876543210" maxlength="10">
                                </div>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label small fw-bold">Email Address <span class="text-danger">*</span></label>
                                <input type="email" id="email" name="email" class="form-control" placeholder="rahul.kumar@gmail.com">
                            </div>

                            <div class="col-12 text-end mt-4">
                                <button type="button" class="btn btn-bank-primary px-4" onclick="nextStep(1)"><i class="fas fa-arrow-right me-1"></i> Next: KYC & Address</button>
                            </div>
                        </div>
                    </div>

                    <!-- STEP 2: KYC & Address Information -->
                    <div class="form-step d-none" id="step2">
                        <h5 class="fw-bold text-navy mb-3 border-bottom pb-2"><i class="fas fa-id-card text-warning me-2"></i>Step 2: Identity Documents & Address</h5>
                        <div class="row g-3">
                            <div class="col-md-6">
                                <label class="form-label small fw-bold">Aadhaar Number (12 Digits) <span class="text-danger">*</span></label>
                                <input type="text" id="aadhaar" name="aadhaar" class="form-control" placeholder="999988887777" maxlength="12">
                            </div>
                            <div class="col-md-6">
                                <label class="form-label small fw-bold">PAN Card Number <span class="text-danger">*</span></label>
                                <input type="text" id="pan" name="pan" class="form-control" style="text-transform: uppercase;" placeholder="ABCDE1234F" maxlength="10">
                            </div>

                            <div class="col-12">
                                <label class="form-label small fw-bold">Residential Street Address <span class="text-danger">*</span></label>
                                <input type="text" id="address" name="address" class="form-control" placeholder="123 Civil Lines, Near Railway Station">
                            </div>

                            <div class="col-md-4">
                                <label class="form-label small fw-bold">City / District</label>
                                <input type="text" name="city" class="form-control" value="Bareilly" readonly>
                            </div>
                            <div class="col-md-4">
                                <label class="form-label small fw-bold">State</label>
                                <input type="text" name="state" class="form-control" value="Uttar Pradesh" readonly>
                            </div>
                            <div class="col-md-4">
                                <label class="form-label small fw-bold">Pincode <span class="text-danger">*</span></label>
                                <input type="text" id="pincode" name="pincode" class="form-control" value="243001" maxlength="6">
                            </div>

                            <div class="col-12 d-flex justify-content-between mt-4">
                                <button type="button" class="btn btn-outline-secondary px-4" onclick="prevStep(2)"><i class="fas fa-arrow-left me-1"></i> Back</button>
                                <button type="button" class="btn btn-bank-primary px-4" onclick="nextStep(2)"><i class="fas fa-arrow-right me-1"></i> Next: Account Credentials</button>
                            </div>
                        </div>
                    </div>

                    <!-- STEP 3: Account Product & Credentials -->
                    <div class="form-step d-none" id="step3">
                        <h5 class="fw-bold text-navy mb-3 border-bottom pb-2"><i class="fas fa-lock text-success me-2"></i>Step 3: Account Product & Credentials</h5>
                        <div class="row g-3">
                            <div class="col-md-6">
                                <label class="form-label small fw-bold">Account Product Type <span class="text-danger">*</span></label>
                                <select name="productId" class="form-select">
                                    <option value="1" selected>Zero Balance Basic Savings (₹0 Min Bal | ₹0 Maintenance Fee)</option>
                                    <option value="2">Regular Savings Account (₹1,000 Min Bal | ₹50/mo Maintenance)</option>
                                    <option value="3">Premium Savings Account (₹10,000 Min Bal | ₹150/mo Maintenance)</option>
                                    <option value="4">Standard Current Account (Commercial | ₹5,000 Min Bal)</option>
                                </select>
                            </div>

                            <div class="col-md-6">
                                <label class="form-label small fw-bold">Home Branch Choice</label>
                                <select name="branchId" class="form-select">
                                    <option value="1">Civil Lines Main Branch (BCBI0000001)</option>
                                    <option value="2">Izzatnagar Branch (BCBI0000002)</option>
                                    <option value="3">Rajendra Nagar Branch (BCBI0000003)</option>
                                </select>
                            </div>

                            <div class="col-md-6">
                                <label class="form-label small fw-bold">Choose Unique Username <span class="text-danger">*</span></label>
                                <input type="text" id="username" name="username" class="form-control" placeholder="rahulkumar95">
                            </div>

                            <div class="col-md-6">
                                <label class="form-label small fw-bold">Login Password <span class="text-danger">*</span></label>
                                <input type="password" id="password" name="password" class="form-control" placeholder="StrongPass@123" onkeyup="checkPasswordMatch()">
                            </div>

                            <div class="col-md-6">
                                <label class="form-label small fw-bold">Confirm Password <span class="text-danger">*</span></label>
                                <input type="password" id="confirmPassword" class="form-control" placeholder="Repeat Password" onkeyup="checkPasswordMatch()">
                                <div id="passwordMatchMsg" class="small mt-1"></div>
                            </div>

                            <div class="col-12">
                                <div class="alert alert-light border small text-muted">
                                    <i class="fas fa-info-circle text-primary me-1"></i> Account opening is <strong>Zero Initial Balance (₹ 0.00)</strong>. Your account balance remains 0 until funds are explicitly deposited.
                                </div>
                            </div>

                            <div class="col-12">
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" id="termsCheck" checked>
                                    <label class="form-check-label small text-muted" for="termsCheck">
                                        I confirm that the details provided are accurate and I agree to the <a href="#" class="text-decoration-none">Banking Terms & Conditions</a> and KYC policy.
                                    </label>
                                </div>
                            </div>

                            <div class="col-12 d-flex justify-content-between mt-4">
                                <button type="button" class="btn btn-outline-secondary px-4" onclick="prevStep(3)"><i class="fas fa-arrow-left me-1"></i> Back</button>
                                <button type="submit" class="btn btn-bank-gold px-5 py-2 fw-bold" id="submitBtn"><i class="fas fa-check-circle me-2"></i> Submit & Open Account</button>
                            </div>
                        </div>
                    </div>
                </form>

                <div class="text-center mt-4 border-top pt-3">
                    <span class="small text-muted">Already registered for Internet Banking? </span>
                    <a href="${pageContext.request.contextPath}/login" class="fw-bold text-navy text-decoration-none">Login to Account</a>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Animated Banking Loading Overlay (2-3 Seconds Simulated Processing) -->
<div class="loading-overlay" id="loadingOverlay">
    <div class="loading-card">
        <div class="bank-spinner"></div>
        <h4 class="fw-bold text-navy mb-1">Creating Your Bank Account</h4>
        <p class="small text-muted mb-3">BAREILLY CITY BANK OF INDIA</p>

        <div class="progress mb-3" style="height: 8px;">
            <div class="progress-bar bg-warning progress-bar-striped progress-bar-animated" id="loadingProgressBar" style="width: 10%;"></div>
        </div>

        <div class="loading-step-list" id="loadingStepList">
            <div class="loading-step-item active" id="lStep1"><i class="fas fa-circle-notch fa-spin me-2"></i> Validating personal and contact details...</div>
            <div class="loading-step-item" id="lStep2"><i class="fas fa-circle me-2"></i> Verifying Aadhaar & PAN identity records...</div>
            <div class="loading-step-item" id="lStep3"><i class="fas fa-circle me-2"></i> Initializing Double-Entry General Ledger...</div>
            <div class="loading-step-item" id="lStep4"><i class="fas fa-circle me-2"></i> Allocating Account Number & Setting Zero Initial Balance (₹ 0.00)...</div>
            <div class="loading-step-item" id="lStep5"><i class="fas fa-circle me-2"></i> Account Created! Preparing Dashboard...</div>
        </div>
    </div>
</div>

<script>
let currentStep = 1;

function showStep(step) {
    document.querySelectorAll('.form-step').forEach(s => s.classList.add('d-none'));
    document.getElementById('step' + step).classList.remove('d-none');

    // Update Stepper
    for (let i = 1; i <= 3; i++) {
        let indicator = document.getElementById('stepIndicator' + i);
        if (i < step) {
            indicator.className = 'stepper-item completed';
        } else if (i === step) {
            indicator.className = 'stepper-item active';
        } else {
            indicator.className = 'stepper-item';
        }
    }

    let bar = document.getElementById('stepperBar');
    if (step === 1) bar.style.width = '0%';
    else if (step === 2) bar.style.width = '40%';
    else if (step === 3) bar.style.width = '80%';

    currentStep = step;
    window.scrollTo({ top: 100, behavior: 'smooth' });
}

function nextStep(fromStep) {
    if (validateStep(fromStep)) {
        showStep(fromStep + 1);
    }
}

function prevStep(fromStep) {
    showStep(fromStep - 1);
}

function validateStep(step) {
    let alertBox = document.getElementById('formAlertBox');
    alertBox.innerHTML = '';

    if (step === 1) {
        let fn = document.getElementById('firstName').value.trim();
        let ln = document.getElementById('lastName').value.trim();
        let mobile = document.getElementById('mobile').value.trim();
        let email = document.getElementById('email').value.trim();

        if (!fn || !ln) {
            showAlert('Please enter your First Name and Last Name.');
            return false;
        }
        if (!/^[6-9]\d{9}$/.test(mobile)) {
            showAlert('Please enter a valid 10-digit Indian mobile number.');
            return false;
        }
        if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
            showAlert('Please enter a valid email address.');
            return false;
        }
    } else if (step === 2) {
        let aadhaar = document.getElementById('aadhaar').value.trim();
        let pan = document.getElementById('pan').value.trim();
        let address = document.getElementById('address').value.trim();

        if (!/^\d{12}$/.test(aadhaar)) {
            showAlert('Please enter a valid 12-digit Aadhaar number.');
            return false;
        }
        if (!/^[A-Za-z]{5}[0-9]{4}[A-Za-z]{1}$/.test(pan)) {
            showAlert('Please enter a valid 10-character PAN number (e.g. ABCDE1234F).');
            return false;
        }
        if (!address) {
            showAlert('Please enter your Residential Address.');
            return false;
        }
    } else if (step === 3) {
        let username = document.getElementById('username').value.trim();
        let p = document.getElementById('password').value;
        let cp = document.getElementById('confirmPassword').value;
        let terms = document.getElementById('termsCheck').checked;

        if (!username || username.length < 4) {
            showAlert('Please enter a unique username (at least 4 characters).');
            return false;
        }
        if (!p) {
            showAlert('Please enter a password.');
            return false;
        }
        if (p !== cp) {
            showAlert('Password and Confirm Password do not match.');
            return false;
        }
        if (!terms) {
            showAlert('Please agree to the Banking Terms & Conditions.');
            return false;
        }
    }
    return true;
}

function checkPasswordMatch() {
    let p = document.getElementById('password').value;
    let cp = document.getElementById('confirmPassword').value;
    let msg = document.getElementById('passwordMatchMsg');

    if (!cp) {
        msg.innerText = '';
        return;
    }

    if (p === cp) {
        msg.innerHTML = '<span class="text-success"><i class="fas fa-check-circle me-1"></i> Passwords match!</span>';
    } else {
        msg.innerHTML = '<span class="text-danger"><i class="fas fa-times-circle me-1"></i> Passwords do not match.</span>';
    }
}

function showAlert(message) {
    let alertBox = document.getElementById('formAlertBox');
    alertBox.innerHTML = '<div class="alert alert-danger mb-3"><i class="fas fa-exclamation-circle me-2"></i>' + message + '</div>';
    window.scrollTo({ top: 150, behavior: 'smooth' });
}

// Multi-step Submit & 2-3 Second Loading Animation
document.getElementById('multiStepForm').addEventListener('submit', function (e) {
    e.preventDefault();

    if (!validateStep(1)) { showStep(1); return; }
    if (!validateStep(2)) { showStep(2); return; }
    if (!validateStep(3)) { return; }

    const form = this;
    const overlay = document.getElementById('loadingOverlay');
    const pBar = document.getElementById('loadingProgressBar');

    // Show Full-Screen Loading Overlay
    overlay.style.display = 'flex';

    // Step-by-step progress animation over 2.8 - 3.2 seconds
    setTimeout(() => {
        pBar.style.width = '30%';
        updateLoadingItem('lStep1', 'done');
        updateLoadingItem('lStep2', 'active');
    }, 600);

    setTimeout(() => {
        pBar.style.width = '55%';
        updateLoadingItem('lStep2', 'done');
        updateLoadingItem('lStep3', 'active');
    }, 1300);

    setTimeout(() => {
        pBar.style.width = '80%';
        updateLoadingItem('lStep3', 'done');
        updateLoadingItem('lStep4', 'active');
    }, 2000);

    setTimeout(() => {
        pBar.style.width = '100%';
        updateLoadingItem('lStep4', 'done');
        updateLoadingItem('lStep5', 'done');

        // Submit form via Fetch API after simulated processing
        const formData = new FormData(form);
        fetch(form.action, {
            method: 'POST',
            headers: { 'X-Requested-With': 'XMLHttpRequest' },
            body: new URLSearchParams(formData)
        })
        .then(res => res.json())
        .then(data => {
            if (data.success) {
                setTimeout(() => {
                    window.location.href = data.data || '${pageContext.request.contextPath}/customer/dashboard';
                }, 600);
            } else {
                overlay.style.display = 'none';
                showAlert(data.message || 'Registration failed.');
            }
        })
        .catch(err => {
            overlay.style.display = 'none';
            showAlert('Server request failed. Please check inputs.');
        });

    }, 2800);
});

function updateLoadingItem(id, state) {
    let item = document.getElementById(id);
    if (!item) return;

    if (state === 'done') {
        item.className = 'loading-step-item done';
        item.innerHTML = '<i class="fas fa-check-circle text-success me-2"></i>' + item.innerText;
    } else if (state === 'active') {
        item.className = 'loading-step-item active';
        item.innerHTML = '<i class="fas fa-spinner fa-spin text-warning me-2"></i>' + item.innerText;
    }
}
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
