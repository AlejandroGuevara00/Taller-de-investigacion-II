package com.prototipo.alertacusco.alerts

import com.prototipo.alertacusco.model.Reporte
import com.prototipo.alertacusco.model.ZonaRiesgo

object ZonaRiesgoManager {

    fun analizar(
        reportes: List<Reporte>
    ): List<ZonaRiesgo> {

        return reportes
            .groupBy { it.distrito }
            .map { entry ->

                val cantidad =
                    entry.value.size

                val riesgo =
                    when {

                        cantidad >= 6 ->
                            "Alto"

                        cantidad >= 3 ->
                            "Medio"

                        else ->
                            "Bajo"
                    }

                ZonaRiesgo(
                    distrito = entry.key,
                    cantidadReportes = cantidad,
                    nivelRiesgo = riesgo
                )
            }
    }
}