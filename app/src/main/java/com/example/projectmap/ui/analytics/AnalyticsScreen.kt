package com.example.projectmap.ui.analytics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.DarkMode
import com.example.projectmap.ui.dashboard.TransactionViewModel
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AnalyticsScreen(
    isDarkMode: Boolean,
    onThemeToggle: () -> Unit,
    onNavigateToDashboard: () -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: TransactionViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val totalIncome by viewModel.totalIncome.collectAsState()
    val totalExpense by viewModel.totalExpense.collectAsState()
    val transactions by viewModel.transactions.collectAsState()

    val formatRp = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID"))
    formatRp.maximumFractionDigits = 0

    val totalTransactions = transactions.size
    val expenseCount = transactions.count { it.transaction_type == "expense" }
    val averageExpense = if (expenseCount > 0) totalExpense / expenseCount else 0L
    val netSavings = totalIncome - totalExpense
    val savingsRatio = if (totalIncome > 0) (totalExpense.toFloat() / totalIncome.toFloat()).coerceAtMost(1f) else 0f

    var isSimulatingSync by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        delay(3000L)
        isSimulatingSync = false
    }

    val calendar = Calendar.getInstance()
    val currentMonth = calendar.get(Calendar.MONTH)

    val dailyExpenses = FloatArray(31)
    val dailyActivityCount = IntArray(31)

    transactions.filter {
        calendar.time = it.date
        calendar.get(Calendar.MONTH) == currentMonth
    }.forEach {
        calendar.time = it.date
        val dayIndex = calendar.get(Calendar.DAY_OF_MONTH) - 1

        dailyActivityCount[dayIndex]++
        if (it.transaction_type == "expense") {
            dailyExpenses[dayIndex] += it.amount_of_money.toFloat()
        }
    }

    val categoryTotals = transactions.filter { it.transaction_type == "expense" }
        .groupBy { it.category }
        .mapValues { entry -> entry.value.sumOf { it.amount_of_money }.toFloat() }
    val topCategories = categoryTotals.entries.sortedByDescending { it.value }.take(5).associate { it.key to it.value }

    Scaffold(
        bottomBar = {
            Column(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface)) {
                HorizontalDivider(color = MaterialTheme.colorScheme.outline, thickness = 3.dp)
                Row(
                    modifier = Modifier.fillMaxWidth().navigationBarsPadding().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onNavigateToDashboard) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) { Icon(Icons.Default.Home, contentDescription = "Beranda", tint = Color.Gray); Text("Beranda", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                    }
                    TextButton(onClick = {}) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Analitik", tint = MaterialTheme.colorScheme.primary); Text("Analitik", color = MaterialTheme.colorScheme.primary, fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                    }
                    TextButton(onClick = onNavigateToSettings) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) { Icon(Icons.Default.Settings, contentDescription = "Pengaturan", tint = Color.Gray); Text("Pengaturan", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->

        if (isSimulatingSync) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary, strokeWidth = 4.dp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Menganalisis data...", color = Color.Gray, fontWeight = FontWeight.Bold)
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .statusBarsPadding()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Financial Tracker", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                        IconButton(onClick = onThemeToggle) {
                            Icon(
                                imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = "Tema", tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("ANALITIK DATA", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onBackground)
                }

                Box(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp)).border(3.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp)).shadow(6.dp, RoundedCornerShape(12.dp), clip = false).padding(20.dp)) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("TREN PENGELUARAN (BULAN INI)", fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.padding(bottom = 16.dp))
                        LineChartCanvas(data = dailyExpenses.toList(), lineColor = Color(0xFFF44336))
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))

                Box(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp)).border(3.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp)).shadow(6.dp, RoundedCornerShape(12.dp), clip = false).padding(20.dp)) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("PERBANDINGAN ARUS KAS", fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.padding(bottom = 24.dp))
                        BarChartCanvas(income = totalIncome.toFloat(), expense = totalExpense.toFloat())
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                            Row(verticalAlignment = Alignment.CenterVertically) { Box(modifier = Modifier.size(16.dp).background(Color(0xFF4CAF50)).border(2.dp, Color.Black)); Text(" Masuk", color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                            Row(verticalAlignment = Alignment.CenterVertically) { Box(modifier = Modifier.size(16.dp).background(Color(0xFFF44336)).border(2.dp, Color.Black)); Text(" Keluar", color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))

                Box(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp)).border(3.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp)).shadow(6.dp, RoundedCornerShape(12.dp), clip = false).padding(20.dp)) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("FOKUS PENGELUARAN", fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.padding(bottom = 16.dp))
                        if (topCategories.size >= 3) {
                            RadarChartCanvas(data = topCategories)
                        } else {
                            Box(modifier = Modifier.height(180.dp), contentAlignment = Alignment.Center) {
                                Text("Butuh minimal 3 kategori pengeluaran untuk menampilkan radar.", color = Color.Gray, fontSize = 12.sp, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))

                Box(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp)).border(3.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp)).shadow(6.dp, RoundedCornerShape(12.dp), clip = false).padding(20.dp)) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("INTENSITAS TRANSAKSI", fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.padding(bottom = 16.dp))
                        HeatmapCanvas(activityCounts = dailyActivityCount.toList())
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))

                Box(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp)).border(3.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp)).shadow(6.dp, RoundedCornerShape(12.dp), clip = false).padding(20.dp)) {
                    Column {
                        Text("RASIO TABUNGAN (DEFENSE RATIO)", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.padding(bottom = 12.dp))

                        LinearProgressIndicator(
                            progress = { savingsRatio },
                            modifier = Modifier.fillMaxWidth().height(16.dp).border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp)),
                            color = if (savingsRatio > 0.8f) Color(0xFFF44336) else MaterialTheme.colorScheme.primary,
                            trackColor = Color(0xFFE0E0E0), strokeCap = StrokeCap.Round
                        )
                        Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Terpakai: ${(savingsRatio * 100).toInt()}%", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                            Text(
                                text = "Sisa Tabungan: ${formatRp.format(netSavings).replace("Rp", "Rp ")}",
                                fontSize = 12.sp, fontWeight = FontWeight.ExtraBold,
                                color = if (netSavings >= 0) Color(0xFF4CAF50) else Color(0xFFF44336)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))

                Text("METRIK KUNCI", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.padding(bottom = 12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Box(modifier = Modifier.weight(1f).background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(8.dp)).border(3.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp)).padding(16.dp)) {
                        Column {
                            Text("Volume Data", color = Color(0xFF121212), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text("$totalTransactions", fontWeight = FontWeight.ExtraBold, fontSize = 24.sp, color = Color(0xFF121212))
                            Text("Total Transaksi", color = Color(0xFF121212), fontSize = 10.sp)
                        }
                    }
                    Box(modifier = Modifier.weight(1f).background(MaterialTheme.colorScheme.tertiary, RoundedCornerShape(8.dp)).border(3.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp)).padding(16.dp)) {
                        Column {
                            Text("Rata-rata Keluar", color = Color(0xFF121212), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text(formatRp.format(averageExpense).replace("Rp", "Rp\n"), fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = Color(0xFF121212))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun BarChartCanvas(income: Float, expense: Float) {
    val maxVal = if (income == 0f && expense == 0f) 1f else maxOf(income, expense)
    val outlineColor = MaterialTheme.colorScheme.outline

    Canvas(modifier = Modifier.fillMaxWidth().height(180.dp)) {
        val canvasWidth = size.width; val canvasHeight = size.height; val barWidth = canvasWidth / 4

        drawLine(color = outlineColor, start = Offset(0f, canvasHeight), end = Offset(canvasWidth, canvasHeight), strokeWidth = 6f)
        val incomeHeight = (income / maxVal) * (canvasHeight - 20f); val expenseHeight = (expense / maxVal) * (canvasHeight - 20f)

        if (income > 0) {
            val incomeX = canvasWidth / 4 - (barWidth / 2); val incomeY = canvasHeight - incomeHeight
            drawRect(color = Color.Black, topLeft = Offset(incomeX + 10f, incomeY + 10f), size = Size(barWidth, incomeHeight))
            drawRect(color = Color(0xFF4CAF50), topLeft = Offset(incomeX, incomeY), size = Size(barWidth, incomeHeight))
            drawRect(color = outlineColor, topLeft = Offset(incomeX, incomeY), size = Size(barWidth, incomeHeight), style = Stroke(width = 6f))
        }
        if (expense > 0) {
            val expenseX = (canvasWidth / 4 * 3) - (barWidth / 2); val expenseY = canvasHeight - expenseHeight
            drawRect(color = Color.Black, topLeft = Offset(expenseX + 10f, expenseY + 10f), size = Size(barWidth, expenseHeight))
            drawRect(color = Color(0xFFF44336), topLeft = Offset(expenseX, expenseY), size = Size(barWidth, expenseHeight))
            drawRect(color = outlineColor, topLeft = Offset(expenseX, expenseY), size = Size(barWidth, expenseHeight), style = Stroke(width = 6f))
        }
    }
}

@Composable
fun LineChartCanvas(data: List<Float>, lineColor: Color) {
    val maxVal = data.maxOrNull()?.takeIf { it > 0 } ?: 1f
    val outlineColor = MaterialTheme.colorScheme.outline

    Canvas(modifier = Modifier.fillMaxWidth().height(150.dp)) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val stepX = canvasWidth / (data.size - 1).coerceAtLeast(1)

        val path = Path()
        val fillPath = Path()

        fillPath.moveTo(0f, canvasHeight)

        data.forEachIndexed { index, value ->
            val x = index * stepX
            val y = canvasHeight - ((value / maxVal) * (canvasHeight - 20f))

            if (index == 0) {
                path.moveTo(x, y)
                fillPath.lineTo(x, y)
            } else {
                path.lineTo(x, y)
                fillPath.lineTo(x, y)
            }
            drawCircle(color = Color.Black, radius = 6f, center = Offset(x, y + 4f))
            drawCircle(color = lineColor, radius = 6f, center = Offset(x, y))
        }

        fillPath.lineTo(canvasWidth, canvasHeight)
        fillPath.close()

        drawPath(path = path, color = lineColor, style = Stroke(width = 6f, join = StrokeJoin.Round))
        drawLine(color = outlineColor, start = Offset(0f, canvasHeight), end = Offset(canvasWidth, canvasHeight), strokeWidth = 4f)
    }
}

@Composable
fun HeatmapCanvas(activityCounts: List<Int>) {
    val maxActivity = activityCounts.maxOrNull()?.takeIf { it > 0 } ?: 1
    val baseColor = MaterialTheme.colorScheme.primary

    Canvas(modifier = Modifier.fillMaxWidth().height(120.dp)) {
        val cols = 7
        val rows = 5

        val cellWidth = size.width / cols
        val cellHeight = size.height / rows
        val padding = 4f

        for (i in 0 until 31) {
            val col = i % cols
            val row = i / cols

            val count = activityCounts.getOrElse(i) { 0 }
            val intensity = (count.toFloat() / maxActivity).coerceIn(0f, 1f)

            val color = if (count == 0) Color.LightGray.copy(alpha = 0.3f)
            else baseColor.copy(alpha = 0.3f + (0.7f * intensity))

            drawRoundRect(
                color = color,
                topLeft = Offset(col * cellWidth + padding, row * cellHeight + padding),
                size = Size(cellWidth - padding * 2, cellHeight - padding * 2),
                cornerRadius = CornerRadius(8f, 8f),
                style = Fill
            )
        }
    }
}

@Composable
fun RadarChartCanvas(data: Map<String, Float>) {
    val maxVal = data.values.maxOrNull()?.takeIf { it > 0 } ?: 1f
    val values = data.values.toList()
    val sides = values.size
    val outlineColor = MaterialTheme.colorScheme.outline
    val fillColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
    val dotColor = MaterialTheme.colorScheme.secondary

    Canvas(modifier = Modifier.fillMaxWidth().height(200.dp)) {
        val radius = size.height / 2f - 20f
        val center = Offset(size.width / 2f, size.height / 2f)
        val angleStep = (2 * PI) / sides

        for (step in 1..4) {
            val stepRadius = radius * (step / 4f)
            val webPath = Path()
            for (i in 0 until sides) {
                val angle = i * angleStep - PI / 2
                val x = center.x + stepRadius * cos(angle).toFloat()
                val y = center.y + stepRadius * sin(angle).toFloat()
                if (i == 0) webPath.moveTo(x, y) else webPath.lineTo(x, y)
            }
            webPath.close()
            drawPath(path = webPath, color = Color.Gray.copy(alpha = 0.3f), style = Stroke(width = 2f))
        }

        for (i in 0 until sides) {
            val angle = i * angleStep - PI / 2
            val x = center.x + radius * cos(angle).toFloat()
            val y = center.y + radius * sin(angle).toFloat()
            drawLine(color = Color.Gray.copy(alpha = 0.3f), start = center, end = Offset(x, y), strokeWidth = 2f)
        }

        val dataPath = Path()
        for (i in 0 until sides) {
            val valueRadius = radius * (values[i] / maxVal)
            val angle = i * angleStep - PI / 2
            val x = center.x + valueRadius * cos(angle).toFloat()
            val y = center.y + valueRadius * sin(angle).toFloat()

            if (i == 0) dataPath.moveTo(x, y) else dataPath.lineTo(x, y)
            drawCircle(color = dotColor, radius = 6f, center = Offset(x, y))
        }
        dataPath.close()

        drawPath(path = dataPath, color = fillColor, style = Fill)
        drawPath(path = dataPath, color = outlineColor, style = Stroke(width = 4f, join = StrokeJoin.Round))
    }
}