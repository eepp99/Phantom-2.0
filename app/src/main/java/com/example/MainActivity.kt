package com.example

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.AppScreen
import com.example.ui.WalletViewModel
import com.example.ui.components.AccountsBottomSheetModal
import com.example.ui.components.ActivityTabContent
import com.example.ui.components.ChooseUsernameScreen
import com.example.ui.components.CollectiblesTabContent
import com.example.ui.components.EduTopicSheet
import com.example.ui.components.FaucetModalSheet
import com.example.ui.components.OnboardingScreen
import com.example.ui.components.PhantomBottomBar
import com.example.ui.components.PhantomTopHeader
import com.example.ui.components.ReceiveModalSheet
import com.example.ui.components.SeedPhraseBackupScreen
import com.example.data.HelpTopicsList
import com.example.ui.components.SendModalSheet
import com.example.ui.components.SettingsTabContent
import com.example.ui.components.SwapTabContent
import com.example.ui.components.TokenManageModalSheet
import com.example.ui.components.TokensTabContent
import com.example.ui.components.TxDetailsModalSheet
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.PhantomBg
import com.example.ui.theme.PhantomGreen
import com.example.ui.theme.PhantomSurfaceHigh
import com.example.ui.theme.PhantomText

class MainActivity : ComponentActivity() {
    private val viewModel: WalletViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                PhantomAppRoot(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun PhantomAppRoot(viewModel: WalletViewModel) {
    val activeScreen by viewModel.activeScreen.collectAsStateWithLifecycle()
    val activeTab by viewModel.activeTab.collectAsStateWithLifecycle()
    val dashboardState by viewModel.dashboardState.collectAsStateWithLifecycle()
    val toastMessage by viewModel.toastMessage.collectAsStateWithLifecycle()
    val isBroadcasting by viewModel.isBroadcasting.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var activeTopTab by remember { mutableStateOf(0) } // 0: Home, 1: Trade, 2: Explore
    var showAccountsModal by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PhantomBg)
    ) {
        when (val screen = activeScreen) {
            is AppScreen.Onboarding -> {
                OnboardingScreen(
                    onCreateClick = { viewModel.createAccount("Account 1") },
                    onImportClick = { viewModel.importExistingAccount(it) }
                )
            }
            is AppScreen.SeedBackup -> {
                SeedPhraseBackupScreen(
                    wallet = screen.wallet,
                    onConfirmSaved = {
                        if (screen.isNew) {
                            viewModel.navigateTo(AppScreen.ChooseUsername(screen.wallet))
                        } else {
                            viewModel.navigateTo(AppScreen.Dashboard)
                        }
                    },
                    onCopyPhrase = {
                        copyToClipboard(context, screen.wallet.seedPhrase)
                        viewModel.showToast("Recovery phrase copied to clipboard")
                    },
                    onBack = {
                        if (screen.isNew) {
                            viewModel.navigateTo(AppScreen.Onboarding)
                        } else {
                            viewModel.navigateTo(AppScreen.Dashboard)
                        }
                    }
                )
            }
            is AppScreen.ChooseUsername -> {
                ChooseUsernameScreen(
                    wallet = screen.wallet,
                    onContinue = { chosenName ->
                        viewModel.finishUsernameSetup(screen.wallet, chosenName)
                    },
                    onBack = {
                        viewModel.navigateTo(AppScreen.SeedBackup(screen.wallet, isNew = true))
                    }
                )
            }
            else -> {
                // Main Dashboard & Tab Container
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = PhantomBg,
                    topBar = {
                        Column(modifier = Modifier.statusBarsPadding()) {
                            PhantomTopHeader(
                                wallets = dashboardState.wallets,
                                currentWallet = dashboardState.currentWallet,
                                activeTopTab = activeTopTab,
                                onSelectTopTab = { selectedTop ->
                                    activeTopTab = selectedTop
                                    if (selectedTop == 1) {
                                        viewModel.setTab(2) // Switch bottom to Swap when Trade is chosen
                                    } else if (activeTab == 2) {
                                        viewModel.setTab(0)
                                    }
                                },
                                onOpenAccountsModal = { showAccountsModal = true }
                            )
                        }
                    }
                ) { paddingValues ->
                    Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                        if (activeTopTab == 1 || activeTab == 2) {
                            SwapTabContent(
                                tokens = dashboardState.tokens,
                                isBroadcasting = isBroadcasting,
                                onSwap = { from, to, fromAmt, toAmt ->
                                    viewModel.performSwap(from, to, fromAmt, toAmt)
                                },
                                onHelpClick = { topicId ->
                                    HelpTopicsList.find { it.id == topicId }?.let {
                                        viewModel.navigateTo(AppScreen.HelpModal(it))
                                    }
                                }
                            )
                        } else when (activeTab) {
                            0 -> TokensTabContent(
                                activeTopTab = activeTopTab,
                                currentAccountName = dashboardState.currentWallet?.name ?: "Account 1",
                                totalBalanceUsd = dashboardState.totalBalanceUsd,
                                tokens = dashboardState.tokens,
                                onOpenAccountsModal = { showAccountsModal = true },
                                onReceiveClick = { viewModel.navigateTo(AppScreen.ReceiveModal) },
                                onSendClick = { viewModel.navigateTo(AppScreen.SendFlow) },
                                onSwapClick = {
                                    activeTopTab = 1
                                    viewModel.setTab(2)
                                },
                                onFaucetClick = { viewModel.navigateTo(AppScreen.DepositModal) },
                                onTokenClick = { viewModel.navigateTo(AppScreen.TokenManage(it)) }
                            )
                            1 -> CollectiblesTabContent(
                                collectibles = dashboardState.collectibles,
                                onReceiveClick = {
                                    viewModel.depositAssets("SOL", 0.5)
                                    viewModel.showToast("Collectible incoming...")
                                }
                            )
                            3 -> ActivityTabContent(
                                transactions = dashboardState.transactions,
                                onTxClick = { viewModel.navigateTo(AppScreen.TxDetails(it)) }
                            )
                            4 -> SettingsTabContent(
                                currentWallet = dashboardState.currentWallet,
                                onRevealSeed = {
                                    dashboardState.currentWallet?.let {
                                        viewModel.navigateTo(AppScreen.SeedBackup(it, isNew = false))
                                    }
                                },
                                onResetData = { viewModel.resetWalletData() },
                                onHelpTopic = { viewModel.navigateTo(AppScreen.HelpModal(it)) }
                            )
                        }
                    }
                }
            }
        }

        if (showAccountsModal) {
            AccountsBottomSheetModal(
                wallets = dashboardState.wallets,
                onDismiss = { showAccountsModal = false },
                onSelectWallet = { viewModel.selectAccount(it) },
                onCreateWallet = { viewModel.createAccount("Account ${dashboardState.wallets.size + 1}") }
            )
        }


        // Modals Overlay Handling
        when (val screen = activeScreen) {
            is AppScreen.SendFlow -> {
                SendModalSheet(
                    tokens = dashboardState.tokens,
                    isBroadcasting = isBroadcasting,
                    onDismiss = { viewModel.navigateTo(AppScreen.Dashboard) },
                    onSend = { symbol, amount, dest ->
                        viewModel.sendTokens(symbol, amount, dest)
                    }
                )
            }
            is AppScreen.ReceiveModal -> {
                dashboardState.currentWallet?.let { wallet ->
                    ReceiveModalSheet(
                        wallet = wallet,
                        onDismiss = { viewModel.navigateTo(AppScreen.Dashboard) },
                        onCopyAddress = {
                            copyToClipboard(context, wallet.address)
                            viewModel.showToast("Address copied")
                        }
                    )
                }
            }
            is AppScreen.DepositModal -> {
                FaucetModalSheet(
                    onDismiss = { viewModel.navigateTo(AppScreen.Dashboard) },
                    onDrop = { sym, amt ->
                        viewModel.depositAssets(sym, amt)
                    }
                )
            }
            is AppScreen.HelpModal -> {
                EduTopicSheet(
                    topic = screen.topic,
                    onDismiss = { viewModel.navigateTo(AppScreen.Dashboard) }
                )
            }
            is AppScreen.TokenManage -> {
                TokenManageModalSheet(
                    token = screen.token,
                    onDismiss = { viewModel.navigateTo(AppScreen.Dashboard) },
                    onAddAmount = { amt -> viewModel.depositAssets(screen.token.symbol, amt) },
                    onSetBalance = { bal -> viewModel.setTokenManualBalance(screen.token.symbol, bal) }
                )
            }
            is AppScreen.TxDetails -> {
                TxDetailsModalSheet(
                    tx = screen.tx,
                    onDismiss = { viewModel.navigateTo(AppScreen.Dashboard) },
                    onCopy = { str ->
                        copyToClipboard(context, str)
                        viewModel.showToast("Copied to clipboard")
                    }
                )
            }
            else -> {}
        }

        // Floating Toast Notification
        AnimatedVisibility(
            visible = toastMessage != null,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 70.dp)
                .padding(horizontal = 24.dp)
        ) {
            toastMessage?.let { msg ->
                Surface(
                    color = PhantomSurfaceHigh,
                    shape = RoundedCornerShape(24.dp),
                    shadowElevation = 12.dp,
                    border = androidx.compose.foundation.BorderStroke(1.dp, PhantomGreen.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = msg,
                        color = PhantomText,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp)
                    )
                }
            }
        }
    }
}

fun copyToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Phantom", text)
    clipboard.setPrimaryClip(clip)
}
