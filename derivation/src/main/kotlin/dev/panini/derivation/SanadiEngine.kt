package dev.panini.derivation

import dev.panini.core.Lakara
import dev.panini.core.PadaType
import dev.panini.core.Purusha
import dev.panini.core.TingAffix
import dev.panini.core.Vacana
import dev.panini.shiksha.Varnamala

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

    /** Derives a Sanādyanta stem and its conjugated form for a given primary root dynamically. */
    fun derive(
        root: String,
        type: SanadiType,
        lakara: Lakara = Lakara.LAT,
        purusha: Purusha = Purusha.PRATHAMA,
        vacana: Vacana = Vacana.EKAVACANA,
    ): SanadiDerivationResult {
        val steps = mutableListOf<String>()
        steps += "Primary verbal root: $root"

        return when (type) {
            SanadiType.DESIDERATIVE -> deriveDesiderative(root, lakara, purusha, vacana, steps)
            SanadiType.CAUSATIVE -> deriveCausative(root, lakara, purusha, vacana, steps)
            SanadiType.INTENSIVE -> deriveIntensive(root, lakara, purusha, vacana, steps)
        }
    }

    private fun deriveDesiderative(
        root: String,
        lakara: Lakara,
        purusha: Purusha,
        vacana: Vacana,
        steps: MutableList<String>,
    ): SanadiDerivationResult {
        val derivation = tingantaEngine.derive(
            TingantaDerivationRequest(root, vacana, purusha, lakara, pada = PadaType.PARASMAIPADA, sanadiPratyayas = listOf("सन्")),
        )
        steps.addAll(derivation.applications.map { "${it.sutra}: ${it.explanation}" })
        val derivedStem = tracedSanadiStem(derivation, "सन्")
        val finalForm = derivation.final.surface

        return SanadiDerivationResult(
            primaryRoot = root,
            sanadiType = SanadiType.DESIDERATIVE,
            derivedStem = derivedStem,
            conjugatedForm = finalForm,
            steps = steps,
        )
    }

    private fun deriveCausative(
        root: String,
        lakara: Lakara,
        purusha: Purusha,
        vacana: Vacana,
        steps: MutableList<String>,
    ): SanadiDerivationResult {
        val req = TingantaDerivationRequest(
            dhatu = root,
            vacana = vacana,
            purusha = purusha,
            lakara = lakara,
            pada = PadaType.PARASMAIPADA,
            sanadiPratyayas = listOf("णिच्"),
        )
        val derivationResult = tingantaEngine.deriveExplicitSanadi(req)
        steps.addAll(derivationResult.applications.map { "${it.sutra}: ${it.explanation}" })
        val stem = tracedConjugationalStem(derivationResult)
        val finalForm = derivationResult.final.surface

        return SanadiDerivationResult(
            primaryRoot = root,
            sanadiType = SanadiType.CAUSATIVE,
            derivedStem = stem,
            conjugatedForm = finalForm,
            steps = steps,
        )
    }

    private fun deriveIntensive(
        root: String,
        lakara: Lakara,
        purusha: Purusha,
        vacana: Vacana,
        steps: MutableList<String>,
    ): SanadiDerivationResult {
        val derivation = tingantaEngine.derive(
            TingantaDerivationRequest(root, vacana, purusha, lakara, pada = PadaType.ATMANEPADA, sanadiPratyayas = listOf("यङ्")),
        )
        steps.addAll(derivation.applications.map { "${it.sutra}: ${it.explanation}" })
        val stem = tracedSanadiStem(derivation, "यङ्")
        val finalForm = derivation.final.surface

        return SanadiDerivationResult(
            primaryRoot = root,
            sanadiType = SanadiType.INTENSIVE,
            derivedStem = stem,
            conjugatedForm = finalForm,
            steps = steps,
        )
    }

    private fun tracedSanadiStem(
        derivation: DerivationResult,
        pratyaya: String,
    ): String {
        val states = listOf(derivation.final) + derivation.applications.asReversed().map { it.after }
        val state = states.firstOrNull { candidate ->
            candidate.terms.any {
                it.kind == TermKind.PRATYAYA && it.upadesha == pratyaya
            }
        } ?: error("The derivation trace has no processed $pratyaya term.")
        val sanadiIndex = state.terms.indexOfFirst {
            it.kind == TermKind.PRATYAYA && it.upadesha == pratyaya
        }
        require(sanadiIndex >= 0) { "The completed trace has no surviving $pratyaya term." }
        val surface = state.copy(terms = state.terms.take(sanadiIndex + 1)).surface
        return if (surface.lastOrNull()?.let(Varnamala::isConsonant) == true) "$surface्" else surface
    }

    private fun tracedConjugationalStem(derivation: DerivationResult): String {
        val endingIndex = derivation.final.terms.indexOfLast { term ->
            TingAffix.entries.any { it.upadesha == term.upadesha || it.upadesha == term.sthaniProps?.upadesha }
        }
        require(endingIndex > 0) { "The completed causative trace has no tiṅ boundary." }
        val surface = derivation.final.copy(terms = derivation.final.terms.take(endingIndex)).surface
        return if (surface.lastOrNull()?.let(Varnamala::isConsonant) == true) "$surface्" else surface
    }

}
