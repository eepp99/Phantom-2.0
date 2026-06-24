package com.example.ui.components

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.TokenBalanceEntity
import com.example.ui.theme.PhantomBg
import com.example.ui.theme.PhantomBorder
import com.example.ui.theme.PhantomGreen
import com.example.ui.theme.PhantomPurple
import com.example.ui.theme.PhantomPurpleBright
import com.example.ui.theme.PhantomRed
import com.example.ui.theme.PhantomSurface
import com.example.ui.theme.PhantomSurfaceHigh
import com.example.ui.theme.PhantomText
import com.example.ui.theme.PhantomTextSecondary
import com.example.ui.theme.SolanaTeal

@Composable
fun TokensTabContent(
    totalBalanceUsd: Double,
    tokens: List<TokenBalanceEntity>,
    onReceiveClick: () -> Unit,
    onSendClick: () -> Unit,
    onSwapClick: () -> Unit,
    onFaucetClick: () -> Unit,
    onTokenClick: (TokenBalanceEntity) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Hero Balance Display
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = String.format("$%,.2f", totalBalanceUsd),
                color = PhantomText,
                fontSize = 42.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = PhantomGreen.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "+$82.40 (+5.84%)",
                        color = PhantomGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text("24h Change", color = PhantomTextSecondary, fontSize = 13.sp)
            }
            Spacer(modifier = Modifier.height(28.dp))
        }

        // 4 Action Buttons
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ActionButton(title = "Receive", icon = Icons.Default.ArrowDownward, onClick = onReceiveClick)
                ActionButton(title = "Send", icon = Icons.Default.ArrowUpward, onClick = onSendClick)
                ActionButton(title = "Swap", icon = Icons.Default.SwapHoriz, onClick = onSwapClick)
                ActionButton(title = "Deposit", icon = Icons.Default.Add, isAccent = true, onClick = onFaucetClick)
            }
            Spacer(modifier = Modifier.height(32.dp))
        }

        // Token Section Title
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Portfolio", color = PhantomText, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("Tap to Manage", color = SolanaTeal, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Token List
        items(tokens) { token ->
            TokenRowItem(token = token, onClick = { onTokenClick(token) })
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun ActionButton(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isAccent: Boolean = false,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(
                    if (isAccent) Brush.linearGradient(listOf(SolanaTeal, PhantomPurpleBright))
                    else Brush.linearGradient(listOf(PhantomSurfaceHigh, PhantomSurface))
                )
                .then(
                    if (!isAccent) Modifier.background(PhantomSurfaceHigh) else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isAccent) PhantomBg else PhantomText,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            color = PhantomText,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun TokenRowItem(token: TokenBalanceEntity, onClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = PhantomSurface),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Token Icon Circle
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            try {
                                Color(android.graphics.Color.parseColor(token.iconColorHex))
                            } catch (e: Exception) {
                                PhantomPurple
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = token.symbol.take(1),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(token.name, color = PhantomText, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(String.format("$%,.2f", token.priceUsd), color = PhantomTextSecondary, fontSize = 13.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        val isPos = token.change24h >= 0
                        Text(
                            text = (if (isPos) "+" else "") + String.format("%.2f%%", token.change24h),
                            color = if (isPos) PhantomGreen else PhantomRed,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                val totalVal = token.balance * token.priceUsd
                Text(
                    text = String.format("$%,.2f", totalVal),
                    color = PhantomText,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = if (token.balance > 1000) String.format("%,.0f %s", token.balance, token.symbol)
                           else String.format("%,.2f %s", token.balance, token.symbol),
                    color = PhantomTextSecondary,
                    fontSize = 13.sp
                )
            }
        }
    }
}
