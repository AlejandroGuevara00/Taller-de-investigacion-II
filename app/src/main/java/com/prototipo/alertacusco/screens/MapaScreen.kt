package com.prototipo.alertacusco.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.prototipo.alertacusco.firebase.FirestoreManager
import com.prototipo.alertacusco.model.Reporte
import com.prototipo.alertacusco.ui.theme.AlertaCard
import com.prototipo.alertacusco.ui.theme.AlertaGreen
import com.prototipo.alertacusco.ui.theme.AlertaOrange
import com.prototipo.alertacusco.ui.theme.AlertaPrimaryButton
import com.prototipo.alertacusco.ui.theme.AlertaRed
import com.prototipo.alertacusco.ui.theme.DarkBackground
import com.prototipo.alertacusco.ui.theme.LightBackground
import com.google.android.gms.maps.model.MapStyleOptions
import com.prototipo.alertacusco.R

@Composable
fun MapaScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val firestoreManager = FirestoreManager()
    val isDark = isSystemInDarkTheme()

    var reportes by remember {
        mutableStateOf<List<Reporte>>(emptyList())
    }

    var reporteSeleccionado by remember {
        mutableStateOf<Reporte?>(null)
    }

    LaunchedEffect(Unit) {
        firestoreManager.obtenerReportes(
            onSuccess = {
                reportes = it
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

    val cusco =
        LatLng(
            -13.53195,
            -71.96746
        )

    val cameraPositionState =
        rememberCameraPositionState {
            position =
                CameraPosition.fromLatLngZoom(
                    cusco,
                    13f
                )
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
    ) {

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                mapType = MapType.NORMAL,
                isMyLocationEnabled = false,
                mapStyleOptions =
                    if (isDark) {
                        MapStyleOptions.loadRawResourceStyle(
                            context,
                            R.raw.map_style_dark
                        )
                    } else {
                        null
                    }
            )
        ) {

            reportes.forEach { reporte ->

                if (
                    reporte.latitud != 0.0 &&
                    reporte.longitud != 0.0
                ) {

                    val colorMarcador =
                        when (reporte.nivelRiesgo) {
                            "Alto" -> BitmapDescriptorFactory.HUE_RED
                            "Medio" -> BitmapDescriptorFactory.HUE_YELLOW
                            else -> BitmapDescriptorFactory.HUE_GREEN
                        }

                    Marker(
                        state = MarkerState(
                            position = LatLng(
                                reporte.latitud,
                                reporte.longitud
                            )
                        ),
                        title = reporte.tipoIncidente,
                        snippet = "Distrito: ${reporte.distrito} | Riesgo: ${reporte.nivelRiesgo}",
                        icon = BitmapDescriptorFactory.defaultMarker(
                            colorMarcador
                        ),
                        onClick = {

                            reporteSeleccionado = reporte

                            true
                        }
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

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
                        Text("🗺️")
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = "Mapa delictivo",
                            style = MaterialTheme.typography.titleLarge
                        )

                        Text(
                            text = "Reportes ciudadanos geolocalizados",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(
                                alpha = 0.72f
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    LeyendaRiesgoMapa(
                        texto = "Alto",
                        color = AlertaRed,
                        modifier = Modifier.weight(1f)
                    )

                    LeyendaRiesgoMapa(
                        texto = "Medio",
                        color = AlertaOrange,
                        modifier = Modifier.weight(1f)
                    )

                    LeyendaRiesgoMapa(
                        texto = "Bajo",
                        color = AlertaGreen,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        reporteSeleccionado?.let { reporte ->

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                AlertaCard {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(
                                    obtenerColorRiesgoMapa(
                                        reporte.nivelRiesgo
                                    ).copy(
                                        alpha = 0.14f
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = obtenerIconoRiesgoMapa(
                                    reporte.nivelRiesgo
                                )
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(
                                text = reporte.tipoIncidente,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                text = reporte.distrito.ifBlank {
                                    "Distrito no identificado"
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(
                                    alpha = 0.72f
                                )
                            )
                        }

                        Text(
                            text = reporte.nivelRiesgo,
                            style = MaterialTheme.typography.labelLarge,
                            color = obtenerColorRiesgoMapa(
                                reporte.nivelRiesgo
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Fecha: ${reporte.fecha} | Hora: ${reporte.hora}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = "Estado: ${reporte.estado}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.72f
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    AlertaPrimaryButton(
                        text = "Ver detalle"
                    ) {
                        navController.navigate(
                            "detalle_reporte/${reporte.id}"
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    TextButton(
                        onClick = {
                            reporteSeleccionado = null
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cerrar")
                    }
                }
            }
        }
    }
}

@Composable
fun LeyendaRiesgoMapa(
    texto: String,
    color: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = texto,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

fun obtenerColorRiesgoMapa(
    riesgo: String
): androidx.compose.ui.graphics.Color {
    return when (riesgo) {
        "Alto" -> AlertaRed
        "Medio" -> AlertaOrange
        else -> AlertaGreen
    }
}

fun obtenerIconoRiesgoMapa(
    riesgo: String
): String {
    return when (riesgo) {
        "Alto" -> "🔴"
        "Medio" -> "🟡"
        else -> "🟢"
    }
}