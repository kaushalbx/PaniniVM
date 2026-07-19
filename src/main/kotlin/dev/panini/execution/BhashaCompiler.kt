package dev.panini.execution

import dev.panini.derivation.Lakara
import dev.panini.dhatupatha.DhatuPatha

/** Output contract expected from a future Sanskrit sentence analyzer. */
data class VakyaAnalysis(
    val speaker: String,
    val listener: String,
    val sourceText: String,
    val prayojana: VakyaPrayojana,
    val polarity: Polarity = Polarity.POSITIVE,
    val lakara: Lakara? = null,
    val kriyas: List<KriyaAnalysis>,
    val dependencies: Set<ActionDependency> = kriyas.zipWithNext { before, after ->
        ActionDependency(before.id, after.id)
    }.toSet(),
)

data class KriyaAnalysis(
    val id: String,
    /** Stable Dhātupāṭha identity; surface spelling is not sufficient. */
    val dhatuId: String,
    val karakas: Map<Karaka, ExecutionExpression>,
    val selectedOperation: String? = null,
    val metadata: Map<String, String> = emptyMap(),
)

sealed interface UktiCompilation {
    data class Compiled(val ukti: Ukti, val trace: List<String>) : UktiCompilation
    data class Invalid(val message: String) : UktiCompilation
}

/** Converts analyzed language into the stable semantic input of the runtime. */
object BhashaCompiler {
    fun compile(analysis: VakyaAnalysis): UktiCompilation {
        if (analysis.kriyas.isEmpty()) {
            return UktiCompilation.Invalid("No verbal action was identified in the sentence analysis.")
        }
        if (analysis.kriyas.map { it.id }.distinct().size != analysis.kriyas.size) {
            return UktiCompilation.Invalid("Every analyzed verbal occurrence requires a unique id.")
        }

        val invocations = analysis.kriyas.map { kriya ->
            val dhatu = DhatuPatha.find(kriya.dhatuId)
                ?: return UktiCompilation.Invalid("Unknown Dhātupāṭha identity: ${kriya.dhatuId}")
            DhatuInvocation(
                id = kriya.id,
                dhatu = dhatu,
                bindings = kriya.karakas,
                selectedOperation = kriya.selectedOperation,
                metadata = kriya.metadata,
            )
        }
        return UktiCompilation.Compiled(
            Ukti(
                speaker = analysis.speaker,
                listener = analysis.listener,
                text = analysis.sourceText,
                prayojana = analysis.prayojana,
                polarity = analysis.polarity,
                lakara = analysis.lakara,
                invocations = invocations,
                dependencies = analysis.dependencies,
            ),
            listOf(
                "Compiled ${invocations.size} verbal occurrence(s).",
                "Preserved purpose ${analysis.prayojana} and polarity ${analysis.polarity}.",
            ),
        )
    }
}
