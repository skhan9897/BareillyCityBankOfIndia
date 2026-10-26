package com.bank.bareillycitybankofindia.app

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bank.bareillycitybankofindia.app.data.api.BankRepository
import com.bank.bareillycitybankofindia.app.data.model.*
import com.bank.bareillycitybankofindia.app.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BareillyBankTheme {
                MainBankingAppNav()
            }
        }
    }
}

enum class Screen {
    SPLASH, LOGIN, REGISTER, DASHBOARD, TRANSFER, PASSBOOK, VIRTUAL_CARD, NOTIFICATIONS
}

@Composable
fun BankTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.DarkGray) },
        leadingIcon = leadingIcon,
        visualTransformation = visualTransformation,
        singleLine = singleLine,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = BankNavy,
            unfocusedTextColor = BankNavy,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedLabelColor = BankNavy,
            unfocusedLabelColor = Color.DarkGray,
            focusedBorderColor = BankNavy,
            unfocusedBorderColor = Color(0xFFCBD5E1)
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainBankingAppNav() {
    var currentScreen by remember { mutableStateOf(Screen.SPLASH) }
    var loggedInCustomer by remember { mutableStateOf<Customer?>(null) }
    var accountsList by remember { mutableStateOf<List<Account>>(emptyList()) }
    var transactionsList by remember { mutableStateOf<List<Transaction>>(emptyList()) }
    var notificationsList by remember { mutableStateOf<List<BankNotification>>(emptyList()) }
    var virtualCard by remember { mutableStateOf<VirtualCard?>(null) }

    val repository = BankRepository.instance
    val scope = rememberCoroutineScope()

    fun refreshData() {
        scope.launch {
            loggedInCustomer = repository.getCustomerProfile()
            accountsList = repository.getAccounts()
            transactionsList = repository.getTransactions()
            notificationsList = repository.getNotifications()
            virtualCard = repository.getVirtualCard()
        }
    }

    LaunchedEffect(Unit) {
        refreshData()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when (currentScreen) {
            Screen.SPLASH -> SplashScreen { currentScreen = Screen.LOGIN }
            Screen.LOGIN -> LoginScreen(
                onLoginSuccess = {
                    refreshData()
                    currentScreen = Screen.DASHBOARD
                },
                onNavigateRegister = { currentScreen = Screen.REGISTER }
            )
            Screen.REGISTER -> RegisterScreen(
                onRegisterSuccess = {
                    refreshData()
                    currentScreen = Screen.DASHBOARD
                },
                onBackToLogin = { currentScreen = Screen.LOGIN }
            )
            Screen.DASHBOARD -> DashboardScreen(
                customer = loggedInCustomer,
                accounts = accountsList,
                transactions = transactionsList,
                notifications = notificationsList,
                card = virtualCard,
                onNavigate = { screen ->
                    refreshData()
                    currentScreen = screen
                },
                onLogout = {
                    currentScreen = Screen.LOGIN
                }
            )
            Screen.TRANSFER -> TransferScreen(
                accounts = accountsList,
                onTransferComplete = {
                    refreshData()
                    currentScreen = Screen.DASHBOARD
                },
                onBack = { currentScreen = Screen.DASHBOARD }
            )
            Screen.PASSBOOK -> PassbookScreen(
                transactions = transactionsList,
                onBack = { currentScreen = Screen.DASHBOARD }
            )
            Screen.VIRTUAL_CARD -> VirtualCardScreen(
                card = virtualCard,
                customer = loggedInCustomer,
                onBack = { currentScreen = Screen.DASHBOARD }
            )
            Screen.NOTIFICATIONS -> NotificationsScreen(
                notifications = notificationsList,
                onBack = { currentScreen = Screen.DASHBOARD }
            )
        }
    }
}

