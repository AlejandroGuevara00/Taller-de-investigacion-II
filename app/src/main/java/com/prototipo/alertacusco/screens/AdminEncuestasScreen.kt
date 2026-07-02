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
import com.prototipo.alertacusco.firebase.AdminManager
import com.prototipo.alertacusco.firebase.FirestoreManager
import com.prototipo.alertacusco.model.EncuestaRespuesta
import com.prototipo.alertacusco.ui.theme.AlertaCard
import com.prototipo.alertacusco.ui.theme.AlertaMetricRow
import com.prototipo.alertacusco.ui.theme.AlertaRed
import com.prototipo.alertacusco.ui.theme.DarkBackground
import com.prototipo.alertacusco.ui.theme.LightBackground
import java.util.Locale

@Composable
fun AdminEncuestasScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val firestoreManager = FirestoreManager()
    val isDark = isSystemInDarkTheme()

    val esAdmin =
        AdminManager.esAdministrador()

    var encuestas by remember {
        mutableStateOf<List<EncuestaRespuesta>>(emptyList())
    }

    LaunchedEffect(Unit) {

        if (esAdmin) {
            firestoreManager.obtenerEncuestas(
                onSuccess = {
                    encuestas = it
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
    }

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
                text = "Solo el administrador puede visualizar los resultados de las encuestas.",
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

    val promedioGeneral =
        if (encuestas.isNotEmpty()) {
            encuestas.map {
                it.promedio
            }.average()
        } else {
            0.0
        }

    val promedioAF =
        promedioDimensionEncuesta(
            encuestas
        ) {
            it.adecuacionFuncional
        }

    val promedioFI =
        promedioDimensionEncuesta(
            encuestas
        ) {
            it.fiabilidad
        }

    val promedioUS =
        promedioDimensionEncuesta(
            encuestas
        ) {
            it.usabilidad
        }

    val promedioSE =
        promedioDimensionEncuesta(
            encuestas
        ) {
            it.seguridad
        }

    val promedioED =
        promedioDimensionEncuesta(
            encuestas
        ) {
            it.eficienciaDesempeno
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
            text = "Resultados de encuestas",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Solo el administrador puede ver las respuestas enviadas por los usuarios.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(
                alpha = 0.70f
            )
        )

        Spacer(modifier = Modifier.height(18.dp))

        AlertaCard {

            Text(
                text = "Resumen general",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(14.dp))

            AlertaMetricRow(
                titulo = "Encuestas recibidas",
                valor = encuestas.size.toString(),
                icono = "📝"
            )

            Spacer(modifier = Modifier.height(12.dp))

            AlertaMetricRow(
                titulo = "Promedio general",
                valor = formatearEncuesta(promedioGeneral),
                icono = "📊"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        AlertaCard {

            Text(
                text = "Promedios por dimensión",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(12.dp))

            TextoPromedioEncuesta(
                titulo = "Adecuación funcional",
                valor = promedioAF
            )

            TextoPromedioEncuesta(
                titulo = "Fiabilidad",
                valor = promedioFI
            )

            TextoPromedioEncuesta(
                titulo = "Usabilidad",
                valor = promedioUS
            )

            TextoPromedioEncuesta(
                titulo = "Seguridad",
                valor = promedioSE
            )

            TextoPromedioEncuesta(
                titulo = "Eficiencia del desempeño",
                valor = promedioED
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (encuestas.isEmpty()) {

            AlertaCard {
                Text(
                    text = "Aún no hay encuestas enviadas",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Cuando los usuarios completen la encuesta, los resultados aparecerán aquí.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.70f
                    )
                )
            }

        } else {

            Text(
                text = "Detalle de respuestas",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {

                items(encuestas) { encuesta ->

                    TarjetaResultadoEncuesta(
                        encuesta = encuesta
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
fun TextoPromedioEncuesta(
    titulo: String,
    valor: Double
) {
    Text(
        text = "$titulo: ${formatearEncuesta(valor)}",
        style = MaterialTheme.typography.bodyMedium
    )

    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun TarjetaResultadoEncuesta(
    encuesta: EncuestaRespuesta
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
                        AlertaRed.copy(
                            alpha = 0.14f
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("📋")
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = encuesta.correoUsuario,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${encuesta.fecha} | ${encuesta.hora}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.72f
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Total: ${encuesta.total}",
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = "Promedio: ${formatearEncuesta(encuesta.promedio)}",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Adecuación funcional: ${formatearEncuesta(encuesta.adecuacionFuncional)}",
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = "Fiabilidad: ${formatearEncuesta(encuesta.fiabilidad)}",
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = "Usabilidad: ${formatearEncuesta(encuesta.usabilidad)}",
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = "Seguridad: ${formatearEncuesta(encuesta.seguridad)}",
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = "Eficiencia: ${formatearEncuesta(encuesta.eficienciaDesempeno)}",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Respuestas: AF1=${encuesta.af1}, AF2=${encuesta.af2}, FI1=${encuesta.fi1}, FI2=${encuesta.fi2}, US1=${encuesta.us1}, US2=${encuesta.us2}, SE1=${encuesta.se1}, SE2=${encuesta.se2}, ED1=${encuesta.ed1}, ED2=${encuesta.ed2}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(
                alpha = 0.72f
            )
        )
    }
}

fun promedioDimensionEncuesta(
    encuestas: List<EncuestaRespuesta>,
    selector: (EncuestaRespuesta) -> Double
): Double {
    return if (encuestas.isNotEmpty()) {
        encuestas.map(selector).average()
    } else {
        0.0
    }
}

fun formatearEncuesta(
    valor: Double
): String {
    return String.format(
        Locale.US,
        "%.2f",
        valor
    )
}