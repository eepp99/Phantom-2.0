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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DefaultContacts
import com.example.data.HelpTopic
import com.example.data.TokenBalanceEntity
import com.example.data.WalletEntity
import com.example.ui.theme.PhantomBg
import com.example.ui.theme.PhantomBorder
import com.example.ui.theme.PhantomGreen
import com.example.ui.theme.PhantomPurple
import com.example.ui.theme.PhantomPurpleBright
import com.example.ui.theme.PhantomSurface
import com.example.ui.theme.PhantomSurfaceHigh
import com.example.ui.theme.PhantomText
import com.example.ui.theme.PhantomTextSecondary
import com.example.ui.theme.SolanaTeal

@Composable
fun OnboardingScreen(
    onCreateClick: () -> Unit,
    onImportClick: (String) -> Unit
) {
    var showImport by remember { mutableStateOf(false) }
    var importText by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(PhantomSurfaceHigh, PhantomBg, PhantomBg)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(PhantomPurple.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AccountBalanceWallet,
                    contentDescription = "Wallet",
                    tint = PhantomPurple,
                    modifier = Modifier.size(56.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Phantom",
                color = PhantomText,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "A non-custodial Solana wallet to manage Web3 assets, portfolios, and instant blockchain transfers safely.",
                color = PhantomTextSecondary,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = onCreateClick,
                colors = ButtonDefaults.buttonColors(containerColor = PhantomPurpleBright, contentColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Create a New Wallet", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                color = PhantomSurface,
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, PhantomBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clickable { showImport = true }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("I Already Have a Wallet", color = PhantomText, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }

        if (showImport) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(PhantomBg.copy(alpha = 0.9f))
                    .clickable { showImport = false },
                contentAlignment = Alignment.Center
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = PhantomSurface),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth()
                        .clickable(enabled = false) {}
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("Import Recovery Phrase", color = PhantomText, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Paste your 12-word recovery phrase to restore your wallet.", color = PhantomTextSecondary, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = importText,
                            onValueChange = { importText = it },
                            placeholder = { Text("apple banana cherry...", color = PhantomTextSecondary.copy(alpha = 0.5f)) },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = PhantomText, unfocusedTextColor = PhantomText),
                            modifier = Modifier.fillMaxWidth().height(100.dp)
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            Text(
                                "Cancel",
                                color = PhantomTextSecondary,
                                modifier = Modifier
                                    .clickable { showImport = false }
                                    .padding(12.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    showImport = false
                                    onImportClick(importText)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = PhantomPurple)
                            ) {
                                Text("Import", color = PhantomBg, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SeedPhraseBackupScreen(
    wallet: WalletEntity,
    onConfirmSaved: () -> Unit,
    onCopyPhrase: () -> Unit
) {
    val words = wallet.seedPhrase.split(" ")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PhantomBg)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(20.dp))
            Text("Secret Recovery Phrase", color = PhantomText, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "This is the master key to your wallet. Write down these 12 words in order.",
                color = PhantomTextSecondary,
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = PhantomSurface),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.height(220.dp)
                    ) {
                        itemsIndexed(words) { i, word ->
                            Surface(
                                color = PhantomSurfaceHigh,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("${i + 1}.", color = PhantomTextSecondary, fontSize = 11.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(word, color = PhantomText, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        color = PhantomPurple.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onCopyPhrase() }
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = PhantomPurple, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Copy Recovery Phrase", color = PhantomPurple, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        Column {
            Button(
                onClick = onConfirmSaved,
                colors = ButtonDefaults.buttonColors(containerColor = PhantomPurpleBright),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Okay, I saved it", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendModalSheet(
    tokens: List<TokenBalanceEntity>,
    isBroadcasting: Boolean,
    onDismiss: () -> Unit,
    onSend: (String, Double, String) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = PhantomSurface
    ) {
        var selectedToken by remember { mutableStateOf(tokens.firstOrNull()?.symbol ?: "SOL") }
        var destAddr by remember { mutableStateOf("") }
        var amountText by remember { mutableStateOf("1.0") }

        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Send Tokens", color = PhantomText, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = PhantomTextSecondary)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            Text("SELECT TOKEN", color = PhantomTextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                tokens.forEach { tok ->
                    val sel = selectedToken == tok.symbol
                    Surface(
                        color = if (sel) PhantomPurple else PhantomSurfaceHigh,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.clickable { selectedToken = tok.symbol }
                    ) {
                        Text(
                            text = "${tok.symbol} (${String.format("%,.1f", tok.balance)})",
                            color = if (sel) PhantomBg else PhantomText,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("RECIPIENT OR CONTACT", color = PhantomTextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = destAddr,
                onValueChange = { destAddr = it },
                placeholder = { Text("Paste Solana address...", color = PhantomTextSecondary.copy(alpha = 0.5f)) },
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = PhantomText, unfocusedTextColor = PhantomText),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text("Quick Pick Contacts:", color = PhantomTextSecondary, fontSize = 11.sp)
            Spacer(modifier = Modifier.height(6.dp))
            LazyColumn(modifier = Modifier.height(120.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                items(DefaultContacts) { contact ->
                    Surface(
                        color = PhantomSurfaceHigh.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { destAddr = contact.address }
                    ) {
                        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(28.dp).clip(CircleShape).background(PhantomPurple.copy(alpha = 0.2f)), contentAlignment = Alignment.Center) {
                                    Text(contact.initial, color = PhantomPurple, fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(contact.name, color = PhantomText, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text(contact.address, color = PhantomTextSecondary, fontSize = 10.sp)
                                }
                            }
                            Text(contact.tag, color = SolanaTeal, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("AMOUNT", color = PhantomTextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = amountText,
                onValueChange = { amountText = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = PhantomText, unfocusedTextColor = PhantomText),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val amt = amountText.toDoubleOrNull() ?: 0.0
                    onSend(selectedToken, amt, destAddr.ifBlank { "7TolyDevnetAddr" })
                },
                enabled = !isBroadcasting && (amountText.toDoubleOrNull() ?: 0.0) > 0,
                colors = ButtonDefaults.buttonColors(containerColor = PhantomPurpleBright, contentColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                if (isBroadcasting) {
                    androidx.compose.material3.CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Broadcasting...")
                } else {
                    Icon(Icons.Default.Send, contentDescription = "Send", modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Confirm Send", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiveModalSheet(
    wallet: WalletEntity,
    onDismiss: () -> Unit,
    onCopyAddress: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = PhantomSurface
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Receive Assets", color = PhantomText, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = PhantomTextSecondary)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.size(200.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.QrCode, contentDescription = "QR", tint = Color.Black, modifier = Modifier.size(140.dp))
                        Text("SOLANA QR", color = Color.DarkGray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(wallet.name, color = PhantomText, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(wallet.address, color = PhantomTextSecondary, fontSize = 13.sp)

            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onCopyAddress,
                colors = ButtonDefaults.buttonColors(containerColor = PhantomPurple),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = PhantomBg)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Copy Wallet Address", color = PhantomBg, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FaucetModalSheet(
    onDismiss: () -> Unit,
    onDrop: (String, Double) -> Unit
) {
    var selectedSymbol by remember { mutableStateOf("SOL") }
    var amountText by remember { mutableStateOf("10.0") }
    val symbols = listOf("SOL", "USDC", "BONK", "JUP", "RAY", "MSOL")

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = PhantomSurface
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(bottom = 40.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Deposit Crypto Assets", color = PhantomText, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = PhantomTextSecondary)
                }
            }
            Text("Manually input any amount of crypto to add to your wallet balance.", color = PhantomTextSecondary, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(20.dp))

            Text("SELECT ASSET", color = PhantomTextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(symbols) { sym ->
                    val sel = selectedSymbol == sym
                    Surface(
                        color = if (sel) PhantomPurple else PhantomSurfaceHigh,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.clickable { selectedSymbol = sym }
                    ) {
                        Text(
                            text = sym,
                            color = if (sel) PhantomBg else PhantomText,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text("CUSTOM INPUT AMOUNT", color = PhantomTextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = amountText,
                onValueChange = { amountText = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                placeholder = { Text("Input numbers (e.g. 100, 50.5)") },
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = PhantomText, unfocusedTextColor = PhantomText),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    val amt = amountText.toDoubleOrNull() ?: 0.0
                    if (amt > 0) {
                        onDrop(selectedSymbol, amt)
                        onDismiss()
                    }
                },
                enabled = (amountText.toDoubleOrNull() ?: 0.0) > 0,
                colors = ButtonDefaults.buttonColors(containerColor = PhantomPurpleBright, contentColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add +$amountText $selectedSymbol", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TokenManageModalSheet(
    token: TokenBalanceEntity,
    onDismiss: () -> Unit,
    onAddAmount: (Double) -> Unit,
    onSetBalance: (Double) -> Unit
) {
    var amountText by remember { mutableStateOf("") }
    var mode by remember { mutableStateOf("ADD") } // ADD or SET

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = PhantomSurface
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(bottom = 40.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("${token.name} (${token.symbol})", color = PhantomText, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = PhantomTextSecondary)
                }
            }
            Text("Current Balance: ${String.format("%,.4f", token.balance)} ${token.symbol}", color = PhantomPurpleBright, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text("Market Price: $${token.priceUsd}", color = PhantomTextSecondary, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(20.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Surface(
                    color = if (mode == "ADD") PhantomPurple else PhantomSurfaceHigh,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f).clickable { mode = "ADD" }
                ) {
                    Text("Add Amount", color = if (mode == "ADD") PhantomBg else PhantomText, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.padding(12.dp))
                }
                Surface(
                    color = if (mode == "SET") PhantomPurple else PhantomSurfaceHigh,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f).clickable { mode = "SET" }
                ) {
                    Text("Set Exact Balance", color = if (mode == "SET") PhantomBg else PhantomText, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.padding(12.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(if (mode == "ADD") "INPUT AMOUNT TO ADD" else "INPUT NEW TOTAL BALANCE", color = PhantomTextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = amountText,
                onValueChange = { amountText = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                placeholder = { Text("Input numbers manually...") },
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = PhantomText, unfocusedTextColor = PhantomText),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    val valNum = amountText.toDoubleOrNull() ?: return@Button
                    if (mode == "ADD") {
                        onAddAmount(valNum)
                    } else {
                        onSetBalance(valNum)
                    }
                    onDismiss()
                },
                enabled = amountText.toDoubleOrNull() != null,
                colors = ButtonDefaults.buttonColors(containerColor = PhantomGreen, contentColor = PhantomBg),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text(if (mode == "ADD") "Add Crypto" else "Update Balance", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EduTopicSheet(
    topic: HelpTopic,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = PhantomSurface
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(bottom = 40.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Documentation", color = SolanaTeal, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = PhantomTextSecondary)
                }
            }
            Text(topic.title, color = PhantomText, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Surface(color = PhantomSurfaceHigh, shape = RoundedCornerShape(12.dp)) {
                Text(topic.shortSummary, color = PhantomPurple, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, modifier = Modifier.padding(12.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = topic.detailedContent,
                color = PhantomTextSecondary,
                fontSize = 14.sp,
                lineHeight = 22.sp
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = PhantomPurple), modifier = Modifier.fillMaxWidth().height(50.dp)) {
                Text("Got it!", color = PhantomBg, fontWeight = FontWeight.Bold)
            }
        }
    }
}
