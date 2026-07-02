package com.prototipo.alertacusco.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun AlertaScreenContainer(
    content: @Composable ColumnScope.() -> Unit
) {
    val isDark = isSystemInDarkTheme()

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
            .padding(16.dp),
        content = content
    )
}

@Composable
fun AlertaCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val isDark = isSystemInDarkTheme()

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor =
                if (isDark) {
                    DarkCard
                } else {
                    LightCard
                },
            contentColor =
                if (isDark) {
                    DarkTextPrimary
                } else {
                    LightTextPrimary
                }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

@Composable
fun AlertaPrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White
        ),
        contentPadding = PaddingValues(
            vertical = 15.dp
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun AlertaSecondaryButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.primary
        ),
        contentPadding = PaddingValues(
            vertical = 15.dp
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun AlertaDangerButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = AlertaRed,
            contentColor = Color.White
        ),
        contentPadding = PaddingValues(
            vertical = 15.dp
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun AlertaScreenHeader(
    titulo: String,
    descripcion: String,
    icono: String? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (icono != null) {

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(
                        MaterialTheme.colorScheme.primary.copy(
                            alpha = 0.14f
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = icono,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Spacer(modifier = Modifier.size(12.dp))
        }

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = titulo,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = descripcion,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(
                    alpha = 0.70f
                )
            )
        }
    }
}

@Composable
fun AlertaEmptyState(
    icono: String,
    titulo: String,
    descripcion: String
) {
    AlertaCard {

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                    .background(
                        MaterialTheme.colorScheme.primary.copy(
                            alpha = 0.12f
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = icono,
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = descripcion,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.72f
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun AlertaChip(
    texto: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50.dp))
            .background(
                color.copy(
                    alpha = 0.14f
                )
            )
            .padding(
                horizontal = 12.dp,
                vertical = 6.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = texto,
            color = color,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun RiesgoChip(
    riesgo: String,
    modifier: Modifier = Modifier
) {
    val color =
        when (riesgo) {
            "Alto" -> AlertaRed
            "Medio" -> AlertaOrange
            else -> AlertaGreen
        }

    val icono =
        when (riesgo) {
            "Alto" -> "🔴"
            "Medio" -> "🟡"
            else -> "🟢"
        }

    AlertaChip(
        texto = "$icono $riesgo",
        color = color,
        modifier = modifier
    )
}

@Composable
fun EstadoChip(
    estado: String,
    modifier: Modifier = Modifier
) {
    val color =
        when (estado) {
            "Pendiente" -> AlertaOrange
            "En revisión" -> MaterialTheme.colorScheme.primary
            "Atendido" -> AlertaGreen
            "Descartado" -> AlertaRed
            else -> MaterialTheme.colorScheme.primary
        }

    val icono =
        when (estado) {
            "Pendiente" -> "⏳"
            "En revisión" -> "🔎"
            "Atendido" -> "✅"
            "Descartado" -> "❌"
            else -> "⏳"
        }

    AlertaChip(
        texto = "$icono $estado",
        color = color,
        modifier = modifier
    )
}

@Composable
fun AlertaMetricRow(
    titulo: String,
    valor: String,
    icono: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(
                        MaterialTheme.colorScheme.primary.copy(
                            alpha = 0.12f
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(icono)
            }

            Spacer(modifier = Modifier.size(10.dp))

            Text(
                text = titulo,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Text(
            text = valor,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}