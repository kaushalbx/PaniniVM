package dev.panini.derivation

import dev.panini.core.Lakara
import dev.panini.core.PadaType
import dev.panini.core.Purusha
import dev.panini.core.Vacana

enum class SanadiType(val pratyaya: String) {
    DESIDERATIVE("सन्"),
    CAUSATIVE("णिच्"),
    INTENSIVE("यङ्"),
}

data class SanadiDerivationResult(
    val primaryRoot: String,
    val sanadiType: SanadiType,
    val derivedStem: String,
    val conjugatedForm: String,
    val steps: List<String>,
)

object SanadiEngine {

    private val tingantaEngine = TingantaEngine()

    /** Derives a Sanādyanta stem and its conjugated form for a given primary root 100% dynamically via Sūtras. */
    fun derive(
        root: String,
        type: SanadiType,
        lakara: Lakara = Lakara.LAT,
        purusha: Purusha = Purusha.PRATHAMA,
        vacana: Vacana = Vacana.EKAVACANA,
    ): SanadiDerivationResult {
        val targetPada = if (type == SanadiType.INTENSIVE) PadaType.ATMANEPADA else PadaType.PARASMAIPADA
        val req = TingantaDerivationRequest(
            dhatu = root,
            vacana = vacana,
            purusha = purusha,
            lakara = lakara,
            pada = targetPada,
            sanadiPratyayas = listOf(type.pratyaya),
        )

        val derivationResult = tingantaEngine.derive(req)
        val steps = derivationResult.applications.map { "${it.sutra}: ${it.explanation}" }

        val derivedStem = derivationResult.final.terms.firstOrNull { it.kind == TermKind.DHATU }?.surface ?: root
        val conjugatedForm = derivationResult.final.terms.last().surface

        return SanadiDerivationResult(
            primaryRoot = root,
            sanadiType = type,
            derivedStem = derivedStem,
            conjugatedForm = conjugatedForm,
            steps = steps,
        )
    }
}
