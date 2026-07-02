package com.prototipo.alertacusco.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
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
fun LoginScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val authManager = FirebaseAuthManager()
    val isDark = isSystemInDarkTheme()

    var correo by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                if (isDark) {
                    DarkBackground
                } else {
                    LightBackground
                }
            )
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(90.dp)
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
                    text = "🛡️",
                    style = MaterialTheme.typography.headlineLarge
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Alerta Cusco",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Prevención del delito y seguridad para nuestra comunidad.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.70f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            AlertaCard {

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
                    text = "Iniciar sesión"
                ) {

                    if (
                        correo.isNotBlank() &&
                        password.isNotBlank()
                    ) {

                        authManager.loginUser(
                            correo,
                            password,

                            onSuccess = {

                                Toast.makeText(
                                    context,
                                    "Inicio de sesión correcto",
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
                            "Completa el correo y la contraseña",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = {
                        navController.navigate("register")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Crear cuenta")
                }
            }
        }
    }
}