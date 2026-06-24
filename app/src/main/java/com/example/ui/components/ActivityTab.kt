package com.example.ui.components

import android.text.format.DateUtils
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.TransactionEntity
import com.example.ui.theme.PhantomBg
import com.example.ui.theme.PhantomBorder
import com.example.ui.theme.PhantomGreen
import com.example.ui.theme.PhantomPurple
import com.example.ui.theme.PhantomRed
import com.example.ui.theme.PhantomSurface
import com.example.ui.theme.PhantomSurfaceHigh
import com.example.ui.theme.PhantomText
import com.example.ui.theme.PhantomTextSecondary
import com.example.ui.theme.SolanaTeal

@Composable
fun ActivityTabContent(
    transactions: List<TransactionEntity>,
    onTxClick: (TransactionEntity) -> Unit
) {
    var filter by remember { mutableStateOf("ALL") }

    val filtered = when (filter) {
        "SEND" -> transactions.filter { it.type == "SEND" }
        "RECEIVE" -> transactions.filter { it.type == "RECEIVE" || it.type == "DEPOSIT" }
        "SWAP" -> transactions.filter { it.type == "SWAP" }
        else -> transactions
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text("Activity History", color = PhantomText, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(12.dp))

        // Filter Pills
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            val filters = listOf("ALL" to "All", "SEND" to "Sent", "RECEIVE" to "Received", "SWAP" to "Swaps")
            items(filters) { (key, label) ->
                val sel = filter == key
                Surface(
                    color = if (sel) PhantomPurple else PhantomSurface,
                    shape = RoundedCornerShape(16.dp),
                    border = if (!sel) androidx.compose.foundation.BorderStroke(1.dp, PhantomBorder) else null,
                    modifier = Modifier.clickable { filter = key }
                ) {
                    Text(
                        text = label,
                        color = if (sel) PhantomBg else PhantomText,
                        fontSize = 12.sp,
                        fontWeight = if (sel) FontWeight.Bold else FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(filtered) { tx ->
                TransactionRowItem(tx = tx, onClick = { onTxClick(tx) })
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (filtered.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 60.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.History, contentDescription = "Empty", tint = PhantomTextSecondary, modifier = Modifier.size(48.dp))
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("No Transactions Recorded", color = PhantomText, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text("Your transaction history will appear here.", color = PhantomTextSecondary, fontSize = 12.sp)
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun TransactionRowItem(tx: TransactionEntity, onClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = PhantomSurface),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Type Icon Circle
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(
                            when (tx.type) {
                                "RECEIVE", "DEPOSIT" -> PhantomGreen.copy(alpha = 0.2f)
                                "SEND" -> PhantomRed.copy(alpha = 0.2f)
                                else -> PhantomPurple.copy(alpha = 0.2f)
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (tx.type) {
                            "RECEIVE" -> Icons.Default.ArrowDownward
                            "DEPOSIT" -> Icons.Default.CardGiftcard
                            "SEND" -> Icons.Default.ArrowUpward
                            else -> Icons.Default.SwapHoriz
                        },
                        contentDescription = tx.type,
                        tint = when (tx.type) {
                            "RECEIVE", "DEPOSIT" -> PhantomGreen
                            "SEND" -> PhantomRed
                            else -> PhantomPurple
                        },
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = when (tx.type) {
                            "DEPOSIT" -> "Asset Deposit"
                            "RECEIVE" -> "Received ${tx.tokenSymbol}"
                            "SEND" -> "Sent ${tx.tokenSymbol}"
                            else -> "Swapped ${tx.tokenSymbol}"
                        },
                        color = PhantomText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (tx.counterpartAddress.length > 16) "${tx.counterpartAddress.take(6)}...${tx.counterpartAddress.takeLast(4)}"
                                   else tx.counterpartAddress,
                            color = PhantomTextSecondary,
                            fontSize = 11.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("•", color = PhantomTextSecondary, fontSize = 11.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        val timeSpan = try {
                            DateUtils.getRelativeTimeSpanString(tx.timestamp, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS).toString()
                        } catch (e: Exception) {
                            "Just now"
                        }
                        Text(timeSpan, color = PhantomTextSecondary, fontSize = 11.sp)
                    }
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                val isPos = tx.type == "RECEIVE" || tx.type == "DEPOSIT"
                Text(
                    text = (if (isPos) "+" else if (tx.type == "SEND") "-" else "") +
                           if (tx.amount > 1000) String.format("%,.0f", tx.amount) else String.format("%,.2f", tx.amount),
                    color = if (isPos) PhantomGreen else if (tx.type == "SEND") PhantomText else SolanaTeal,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Surface(
                    color = PhantomSurfaceHigh,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "Confirmed",
                        color = PhantomGreen,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}
