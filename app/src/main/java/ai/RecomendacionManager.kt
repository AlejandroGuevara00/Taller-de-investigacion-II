package com.prototipo.alertacusco.ai

object RecomendacionManager {

    fun generarRecomendacion(
        tipoIncidente: String,
        nivelRiesgo: String
    ): String {

        return when (tipoIncidente.lowercase()) {

            "robo" -> {
                when (nivelRiesgo) {
                    "Alto" -> "Evite transitar solo por esta zona, mantenga sus pertenencias aseguradas y comunique cualquier movimiento sospechoso a las autoridades."
                    "Medio" -> "Tenga cuidado con sus objetos personales y evite mostrar celulares, dinero u objetos de valor en espacios públicos."
                    else -> "Mantenga una actitud preventiva y esté atento a su entorno."
                }
            }

            "asalto" -> {
                when (nivelRiesgo) {
                    "Alto" -> "Evite permanecer en la zona durante horarios de baja concurrencia y procure movilizarse acompañado."
                    "Medio" -> "Transite por calles iluminadas y evite zonas con poca presencia de personas."
                    else -> "Manténgase alerta y priorice rutas seguras."
                }
            }

            "hurto" -> {
                when (nivelRiesgo) {
                    "Alto" -> "Extreme precauciones en lugares concurridos, mercados, paraderos y transporte público."
                    "Medio" -> "Revise constantemente sus pertenencias y evite llevar mochilas o bolsos abiertos."
                    else -> "Proteja sus objetos personales y manténgalos siempre a la vista."
                }
            }

            "violencia" -> {
                when (nivelRiesgo) {
                    "Alto" -> "Evite acercarse al lugar del incidente y comuníquese inmediatamente con las autoridades correspondientes."
                    "Medio" -> "Mantenga distancia de grupos conflictivos y busque apoyo en zonas seguras."
                    else -> "Reporte cualquier situación de agresión o alteración del orden."
                }
            }

            "vandalismo" -> {
                when (nivelRiesgo) {
                    "Alto" -> "Evite permanecer cerca de la zona afectada y reporte daños a la infraestructura pública o privada."
                    "Medio" -> "Manténgase atento a comportamientos sospechosos en espacios públicos."
                    else -> "Contribuya reportando daños o actos que afecten la seguridad del entorno."
                }
            }

            else -> {
                when (nivelRiesgo) {
                    "Alto" -> "Se recomienda evitar la zona temporalmente y mantenerse informado sobre nuevas alertas."
                    "Medio" -> "Transite con precaución y manténgase atento a su entorno."
                    else -> "Mantenga medidas básicas de prevención y seguridad ciudadana."
                }
            }
        }
    }
}