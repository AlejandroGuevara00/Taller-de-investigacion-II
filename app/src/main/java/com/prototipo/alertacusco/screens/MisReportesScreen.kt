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
import com.google.firebase.auth.FirebaseAuth
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
fun MisReportesScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val firestoreManager = FirestoreManager()
    val isDark = isSystemInDarkTheme()

    val usuarioId =
        FirebaseAuth.getInstance()
            .currentUser
            ?.uid ?: ""

    var reportes by remember {
        mutableStateOf<List<Reporte>>(emptyList())
    }

    LaunchedEffect(Unit) {
        firestoreManager.obtenerReportesPorUsuario(
            usuarioId = usuarioId,
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
            text = "Mis reportes",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Consulta los incidentes que registraste y revisa el estado de atención.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(
                alpha = 0.70f
            )
        )

        Spacer(modifier = Modifier.height(18.dp))

        if (reportes.isEmpty()) {

            AlertaCard {
                Text(
                    text = "Aún no tienes reportes",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Cuando registres un incidente, aparecerá en esta sección.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.70f
                    )
                )
            }

        } else {

            Text(
                text = "Reportes registrados: ${reportes.size}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {

                items(reportes) { reporte ->

                    TarjetaMiReporte(
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
fun TarjetaMiReporte(
    reporte: Reporte,
    onClick: () -> Unit
) {
    val colorRiesgo =
        when (reporte.nivelRiesgo) {
            "Alto" -> AlertaRed
            "Medio" -> AlertaOrange
            else -> AlertaGreen
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
                Text(
                    text = when (reporte.nivelRiesgo) {
                        "Alto" -> "🔴"
                        "Medio" -> "🟡"
                        else -> "🟢"
                    }
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
    }
}