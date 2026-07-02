package com.prototipo.alertacusco.screens

import android.widget.Toast
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
import androidx.navigation.NavController
import com.prototipo.alertacusco.ai.EstadoReporteManager
import com.prototipo.alertacusco.ai.RecomendacionManager
import com.prototipo.alertacusco.firebase.FirestoreManager
import com.prototipo.alertacusco.model.Reporte
import com.prototipo.alertacusco.ui.theme.AlertaCard
import com.prototipo.alertacusco.ui.theme.AlertaGreen
import com.prototipo.alertacusco.ui.theme.AlertaOrange
import com.prototipo.alertacusco.ui.theme.AlertaRed
import com.prototipo.alertacusco.ui.theme.DarkBackground
import com.prototipo.alertacusco.ui.theme.LightBackground

@Composable
fun DetalleReporteScreen(
    navController: NavController,
    reporteId: String
) {
    val context = LocalContext.current
    val firestoreManager = FirestoreManager()
    val isDark = isSystemInDarkTheme()

    var reporte by remember {
        mutableStateOf<Reporte?>(null)
    }

    LaunchedEffect(reporteId) {
        firestoreManager.obtenerReportePorId(
            reporteId = reporteId,
            onSuccess = {
                reporte = it
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
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        Text(
            text = "Detalle del reporte",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Información completa del incidente registrado.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(
                alpha = 0.70f
            )
        )

        Spacer(modifier = Modifier.height(18.dp))

        reporte?.let { item ->

            val colorRiesgo =
                when (item.nivelRiesgo) {
                    "Alto" -> AlertaRed
                    "Medio" -> AlertaOrange
                    else -> AlertaGreen
                }

            val iconoRiesgo =
                when (item.nivelRiesgo) {
                    "Alto" -> "🔴"
                    "Medio" -> "🟡"
                    else -> "🟢"
                }

            val iconoEstado =
                EstadoReporteManager.obtenerIconoEstado(
                    item.estado
                )

            val descripcionEstado =
                EstadoReporteManager.obtenerDescripcionEstado(
                    item.estado
                )

            val recomendacion =
                RecomendacionManager.generarRecomendacion(
                    tipoIncidente = item.tipoIncidente,
                    nivelRiesgo = item.nivelRiesgo
                )

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
                                colorRiesgo.copy(
                                    alpha = 0.14f
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = iconoRiesgo,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = item.tipoIncidente,
                            style = MaterialTheme.typography.titleLarge
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Nivel de riesgo: ${item.nivelRiesgo}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(
                                alpha = 0.72f
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                DatoDetalle(
                    titulo = "Descripción",
                    valor = item.descripcion
                )

                DatoDetalle(
                    titulo = "Distrito",
                    valor = item.distrito.ifBlank {
                        "Distrito no identificado"
                    }
                )

                DatoDetalle(
                    titulo = "Fecha y hora",
                    valor = "${item.fecha} | ${item.hora}"
                )

                DatoDetalle(
                    titulo = "Ubicación GPS",
                    valor = "Latitud: ${item.latitud}\nLongitud: ${item.longitud}"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            AlertaCard {

                Text(
                    text = "Estado del reporte",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "$iconoEstado ${item.estado}",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = descripcionEstado,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.72f
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            AlertaCard {

                Text(
                    text = "Evidencia fotográfica",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(10.dp))

                if (item.evidenciaFotografica) {

                    Text(
                        text = "✅ Evidencia registrada",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "La imagen fue adjuntada localmente. La visualización real estará disponible cuando se implemente Firebase Storage.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.72f
                        )
                    )

                } else {

                    Text(
                        text = "Sin evidencia fotográfica",
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
                    text = "Recomendación preventiva",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = recomendacion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.78f
                    )
                )
            }

        } ?: AlertaCard {

            Text(
                text = "Cargando reporte...",
                style = MaterialTheme.typography.titleMedium
            )
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

@Composable
fun DatoDetalle(
    titulo: String,
    valor: String
) {
    Spacer(modifier = Modifier.height(10.dp))

    Text(
        text = titulo,
        style = MaterialTheme.typography.titleMedium
    )

    Spacer(modifier = Modifier.height(4.dp))

    Text(
        text = valor,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface.copy(
            alpha = 0.72f
        )
    )
}