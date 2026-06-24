package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class WalletRepository(private val db: AppDatabase) {
    private val walletDao = db.walletDao()
    private val tokenDao = db.tokenDao()
    private val txDao = db.transactionDao()
    private val collectibleDao = db.collectibleDao()

    val allWallets: Flow<List<WalletEntity>> = walletDao.getAllWallets()
    val selectedWallet: Flow<WalletEntity?> = walletDao.getSelectedWallet()

    fun getTokens(address: String): Flow<List<TokenBalanceEntity>> = tokenDao.getTokensForWallet(address)
    fun getTransactions(address: String): Flow<List<TransactionEntity>> = txDao.getTransactionsForWallet(address)
    fun getCollectibles(address: String): Flow<List<CollectibleEntity>> = collectibleDao.getCollectiblesForWallet(address)

    suspend fun selectWallet(walletId: Int) {
        walletDao.deselectAll()
        walletDao.selectWallet(walletId)
    }

    suspend fun createNewWallet(name: String, customSeed: String? = null): WalletEntity {
        val seed = customSeed ?: generateSeedPhrase()
        val address = generateSolanaAddress()
        
        walletDao.deselectAll()
        val wallet = WalletEntity(
            name = name,
            address = address,
            seedPhrase = seed,
            isSelected = true
        )
        walletDao.insertWallet(wallet)
        populateInitialData(address)
        return wallet
    }

    suspend fun resetAllAndCreateFresh() {
        walletDao.deleteAllWallets()
        createNewWallet("Main Wallet")
    }

    private suspend fun populateInitialData(address: String) {
        val tokens = listOf(
            TokenBalanceEntity(walletAddress = address, symbol = "SOL", name = "Solana", balance = 14.52, priceUsd = 142.80, change24h = 6.42f, iconColorHex = "#9945FF"),
            TokenBalanceEntity(walletAddress = address, symbol = "USDC", name = "USD Coin", balance = 450.00, priceUsd = 1.00, change24h = 0.01f, iconColorHex = "#2775CA"),
            TokenBalanceEntity(walletAddress = address, symbol = "BONK", name = "Bonk", balance = 1250000.0, priceUsd = 0.000028, change24h = 18.5f, iconColorHex = "#F5A623"),
            TokenBalanceEntity(walletAddress = address, symbol = "JUP", name = "Jupiter", balance = 85.0, priceUsd = 0.92, change24h = -2.15f, iconColorHex = "#14F195"),
            TokenBalanceEntity(walletAddress = address, symbol = "RAY", name = "Raydium", balance = 30.0, priceUsd = 2.10, change24h = 4.20f, iconColorHex = "#38BDF8")
        )
        tokens.forEach { tokenDao.insertToken(it) }

        val now = System.currentTimeMillis()
        val txs = listOf(
            TransactionEntity(
                walletAddress = address,
                type = "RECEIVE",
                tokenSymbol = "SOL",
                amount = 5.0,
                counterpartAddress = "7TolyKx9bMqZ...8q3v",
                status = "CONFIRMED",
                timestamp = now - 3600_000L * 2,
                signatureHash = "4Kz9qP2m...v8N1x"
            ),
            TransactionEntity(
                walletAddress = address,
                type = "SWAP",
                tokenSymbol = "BONK",
                amount = 500000.0,
                counterpartAddress = "Jupiter Aggregator",
                status = "CONFIRMED",
                timestamp = now - 3600_000L * 14,
                signatureHash = "8mQz...9pL2v"
            ),
            TransactionEntity(
                walletAddress = address,
                type = "SEND",
                tokenSymbol = "USDC",
                amount = 50.0,
                counterpartAddress = "Stake999x...pool1",
                status = "CONFIRMED",
                timestamp = now - 3600_000L * 36,
                signatureHash = "2vPx...7kM9a"
            )
        )
        txs.forEach { txDao.insertTransaction(it) }

        val nfts = listOf(
            CollectibleEntity(
                walletAddress = address,
                title = "Cyber Ghost Ape #409",
                collectionName = "Mad Lads",
                floorPriceSol = 68.5,
                imageResName = "cyber_solana_nft"
            ),
            CollectibleEntity(
                walletAddress = address,
                title = "Phantom Membership Pass",
                collectionName = "Phantom Access Vault",
                floorPriceSol = 1.2,
                imageResName = "phantom_wallet_icon"
            )
        )
        nfts.forEach { collectibleDao.insertCollectible(it) }
    }

    suspend fun sendToken(walletAddr: String, symbol: String, amount: Double, destination: String): Boolean {
        val token = tokenDao.getToken(walletAddr, symbol) ?: return false
        if (token.balance < amount) return false

        tokenDao.addBalance(walletAddr, symbol, -amount)
        val hash = generateRandomSignatureHash()
        val tx = TransactionEntity(
            walletAddress = walletAddr,
            type = "SEND",
            tokenSymbol = symbol,
            amount = amount,
            counterpartAddress = destination,
            status = "CONFIRMED",
            timestamp = System.currentTimeMillis(),
            signatureHash = hash
        )
        txDao.insertTransaction(tx)
        return true
    }

    suspend fun requestDeposit(walletAddr: String, symbol: String, amount: Double) {
        val existing = tokenDao.getToken(walletAddr, symbol)
        if (existing != null) {
            tokenDao.addBalance(walletAddr, symbol, amount)
        } else {
            val price = when (symbol) {
                "SOL" -> 142.8
                "USDC" -> 1.0
                "BONK" -> 0.000028
                "JUP" -> 0.92
                "RAY" -> 2.10
                else -> 10.0
            }
            val newToken = TokenBalanceEntity(
                walletAddress = walletAddr,
                symbol = symbol,
                name = symbol,
                balance = amount,
                priceUsd = price,
                change24h = 0f,
                iconColorHex = "#14F195"
            )
            tokenDao.insertToken(newToken)
        }

        val tx = TransactionEntity(
            walletAddress = walletAddr,
            type = "DEPOSIT",
            tokenSymbol = symbol,
            amount = amount,
            counterpartAddress = "Instant Deposit Gateway",
            status = "CONFIRMED",
            timestamp = System.currentTimeMillis(),
            signatureHash = generateRandomSignatureHash()
        )
        txDao.insertTransaction(tx)
    }

    suspend fun setTokenManualBalance(walletAddr: String, symbol: String, newBalance: Double) {
        val existing = tokenDao.getToken(walletAddr, symbol)
        if (existing != null) {
            val diff = newBalance - existing.balance
            tokenDao.setTokenBalance(walletAddr, symbol, newBalance)
            if (diff != 0.0) {
                val typeStr = if (diff > 0) "DEPOSIT" else "SEND"
                val tx = TransactionEntity(
                    walletAddress = walletAddr,
                    type = typeStr,
                    tokenSymbol = symbol,
                    amount = kotlin.math.abs(diff),
                    counterpartAddress = "Manual Balance Adjustment",
                    status = "CONFIRMED",
                    timestamp = System.currentTimeMillis(),
                    signatureHash = generateRandomSignatureHash()
                )
                txDao.insertTransaction(tx)
            }
        } else {
            val price = when (symbol) {
                "SOL" -> 142.8
                "USDC" -> 1.0
                "BONK" -> 0.000028
                "JUP" -> 0.92
                "RAY" -> 2.10
                else -> 5.0
            }
            val newToken = TokenBalanceEntity(
                walletAddress = walletAddr,
                symbol = symbol,
                name = symbol,
                balance = newBalance,
                priceUsd = price,
                change24h = 0f,
                iconColorHex = "#9945FF"
            )
            tokenDao.insertToken(newToken)
            val tx = TransactionEntity(
                walletAddress = walletAddr,
                type = "DEPOSIT",
                tokenSymbol = symbol,
                amount = newBalance,
                counterpartAddress = "Manual Balance Entry",
                status = "CONFIRMED",
                timestamp = System.currentTimeMillis(),
                signatureHash = generateRandomSignatureHash()
            )
            txDao.insertTransaction(tx)
        }
    }

    suspend fun swapToken(walletAddr: String, fromSymbol: String, toSymbol: String, fromAmount: Double, toAmount: Double): Boolean {
        val fromToken = tokenDao.getToken(walletAddr, fromSymbol) ?: return false
        if (fromToken.balance < fromAmount) return false

        tokenDao.addBalance(walletAddr, fromSymbol, -fromAmount)
        val existingTo = tokenDao.getToken(walletAddr, toSymbol)
        if (existingTo != null) {
            tokenDao.addBalance(walletAddr, toSymbol, toAmount)
        } else {
            val price = if (toSymbol == "USDC") 1.0 else if (toSymbol == "SOL") 142.8 else 0.05
            tokenDao.insertToken(
                TokenBalanceEntity(walletAddress = walletAddr, symbol = toSymbol, name = toSymbol, balance = toAmount, priceUsd = price, change24h = 2.5f, iconColorHex = "#9945FF")
            )
        }

        val tx = TransactionEntity(
            walletAddress = walletAddr,
            type = "SWAP",
            tokenSymbol = "$fromSymbol to $toSymbol",
            amount = fromAmount,
            counterpartAddress = "Jupiter DEX",
            status = "CONFIRMED",
            timestamp = System.currentTimeMillis(),
            signatureHash = generateRandomSignatureHash()
        )
        txDao.insertTransaction(tx)
        return true
    }

    companion object {
        private val BIP39_WORDS = listOf(
            "abandon", "ability", "able", "about", "above", "absent", "absorb", "abstract", "absurd", "abuse",
            "access", "accident", "account", "accuse", "achieve", "acid", "acoustic", "acquire", "across", "act",
            "action", "actor", "actual", "adapt", "add", "addict", "address", "adjust", "admit", "adult",
            "advance", "advice", "aerobic", "affair", "afford", "afraid", "again", "age", "agent", "agree",
            "ahead", "aim", "air", "airport", "aisle", "alarm", "album", "alcohol", "alert", "alien",
            "all", "alley", "allow", "almost", "alone", "alpha", "already", "also", "alter", "always",
            "amateur", "amazing", "amber", "ambition", "amuse", "anchor", "ancient", "anger", "angle", "angry",
            "animal", "ankle", "announce", "annual", "another", "answer", "antenna", "antique", "anxiety", "any",
            "apart", "apology", "appear", "apple", "approve", "april", "arch", "arctic", "area", "arena",
            "argue", "arm", "armed", "armor", "army", "around", "arrange", "arrest", "arrive", "arrow",
            "art", "artist", "artwork", "ask", "aspect", "assault", "asset", "assist", "assume", "athlete",
            "atom", "attack", "attend", "attitude", "attract", "auction", "audit", "august", "aunt", "author",
            "auto", "autumn", "average", "avocado", "avoid", "awake", "aware", "away", "awesome", "awful",
            "awkward", "axis", "baby", "bachelor", "bacon", "badge", "bag", "balance", "balcony", "ball",
            "bamboo", "banana", "banner", "bar", "barely", "bargain", "barrel", "base", "basic", "basket",
            "battle", "beach", "bean", "beauty", "because", "become", "beef", "before", "begin", "behave",
            "behind", "believe", "below", "belt", "bench", "benefit", "best", "betray", "better", "between",
            "beyond", "bicycle", "bid", "bike", "bind", "biology", "bird", "birth", "bitter", "black",
            "blade", "blame", "blanket", "blast", "bleak", "bless", "blind", "blood", "blossom", "blouse",
            "blue", "blur", "blush", "board", "boat", "body", "boil", "bomb", "bone", "bonus",
            "book", "boost", "border", "boring", "borrow", "boss", "bottom", "bounce", "box", "boy",
            "brain", "brand", "brave", "bread", "breeze", "brick", "bridge", "brief", "bright", "bring",
            "broken", "bronze", "brother", "brown", "brush", "bubble", "budget", "buffalo", "build", "bulb",
            "bundle", "burden", "burger", "bus", "business", "busy", "butter", "buyer", "buzz", "cabbage",
            "cabin", "cable", "cactus", "cage", "cake", "call", "calm", "camera", "camp", "can",
            "canal", "cancel", "candy", "cannon", "canvas", "canyon", "capable", "capital", "captain", "car",
            "carbon", "card", "cargo", "carpet", "carry", "cart", "case", "cash", "casino", "castle",
            "cat", "catalog", "catch", "category", "cattle", "cause", "caution", "cave", "ceiling", "century",
            "cereal", "certain", "chair", "chalk", "champion", "change", "chaos", "chapter", "charge", "chase",
            "chat", "cheap", "check", "cheese", "chef", "cherry", "chest", "chicken", "chief", "child",
            "choice", "choose", "chronic", "chuckle", "circle", "citizen", "city", "civil", "claim", "clap",
            "clean", "clerk", "clever", "click", "client", "cliff", "climb", "clinic", "clip", "clock",
            "close", "cloth", "cloud", "clown", "club", "cluster", "coach", "coast", "coconut", "code",
            "coffee", "coil", "coin", "collect", "color", "column", "combine", "come", "comfort", "comic"
        )

        fun generateSeedPhrase(): String {
            return (1..12).map { BIP39_WORDS.random() }.joinToString(" ")
        }

        fun generateSolanaAddress(): String {
            val chars = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz"
            val prefix = listOf("7x", "HN", "F5", "3q", "9m", "EQ", "8b", "DK").random()
            val suffix = (1..6).map { chars.random() }.joinToString("")
            return "${prefix}Kx9P2mQz8...${suffix}"
        }

        fun generateRandomSignatureHash(): String {
            val chars = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz"
            val start = (1..6).map { chars.random() }.joinToString("")
            val end = (1..6).map { chars.random() }.joinToString("")
            return "${start}...${end}"
        }
    }
}
