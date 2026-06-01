package com.example.projectmap.ui.settings

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectmap.ui.auth.AuthViewModel
import com.example.projectmap.ui.dashboard.Transaction
import com.example.projectmap.ui.dashboard.TransactionViewModel
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun SettingsScreen(
    isDarkMode: Boolean,
    onThemeToggle: () -> Unit,
    onNavigateToDashboard: () -> Unit,
    onNavigateToAnalytics: () -> Unit,
    onLogoutSuccess: () -> Unit,
    authViewModel: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    transactionViewModel: TransactionViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val context = LocalContext.current
    val user = authViewModel.currentUser
    val userEmail = user?.email ?: "Tidak ada email"
    val userName = user?.displayName ?: "Pengguna"
    var showPinDialog by remember { mutableStateOf(false) }
    var newPinInput by remember { mutableStateOf("") }

    val transactions by transactionViewModel.transactions.collectAsState()

    var isSimulatingSync by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        delay(2000L)
        isSimulatingSync = false
    }

    val csvLauncher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("text/csv")) { uri ->
        if (uri != null) {
            exportToCsv(context, transactions, uri)
            Toast.makeText(context, " Laporan CSV Berhasil Disimpan!", Toast.LENGTH_LONG).show()
        }
    }

    val pdfLauncher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/pdf")) { uri ->
        if (uri != null) {
            exportToPdf(context, transactions, uri)
            Toast.makeText(context, "Laporan PDF Berhasil Disimpan!", Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        bottomBar = {
            Column(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface)) {
                HorizontalDivider(color = MaterialTheme.colorScheme.outline, thickness = 3.dp)
                Row(modifier = Modifier.fillMaxWidth().navigationBarsPadding().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
                    TextButton(onClick = onNavigateToDashboard) { Column(horizontalAlignment = Alignment.CenterHorizontally) { Icon(Icons.Default.Home, contentDescription = "Beranda", tint = Color.Gray); Text("Beranda", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold) } }
                    TextButton(onClick = onNavigateToAnalytics) { Column(horizontalAlignment = Alignment.CenterHorizontally) { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Analitik", tint = Color.Gray); Text("Analitik", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold) } }
                    TextButton(onClick = {}) { Column(horizontalAlignment = Alignment.CenterHorizontally) { Icon(Icons.Default.Settings, contentDescription = "Pengaturan", tint = MaterialTheme.colorScheme.primary); Text("Pengaturan", color = MaterialTheme.colorScheme.primary, fontSize = 12.sp, fontWeight = FontWeight.Bold) } }
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
                    Text("Menyinkronkan data pengaturan...", color = Color.Gray, fontWeight = FontWeight.Bold)
                }
            }
        }
        else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues).statusBarsPadding().padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                item {
                    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("Financial Tracker", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                            IconButton(onClick = onThemeToggle) {
                                Icon(
                                    imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                                    contentDescription = "Tema",
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("PENGATURAN AKUN", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onBackground)
                    }
                }

                item {
                    Box(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp)).border(3.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp)).shadow(6.dp, RoundedCornerShape(12.dp), clip = false).padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            val initialLetter = userName.firstOrNull()?.uppercase() ?: "U"
                            Box(
                                modifier = Modifier.size(64.dp).background(MaterialTheme.colorScheme.primary, CircleShape).border(3.dp, MaterialTheme.colorScheme.outline, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(initialLetter, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text("PROFIL SAAT INI", color = Color.Gray, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Text(userName, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = MaterialTheme.colorScheme.onBackground)
                                Text(userEmail, fontSize = 14.sp, color = MaterialTheme.colorScheme.onBackground)
                            }
                        }
                    }
                }

                item {
                    Box(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp)).border(3.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp)).shadow(6.dp, RoundedCornerShape(12.dp), clip = false).padding(20.dp)) {
                        Column {
                            Text("EKSPOR LAPORAN", color = Color.Gray, fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.padding(bottom = 12.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                Button(
                                    onClick = { csvLauncher.launch("Laporan_Keuangan.csv") },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                                    modifier = Modifier.weight(1f).height(50.dp).border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp)).shadow(2.dp, RoundedCornerShape(8.dp), clip = false)
                                ) { Text("Unduh CSV", color = Color.White, fontWeight = FontWeight.Bold) }

                                Button(
                                    onClick = { pdfLauncher.launch("Laporan_Keuangan.pdf") },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)),
                                    modifier = Modifier.weight(1f).height(50.dp).border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp)).shadow(2.dp, RoundedCornerShape(8.dp), clip = false)
                                ) { Text("Unduh PDF", color = Color.White, fontWeight = FontWeight.Bold) }
                            }
                        }
                    }
                }

                item {
                    Button(
                        onClick = { showPinDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        modifier = Modifier.fillMaxWidth().height(56.dp).border(3.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp)).shadow(4.dp, RoundedCornerShape(8.dp), clip = false)
                    ) { Text("GANTI PIN KEAMANAN", color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.ExtraBold) }
                }

                item {
                    Button(
                        onClick = { authViewModel.logout(onSuccess = { Toast.makeText(context, "Berhasil Keluar", Toast.LENGTH_SHORT).show(); onLogoutSuccess() }) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF121212)),
                        modifier = Modifier.fillMaxWidth().height(56.dp).border(3.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp)).shadow(4.dp, RoundedCornerShape(8.dp), clip = false)
                    ) { Text("KELUAR AKUN", color = Color.White, fontWeight = FontWeight.ExtraBold) }
                }
            }
        }

        if (showPinDialog) {
            AlertDialog(
                onDismissRequest = { showPinDialog = false }, title = { Text("Masukkan PIN Baru", fontWeight = FontWeight.Bold) },
                text = {
                    OutlinedTextField(value = newPinInput, onValueChange = { if (it.length <= 6) newPinInput = it }, label = { Text("6 Digit PIN") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
                },
                confirmButton = { TextButton(onClick = { if (newPinInput.length == 6) { authViewModel.updatePin(newPinInput, onSuccess = { Toast.makeText(context, "PIN Diperbarui!", Toast.LENGTH_SHORT).show(); showPinDialog = false; newPinInput = "" }, onError = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }) } }) { Text("Simpan", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold) } },
                dismissButton = { TextButton(onClick = { showPinDialog = false }) { Text("Batal", color = Color.Gray) } }
            )
        }
    }
}

