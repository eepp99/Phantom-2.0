package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WalletDao {
    @Query("SELECT * FROM wallets ORDER BY id ASC")
    fun getAllWallets(): Flow<List<WalletEntity>>

    @Query("SELECT * FROM wallets WHERE isSelected = 1 LIMIT 1")
    fun getSelectedWallet(): Flow<WalletEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWallet(wallet: WalletEntity): Long

    @Query("UPDATE wallets SET isSelected = 0")
    suspend fun deselectAll()

    @Query("UPDATE wallets SET isSelected = 1 WHERE id = :walletId")
    suspend fun selectWallet(walletId: Int)

    @Query("DELETE FROM wallets")
    suspend fun deleteAllWallets()
}

@Dao
interface TokenDao {
    @Query("SELECT * FROM token_balances WHERE walletAddress = :address ORDER BY balance * priceUsd DESC")
    fun getTokensForWallet(address: String): Flow<List<TokenBalanceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToken(token: TokenBalanceEntity)

    @Query("SELECT * FROM token_balances WHERE walletAddress = :address AND symbol = :symbol LIMIT 1")
    suspend fun getToken(address: String, symbol: String): TokenBalanceEntity?

    @Query("UPDATE token_balances SET balance = balance + :amount WHERE walletAddress = :address AND symbol = :symbol")
    suspend fun addBalance(address: String, symbol: String, amount: Double)

    @Query("UPDATE token_balances SET balance = :newBalance WHERE walletAddress = :address AND symbol = :symbol")
    suspend fun setTokenBalance(address: String, symbol: String, newBalance: Double)

    @Query("DELETE FROM token_balances WHERE walletAddress = :address")
    suspend fun deleteTokensForWallet(address: String)
}

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions WHERE walletAddress = :address ORDER BY timestamp DESC")
    fun getTransactionsForWallet(address: String): Flow<List<TransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(tx: TransactionEntity)

    @Query("DELETE FROM transactions WHERE walletAddress = :address")
    suspend fun deleteTransactionsForWallet(address: String)
}

@Dao
interface CollectibleDao {
    @Query("SELECT * FROM collectibles WHERE walletAddress = :address")
    fun getCollectiblesForWallet(address: String): Flow<List<CollectibleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollectible(collectible: CollectibleEntity)

    @Query("DELETE FROM collectibles WHERE walletAddress = :address")
    suspend fun deleteCollectiblesForWallet(address: String)
}
