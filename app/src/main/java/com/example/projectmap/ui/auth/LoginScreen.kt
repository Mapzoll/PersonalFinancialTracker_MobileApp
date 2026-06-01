package com.example.projectmap.ui.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
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
import com.example.projectmap.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val isLoading by viewModel.isLoading.collectAsState()
    var showResetDialog by remember { mutableStateOf(false) }
    var resetEmailInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NeoBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "LOGIN",
            fontSize = 36.sp,
            fontWeight = FontWeight.ExtraBold,
            color = NeoText,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email", fontWeight = FontWeight.Bold) },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NeoBorder,
                unfocusedBorderColor = NeoBorder,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .border(3.dp, NeoBorder, RoundedCornerShape(8.dp))
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password", fontWeight = FontWeight.Bold) },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NeoBorder,
                unfocusedBorderColor = NeoBorder,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .border(3.dp, NeoBorder, RoundedCornerShape(8.dp))
        )

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            TextButton(onClick = { showResetDialog = true }) {
                Text("Lupa Password?", color = Color(0xFFF44336), fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    viewModel.login(
                        email = email,
                        pass = password,
                        onSuccess = {
                            Toast.makeText(context, "Berhasil masuk!", Toast.LENGTH_SHORT).show()
                            onLoginSuccess()
                        },
                        onError = { errorMsg ->
                            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                        }
                    )
                } else {
                    Toast.makeText(context, "Email dan Password wajib diisi", Toast.LENGTH_SHORT).show()
                }
            },
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(containerColor = NeoAccentYellow),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .border(3.dp, NeoBorder, RoundedCornerShape(8.dp))
                .shadow(8.dp, RoundedCornerShape(8.dp), clip = false)
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = NeoText)
            } else {
                Text("MASUK", color = NeoText, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
            }
        }

        TextButton(onClick = onNavigateToRegister, modifier = Modifier.padding(top = 16.dp)) {
            Text("Belum punya akun? Daftar", color = Color(0xFF4F46E5), fontWeight = FontWeight.Bold)
        }

        if (showResetDialog) {
            AlertDialog(
                onDismissRequest = { showResetDialog = false },
                title = { Text("Reset Password", fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        Text("Masukkan email yang terdaftar. Kami akan mengirimkan tautan untuk mengatur ulang password Anda.")
                        OutlinedTextField(
                            value = resetEmailInput,
                            onValueChange = { resetEmailInput = it },
                            label = { Text("Email Anda") },
                            modifier = Modifier.padding(top = 8.dp).fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (resetEmailInput.isNotEmpty()) {
                                viewModel.resetPassword(
                                    email = resetEmailInput,
                                    onSuccess = {
                                        Toast.makeText(context, "Tautan reset telah dikirim ke email Anda!", Toast.LENGTH_LONG).show()
                                        showResetDialog = false
                                        resetEmailInput = ""
                                    },
                                    onError = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
                                )
                            } else {
                                Toast.makeText(context, "Email tidak boleh kosong", Toast.LENGTH_SHORT).show()
                            }
                        }
                    ) { Text("Kirim Tautan", color = NeoPrimary, fontWeight = FontWeight.Bold) }
                },
                dismissButton = {
                    TextButton(onClick = { showResetDialog = false }) { Text("Batal", color = Color.Gray) }
                }
            )
        }
    }
}