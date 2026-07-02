package com.prototipo.alertacusco.ai

object ClasificadorRiesgo {

    fun clasificar(
        tipoIncidente: String
    ): String {

        return when (tipoIncidente.lowercase()) {

            "robo" -> "Alto"

            "asalto" -> "Alto"

            "violencia" -> "Alto"

            "hurto" -> "Medio"

            "vandalismo" -> "Bajo"

            "otro" -> "Bajo"

            else -> "Bajo"
        }
    }
}