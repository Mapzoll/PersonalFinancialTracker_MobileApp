package com.example.projectmap.ui.dashboard

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun DashboardScreen(
    onNavigateToAllTransactions: () -> Unit,
    isDarkMode: Boolean,
    onThemeToggle: () -> Unit,
    onNavigateToAddTransaction: () -> Unit,
    onNavigateToAnalytics: () -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: TransactionViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val displayName = FirebaseAuth.getInstance().currentUser?.displayName ?: "Pengguna"
    val transactions by viewModel.transactions.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var showEditDialog by remember { mutableStateOf(false) }
    var editTransactionId by remember { mutableStateOf("") }
    var editCategory by remember { mutableStateOf("") }
    var editAmount by remember { mutableStateOf("") }
    var editCategoryError by remember { mutableStateOf(false) }
    var editAmountError by remember { mutableStateOf(false) }

    var backPressedOnce by remember { mutableStateOf(false) }
    var isManualRefreshing by remember { mutableStateOf(false) }

    var selectedFilter by remember { mutableStateOf("Semua") }

    val sharedPrefs = remember { context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE) }
    var budgetLimitPercentage by remember { mutableIntStateOf(sharedPrefs.getInt("budget_limit", 80)) }
    var showBudgetDialog by remember { mutableStateOf(false) }
    var budgetInput by remember { mutableStateOf(budgetLimitPercentage.toString()) }

    BackHandler {
        if (backPressedOnce) {
            (context as? android.app.Activity)?.finish()
        } else {
            backPressedOnce = true
            Toast.makeText(context, "Tekan KEMBALI sekali lagi untuk keluar", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(backPressedOnce) {
        if (backPressedOnce) {
            delay(2000L)
            backPressedOnce = false
        }
    }

    val currentHour = remember { Calendar.getInstance().get(Calendar.HOUR_OF_DAY) }
    val greeting = remember(currentHour) {
        when (currentHour) {
            in 5..11 -> "Selamat Pagi,"
            in 12..14 -> "Selamat Siang,"
            in 15..18 -> "Selamat Sore,"
            else -> "Selamat Malam,"
        }
    }

    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.forLanguageTag("id-ID")) }

    val calendar = Calendar.getInstance()
    val todayStart = remember {
        calendar.apply { set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0) }.timeInMillis
    }
    val monthStart = remember {
        calendar.apply { set(Calendar.DAY_OF_MONTH, 1); set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0) }.timeInMillis
    }

    val filteredTransactions = remember(transactions, selectedFilter) {
        when (selectedFilter) {
            "Hari Ini" -> transactions.filter { it.date.time >= todayStart }
            "Bulan Ini" -> transactions.filter { it.date.time >= monthStart }
            else -> transactions
        }
    }

    val dynamicIncome = filteredTransactions.filter { it.transaction_type == "income" }.sumOf { it.amount_of_money }
    val dynamicExpense = filteredTransactions.filter { it.transaction_type == "expense" }.sumOf { it.amount_of_money }
    val dynamicBalance = dynamicIncome - dynamicExpense
    val budgetRatio = if (dynamicIncome > 0) dynamicExpense.toFloat() / dynamicIncome.toFloat() else 0f

    val userBudgetThreshold = budgetLimitPercentage / 100f

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddTransaction, containerColor = MaterialTheme.colorScheme.primary, contentColor = Color.White, shape = RoundedCornerShape(12.dp),
                modifier = Modifier.border(3.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp)).shadow(4.dp, RoundedCornerShape(12.dp))
            ) { Icon(Icons.Default.Add, contentDescription = "Tambah") }
        },
        bottomBar = {
            Column(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface)) {
                HorizontalDivider(color = MaterialTheme.colorScheme.outline, thickness = 3.dp)
                Row(modifier = Modifier.fillMaxWidth().navigationBarsPadding().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
                    TextButton(onClick = { }) { Column(horizontalAlignment = Alignment.CenterHorizontally) { Icon(Icons.Default.Home, contentDescription = "Beranda", tint = MaterialTheme.colorScheme.primary); Text("Beranda", color = MaterialTheme.colorScheme.primary, fontSize = 12.sp, fontWeight = FontWeight.Bold) } }
                    TextButton(onClick = onNavigateToAnalytics) { Column(horizontalAlignment = Alignment.CenterHorizontally) { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Analitik", tint = Color.Gray); Text("Analitik", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold) } }
                    TextButton(onClick = onNavigateToSettings) { Column(horizontalAlignment = Alignment.CenterHorizontally) { Icon(Icons.Default.Settings, contentDescription = "Pengaturan", tint = Color.Gray); Text("Pengaturan", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold) } }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->

        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {

            if (isLoading && transactions.isEmpty() && !isManualRefreshing) {
                LazyColumn(modifier = Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    item { Spacer(modifier = Modifier.height(24.dp)) }
                    item { Box(modifier = Modifier.fillMaxWidth().height(40.dp).clip(RoundedCornerShape(8.dp)).shimmerEffect()) }
                    item { Box(modifier = Modifier.fillMaxWidth().height(120.dp).clip(RoundedCornerShape(12.dp)).shimmerEffect()) }
                    item {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Box(modifier = Modifier.weight(1f).height(80.dp).clip(RoundedCornerShape(8.dp)).shimmerEffect())
                            Box(modifier = Modifier.weight(1f).height(80.dp).clip(RoundedCornerShape(8.dp)).shimmerEffect())
                        }
                    }
                    item { Box(modifier = Modifier.fillMaxWidth().height(250.dp).clip(RoundedCornerShape(12.dp)).shimmerEffect()) }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Column(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Financial Tracker",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Row {
                                    IconButton(onClick = {
                                        coroutineScope.launch {
                                            isManualRefreshing = true
                                            viewModel.refreshData()
                                            delay(1000L)
                                            isManualRefreshing = false
                                            Toast.makeText(context, "Data disinkronkan!", Toast.LENGTH_SHORT).show()
                                        }
                                    }) {
                                        Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = MaterialTheme.colorScheme.onBackground)
                                    }
                                    IconButton(onClick = onThemeToggle) {
                                        Icon(
                                            imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                                            contentDescription = "Tema",
                                            tint = MaterialTheme.colorScheme.onBackground
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(greeting, fontSize = 14.sp, color = Color.Gray)
                            Text("$displayName!", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onBackground)
                        }
                    }

                    item {
                        Column {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                listOf("Hari Ini", "Bulan Ini", "Semua").forEach { filterOpt ->
                                    val isSelected = selectedFilter == filterOpt
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(20.dp))
                                            .background(if (isSelected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.surface)
                                            .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(20.dp))
                                            .clickable { selectedFilter = filterOpt }
                                            .padding(vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = filterOpt,
                                            color = if (isSelected) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onBackground,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth().padding(top = 8.dp, start = 4.dp, end = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Batas Peringatan: $budgetLimitPercentage%", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                Text(
                                    text = "Ubah Batas",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.clickable {
                                        budgetInput = budgetLimitPercentage.toString()
                                        showBudgetDialog = true
                                    }
                                )
                            }
                        }
                    }

                    if (budgetRatio >= userBudgetThreshold && dynamicIncome > 0) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFFFEBEE), RoundedCornerShape(8.dp))
                                    .border(3.dp, Color(0xFFF44336), RoundedCornerShape(8.dp))
                                    .shadow(4.dp, RoundedCornerShape(8.dp), clip = false)
                                    .padding(16.dp)
                            ) {
                                Column {
                                    Text("PERINGATAN ANGGARAN", color = Color(0xFFF44336), fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Pengeluaranmu sudah mencapai ${(budgetRatio * 100).toInt()}% dari pemasukan (Batas: $budgetLimitPercentage%). Segera kurangi pengeluaran!", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    item {
                        Box(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp)).border(3.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp)).shadow(6.dp, RoundedCornerShape(12.dp), clip = false).padding(20.dp)) {
                            Column {
                                Text("TOTAL SALDO", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Text(formatSmartRp(dynamicBalance), color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 32.sp, modifier = Modifier.padding(top = 4.dp))
                            }
                        }
                    }

                    item {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Box(modifier = Modifier.weight(1f).background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp)).border(3.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp)).shadow(4.dp, RoundedCornerShape(8.dp), clip = false).padding(16.dp)) {
                                Column { Text("PEMASUKAN", color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold, fontSize = 10.sp); Text(formatSmartRp(dynamicIncome), color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp) }
                            }
                            Box(modifier = Modifier.weight(1f).background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp)).border(3.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp)).shadow(4.dp, RoundedCornerShape(8.dp), clip = false).padding(16.dp)) {
                                Column { Text("PENGELUARAN", color = Color(0xFFF44336), fontWeight = FontWeight.Bold, fontSize = 10.sp); Text(formatSmartRp(dynamicExpense), color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp) }
                            }
                        }
                    }

                    item {
                        Box(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp)).border(3.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp)).shadow(6.dp, RoundedCornerShape(12.dp), clip = false).padding(20.dp)) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                                Text("ALOKASI", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.padding(bottom = 24.dp))
                                val expenseTransactions = filteredTransactions.filter { it.transaction_type == "expense" }
                                val categoryTotals = expenseTransactions.groupBy { it.category }.mapValues { entry -> entry.value.sumOf { it.amount_of_money } }
                                if (categoryTotals.isEmpty()) { Text("Belum ada data.", color = Color.Gray) } else { DoughnutChart(categoryTotals = categoryTotals) }
                            }
                        }
                    }

                    item {
                        Text("TRANSAKSI TERAKHIR", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.padding(top = 8.dp, bottom = 4.dp))
                    }

                    if (filteredTransactions.isEmpty()) {
                        item {
                            Column(modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(modifier = Modifier.size(72.dp).background(MaterialTheme.colorScheme.surface, CircleShape).border(2.dp, MaterialTheme.colorScheme.outline, CircleShape), contentAlignment = Alignment.Center) {
                                    Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Kosong", modifier = Modifier.size(36.dp), tint = Color.Gray)
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Data Masih Kosong", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)
                                Text("Tidak ada transaksi pada filter waktu ini.", fontSize = 12.sp, color = Color.Gray)
                            }
                        }
                    } else {
                        items(filteredTransactions.take(3)) { transaction ->
                            Box(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp)).border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp)).padding(16.dp)) {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(transaction.category, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                                        Text(dateFormat.format(transaction.date), fontSize = 12.sp, color = Color.Gray)
                                    }
                                    Text(text = (if (transaction.transaction_type == "expense") "-" else "+") + formatSmartRp(transaction.amount_of_money), fontWeight = FontWeight.ExtraBold, color = if (transaction.transaction_type == "expense") Color(0xFFF44336) else Color(0xFF4CAF50), modifier = Modifier.padding(end = 8.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        IconButton(onClick = {
                                            editTransactionId = transaction.transaction_id
                                            editCategory = transaction.category
                                            editAmount = transaction.amount_of_money.toString()
                                            editCategoryError = false
                                            editAmountError = false
                                            showEditDialog = true
                                        }, modifier = Modifier.size(24.dp)) { Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.Gray) }

                                        Spacer(modifier = Modifier.width(12.dp))

                                        IconButton(onClick = { viewModel.deleteTransaction(transaction.transaction_id, onSuccess = { Toast.makeText(context, "Dihapus!", Toast.LENGTH_SHORT).show() }, onError = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }) }, modifier = Modifier.size(24.dp)) { Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = Color(0xFFF44336)) }
                                    }
                                }
                            }
                        }

                        if (filteredTransactions.size > 3) {
                            item {
                                TextButton(onClick = onNavigateToAllTransactions, modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                                    Text("LIHAT LEBIH BANYAK", fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }

            if (isManualRefreshing) {
                Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.3f)), contentAlignment = Alignment.Center) {
                    Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), modifier = Modifier.padding(32.dp).border(3.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp)).shadow(8.dp, RoundedCornerShape(16.dp))) {
                        Row(modifier = Modifier.padding(24.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary, modifier = Modifier.size(36.dp), strokeWidth = 4.dp)
                            Spacer(modifier = Modifier.width(16.dp))
                            Text("Menyinkronkan...", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                        }
                    }
                }
            }
        }

        if (showBudgetDialog) {
            AlertDialog(
                onDismissRequest = { showBudgetDialog = false },
                title = { Text("Atur Batas Anggaran", fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        Text("Masukkan persentase peringatan (1 - 100):", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 8.dp))
                        OutlinedTextField(
                            value = budgetInput,
                            onValueChange = { budgetInput = it },
                            label = { Text("Persentase (%)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        val newLimit = budgetInput.toIntOrNull()?.coerceIn(1, 100) ?: 80
                        budgetLimitPercentage = newLimit
                        sharedPrefs.edit { putInt("budget_limit", newLimit) }
                        showBudgetDialog = false
                        Toast.makeText(context, "Batas peringatan diubah ke $newLimit%", Toast.LENGTH_SHORT).show()
                    }) { Text("Simpan", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold) }
                },
                dismissButton = {
                    TextButton(onClick = { showBudgetDialog = false }) { Text("Batal", color = Color.Gray) }
                }
            )
        }

        if (showEditDialog) {
            AlertDialog(
                onDismissRequest = { showEditDialog = false },
                title = { Text("Edit Transaksi", fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        OutlinedTextField(
                            value = editCategory,
                            onValueChange = {
                                editCategory = it
                                if (it.isNotBlank()) editCategoryError = false
                            },
                            label = { Text("Kategori Baru") },
                            isError = editCategoryError,
                            supportingText = { if (editCategoryError) Text("Kategori tidak boleh kosong!", color = MaterialTheme.colorScheme.error) },
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        )
                        OutlinedTextField(
                            value = editAmount,
                            onValueChange = {
                                editAmount = it
                                if ((it.toLongOrNull() ?: 0L) > 0) editAmountError = false
                            },
                            label = { Text("Nominal Baru") },
                            isError = editAmountError,
                            supportingText = { if (editAmountError) Text("Nominal tidak valid!", color = MaterialTheme.colorScheme.error) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        val nominalBaru = editAmount.toLongOrNull() ?: 0L
                        editCategoryError = editCategory.isBlank()
                        editAmountError = nominalBaru <= 0
                        if (!editCategoryError && !editAmountError) {
                            viewModel.updateTransaction(
                                editTransactionId, editCategory, nominalBaru,
                                onSuccess = {
                                    Toast.makeText(context, "Diubah!", Toast.LENGTH_SHORT).show()
                                    showEditDialog = false
                                },
                                onError = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
                            )
                        }
                    }) { Text("Simpan", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold) }
                },
                dismissButton = {
                    TextButton(onClick = { showEditDialog = false }) { Text("Batal", color = Color.Gray) }
                }
            )
        }
    }
}

