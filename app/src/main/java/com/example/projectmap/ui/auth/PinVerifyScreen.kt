package com.example.projectmap.ui.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

@Composable
fun PinVerifyScreen(
    onPinVerified: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    var pinInput by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showForgotPinDialog by remember { mutableStateOf(false) }
    var newPinInput by remember { mutableStateOf("") }

    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "VERIFIKASI PIN",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Masukkan 6-digit PIN keamanan Anda",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = pinInput,
                onValueChange = { if (it.length <= 6) pinInput = it },
                label = { Text("PIN") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Lock", tint = MaterialTheme.colorScheme.onBackground) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onBackground
                ),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (pinInput.length == 6) {
                        isLoading = true
                        val userId = currentUser?.uid ?: return@Button

                        db.collection("users").document(userId).get()
                            .addOnSuccessListener { doc ->
                                val savedPin = doc.getString("security_pin")

                                if (savedPin == pinInput || savedPin.isNullOrEmpty()) {
                                    if (savedPin.isNullOrEmpty()) {
                                        db.collection("users").document(userId)
                                            .set(mapOf("security_pin" to pinInput, "email" to currentUser.email), SetOptions.merge())
                                    }
                                    onPinVerified()
                                } else {
                                    isLoading = false
                                    Toast.makeText(context, "PIN Salah!", Toast.LENGTH_SHORT).show()
                                }
                            }
                            .addOnFailureListener {
                                isLoading = false
                                Toast.makeText(context, "Gagal terhubung ke database", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(context, "PIN harus 6 digit", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD54F)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .border(3.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(8.dp))
                    .shadow(4.dp, RoundedCornerShape(8.dp), clip = false),
                enabled = !isLoading
            ) {
                Text(
                    text = if (isLoading) "MEMERIKSA..." else "MASUK",
                    color = Color.Black,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = onLogout) {
                    Text("Keluar", color = Color(0xFFF44336), fontWeight = FontWeight.Bold)
                }
                TextButton(onClick = { showForgotPinDialog = true }) {
                    Text("Lupa PIN?", color = Color(0xFF2196F3), fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    if (showForgotPinDialog) {
        AlertDialog(
            onDismissRequest = { showForgotPinDialog = false },
            title = { Text("Reset PIN Keamanan", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(
                        text = "Masukkan 6-digit PIN baru Anda. Anda akan langsung masuk setelah menyimpannya.",
                        fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 16.dp)
                    )
                    OutlinedTextField(
                        value = newPinInput,
                        onValueChange = { if (it.length <= 6) newPinInput = it },
                        label = { Text("PIN Baru") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (newPinInput.length == 6) {
                        val userId = currentUser?.uid ?: return@TextButton
                        db.collection("users").document(userId)
                            .set(mapOf("security_pin" to newPinInput, "email" to currentUser.email), SetOptions.merge())
                            .addOnSuccessListener {
                                Toast.makeText(context, "PIN Berhasil Diubah!", Toast.LENGTH_SHORT).show()
                                showForgotPinDialog = false
                                onPinVerified()
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "Gagal memperbarui PIN", Toast.LENGTH_SHORT).show()
                                showForgotPinDialog = false
                            }
                    } else {
                        Toast.makeText(context, "PIN harus 6 digit", Toast.LENGTH_SHORT).show()
                    }
                }) { Text("Simpan & Masuk", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { showForgotPinDialog = false }) { Text("Batal", color = Color.Gray) }
            }
        )
    }
}