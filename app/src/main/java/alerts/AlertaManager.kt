package com.prototipo.alertacusco.alerts

import com.prototipo.alertacusco.model.Alerta
import com.prototipo.alertacusco.model.Reporte

object AlertaManager {

    fun generarAlerta(
        reporte: Reporte
    ): Alerta {

        return when (reporte.nivelRiesgo) {

            "Alto" -> Alerta(
                titulo = "🔴 Alerta Roja",
                mensaje = "Incidente de alto riesgo: ${reporte.tipoIncidente}"
            )

            "Medio" -> Alerta(
                titulo = "🟡 Alerta Amarilla",
                mensaje = "Incidente de riesgo medio: ${reporte.tipoIncidente}"
            )

            else -> Alerta(
                titulo = "🟢 Alerta Verde",
                mensaje = "Incidente de bajo riesgo: ${reporte.tipoIncidente}"
            )
        }
    }
}