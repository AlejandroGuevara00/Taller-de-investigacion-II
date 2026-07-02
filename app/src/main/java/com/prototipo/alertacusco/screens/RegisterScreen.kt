package com.prototipo.alertacusco.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.prototipo.alertacusco.firebase.FirebaseAuthManager
import com.prototipo.alertacusco.ui.theme.AlertaBlue
import com.prototipo.alertacusco.ui.theme.AlertaBlueDark
import com.prototipo.alertacusco.ui.theme.AlertaCard
import com.prototipo.alertacusco.ui.theme.AlertaPrimaryButton
import com.prototipo.alertacusco.ui.theme.DarkBackground
import com.prototipo.alertacusco.ui.theme.LightBackground

@Composable
fun RegisterScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val authManager = FirebaseAuthManager()
    val isDark = isSystemInDarkTheme()

    var nombres by remember {
        mutableStateOf("")
    }

    var apellidos by remember {
        mutableStateOf("")
    }

    var correo by remember {
        mutableStateOf("")
    }

    var telefono by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                if (isDark) {
                    DarkBackground
                } else {
                    LightBackground
                }
            )
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(28.dp))

        Box(
            modifier = Modifier
                .size(82.dp)
                .clip(CircleShape)
                .background(
                    if (isDark) {
                        AlertaBlueDark.copy(alpha = 0.18f)
                    } else {
                        AlertaBlue.copy(alpha = 0.12f)
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "👤",
                style = MaterialTheme.typography.headlineLarge
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "Crear Cuenta",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Regístrate para reportar incidentes y recibir alertas preventivas.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.70f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        AlertaCard {

            OutlinedTextField(
                value = nombres,
                onValueChange = {
                    nombres = it
                },
                label = {
                    Text("Nombres")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = apellidos,
                onValueChange = {
                    apellidos = it
                },
                label = {
                    Text("Apellidos")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = correo,
                onValueChange = {
                    correo = it
                },
                label = {
                    Text("Correo electrónico")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = telefono,
                onValueChange = {
                    telefono = it
                },
                label = {
                    Text("Teléfono")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                },
                label = {
                    Text("Contraseña")
                },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(18.dp))

            AlertaPrimaryButton(
                text = "Registrarse"
            ) {

                if (
                    nombres.isNotBlank() &&
                    apellidos.isNotBlank() &&
                    correo.isNotBlank() &&
                    telefono.isNotBlank() &&
                    password.isNotBlank()
                ) {

                    authManager.registerUser(
                        correo,
                        password,

                        onSuccess = {

                            Toast.makeText(
                                context,
                                "Usuario registrado correctamente",
                                Toast.LENGTH_LONG
                            ).show()

                            navController.navigate("home") {
                                popUpTo("login") {
                                    inclusive = true
                                }
                            }
                        },

                        onFailure = { error ->

                            Toast.makeText(
                                context,
                                error,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    )

                } else {

                    Toast.makeText(
                        context,
                        "Completa todos los campos",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver al Login")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}