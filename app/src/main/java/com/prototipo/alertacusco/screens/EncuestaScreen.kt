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
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.prototipo.alertacusco.firebase.FirestoreManager
import com.prototipo.alertacusco.model.EncuestaRespuesta
import com.prototipo.alertacusco.ui.theme.AlertaCard
import com.prototipo.alertacusco.ui.theme.AlertaGreen
import com.prototipo.alertacusco.ui.theme.AlertaPrimaryButton
import com.prototipo.alertacusco.ui.theme.AlertaSecondaryButton
import com.prototipo.alertacusco.ui.theme.DarkBackground
import com.prototipo.alertacusco.ui.theme.LightBackground
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class PreguntaEncuesta(
    val codigo: String,
    val texto: String
)

@Composable
fun EncuestaScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val firestoreManager = FirestoreManager()
    val isDark = isSystemInDarkTheme()

    val usuario =
        FirebaseAuth.getInstance()
            .currentUser

    val preguntas =
        remember {
            listOf(
                PreguntaEncuesta(
                    codigo = "AF1",
                    texto = "El sistema permite registrar incidentes ciudadanos de manera adecuada."
                ),
                PreguntaEncuesta(
                    codigo = "AF2",
                    texto = "El sistema permite capturar y utilizar la ubicación del reporte."
                ),
                PreguntaEncuesta(
                    codigo = "FI1",
                    texto = "El sistema registra la información de manera estable."
                ),
                PreguntaEncuesta(
                    codigo = "FI2",
                    texto = "El sistema permite consultar los reportes sin fallas frecuentes."
                ),
                PreguntaEncuesta(
                    codigo = "US1",
                    texto = "La aplicación móvil es fácil de utilizar."
                ),
                PreguntaEncuesta(
                    codigo = "US2",
                    texto = "La navegación entre las pantallas es clara y comprensible."
                ),
                PreguntaEncuesta(
                    codigo = "SE1",
                    texto = "El sistema protege el acceso mediante autenticación de usuario."
                ),
                PreguntaEncuesta(
                    codigo = "SE2",
                    texto = "El sistema diferencia correctamente los roles de usuario y administrador."
                ),
                PreguntaEncuesta(
                    codigo = "ED1",
                    texto = "El sistema responde rápidamente al registrar incidentes."
                ),
                PreguntaEncuesta(
                    codigo = "ED2",
                    texto = "El sistema permite consultar alertas, mapa y reportes de forma rápida."
                )
            )
        }

    val respuestas =
        remember {
            mutableStateMapOf<String, Int>()
        }

    fun obtenerRespuesta(codigo: String): Int {
        return respuestas[codigo] ?: 0
    }

    fun promedioDosValores(
        valor1: Int,
        valor2: Int
    ): Double {
        return (valor1 + valor2) / 2.0
    }

    fun enviarEncuesta() {

        val faltanRespuestas =
            preguntas.any {
                obtenerRespuesta(it.codigo) == 0
            }

        if (faltanRespuestas) {
            Toast.makeText(
                context,
                "Responde todas las preguntas antes de enviar",
                Toast.LENGTH_LONG
            ).show()

            return
        }

        val af1 = obtenerRespuesta("AF1")
        val af2 = obtenerRespuesta("AF2")

        val fi1 = obtenerRespuesta("FI1")
        val fi2 = obtenerRespuesta("FI2")

        val us1 = obtenerRespuesta("US1")
        val us2 = obtenerRespuesta("US2")

        val se1 = obtenerRespuesta("SE1")
        val se2 = obtenerRespuesta("SE2")

        val ed1 = obtenerRespuesta("ED1")
        val ed2 = obtenerRespuesta("ED2")

        val total =
            af1 + af2 +
                    fi1 + fi2 +
                    us1 + us2 +
                    se1 + se2 +
                    ed1 + ed2

        val promedio =
            total / 10.0

        val fecha =
            SimpleDateFormat(
                "dd/MM/yyyy",
                Locale.getDefault()
            ).format(Date())

        val hora =
            SimpleDateFormat(
                "HH:mm",
                Locale.getDefault()
            ).format(Date())

        val respuesta =
            EncuestaRespuesta(
                usuarioId = usuario?.uid ?: "",
                correoUsuario = usuario?.email ?: "Usuario no identificado",
                fecha = fecha,
                hora = hora,

                af1 = af1,
                af2 = af2,

                fi1 = fi1,
                fi2 = fi2,

                us1 = us1,
                us2 = us2,

                se1 = se1,
                se2 = se2,

                ed1 = ed1,
                ed2 = ed2,

                adecuacionFuncional = promedioDosValores(af1, af2),
                fiabilidad = promedioDosValores(fi1, fi2),
                usabilidad = promedioDosValores(us1, us2),
                seguridad = promedioDosValores(se1, se2),
                eficienciaDesempeno = promedioDosValores(ed1, ed2),

                total = total,
                promedio = promedio
            )

        firestoreManager.guardarEncuestaRespuesta(
            respuesta = respuesta,
            onSuccess = {
                Toast.makeText(
                    context,
                    "Encuesta enviada correctamente",
                    Toast.LENGTH_LONG
                ).show()

                respuestas.clear()
                navController.popBackStack()
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
            text = "Encuesta del sistema",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Responde la encuesta sobre el uso del prototipo. Tus respuestas serán enviadas al administrador.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(
                alpha = 0.70f
            )
        )

        Spacer(modifier = Modifier.height(18.dp))

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
                            AlertaGreen.copy(
                                alpha = 0.16f
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("📝")
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "Escala de valoración",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "1 = Muy bajo, 2 = Bajo, 3 = Regular, 4 = Bueno, 5 = Muy bueno",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.72f
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        preguntas.forEachIndexed { index, pregunta ->

            TarjetaPreguntaEncuesta(
                numero = index + 1,
                pregunta = pregunta,
                respuestas = respuestas
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))

        AlertaPrimaryButton(
            text = "Enviar encuesta"
        ) {
            enviarEncuesta()
        }

        Spacer(modifier = Modifier.height(10.dp))

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
fun TarjetaPreguntaEncuesta(
    numero: Int,
    pregunta: PreguntaEncuesta,
    respuestas: SnapshotStateMap<String, Int>
) {
    val valorSeleccionado =
        respuestas[pregunta.codigo] ?: 0

    AlertaCard {

        Text(
            text = "$numero. ${pregunta.texto}",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            for (valor in 1..5) {

                val seleccionado =
                    valorSeleccionado == valor

                if (seleccionado) {
                    AlertaPrimaryButton(
                        text = valor.toString(),
                        modifier = Modifier.weight(1f)
                    ) {
                        respuestas[pregunta.codigo] = valor
                    }
                } else {
                    AlertaSecondaryButton(
                        text = valor.toString(),
                        modifier = Modifier.weight(1f)
                    ) {
                        respuestas[pregunta.codigo] = valor
                    }
                }
            }
        }
    }
}