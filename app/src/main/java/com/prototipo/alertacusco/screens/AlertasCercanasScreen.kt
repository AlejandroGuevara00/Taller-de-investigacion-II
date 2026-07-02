package com.prototipo.alertacusco.screens

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.prototipo.alertacusco.firebase.FirestoreManager
import com.prototipo.alertacusco.location.DistanciaManager
import com.prototipo.alertacusco.location.LocationManager
import com.prototipo.alertacusco.model.ReporteCercano
import com.prototipo.alertacusco.ui.theme.AlertaCard
import com.prototipo.alertacusco.ui.theme.AlertaGreen
import com.prototipo.alertacusco.ui.theme.AlertaOrange
import com.prototipo.alertacusco.ui.theme.AlertaPrimaryButton
import com.prototipo.alertacusco.ui.theme.AlertaRed
import com.prototipo.alertacusco.ui.theme.DarkBackground
import com.prototipo.alertacusco.ui.theme.LightBackground

@Composable
fun AlertasCercanasScreen(
    navController: NavController
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val isDark = isSystemInDarkTheme()

    val locationManager = LocationManager(context)
    val firestoreManager = FirestoreManager()

    var reportesCercanos by remember {
        mutableStateOf<List<ReporteCercano>>(emptyList())
    }

    var cargando by remember {
        mutableStateOf(false)
    }

    fun buscarAlertasCercanas() {

        cargando = true

        locationManager.obtenerUbicacion(
            onSuccess = { latUsuario, lngUsuario ->

                firestoreManager.obtenerReportes(
                    onSuccess = { reportes ->

                        reportesCercanos =
                            reportes
                                .map { reporte ->

                                    val distancia =
                                        DistanciaManager.calcularDistanciaMetros(
                                            lat1 = latUsuario,
                                            lon1 = lngUsuario,
                                            lat2 = reporte.latitud,
                                            lon2 = reporte.longitud
                                        )

                                    ReporteCercano(
                                        reporte = reporte,
                                        distanciaMetros = distancia
                                    )
                                }
                                .filter {
                                    it.distanciaMetros <= 1000
                                }
                                .sortedBy {
                                    it.distanciaMetros
                                }

                        cargando = false
                    },
                    onFailure = { error ->

                        cargando = false

                        Toast.makeText(
                            context,
                            error,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                )
            },
            onFailure = { error ->

                cargando = false

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
                buscarAlertasCercanas()
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
            .padding(16.dp)
    ) {

        Text(
            text = "Alertas cercanas",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Analiza reportes registrados cerca de tu ubicación actual.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(
                alpha = 0.70f
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        AlertaCard {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(
                            MaterialTheme.colorScheme.primary.copy(
                                alpha = 0.14f
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("📍")
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "Radio de análisis",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = "1 km desde tu ubicación actual",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.70f
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            AlertaPrimaryButton(
                text = "Analizar mi zona actual"
            ) {
                val permiso =
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )

                if (permiso == PackageManager.PERMISSION_GRANTED) {
                    buscarAlertasCercanas()
                } else {
                    permissionLauncher.launch(
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (cargando) {

            AlertaCard {
                Text(
                    text = "Analizando reportes cercanos...",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "El sistema está calculando la distancia entre tu ubicación y los incidentes registrados.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.70f
                    )
                )
            }

        } else {

            if (reportesCercanos.isEmpty()) {

                AlertaCard {
                    Text(
                        text = "Sin alertas cercanas",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "No se detectaron reportes dentro del radio de 1 km.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.70f
                        )
                    )
                }

            } else {

                Text(
                    text = "Reportes detectados: ${reportesCercanos.size}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {

                    items(reportesCercanos) { item ->

                        TarjetaAlertaCercana(
                            item = item
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
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
    }
}

@Composable
fun TarjetaAlertaCercana(
    item: ReporteCercano
) {
    val colorRiesgo =
        when (item.reporte.nivelRiesgo) {
            "Alto" -> AlertaRed
            "Medio" -> AlertaOrange
            else -> AlertaGreen
        }

    val icono =
        when (item.reporte.nivelRiesgo) {
            "Alto" -> "🔴"
            "Medio" -> "🟡"
            else -> "🟢"
        }

    AlertaCard {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(
                        colorRiesgo.copy(
                            alpha = 0.14f
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(icono)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = item.reporte.tipoIncidente,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = item.reporte.distrito,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.72f
                    )
                )
            }

            Badge(
                containerColor = colorRiesgo
            ) {
                Text(
                    text = item.reporte.nivelRiesgo,
                    color = androidx.compose.ui.graphics.Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Distancia aproximada: ${item.distanciaMetros.toInt()} metros",
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = "Fecha: ${item.reporte.fecha} | Hora: ${item.reporte.hora}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(
                alpha = 0.70f
            )
        )
    }
}