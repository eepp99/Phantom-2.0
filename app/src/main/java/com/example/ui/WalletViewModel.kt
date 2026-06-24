package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.CollectibleEntity
import com.example.data.HelpTopic
import com.example.data.TokenBalanceEntity
import com.example.data.TransactionEntity
import com.example.data.WalletEntity
import com.example.data.WalletRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class DashboardState(
    val wallets: List<WalletEntity> = emptyList(),
    val currentWallet: WalletEntity? = null,
    val tokens: List<TokenBalanceEntity> = emptyList(),
    val transactions: List<TransactionEntity> = emptyList(),
    val collectibles: List<CollectibleEntity> = emptyList(),
    val totalBalanceUsd: Double = 0.0,
    val totalChangePercent24h: Float = 0.0f
)

class WalletViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val repository = WalletRepository(db)

    private val _activeScreen = MutableStateFlow<AppScreen>(AppScreen.Onboarding)
    val activeScreen: StateFlow<AppScreen> = _activeScreen

    private val _activeTab = MutableStateFlow(0) // 0: Tokens, 1: NFTs, 2: Swap, 3: Activity, 4: Settings
    val activeTab: StateFlow<Int> = _activeTab

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage

    private val _isBroadcasting = MutableStateFlow(false)
    val isBroadcasting: StateFlow<Boolean> = _isBroadcasting

    val dashboardState: StateFlow<DashboardState> = repository.allWallets.flatMapLatest { wallets ->
        val selected = wallets.find { it.isSelected } ?: wallets.firstOrNull()
        if (selected == null) {
            flowOf(DashboardState())
        } else {
            combine(
                repository.getTokens(selected.address),
                repository.getTransactions(selected.address),
                repository.getCollectibles(selected.address)
            ) { tokens, txs, nfts ->
                val totalUsd = tokens.sumOf { it.balance * it.priceUsd }
                DashboardState(
                    wallets = wallets,
                    currentWallet = selected,
                    tokens = tokens,
                    transactions = txs,
                    collectibles = nfts,
                    totalBalanceUsd = totalUsd,
                    totalChangePercent24h = +5.84f
                )
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DashboardState())

    private var cleanSeeded = false

    init {
        viewModelScope.launch {
            repository.allWallets.collect { wallets ->
                if (wallets.isNotEmpty()) {
                    if (!cleanSeeded) {
                        cleanSeeded = true
                        repository.seedIfEmptyForAllWallets(wallets)
                    }
                    if (_activeScreen.value is AppScreen.Onboarding) {
                        _activeScreen.value = AppScreen.Dashboard
                    }
                }
            }
        }
    }

    fun showToast(msg: String) {
        viewModelScope.launch {
            _toastMessage.value = msg
            delay(2500)
            _toastMessage.value = null
        }
    }

    fun setTab(tabIndex: Int) {
        _activeTab.value = tabIndex
    }

    fun navigateTo(screen: AppScreen) {
        _activeScreen.value = screen
    }

    fun createAccount(name: String = "Main Wallet") {
        viewModelScope.launch {
            val wallet = repository.createNewWallet(name)
            _activeScreen.value = AppScreen.SeedBackup(wallet, isNew = true)
        }
    }

    fun importExistingAccount(seedPhrase: String) {
        viewModelScope.launch {
            repository.createNewWallet("Imported Wallet", customSeed = seedPhrase.ifBlank { WalletRepository.generateSeedPhrase() })
            showToast("Wallet imported successfully")
            _activeScreen.value = AppScreen.Dashboard
        }
    }

    fun selectAccount(walletId: Int) {
        viewModelScope.launch {
            repository.selectWallet(walletId)
            showToast("Switched active wallet")
        }
    }

    fun finishUsernameSetup(wallet: WalletEntity, username: String) {
        viewModelScope.launch {
            val formattedName = if (username.startsWith("@")) username else "@$username"
            repository.renameWallet(wallet, formattedName)
            _activeScreen.value = AppScreen.Dashboard
            showToast("Welcome $formattedName")
        }
    }

    fun depositAssets(symbol: String, amount: Double) {
        val current = dashboardState.value.currentWallet ?: return
        viewModelScope.launch {
            repository.requestDeposit(current.address, symbol, amount)
            showToast("Deposited +$amount $symbol")
        }
    }

    fun setTokenManualBalance(symbol: String, newBalance: Double) {
        val current = dashboardState.value.currentWallet ?: return
        viewModelScope.launch {
            repository.setTokenManualBalance(current.address, symbol, newBalance)
            showToast("Updated $symbol balance to $newBalance")
        }
    }

    fun sendTokens(symbol: String, amount: Double, destAddress: String) {
        val current = dashboardState.value.currentWallet ?: return
        viewModelScope.launch {
            _isBroadcasting.value = true
            delay(800)
            val success = repository.sendToken(current.address, symbol, amount, destAddress)
            _isBroadcasting.value = false
            if (success) {
                showToast("Sent $amount $symbol Confirmed!")
                _activeScreen.value = AppScreen.Dashboard
            } else {
                showToast("Insufficient balance or invalid token")
            }
        }
    }

    fun performSwap(fromSymbol: String, toSymbol: String, fromAmount: Double, toAmount: Double) {
        val current = dashboardState.value.currentWallet ?: return
        viewModelScope.launch {
            _isBroadcasting.value = true
            delay(800)
            val success = repository.swapToken(current.address, fromSymbol, toSymbol, fromAmount, toAmount)
            _isBroadcasting.value = false
            if (success) {
                showToast("Swapped $fromAmount $fromSymbol to $toAmount $toSymbol")
            } else {
                showToast("Insufficient $fromSymbol balance")
            }
        }
    }

    fun resetWalletData() {
        viewModelScope.launch {
            repository.resetAllAndCreateFresh()
            showToast("Portfolio reset to default state")
            _activeScreen.value = AppScreen.Dashboard
        }
    }
}
