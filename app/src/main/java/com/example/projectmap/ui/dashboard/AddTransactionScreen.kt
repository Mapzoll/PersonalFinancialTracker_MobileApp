package com.example.projectmap.ui.dashboard

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    onNavigateBack: () -> Unit,
    @Suppress("UNUSED_PARAMETER") onLogout: () -> Unit,
    viewModel: TransactionViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val context = LocalContext.current
    var nlpInput by remember { mutableStateOf("") }
    var isExpense by remember { mutableStateOf(true) }

    val predefinedCategories = listOf("Makanan", "Transportasi", "Hiburan", "Tagihan", "Gaji", "Lainnya")
    var selectedCategory by remember { mutableStateOf(predefinedCategories[0]) }
    var customCategory by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TAMBAH TRANSAKSI", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = MaterialTheme.colorScheme.onBackground) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text("Pencatatan Otomatis (NLP)", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                OutlinedTextField(
                    value = nlpInput, onValueChange = { nlpInput = it },
                    placeholder = { Text("Makan siang 50000") },
                    modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                    )
                )
            }

            item {
                Button(
                    onClick = {
                        val parts = nlpInput.split(" ")
                        val extractedAmount = parts.lastOrNull()?.filter { it.isDigit() }
                        val extractedCategory = parts.dropLast(1).joinToString(" ")

                        if (!extractedAmount.isNullOrBlank() && extractedCategory.isNotBlank()) {
                            amount = extractedAmount
                            selectedCategory = "Lainnya"
                            customCategory = extractedCategory
                        } else {
                            Toast.makeText(context, "Format salah! Contoh: Bakso 15000", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                    modifier = Modifier.fillMaxWidth().height(50.dp).border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp)).shadow(2.dp, RoundedCornerShape(8.dp), clip = false)
                ) { Text("PROSES OTOMATIS", color = Color(0xFF121212), fontWeight = FontWeight.Bold) }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Detail Transaksi", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = isExpense, onClick = { isExpense = true }, colors = RadioButtonDefaults.colors(selectedColor = Color(0xFFF44336)))
                    Text("Pengeluaran", color = MaterialTheme.colorScheme.onBackground)
                    Spacer(modifier = Modifier.width(16.dp))
                    RadioButton(selected = !isExpense, onClick = { isExpense = false }, colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF4CAF50)))
                    Text("Pemasukan", color = MaterialTheme.colorScheme.onBackground)
                }
            }

            item {
                Text("Kategori", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.padding(bottom = 8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    predefinedCategories.take(3).forEach { cat ->
                        CategoryChip(text = cat, isSelected = selectedCategory == cat, onClick = { selectedCategory = cat }, modifier = Modifier.weight(1f))
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    predefinedCategories.drop(3).forEach { cat ->
                        CategoryChip(text = cat, isSelected = selectedCategory == cat, onClick = { selectedCategory = cat }, modifier = Modifier.weight(1f))
                    }
                }
            }

            if (selectedCategory == "Lainnya") {
                item {
                    OutlinedTextField(
                        value = customCategory, onValueChange = { customCategory = it },
                        label = { Text("Tulis Kategori Spesifik") },
                        modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.onBackground,
                            unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                        )
                    )
                }
            }

            item {
                OutlinedTextField(
                    value = amount, onValueChange = { amount = it },
                    label = { Text("Nominal (Rp)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                    )
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    enabled = !isLoading,
                    onClick = {
                        val amountLong = amount.toLongOrNull() ?: 0L
                        val finalCategory = if (selectedCategory == "Lainnya") customCategory else selectedCategory

                        if (finalCategory.isNotBlank() && amountLong > 0) {
                            isLoading = true
                            val type = if (isExpense) "expense" else "income"

                            viewModel.addTransaction(
                                type, amountLong, finalCategory,
                                onSuccess = {
                                    isLoading = false
                                    Toast.makeText(context, "Transaksi Berhasil Disimpan!", Toast.LENGTH_LONG).show()
                                    onNavigateBack()
                                },
                                onError = {
                                    isLoading = false
                                    Toast.makeText(context, "Gagal: $it", Toast.LENGTH_LONG).show()
                                }
                            )
                        } else {
                            Toast.makeText(context, "Isi data dengan benar!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        disabledContainerColor = Color.Gray
                    ),
                    modifier = Modifier.fillMaxWidth().height(60.dp).border(3.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp)).shadow(4.dp, RoundedCornerShape(8.dp), clip = false)
                ) {
                    Text(
                        text = if (isLoading) " MENYIMPAN..." else "SIMPAN TRANSAKSI",
                        color = if (isLoading) Color.White else Color(0xFF121212),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryChip(text: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val bgColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    val textColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onBackground
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .height(40.dp)
            .background(bgColor, RoundedCornerShape(8.dp))
            .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
            .clickable { onClick() }
    ) {
        Text(text, color = textColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}