// -------------------------------------------------------------
// 1. SPLASH SCREEN (2.5 Seconds Animated App Launch)
// -------------------------------------------------------------
@Composable
fun SplashScreen(onComplete: () -> Unit) {
    var progress by remember { mutableFloatStateOf(0.15f) }
    var statusText by remember { mutableStateOf("Initializing Safe & Secure Banking...") }

    LaunchedEffect(Unit) {
        delay(600)
        progress = 0.45f
        statusText = "Connecting to Core Ledger Server..."
        delay(700)
        progress = 0.80f
        statusText = "Verifying Security Tokens & Encrypted Channel..."
        delay(700)
        progress = 1.0f
        statusText = "Welcome to Bareilly City Bank!"
        delay(500)
        onComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(BankNavyDark, BankNavy, BankBlue)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .background(BankNavy)
                    .border(3.dp, BankGold, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AccountBalance,
                    contentDescription = "Bank Emblem Logo",
                    tint = BankGold,
                    modifier = Modifier.size(60.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "BAREILLY CITY BANK OF INDIA",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                letterSpacing = 1.sp
            )

            Text(
                text = "YOUR TRUST OUR STRENGTH",
                color = BankGold,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .width(220.dp)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = BankGold,
                trackColor = Color.White.copy(alpha = 0.2f),
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = statusText,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

// -------------------------------------------------------------
// 2. LOGIN SCREEN
// -------------------------------------------------------------
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateRegister: () -> Unit
) {
    var username by remember { mutableStateOf("customer1") }
    var password by remember { mutableStateOf("Customer@123") }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BankLightBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .background(BankNavy),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Lock Icon",
                            tint = BankGold,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Customer Internet Banking",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = BankNavy,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "BAREILLY CITY BANK OF INDIA",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 2.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    BankTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = "Customer Username",
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = BankNavy) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    BankTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = "Login Password",
                        leadingIcon = { Icon(Icons.Default.Key, contentDescription = null, tint = BankGold) },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            isLoading = true
                            scope.launch {
                                val res = BankRepository.instance.login(username, password)
                                isLoading = false
                                res.onSuccess {
                                    Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT).show()
                                    onLoginSuccess()
                                }.onFailure { err ->
                                    Toast.makeText(context, err.message, Toast.LENGTH_LONG).show()
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BankNavy),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Icon(Icons.Default.Login, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Login Securely", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("New Customer? ", fontSize = 13.sp, color = Color.Gray)
                        Text(
                            text = "Open Zero Balance Account",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = BankBlue,
                            modifier = Modifier.clickable { onNavigateRegister() }
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 3. REGISTER SCREEN (Step-by-Step Account Opening)
// -------------------------------------------------------------
@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onBackToLogin: () -> Unit
) {
    var step by remember { mutableIntStateOf(1) }
    var firstName by remember { mutableStateOf("Rahul") }
    var lastName by remember { mutableStateOf("Kumar") }
    var mobile by remember { mutableStateOf("9876543210") }
    var email by remember { mutableStateOf("rahul.kumar@gmail.com") }
    var aadhaar by remember { mutableStateOf("999988887777") }
    var pan by remember { mutableStateOf("ABCDE1234F") }
    var address by remember { mutableStateOf("Civil Lines, Bareilly") }
    var username by remember { mutableStateOf("rahulkumar95") }
    var password by remember { mutableStateOf("StrongPass@123") }

    var isProcessing by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize().background(BankLightBg)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            IconButton(onClick = onBackToLogin) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = BankNavy)
            }

            Text("Open Bank Account Online", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = BankNavy)
            Text("BAREILLY CITY BANK OF INDIA", fontSize = 12.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(16.dp))

            // Stepper Indicator
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                listOf("1. Personal", "2. KYC", "3. Credentials").forEachIndexed { index, title ->
                    val isCurrent = step == index + 1
                    Text(
                        text = title,
                        fontSize = 12.sp,
                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                        color = if (isCurrent) BankNavy else Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    when (step) {
                        1 -> {
                            Text("Step 1: Personal Information", fontWeight = FontWeight.Bold, color = BankNavy)
                            Spacer(modifier = Modifier.height(12.dp))
                            BankTextField(value = firstName, onValueChange = { firstName = it }, label = "First Name", modifier = Modifier.fillMaxWidth())
                            Spacer(modifier = Modifier.height(8.dp))
                            BankTextField(value = lastName, onValueChange = { lastName = it }, label = "Last Name", modifier = Modifier.fillMaxWidth())
                            Spacer(modifier = Modifier.height(8.dp))
                            BankTextField(value = mobile, onValueChange = { mobile = it }, label = "Mobile Number (10 digits)", modifier = Modifier.fillMaxWidth())
                            Spacer(modifier = Modifier.height(8.dp))
                            BankTextField(value = email, onValueChange = { email = it }, label = "Email Address", modifier = Modifier.fillMaxWidth())
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { step = 2 },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = BankNavy)
                            ) {
                                Text("Next: KYC Details")
                            }
                        }
                        2 -> {
                            Text("Step 2: KYC & Address Details", fontWeight = FontWeight.Bold, color = BankNavy)
                            Spacer(modifier = Modifier.height(12.dp))
                            BankTextField(value = aadhaar, onValueChange = { aadhaar = it }, label = "Aadhaar Number (12 digits)", modifier = Modifier.fillMaxWidth())
                            Spacer(modifier = Modifier.height(8.dp))
                            BankTextField(value = pan, onValueChange = { pan = it }, label = "PAN Card Number", modifier = Modifier.fillMaxWidth())
                            Spacer(modifier = Modifier.height(8.dp))
                            BankTextField(value = address, onValueChange = { address = it }, label = "Residential Address", modifier = Modifier.fillMaxWidth())
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                OutlinedButton(onClick = { step = 1 }) { Text("Back") }
                                Button(
                                    onClick = { step = 3 },
                                    colors = ButtonDefaults.buttonColors(containerColor = BankNavy)
                                ) { Text("Next: Account Setup") }
                            }
                        }
                        3 -> {
                            Text("Step 3: Account & Login Setup", fontWeight = FontWeight.Bold, color = BankNavy)
                            Spacer(modifier = Modifier.height(12.dp))
                            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF9E7)), modifier = Modifier.fillMaxWidth()) {
                                Text("Selected Product: Zero Balance Basic Savings Account (₹0 Min Bal & ₹0 Maintenance Fee)", fontSize = 12.sp, color = BankDarkGold, modifier = Modifier.padding(12.dp))
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            BankTextField(value = username, onValueChange = { username = it }, label = "Choose Username", modifier = Modifier.fillMaxWidth())
                            Spacer(modifier = Modifier.height(8.dp))
                            BankTextField(value = password, onValueChange = { password = it }, label = "Choose Password", visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                OutlinedButton(onClick = { step = 2 }) { Text("Back") }
                                Button(
                                    onClick = {
                                        isProcessing = true
                                        scope.launch {
                                            delay(2500) // 2.5s Processing
                                            BankRepository.instance.registerCustomer(username, email, mobile, password, firstName, lastName, aadhaar, pan, address)
                                            isProcessing = false
                                            Toast.makeText(context, "Account Created Successfully!", Toast.LENGTH_LONG).show()
                                            onRegisterSuccess()
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = BankGold),
                                    enabled = !isProcessing
                                ) {
                                    if (isProcessing) {
                                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = BankNavy)
                                    } else {
                                        Text("Submit & Open Account", color = Color.Black, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 4. CUSTOMER DASHBOARD SCREEN
// -------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    customer: Customer?,
    accounts: List<Account>,
    transactions: List<Transaction>,
    notifications: List<BankNotification>,
    card: VirtualCard?,
    onNavigate: (Screen) -> Unit,
    onLogout: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("BAREILLY CITY BANK", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("YOUR TRUST OUR STRENGTH", fontSize = 9.sp, color = BankGold)
                    }
                },
                actions = {
                    IconButton(onClick = { onNavigate(Screen.NOTIFICATIONS) }) {
                        BadgedBox(
                            badge = {
                                if (notifications.isNotEmpty()) {
                                    Badge { Text(notifications.size.toString()) }
                                }
                            }
                        ) {
                            Icon(Icons.Default.Notifications, contentDescription = "Alerts", tint = Color.White)
                        }
                    }
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.Logout, contentDescription = "Logout", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BankNavy)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(BankLightBg)
                .verticalScroll(rememberScrollState())
        ) {
            // Profile Welcome Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BankNavy)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(BankGold),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = (customer?.firstName?.take(1) ?: "A"),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = BankNavy
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Welcome back,", fontSize = 12.sp, color = Color.LightGray)
                                Text(customer?.fullName ?: "Customer", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (customer?.kycStatus == "APPROVED") BankSuccess else BankGold
                        ) {
                            Text(
                                text = "KYC: ${customer?.kycStatus ?: "APPROVED"}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Total Net Worth", fontSize = 11.sp, color = Color.LightGray)
                            Text("₹ ${accounts.firstOrNull()?.availableBalance ?: 25000.00}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = BankGold)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Customer Number", fontSize = 11.sp, color = Color.LightGray)
                            Text(customer?.customerNumber ?: "CUST17926676", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }

            // Quick Banking Action Buttons
            Text("Quick Banking Services", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = BankNavy, modifier = Modifier.padding(horizontal = 16.dp))

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionItem(title = "Send Money", icon = Icons.Default.Send, color = BankNavy, modifier = Modifier.weight(1f)) { onNavigate(Screen.TRANSFER) }
                QuickActionItem(title = "Passbook", icon = Icons.Default.ReceiptLong, color = BankSuccess, modifier = Modifier.weight(1f)) { onNavigate(Screen.PASSBOOK) }
                QuickActionItem(title = "Debit Card", icon = Icons.Default.CreditCard, color = BankBlue, modifier = Modifier.weight(1f)) { onNavigate(Screen.VIRTUAL_CARD) }
                QuickActionItem(title = "SMS Alerts", icon = Icons.Default.Notifications, color = BankDarkGold, modifier = Modifier.weight(1f)) { onNavigate(Screen.NOTIFICATIONS) }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Account Card
            Text("Primary Savings Account", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = BankNavy, modifier = Modifier.padding(horizontal = 16.dp))

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(shape = RoundedCornerShape(6.dp), color = BankNavy.copy(alpha = 0.1f)) {
                            Text("BSBD Savings Account", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BankNavy, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                        }
                        Text("BCBI0000001", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.SemiBold)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("A/c No: ${accounts.firstOrNull()?.accountNumber ?: "1000139854769"}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = BankNavy)

                    Spacer(modifier = Modifier.height(6.dp))

                    Text("Available Balance", fontSize = 11.sp, color = Color.Gray)
                    Text("₹ ${accounts.firstOrNull()?.availableBalance ?: 25000.00}", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = BankSuccess)

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { onNavigate(Screen.TRANSFER) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = BankNavy),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Transfer Money Now")
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Recent Transactions
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Recent Transactions", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = BankNavy)
                Text("View All", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BankBlue, modifier = Modifier.clickable { onNavigate(Screen.PASSBOOK) })
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                transactions.take(3).forEach { tx ->
                    TransactionRow(tx = tx)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun QuickActionItem(title: String, icon: ImageVector, color: Color, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = title, tint = color, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(title, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = BankNavy, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun TransactionRow(tx: Transaction) {
    val isCredit = tx.transactionType == "DEPOSIT"
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (isCredit) BankSuccess.copy(alpha = 0.15f) else BankDanger.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isCredit) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                        contentDescription = null,
                        tint = if (isCredit) BankSuccess else BankDanger,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(tx.description, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BankNavy)
                    Text(tx.transactionRef, fontSize = 10.sp, color = Color.Gray)
                }
            }

            Text(
                text = "${if (isCredit) "+" else "-"} ₹ ${tx.amount}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (isCredit) BankSuccess else BankDanger
            )
        }
    }
}

// -------------------------------------------------------------
// 5. FUND TRANSFER SCREEN (with Payment Processing Animation)
// -------------------------------------------------------------
@Composable
fun TransferScreen(
    accounts: List<Account>,
    onTransferComplete: () -> Unit,
    onBack: () -> Unit
) {
    var toAccount by remember { mutableStateOf("100020003003") }
    var amountStr by remember { mutableStateOf("2500") }
    var description by remember { mutableStateOf("Family Support") }
    var isProcessing by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize().background(BankLightBg)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = BankNavy) }
                Text("Instant Money Transfer", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = BankNavy)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Recipient Details", fontWeight = FontWeight.Bold, color = BankNavy)

                    Spacer(modifier = Modifier.height(12.dp))

                    BankTextField(
                        value = toAccount,
                        onValueChange = { toAccount = it },
                        label = "Recipient Account Number / UPI ID",
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    BankTextField(
                        value = amountStr,
                        onValueChange = { amountStr = it },
                        label = "Transfer Amount (₹)",
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    BankTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = "Remarks / Narration",
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            val amount = amountStr.toDoubleOrNull() ?: 0.0
                            if (amount <= 0) {
                                Toast.makeText(context, "Enter a valid amount", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            isProcessing = true
                            scope.launch {
                                val res = BankRepository.instance.transferFunds(accounts.firstOrNull()?.id ?: 101, toAccount, amount, description)
                                isProcessing = false
                                res.onSuccess {
                                    Toast.makeText(context, "₹ $amount Transferred Successfully!", Toast.LENGTH_LONG).show()
                                    onTransferComplete()
                                }.onFailure { err ->
                                    Toast.makeText(context, err.message, Toast.LENGTH_LONG).show()
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BankNavy),
                        enabled = !isProcessing
                    ) {
                        if (isProcessing) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Posting Ledger Entry...")
                        } else {
                            Icon(Icons.Default.Send, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Transfer Money Now", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 6. PASSBOOK / TRANSACTION HISTORY SCREEN
// -------------------------------------------------------------
@Composable
fun PassbookScreen(transactions: List<Transaction>, onBack: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(BankLightBg)) {
        Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = BankNavy) }
                Text("Passbook & Transaction History", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = BankNavy)
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(transactions) { tx ->
                    TransactionRow(tx = tx)
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 7. VIRTUAL DEBIT CARD SCREEN
// -------------------------------------------------------------
@Composable
fun VirtualCardScreen(card: VirtualCard?, customer: Customer?, onBack: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(BankLightBg)) {
        Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = BankNavy) }
                Text("Virtual Debit Card & UPI", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = BankNavy)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 3D Debit Card Widget
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = BankNavy)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("BAREILLY CITY BANK", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                        Text("VISA DEBIT", fontWeight = FontWeight.Bold, color = BankGold, fontSize = 12.sp)
                    }

                    Text("4532 •••• •••• 9821", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Color.White, fontFamily = FontFamily.Monospace)

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("CARD HOLDER", fontSize = 9.sp, color = Color.LightGray)
                            Text(customer?.fullName ?: "ASGAR KHAN", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Column {
                            Text("EXPIRES", fontSize = 9.sp, color = Color.LightGray)
                            Text("12/28", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 8. SMS ALERTS & NOTIFICATIONS SCREEN
// -------------------------------------------------------------
@Composable
fun NotificationsScreen(notifications: List<BankNotification>, onBack: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(BankLightBg)) {
        Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = BankNavy) }
                Text("SMS Alerts & Credit Notifications", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = BankNavy)
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(notifications) { notif ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(notif.title, fontWeight = FontWeight.Bold, color = BankNavy, fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(notif.message, fontSize = 11.sp, color = Color.DarkGray, fontFamily = FontFamily.Monospace)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(notif.createdAt, fontSize = 9.sp, color = Color.Gray, modifier = Modifier.align(Alignment.End))
                        }
                    }
                }
            }
        }
    }
}
