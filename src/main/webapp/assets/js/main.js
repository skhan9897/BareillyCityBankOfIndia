/* BAREILLY CITY BANK OF INDIA - Main JavaScript */

document.addEventListener('DOMContentLoaded', function () {
    // Ensure global loading overlay element exists in DOM
    ensureGlobalLoadingOverlay();

    // Auto-attach AJAX & Loading Overlay handlers to all AJAX post forms
    const forms = document.querySelectorAll('form[data-ajax="true"]');
    forms.forEach(form => {
        form.addEventListener('submit', function (e) {
            e.preventDefault();
            handleBankingFormSubmit(form);
        });
    });
});

function ensureGlobalLoadingOverlay() {
    if (document.getElementById('globalLoadingOverlay')) return;

    const div = document.createElement('div');
    div.id = 'globalLoadingOverlay';
    div.className = 'loading-overlay';
    div.innerHTML = `
        <div class="loading-card">
            <div class="bank-spinner"></div>
            <h4 class="fw-bold text-navy mb-1" id="globalLoadingTitle">Processing Banking Request</h4>
            <p class="small text-muted mb-3">BAREILLY CITY BANK OF INDIA</p>

            <div class="progress mb-3" style="height: 8px;">
                <div class="progress-bar bg-warning progress-bar-striped progress-bar-animated" id="globalLoadingProgressBar" style="width: 10%;"></div>
            </div>

            <div class="loading-step-list" id="globalLoadingStepList"></div>
        </div>
    `;
    document.body.appendChild(div);
}

function handleBankingFormSubmit(form) {
    const action = form.action || '';
    const formData = new FormData(form);
    const alertBox = form.querySelector('.form-alert') || createAlertBox(form);

    // Determine Title and Steps based on Form Action
    let title = form.dataset.loadingTitle || "Processing Request";
    let steps = [];

    if (action.includes('/transfer')) {
        title = "Processing Instant Fund Transfer";
        steps = [
            "Validating source account available balance...",
            "Checking daily transfer limit & idempotency key...",
            "Posting double-entry general ledger journal...",
            "Updating ledger balances & issuing reference ID..."
        ];
    } else if (action.includes('/apply-loan')) {
        title = "Processing Loan Application";
        steps = [
            "Verifying customer credit profile & eligibility score...",
            "Checking monthly income-to-EMI debt ratio...",
            "Submitting application to Loan Officer queue..."
        ];
    } else if (action.includes('/kyc')) {
        title = "Submitting Identity Documents";
        steps = [
            "Validating document format & identity details...",
            "Masking sensitive Aadhaar & PAN numbers...",
            "Submitting to branch verification queue..."
        ];
    } else if (action.includes('/create-fd')) {
        title = "Creating Fixed Deposit Account";
        steps = [
            "Calculating compound interest & maturity returns...",
            "Debiting principal from savings account...",
            "Issuing FD Certificate Number..."
        ];
    } else if (action.includes('/create-rd')) {
        title = "Creating Recurring Deposit Account";
        steps = [
            "Calculating installment maturity schedule...",
            "Debiting first monthly installment from savings...",
            "Generating RD Account Reference..."
        ];
    } else if (action.includes('/login') || action.includes('/admin-login')) {
        title = "Authenticating Portal Access";
        steps = [
            "Validating credentials & BCrypt password hash...",
            "Checking 2FA security challenge & rate limit...",
            "Regenerating secure session & opening portal..."
        ];
    } else if (action.includes('/create-service-request')) {
        title = "Logging Service Request";
        steps = [
            "Categorizing customer service ticket...",
            "Assigning tracking reference ID..."
        ];
    } else if (action.includes('/create-account')) {
        title = "Opening Additional Account";
        steps = [
            "Verifying customer eligibility & branch code...",
            "Initializing account ledger & account number...",
            "Account created with ₹ 0.00 initial balance..."
        ];
    } else {
        steps = [
            "Validating input parameters & security token...",
            "Communicating with core banking engine...",
            "Processing transaction request..."
        ];
    }

    // Trigger 2.5 - 2.8 second animated loading overlay
    showBankingLoading(title, steps, () => {
        fetch(action, {
            method: 'POST',
            headers: { 'X-Requested-With': 'XMLHttpRequest' },
            body: new URLSearchParams(formData)
        })
        .then(response => response.json())
        .then(data => {
            hideBankingLoading();
            if (data.success) {
                alertBox.innerHTML = `<div class="alert alert-success"><i class="fas fa-check-circle me-2"></i> ${data.message}</div>`;
                let targetUrl = (typeof data.data === 'string') ? data.data : (data.data && data.data.redirect ? data.data.redirect : null);
                if (targetUrl) {
                    setTimeout(() => { window.location.href = targetUrl; }, 600);
                } else {
                    setTimeout(() => { window.location.reload(); }, 800);
                }
            } else {
                alertBox.innerHTML = `<div class="alert alert-danger"><i class="fas fa-exclamation-circle me-2"></i> ${data.message}</div>`;
            }
        })
        .catch(err => {
            hideBankingLoading();
            alertBox.innerHTML = `<div class="alert alert-danger"><i class="fas fa-exclamation-triangle me-2"></i> Request failed. Please check inputs.</div>`;
        });
    });
}

