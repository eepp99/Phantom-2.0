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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.TokenBalanceEntity
import com.example.ui.theme.PhantomBg
import com.example.ui.theme.PhantomGreen
import com.example.ui.theme.PhantomPurple
import com.example.ui.theme.PhantomPurpleBright
import com.example.ui.theme.PhantomSurface
import com.example.ui.theme.PhantomSurfaceHigh
import com.example.ui.theme.PhantomText
import com.example.ui.theme.PhantomTextSecondary
import com.example.ui.theme.SolanaTeal

@Composable
fun SwapTabContent(
    tokens: List<TokenBalanceEntity>,
    isBroadcasting: Boolean,
    onSwap: (String, String, Double, Double) -> Unit,
    onHelpClick: (String) -> Unit
) {
    var fromSymbol by remember { mutableStateOf("SOL") }
    var toSymbol by remember { mutableStateOf("USDC") }
    var amountText by remember { mutableStateOf("1.0") }
    var showFromPicker by remember { mutableStateOf(false) }
    var showToPicker by remember { mutableStateOf(false) }

    val fromAmount = amountText.toDoubleOrNull() ?: 0.0
    val fromPrice = tokens.find { it.symbol == fromSymbol }?.priceUsd ?: if (fromSymbol == "SOL") 142.8 else 1.0
    val toPrice = tokens.find { it.symbol == toSymbol }?.priceUsd ?: if (toSymbol == "USDC") 1.0 else if (toSymbol == "SOL") 142.8 else 0.000028

    val estimatedToAmount = if (toPrice > 0) (fromAmount * fromPrice) / toPrice else 0.0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Swap Tokens", color = PhantomText, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onHelpClick("slippage") }
            ) {
                Surface(
                    color = PhantomSurfaceHigh,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Settings, contentDescription = "Slippage", tint = PhantomTextSecondary, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Slippage 0.5%", color = PhantomTextSecondary, fontSize = 11.sp)
                    }
                }
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.Default.Info, contentDescription = "Info", tint = PhantomPurple, modifier = Modifier.size(16.dp))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Swap Card Container
        Box {
            Column {
                // You Pay Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = PhantomSurface),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("You Pay", color = PhantomTextSecondary, fontSize = 12.sp)
                            val avail = tokens.find { it.symbol == fromSymbol }?.balance ?: 0.0
                            Text("Balance: ${String.format("%,.2f", avail)}", color = PhantomTextSecondary, fontSize = 12.sp)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Token Selector Pill
                            Box {
                                Surface(
                                    color = PhantomSurfaceHigh,
                                    shape = RoundedCornerShape(20.dp),
                                    modifier = Modifier.clickable { showFromPicker = true }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(fromSymbol, color = PhantomText, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Pick", tint = PhantomTextSecondary)
                                    }
                                }
                                TokenPickerMenu(
                                    expanded = showFromPicker,
                                    tokens = tokens,
                                    onDismiss = { showFromPicker = false },
                                    onSelect = {
                                        showFromPicker = false
                                        if (it == toSymbol) toSymbol = fromSymbol
                                        fromSymbol = it
                                    }
                                )
                            }

                            // Amount Input
                            OutlinedTextField(
                                value = amountText,
                                onValueChange = { amountText = it },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = PhantomText,
                                    unfocusedTextColor = PhantomText,
                                    focusedBorderColor = Color.Transparent,
                                    unfocusedBorderColor = Color.Transparent
                                ),
                                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, textAlign = androidx.compose.ui.text.style.TextAlign.End),
                                modifier = Modifier.width(160.dp)
                            )
                        }
                        Text(
                            text = "≈ $${String.format("%,.2f", fromAmount * fromPrice)}",
                            color = PhantomTextSecondary,
                            fontSize = 12.sp,
                            modifier = Modifier.align(Alignment.End)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // You Receive Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = PhantomSurface),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("You Receive (Estimated)", color = PhantomTextSecondary, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box {
                                Surface(
                                    color = PhantomSurfaceHigh,
                                    shape = RoundedCornerShape(20.dp),
                                    modifier = Modifier.clickable { showToPicker = true }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(toSymbol, color = PhantomText, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Pick", tint = PhantomTextSecondary)
                                    }
                                }
                                TokenPickerMenu(
                                    expanded = showToPicker,
                                    tokens = tokens,
                                    onDismiss = { showToPicker = false },
                                    onSelect = {
                                        showToPicker = false
                                        if (it == fromSymbol) fromSymbol = toSymbol
                                        toSymbol = it
                                    }
                                )
                            }

                            Text(
                                text = if (estimatedToAmount > 1000) String.format("%,.0f", estimatedToAmount) else String.format("%,.4f", estimatedToAmount),
                                color = SolanaTeal,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "≈ $${String.format("%,.2f", estimatedToAmount * toPrice)}",
                            color = PhantomTextSecondary,
                            fontSize = 12.sp,
                            modifier = Modifier.align(Alignment.End)
                        )
                    }
                }
            }

            // Invert Circle Button
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(PhantomBg)
                    .padding(4.dp)
                    .clip(CircleShape)
                    .background(PhantomPurple)
                    .clickable {
                        val temp = fromSymbol
                        fromSymbol = toSymbol
                        toSymbol = temp
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.SwapVert, contentDescription = "Invert", tint = PhantomBg)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Route Info Card
        Card(
            colors = CardDefaults.cardColors(containerColor = PhantomSurfaceHigh.copy(alpha = 0.5f)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Routing Gateway", color = PhantomTextSecondary, fontSize = 12.sp)
                    Text("Jupiter Liquidity Pool", color = PhantomGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { onHelpClick("gas_fee") }) {
                        Text("Network Gas Fee", color = PhantomTextSecondary, fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(Icons.Default.Info, contentDescription = "Fee", tint = PhantomTextSecondary, modifier = Modifier.size(14.dp))
                    }
                    Text("< 0.000005 SOL ($0.0007)", color = PhantomText, fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Submit Button
        Button(
            onClick = { onSwap(fromSymbol, toSymbol, fromAmount, estimatedToAmount) },
            enabled = !isBroadcasting && fromAmount > 0,
            colors = ButtonDefaults.buttonColors(
                containerColor = PhantomPurpleBright,
                contentColor = Color.White,
                disabledContainerColor = PhantomSurfaceHigh,
                disabledContentColor = PhantomTextSecondary
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            if (isBroadcasting) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                Spacer(modifier = Modifier.width(10.dp))
                Text("Swapping via Jupiter...", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            } else {
                Text("Confirm Swap", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun TokenPickerMenu(
    expanded: Boolean,
    tokens: List<TokenBalanceEntity>,
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        modifier = Modifier.background(PhantomSurface).width(160.dp)
    ) {
        val syms = listOf("SOL", "USDC", "BONK", "JUP", "RAY")
        syms.forEach { sym ->
            DropdownMenuItem(
                text = { Text(sym, color = PhantomText, fontWeight = FontWeight.Bold) },
                onClick = { onSelect(sym) }
            )
        }
    }
}
