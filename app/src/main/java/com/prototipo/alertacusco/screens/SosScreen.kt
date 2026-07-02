package com.prototipo.alertacusco.screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.prototipo.alertacusco.location.LocationManager
import com.prototipo.alertacusco.ui.theme.AlertaCard
import com.prototipo.alertacusco.ui.theme.AlertaPrimaryButton
import com.prototipo.alertacusco.ui.theme.AlertaRed
import com.prototipo.alertacusco.ui.theme.AlertaSecondaryButton
import com.prototipo.alertacusco.ui.theme.DarkBackground
import com.prototipo.alertacusco.ui.theme.LightBackground

@Composable
fun SosScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val locationManager = LocationManager(context)
    val isDark = isSystemInDarkTheme()

    var latitud by remember {
        mutableStateOf<Double?>(null)
    }

    var longitud by remember {
        mutableStateOf<Double?>(null)
    }

    fun obtenerUbicacion() {
        locationManager.obtenerUbicacion(
            onSuccess = { lat, lng ->
                latitud = lat
                longitud = lng

                Toast.makeText(
                    context,
                    "Ubicación obtenida correctamente",
                    Toast.LENGTH_LONG
                ).show()
            },
            onFailure = { error ->
                Toast.makeText(
                    context,
                    error,
                    Toast.LENGTH_LONG
                ).show()
            }
        )
    }

    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { concedido ->

            if (concedido) {
                obtenerUbicacion()
            } else {
                Toast.makeText(
                    context,
                    "Permiso de ubicación denegado",
                    Toast.LENGTH_LONG
                ).show()
            }
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
            text = "SOS de emergencia",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Comparte tu ubicación actual en caso de una situación de riesgo.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(
                alpha = 0.70f
            )
        )

        Spacer(modifier = Modifier.height(18.dp))

        AlertaCard {

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .clip(CircleShape)
                        .background(
                            AlertaRed.copy(
                                alpha = 0.16f
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "SOS",
                        style = MaterialTheme.typography.headlineMedium,
                        color = AlertaRed
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Botón de auxilio rápido",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Obtén tu ubicación GPS y compártela por WhatsApp, SMS, correo u otra aplicación.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.72f
                    )
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            AlertaPrimaryButton(
                text = "Obtener mi ubicación"
            ) {

                val permiso =
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )

                if (permiso == PackageManager.PERMISSION_GRANTED) {
                    obtenerUbicacion()
                } else {
                    permissionLauncher.launch(
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        AlertaCard {

            Text(
                text = "Ubicación actual",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (latitud != null && longitud != null) {

                Text(
                    text = "✅ Ubicación lista para compartir",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text("Latitud: $latitud")
                Text("Longitud: $longitud")

                Spacer(modifier = Modifier.height(16.dp))

                AlertaPrimaryButton(
                    text = "Compartir ubicación de emergencia"
                ) {

                    val enlaceMapa =
                        "https://www.google.com/maps?q=$latitud,$longitud"

                    val mensaje =
                        "SOS. Necesito ayuda. Esta es mi ubicación actual: $enlaceMapa"

                    val intent =
                        Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(
                                Intent.EXTRA_TEXT,
                                mensaje
                            )
                        }

                    context.startActivity(
                        Intent.createChooser(
                            intent,
                            "Compartir ubicación de emergencia"
                        )
                    )
                }

            } else {

                Text(
                    text = "📍 Ubicación pendiente",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Primero debes obtener tu ubicación actual.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.72f
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        AlertaCard {

            Text(
                text = "Contacto de emergencia",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Registra o llama rápidamente a una persona de confianza.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.72f
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            AlertaSecondaryButton(
                text = "Ir a contacto de emergencia"
            ) {
                navController.navigate("contacto_emergencia")
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