package com.example.ui

import com.example.data.CollectibleEntity
import com.example.data.HelpTopic
import com.example.data.TokenBalanceEntity
import com.example.data.TransactionEntity
import com.example.data.WalletEntity

sealed class AppScreen {
    object Onboarding : AppScreen()
    data class SeedBackup(val wallet: WalletEntity, val isNew: Boolean) : AppScreen()
    object Dashboard : AppScreen()
    object SendFlow : AppScreen()
    object ReceiveModal : AppScreen()
    object DepositModal : AppScreen()
    data class TokenManage(val token: TokenBalanceEntity) : AppScreen()
    data class TxDetails(val tx: TransactionEntity) : AppScreen()
    data class HelpModal(val topic: HelpTopic) : AppScreen()
}
