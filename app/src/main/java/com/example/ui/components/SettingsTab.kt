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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.HelpTopic
import com.example.data.HelpTopics
import com.example.data.WalletEntity
import com.example.ui.theme.PhantomBorder
import com.example.ui.theme.PhantomGreen
import com.example.ui.theme.PhantomPurple
import com.example.ui.theme.PhantomRed
import com.example.ui.theme.PhantomSurface
import com.example.ui.theme.PhantomText
import com.example.ui.theme.PhantomTextSecondary
import com.example.ui.theme.SolanaTeal

@Composable
fun SettingsTabContent(
    currentWallet: WalletEntity?,
    onRevealSeed: () -> Unit,
    onResetData: () -> Unit,
    onHelpTopic: (HelpTopic) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Settings", color = PhantomText, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(16.dp))

        // Security Section
        Text("SECURITY", color = PhantomTextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            colors = CardDefaults.cardColors(containerColor = PhantomSurface),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                SettingsRow(
                    icon = Icons.Default.Security,
                    iconTint = PhantomPurple,
                    title = "Show Secret Recovery Phrase",
                    subtitle = "Inspect 12-word BIP39 seed",
                    onClick = onRevealSeed
                )
                androidx.compose.material3.HorizontalDivider(color = PhantomBorder.copy(alpha = 0.5f), modifier = Modifier.padding(horizontal = 16.dp))
                SettingsRow(
                    icon = Icons.Default.Wifi,
                    iconTint = SolanaTeal,
                    title = "Network RPC Gateway",
                    subtitle = "Solana Cluster Connected",
                    onClick = {}
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Help Topics Section
        Text("KNOWLEDGE BASE", color = PhantomTextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            colors = CardDefaults.cardColors(containerColor = PhantomSurface),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                HelpTopics.forEachIndexed { index, topic ->
                    SettingsRow(
                        icon = Icons.Default.HelpOutline,
                        iconTint = PhantomGreen,
                        title = topic.title,
                        subtitle = topic.shortSummary,
                        onClick = { onHelpTopic(topic) }
                    )
                    if (index < HelpTopics.lastIndex) {
                        androidx.compose.material3.HorizontalDivider(color = PhantomBorder.copy(alpha = 0.5f), modifier = Modifier.padding(horizontal = 16.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Data Management
        Text("DATA MANAGEMENT", color = PhantomTextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            colors = CardDefaults.cardColors(containerColor = PhantomSurface),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            SettingsRow(
                icon = Icons.Default.Refresh,
                iconTint = PhantomRed,
                title = "Reset Portfolio Data",
                subtitle = "Reset all token balances to default",
                onClick = onResetData
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // App Footer
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Phantom v1.0", color = PhantomTextSecondary, fontSize = 11.sp)
            Text("Built with Jetpack Compose", color = PhantomTextSecondary.copy(alpha = 0.6f), fontSize = 10.sp)
        }
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun SettingsRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(iconTint.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = title, tint = iconTint, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(title, color = PhantomText, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(subtitle, color = PhantomTextSecondary, fontSize = 12.sp)
            }
        }
        Icon(Icons.Default.ChevronRight, contentDescription = "Open", tint = PhantomTextSecondary)
    }
}
