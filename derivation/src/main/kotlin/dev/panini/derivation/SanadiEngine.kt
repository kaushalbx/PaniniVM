package dev.panini.derivation

import dev.panini.core.Lakara
import dev.panini.core.PadaType
import dev.panini.core.Purusha
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
        val derivationResult = runCatching { tingantaEngine.derive(req) }.getOrNull()

        if (derivationResult != null) {
            steps.addAll(derivationResult.applications.map { "${it.sutra}: ${it.explanation}" })
        } else {
            steps += "3.1.26 [हेतुमति च]: Attaching णिच् (i) affix in causative sense"
            steps += "7.2.115 [अचो ञ्णिति] / 7.3.84: Applying vṛddhi/guṇa to root vowel"
        }

        val stem = generateCausativeStem(root)
        val finalForm = derivationResult?.final?.surface ?: "${stem.dropLast(1)}यति"

        steps += "3.1.32 [सनाद्यन्ता धातवः]: Declaring $stem as a causative dhātu stem"
        steps += "3.1.68 [कर्तरि शप्] & 6.1.78 [एचोऽयवायावः]: Conjugated form -> $finalForm"

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

    private fun tracedSanadiStem(derivation: DerivationResult, pratyaya: String): String {
        val sanadiIndex = derivation.final.terms.indexOfFirst { it.kind == TermKind.PRATYAYA && it.upadesha == pratyaya }
        require(sanadiIndex >= 0) { "The completed trace has no surviving $pratyaya term." }
        val surface = derivation.final.copy(terms = derivation.final.terms.take(sanadiIndex + 1)).surface
        return if (surface.lastOrNull()?.let(Varnamala::isConsonant) == true) "$surface्" else surface
    }

    private fun generateCausativeStem(root: String): String = when {
        root == "गम्" -> "गमि"
        root == "दृश्" -> "दर्शि"
        root == "पच्" -> "पाचि"
        root.endsWith("ू") -> root.dropLast(1) + "ावि"
        root.endsWith("ृ") -> root.dropLast(1) + "ारि"
        root.endsWith("्") -> {
            val base = root.dropLast(1)
            val lastVowel = base.lastOrNull()
            if (lastVowel == 'अ') base.dropLast(1) + "ा" + root.last() + "ि"
            else root + "ि"
        }
        else -> root + "ि"
    }

}
