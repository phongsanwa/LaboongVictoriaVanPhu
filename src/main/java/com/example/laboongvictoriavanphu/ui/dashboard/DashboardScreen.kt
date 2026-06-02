package com.example.laboongvictoriavanphu.ui.dashboard

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.laboongvictoriavanphu.ui.theme.*

// ── Data models ──────────────────────────────────────────────────────────────

data class NearbyStore(
    val name: String,
    val address: String,
    val distance: String,
    val isOpen: Boolean
)

data class Transaction(
    val storeName: String,
    val date: String,
    val points: Int,
    val isEarned: Boolean
)

data class Voucher(
    val title: String,
    val description: String,
    val expiryDate: String,
    val discount: String
)

// ── Main Dashboard ────────────────────────────────────────────────────────────

@Composable
fun DashboardScreen() {
    val currentPoints = 2_350
    val nextMilestone = 3_000
    val userName = "Nguyễn Văn A"

    val stores = listOf(
        NearbyStore("Laboong Quận 1", "12 Nguyễn Huệ, Q.1", "0.3 km", true),
        NearbyStore("Laboong Quận 3", "45 Võ Văn Tần, Q.3", "1.2 km", true),
        NearbyStore("Laboong Bình Thạnh", "88 Đinh Tiên Hoàng, BT", "2.7 km", false),
    )

    val transactions = listOf(
        Transaction("Laboong Quận 1", "Hôm nay, 09:30", 150, true),
        Transaction("Laboong Quận 3", "Hôm qua, 14:15", 200, true),
        Transaction("Đổi voucher giảm 20%", "28/05/2026", -500, false),
    )

    val vouchers = listOf(
        Voucher(
            title = "Giảm 20% cho đơn hàng tiếp theo",
            description = "Áp dụng cho tất cả sản phẩm tại Laboong",
            expiryDate = "30/06/2026",
            discount = "20%"
        ),
        Voucher(
            title = "Miễn phí 1 ly cà phê",
            description = "Đổi điểm lấy ly Espresso size M",
            expiryDate = "15/06/2026",
            discount = "FREE"
        ),
    )

    Scaffold(
        containerColor = LaboongBackground,
        topBar = { DashboardTopBar(userName) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // Points hero card
            PointsHeroCard(currentPoints = currentPoints, nextMilestone = nextMilestone)

            Spacer(modifier = Modifier.height(16.dp))

            // QR scan CTA
            QRScanButton()

            Spacer(modifier = Modifier.height(20.dp))

            // Voucher banners
            SectionTitle("Ưu đãi dành cho bạn")
            Spacer(modifier = Modifier.height(10.dp))
            VoucherBannerList(vouchers)

            Spacer(modifier = Modifier.height(20.dp))

            // Nearby stores
            SectionTitle("Cửa hàng gần bạn")
            Spacer(modifier = Modifier.height(10.dp))
            NearbyStoreList(stores)

            Spacer(modifier = Modifier.height(20.dp))

            // Recent transactions
            SectionTitle("Lịch sử giao dịch")
            Spacer(modifier = Modifier.height(10.dp))
            RecentTransactionList(transactions)

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// ── Top Bar ───────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardTopBar(userName: String) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = "Xin chào,",
                    fontSize = 12.sp,
                    color = LaboongTextSecondary
                )
                Text(
                    text = userName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = LaboongTextPrimary
                )
            }
        },
        navigationIcon = {
            Box(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(LaboongOrange.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = LaboongOrange,
                    modifier = Modifier.size(22.dp)
                )
            }
        },
        actions = {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Thông báo",
                    tint = LaboongTextPrimary
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = LaboongSurface
        )
    )
}

// ── Points Hero Card ──────────────────────────────────────────────────────────

@Composable
private fun PointsHeroCard(currentPoints: Int, nextMilestone: Int) {
    val progress = currentPoints.toFloat() / nextMilestone.toFloat()
    val pointsLeft = nextMilestone - currentPoints

    var animationPlayed by remember { mutableStateOf(false) }
    val animatedProgress by animateFloatAsState(
        targetValue = if (animationPlayed) progress else 0f,
        animationSpec = tween(durationMillis = 1200),
        label = "progress"
    )
    LaunchedEffect(Unit) { animationPlayed = true }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(LaboongOrange, LaboongOrangeDark)
                )
            )
            .padding(24.dp)
    ) {
        Column {
            Text(
                text = "Điểm thưởng của bạn",
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.85f)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "%,d".format(currentPoints),
                    fontSize = 52.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    lineHeight = 56.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "điểm",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.9f),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Mốc tiếp theo: %,d điểm".format(nextMilestone),
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.85f)
                )
                Text(
                    text = "Còn $pointsLeft điểm",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LaboongYellow
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(50)),
                color = LaboongYellow,
                trackColor = Color.White.copy(alpha = 0.3f),
                strokeCap = StrokeCap.Round,
                gapSize = 0.dp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "0", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                Text(
                    text = "%,d".format(nextMilestone),
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Milestone reward hint
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.18f))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = LaboongYellow,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Đạt ${"%,d".format(nextMilestone)} điểm → Đổi voucher giảm 50K",
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

