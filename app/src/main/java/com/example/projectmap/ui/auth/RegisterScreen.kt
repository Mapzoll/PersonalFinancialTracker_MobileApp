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
import androidx.compose.material.icons.filled.Person
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
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit,
    viewModel: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var pin by remember { mutableStateOf("") }

    val isLoading by viewModel.isLoading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NeoBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "DAFTAR AKUN",
            fontSize = 36.sp,
            fontWeight = FontWeight.ExtraBold,
            color = NeoText,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nama Panggilan", fontWeight = FontWeight.Bold) },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
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
                .padding(bottom = 16.dp)
                .border(3.dp, NeoBorder, RoundedCornerShape(8.dp))
        )

        OutlinedTextField(
            value = pin,
            onValueChange = { if (it.length <= 6) pin = it },
            label = { Text("6-Digit Security PIN", fontWeight = FontWeight.Bold) },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            visualTransformation = PasswordVisualTransformation(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NeoBorder,
                unfocusedBorderColor = NeoBorder,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
                .border(3.dp, NeoBorder, RoundedCornerShape(8.dp))
        )

        Button(
            onClick = {
                if (name.isNotEmpty() && email.isNotEmpty() && password.length >= 6 && pin.length == 6) {
                    viewModel.register(email, password, pin, name,
                        onSuccess = {
                            Toast.makeText(context, "Berhasil membuat akun, silakan login sekarang", Toast.LENGTH_LONG).show()
                            onRegisterSuccess()
                        },
                        onError = { errorMsg ->
                            Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                        }
                    )
                } else {
                    Toast.makeText(context, "Data tidak lengkap atau PIN kurang dari 6", Toast.LENGTH_SHORT).show()
                }
            },
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(containerColor = NeoAccentPink),
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
                Text("DAFTAR SEKARANG", color = NeoText, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
            }
        }

        TextButton(onClick = onNavigateToLogin, modifier = Modifier.padding(top = 16.dp)) {
            Text("Sudah punya akun? Masuk", color = Color(0xFF4F46E5), fontWeight = FontWeight.Bold)
        }
    }
}