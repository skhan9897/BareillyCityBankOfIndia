package com.bank.bareillycitybankofindia.app.data.model

data class User(
    val id: Long = 0,
    val username: String = "",
    val email: String = "",
    val mobile: String = "",
    val userType: String = "CUSTOMER",
    val status: String = "ACTIVE"
)

data class Customer(
    val id: Long = 0,
    val userId: Long = 0,
    val customerNumber: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val mobile: String = "",
    val email: String = "",
    val kycStatus: String = "PENDING",
    val addressLine1: String = "",
    val city: String = "Bareilly",
    val state: String = "Uttar Pradesh"
) {
    val fullName: String get() = "$firstName $lastName".trim()
}

data class Account(
    val id: Long = 0,
    val accountNumber: String = "",
    val customerId: Long = 0,
    val productName: String = "Zero Balance Basic Savings Account (BSBD)",
    val accountType: String = "SAVINGS",
    val availableBalance: Double = 0.00,
    val ledgerBalance: Double = 0.00,
    val ifscCode: String = "BCBI0000001",
    val branchName: String = "Civil Lines Main Branch",
    val status: String = "ACTIVE"
)

data class Transaction(
    val id: Long = 0,
    val transactionRef: String = "",
    val fromAccountId: Long? = null,
    val toAccountId: Long? = null,
    val transactionType: String = "TRANSFER",
    val amount: Double = 0.0,
    val status: String = "SUCCESS",
    val description: String = "",
    val createdAt: String = ""
)

data class VirtualCard(
    val id: Long = 0,
    val accountId: Long = 0,
    val cardNumberMasked: String = "4532 •••• •••• 9821",
    val cardType: String = "DEBIT",
    val expiryMonth: Int = 12,
    val expiryYear: Int = 2028,
    val dailyLimit: Double = 50000.0,
    val status: String = "ACTIVE"
)

data class BankNotification(
    val id: Long = 0,
    val title: String = "",
    val message: String = "",
    val type: String = "SUCCESS",
    val createdAt: String = ""
)

data class ApiResponse<T>(
    val success: Boolean = false,
    val message: String = "",
    val data: T? = null
)