fun exportToCsv(context: Context, transactions: List<Transaction>, uri: Uri) {
    try {
        val dateFormat = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.forLanguageTag("id-ID"))
        val sb = java.lang.StringBuilder()
        sb.append("Tanggal,Kategori,Tipe,Nominal\n")
        transactions.forEach { t ->
            val type = if(t.transaction_type == "expense") "Pengeluaran" else "Pemasukan"
            sb.append("${dateFormat.format(t.date)},${t.category},$type,${t.amount_of_money}\n")
        }
        context.contentResolver.openOutputStream(uri)?.use { it.write(sb.toString().toByteArray()) }
    } catch (e: Exception) {
        Toast.makeText(context, "Gagal menyimpan CSV: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}

fun exportToPdf(context: Context, transactions: List<Transaction>, uri: Uri) {
    try {
        val pdfDocument = android.graphics.pdf.PdfDocument()
        val pageInfo = android.graphics.pdf.PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas
        val paint = android.graphics.Paint()

        paint.textSize = 20f
        paint.isFakeBoldText = true
        canvas.drawText("Laporan Arus Kas Keuangan", 50f, 50f, paint)

        paint.textSize = 12f
        paint.isFakeBoldText = false
        var yPosition = 100f

        canvas.drawText("Tanggal", 50f, yPosition, paint)
        canvas.drawText("Kategori", 200f, yPosition, paint)
        canvas.drawText("Tipe", 350f, yPosition, paint)
        canvas.drawText("Nominal", 450f, yPosition, paint)
        canvas.drawLine(50f, yPosition + 5f, 545f, yPosition + 5f, paint)
        yPosition += 25f

        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.forLanguageTag("id-ID"))
        val formatUang = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID")).apply { maximumFractionDigits = 0 }

        val maxRowsPerPage = 35
        transactions.take(maxRowsPerPage).forEach { t ->
            val type = if(t.transaction_type == "expense") "Keluar" else "Masuk"
            val nominalStr = formatUang.format(t.amount_of_money).replace("Rp", "Rp ")

            canvas.drawText(dateFormat.format(t.date), 50f, yPosition, paint)
            canvas.drawText(t.category, 200f, yPosition, paint)
            canvas.drawText(type, 350f, yPosition, paint)
            canvas.drawText(nominalStr, 450f, yPosition, paint)
            yPosition += 20f
        }

        if (transactions.size > maxRowsPerPage) {
            paint.color = android.graphics.Color.GRAY
            canvas.drawText("*Hanya menampilkan $maxRowsPerPage transaksi terbaru dalam PDF ini.", 50f, yPosition + 20f, paint)
        }

        pdfDocument.finishPage(page)
        context.contentResolver.openOutputStream(uri)?.use { pdfDocument.writeTo(it) }
        pdfDocument.close()
    } catch (e: Exception) {
        Toast.makeText(context, "Gagal menyimpan PDF: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}