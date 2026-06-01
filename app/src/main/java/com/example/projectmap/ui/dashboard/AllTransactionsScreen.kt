package com.example.projectmap.ui.dashboard

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllTransactionsScreen(
    onNavigateBack: () -> Unit,
    viewModel: TransactionViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val context = LocalContext.current
    val transactions by viewModel.transactions.collectAsState()
    val formatRp = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID")).apply { maximumFractionDigits = 0 }

    var showEditDialog by remember { mutableStateOf(false) }
    var editTransactionId by remember { mutableStateOf("") }
    var editCategory by remember { mutableStateOf("") }
    var editAmount by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("RIWAYAT TRANSAKSI", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = MaterialTheme.colorScheme.onBackground) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (transactions.isEmpty()) {
                item { Text("Belum ada transaksi.", color = Color.Gray) }
            } else {
                items(transactions) { transaction ->
                    Box(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp)).border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp)).padding(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(transaction.category, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                                val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.forLanguageTag("id-ID"))
                                Text(dateFormat.format(transaction.date), fontSize = 12.sp, color = Color.Gray)
                            }
                            Text(text = (if (transaction.transaction_type == "expense") "-" else "+") + formatRp.format(transaction.amount_of_money).replace("Rp", "Rp "), fontWeight = FontWeight.ExtraBold, color = if (transaction.transaction_type == "expense") Color(0xFFF44336) else Color(0xFF4CAF50), modifier = Modifier.padding(end = 8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = { editTransactionId = transaction.transaction_id; editCategory = transaction.category; editAmount = transaction.amount_of_money.toString(); showEditDialog = true }, modifier = Modifier.size(24.dp)) { Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.Gray) }
                                Spacer(modifier = Modifier.width(12.dp))
                                IconButton(onClick = { viewModel.deleteTransaction(transaction.transaction_id, onSuccess = { Toast.makeText(context.applicationContext, "Dihapus!", Toast.LENGTH_SHORT).show() }, onError = { Toast.makeText(context.applicationContext, it, Toast.LENGTH_SHORT).show() }) }, modifier = Modifier.size(24.dp)) { Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = Color(0xFFF44336)) }
                            }
                        }
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }

        if (showEditDialog) {
            AlertDialog(
                onDismissRequest = { showEditDialog = false },
                title = { Text("Edit Transaksi", fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        OutlinedTextField(value = editCategory, onValueChange = { editCategory = it }, label = { Text("Kategori Baru") }, modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp))
                        OutlinedTextField(value = editAmount, onValueChange = { editAmount = it }, label = { Text("Nominal Baru") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
                    }
                },
                confirmButton = { TextButton(onClick = {
                    val nominalBaru = editAmount.toLongOrNull() ?: 0L
                    if (editCategory.isNotBlank() && nominalBaru > 0) { viewModel.updateTransaction(editTransactionId, editCategory, nominalBaru, onSuccess = { Toast.makeText(context.applicationContext, "Diubah!", Toast.LENGTH_SHORT).show(); showEditDialog = false }, onError = { Toast.makeText(context.applicationContext, it, Toast.LENGTH_SHORT).show() }) }
                }) { Text("Simpan", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold) } },
                dismissButton = { TextButton(onClick = { showEditDialog = false }) { Text("Batal", color = Color.Gray) } }
            )
        }
    }
}