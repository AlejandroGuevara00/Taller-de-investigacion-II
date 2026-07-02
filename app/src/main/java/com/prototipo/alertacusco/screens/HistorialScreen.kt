package com.prototipo.alertacusco.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.prototipo.alertacusco.ai.EstadoReporteManager
import com.prototipo.alertacusco.firebase.FirestoreManager
import com.prototipo.alertacusco.model.Reporte
import com.prototipo.alertacusco.ui.theme.AlertaCard
import com.prototipo.alertacusco.ui.theme.AlertaGreen
import com.prototipo.alertacusco.ui.theme.AlertaOrange
import com.prototipo.alertacusco.ui.theme.AlertaRed
import com.prototipo.alertacusco.ui.theme.DarkBackground
import com.prototipo.alertacusco.ui.theme.LightBackground

@Composable
fun HistorialScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val firestoreManager = FirestoreManager()
    val isDark = isSystemInDarkTheme()

    var reportes by remember {
        mutableStateOf<List<Reporte>>(emptyList())
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
            text = "Historial general",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Consulta todos los incidentes registrados por la comunidad.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(
                alpha = 0.70f
            )
        )

        Spacer(modifier = Modifier.height(18.dp))

        if (reportes.isEmpty()) {

            AlertaCard {
                Text(
                    text = "No hay reportes registrados",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Cuando los ciudadanos registren incidentes, aparecerán en esta sección.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.70f
                    )
                )
            }

        } else {

            Text(
                text = "Total de reportes: ${reportes.size}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {

                items(reportes) { reporte ->

                    TarjetaHistorialReporte(
                        reporte = reporte,
                        onClick = {
                            navController.navigate(
                                "detalle_reporte/${reporte.id}"
                            )
                        }
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
fun TarjetaHistorialReporte(
    reporte: Reporte,
    onClick: () -> Unit
) {
    val colorRiesgo =
        when (reporte.nivelRiesgo) {
            "Alto" -> AlertaRed
            "Medio" -> AlertaOrange
            else -> AlertaGreen
        }

    val iconoRiesgo =
        when (reporte.nivelRiesgo) {
            "Alto" -> "🔴"
            "Medio" -> "🟡"
            else -> "🟢"
        }

    val iconoEstado =
        EstadoReporteManager.obtenerIconoEstado(
            reporte.estado
        )

    AlertaCard(
        modifier = Modifier.clickable {
            onClick()
        }
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        colorRiesgo.copy(
                            alpha = 0.14f
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(iconoRiesgo)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = reporte.tipoIncidente,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

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
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Riesgo: ${reporte.nivelRiesgo}",
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = "Estado: $iconoEstado ${reporte.estado}",
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = "Fecha: ${reporte.fecha} | Hora: ${reporte.hora}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(
                alpha = 0.70f
            )
        )

        if (reporte.evidenciaFotografica) {
            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "📷 Evidencia fotográfica registrada",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.75f
                )
            )
        }
    }
}