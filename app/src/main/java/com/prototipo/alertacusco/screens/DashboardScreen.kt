package com.prototipo.alertacusco.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.prototipo.alertacusco.firebase.FirestoreManager
import com.prototipo.alertacusco.model.Reporte
import com.prototipo.alertacusco.ui.theme.AlertaCard
import com.prototipo.alertacusco.ui.theme.AlertaGreen
import com.prototipo.alertacusco.ui.theme.AlertaOrange
import com.prototipo.alertacusco.ui.theme.AlertaRed
import com.prototipo.alertacusco.ui.theme.DarkBackground
import com.prototipo.alertacusco.ui.theme.LightBackground

@Composable
fun DashboardScreen(
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

    val totalReportes = reportes.size

    val riesgoAlto =
        reportes.count {
            it.nivelRiesgo == "Alto"
        }

    val riesgoMedio =
        reportes.count {
            it.nivelRiesgo == "Medio"
        }

    val riesgoBajo =
        reportes.count {
            it.nivelRiesgo == "Bajo"
        }

    val pendientes =
        reportes.count {
            it.estado == "Pendiente"
        }

    val enRevision =
        reportes.count {
            it.estado == "En revisión"
        }

    val atendidos =
        reportes.count {
            it.estado == "Atendido"
        }

    val descartados =
        reportes.count {
            it.estado == "Descartado"
        }

    val incidenteMasFrecuente =
        reportes
            .groupBy {
                it.tipoIncidente
            }
            .maxByOrNull {
                it.value.size
            }
            ?.key ?: "Sin datos"

    val distritoMasReportado =
        reportes
            .filter {
                it.distrito.isNotBlank()
            }
            .groupBy {
                it.distrito
            }
            .maxByOrNull {
                it.value.size
            }
            ?.key ?: "Sin datos"

    val porcentajeAtencion =
        if (totalReportes > 0) {
            atendidos.toFloat() / totalReportes.toFloat()
        } else {
            0f
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
            text = "Dashboard estadístico",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Resumen analítico de reportes, riesgos y estado de atención.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(
                alpha = 0.70f
            )
        )

        Spacer(modifier = Modifier.height(18.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            TarjetaMetricaDashboard(
                titulo = "Total",
                valor = totalReportes.toString(),
                icono = "📊",
                modifier = Modifier.weight(1f)
            )

            TarjetaMetricaDashboard(
                titulo = "Atendidos",
                valor = atendidos.toString(),
                icono = "✅",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            TarjetaMetricaDashboard(
                titulo = "Pendientes",
                valor = pendientes.toString(),
                icono = "⏳",
                modifier = Modifier.weight(1f)
            )

            TarjetaMetricaDashboard(
                titulo = "En revisión",
                valor = enRevision.toString(),
                icono = "🔎",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        AlertaCard {

            Text(
                text = "Porcentaje de atención",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${(porcentajeAtencion * 100).toInt()}%",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = porcentajeAtencion,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = AlertaGreen,
                trackColor = MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.12f
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Reportes atendidos respecto al total registrado.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.72f
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        AlertaCard {

            Text(
                text = "Reportes por nivel de riesgo",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(12.dp))

            BarraDashboard(
                titulo = "Riesgo alto",
                valor = riesgoAlto,
                total = totalReportes,
                color = AlertaRed
            )

            BarraDashboard(
                titulo = "Riesgo medio",
                valor = riesgoMedio,
                total = totalReportes,
                color = AlertaOrange
            )

            BarraDashboard(
                titulo = "Riesgo bajo",
                valor = riesgoBajo,
                total = totalReportes,
                color = AlertaGreen
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        AlertaCard {

            Text(
                text = "Estado de atención",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(12.dp))

            BarraDashboard(
                titulo = "Pendientes",
                valor = pendientes,
                total = totalReportes,
                color = AlertaOrange
            )

            BarraDashboard(
                titulo = "En revisión",
                valor = enRevision,
                total = totalReportes,
                color = MaterialTheme.colorScheme.primary
            )

            BarraDashboard(
                titulo = "Atendidos",
                valor = atendidos,
                total = totalReportes,
                color = AlertaGreen
            )

            BarraDashboard(
                titulo = "Descartados",
                valor = descartados,
                total = totalReportes,
                color = AlertaRed
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        AlertaCard {

            Text(
                text = "Patrones detectados",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(12.dp))

            DatoDashboard(
                titulo = "Incidente más frecuente",
                valor = incidenteMasFrecuente
            )

            Spacer(modifier = Modifier.height(10.dp))

            DatoDashboard(
                titulo = "Distrito con más reportes",
                valor = distritoMasReportado
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
fun TarjetaMetricaDashboard(
    titulo: String,
    valor: String,
    icono: String,
    modifier: Modifier = Modifier
) {
    AlertaCard(
        modifier = modifier
    ) {

        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(
                    MaterialTheme.colorScheme.primary.copy(
                        alpha = 0.14f
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(icono)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = valor,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = titulo,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(
                alpha = 0.72f
            )
        )
    }
}

@Composable
fun BarraDashboard(
    titulo: String,
    valor: Int,
    total: Int,
    color: androidx.compose.ui.graphics.Color
) {
    val progreso =
        if (total > 0) {
            valor.toFloat() / total.toFloat()
        } else {
            0f
        }

    Text(
        text = "$titulo: $valor",
        style = MaterialTheme.typography.bodyMedium
    )

    Spacer(modifier = Modifier.height(6.dp))

    LinearProgressIndicator(
        progress = progreso,
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp),
        color = color,
        trackColor = MaterialTheme.colorScheme.onSurface.copy(
            alpha = 0.12f
        )
    )

    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
fun DatoDashboard(
    titulo: String,
    valor: String
) {
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