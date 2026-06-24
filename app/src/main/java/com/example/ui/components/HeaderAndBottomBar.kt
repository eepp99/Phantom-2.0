package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.example.ui.theme.PhantomAvatarBlue
import com.example.ui.theme.PhantomAvatarYellow
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
    activeTopTab: Int, // 0: Home, 1: Trade, 2: Explore
    onSelectTopTab: (Int) -> Unit,
    onOpenAccountsModal: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Far Left Avatar Circle (Yellow/Blue with Character/Sunglasses feel)
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(PhantomAvatarYellow)
                .clickable { onOpenAccountsModal() },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(PhantomAvatarBlue),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Face,
                    contentDescription = "Avatar",
                    tint = PhantomBg,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Home | Trade | Explore Capsule Pills
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            val tabs = listOf("Home", "Trade", "Explore")
            tabs.forEachIndexed { i, title ->
                val sel = activeTopTab == i
                Surface(
                    color = if (sel) PhantomPurple else PhantomSurfaceHigh,
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.clickable { onSelectTopTab(i) }
                ) {
                    Text(
                        text = title,
                        color = if (sel) Color.Black else PhantomTextSecondary,
                        fontWeight = if (sel) FontWeight.ExtraBold else FontWeight.SemiBold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountsBottomSheetModal(
    wallets: List<WalletEntity>,
    onDismiss: () -> Unit,
    onSelectWallet: (Int) -> Unit,
    onCreateWallet: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = PhantomBg
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(bottom = 36.dp)
        ) {
            // Top Bar: X | Your Accounts | +
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = PhantomSurface,
                    shape = CircleShape,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { onDismiss() }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = PhantomText, modifier = Modifier.size(18.dp))
                    }
                }

                Text(
                    text = "Your Accounts",
                    color = PhantomText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Surface(
                    color = PhantomSurface,
                    shape = CircleShape,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {
                            onDismiss()
                            onCreateWallet()
                        }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Add, contentDescription = "Add Account", tint = PhantomText, modifier = Modifier.size(18.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Account Cards
            wallets.forEach { wallet ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = PhantomSurface),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onSelectWallet(wallet.id)
                            onDismiss()
                        }
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Avatar Pill with Checkmark
                            Box(contentAlignment = Alignment.BottomEnd) {
                                Surface(
                                    color = PhantomSurfaceHigh,
                                    shape = CircleShape,
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(wallet.name.take(2).uppercase(), color = PhantomTextSecondary, fontWeight = FontWeight.Bold)
                                    }
                                }
                                if (wallet.isSelected) {
                                    Surface(
                                        color = PhantomPurple,
                                        shape = CircleShape,
                                        modifier = Modifier.size(18.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(Icons.Default.Check, contentDescription = "Selected", tint = PhantomBg, modifier = Modifier.size(12.dp))
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column {
                                Text(wallet.name, color = PhantomText, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text("$0.00", color = PhantomTextSecondary, fontSize = 13.sp)
                            }
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Surface(color = PhantomSurfaceHigh, shape = RoundedCornerShape(12.dp)) {
                                Icon(Icons.Default.GridView, contentDescription = "QR", tint = PhantomText, modifier = Modifier.padding(8.dp).size(18.dp))
                            }
                            Surface(color = PhantomSurfaceHigh, shape = RoundedCornerShape(12.dp)) {
                                Icon(Icons.Default.MoreHoriz, contentDescription = "Menu", tint = PhantomText, modifier = Modifier.padding(8.dp).size(18.dp))
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
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
        containerColor = PhantomBg,
        tonalElevation = 0.dp,
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
                    indicatorColor = PhantomSurface,
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
