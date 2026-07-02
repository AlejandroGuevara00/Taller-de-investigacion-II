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
import com.prototipo.alertacusco.firebase.AdminManager
import com.prototipo.alertacusco.firebase.FirestoreManager
import com.prototipo.alertacusco.model.Reporte
import com.prototipo.alertacusco.ui.theme.AlertaCard
import com.prototipo.alertacusco.ui.theme.AlertaGreen
import com.prototipo.alertacusco.ui.theme.AlertaOrange
import com.prototipo.alertacusco.ui.theme.AlertaPrimaryButton
import com.prototipo.alertacusco.ui.theme.AlertaRed
import com.prototipo.alertacusco.ui.theme.AlertaSecondaryButton
import com.prototipo.alertacusco.ui.theme.DarkBackground
import com.prototipo.alertacusco.ui.theme.LightBackground

@Composable
fun AdminReportesScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val firestoreManager = FirestoreManager()
    val isDark = isSystemInDarkTheme()

    val esAdmin = AdminManager.esAdministrador()

    if (!esAdmin) {

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
                text = "Acceso denegado",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "No tienes permisos para acceder al Panel Administrador.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(
                    alpha = 0.70f
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver")
            }
        }

        return
    }

    var reportes by remember {
        mutableStateOf<List<Reporte>>(emptyList())
    }

    var filtroEstado by remember {
        mutableStateOf("Todos")
    }

    var filtroRiesgo by remember {
        mutableStateOf("Todos")
    }

    val reportesFiltrados =
        reportes.filter { reporte ->

            val cumpleEstado =
                filtroEstado == "Todos" ||
                        reporte.estado == filtroEstado

            val cumpleRiesgo =
                filtroRiesgo == "Todos" ||
                        reporte.nivelRiesgo == filtroRiesgo

            cumpleEstado && cumpleRiesgo
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
            text = "Panel administrador",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Gestión, revisión y seguimiento de reportes ciudadanos.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(
                alpha = 0.70f
            )
        )

        Spacer(modifier = Modifier.height(18.dp))

        AlertaCard {

            Text(
                text = "Filtros de gestión",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Estado del reporte",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                BotonFiltroAdmin(
                    texto = "Todos",
                    seleccionado = filtroEstado == "Todos",
                    modifier = Modifier.weight(1f)
                ) {
                    filtroEstado = "Todos"
                }

                BotonFiltroAdmin(
                    texto = "Pendiente",
                    seleccionado = filtroEstado == "Pendiente",
                    modifier = Modifier.weight(1f)
                ) {
                    filtroEstado = "Pendiente"
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                BotonFiltroAdmin(
                    texto = "En revisión",
                    seleccionado = filtroEstado == "En revisión",
                    modifier = Modifier.weight(1f)
                ) {
                    filtroEstado = "En revisión"
                }

                BotonFiltroAdmin(
                    texto = "Atendido",
                    seleccionado = filtroEstado == "Atendido",
                    modifier = Modifier.weight(1f)
                ) {
                    filtroEstado = "Atendido"
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            BotonFiltroAdmin(
                texto = "Descartado",
                seleccionado = filtroEstado == "Descartado"
            ) {
                filtroEstado = "Descartado"
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Nivel de riesgo",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                BotonFiltroAdmin(
                    texto = "Todos",
                    seleccionado = filtroRiesgo == "Todos",
                    modifier = Modifier.weight(1f)
                ) {
                    filtroRiesgo = "Todos"
                }

                BotonFiltroAdmin(
                    texto = "Alto",
                    seleccionado = filtroRiesgo == "Alto",
                    modifier = Modifier.weight(1f)
                ) {
                    filtroRiesgo = "Alto"
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                BotonFiltroAdmin(
                    texto = "Medio",
                    seleccionado = filtroRiesgo == "Medio",
                    modifier = Modifier.weight(1f)
                ) {
                    filtroRiesgo = "Medio"
                }

                BotonFiltroAdmin(
                    texto = "Bajo",
                    seleccionado = filtroRiesgo == "Bajo",
                    modifier = Modifier.weight(1f)
                ) {
                    filtroRiesgo = "Bajo"
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Reportes encontrados: ${reportesFiltrados.size}",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (reportesFiltrados.isEmpty()) {

            AlertaCard {
                Text(
                    text = "No hay reportes con estos filtros",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Cambia los filtros para visualizar otros reportes.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.70f
                    )
                )
            }

        } else {

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {

                items(reportesFiltrados) { reporte ->

                    TarjetaAdminReporte(
                        reporte = reporte,
                        onClick = {
                            navController.navigate(
                                "admin_detalle_reporte/${reporte.id}"
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
fun BotonFiltroAdmin(
    texto: String,
    seleccionado: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    if (seleccionado) {
        AlertaPrimaryButton(
            text = texto,
            modifier = modifier
        ) {
            onClick()
        }
    } else {
        AlertaSecondaryButton(
            text = texto,
            modifier = modifier
        ) {
            onClick()
        }
    }
}

@Composable
fun TarjetaAdminReporte(
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