// ── QR Scan Button ────────────────────────────────────────────────────────────

@Composable
private fun QRScanButton() {
    Button(
        onClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = LaboongOrange
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
    ) {
        Icon(
            imageVector = Icons.Default.QrCodeScanner,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = Color.White
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = "Quét mã QR để tích điểm",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

// ── Section Title ─────────────────────────────────────────────────────────────

@Composable
private fun SectionTitle(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = LaboongTextPrimary
        )
        TextButton(onClick = {}) {
            Text(
                text = "Xem tất cả",
                fontSize = 13.sp,
                color = LaboongOrange
            )
        }
    }
}

// ── Voucher Banners ───────────────────────────────────────────────────────────

@Composable
private fun VoucherBannerList(vouchers: List<Voucher>) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        vouchers.forEach { voucher ->
            VoucherBannerItem(voucher)
        }
    }
}

@Composable
private fun VoucherBannerItem(voucher: Voucher) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(LaboongSurface)
            .border(1.dp, LaboongDivider, RoundedCornerShape(16.dp))
            .padding(0.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Discount badge
        Box(
            modifier = Modifier
                .width(72.dp)
                .fillMaxHeight()
                .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(LaboongOrange, LaboongOrangeDark)
                    )
                )
                .padding(vertical = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = voucher.discount,
                fontSize = if (voucher.discount == "FREE") 14.sp else 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f).padding(vertical = 14.dp)) {
            Text(
                text = voucher.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = LaboongTextPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = voucher.description,
                fontSize = 12.sp,
                color = LaboongTextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(LaboongOrange.copy(alpha = 0.1f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "HSD: ${voucher.expiryDate}",
                        fontSize = 11.sp,
                        color = LaboongOrange,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(14.dp))
    }
}

// ── Nearby Stores ─────────────────────────────────────────────────────────────

@Composable
private fun NearbyStoreList(stores: List<NearbyStore>) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        stores.forEach { store ->
            NearbyStoreItem(store)
        }
    }
}

@Composable
private fun NearbyStoreItem(store: NearbyStore) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(LaboongSurface)
            .border(1.dp, LaboongDivider, RoundedCornerShape(16.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Store icon
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(LaboongOrange.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "☕", fontSize = 22.sp)
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = store.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = LaboongTextPrimary
            )
            Spacer(modifier = Modifier.height(3.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = LaboongTextSecondary,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = store.address,
                    fontSize = 12.sp,
                    color = LaboongTextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = store.distance,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = LaboongOrange
            )
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        if (store.isOpen) LaboongGreen.copy(alpha = 0.12f)
                        else LaboongTextHint.copy(alpha = 0.3f)
                    )
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = if (store.isOpen) "Mở cửa" else "Đóng cửa",
                    fontSize = 11.sp,
                    color = if (store.isOpen) LaboongGreen else LaboongTextSecondary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

// ── Recent Transactions ───────────────────────────────────────────────────────

@Composable
private fun RecentTransactionList(transactions: List<Transaction>) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(LaboongSurface)
            .border(1.dp, LaboongDivider, RoundedCornerShape(16.dp))
    ) {
        transactions.forEachIndexed { index, tx ->
            TransactionItem(tx)
            if (index < transactions.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 14.dp),
                    color = LaboongDivider,
                    thickness = 0.8.dp
                )
            }
        }
    }
}

@Composable
private fun TransactionItem(tx: Transaction) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(
                    if (tx.isEarned) LaboongOrange.copy(alpha = 0.1f)
                    else LaboongRed.copy(alpha = 0.1f)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (tx.isEarned) "⭐" else "🎁",
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = tx.storeName,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = LaboongTextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = tx.date,
                fontSize = 12.sp,
                color = LaboongTextSecondary
            )
        }

        Text(
            text = "${if (tx.isEarned) "+" else ""}${tx.points} điểm",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = if (tx.isEarned) LaboongGreen else LaboongRed
        )
    }
}

// ── Preview ───────────────────────────────────────────────────────────────────

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardScreenPreview() {
    LaboongVictoriaVanPhuTheme {
        DashboardScreen()
    }
}
