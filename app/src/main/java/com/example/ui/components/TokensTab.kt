package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.TokenBalanceEntity
import com.example.ui.theme.PhantomBg
import com.example.ui.theme.PhantomBorder
import com.example.ui.theme.PhantomGreen
import com.example.ui.theme.PhantomPurple
import com.example.ui.theme.PhantomRed
import com.example.ui.theme.PhantomSurface
import com.example.ui.theme.PhantomSurfaceHigh
import com.example.ui.theme.PhantomText
import com.example.ui.theme.PhantomTextSecondary
import com.example.ui.theme.SolanaOrange
import com.example.ui.theme.SolanaTeal

@Composable
fun TokensTabContent(
    activeTopTab: Int, // 0: Home, 1: Trade, 2: Explore
    currentAccountName: String,
    totalBalanceUsd: Double,
    tokens: List<TokenBalanceEntity>,
    onOpenAccountsModal: () -> Unit,
    onReceiveClick: () -> Unit,
    onSendClick: () -> Unit,
    onSwapClick: () -> Unit,
    onFaucetClick: () -> Unit,
    onTokenClick: (TokenBalanceEntity) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize().background(PhantomBg)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            if (activeTopTab == 0) {
                // EXACT UI PLACING & DESIGN FROM SCREENSHOT
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Left aligned Account dropdown
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onOpenAccountsModal() }
                            .padding(vertical = 4.dp, horizontal = 2.dp)
                    ) {
                        Text(
                            text = currentAccountName,
                            color = Color(0xFFE4E4E7),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Switch Account",
                            tint = PhantomTextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Huge Balance Display
                    Text(
                        text = if (totalBalanceUsd == 0.0) "$0.00" else String.format("$%,.2f", totalBalanceUsd),
                        color = PhantomText,
                        fontSize = 50.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-1).sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Subtitle
                    Text(
                        text = "Add funds to get started. Withdraw anytime.",
                        color = PhantomTextSecondary,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Big Action Button (Add Funds)
                    Button(
                        onClick = onFaucetClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PhantomPurple,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(26.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Text(
                            text = "Add Funds",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    Spacer(modifier = Modifier.height(36.dp))
                }

                // For Your First Trade Section
                item {
                    Text(
                        text = "For Your First Trade",
                        color = PhantomText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TradeStarterCard(
                            name = "BTC",
                            price = "$62,057.00",
                            change = "-0.08%",
                            isPos = false,
                            symbol = "BTC",
                            onClick = onSwapClick
                        )
                        TradeStarterCard(
                            name = "ETH",
                            price = "$1,653.01",
                            change = "+0.29%",
                            isPos = true,
                            symbol = "ETH",
                            onClick = onSwapClick
                        )
                        TradeStarterCard(
                            name = "SOL",
                            price = "$145.20",
                            change = "+5.12%",
                            isPos = true,
                            symbol = "SOL",
                            onClick = onSwapClick
                        )
                    }

                    Spacer(modifier = Modifier.height(36.dp))
                }

                // Popular Section Header
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSwapClick() }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Popular",
                            color = PhantomText,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "More Popular",
                            tint = PhantomTextSecondary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

                // Popular Tokens List exactly like screenshot UI placing
                val popularTokens = listOf(
                    PopularTokenData("SPCX", "SPCX", "$12M MC", "$152.69", "+1.60%", true, badgeNum = 1),
                    PopularTokenData("KINS", "KINS", "$13M MC", "$0.0129", "+9.88%", true, badgeNum = 2),
                    PopularTokenData("CARDS", "CARDS", "$300K MC", "$0.30", "+16.96%", true, badgeNum = 0)
                )

                items(popularTokens) { pTok ->
                    PopularTokenRowItem(data = pTok, onClick = {
                        val fakeEntity = TokenBalanceEntity(0, "", pTok.symbol, pTok.name, 0.0, 150.0, 1.6f, "#9945FF")
                        onTokenClick(fakeEntity)
                    })
                }

                // If user has actual token balances > 0, show them below
                if (tokens.isNotEmpty() && totalBalanceUsd > 0.0) {
                    item {
                        Spacer(modifier = Modifier.height(28.dp))
                        Text(
                            text = "Your Tokens",
                            color = PhantomText,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    items(tokens) { tok ->
                        UserTokenRowItem(token = tok, onClick = { onTokenClick(tok) })
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            } else {
                // EXPLORE TAB
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Explore", color = PhantomText, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(14.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = PhantomSurface),
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier.fillMaxWidth().clickable { onSwapClick() }
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(44.dp).clip(CircleShape).background(PhantomPurple.copy(alpha = 0.2f)), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.ShowChart, contentDescription = "Perps", tint = PhantomPurple)
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text("Speculate with Perps", color = PhantomText, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text("Long or short 100s of markets", color = PhantomTextSecondary, fontSize = 13.sp)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = PhantomSurface),
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier.fillMaxWidth().clickable { onSwapClick() }
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(44.dp).clip(CircleShape).background(SolanaTeal.copy(alpha = 0.2f)), contentAlignment = Alignment.Center) {
                                Text("🪙", fontSize = 22.sp)
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text("Trade Real World Assets", color = PhantomText, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text("Popular stocks and commodities", color = PhantomTextSecondary, fontSize = 13.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))
                    Text("Following", color = PhantomText, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Card(colors = CardDefaults.cardColors(containerColor = PhantomSurface), shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(24.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                            Surface(color = PhantomPurple.copy(alpha = 0.15f), shape = RoundedCornerShape(16.dp), modifier = Modifier.size(52.dp)) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Favorite, contentDescription = "Heart", tint = PhantomPurple, modifier = Modifier.size(28.dp))
                                }
                            }
                            Spacer(modifier = Modifier.height(14.dp))
                            Text("Add tokens to your Following list", color = PhantomText, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Stay up to date by 'Following' the tokens you care about the most", color = PhantomTextSecondary, fontSize = 12.sp, textAlign = TextAlign.Center)
                            Spacer(modifier = Modifier.height(16.dp))
                            Surface(color = PhantomSurfaceHigh, shape = RoundedCornerShape(20.dp), modifier = Modifier.clickable { onSwapClick() }) {
                                Text("Browse tokens", color = PhantomText, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(100.dp)) // Padding for bottom floating bar
            }
        }

        // Floating Bottom Area: Search Bar & + FAB right at bottom
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp, vertical = 14.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = PhantomSurface,
                shape = RoundedCornerShape(26.dp),
                border = BorderStroke(1.dp, PhantomBorder),
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .clickable { onSwapClick() }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = PhantomTextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Search Phantom",
                        color = PhantomTextSecondary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            FloatingActionButton(
                onClick = onFaucetClick,
                containerColor = PhantomPurple,
                contentColor = Color.Black,
                shape = CircleShape,
                modifier = Modifier.size(52.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Funds",
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
fun TradeStarterCard(
    name: String,
    price: String,
    change: String,
    isPos: Boolean,
    symbol: String,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = PhantomSurface),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .width(132.dp)
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Actual Logo Rendering
            TokenLogoRenderer(symbol = symbol, sizeDp = 38)
            
            Spacer(modifier = Modifier.height(16.dp))
            Text(name, color = PhantomText, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(price, color = PhantomTextSecondary, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = change,
                color = if (isPos) PhantomGreen else PhantomRed,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

data class PopularTokenData(
    val symbol: String,
    val name: String,
    val marketCap: String,
    val price: String,
    val change: String,
    val isPos: Boolean,
    val badgeNum: Int
)

@Composable
fun PopularTokenRowItem(data: PopularTokenData, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(46.dp)) {
                TokenLogoRenderer(symbol = data.symbol, sizeDp = 44)
                
                // Badge overlay
                if (data.badgeNum > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(18.dp)
                            .clip(CircleShape)
                            .background(if (data.badgeNum == 1) Color(0xFFF59E0B) else Color(0xFF38BDF8))
                            .border(2.dp, Color.Black, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (data.badgeNum == 1) {
                            Text("1", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold)
                        } else {
                            Icon(Icons.Default.CalendarToday, contentDescription = "Cal", tint = Color.Black, modifier = Modifier.size(10.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Text(data.name, color = PhantomText, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(2.dp))
                Text(data.marketCap, color = PhantomTextSecondary, fontSize = 13.sp)
            }
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(data.price, color = PhantomText, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = data.change,
                color = if (data.isPos) PhantomGreen else PhantomRed,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun UserTokenRowItem(token: TokenBalanceEntity, onClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = PhantomSurface),
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier.fillMaxWidth().clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(14.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                TokenLogoRenderer(symbol = token.symbol, sizeDp = 42)
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
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                val totalVal = token.balance * token.priceUsd
                Text(String.format("$%,.2f", totalVal), color = PhantomText, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(String.format("%,.2f %s", token.balance, token.symbol), color = PhantomTextSecondary, fontSize = 13.sp)
            }
        }
    }
}

@Composable
fun TokenLogoRenderer(symbol: String, sizeDp: Int) {
    Box(
        modifier = Modifier
            .size(sizeDp.dp)
            .clip(CircleShape)
            .background(
                when (symbol.uppercase()) {
                    "BTC" -> SolanaOrange
                    "ETH" -> Color(0xFF27272A)
                    "SOL" -> Color(0xFF141416)
                    "SPCX" -> Color(0xFF18181B)
                    "KINS" -> Color(0xFFFED7AA)
                    "CARDS" -> Color(0xFFFEF08A)
                    else -> PhantomSurfaceHigh
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        when (symbol.uppercase()) {
            "BTC" -> Text("₿", color = Color.White, fontSize = (sizeDp * 0.55).sp, fontWeight = FontWeight.ExtraBold)
            "ETH" -> Text("♦", color = Color.White, fontSize = (sizeDp * 0.55).sp)
            "SOL" -> Canvas(modifier = Modifier.size((sizeDp * 0.6).dp)) {
                // Draw clean Solana gradient logo bars
                val w = size.width
                val h = size.height
                val p1 = Path().apply {
                    moveTo(w * 0.2f, h * 0.2f)
                    lineTo(w * 0.9f, h * 0.2f)
                    lineTo(w * 0.8f, h * 0.35f)
                    lineTo(w * 0.1f, h * 0.35f)
                    close()
                }
                val p2 = Path().apply {
                    moveTo(w * 0.1f, h * 0.5f)
                    lineTo(w * 0.8f, h * 0.5f)
                    lineTo(w * 0.9f, h * 0.65f)
                    lineTo(w * 0.2f, h * 0.65f)
                    close()
                }
                val p3 = Path().apply {
                    moveTo(w * 0.2f, h * 0.8f)
                    lineTo(w * 0.9f, h * 0.8f)
                    lineTo(w * 0.8f, h * 0.95f)
                    lineTo(w * 0.1f, h * 0.95f)
                    close()
                }
                drawPath(p1, SolanaTeal)
                drawPath(p2, Color(0xFF9945FF))
                drawPath(p3, SolanaTeal)
            }
            "SPCX" -> Canvas(modifier = Modifier.size((sizeDp * 0.6).dp)) {
                // Draw SPCX swoosh and star
                drawCircle(color = Color.White, radius = size.width * 0.12f, center = Offset(size.width * 0.3f, size.height * 0.5f))
                drawArc(
                    color = Color.White,
                    startAngle = 180f,
                    sweepAngle = 100f,
                    useCenter = false,
                    style = Stroke(width = 4f),
                    topLeft = Offset(0f, 0f),
                    size = size
                )
            }
            "KINS" -> Text("■_■", color = Color(0xFF9A3412), fontSize = (sizeDp * 0.35).sp, fontWeight = FontWeight.ExtraBold)
            "CARDS" -> Text("♠", color = Color(0xFFD97706), fontSize = (sizeDp * 0.55).sp)
            else -> Text(symbol.take(1), color = Color.White, fontWeight = FontWeight.Bold, fontSize = (sizeDp * 0.5).sp)
        }
    }
}
