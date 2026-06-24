package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.WalletEntity
import com.example.ui.theme.PhantomBg
import com.example.ui.theme.PhantomBorder
import com.example.ui.theme.PhantomPurple
import com.example.ui.theme.PhantomSurface
import com.example.ui.theme.PhantomSurfaceHigh
import com.example.ui.theme.PhantomText
import com.example.ui.theme.PhantomTextSecondary
import com.example.ui.theme.SolanaTeal

@Composable
fun PhantomTopHeader(
    wallets: List<WalletEntity>,
    currentWallet: WalletEntity?,
    onSelectWallet: (Int) -> Unit,
    onCreateNewWallet: () -> Unit,
    onCopyAddress: () -> Unit
) {
    var showDropdown by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Account Switcher Dropdown
        Box {
            Surface(
                color = PhantomSurfaceHigh,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.clickable { showDropdown = true }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(PhantomPurple),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountBalanceWallet,
                            contentDescription = "Wallet Icon",
                            tint = PhantomBg,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = currentWallet?.name ?: "Main Wallet",
                        color = PhantomText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Switch Account",
                        tint = PhantomTextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            DropdownMenu(
                expanded = showDropdown,
                onDismissRequest = { showDropdown = false },
                modifier = Modifier
                    .background(PhantomSurface)
                    .width(220.dp)
            ) {
                Text(
                    "YOUR WALLETS",
                    color = PhantomTextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
                wallets.forEach { wallet ->
                    DropdownMenuItem(
                        text = {
                            Column {
                                Text(wallet.name, color = PhantomText, fontWeight = FontWeight.SemiBold)
                                Text(truncateAddr(wallet.address), color = PhantomTextSecondary, fontSize = 11.sp)
                            }
                        },
                        trailingIcon = {
                            if (wallet.isSelected) {
                                Icon(Icons.Default.Check, contentDescription = "Selected", tint = SolanaTeal, modifier = Modifier.size(18.dp))
                            }
                        },
                        onClick = {
                            showDropdown = false
                            onSelectWallet(wallet.id)
                        }
                    )
                }
                DropdownMenuItem(
                    text = { Text("+ Create Another Wallet", color = PhantomPurple, fontWeight = FontWeight.Bold) },
                    onClick = {
                        showDropdown = false
                        onCreateNewWallet()
                    }
                )
            }
        }

        // Network & Address Copy Pill
        if (currentWallet != null) {
            Surface(
                color = PhantomSurface,
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, PhantomBorder),
                modifier = Modifier.clickable { onCopyAddress() }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(SolanaTeal)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = truncateAddr(currentWallet.address),
                        color = PhantomTextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy Address",
                        tint = PhantomTextSecondary,
                        modifier = Modifier.size(13.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PhantomBottomBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    NavigationBar(
        containerColor = PhantomSurface,
        tonalElevation = 8.dp,
        modifier = Modifier.navigationBarsPadding()
    ) {
        val navItems = listOf(
            Triple("Tokens", Icons.Filled.AccountBalanceWallet, Icons.Outlined.AccountBalanceWallet),
            Triple("Collectibles", Icons.Filled.Star, Icons.Outlined.Star),
            Triple("Swap", Icons.Filled.SwapHoriz, Icons.Outlined.SwapHoriz),
            Triple("Activity", Icons.Filled.History, Icons.Outlined.History),
            Triple("Settings", Icons.Filled.Settings, Icons.Outlined.Settings)
        )

        navItems.forEachIndexed { index, item ->
            val isSelected = selectedTab == index
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(index) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.second else item.third,
                        contentDescription = item.first,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = item.first,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PhantomPurple,
                    selectedTextColor = PhantomPurple,
                    indicatorColor = PhantomSurfaceHigh,
                    unselectedIconColor = PhantomTextSecondary,
                    unselectedTextColor = PhantomTextSecondary
                )
            )
        }
    }
}

fun truncateAddr(addr: String): String {
    if (addr.length <= 10) return addr
    return "${addr.take(4)}...${addr.takeLast(4)}"
}
