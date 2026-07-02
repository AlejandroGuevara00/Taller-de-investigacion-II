package com.prototipo.alertacusco.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.prototipo.alertacusco.alerts.ZonaRiesgoManager
import com.prototipo.alertacusco.firebase.FirestoreManager
import com.prototipo.alertacusco.model.ZonaRiesgo
import com.prototipo.alertacusco.ui.theme.AlertaCard
import com.prototipo.alertacusco.ui.theme.AlertaGreen
import com.prototipo.alertacusco.ui.theme.AlertaOrange
import com.prototipo.alertacusco.ui.theme.AlertaRed
import com.prototipo.alertacusco.ui.theme.DarkBackground
import com.prototipo.alertacusco.ui.theme.LightBackground

@Composable
fun ZonaRiesgoScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val firestoreManager = FirestoreManager()
    val isDark = isSystemInDarkTheme()

    var zonas by remember {
        mutableStateOf<List<ZonaRiesgo>>(emptyList())
    }

    LaunchedEffect(Unit) {
        firestoreManager.obtenerReportes(
            onSuccess = { reportes ->
                zonas =
                    ZonaRiesgoManager
                        .analizar(reportes)
                        .sortedByDescending {
                            it.cantidadReportes
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
    }

    val zonaCritica =
        zonas.firstOrNull {
            it.nivelRiesgo == "Alto"
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
            text = "Zonas de riesgo",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Análisis automático de distritos según la cantidad de reportes registrados.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(
                alpha = 0.70f
            )
        )

        Spacer(modifier = Modifier.height(18.dp))

        zonaCritica?.let { zona ->

            AlertaCard {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(
                                AlertaRed.copy(
                                    alpha = 0.15f
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("⚠️")
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = "Zona crítica detectada",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(
                            text = zona.distrito,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(
                                alpha = 0.72f
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Reportes registrados: ${zona.cantidadReportes}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "Nivel de riesgo: ${zona.nivelRiesgo}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Se recomienda priorizar acciones preventivas y monitoreo ciudadano en esta zona.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.72f
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        if (zonas.isEmpty()) {

            AlertaCard {
                Text(
                    text = "Sin zonas registradas",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Aún no hay reportes suficientes para analizar zonas de riesgo.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.70f
                    )
                )
            }

        } else {

            Text(
                text = "Zonas analizadas: ${zonas.size}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {

                items(zonas) { zona ->

                    TarjetaZonaRiesgo(
                        zona = zona
                    )

                    Spacer(modifier = Modifier.height(12.dp))
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
fun TarjetaZonaRiesgo(
    zona: ZonaRiesgo
) {
    val color =
        when (zona.nivelRiesgo) {
            "Alto" -> AlertaRed
            "Medio" -> AlertaOrange
            else -> AlertaGreen
        }

    val icono =
        when (zona.nivelRiesgo) {
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
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        color.copy(
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
                    text = zona.distrito.ifBlank {
                        "Distrito no identificado"
                    },
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Nivel de riesgo: ${zona.nivelRiesgo}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.72f
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Cantidad de reportes: ${zona.cantidadReportes}",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = obtenerDescripcionZonaRiesgo(
                zona.nivelRiesgo
            ),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(
                alpha = 0.72f
            )
        )
    }
}

fun obtenerDescripcionZonaRiesgo(
    nivelRiesgo: String
): String {
    return when (nivelRiesgo) {
        "Alto" -> "Zona prioritaria para intervención preventiva y mayor vigilancia."
        "Medio" -> "Zona que requiere seguimiento y monitoreo frecuente."
        else -> "Zona con baja concentración de reportes."
    }
}