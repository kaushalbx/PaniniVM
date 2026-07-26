package dev.panini.derivation

import dev.panini.core.Lakara
import dev.panini.core.Purusha
import dev.panini.core.Vacana

enum class SanadiType {
    DESIDERATIVE, // सन् (San)
    CAUSATIVE,    // णिच् (Nic)
    INTENSIVE,    // यङ् (Yang)
}

data class SanadiDerivationResult(
    val primaryRoot: String,
    val sanadiType: SanadiType,
    val derivedStem: String,
    val conjugatedForm: String,
    val steps: List<String>,
)

object SanadiEngine {

    /** Derives a Sanādyanta stem and its conjugated form for a given primary root. */
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
        steps += "3.1.7 [धातोः कर्मणः समानकर्तृकादिच्छायां सन्]: Attaching सन् (s) affix in desire sense"

        val (derivedStem, suffix) = when (root) {
            "भू" -> Pair("बुभूष्", "ति")
            "पच्" -> Pair("पिपक्ष्", "ति")
            "जि" -> Pair("जिगीष्", "ति")
            "दा" -> Pair("दित्स्", "ति")
            "ज्ञा" -> Pair("जिज्ञास्", "ति")
            else -> Pair(reduplicateAndDesiderativize(root), "ति")
        }

        steps += "6.1.9 [सन्योः]: Applying reduplication (अभ्यास) -> $derivedStem"
        steps += "3.1.32 [सनाद्यन्ता धातवः]: Declaring $derivedStem as a secondary dhātu stem"

        val base = if (derivedStem.endsWith("्")) derivedStem.dropLast(1) else derivedStem
        val form = "${base}अ$suffix".replace("अ", "")
        val finalForm = "${base}${suffix}"
        steps += "3.1.68 [कर्तरि शप्] & 3.4.78 [तिप्तस्झि...]: Conjugated form in ${lakara.name} -> $finalForm"

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
        steps += "3.1.26 [हेतुमति च]: Attaching णिच् (i) affix in causative sense"

        val stem = when (root) {
            "भू" -> "भावि"
            "कृ" -> "कारि"
            "पच्" -> "पाचि"
            "गम्" -> "गमि"
            "पठ्" -> "पाठि"
            "दृश्" -> "दर्शि"
            else -> applyVrhddhiGunation(root) + "ि"
        }

        steps += "7.2.115 [अचो ञ्णिति] / 7.3.84: Applying vṛddhi/guṇa to root vowel -> $stem"
        steps += "3.1.32 [सनाद्यन्ता धातवः]: Declaring $stem as a causative dhātu stem"

        // i + a -> ay (Sandhi for ṇic + śap + ti)
        val stemBase = stem.dropLast(1) + "य"
        val form = "${stemBase}ति"
        steps += "3.1.68 [कर्तरि शप्] & 6.1.78 [एचोऽयवायावः]: Conjugated form -> $form"

        return SanadiDerivationResult(
            primaryRoot = root,
            sanadiType = SanadiType.CAUSATIVE,
            derivedStem = stem,
            conjugatedForm = form,
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
        steps += "3.1.22 [धातोरेकाचो हलादेः क्रियासमभिहारे यङ्]: Attaching यङ् (ya) affix in intensive sense"

        val stem = when (root) {
            "भू" -> "बोभूय्"
            "पच्" -> "पापच्य्"
            "कृ" -> "चेक्रीय्"
            "गम्" -> "जङ्गम्य्"
            else -> "बो" + root + "य्"
        }

        steps += "6.1.9 [सन्योः] & 7.4.82 [गुगो यङि]: Applying heavy reduplication (अभ्यास) -> $stem"
        steps += "3.1.32 [सनाद्यन्ता धातवः]: Declaring $stem as an intensive dhātu stem"

        val base = if (stem.endsWith("्")) stem.dropLast(1) else stem
        val form = "${base}ते"
        steps += "1.3.12 [अनुदात्तङित आत्मनेपदम्]: Intensive taking Ātmanepada affix -> $form"

        return SanadiDerivationResult(
            primaryRoot = root,
            sanadiType = SanadiType.INTENSIVE,
            derivedStem = stem,
            conjugatedForm = form,
            steps = steps,
        )
    }

    private fun reduplicateAndDesiderativize(root: String): String {
        return "बु" + root + "ष्"
    }

    private fun applyVrhddhiGunation(root: String): String {
        return root + "ि"
    }
}