fun formatSmartRp(amount: Long): String {
    val absAmount = kotlin.math.abs(amount)
    val sign = if (amount < 0) "-" else ""
    val localeId = Locale.forLanguageTag("id-ID")

    return when {
        absAmount >= 1_000_000_000 -> "$sign Rp ${String.format(localeId, "%.2f M", absAmount / 1_000_000_000.0).replace(",00", "")}"
        absAmount >= 1_000_000 -> "$sign Rp ${String.format(localeId, "%.1f Jt", absAmount / 1_000_000.0).replace(",0", "")}"
        else -> {
            val format = NumberFormat.getCurrencyInstance(localeId)
            format.maximumFractionDigits = 0
            sign + format.format(absAmount).replace("Rp", "Rp ")
        }
    }
}

@Composable
fun DoughnutChart(categoryTotals: Map<String, Long>) {
    val totalExpense = categoryTotals.values.sum().toFloat()
    val colors = listOf(MaterialTheme.colorScheme.tertiary, MaterialTheme.colorScheme.secondary, Color(0xFF4CAF50), Color(0xFF2196F3), Color(0xFFFF5722))
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(180.dp)) {
        Canvas(modifier = Modifier.size(160.dp)) {
            var startAngle = -90f; var colorIndex = 0
            categoryTotals.forEach { (_, amount) ->
                val sweepAngle = (amount.toFloat() / totalExpense) * 360f
                drawArc(color = colors[colorIndex % colors.size], startAngle = startAngle, sweepAngle = sweepAngle, useCenter = false, style = Stroke(width = 40f, cap = StrokeCap.Butt))
                drawArc(color = Color.Black, startAngle = startAngle, sweepAngle = sweepAngle, useCenter = false, style = Stroke(width = 40f, cap = StrokeCap.Butt))
                drawArc(color = colors[colorIndex % colors.size], startAngle = startAngle, sweepAngle = sweepAngle, useCenter = false, style = Stroke(width = 34f, cap = StrokeCap.Butt))
                startAngle += sweepAngle; colorIndex++
            }
        }
        val biggestCategory = categoryTotals.maxByOrNull { it.value }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Terbesar", fontSize = 10.sp, color = Color.Gray)
            Text(biggestCategory?.key ?: "-", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onBackground)
        }
    }
}

fun Modifier.shimmerEffect(): Modifier = this.composed {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerAnim"
    )
    val brush = Brush.linearGradient(
        colors = listOf(Color.LightGray.copy(alpha = 0.6f), Color.LightGray.copy(alpha = 0.2f), Color.LightGray.copy(alpha = 0.6f)),
        start = Offset.Zero,
        end = Offset(x = translateAnim, y = translateAnim)
    )
    Modifier.background(brush)
}