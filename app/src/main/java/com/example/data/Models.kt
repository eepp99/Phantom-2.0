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

data class CoinCatalogItem(
    val symbol: String,
    val name: String,
    val priceUsd: Double,
    val change24h: Float,
    val iconColorHex: String,
    val category: String = "Solana DeFi"
)

val ALL_SOLANA_COINS = listOf(
    CoinCatalogItem("SOL", "Solana", 145.20, +5.12f, "#14F195", "Layer 1"),
    CoinCatalogItem("USDC", "USD Coin", 1.00, +0.01f, "#2775CA", "Stablecoin"),
    CoinCatalogItem("USDT", "Tether USD", 1.00, -0.02f, "#26A17B", "Stablecoin"),
    CoinCatalogItem("BTC", "Bitcoin (Portal)", 62057.00, -0.08f, "#F7931A", "Wrapped L1"),
    CoinCatalogItem("ETH", "Ethereum (Portal)", 1653.01, +0.29f, "#627EEA", "Wrapped L1"),
    CoinCatalogItem("JUP", "Jupiter", 0.92, +8.40f, "#26C59A", "DEX Aggregator"),
    CoinCatalogItem("BONK", "Bonk", 0.000028, +14.50f, "#E3872D", "Meme"),
    CoinCatalogItem("WIF", "dogwifhat", 2.45, +22.10f, "#C4A482", "Meme"),
    CoinCatalogItem("RAY", "Raydium", 2.10, +4.15f, "#4C52EC", "DEX / AMM"),
    CoinCatalogItem("RENDER", "Render Network", 7.85, +6.30f, "#E51B24", "AI / DePIN"),
    CoinCatalogItem("PYTH", "Pyth Network", 0.38, -1.20f, "#9945FF", "Oracle"),
    CoinCatalogItem("JTO", "Jito", 3.15, +9.80f, "#70C7BA", "Liquid Staking"),
    CoinCatalogItem("ORCA", "Orca", 3.40, +2.10f, "#FAD75A", "DEX / AMM"),
    CoinCatalogItem("HNT", "Helium", 4.80, -0.50f, "#474DFF", "DePIN"),
    CoinCatalogItem("MSOL", "Marinade Staked SOL", 168.40, +5.10f, "#3023AE", "Liquid Staking"),
    CoinCatalogItem("POPCAT", "Popcat", 1.15, +18.40f, "#EAA221", "Meme"),
    CoinCatalogItem("MEW", "cat in a dogs world", 0.0058, +11.20f, "#F5ACB8", "Meme"),
    CoinCatalogItem("DRIFT", "Drift Protocol", 0.55, +3.40f, "#6851FF", "Perp DEX"),
    CoinCatalogItem("TNSR", "Tensor", 0.72, -2.10f, "#27272A", "NFT Marketplace"),
    CoinCatalogItem("SLERF", "Slerf", 0.25, +5.60f, "#5C81A6", "Meme"),
    CoinCatalogItem("SPCX", "SPCX Token", 152.69, +1.60f, "#16161A", "Ecosystem"),
    CoinCatalogItem("KINS", "KINS Sol", 0.0129, +9.88f, "#C4E8F8", "Gaming"),
    CoinCatalogItem("CARDS", "CARDS Protocol", 0.30, +16.96f, "#221D1A", "SocialFi"),
    CoinCatalogItem("BSL", "BankSol Liquidity", 12.50, +4.20f, "#9945FF", "Yield Vault")
)