function showBankingLoading(title, steps, onComplete) {
    ensureGlobalLoadingOverlay();

    const overlay = document.getElementById('globalLoadingOverlay');
    const titleEl = document.getElementById('globalLoadingTitle');
    const pBar = document.getElementById('globalLoadingProgressBar');
    const stepList = document.getElementById('globalLoadingStepList');

    titleEl.innerText = title;
    pBar.style.width = '10%';
    stepList.innerHTML = '';

    // Render Step List Items
    steps.forEach((stepText, idx) => {
        const item = document.createElement('div');
        item.id = 'gStep_' + idx;
        item.className = 'loading-step-item' + (idx === 0 ? ' active' : '');
        item.innerHTML = (idx === 0 ? '<i class="fas fa-spinner fa-spin text-warning me-2"></i>' : '<i class="fas fa-circle me-2"></i>') + stepText;
        stepList.appendChild(item);
    });

    overlay.style.display = 'flex';

    // Animate steps progressively over 2.4 - 2.8 seconds
    const intervalMs = Math.floor(2200 / steps.length);

    steps.forEach((_, idx) => {
        setTimeout(() => {
            let progress = Math.min(100, Math.floor(((idx + 1) / steps.length) * 100));
            pBar.style.width = progress + '%';

            // Mark current done
            const curr = document.getElementById('gStep_' + idx);
            if (curr) {
                curr.className = 'loading-step-item done';
                curr.innerHTML = '<i class="fas fa-check-circle text-success me-2"></i>' + steps[idx];
            }

            // Mark next active
            const next = document.getElementById('gStep_' + (idx + 1));
            if (next) {
                next.className = 'loading-step-item active';
                next.innerHTML = '<i class="fas fa-spinner fa-spin text-warning me-2"></i>' + steps[idx + 1];
            }
        }, (idx + 1) * intervalMs);
    });

    // Execute completion after full animation
    setTimeout(() => {
        if (onComplete) onComplete();
    }, steps.length * intervalMs + 300);
}

function hideBankingLoading() {
    const overlay = document.getElementById('globalLoadingOverlay');
    if (overlay) overlay.style.display = 'none';
}

function triggerQuickActionLoading(title, steps, postUrl, bodyParams) {
    showBankingLoading(title, steps, () => {
        fetch(postUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'X-Requested-With': 'XMLHttpRequest'
            },
            body: bodyParams
        })
        .then(r => r.json())
        .then(d => {
            hideBankingLoading();
            alert(d.message);
            window.location.reload();
        })
        .catch(err => {
            hideBankingLoading();
            alert('Operation failed. Please try again.');
        });
    });
}

function createAlertBox(form) {
    const div = document.createElement('div');
    div.className = 'form-alert my-2';
    form.insertBefore(div, form.firstChild);
    return div;
}

function formatCurrency(amount) {
    return new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR' }).format(amount);
}
