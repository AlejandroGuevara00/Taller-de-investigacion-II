package com.prototipo.alertacusco.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.prototipo.alertacusco.ai.ClasificadorRiesgo
import com.prototipo.alertacusco.firebase.FirestoreManager
import com.prototipo.alertacusco.location.GeocodingManager
import com.prototipo.alertacusco.location.LocationManager
import com.prototipo.alertacusco.model.Reporte
import com.prototipo.alertacusco.ui.theme.AlertaCard
import com.prototipo.alertacusco.ui.theme.AlertaPrimaryButton
import com.prototipo.alertacusco.ui.theme.AlertaSecondaryButton
import com.prototipo.alertacusco.ui.theme.DarkBackground
import com.prototipo.alertacusco.ui.theme.LightBackground
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.foundation.clickable
import androidx.compose.material3.DropdownMenu


@Composable
fun ReportScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val firestoreManager = FirestoreManager()
    val geocodingManager = GeocodingManager(context)
    val locationManager = LocationManager(context)
    val isDark = isSystemInDarkTheme()

    val usuarioId =
        FirebaseAuth.getInstance()
            .currentUser
            ?.uid ?: ""

    var tipoIncidente by remember {
        mutableStateOf("")
    }

    var descripcion by remember {
        mutableStateOf("")
    }

    var latitud by remember {
        mutableStateOf<Double?>(null)
    }

    var longitud by remember {
        mutableStateOf<Double?>(null)
    }

    var imagenUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    val tipos = listOf(
        "Robo",
        "Asalto",
        "Hurto",
        "Vandalismo",
        "Violencia",
        "Otro"
    )

    val launcherImagen =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->

            imagenUri = uri

            if (uri != null) {
                Toast.makeText(
                    context,
                    "Imagen seleccionada correctamente",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->

            if (isGranted) {
                locationManager.obtenerUbicacion(
                    onSuccess = { lat, lng ->

                        latitud = lat
                        longitud = lng

                        Toast.makeText(
                            context,
                            "Ubicación capturada correctamente",
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
            } else {
                Toast.makeText(
                    context,
                    "Permiso de ubicación denegado",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    val riesgoPreview =
        if (tipoIncidente.isNotBlank()) {
            ClasificadorRiesgo.clasificar(tipoIncidente)
        } else {
            ""
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
            text = "Reportar incidente",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Completa la información del incidente para generar una alerta preventiva.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(
                alpha = 0.70f
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        AlertaCard {

            Text(
                text = "Información del incidente",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(14.dp))

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {

                AlertaSecondaryButton(
                    text = if (tipoIncidente.isBlank()) {
                        "Selecciona el tipo de incidente"
                    } else {
                        tipoIncidente
                    }
                ) {
                    expanded = true
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {

                    tipos.forEach { tipo ->

                        DropdownMenuItem(
                            text = {
                                Text(tipo)
                            },
                            onClick = {
                                tipoIncidente = tipo
                                expanded = false
                            }
                        )
                    }
                }
            }

            if (riesgoPreview.isNotBlank()) {

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Nivel de riesgo estimado: ${obtenerIconoRiesgoReporte(riesgoPreview)} $riesgoPreview",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = descripcion,
                onValueChange = {
                    descripcion = it
                },
                label = {
                    Text("Descripción")
                },
                placeholder = {
                    Text("Describe qué ocurrió, dónde y cuándo.")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        AlertaCard {

            Text(
                text = "Ubicación GPS",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Captura la ubicación actual para ubicar el incidente en el mapa delictivo.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.70f
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (latitud != null && longitud != null) {

                Text(
                    text = "✅ Ubicación capturada",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text("Latitud: $latitud")
                Text("Longitud: $longitud")

            } else {

                Text(
                    text = "📍 Ubicación pendiente",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Aún no se ha capturado la ubicación del incidente.",
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.70f
                    )
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            AlertaSecondaryButton(
                text = "Capturar ubicación GPS"
            ) {

                val permiso =
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )

                if (permiso == PackageManager.PERMISSION_GRANTED) {

                    locationManager.obtenerUbicacion(
                        onSuccess = { lat, lng ->

                            latitud = lat
                            longitud = lng

                            Toast.makeText(
                                context,
                                "Ubicación capturada correctamente",
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
                text = "Evidencia fotográfica",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Puedes adjuntar una imagen como evidencia. Por ahora se registrará como evidencia local.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.70f
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (imagenUri != null) {

                Text(
                    text = "✅ Evidencia fotográfica adjunta",
                    style = MaterialTheme.typography.titleMedium
                )

            } else {

                Text(
                    text = "📷 Sin evidencia fotográfica",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            AlertaSecondaryButton(
                text = "Adjuntar fotografía"
            ) {
                launcherImagen.launch("image/*")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        AlertaPrimaryButton(
            text = "Enviar reporte"
        ) {

            if (
                tipoIncidente.isNotBlank() &&
                descripcion.isNotBlank() &&
                latitud != null &&
                longitud != null
            ) {

                val riesgo =
                    ClasificadorRiesgo.clasificar(
                        tipoIncidente
                    )

                val fechaActual =
                    SimpleDateFormat(
                        "dd/MM/yyyy",
                        Locale.getDefault()
                    ).format(Date())

                val horaActual =
                    SimpleDateFormat(
                        "HH:mm",
                        Locale.getDefault()
                    ).format(Date())

                val distrito =
                    geocodingManager.obtenerDistrito(
                        latitud!!,
                        longitud!!
                    )

                val reporte =
                    Reporte(
                        tipoIncidente = tipoIncidente,
                        descripcion = descripcion,
                        fecha = fechaActual,
                        hora = horaActual,
                        latitud = latitud!!,
                        longitud = longitud!!,
                        distrito = distrito,
                        nivelRiesgo = riesgo,
                        usuarioId = usuarioId,
                        evidenciaFotografica = imagenUri != null,
                        fotoUrl = "",
                        estado = "Pendiente"
                    )

                firestoreManager.guardarReporte(
                    reporte = reporte,
                    onSuccess = {

                        Toast.makeText(
                            context,
                            "Reporte enviado correctamente",
                            Toast.LENGTH_LONG
                        ).show()

                        tipoIncidente = ""
                        descripcion = ""
                        latitud = null
                        longitud = null
                        imagenUri = null
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
                    "Completa los campos y captura la ubicación GPS",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

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

fun obtenerIconoRiesgoReporte(
    riesgo: String
): String {
    return when (riesgo) {
        "Alto" -> "🔴"
        "Medio" -> "🟡"
        else -> "🟢"
    }
}