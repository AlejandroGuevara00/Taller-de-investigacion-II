package com.prototipo.alertacusco.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.prototipo.alertacusco.ui.theme.AlertaCard
import com.prototipo.alertacusco.ui.theme.AlertaGreen
import com.prototipo.alertacusco.ui.theme.AlertaPrimaryButton
import com.prototipo.alertacusco.ui.theme.AlertaSecondaryButton
import com.prototipo.alertacusco.ui.theme.DarkBackground
import com.prototipo.alertacusco.ui.theme.LightBackground

@Composable
fun ContactoEmergenciaScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val isDark = isSystemInDarkTheme()

    val preferencias =
        context.getSharedPreferences(
            "contacto_emergencia",
            Context.MODE_PRIVATE
        )

    var nombreContacto by remember {
        mutableStateOf(
            preferencias.getString("nombre", "") ?: ""
        )
    }

    var telefonoContacto by remember {
        mutableStateOf(
            preferencias.getString("telefono", "") ?: ""
        )
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
            .padding(16.dp)
    ) {

        Text(
            text = "Contacto de emergencia",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Guarda un contacto de confianza para comunicarte rápidamente.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(
                alpha = 0.70f
            )
        )

        Spacer(modifier = Modifier.height(18.dp))

        AlertaCard {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(
                            AlertaGreen.copy(
                                alpha = 0.16f
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("📞")
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "Persona de confianza",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Este contacto se guarda solo en tu dispositivo.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.72f
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            OutlinedTextField(
                value = nombreContacto,
                onValueChange = {
                    nombreContacto = it
                },
                label = {
                    Text("Nombre del contacto")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = telefonoContacto,
                onValueChange = {
                    telefonoContacto = it
                },
                label = {
                    Text("Número de teléfono")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(18.dp))

            AlertaPrimaryButton(
                text = "Guardar contacto"
            ) {

                if (
                    nombreContacto.isNotBlank() &&
                    telefonoContacto.isNotBlank()
                ) {
                    preferencias.edit()
                        .putString("nombre", nombreContacto)
                        .putString("telefono", telefonoContacto)
                        .apply()

                    Toast.makeText(
                        context,
                        "Contacto guardado correctamente",
                        Toast.LENGTH_LONG
                    ).show()

                } else {
                    Toast.makeText(
                        context,
                        "Completa el nombre y el teléfono",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            AlertaSecondaryButton(
                text = "Llamar contacto"
            ) {

                if (telefonoContacto.isNotBlank()) {

                    val intent =
                        Intent(
                            Intent.ACTION_DIAL,
                            Uri.parse("tel:$telefonoContacto")
                        )

                    context.startActivity(intent)

                } else {
                    Toast.makeText(
                        context,
                        "Primero registra un número de emergencia",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = {
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver")
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}