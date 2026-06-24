package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wallets")
data class WalletEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val address: String,
    val seedPhrase: String,
    val isSelected: Boolean = false,
    val network: String = "Solana Mainnet-Beta"
)

@Entity(tableName = "token_balances")
data class TokenBalanceEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val walletAddress: String,
    val symbol: String,
    val name: String,
    val balance: Double,
    val priceUsd: Double,
    val change24h: Float,
    val iconColorHex: String
)

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val walletAddress: String,
    val type: String, // "RECEIVE", "SEND", "SWAP", "DEPOSIT"
    val tokenSymbol: String,
    val amount: Double,
    val counterpartAddress: String,
    val status: String = "CONFIRMED", // "CONFIRMED", "PENDING", "FAILED"
    val timestamp: Long = System.currentTimeMillis(),
    val signatureHash: String,
    val feeSol: Double = 0.000005
)

@Entity(tableName = "collectibles")
data class CollectibleEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val walletAddress: String,
    val title: String,
    val collectionName: String,
    val floorPriceSol: Double,
    val imageResName: String
)

data class WalletContact(
    val name: String,
    val address: String,
    val tag: String,
    val initial: String
)

val DefaultContacts = listOf(
    WalletContact("Anatoly Yakovenko", "7TolyKx9bMqZ...8q3v", "Solana Core", "A"),
    WalletContact("Vitalik Buterin", "0xVit4l1k...e81a", "External", "V"),
    WalletContact("Staking Vault", "Stake999x...pool1", "DeFi Yield", "S"),
    WalletContact("Personal Cold Storage", "Gho5tW4ll3t...vault", "Vault", "P"),
    WalletContact("Magic Eden", "M4g1cEd3n...nft", "Marketplace", "M")
)

data class HelpTopic(
    val id: String,
    val title: String,
    val shortSummary: String,
    val detailedContent: String
)

val HelpTopicsList = listOf(
    HelpTopic(
        id = "seed_phrase",
        title = "Secret Recovery Phrase",
        shortSummary = "12 or 24 words that act as the master cryptographic key to your wallet.",
        detailedContent = "A Secret Recovery Phrase is generated from industry standards like BIP39. Anyone who possesses these exact words in this sequence has full control over the funds associated with the wallet across any compatible decentralized application. Store your recovery phrase securely offline."
    ),
    HelpTopic(
        id = "gas_fee",
        title = "Network Gas Fees",
        shortSummary = "The computational fee paid to network validators to finalize your transfer.",
        detailedContent = "On the Solana blockchain, transaction fees are exceptionally low (typically ~0.000005 SOL). Validators confirm token ownership and permanently commit transfer instructions to the distributed ledger."
    ),
    HelpTopic(
        id = "signature",
        title = "Transaction Signature Hash",
        shortSummary = "A unique cryptographic identifier proving transaction execution.",
        detailedContent = "When you authorize a transaction with your private key, a unique alphanumeric signature is recorded on the network. You can inspect this signature on public block explorers to verify confirmation status and block height."
    ),
    HelpTopic(
        id = "slippage",
        title = "Price Slippage Tolerance",
        shortSummary = "The maximum acceptable price movement during a decentralized token swap.",
        detailedContent = "When exchanging tokens on liquidity pools or DEX aggregators, market prices can fluctuate slightly between authorization and block inclusion. Setting a reasonable slippage tolerance protects trades against volatile price swings."
    )
)

val HelpTopics = HelpTopicsList
